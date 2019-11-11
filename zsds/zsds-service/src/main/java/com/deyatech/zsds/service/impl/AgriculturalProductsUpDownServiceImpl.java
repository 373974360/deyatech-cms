package com.deyatech.zsds.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.admin.feign.AdminFeign;
import com.deyatech.admin.vo.UserVo;
import com.deyatech.common.context.UserContextHelper;
import com.deyatech.zsds.entity.AgriculturalProductsUpDown;
import com.deyatech.zsds.entity.LogisticsDistribution;
import com.deyatech.zsds.utils.ExcelUtil;
import com.deyatech.zsds.vo.AgriculturalProductsUpDownVo;
import com.deyatech.zsds.mapper.AgriculturalProductsUpDownMapper;
import com.deyatech.zsds.service.AgriculturalProductsUpDownService;
import com.deyatech.common.base.BaseServiceImpl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 农产品上行/下行 服务实现类
 * </p>
 *
 * @Author csm
 * @since 2019-11-08
 */
@Service
public class AgriculturalProductsUpDownServiceImpl extends BaseServiceImpl<AgriculturalProductsUpDownMapper, AgriculturalProductsUpDown> implements AgriculturalProductsUpDownService {

    @Autowired
    private AdminFeign adminFeign;

    @Autowired
    private AgriculturalProductsUpDownMapper agriculturalProductsUpDownMapper;

    /**
     * 单个将对象转换为vo农产品上行/下行
     *
     * @param agriculturalProductsUpDown
     * @return
     */
    @Override
    public AgriculturalProductsUpDownVo setVoProperties(AgriculturalProductsUpDown agriculturalProductsUpDown){
        AgriculturalProductsUpDownVo agriculturalProductsUpDownVo = new AgriculturalProductsUpDownVo();
        BeanUtil.copyProperties(agriculturalProductsUpDown, agriculturalProductsUpDownVo);
        return agriculturalProductsUpDownVo;
    }

