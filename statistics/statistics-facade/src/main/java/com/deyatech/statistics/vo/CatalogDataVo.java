package com.deyatech.statistics.vo;


import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import lombok.Data;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * 描述：
 *
 * @Author: MaChaoWei
 * @Date: 2020/2/5 15:29
 */
@Data
public class CatalogDataVo {
    /**
     * 栏目ID
     * */
    String catId;
    /**
     * 栏目名称
     * */
    String catName;
    /**
     * 总量
     * */
    Integer count;
    /**
     * 发布量
     * */
    Integer pubCount;
    /**
     * 采用率
     * */
    String rate;
    /**
     * 平均日发稿量
     * */
    String averageDay;
    /**
     * 平均周发稿量
     * */
    String averageWeek;
    /**
     * 平均月发稿量
     * */
    String averageMonth;
    /**
     * 栏目保障单位
     * */
    String deptName;


    // 发稿率保留的位数
    private final static int DIGIT = 2;
    // 数字格式化对象,用于对发稿率进行格式化
    private static NumberFormat nf = null;
    static{
        nf = new DecimalFormat();
        nf.setMaximumFractionDigits(DIGIT);
        nf.setRoundingMode(RoundingMode.HALF_UP);
    }

    /**
     * 设置发布率,在调用本方法前请先确保已发和全部统计都已经设值
     * 否则,会出现发布率为0%的情况
     */
    public void setReleaseRate() {
        if(count != 0){
            float i_rate = (float)pubCount * 100 / count;
            rate = nf.format(i_rate);
        } else {
            rate = "0";
        }
        rate += "%";
    }



    /**
     * 日、周、月平均发布量
     */
    public void setReleaseAverage(String startTime,String endTime) {
        if(pubCount != 0){
            //日平均发布量
            long betweenDay = DateUtil.between(DateUtil.parse(startTime), DateUtil.parse(endTime), DateUnit.DAY)+1;
            float day_rate = (float)pubCount / betweenDay;
            averageDay = nf.format(day_rate);
            //周平均发布量
            if(betweenDay>=7){
                float week_rat = (float)pubCount / ((float)betweenDay/7);
                averageWeek = nf.format(week_rat);
            }else{
                averageWeek = "-";
            }
            //月平均发布量
            if(betweenDay>=30){
                float day_rat = (float)pubCount / ((float)betweenDay/30);
                averageMonth = nf.format(day_rat);
            }else{
                averageMonth = "-";
            }
        } else {
            averageDay = "0";
            averageWeek = "0";
            averageMonth = "0";
        }
    }
}
