package com.deyatech.station.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.deyatech.station.entity.Page;
import com.deyatech.station.vo.PageVo;
import com.deyatech.station.mapper.PageMapper;
import com.deyatech.station.service.PageService;
import com.deyatech.common.base.BaseServiceImpl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Collection;
import java.util.Map;

/**
 * <p>
 * 页面管理 服务实现类
 * </p>
 *
 * @Author csm.
 * @since 2019-08-01
 */
@Service
public class PageServiceImpl extends BaseServiceImpl<PageMapper, Page> implements PageService {

    /**
     * 单个将对象转换为vo页面管理
     *
     * @param page
     * @return
     */
    @Override
    public PageVo setVoProperties(Page page){
        PageVo pageVo = new PageVo();
        BeanUtil.copyProperties(page, pageVo);
        return pageVo;
    }

    /**
     * 批量将对象转换为vo页面管理
     *
     * @param pages
     * @return
     */
    @Override
    public List<PageVo> setVoProperties(Collection pages){
        List<PageVo> pageVos = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(pages)) {
            for (Object page : pages) {
                PageVo pageVo = new PageVo();
                BeanUtil.copyProperties(page, pageVo);
                pageVos.add(pageVo);
            }
        }
        return pageVos;
    }

    /**
     * 验证当前输入页面路径是否已经存在
     * @param page
     * @return
     */
    @Override
    public String existsPagePath(Page page) {
        String message;
        // 校验页面路径
        if (!this.validatePagePath(page)) {
            message = "页面路径错误";
            return message;
        }
        // TODO TemplateConstants.PAGE_NO_OPERATING_FOLDER
        if (page.getPagePath().startsWith("/template")) {
            message = "此目录不允许操作";
            return message;
        }
        if (StrUtil.isEmpty(page.getPageEnglishName())) {
            message = "英文名称为空";
            return message;
        }
        Page pageQuery = new Page();
        pageQuery.setPagePath(page.getPagePath());
        pageQuery.setSiteId(page.getSiteId());
        pageQuery.setPageEnglishName(page.getPageEnglishName());
        Page pageResult = super.getByBean(pageQuery);
        if (ObjectUtil.isNotNull(pageResult) && !pageResult.getId().equals(page.getId())) {
            message = "此地址已存在当前页面";
            return message;
        }
        return null;
    }

    private boolean validatePagePath(Page page) {
        String pagePath = page.getPagePath();
        String regExp = "^(\\/([\u4E00-\u9FA5]|\\w)+)*\\/$";
        return StrUtil.isNotEmpty(pagePath) && pagePath.matches(regExp);
    }

    @Override
    public boolean saveOrUpdate(Page entity) {
        // 校验页面路径
        if (!this.validatePagePath(entity)) {
            return false;
        }
        // 保存信息
        super.saveOrUpdate(entity);
        // 获取站点根目录 TODO
        String siteRoot = "";
        // 获取站点模板路径信息 TODO
        String templateRoot = "";

        // 根据站点id 查询setting_site_base_config配置
        // TODO
        Map map = CollectionUtil.newHashMap();
        map.put("site", "setting_site_base_config配置");
        map.put("siteId", entity.getSiteId());

        // TODO TemplateConstants.PAGE_SUFFIX
        String pagePath = siteRoot + entity.getPagePath() + entity.getPageEnglishName() + ".html";
        Map models = CollectionUtil.newHashMap();
        models.put("distFile", new File(pagePath));
        models.put("varMap", map);

        // 生成静态页面 TODO
        //thymeleafUtilApi.getThyToStaticFile(templateRoot.toString(), templatePath, models);

        return true;
    }
}
