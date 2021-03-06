package com.deyatech.station.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.common.base.BaseController;
import com.deyatech.common.entity.RestResult;
import com.deyatech.common.exception.BusinessException;
import com.deyatech.station.entity.Material;
import com.deyatech.station.service.MaterialService;
import com.deyatech.station.vo.MaterialDirectoryVo;
import com.deyatech.station.vo.MaterialVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 上传文件信息 前端控制器
 * </p>
 * @author: lee.
 * @since 2019-08-28
 */
@Slf4j
@RestController
@RequestMapping("/station/material")
@Api(tags = {"接口"})
public class MaterialController extends BaseController {

    @Autowired
    MaterialService materialService;

    /**
     * 根据url获取材料
     *
     * @param url
     * @return
     */
    @GetMapping("/getDownloadMaterialsByUrl")
    @ApiOperation(value="根据url获取材料", notes="根据url获取材料")
    @ApiImplicitParam(name = "url", value = "url", required = false, dataType = "String", paramType = "query")
    public RestResult getDownloadMaterialsByUrl(String url) {
        return RestResult.ok(materialService.getDownloadMaterialsByUrl(url));
    }

    /**
     * 单个保存或者更新
     *
     * @param material
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation(value="单个保存或者更新", notes="根据对象保存或者更新信息")
    @ApiImplicitParam(name = "material", value = "对象", required = true, dataType = "Material", paramType = "query")
    public RestResult<Boolean> saveOrUpdate(Material material) {
        log.info(String.format("保存或者更新: %s ", JSONUtil.toJsonStr(material)));
        boolean result = materialService.saveOrUpdate(material);
        return RestResult.ok(result);
    }

    /**
     * 批量保存或者更新
     *
     * @param materialList
     * @return
     */
    @PostMapping("/saveOrUpdateBatch")
    @ApiOperation(value="批量保存或者更新", notes="根据对象集合批量保存或者更新信息")
    @ApiImplicitParam(name = "materialList", value = "对象集合", required = true, allowMultiple = true, dataType = "Material", paramType = "query")
    public RestResult<Boolean> saveOrUpdateBatch(Collection<Material> materialList) {
        log.info(String.format("批量保存或者更新: %s ", JSONUtil.toJsonStr(materialList)));
        boolean result = materialService.saveOrUpdateBatch(materialList);
        return RestResult.ok(result);
    }

    /**
     * 根据Material对象属性逻辑删除
     *
     * @param material
     * @return
     */
    @PostMapping("/removeByMaterial")
    @ApiOperation(value="根据Material对象属性逻辑删除", notes="根据对象逻辑删除信息")
    @ApiImplicitParam(name = "material", value = "对象", required = true, dataType = "Material", paramType = "query")
    public RestResult<Boolean> removeByMaterial(Material material) {
        log.info(String.format("根据Material对象属性逻辑删除: %s ", material));
        boolean result = materialService.removeByBean(material);
        return RestResult.ok(result);
    }


    /**
     * 根据ID批量逻辑删除
     *
     * @param ids
     * @return
     */
    @PostMapping("/removeByIds")
    @ApiOperation(value="根据ID批量逻辑删除", notes="根据对象ID批量逻辑删除信息")
    @ApiImplicitParam(name = "ids", value = "对象ID集合", required = true, allowMultiple = true, dataType = "Serializable", paramType = "query")
    public RestResult<Boolean> removeByIds(@RequestParam("ids[]") List<String> ids) {
        log.info(String.format("根据id批量删除: %s ", JSONUtil.toJsonStr(ids)));
//        boolean result = materialService.removeByIds(ids);
        // 真实删除
        int count = materialService.deletePhysicsMaterialByIds(ids);
        return RestResult.ok(count > 0 ? true : false);
    }

    /**
     * 根据Material对象属性获取
     *
     * @param material
     * @return
     */
    @GetMapping("/getByMaterial")
    @ApiOperation(value="根据Material对象属性获取", notes="根据对象属性获取信息")
    @ApiImplicitParam(name = "material", value = "对象", required = false, dataType = "Material", paramType = "query")
    public RestResult<MaterialVo> getByMaterial(Material material) {
        material = materialService.getByBean(material);
        MaterialVo materialVo = materialService.setVoProperties(material);
        log.info(String.format("根据id获取：%s", JSONUtil.toJsonStr(materialVo)));
        return RestResult.ok(materialVo);
    }

