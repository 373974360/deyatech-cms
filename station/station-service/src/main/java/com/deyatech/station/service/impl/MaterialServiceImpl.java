package com.deyatech.station.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.deyatech.common.Constants;
import com.deyatech.common.base.BaseServiceImpl;
import com.deyatech.common.entity.FileUploadResult;
import com.deyatech.common.entity.RestResult;
import com.deyatech.common.enums.MaterialUsePlaceEnum;
import com.deyatech.common.enums.WaterMarkTypeEnum;
import com.deyatech.common.enums.YesNoEnum;
import com.deyatech.common.exception.BusinessException;
import com.deyatech.resource.entity.Setting;
import com.deyatech.resource.feign.ResourceFeign;
import com.deyatech.station.cache.SiteCache;
import com.deyatech.station.config.SiteProperties;
import com.deyatech.station.entity.Material;
import com.deyatech.station.mapper.MaterialMapper;
import com.deyatech.station.service.MaterialService;
import com.deyatech.station.vo.MaterialDirectoryVo;
import com.deyatech.station.vo.MaterialVo;
import io.lettuce.core.protocol.CommandType;
import lombok.extern.slf4j.Slf4j;
import org.im4java.core.*;
import org.im4java.process.StandardStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * <p>
 * 上传文件信息 服务实现类
 * </p>
 *
 * @Author lee.
 * @since 2019-08-28
 */
@Service
@Slf4j
public class MaterialServiceImpl extends BaseServiceImpl<MaterialMapper, Material> implements MaterialService {

    @Autowired
    SiteCache siteCache;
    @Autowired
    SiteProperties siteProperties;
    @Autowired
    ResourceFeign resourceFeign;

    /**
     * 单个将对象转换为vo
     *
     * @param material
     * @return
     */
    @Override
    public MaterialVo setVoProperties(Material material){
        MaterialVo fileVo = new MaterialVo();
        BeanUtil.copyProperties(material, fileVo);
        return fileVo;
    }

