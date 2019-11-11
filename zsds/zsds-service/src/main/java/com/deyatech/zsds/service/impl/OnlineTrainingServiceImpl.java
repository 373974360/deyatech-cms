package com.deyatech.zsds.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.admin.feign.AdminFeign;
import com.deyatech.admin.vo.UserVo;
import com.deyatech.common.context.UserContextHelper;
import com.deyatech.zsds.entity.OnlineTraining;
import com.deyatech.zsds.utils.ExcelUtil;
import com.deyatech.zsds.vo.OnlineTrainingVo;
import com.deyatech.zsds.mapper.OnlineTrainingMapper;
import com.deyatech.zsds.service.OnlineTrainingService;
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
 * 在线培训系统 服务实现类
 * </p>
 *
 * @Author csm
 * @since 2019-11-08
 */
@Service
public class OnlineTrainingServiceImpl extends BaseServiceImpl<OnlineTrainingMapper, OnlineTraining> implements OnlineTrainingService {

    @Autowired
    private AdminFeign adminFeign;

    @Autowired
    private OnlineTrainingMapper onlineTrainingMapper;

    /**
     * 单个将对象转换为vo在线培训系统
     *
     * @param onlineTraining
     * @return
     */
    @Override
    public OnlineTrainingVo setVoProperties(OnlineTraining onlineTraining){
        OnlineTrainingVo onlineTrainingVo = new OnlineTrainingVo();
        BeanUtil.copyProperties(onlineTraining, onlineTrainingVo);
        return onlineTrainingVo;
    }

    /**
     * 批量将对象转换为vo在线培训系统
     *
     * @param onlineTrainings
     * @return
     */
    @Override
    public List<OnlineTrainingVo> setVoProperties(Collection onlineTrainings){
        List<OnlineTrainingVo> onlineTrainingVos = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(onlineTrainings)) {
            for (Object onlineTraining : onlineTrainings) {
                OnlineTrainingVo onlineTrainingVo = new OnlineTrainingVo();
                BeanUtil.copyProperties(onlineTraining, onlineTrainingVo);
                onlineTrainingVos.add(onlineTrainingVo);
            }
        }
        return onlineTrainingVos;
    }

    @Override
    public IPage<OnlineTrainingVo> pageByOnlineTrainingVo(OnlineTrainingVo onlineTrainingVo) {
        // 设置部门id
        onlineTrainingVo.setDepartmentId(this.getDepartmentId());
        return onlineTrainingMapper.pageByOnlineTrainingVo(getPageByBean(onlineTrainingVo), onlineTrainingVo);
    }

    @Override
    public boolean saveOrUpdate(OnlineTraining entity) {
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
        List<OnlineTraining> onlineTrainingList = CollectionUtil.newArrayList();
        // 获取后台用户部门id
        String departmentId = this.getDepartmentId();

        // 获取第一张表
        Sheet sheet = wb.getSheetAt(0);
        // 录入失败数量
        int failCount = 0;
        for (Row row : sheet) {

            // 第四行开始录入
            if(row.getRowNum() < 3) { continue; }

            // 培训期数/培训人数为空时不录入
            if(ExcelUtil.cellIsNull(row.getCell(1)) && ExcelUtil.cellIsNull(row.getCell(2))) { continue; }

            try {
                OnlineTraining onlineTraining = this.getBeanFromRow(row);
                // 设置部门id
                onlineTraining.setDepartmentId(departmentId);

                onlineTrainingList.add(onlineTraining);
            }
            catch (Exception e) {
                e.printStackTrace();
                failCount++;
                continue;
            }
        }
        // 保存
        if (CollectionUtil.isNotEmpty(onlineTrainingList)) {
            super.saveBatch(onlineTrainingList);
        }

        map.put("failCount", failCount);
        map.put("successCount", onlineTrainingList.size());
        return map;
    }

    private OnlineTraining getBeanFromRow(Row row) {
        OnlineTraining onlineTraining =  new OnlineTraining();
        // 培训期数
        Object cell1 = ExcelUtil.getCellValue(row.getCell(1));
        Integer period = ObjectUtil.isNull(cell1) ? 0 : this.getNumberByString(cell1);
        // 本期培训人数
        Object cell2 = ExcelUtil.getCellValue(row.getCell(2));
        Integer number = ObjectUtil.isNull(cell2) ? 0 : this.getNumberByString(cell2);

        onlineTraining.setPeriod(period);
        onlineTraining.setNumber(number);

        return onlineTraining;
    }

    private Integer getNumberByString(Object value) {
        Pattern p = Pattern.compile("[^0-9]");
        Matcher m = p.matcher(String.valueOf(value));
        String s = m.replaceAll("").trim();
        return Integer.valueOf(s);
    }

    @Override
    public List<OnlineTrainingVo> getOnlineInfoByDate(String dateStr) {
        return onlineTrainingMapper.getOnlineInfoByDate(dateStr);
    }

    @Override
    public List<OnlineTrainingVo> getOnlineInfoByDateRange(String startDate, String endDate) {
        return onlineTrainingMapper.getOnlineInfoByDateRange(startDate, endDate);
    }

    @Override
    public List<OnlineTrainingVo> getTodayOnlineInfo() {
        return onlineTrainingMapper.getTodayOnlineInfo();
    }
}