    /**
     * 根据Material对象属性检索所有
     *
     * @param material
     * @return
     */
    @GetMapping("/listByMaterial")
    @ApiOperation(value="根据Material对象属性检索所有", notes="根据Material对象属性检索所有信息")
    @ApiImplicitParam(name = "material", value = "对象", required = false, dataType = "Material", paramType = "query")
    public RestResult<Collection<MaterialVo>> listByMaterial(Material material) {
        Collection<Material> materialCollection = materialService.listByBean(material);
        Collection<MaterialVo> materialVoCollection = materialService.setVoProperties(materialCollection);
        log.info(String.format("根据Material对象属性检索所有: %s ",JSONUtil.toJsonStr(materialVoCollection)));
        return RestResult.ok(materialVoCollection);
    }

    /**
     * 根据Material对象属性分页检索
     *
     * @param material
     * @return
     */
    @GetMapping("/pageByMaterial")
    @ApiOperation(value="根据Material对象属性分页检索", notes="根据Material对象属性分页检索信息")
    @ApiImplicitParam(name = "material", value = "对象", required = false, dataType = "Material", paramType = "query")
    public RestResult<IPage<MaterialVo>> pageByMaterial(Material material) {
        IPage<MaterialVo> materialVoIPage = materialService.pageByMaterial(material);
        materialVoIPage.setRecords(materialService.setVoProperties(materialVoIPage.getRecords()));
        log.info(String.format("根据Materials对象属性分页检索: %s ",JSONUtil.toJsonStr(materialVoIPage)));
        return RestResult.ok(materialVoIPage);
    }

    /**
     * 根据树目录分页检索
     *
     * @param directory
     * @return
     */
    @GetMapping("/pageByDirectory")
    @ApiOperation(value="根据树目录分页检索", notes="根据树目录分页检索")
    @ApiImplicitParam(name = "directory", value = "对象", required = false, dataType = "MaterialDirectoryVo", paramType = "query")
    public RestResult<IPage<MaterialVo>> pageByDirectory(MaterialDirectoryVo directory) {
        log.info(String.format("根据树目录分页检索参数: %s ",JSONUtil.toJsonStr(directory)));
        if (StrUtil.isEmpty(directory.getPath()) || StrUtil.isEmpty(directory.getSiteId())) {
            return RestResult.error("参数不正确");
        }
        directory.setPath(directory.getPath().replace("\\", "/"));
        IPage<MaterialVo> materialVoIPage = materialService.pageByDirectory(directory);
        materialVoIPage.setRecords(materialService.setVoProperties(materialVoIPage.getRecords()));
        return RestResult.ok(materialVoIPage);
    }

    /**
     * 获取站点上传文件路径
     *
     * @param siteId
     * @return
     */
    @GetMapping("/getSiteUploadPath")
    @ApiImplicitParam(name = "siteId", value = "站点id", required = true, dataType = "String", paramType = "query")
    public RestResult getSiteUploadPath(String siteId) {
        return RestResult.ok(materialService.getSiteUploadPath(siteId));
    }