    /**
     * 批量将对象转换为vo
     *
     * @param files
     * @return
     */
    @Override
    public List<MaterialVo> setVoProperties(Collection files){
        List<MaterialVo> fileVos = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(files)) {
            for (Object file : files) {
                MaterialVo fileVo = new MaterialVo();
                BeanUtil.copyProperties(file, fileVo);
                fileVos.add(fileVo);
            }
        }
        return fileVos;
    }

    /**
     * 根据url获取材料
     *
     * @param url
     * @return
     */
    @Override
    public List<MaterialVo> getDownloadMaterialsByUrl(String url) {
        if (StrUtil.isEmpty(url))
            return new ArrayList<>();
        return baseMapper.getDownloadMaterialsByUrl(Arrays.asList(url.split(",")));
    }
    @Override
    public List<MaterialVo> getDisplayMaterialsByUrl(String url) {
        if (StrUtil.isEmpty(url))
            return new ArrayList<>();
        return baseMapper.getDisplayMaterialsByUrl(Arrays.asList(url.split(",")));
    }

    /**
     * 获取站点目录树
     *
     * @param siteId
     * @return
     */
    @Override
    public List<MaterialDirectoryVo> getDirectoryTree(String siteId) {
        List<MaterialDirectoryVo> tree = new ArrayList<>();
        getTree(this.getSiteUploadPath(siteId), tree);
        return tree;
    }

    /**
     * 递归树目录
     *
     * @param path
     * @param tree
     */
    private static void getTree(String path, List<MaterialDirectoryVo> tree) {
        try {
            Files.newDirectoryStream(Paths.get(path))
                    .forEach(p -> {
                        File file = p.toFile();
                        if (file.isDirectory()) {
                            MaterialDirectoryVo directory = new MaterialDirectoryVo();
                            directory.setLabel(file.getName());
                            directory.setChildren(new ArrayList<>());
                            directory.setPath(file.getAbsolutePath());
                            getTree(file.getAbsolutePath(), directory.getChildren());
                            tree.add(directory);
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据树目录分页检索
     *
     * @param directory
     * @return
     */
    @Override
    public IPage<MaterialVo> pageByDirectory(MaterialDirectoryVo directory) {
        Page page = new Page();
        page.setCurrent(directory.getPage());
        page.setSize(directory.getSize());
        return baseMapper.pageByDirectory(page, directory);
    }

    /**
     * 根据材料翻页检索
     *
     * @param material
     * @return
     */
    @Override
    public IPage<MaterialVo> pageByMaterial(Material material) {
        return baseMapper.pageByMaterial(getPageByBean(material),material);
    }

    /**
     * 物理删除材料
     *
     * @param ids
     * @return
     */
    @Override
    public int deletePhysicsMaterialByIds(List<String> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return 0;
        }
        List<MaterialVo> list = baseMapper.selectMaterialByIds(ids);
        int count = baseMapper.deleteMaterialByIds(ids);
        if (count > 0) {
            list.stream().forEach(m -> {
                try {
                    log.info("删除文件：" + m.getPath());
                    Files.deleteIfExists(Paths.get(m.getPath()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        return count;
    }

    /**
     * 标记材料使用地
     *
     * @param oldUrlList
     * @param newUrlList
     * @param usePlace
     */
    @Override
    public void markMaterialUsePlace(List<String> oldUrlList, List<String> newUrlList, String usePlace) {
        if (CollectionUtil.isNotEmpty(oldUrlList)) {
            baseMapper.updateUsePlaceByUrl(oldUrlList, MaterialUsePlaceEnum.UNUSED.getCode());
        }
        if (CollectionUtil.isNotEmpty(newUrlList)) {
            baseMapper.updateUsePlaceByUrl(newUrlList, usePlace);
        }
    }

    private void uploadFile(byte[] file, String filePath, String fileName) throws Exception {
        File targetFile = new File(filePath);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }

        FileOutputStream out = new FileOutputStream(filePath + fileName);
        out.write(file);
        out.flush();
        out.close();
    }

    /**
     * 获取站点上传文件路径
     *
     * @param siteId
     * @return
     */
    @Override
    public String getSiteUploadPath(String siteId) {
        String path;
        // 站点全局配置
        if ("0".equals(siteId)) {
            // hostsRoot
            path =  new File(this.siteProperties.getHostsRoot(), Constants.UPLOAD_DEFAULT_PREFIX_URL).getAbsolutePath();
        } else {
            // hostsRoot + englishName
            path =  new File(siteCache.getStationGroupRootPath(siteId), Constants.UPLOAD_DEFAULT_PREFIX_URL).getAbsolutePath();
        }
        path = path.replace("\\", "/");
        if (!path.endsWith("/")) {
            path += "/";
        }
        log.info("站点材料上传路径：" + path);
        return path;
    }

    /**
     * 获取文件物理路径
     *
     * @param siteId
     * @param url
     * @return
     */
    @Override
    public String getFilePath(String siteId, String url) {
        // 获取站点根目录
        String sitePath = siteCache.getStationGroupRootPath(siteId);
//        String fileName = url.substring(url.lastIndexOf("/")+1);
        StringBuilder filePath = new StringBuilder(sitePath);
//        filePath.append(fileName.substring(0, 4));
//        filePath.append("/");
//        filePath.append(fileName.substring(4, 6));
//        filePath.append("/");
//        filePath.append(fileName.substring(6, 8));
//        filePath.append("/");
        filePath.append(url);
        return  filePath.toString();
    }

    private void imageHandle(String sitePath, String part, String extName, String siteId) throws Exception {
        extName = extName.toLowerCase();
        if (!".jpg".equals(extName) && !".jpeg".equals(extName) && !".png".equals(extName) && !".bmp".equals(extName) && !".gif".equals(extName)) {
            return;
        }
        String filePath = sitePath + part + extName;
        Setting setting = resourceFeign.getStationSetting(siteId).getData();
        if (Objects.nonNull(setting)) {
            // 水印处理
            int isWatermark = setting.getWatermarkEnable();
            if (YesNoEnum.YES.getCode() == isWatermark) {
                String watermarkPath = sitePath + part + "_watermark" + extName;
                log.info("原始图片: " + filePath);
                log.info("生成图片: " + watermarkPath);
                // 图片水印
                if (WaterMarkTypeEnum.PICTURE.getCode() == setting.getWatermarkType()) {
                    String image = getFilePath(StrUtil.isEmpty(setting.getStationGroupId()) ? "0" : setting.getStationGroupId(), setting.getWatermarkUrl());
                    log.info("水印图片: " + image);
                    IMOperation op = new IMOperation();
                    op.gravity(setting.getWatermarkPosition()); //位置 center 中, north 上, south 下, west 左, east 右, northwest 左上, southwest 左下 northeast 右上 southeast 右下
                    op.dissolve(100 - setting.getWatermarkTransparency()); //水印清晰度 ，0-100  最好设置高点要不看起来没效果
                    op.addImage(image);
                    op.addImage(filePath);
                    op.addImage(watermarkPath);
                    // gm composite -gravity center -dissolve 50 watermark.png input.jpg output.jpg
                    CompositeCmd cmd = new CompositeCmd(true);
                    cmd.setSearchPath(siteProperties.getGmPath());
                    cmd.setErrorConsumer(StandardStream.STDERR);
                    cmd.run(op);
                }
                // 文字水印
                else {
                    log.info("水印文字: " + setting.getWatermarkWord());
                    float rate = 0.9F;
                    int x = 0;
                    int y = 0;
                    String gravity = setting.getWatermarkPosition();
                    if (gravity.contains("north")) {
                        y = (int) (setting.getWatermarkPointSize() * rate);
                    }
                    if (gravity.contains("south")) {
                        y = (int) (setting.getWatermarkPointSize() * (1 - rate));
                    }
                    IMOperation op = new IMOperation();
                    op.font(siteProperties.getGmFont());
                    op.gravity(gravity);
                    op.pointsize(setting.getWatermarkPointSize());
                    op.fill(setting.getWatermarkFillColor());
                    op.draw("text " + x + "," + y + " '" + new String(setting.getWatermarkWord().getBytes("utf-8"),"gbk") + "'");
                    op.addImage(filePath);
                    op.addImage(watermarkPath);
                    // gm convert -font /usr/share/fonts/win/simsun.ttc -gravity center -pointsize 20 -fill red -draw "text 0,0 'hello水印'" input.jpg output.jpg
                    ConvertCmd cmd = new ConvertCmd(true);
                    cmd.setSearchPath(siteProperties.getGmPath());
                    cmd.setErrorConsumer(StandardStream.STDERR);
                    cmd.run(op);
                }
                filePath = watermarkPath;
            }
            // 尺寸处理
            int isThumbnail = setting.getThumbnailEnable();
            if (YesNoEnum.YES.getCode() == isThumbnail) {
                String thumbnailPath = sitePath + part + "_thumbnail" + extName;
                resize(filePath, thumbnailPath, setting.getThumbnailWidth());
            }
        }
    }

    /**
     * 缩小尺寸
     *
     * @param srcFilePath
     * @param destFilePath
     * @param destWidth
     * @throws Exception
     */
    private void resize(String srcFilePath, String destFilePath, int destWidth) throws Exception {
        BufferedImage image = ImageIO.read(new File(srcFilePath));
        if (destWidth >= image.getWidth()) {
            return;
        }
        IMOperation op = new IMOperation();
        op.resize(destWidth, null);
        op.sharpen(1.0, 3.0);
        op.quality(100d);
        op.addImage(srcFilePath);
        op.addImage(destFilePath);
        // gm convert -resize 100x100 -sharpen 1.0 -quality 100 input.jpg output.jpg
        ConvertCmd cmd = new ConvertCmd(true);
        cmd.setSearchPath(siteProperties.getGmPath());
        cmd.setErrorConsumer(StandardStream.STDERR);
        cmd.run(op);
    }


    /**
     * 处理水印图片大小
     *
     * @param siteId
     * @param url
     */
    @Override
    public void watermarkHandle(String siteId, String url) {
        Setting setting = resourceFeign.getStationSetting(siteId).getData();
        if (Objects.nonNull(setting)) {
            // 有图片水印
            if (YesNoEnum.YES.getCode() == setting.getWatermarkEnable() && WaterMarkTypeEnum.PICTURE.getCode() == setting.getWatermarkType()) {
                try {
                    String filePath = getFilePath(siteId, url);
                    resize(filePath, filePath, setting.getWatermarkWidth());
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new BusinessException(HttpStatus.HTTP_INTERNAL_ERROR, "水印图片尺寸处理异常");
                }
            }
        }
    }

    /**
     * 文件上传处理
     *
     * @param file
     * @param siteId
     * @param attach
     * @param deal
     * @return
     */
    @Override
    public FileUploadResult uploadFileHandle(MultipartFile file, String siteId, String attach, String deal) {
        try {
            String datePath = DateUtil.format(new Date(), "yyyy/MM/dd") + "/";
            String originalFilename = file.getOriginalFilename();
            String extName = originalFilename.substring(originalFilename.lastIndexOf("."));
            String part = DateUtil.format(new Date(), DatePattern.PURE_DATETIME_FORMAT) + RandomUtil.randomNumbers(4);
            String sitePath = getSiteUploadPath(siteId) + datePath;
            String filePath = sitePath + part + extName;
            uploadFile(file.getBytes(), sitePath, part + extName);
            String url = Constants.UPLOAD_DEFAULT_PREFIX_URL.concat(datePath + part + extName);
            //转存文件
            FileUploadResult result = new FileUploadResult();
            result.setState("SUCCESS");
            result.setOriginal(originalFilename);
            result.setTitle(originalFilename);
            result.setUrl(url); // 返回相对路径
            result.setFilePath(filePath);// 文件存储的物理绝对地址
            result.setAttach(attach);// 前台来的参数，原样返回，内容动态表单用
            MaterialVo material = new MaterialVo();
            material.setName(originalFilename);
            material.setType(extName);
            material.setUrl(url);
            material.setPath(filePath);
            material.setSiteId(siteId);
            material.setUsePlace(MaterialUsePlaceEnum.UNKNOWN.getCode());
            super.saveOrUpdate(material);
            material.setValue(url);
            result.setCustomData(material);
            if (!"no".equals(deal)) {
                imageHandle(sitePath, part, extName, siteId);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("上传失败");
            throw new BusinessException(HttpStatus.HTTP_INTERNAL_ERROR, "上传失败");
        }
    }


}
