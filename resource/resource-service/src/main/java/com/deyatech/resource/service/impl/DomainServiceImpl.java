package com.deyatech.resource.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.common.base.BaseServiceImpl;
import com.deyatech.common.enums.EnableEnum;
import com.deyatech.common.exception.BusinessException;
import com.deyatech.resource.entity.Domain;
import com.deyatech.resource.entity.StationGroup;
import com.deyatech.resource.mapper.DomainMapper;
import com.deyatech.resource.service.DomainService;
import com.deyatech.resource.service.StationGroupService;
import com.deyatech.resource.util.StringHelper;
import com.deyatech.resource.vo.DomainVo;
import com.deyatech.station.config.SiteProperties;
import com.deyatech.station.feign.StationFeign;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @Author lee.
 * @since 2019-08-01
 */
@Slf4j
@Service
public class DomainServiceImpl extends BaseServiceImpl<DomainMapper, Domain> implements DomainService {
    public static final String NGINX_DISABLED_SUFFIX = ".disable";
    public static final String NGINX_ENABLE_SUFFIX = ".enable";

    @Autowired
    StationGroupService stationGroupService;

    @Autowired
    StationFeign stationFeign;

    /**
     * 单个将对象转换为vo
     *
     * @param domain
     * @return
     */
    @Override
    public DomainVo setVoProperties(Domain domain){
        DomainVo domainVo = new DomainVo();
        BeanUtil.copyProperties(domain, domainVo);
        return domainVo;
    }

