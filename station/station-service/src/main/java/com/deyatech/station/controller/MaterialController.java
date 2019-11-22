package com.deyatech.station.controller;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.common.Constants;
import com.deyatech.common.base.BaseController;
import com.deyatech.common.entity.FileUploadResult;
import com.deyatech.common.entity.RestResult;
import com.deyatech.common.exception.BusinessException;
import com.deyatech.station.entity.Material;
import com.deyatech.station.service.MaterialService;
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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Date;
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
        boolean result = materialService.removeByIds(ids);
        return RestResult.ok(result);
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
        IPage<MaterialVo> materialVoIPage = materialService.pageByBean(material);
        materialVoIPage.setRecords(materialService.setVoProperties(materialVoIPage.getRecords()));
        log.info(String.format("根据Materials对象属性分页检索: %s ",JSONUtil.toJsonStr(materialVoIPage)));
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
            @ApiImplicitParam(name = "attach", value = "回传附加参数", required = true, dataType = "String", paramType = "query")
    })
    public RestResult uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("siteId") String siteId, @RequestParam(value = "attach", required = false) String attach) {
        if (StrUtil.isEmpty(siteId)) {
            throw new BusinessException(HttpStatus.HTTP_INTERNAL_ERROR, "站点编号不存在");
        }
        String sitePath = materialService.getSiteUploadPath(siteId);
        sitePath += DateUtil.format(new Date(), "yyyy/MM/dd") + "/";
        FileUploadResult result = new FileUploadResult();
        //判断图片是否为空
        if (file.isEmpty()) {
            log.error("上传的文件是空文件");
            return RestResult.build(HttpStatus.HTTP_INTERNAL_ERROR, "上传的文件是空文件");
        }
        try {
            String originalFilename = file.getOriginalFilename();
            int index = originalFilename.lastIndexOf(".");
            //获取文件扩展名
            String extName;
            if (index != -1) {
                extName = originalFilename.substring(index);
            } else {
                log.error("文件类型无法识别");
                return RestResult.build(HttpStatus.HTTP_INTERNAL_ERROR, "文件类型无法识别");
            }
            String fileName = DateUtil.format(new Date(), DatePattern.PURE_DATETIME_FORMAT) + RandomUtil.randomNumbers(4) + extName;
            //调用文件处理类FileUtil，处理文件，将文件写入指定位置
            uploadFile(file.getBytes(), sitePath, fileName);
            String url = Constants.UPLOAD_DEFAULT_PREFIX_URL.concat(fileName);
            if (StrUtil.isNotBlank(url)) {
                //转存文件
                result.setState("SUCCESS");
                result.setOriginal(originalFilename);
                result.setTitle(originalFilename);
                result.setUrl(url);
                result.setFilePath(sitePath + fileName);// 文件存储的物理绝对地址
                result.setAttach(attach);//前台来的参数，原样返回，内容动态表单用

                MaterialVo material = new MaterialVo();
                material.setName(originalFilename);
                material.setType(extName);
                material.setUrl(url);
                material.setPath(result.getFilePath());
                material.setSiteId(siteId);
                materialService.saveOrUpdate(material);
                material.setValue(url);
                result.setCustomData(material);

                return RestResult.build(HttpStatus.HTTP_OK, "上传成功", result);
            } else {
                result.setState("ERROR");
                log.error("上传失败");
                return RestResult.build(HttpStatus.HTTP_INTERNAL_ERROR, "上传失败", result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("上传失败");
            throw new BusinessException(HttpStatus.HTTP_INTERNAL_ERROR, "上传失败");
        }
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
        showImage(getFilePath(siteId, url), response);
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
        downloadFile(getFilePath(siteId, url), request, response);
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
     * 获取文件物理路径
     *
     * @param siteId
     * @param url
     * @return
     */
    private String getFilePath(String siteId, String url) {
        String sitePath = materialService.getSiteUploadPath(siteId);
        String fileName = url.replace(Constants.UPLOAD_DEFAULT_PREFIX_URL,"");
        StringBuilder filePath = new StringBuilder(sitePath);
        filePath.append(fileName.substring(0, 4));
        filePath.append("/");
        filePath.append(fileName.substring(4, 6));
        filePath.append("/");
        filePath.append(fileName.substring(6, 8));
        filePath.append("/");
        filePath.append(fileName);
        return  filePath.toString();
    }

    /**
     * 查看图片
     *
     * @param filePath
     * @param response
     */
    private void showImage(String filePath, HttpServletResponse response) {
        if (StrUtil.isEmpty(filePath)) {
            throw new BusinessException(HttpStatus.HTTP_INTERNAL_ERROR, "文件路径空");
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
            log.error("读取文件失败", e);
            throw new BusinessException(HttpStatus.HTTP_INTERNAL_ERROR, "读取文件失败");
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
            log.error("读取文件失败", e);
            throw new BusinessException(HttpStatus.HTTP_INTERNAL_ERROR, "读取文件失败");
        } finally {
            close(in);
            close(out);
        }
    }

}
