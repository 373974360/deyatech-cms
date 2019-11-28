package com.deyatech.template.thymeleaf.utils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.util.List;


/**
 * 描述：
 *
 * @Author: MaChaoWei
 * @Date: 2019/11/27 21:14
 */
@Data
public class PageUtil<T> extends Page<T> {

    private String prePageUrl;
    private String nextPageUrl;
    private String endPageUrl;
    private String firstPageUrl;
    private String currPageUrl;
    private List<String> pageList;
}
