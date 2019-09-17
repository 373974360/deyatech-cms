package com.deyatech.station.controller;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import com.deyatech.common.Constants;
import com.deyatech.common.entity.FileUploadResult;
import com.deyatech.common.exception.BusinessException;
import com.deyatech.station.cache.SiteCache;
import com.deyatech.station.entity.Material;
import com.deyatech.station.vo.MaterialVo;
import com.deyatech.station.service.MaterialService;
import com.deyatech.common.entity.RestResult;
import io.swagger.annotations.ApiImplicitParams;
import lombok.extern.slf4j.Slf4j;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.springframework.web.bind.annotation.RestController;
import com.deyatech.common.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

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

    @Autowired
    SiteCache siteCache;

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
        String siteRootPath = siteCache.getStationGroupRootPath(siteId);
        String uploadPath = new File(siteRootPath, Constants.UPLOAD_DEFAULT_PREFIX_URL).getAbsolutePath();
        return RestResult.ok(uploadPath);
    }

    /**
     * 上传文件
     *
     * @param file
     * @param path
     * @return
     */
    @PostMapping("/uploadFile")
    @ApiOperation(value = "上传文件", notes = "上传文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "文件", required = true, dataType = "MultipartFile", paramType = "query"),
            @ApiImplicitParam(name = "path", value = "路径", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "siteId", value = "站点id", required = false, dataType = "String", paramType = "query")
    })
    public RestResult uploadFile(@RequestParam("file") MultipartFile file, String path, String siteId) {
        String fileSeparator = System.getProperty("file.separator");
        if (!path.endsWith(fileSeparator)) {
            path += fileSeparator;
        }

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
            uploadFile(file.getBytes(), path, fileName);
            String url = Constants.UPLOAD_DEFAULT_PREFIX_URL.concat(fileName);
            if (StrUtil.isNotBlank(url)) {
                //转存文件
                result.setState("SUCCESS");
                result.setOriginal(originalFilename);
                result.setTitle(originalFilename);
                result.setUrl(url);
                result.setFilePath(path + fileName);

                Material material = new Material();
                material.setName(originalFilename);
                material.setType(extName);
                material.setUrl(url);
                material.setPath(result.getFilePath());
                material.setSiteId(siteId);
                materialService.saveOrUpdate(material);

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
}
