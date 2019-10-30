package com.deyatech.template.service;

import com.deyatech.template.entity.StationGit;
import com.deyatech.template.vo.StationGitVo;
import com.deyatech.common.base.BaseService;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 *  站点git模板地址信息 服务类
 * </p>
 *
 * @Author lee.
 * @since 2019-08-06
 */
public interface StationGitService extends BaseService<StationGit> {

    /**
     * 单个将对象转换为vo站点git模板地址信息
     *
     * @param stationGit
     * @return
     */
    StationGitVo setVoProperties(StationGit stationGit);

    /**
     * 批量将对象转换为vo站点git模板地址信息
     *
     * @param stationGits
     * @return
     */
    List<StationGitVo> setVoProperties(Collection stationGits);


    /**
     * 关联查询站点和站点git地址信息
     *
     * @return
     */
    List<StationGitVo> listByStationGroupAndStationGit();


    /**
     *  根据模板
     *
     * @return
     */
    String getTemplateFiles(String siteId,String path);

    /**
     *  根据模板
     *
     * @return
     */
    String getTemplateAllFiles(String siteId,String type);

    /**
     *  读取模板内容
     *
     * @return
     */
    String getFileContent(String path);


    /**
     *  根据站点ID查询站点模板git信息
     *
     * @return
     */
    StationGit getStationGitBySiteId(String siteId);

}