    /**
     * 上传文件
     *
     * @param file 上传文件
     * @param siteId 站点编号
     * @param attach 回传附加参数
     * @return
     */
    @PostMapping("/uploadFile")
    @ApiOperation(value = "上传文件", notes = "上传文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "文件", required = true, dataType = "MultipartFile", paramType = "query"),
            @ApiImplicitParam(name = "siteId", value = "站点编号", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "attach", value = "回传附加参数", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "deal", value = "是否处理", required = true, dataType = "String", paramType = "query")
    })
    public RestResult uploadFile(@RequestParam("file") MultipartFile file,
                                 @RequestParam("siteId") String siteId,
                                 @RequestParam(value = "attach", required = false) String attach,
                                 @RequestParam(value = "deal", required = false) String deal) {
        if (StrUtil.isEmpty(siteId)) {
            throw new BusinessException(HttpStatus.HTTP_INTERNAL_ERROR, "站点编号不存在");
        }
        // 判断图片是否为空
        if (file.isEmpty()) {
            log.error("上传的文件是空文件");
            return RestResult.build(HttpStatus.HTTP_INTERNAL_ERROR, "上传的文件是空文件");
        }
        if (file.getOriginalFilename().lastIndexOf(".") == -1) {
            log.error("文件类型无法识别");
            return RestResult.build(HttpStatus.HTTP_INTERNAL_ERROR, "文件类型无法识别");
        }
        return RestResult.ok(materialService.uploadFileHandle(file, siteId, attach, deal));
    }

    /**
     * 查看图片
     *
     * @param siteId 站点编号
     * @param url 上传返回的url地址
     * @param response
     */
    @GetMapping("/showImageBySiteIdAndUrl")
    @ApiOperation(value = "查看图片", notes = "查看图片")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "siteId", value = "站点编号", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "url", value = "图片URL", required = true, dataType = "String", paramType = "query")
    })
    public void showImageBySiteIdAndUrl(String siteId, String url, HttpServletResponse response) {
        showImage(materialService.getFilePath(siteId, url), response);
    }

    /**
     * 查看图片
     *
     * @param filePath 图片绝对路径地址
     * @param response
     */
    @GetMapping("/showImageByFilePath")
    @ApiOperation(value = "查看图片", notes = "查看图片")
    @ApiImplicitParam(name = "filePath", value = "图片绝对路径地址", required = true, dataType = "String", paramType = "query")
    public void showImageByFilePath(String filePath, HttpServletResponse response) {
        showImage(filePath, response);
    }

    /**
     * 下载文件
     *
     * @param siteId
     * @param url
     * @param response
     */
    @GetMapping("/downloadFileBySiteIdAndUrl")
    @ApiOperation(value = "下载文件", notes = "下载文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "siteId", value = "站点编号", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "url", value = "图片URL", required = true, dataType = "String", paramType = "query")
    })
    public void downloadFileBySiteIdAndUrl(String siteId, String url, HttpServletRequest request, HttpServletResponse response) {
        downloadFile(materialService.getFilePath(siteId, url), request, response);
    }

    /**
     * 下载文件
     *
     * @param filePath
     * @param response
     */
    @GetMapping("/downloadFileByFilePath")
    @ApiOperation(value = "下载文件", notes = "下载文件")
    @ApiImplicitParam(name = "filePath", value = "图片绝对路径地址", required = true, dataType = "String", paramType = "query")
    public void downloadFileByFilePath(String filePath, HttpServletRequest request, HttpServletResponse response) {
        downloadFile(filePath, request, response);
    }

        /**
     * 查看图片
     *
     * @param filePath
     * @param response
     */
    private void showImage(String filePath, HttpServletResponse response) {
        if (StrUtil.isEmpty(filePath)) {
            log.error("图片路径为空");
            return;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            log.error("图片不存在：" + filePath);
            return;
        }
        filePath = filePath.replace("\\","/");
        FileInputStream in = null;
        OutputStream out = null;
        try {
            response.setContentType("image/jpeg");
            in = new FileInputStream(filePath);
            out = response.getOutputStream();
            IOUtils.copy(in, out);
        } catch (IOException e) {
            log.error("图片读取失败", e);
        } finally {
            close(in);
            close(out);
        }
    }

    /**
     * 下载文件
     *
     * @param filePath
     * @param request
     * @param response
     */
    private void downloadFile(String filePath, HttpServletRequest request, HttpServletResponse response) {
        if (StrUtil.isEmpty(filePath)) {
            log.error("文件路径为空");
            return;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            log.error("文件不存在：" + filePath);
            return;
        }
        FileInputStream in = null;
        OutputStream out = null;
        try {
            String fileName = filePath.substring(filePath.lastIndexOf('/') + 1);
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.setContentType(request.getServletContext().getMimeType(filePath));
            in = new FileInputStream(filePath);
            out = response.getOutputStream();
            IOUtils.copy(in, out);
        } catch (IOException e) {
            log.error("文件读取失败", e);
        } finally {
            close(in);
            close(out);
        }
    }

    /**
     * 获取目录树
     *
     * @param siteId 站点编号
     */
    @RequestMapping("/getDirectoryTree")
    @ApiOperation(value = "获取目录树", notes = "获取目录树")
    @ApiImplicitParam(name = "siteId", value = "站点编号", required = true, dataType = "String", paramType = "query")
    private RestResult getDirectoryTree(String siteId) {
        return RestResult.ok(materialService.getDirectoryTree(siteId));
    }

}
