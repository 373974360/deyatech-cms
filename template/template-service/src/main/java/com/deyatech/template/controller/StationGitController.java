package com.deyatech.template.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.deyatech.station.feign.StationFeign;
import com.deyatech.template.entity.StationGit;
import com.deyatech.template.utils.JGitUtil;
import com.deyatech.template.utils.ZipUtil;
import com.deyatech.template.vo.StationGitVo;
import com.deyatech.template.service.StationGitService;
import com.deyatech.common.entity.RestResult;
import io.swagger.annotations.ApiImplicitParams;
import lombok.extern.slf4j.Slf4j;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.Collection;
import java.util.List;
import org.springframework.web.bind.annotation.RestController;
import com.deyatech.common.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 站点git模板地址信息 前端控制器
 * </p>
 * @author: lee.
 * @since 2019-08-06
 */
@Slf4j
@RestController
@RequestMapping("/template/stationGit")
@Api(tags = {"站点git模板地址信息接口"})
public class StationGitController extends BaseController {


    @Autowired
    StationFeign stationFeign;

    @Autowired
    StationGitService stationGitService;

    /**
     * 单个保存或者更新站点git模板地址信息
     *
     * @param stationGit
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation(value="单个保存或者更新站点git模板地址信息", notes="根据站点git模板地址信息对象保存或者更新站点git模板地址信息信息")
    @ApiImplicitParam(name = "stationGit", value = "站点git模板地址信息对象", required = true, dataType = "StationGit", paramType = "query")
    public RestResult<Boolean> saveOrUpdate(StationGit stationGit) {
        log.info(String.format("保存或者更新站点git模板地址信息: %s ", JSONUtil.toJsonStr(stationGit)));
        boolean result = false;
        if(StringUtils.isEmpty(stationGit.getId())){
            result = stationGitService.save(stationGit);
        }
        return RestResult.ok(result);
    }

    /**
     * 批量保存或者更新站点git模板地址信息
     *
     * @param stationGitList
     * @return
     */
    @PostMapping("/saveOrUpdateBatch")
    @ApiOperation(value="批量保存或者更新站点git模板地址信息", notes="根据站点git模板地址信息对象集合批量保存或者更新站点git模板地址信息信息")
    @ApiImplicitParam(name = "stationGitList", value = "站点git模板地址信息对象集合", required = true, allowMultiple = true, dataType = "StationGit", paramType = "query")
    public RestResult<Boolean> saveOrUpdateBatch(Collection<StationGit> stationGitList) {
        log.info(String.format("批量保存或者更新站点git模板地址信息: %s ", JSONUtil.toJsonStr(stationGitList)));
        boolean result = stationGitService.saveOrUpdateBatch(stationGitList);
        return RestResult.ok(result);
    }

    /**
     * 根据StationGit对象属性逻辑删除站点git模板地址信息
     *
     * @param stationGit
     * @return
     */
    @PostMapping("/removeByStationGit")
    @ApiOperation(value="根据StationGit对象属性逻辑删除站点git模板地址信息", notes="根据站点git模板地址信息对象逻辑删除站点git模板地址信息信息")
    @ApiImplicitParam(name = "stationGit", value = "站点git模板地址信息对象", required = true, dataType = "StationGit", paramType = "query")
    public RestResult<Boolean> removeByStationGit(StationGit stationGit) {
        log.info(String.format("根据StationGit对象属性逻辑删除站点git模板地址信息: %s ", stationGit));
        boolean result = stationGitService.removeByBean(stationGit);
        return RestResult.ok(result);
    }


    /**
     * 根据ID批量逻辑删除站点git模板地址信息
     *
     * @param ids
     * @return
     */
    @PostMapping("/removeByIds")
    @ApiOperation(value="根据ID批量逻辑删除站点git模板地址信息", notes="根据站点git模板地址信息对象ID批量逻辑删除站点git模板地址信息信息")
    @ApiImplicitParam(name = "ids", value = "站点git模板地址信息对象ID集合", required = true, allowMultiple = true, dataType = "Serializable", paramType = "query")
    public RestResult<Boolean> removeByIds(@RequestParam("ids[]") List<String> ids) {
        log.info(String.format("根据id批量删除站点git模板地址信息: %s ", JSONUtil.toJsonStr(ids)));
        boolean result = stationGitService.removeByIds(ids);
        return RestResult.ok(result);
    }