    /**
     * 批量将对象转换为vo
     *
     * @param domains
     * @return
     */
    @Override
    public List<DomainVo> setVoProperties(Collection domains){
        List<DomainVo> domainVos = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(domains)) {
            for (Object domain : domains) {
                DomainVo domainVo = new DomainVo();
                BeanUtil.copyProperties(domain, domainVo);
                domainVos.add(domainVo);
            }
        }
        return domainVos;
    }

    /**
     * 根据条件翻页查询域名
     *
     * @param domainVo
     * @return
     */
    @Override
    public IPage<DomainVo> pageSelectByDomainVo(DomainVo domainVo) {
        return baseMapper.pageSelectByDomainVo(getPageByBean(domainVo), domainVo);
    }

    /**
     * 根据条件查询所有域名
     *
     * @param domainVo
     * @return
     */
    @Override
    public Collection<DomainVo> listSelectByDomainVo(DomainVo domainVo) {
        return baseMapper.listSelectByDomainVo(domainVo);
    }

    /**
     * 统计域名件数
     *
     * @param id
     * @param name
     * @return
     */
    @Override
    public long countName(String id, String name) {
        return baseMapper.countName(id, name);
    }

    /**
     * 统计英文件数
     *
     * @param id
     * @param englishName
     * @return
     */
    @Override
    public long countEnglishName(String id, String englishName) {
        return baseMapper.countEnglishName(id, englishName);
    }

    /**
     * 根据编号检索域名
     *
     * @param id
     * @return
     */
    @Override
    public Domain getById(Serializable id) {
        return baseMapper.getDomainById(id);
    }

    /**
     * 启用停用域名
     *
     * @param id
     * @param flag
     * @return
     */
    @Override
    public long runOrStopDomainById(String id, String flag) {
        long count = 0;
        Domain domain = getById(id);
        // 启用
        if ("run".equals(flag)) {
            count = baseMapper.updateEnableById(id, EnableEnum.ENABLE.getCode());
            if (count > 0) {
                this.enableNginxConf(domain.getName());
                this.reloadNginx();
            }

            // 停用
        } else if ("stop".equals(flag)) {
            count = baseMapper.updateEnableById(id, EnableEnum.DISABLE.getCode());
            if (count > 0) {
                this.disableNginxConf(domain.getName());
                this.reloadNginx();
            }
        }
        return count;
    }

    /**
     * 删除域名和配置
     *
     * @param idList
     * @param maps
     * @return
     */
    @Override
    public boolean removeDomainsAndConfig(Collection<String> idList, Map<String, Domain> maps) {
        if(CollectionUtil.isEmpty(idList)) return false;
        // 删除域名
        long count = baseMapper.updateEnableByIds(idList, EnableEnum.DELETED.getCode());
        if (count > 0 && CollectionUtil.isNotEmpty(idList)) {
            boolean reload = false;
            for (String id : idList) {
                Domain domain =  maps.get(id);
                if (Objects.nonNull(domain)) {
                    this.deleteNginxConf(domain.getName());
                }
            }
            if (reload) {
                this.reloadNginx();
            }
        }
        return count > 0 ? true : false;
    }

    /**
     * 添加或保存域名生成 nginx
     *
     * @param domain
     * @return
     */

    @Override
    public boolean saveOrUpdateAndNginx(Domain domain) {
        Domain oldDomain = null;
        boolean flag;
        if (StrUtil.isNotEmpty(domain.getId())) {
            oldDomain = this.getById(domain.getId());
            // 更新 enable
            flag = baseMapper.updateDomainById(domain) > 0 ? true : false;
        } else {
            flag = super.save(domain);
        }
        if (flag) {
            //生成 nginx 配置 和 站点目录
            this.createDomainNginxConfig(domain, oldDomain);
        }
        return flag;
    }

    /**
     * 每个域名创建一个 nginx 配置
     *
     * @param domain
     */
    private void createDomainNginxConfig(Domain domain, Domain oldDomain) {
        // 站点
        StationGroup stationGroup = stationGroupService.getById(domain.getStationGroupId());
        boolean isChange = false;
        if (Objects.nonNull(oldDomain)) {
            // 站点 或 域名变更
            if (!domain.getStationGroupId().equals(oldDomain.getStationGroupId()) ||
                !domain.getName().equals(oldDomain.getName())) {
                this.deleteNginxConf(oldDomain.getName());
                this.deleteNginxConf(domain.getName());
                isChange = true;
            }
        } else {
            isChange = true;
        }
        // 新建或有变更是
        if (isChange) {
            // 创建 nginx 配置文件
            this.createNginxConf(stationGroup, domain);
            this.reloadNginx();
        }
    }

    /**
     * 删除 nginx 配置文件
     *
     * @param domainName
     */
    private void deleteNginxConf(String domainName) {
        String nginxConfigDir = stationFeign.getSiteProperties().getData().getNginxConfigDir();
        File enable = new File(nginxConfigDir, domainName + NGINX_ENABLE_SUFFIX);
        if (enable != null && enable.exists()) {
            enable.delete();
        }
        File disabled = new File(nginxConfigDir, domainName + NGINX_DISABLED_SUFFIX);
        if (disabled != null && disabled.exists()) {
            disabled.delete();
        }
    }

    /**
     * 创建 nginx 配置文件
     */
    private void createNginxConf(StationGroup stationGroup, Domain domain) {
        SiteProperties site = stationFeign.getSiteProperties().getData();
        String hostRoot = site.getHostsRoot();
        hostRoot = hostRoot.replace("\\\\", "/");
        if (!hostRoot.endsWith("/")) {
            hostRoot += "/";
        }
        Map<String, Object> map = new HashMap<>();
        // 端口
        map.put("sitePort", domain.getPort());
        // 域名
        map.put("serverNames", domain.getName());
        // 站点目录
        map.put("siteRootDir", hostRoot + stationGroup.getEnglishName());
        // 代理
        map.put("proxyPass", site.getNginxProxyPass());
        // 站点ID
        map.put("siteId", stationGroup.getId());
        // 增加配置 nginx
        try {
            File siteNginxTemplateFile = ResourceUtils.getFile("classpath:nginx_site.template");
            String nginxTemplate = FileUtils.readFileToString(siteNginxTemplateFile, Charset.forName("UTF-8"));
            String nginxContent = StringHelper.processTemplate(nginxTemplate, map);
            // 配置文件
            File desc = new File(site.getNginxConfigDir(), domain.getName() + NGINX_ENABLE_SUFFIX);
            if (!desc.getParentFile().exists()) {
                desc.getParentFile().mkdirs();
            }
            FileUtils.writeStringToFile(desc, nginxContent, Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行 nginx 刷新配置指令
     */
    private void reloadNginx() {
        BufferedReader br = null;
        try {
            String osName = System.getProperty("os.name");
            File file = null;
            Process exec = null;
            if(osName.toLowerCase().startsWith("win")){
                file = ResourceUtils.getFile("classpath:reloadNginx.bat");
                if(ObjectUtil.isNotNull(file)){
                    exec = Runtime.getRuntime().exec(file.getPath());
                }
            }else{
                file = ResourceUtils.getFile("classpath:reloadNginx.sh");
                if(ObjectUtil.isNotNull(file)){
                    exec = Runtime.getRuntime().exec("sh ".concat(file.getPath()));
                }
            }
            br = new BufferedReader(new InputStreamReader(exec.getInputStream()));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            System.out.println(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 禁用站点配置文件
     *
     * @param domainName
     */
    private void disableNginxConf(String domainName) {
        String nginxConfigDir = stationFeign.getSiteProperties().getData().getNginxConfigDir();
        File enable = new File(nginxConfigDir, domainName + NGINX_ENABLE_SUFFIX);
        File disabled = new File(nginxConfigDir, domainName + NGINX_DISABLED_SUFFIX);
        if (Objects.nonNull(enable) && enable.exists()) {
            if (Objects.nonNull(disabled) && disabled.exists()) {
                disabled.delete();
            }
            if (enable.renameTo(disabled)) {
                this.reloadNginx();
            }
        } else {
            log.error("配置文件{}不存在，禁用站点操作失败", enable.getAbsolutePath());
        }
    }

    /**
     * 启用站点配置文件
     */
    private void enableNginxConf(String domainName) {
        String nginxConfigDir = stationFeign.getSiteProperties().getData().getNginxConfigDir();
        File enable = new File(nginxConfigDir, domainName + NGINX_ENABLE_SUFFIX);
        File disabled = new File(nginxConfigDir, domainName + NGINX_DISABLED_SUFFIX);
        if (Objects.nonNull(disabled) && disabled.exists()) {
            if (Objects.nonNull(enable) && enable.exists()) {
                enable.delete();
            }
            if (disabled.renameTo(enable)) {
                this.reloadNginx();
            }
        } else {
            log.error("配置文件{}不存在，启用站点操作失败", disabled.getAbsolutePath());
        }
    }


    /**
     * 启用停用 站点下所有域名 nginx 配置
     *
     * @param stationGroupId
     */
    @Override
    public void runOrStopStationGroupNginxConfig(String stationGroupId) {
        if (StrUtil.isEmpty(stationGroupId))
            return;
        // 检错站点下的所有域名
        Collection<DomainVo> list = baseMapper.selectDomainByStationGroupId(stationGroupId);;
        StationGroup stationGroup = stationGroupService.getById(stationGroupId);
        // 设置站点下所有域名的状态
        long count = baseMapper.updateEnableByStationGroupId(stationGroup.getId(), stationGroup.getEnable());
        if (count > 0 && CollectionUtil.isNotEmpty(list)) {
            for (DomainVo domain : list) {
                // 启用
                if (EnableEnum.ENABLE.getCode().equals(stationGroup.getEnable())) {
                    this.enableNginxConf(domain.getName());
                    // 停用
                } else if (EnableEnum.DISABLE.getCode().equals(stationGroup.getEnable())) {
                    this.disableNginxConf(domain.getName());
                }
            }
            this.reloadNginx();
        }
    }

    /**
     * 删除 站点下所有域名 nginx 配置
     *
     * @param ids
     * @param maps
     */
    @Override
    public void removeStationGroupNginxConfig(List<String> ids, Map<String, StationGroup> maps) {
        if (CollectionUtil.isEmpty(ids) || Objects.isNull(maps)) {
            return;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        for (String stationGroupId : ids) {
            // 站点目录标记删除
            StationGroup stationGroup = maps.get(stationGroupId);
            File src = new File(stationFeign.getSiteProperties().getData().getHostsRoot(), stationGroup.getEnglishName());
            File dest = new File(stationFeign.getSiteProperties().getData().getHostsRoot(), stationGroup.getEnglishName() + "_delete_" + format.format(new Date()));
            src.renameTo(dest);
            // 删除站点下的所有域名配置
            Collection<DomainVo> list = baseMapper.selectDomainByStationGroupId(stationGroupId);
            long count = baseMapper.updateEnableByStationGroupId(stationGroup.getId(), EnableEnum.DELETED.getCode());
            if (count > 0 && CollectionUtil.isNotEmpty(list)) {
                for (DomainVo domain : list) {
                    this.deleteNginxConf(domain.getName());
                }
            }
        }
        this.reloadNginx();
    }
}
