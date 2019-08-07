package com.deyatech.resource.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.http.HttpStatus;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.common.base.BaseServiceImpl;
import com.deyatech.common.enums.EnableEnum;
import com.deyatech.common.enums.YesNoEnum;
import com.deyatech.common.exception.BusinessException;
import com.deyatech.resource.config.SiteProperties;
import com.deyatech.resource.entity.Domain;
import com.deyatech.resource.entity.StationGroup;
import com.deyatech.resource.mapper.DomainMapper;
import com.deyatech.resource.service.DomainService;
import com.deyatech.resource.service.StationGroupService;
import com.deyatech.resource.util.StringHelper;
import com.deyatech.resource.vo.DomainVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.*;
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
    public static final String DOMAIN_NAME_ADD = "add";
    public static final String DOMAIN_NAME_REMOVE = "remove";

    @Autowired
    StationGroupService stationGroupService;

    @Autowired
    SiteProperties siteProperties;

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
     * 根据站群编号统计域名件数
     *
     * @param id
     * @param stationGroupId
     * @param name
     * @return
     */
    @Override
    public long countNameByStationGroupId(String id, String stationGroupId, String name) {
        return baseMapper.countNameByStationGroupId(id, stationGroupId, name);
    }

    /**
     * 添加或保存
     * @param entity
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean saveUpdate (Domain entity) {
        DomainVo domainVo = getMainByByStationGroupId(entity.getStationGroupId());
        // 没有主域名时，把现在的设置为主域名
        if (Objects.isNull(domainVo)) {
            entity.setSign(YesNoEnum.YES.getCode().toString());
        }
        // 更新站群下其他域名为非主域名
        if (YesNoEnum.YES.getCode().toString().equals(entity.getSign())) {
            baseMapper.updateSignByStationGroupId(entity.getStationGroupId(), YesNoEnum.NO.getCode().toString());
        }
        return super.saveOrUpdate(entity);
    }

    /**
     * 添加或保存以及生成nginx
     *
     * @param entity
     * @return
     */

    @Override
    public boolean saveOrUpdateAndNginx(Domain entity) {
        boolean flag = saveUpdate(entity);
        if (flag) {
            //生成 nginx 配置 和 站点目录
            this.createOrUpdateNginxConfig(entity);
        }
        return flag;
    }

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
    public long runOrStopStationById(String id, String flag) {
        long count = 0;
        Domain domain = getById(id);
        StationGroup stationGroup = stationGroupService.getById(domain.getStationGroupId());
        if ("run".equals(flag)) {
            count = baseMapper.updateEnableById(id, EnableEnum.ENABLE.getCode());
            if (count > 0) {
                addOrRemoveDomainFromConfig(stationGroup.getEnglishName(), domain.getName(), DOMAIN_NAME_ADD);
                this.reloadNginx();
            }
        } else if ("stop".equals(flag)) {
            count = baseMapper.updateEnableById(id, EnableEnum.DISABLE.getCode());
            if (count > 0) {
                addOrRemoveDomainFromConfig(stationGroup.getEnglishName(), domain.getName(), DOMAIN_NAME_REMOVE);
                this.reloadNginx();
            }
        }
        return count;
    }

    /**
     * 更新主域名
     *
     * @param id
     * @param stationGroupId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public long updateSignByIdAndStationGroupId(String id, String stationGroupId) {
        // 更新站群下其他域名为非主域名
        baseMapper.updateSignByStationGroupId(stationGroupId, YesNoEnum.NO.getCode().toString());
        // 把当前域名更新成主域名
        return baseMapper.updateSignById(id, YesNoEnum.YES.getCode().toString());
    }

    /**
     * 获取站群下的主域名
     *
     * @param stationGroupId
     * @return
     */
    @Override
    public DomainVo getMainByByStationGroupId(String stationGroupId) {
        return baseMapper.getMainByByStationGroupId(stationGroupId);
    }

    /**
     * 如果传入的是主域名，则创建目录和 nginx 配置，否则仅将新域名增加到主域名对应的配置文件中
     *
     * @param domain
     */
    public void createOrUpdateNginxConfig(Domain domain) {
        // 站群
        StationGroup stationGroup = stationGroupService.getById(domain.getStationGroupId());
        String stationGroupEnglishName = stationGroup.getEnglishName();
        if (YesNoEnum.YES.getCode().toString().equals(domain.getSign())) { //如果是主域名
            this.createNginxPage(stationGroupEnglishName);
            this.createNginxConf(stationGroup);
        } else {
            addOrRemoveDomainFromConfig(stationGroupEnglishName, domain.getName(), DOMAIN_NAME_ADD);
        }
        this.reloadNginx();
    }

    /**
     * 从配置文件中添加或移除域名
     *
     * @param stationGroupEnglishName
     * @param domainName
     * @param flag
     */
    public void addOrRemoveDomainFromConfig(String stationGroupEnglishName, String domainName, String flag) {
        //查找到主域名的配置文件添加一个主机名
        File desc = new File(siteProperties.getNginxConfigDir(), stationGroupEnglishName + NGINX_ENABLE_SUFFIX);
        try {
            FileInputStream input = new FileInputStream(desc);
            List<String> lines = IOUtils.readLines(input, "utf-8");
            StringBuilder newLine = null;
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                if (line.contains("server_name")) {
                    if (DOMAIN_NAME_ADD.equals(flag) && !line.contains(domainName)) {
                        newLine = new StringBuilder(line);
                        int endIndex = line.indexOf(";");
                        if (endIndex > -1) {
                            newLine.insert(endIndex, " " + domainName);
                            lines.set(i, newLine.toString());
                        }
                    } else if (DOMAIN_NAME_REMOVE.equals(flag) && line.contains(domainName)) {
                        newLine = new StringBuilder(line);
                        int endIndex = line.indexOf(domainName);
                        if (endIndex > -1) {
                            newLine.delete(endIndex - 1, endIndex + domainName.length());
                            lines.set(i, newLine.toString());
                        }
                    }
                    break;
                }
            }
            if (newLine != null) { //说明有改动 server_name
                FileUtils.writeStringToFile(desc, StringUtils.join(lines, CharUtils.LF));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建 nginx 站点默认页面
     *
     * @param stationGroupEnglishName
     */
    private void createNginxPage(String stationGroupEnglishName) {
        String vhostRootDir = this.getRootDirBySiteEnglishName(stationGroupEnglishName);
        File vhostRootDirFile = new File(vhostRootDir);
        try {
            File dest = new File(vhostRootDirFile, "index.html");
            if (!dest.getParentFile().exists()) {
                boolean mkdirs = dest.getParentFile().mkdirs();
            }
            FileUtils.writeStringToFile(dest, "<!DOCTYPE html><html><head><meta charset=utf-8><link rel=stylesheet href=\"\"><title>新站点创建成功</title></head><body>" + stationGroupEnglishName + "新站点创建成功</body></html>");
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(HttpStatus.HTTP_INTERNAL_ERROR, String.format("站点 %s 创建失败", stationGroupEnglishName));
        }
    }

    /**
     * 创建 nginx 配置文件
     */
    private void createNginxConf(StationGroup stationGroup) {
        String stationGroupEnglishName = stationGroup.getEnglishName();
        DomainVo domainVo = new DomainVo();
        domainVo.setStationGroupId(stationGroup.getId());
        // 站群下的所有域名
        Collection<DomainVo> list = listSelectByDomainVo(domainVo);
        List<String> domainList = new ArrayList<>();
        if (list != null) {
            for (DomainVo vo : list) {
                // 启用状态
                if (vo.getEnable() == EnableEnum.ENABLE.getCode()) {
                    domainList.add(vo.getName());
                }
            }
        }

        String vhostRootDir = this.getRootDirBySiteEnglishName(stationGroupEnglishName);
        File vhostRootDirFile = new File(vhostRootDir);
        Map<String, Object> map = new HashMap<>();
        map.put("sitePort", siteProperties.getNginxPort());
        map.put("serverNames", StringUtils.join(domainList, " "));
        map.put("siteRootDir", vhostRootDirFile.getAbsolutePath());
        map.put("proxyPass", siteProperties.getNginxProxyPass());
        map.put("siteId", stationGroup.getId());
        //增加配置 nginx
        try {
            File siteNginxTemplateFile = ResourceUtils.getFile("classpath:nginx_site.template");
            String nginxTemplate = FileUtils.readFileToString(siteNginxTemplateFile);
            String nginxContent = StringHelper.processTemplate(nginxTemplate, map);
            File desc = new File(siteProperties.getNginxConfigDir(), stationGroupEnglishName + NGINX_ENABLE_SUFFIX);
            if (!desc.getParentFile().exists()) {
                desc.getParentFile().mkdirs();
            }
            FileUtils.writeStringToFile(desc, nginxContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除站点配置文件
     *
     * @param stationGroup
     */
    @Override
    public void deleteNginxConfig(StationGroup stationGroup) {
        File disabled = new File(siteProperties.getNginxConfigDir(), stationGroup.getEnglishName() + NGINX_DISABLED_SUFFIX);
        if (Objects.nonNull(disabled) && disabled.exists()) {
            disabled.delete();
            this.reloadNginx();
        } else {
            log.error("配置文件{}不存在，删除站点操作失败", disabled.getAbsolutePath());
        }

    }

    /**
     * 禁用站点配置文件
     *
     * @param stationGroup
     */
    @Override
    public void disableNginxConfig(StationGroup stationGroup) {
        File enable = new File(siteProperties.getNginxConfigDir(), stationGroup.getEnglishName() + NGINX_ENABLE_SUFFIX);
        File disabled = new File(siteProperties.getNginxConfigDir(), stationGroup.getEnglishName() + NGINX_DISABLED_SUFFIX);
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
    @Override
    public void enableNginxConfig(StationGroup stationGroup) {
        File disabled = new File(siteProperties.getNginxConfigDir(), stationGroup.getEnglishName() + NGINX_DISABLED_SUFFIX);
        File enable = new File(siteProperties.getNginxConfigDir(), stationGroup.getEnglishName() + NGINX_ENABLE_SUFFIX);
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
     * 执行 nginx 刷新配置指令
     */
    private void reloadNginx() {
        BufferedReader br = null;
        try {
            // TODO 本地测试
            Process exec = Runtime.getRuntime().exec("C:/env/nginx-1.16.0/nginx_reload.bat");
//            Process exec = Runtime.getRuntime().exec("nginx -s reload");
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
     * 根据站点返回站点主目录绝对路径
     *
     * @param stationGroupEnglishName
     * @return
     */
    public String getRootDirBySiteEnglishName(String stationGroupEnglishName) {
        return new File(siteProperties.getHostsRoot(), stationGroupEnglishName).getAbsolutePath();
    }

    /**
     * 根据站点返回站点的模板存放位置的绝对路径
     *
     * @param domain
     * @return
     */
    public String getRootTemplateDirByDomain(Domain domain) {
        StationGroup stationGroup = stationGroupService.getById(domain.getStationGroupId());
        return new File(new File(siteProperties.getHostsRoot(), stationGroup.getEnglishName()), "template/").getAbsolutePath();
    }

    /**
     * 删除域名和配置
     *
     * @param idList
     * @return
     */
    @Override
    public boolean removeDomainsAndConfig(Collection<String> idList, Map<String, Domain> maps) {
        boolean res = super.removeByIds(idList);
        if (res) {
            idList.stream().forEach(id -> {
                Domain domain = maps.get(id);
                StationGroup stationGroup = stationGroupService.getById(domain.getStationGroupId());
                // 移除配置文件的域名
                addOrRemoveDomainFromConfig(stationGroup.getEnglishName(), domain.getName(), DOMAIN_NAME_REMOVE);
                this.reloadNginx();
            });
        }
        return res;
    }

    /**
     * 修改状态根据站群编号
     *
     * @param stationGroupId
     * @param enable
     * @return
     */
    @Override
    public long updateEnableByStationGroupId(String stationGroupId, int enable) {
        return baseMapper.updateEnableByStationGroupId(stationGroupId, enable);
    }

    /**
     * 获取nginx端口
     *
     * @return
     */
    @Override
    public String getNginxPort() {
        return siteProperties.getNginxPort() == null ? "" : siteProperties.getNginxPort().toString();
    }
}