    /**
     * 根据StationGit对象属性获取站点git模板地址信息
     *
     * @param stationGit
     * @return
     */
    @GetMapping("/getByStationGit")
    @ApiOperation(value="根据StationGit对象属性获取站点git模板地址信息", notes="根据站点git模板地址信息对象属性获取站点git模板地址信息信息")
    @ApiImplicitParam(name = "stationGit", value = "站点git模板地址信息对象", required = false, dataType = "StationGit", paramType = "query")
    public RestResult<StationGitVo> getByStationGit(StationGit stationGit) {
        stationGit = stationGitService.getByBean(stationGit);
        StationGitVo stationGitVo = stationGitService.setVoProperties(stationGit);
        log.info(String.format("根据id获取站点git模板地址信息：%s", JSONUtil.toJsonStr(stationGitVo)));
        return RestResult.ok(stationGitVo);
    }

    /**
     * 根据StationGit对象属性检索所有站点git模板地址信息
     *
     * @param stationGit
     * @return
     */
    @GetMapping("/listByStationGit")
    @ApiOperation(value="根据StationGit对象属性检索所有站点git模板地址信息", notes="根据StationGit对象属性检索所有站点git模板地址信息信息")
    @ApiImplicitParam(name = "stationGit", value = "站点git模板地址信息对象", required = false, dataType = "StationGit", paramType = "query")
    public RestResult<Collection<StationGitVo>> listByStationGit(StationGit stationGit) {
        Collection<StationGit> stationGits = stationGitService.listByBean(stationGit);
        Collection<StationGitVo> stationGitVos = stationGitService.setVoProperties(stationGits);
        log.info(String.format("根据StationGit对象属性检索所有站点git模板地址信息: %s ",JSONUtil.toJsonStr(stationGitVos)));
        return RestResult.ok(stationGitVos);
    }

    /**
     * 根据StationGit对象属性分页检索站点git模板地址信息
     *
     * @param stationGit
     * @return
     */
    @GetMapping("/pageByStationGit")
    @ApiOperation(value="根据StationGit对象属性分页检索站点git模板地址信息", notes="根据StationGit对象属性分页检索站点git模板地址信息信息")
    @ApiImplicitParam(name = "stationGit", value = "站点git模板地址信息对象", required = false, dataType = "StationGit", paramType = "query")
    public RestResult<IPage<StationGitVo>> pageByStationGit(StationGit stationGit) {
        IPage<StationGitVo> stationGits = stationGitService.pageByBean(stationGit);
        stationGits.setRecords(stationGitService.setVoProperties(stationGits.getRecords()));
        log.info(String.format("根据StationGit对象属性分页检索站点git模板地址信息: %s ",JSONUtil.toJsonStr(stationGits)));
        return RestResult.ok(stationGits);
    }

    /**
     * 关联查询站点和站点git地址信息表
     *
     * @return
     */
    @GetMapping("/listByStationGroupAndStationGit")
    @ApiOperation(value="关联查询站点和站点git地址信息", notes="关联查询站点和站点git地址信息")
    public RestResult<Collection<StationGitVo>> listByStationGroupAndStationGit() {
        Collection<StationGitVo> stationGitVos = stationGitService.listByStationGroupAndStationGit();
        log.info(String.format("根据StationGit对象属性检索所有站点git模板地址信息: %s ",JSONUtil.toJsonStr(stationGitVos)));
        return RestResult.ok(stationGitVos);
    }



