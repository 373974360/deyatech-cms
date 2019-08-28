package com.deyatech.station.controller;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import com.deyatech.common.Constants;
import com.deyatech.common.entity.FileUploadResult;
import com.deyatech.common.exception.BusinessException;
import com.deyatech.station.entity.File;
import com.deyatech.station.vo.FileVo;
import com.deyatech.station.service.FileService;
import com.deyatech.common.entity.RestResult;
import io.swagger.annotations.ApiImplicitParams;
import lombok.extern.slf4j.Slf4j;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import java.io.Serializable;
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
@RequestMapping("/station/file")
@Api(tags = {"接口"})
public class FileController extends BaseController {

    @Value("${uploadPath}")
    private String uploadPath;

    @Autowired
    FileService fileService;

    /**
     * 单个保存或者更新
     *
     * @param file
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation(value="单个保存或者更新", notes="根据对象保存或者更新信息")
    @ApiImplicitParam(name = "file", value = "对象", required = true, dataType = "File", paramType = "query")
    public RestResult<Boolean> saveOrUpdate(File file) {
        log.info(String.format("保存或者更新: %s ", JSONUtil.toJsonStr(file)));
        boolean result = fileService.saveOrUpdate(file);
        return RestResult.ok(result);
    }

    /**
     * 批量保存或者更新
     *
     * @param fileList
     * @return
     */
    @PostMapping("/saveOrUpdateBatch")
    @ApiOperation(value="批量保存或者更新", notes="根据对象集合批量保存或者更新信息")
    @ApiImplicitParam(name = "fileList", value = "对象集合", required = true, allowMultiple = true, dataType = "File", paramType = "query")
    public RestResult<Boolean> saveOrUpdateBatch(Collection<File> fileList) {
        log.info(String.format("批量保存或者更新: %s ", JSONUtil.toJsonStr(fileList)));
        boolean result = fileService.saveOrUpdateBatch(fileList);
        return RestResult.ok(result);
    }

    /**
     * 根据File对象属性逻辑删除
     *
     * @param file
     * @return
     */
    @PostMapping("/removeByFile")
    @ApiOperation(value="根据File对象属性逻辑删除", notes="根据对象逻辑删除信息")
    @ApiImplicitParam(name = "file", value = "对象", required = true, dataType = "File", paramType = "query")
    public RestResult<Boolean> removeByFile(File file) {
        log.info(String.format("根据File对象属性逻辑删除: %s ", file));
        boolean result = fileService.removeByBean(file);
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
        boolean result = fileService.removeByIds(ids);
        return RestResult.ok(result);
    }

    /**
     * 根据File对象属性获取
     *
     * @param file
     * @return
     */
    @GetMapping("/getByFile")
    @ApiOperation(value="根据File对象属性获取", notes="根据对象属性获取信息")
    @ApiImplicitParam(name = "file", value = "对象", required = false, dataType = "File", paramType = "query")
    public RestResult<FileVo> getByFile(File file) {
        file = fileService.getByBean(file);
        FileVo fileVo = fileService.setVoProperties(file);
        log.info(String.format("根据id获取：%s", JSONUtil.toJsonStr(fileVo)));
        return RestResult.ok(fileVo);
    }

    /**
     * 根据File对象属性检索所有
     *
     * @param file
     * @return
     */
    @GetMapping("/listByFile")
    @ApiOperation(value="根据File对象属性检索所有", notes="根据File对象属性检索所有信息")
    @ApiImplicitParam(name = "file", value = "对象", required = false, dataType = "File", paramType = "query")
    public RestResult<Collection<FileVo>> listByFile(File file) {
        Collection<File> files = fileService.listByBean(file);
        Collection<FileVo> fileVos = fileService.setVoProperties(files);
        log.info(String.format("根据File对象属性检索所有: %s ",JSONUtil.toJsonStr(fileVos)));
        return RestResult.ok(fileVos);
    }

    /**
     * 根据File对象属性分页检索
     *
     * @param file
     * @return
     */
    @GetMapping("/pageByFile")
    @ApiOperation(value="根据File对象属性分页检索", notes="根据File对象属性分页检索信息")
    @ApiImplicitParam(name = "file", value = "对象", required = false, dataType = "File", paramType = "query")
    public RestResult<IPage<FileVo>> pageByFile(File file) {
        IPage<FileVo> files = fileService.pageByBean(file);
        files.setRecords(fileService.setVoProperties(files.getRecords()));
        log.info(String.format("根据File对象属性分页检索: %s ",JSONUtil.toJsonStr(files)));
        return RestResult.ok(files);
    }

    @PostMapping("/uploadFile")
    @ApiOperation(value = "上传文件", notes = "上传文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "文件", required = true, dataType = "MultipartFile", paramType = "query"),
            @ApiImplicitParam(name = "path", value = "路径", required = false, dataType = "String", paramType = "query")
    })
    public RestResult uploadFile(@RequestParam("file") MultipartFile file, String path) {
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
            uploadFile(file.getBytes(), uploadPath, fileName);
            String url = Constants.UPLOAD_DEFAULT_PREFIX_URL.concat(fileName);
            if (StrUtil.isNotBlank(url)) {
                //转存文件
                result.setState("SUCCESS");
                result.setOriginal(originalFilename);
                result.setTitle(originalFilename);
                result.setUrl(url);
                result.setFilePath(uploadPath + fileName);

                File f = new File();
                f.setName(originalFilename);
                f.setType(extName);
                f.setUrl(url);
                f.setPath(result.getFilePath());
                fileService.saveOrUpdate(f);

                result.setCustomData(f);

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