    /**
     * 批量将对象转换为vo农产品上行/下行
     *
     * @param agriculturalProductsUpDowns
     * @return
     */
    @Override
    public List<AgriculturalProductsUpDownVo> setVoProperties(Collection agriculturalProductsUpDowns){
        List<AgriculturalProductsUpDownVo> agriculturalProductsUpDownVos = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(agriculturalProductsUpDowns)) {
            for (Object agriculturalProductsUpDown : agriculturalProductsUpDowns) {
                AgriculturalProductsUpDownVo agriculturalProductsUpDownVo = new AgriculturalProductsUpDownVo();
                BeanUtil.copyProperties(agriculturalProductsUpDown, agriculturalProductsUpDownVo);
                agriculturalProductsUpDownVos.add(agriculturalProductsUpDownVo);
            }
        }
        return agriculturalProductsUpDownVos;
    }

    @Override
    public IPage<AgriculturalProductsUpDownVo> pageByAgriculturalProductsUpDownVo(AgriculturalProductsUpDownVo agriculturalProductsUpDownVo) {
        // 设置部门id
        agriculturalProductsUpDownVo.setDepartmentId(this.getDepartmentId());
        return agriculturalProductsUpDownMapper.pageByAgriculturalProductsUpDownVo(getPageByBean(agriculturalProductsUpDownVo), agriculturalProductsUpDownVo);
    }

    @Override
    public boolean saveOrUpdate(AgriculturalProductsUpDown entity) {
        // 设置部门id
        entity.setDepartmentId(this.getDepartmentId());
        return super.saveOrUpdate(entity);
    }

    /**
     * 获取后台用户部门id
     * @return
     */
    private String getDepartmentId() {
        // 获取后台用户id
        String userId = UserContextHelper.getUserId();
        // 获取用户信息
        UserVo userVo = adminFeign.getUserByUserId(userId).getData();
        return userVo.getDepartmentId();
    }

    /**
     * 上传Excel文件
     * @param file
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map importExcel(MultipartFile file) {
        // 获取Workbook对象
        Map map = ExcelUtil.getWorkbookFromExcel(file);
        if (ObjectUtil.isNull(map.get("message"))) {
            Workbook wb = (Workbook) map.get("wb");
            map.remove("wb");
            // 操作Excel获取信息
            this.getInfoFromExcel(wb, map);
        }

        return map;
    }

    private Map getInfoFromExcel(Workbook wb, Map map) {
        List<AgriculturalProductsUpDown> agriculturalProductsUpDownList = CollectionUtil.newArrayList();
        // 获取后台用户部门id
        String departmentId = this.getDepartmentId();

        // 获取第一张表
        Sheet sheet = wb.getSheetAt(0);
        // 录入失败数量
        int failCount = 0;
        for (Row row : sheet) {

            // 第四行开始录入
            if(row.getRowNum() < 3) { continue; }

            // 网络销售额度为空时不录入
            if(ExcelUtil.cellIsNull(row.getCell(1))) { continue; }

            try {
                AgriculturalProductsUpDown agriculturalProductsUpDown = this.getBeanFromRow(row);
                // 设置部门id
                agriculturalProductsUpDown.setDepartmentId(departmentId);

                agriculturalProductsUpDownList.add(agriculturalProductsUpDown);
            }
            catch (Exception e) {
                e.printStackTrace();
                failCount++;
                continue;
            }
        }
        // 保存
        if (CollectionUtil.isNotEmpty(agriculturalProductsUpDownList)) {
            super.saveBatch(agriculturalProductsUpDownList);
        }

        map.put("failCount", failCount);
        map.put("successCount", agriculturalProductsUpDownList.size());
        return map;
    }

    private AgriculturalProductsUpDown getBeanFromRow(Row row) {
        AgriculturalProductsUpDown agriculturalProductsUpDown = new AgriculturalProductsUpDown();
        // 网络销售额度
        Object cell1 = ExcelUtil.getCellValue(row.getCell(1));
        Double onlineSalesQuota = this.getNumberByString(cell1);

        agriculturalProductsUpDown.setOnlineSalesQuota(onlineSalesQuota);

        return agriculturalProductsUpDown;
    }

    private Double getNumberByString(Object value) {
        Pattern p = Pattern.compile("[^0-9]^\\.");
        Matcher m = p.matcher(String.valueOf(value));
        String s = m.replaceAll("").trim();
        return Double.valueOf(s);
    }

    @Override
    public List<AgriculturalProductsUpDownVo> getAgriculturalInfoByDate(String dateStr) {
        List<AgriculturalProductsUpDownVo> agriculturalProductsUpDownVos = agriculturalProductsUpDownMapper.getAgriculturalInfoByDate(dateStr);
        // 网络销售额度字符串
        agriculturalProductsUpDownVos.stream().forEach(a -> a.setOnlineSalesQuotaStr(this.getDoubleStr(a.getOnlineSalesQuota())));

        return agriculturalProductsUpDownVos;
    }

    private String getDoubleStr(Double value) {
        NumberFormat nf = NumberFormat.getInstance();
        //设置保留多少位小数
        nf.setMaximumFractionDigits(2);
        // 取消科学计数法
        nf.setGroupingUsed(false);

        return nf.format(value);
    }

    @Override
    public List<AgriculturalProductsUpDownVo> getAgriculturalInfoByDateRange(String startDate, String endDate) {
        List<AgriculturalProductsUpDownVo> agriculturalProductsUpDownVos = agriculturalProductsUpDownMapper.getAgriculturalInfoByDateRange(startDate, endDate);
        // 网络销售额度字符串
        agriculturalProductsUpDownVos.stream().forEach(a -> a.setOnlineSalesQuotaStr(this.getDoubleStr(a.getOnlineSalesQuota())));

        return agriculturalProductsUpDownVos;
    }

    @Override
    public List<AgriculturalProductsUpDownVo> getTodayAgriculturalInfo() {
        List<AgriculturalProductsUpDownVo> agriculturalProductsUpDownVos = agriculturalProductsUpDownMapper.getTodayAgriculturalInfo();
        // 网络销售额度字符串
        agriculturalProductsUpDownVos.stream().forEach(a -> a.setOnlineSalesQuotaStr(this.getDoubleStr(a.getOnlineSalesQuota())));

        return agriculturalProductsUpDownVos;
    }
}