    /**
     * 从git地址远程同步模板文件
     *
     * @param gitUrl
     * @param userName
     * @param password
     * @param siteId
     * @return
     */
    @PostMapping("/sync")
    @ApiOperation(value="从git地址远程同步模板文件", notes="从git地址远程同步模板文件")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "gitUrl", value = "Git地址", required = true, dataType = "String", paramType = "query"),
        @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "String", paramType = "query"),
        @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "query"),
        @ApiImplicitParam(name = "siteId", value = "站点ID", required = true, dataType = "String", paramType = "query")
    })
    public RestResult<Boolean> sync(String siteId,String gitUrl,String userName,String password) {
        try {
            String dir = stationFeign.getStationGroupTemplatePathBySiteId(siteId).getData();
            JGitUtil.cloneRepository(gitUrl,dir,userName,password);
        }catch (Exception e){
            log.error("远程同步失败，请稍后再试或者检查用户名、密码是否错误", e);
            return RestResult.error("远程同步失败，请稍后再试或者检查用户名、密码是否错误");
        }
        return RestResult.ok();
    }

    /**
     * 根据站点ID获取模板文件信息
     *
     * @param siteId
     * @param path
     * @return
     */
    @GetMapping("/listTemplateFiles")
    @ApiOperation(value="根据站点ID获取模板文件信息", notes="根据站点ID获取模板文件信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "path", value = "读取路径", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "siteId", value = "站点ID", required = true, dataType = "String", paramType = "query")
    })
    public RestResult<String> listTemplateFiles(String siteId,String path) {
        String json = stationGitService.getTemplateFiles(siteId,path);
        if(json.equals("error")){
            return RestResult.error("站点目录获取失败");
        }
        return RestResult.ok(json);
    }

    /**
     * 根据站点ID递归获取所有模板文件信息
     *
     * @param siteId
     * @return
     */
    @GetMapping("/listTemplateAllFiles")
    @ApiOperation(value="根据站点ID递归获取所有模板文件信息", notes="根据站点ID递归获取所有模板文件信息")
    @ApiImplicitParam(name = "siteId", value = "站点ID", required = true, dataType = "String", paramType = "query")
    public RestResult<String> listTemplateAllFiles(String siteId,String type) {
        String json = stationGitService.getTemplateAllFiles(siteId,type);
        if(json.equals("error")){
            return RestResult.error("站点目录获取失败");
        }
        return RestResult.ok(json);
    }

    /**
     * 查看文件内容详情
     *
     * @param path
     * @return
     */
    @GetMapping("/getFileContent")
    @ApiOperation(value="查看文件内容详情", notes="查看文件内容详情")
    @ApiImplicitParam(name = "path", value = "读取路径", required = true, dataType = "String", paramType = "query")
    public RestResult<String> getFileContent(String path) {
        return RestResult.ok(stationGitService.getFileContent(path));
    }



    /**
     * 解压上传的模板压缩包
     *
     * @param filePath
     * @param siteId
     * @return
     */
    @GetMapping("/unzip")
    @ApiOperation(value="解压上传的模板压缩包", notes="解压上传的模板压缩包")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fileName", value = "文件名称", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "siteId", value = "站点ID", required = true, dataType = "String", paramType = "query")
    })
    public RestResult unzip(String filePath,String dirPath,String fileName,String siteId) {
        String dir = stationFeign.getStationGroupTemplatePathBySiteId(siteId).getData();
        //File local = new File(dir);
        File file = new File(filePath);
        String extName = filePath.substring(filePath.lastIndexOf(".")+1);
        try {
            if(extName.equals("zip")){
                //FileUtils.deleteDirectory(local);
                ZipUtil.unzip(filePath,dir);
            }else{
                if(StringUtils.isEmpty(dirPath)){
                    dirPath = dir;
                }
                FileUtils.copyFile(file,new File(dirPath + "/" + fileName));
            }
            FileUtils.deleteQuietly(file);
        }catch (Exception e){
            e.printStackTrace();
        }
        return RestResult.ok(true);
    }


    /**
     * 根据站点ID查询站点git信息
     *
     * @param siteId
     * @return
     */
    @GetMapping("/getStationGitBySiteId")
    @ApiOperation(value="根据站点ID查询站点git信息", notes="根据站点ID查询站点git信息")
    @ApiImplicitParam(name = "siteId", value = "站点ID", required = true, dataType = "String", paramType = "query")
    public RestResult<StationGit> getStationGitBySiteId(String siteId) {
        return RestResult.ok(stationGitService.getStationGitBySiteId(siteId));
    }
}
