package com.deyatech.zsds.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.admin.feign.AdminFeign;
import com.deyatech.admin.vo.UserVo;
import com.deyatech.common.context.UserContextHelper;
import com.deyatech.zsds.entity.LogisticsDistribution;
import com.deyatech.zsds.utils.ExcelUtil;
import com.deyatech.zsds.vo.LogisticsDistributionVo;
import com.deyatech.zsds.mapper.LogisticsDistributionMapper;
import com.deyatech.zsds.service.LogisticsDistributionService;
import com.deyatech.common.base.BaseServiceImpl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 物流配送体系 服务实现类
 * </p>
 *
 * @Author csm
 * @since 2019-11-08
 */
@Service
public class LogisticsDistributionServiceImpl extends BaseServiceImpl<LogisticsDistributionMapper, LogisticsDistribution> implements LogisticsDistributionService {

    @Autowired
    private AdminFeign adminFeign;

    @Autowired
    private LogisticsDistributionMapper logisticsDistributionMapper;

    /**
     * 单个将对象转换为vo物流配送体系
     *
     * @param logisticsDistribution
     * @return
     */
    @Override
    public LogisticsDistributionVo setVoProperties(LogisticsDistribution logisticsDistribution){
        LogisticsDistributionVo logisticsDistributionVo = new LogisticsDistributionVo();
        BeanUtil.copyProperties(logisticsDistribution, logisticsDistributionVo);
        return logisticsDistributionVo;
    }

    /**
     * 批量将对象转换为vo物流配送体系
     *
     * @param logisticsDistributions
     * @return
     */
    @Override
    public List<LogisticsDistributionVo> setVoProperties(Collection logisticsDistributions){
        List<LogisticsDistributionVo> logisticsDistributionVos = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(logisticsDistributions)) {
            for (Object logisticsDistribution : logisticsDistributions) {
                LogisticsDistributionVo logisticsDistributionVo = new LogisticsDistributionVo();
                BeanUtil.copyProperties(logisticsDistribution, logisticsDistributionVo);
                logisticsDistributionVos.add(logisticsDistributionVo);
            }
        }
        return logisticsDistributionVos;
    }

    @Override
    public IPage<LogisticsDistributionVo> pageByLogisticsDistributionVo(LogisticsDistributionVo logisticsDistributionVo) {
        // 设置部门id
        logisticsDistributionVo.setDepartmentId(this.getDepartmentId());
        return logisticsDistributionMapper.pageByLogisticsDistributionVo(getPageByBean(logisticsDistributionVo), logisticsDistributionVo);
    }

    @Override
    public boolean saveOrUpdate(LogisticsDistribution entity) {
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
        List<LogisticsDistribution> logisticsDistributionList = CollectionUtil.newArrayList();
        // 获取后台用户部门id
        String departmentId = this.getDepartmentId();

        // 获取第一张表
        Sheet sheet = wb.getSheetAt(0);
        // 录入失败数量
        int failCount = 0;
        for (Row row : sheet) {

            // 第四行开始录入
            if(row.getRowNum() < 3) { continue; }

            // 快递收货数量/快递发货数量为空时不录入
            if(ExcelUtil.cellIsNull(row.getCell(1)) && ExcelUtil.cellIsNull(row.getCell(2))) { continue; }

            try {
                LogisticsDistribution logisticsDistribution = this.getBeanFromRow(row);
                // 设置部门id
                logisticsDistribution.setDepartmentId(departmentId);

                logisticsDistributionList.add(logisticsDistribution);
            }
            catch (Exception e) {
                e.printStackTrace();
                failCount++;
                continue;
            }
        }
        // 保存
        if (CollectionUtil.isNotEmpty(logisticsDistributionList)) {
            super.saveBatch(logisticsDistributionList);
        }

        map.put("failCount", failCount);
        map.put("successCount", logisticsDistributionList.size());
        return map;
    }

    private LogisticsDistribution getBeanFromRow(Row row) {
        LogisticsDistribution logisticsDistribution = new LogisticsDistribution();
        // 快递收货数量
        Object cell1 = ExcelUtil.getCellValue(row.getCell(1));
        Long receivedCount = ObjectUtil.isNull(cell1) ? 0L : this.getNumberByString(cell1);
        // 快递发货数量
        Object cell2 = ExcelUtil.getCellValue(row.getCell(2));
        Long deliveredCount = ObjectUtil.isNull(cell2) ? 0L : this.getNumberByString(cell2);

        logisticsDistribution.setReceivedCount(receivedCount);
        logisticsDistribution.setDeliveredCount(deliveredCount);

        return logisticsDistribution;
    }

    private Long getNumberByString(Object value) {
        Pattern p = Pattern.compile("[^0-9]");
        Matcher m = p.matcher(String.valueOf(value));
        String s = m.replaceAll("").trim();
        return Long.valueOf(s);
    }

    @Override
    public List<LogisticsDistributionVo> getLogisticsInfoByDate(String dateStr) {
        return logisticsDistributionMapper.getLogisticsInfoByDate(dateStr);
    }

    @Override
    public List<LogisticsDistributionVo> getLogisticsInfoByDateRange(String startDate, String endDate) {
        return logisticsDistributionMapper.getLogisticsInfoByDateRange(startDate, endDate);
    }

    @Override
    public List<LogisticsDistributionVo> getTodayLogisticsInfo() {
        return logisticsDistributionMapper.getTodayLogisticsInfo();
    }
}
