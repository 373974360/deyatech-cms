package com.deyatech.zsds.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.util.Iterator;
import java.util.Map;

/**
 * Excel个工具类
 * @author csm
 * @since 2019-10-28
 */
public class ExcelUtil {

    public static boolean isExcel2003(String filePath) {
        boolean b = StrUtil.isNotEmpty(filePath) ? filePath.contains(".xls") : false;
        return b;
    }

    public static boolean isExcel2007(String filePath) {
        boolean b = StrUtil.isNotEmpty(filePath) ? filePath.contains(".xlsx") : false;
        return b;
    }

    /**
     * 判断列是否为空
     * @param cell
     * @return
     */
    public static boolean cellIsNull(Cell cell){
        //判断各个单元格是否为空
        if(cell == null || "".equals(cell) || cell.getCellType() == CellType.BLANK){
            return true;
        }
        return false;
    }

    /**
     * 判断行是否为空
     * @param row
     * @return
     */
    public static boolean rowIsNull(Row row){
        Iterator<Cell> cellItr = row.iterator();
        while(cellItr.hasNext()){
            Cell c =cellItr.next();
            if(!cellIsNull(c)){
                return false;
            }
        }
        return true;
    }

    /**
     * 获取数据
     * @param cell
     * @return
     */
    public static Object getCellValue(Cell cell) {
        if (cellIsNull(cell)) { return null; }
        Object value = null;
        CellType cellType = cell.getCellType();
        // 布尔
        if (cellType == CellType.BOOLEAN) { value = cell.getBooleanCellValue(); }
        // 字符串
        if (cellType == CellType.STRING) { value = cell.getStringCellValue(); }
        // 数值
        if (cellType == CellType.NUMERIC) { value = cell.getNumericCellValue(); }
        // 公式
        if (cellType == CellType.FORMULA) {
            // 时间公式
            if (cell.getCellFormula().equalsIgnoreCase("NOW()")) {
                value = cell.getDateCellValue();
            } else {
                FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
                evaluator.evaluateFormulaCell(cell);
                CellValue cellValue = evaluator.evaluate(cell);
                CellType FormulaCellType = cellValue.getCellType();
                if (FormulaCellType == CellType.BOOLEAN) { value = cellValue.getBooleanValue(); }
                if (FormulaCellType == CellType.STRING) { value = cellValue.getStringValue(); }
                if (FormulaCellType == CellType.NUMERIC) { value = cellValue.getNumberValue(); }
            }
        }
        return value;
    }

    /**
     * 获取Workbook对象
     * @param file
     * @return
     */
    public static Map getWorkbookFromExcel(MultipartFile file) {
        Map map = CollectionUtil.newHashMap();
        Workbook wb = null;
        try {
            if (isExcel2007(file.getOriginalFilename())) {
                wb = new XSSFWorkbook(file.getInputStream());
            } else if (isExcel2003(file.getOriginalFilename())) {
                wb = new HSSFWorkbook(file.getInputStream());
            } else {
                map.put("message", "上传文件不是Excel类型");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            map.put("message", "解析文件失败");
        }
        map.put("wb", wb);
        return map;
    }
}
