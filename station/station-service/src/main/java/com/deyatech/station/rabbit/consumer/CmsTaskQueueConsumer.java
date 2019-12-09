package com.deyatech.station.rabbit.consumer;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.deyatech.station.entity.Template;
import com.deyatech.station.rabbit.constants.RabbitMQConstants;
import com.deyatech.station.index.IndexService;
import com.deyatech.station.service.TemplateService;
import com.deyatech.station.vo.TemplateVo;
import com.deyatech.template.feign.TemplateFeign;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.util.*;

/**
 * CMS中的任务队列消息处理
 * @Author csm
 * @Date 2019/08/13
 */
@Component
@Slf4j
public class CmsTaskQueueConsumer {

    @Autowired
    IndexService indexService;
    @Autowired
    TemplateFeign templateFeign;
    @Autowired
    TemplateService templateService;

    /**
     * 处理生成静态页面任务
     * @param templateVo
     */
    @RabbitListener(queues = RabbitMQConstants.QUEUE_NAME_STATIC_PAGE_TASK)
    public void handleCmsStaticTask(TemplateVo templateVo) {
        log.info(String.format("处理索引任务：%s", JSONUtil.toJsonStr(templateVo)));
        String messageCode = templateVo.getCode();
        // 创建/删除、更新静态页
        templateFeign.generateStaticTemplate(templateVo,messageCode);
    }

    /**
     * 处理索引任务
     * @param templateVo
     */
    @RabbitListener(queues = RabbitMQConstants.QUEUE_NAME_INDEX_TASK)
    public void handleCmsIndexTask(TemplateVo templateVo) {
        log.info(String.format("处理索引任务：%s", JSONUtil.toJsonStr(templateVo)));
        String messageCode = templateVo.getCode();

        // 索引/删除、更新数据
        if (RabbitMQConstants.MQ_CMS_INDEX_COMMAND_ADD.equalsIgnoreCase(messageCode)) {
            this.addIndex(templateVo);
        } else if (RabbitMQConstants.MQ_CMS_INDEX_COMMAND_UPDATE.equalsIgnoreCase(messageCode)) {
            this.updateIndex(templateVo);
        } else if (RabbitMQConstants.MQ_CMS_INDEX_COMMAND_DELETE.equalsIgnoreCase(messageCode)) {
            this.deleteIndex(templateVo);
        } else {
            log.warn("未知的索引任务: %s ", JSONUtil.toJsonStr(templateVo));
        }
    }

    private void addIndex(TemplateVo templateVo) {
        // TODO 设置其他附加属性this.setVoProperties(template） 设置contentModelName
        TemplateVo templateVoResult = templateService.setVoProperties(templateVo);
        BeanMap dataRow = BeanMap.create(templateVoResult);
        HashMap<Object, Object> objectObjectHashMap = CollectionUtil.newHashMap();
        objectObjectHashMap.putAll(dataRow);
        // 筛选要删除的key
        Set<Object> removeKey = new HashSet<>();
        for (Object key : objectObjectHashMap.keySet()) {
            Object value = objectObjectHashMap.get(key);
            if (value == null) {
                log.debug(key + " value is null ");
                continue;
            }
            if (!(value instanceof String)
                    && !(value instanceof Short)
                    && !(value instanceof Byte)
                    && !(value instanceof Float)
                    && !(value instanceof Double)
                    && !(value instanceof Integer)
                    && !(value instanceof Long)
                    && !(value instanceof Date)
                    && !(value instanceof Boolean)
                    && !(value instanceof Array)
                    && !(value instanceof List)
                    && !(value instanceof Map)
            ) {
                removeKey.add(key);
                log.warn(key + " value type is  " + value.getClass() + " for ignore add to index");
            }
            if (value instanceof Date) {
                log.debug(key + "  convert to  string  " + value.getClass());
                objectObjectHashMap.put(key, DateFormatUtils.format((Date) value, "yyyy-MM-dd'T'HH:mm:ss.sss'Z'"));
            }
        }
        for (Object key : removeKey) {
            objectObjectHashMap.remove(key);
        }
        // 向索引中添加数据
        indexService.addData(templateVo.getIndex(), templateVo.getId(), objectObjectHashMap);
    }

    private void deleteIndex(TemplateVo templateVo) {
        // 删除索引中数据
        indexService.deleteData(templateVo.getIndex(), templateVo.getId());
    }

    private void updateIndex(TemplateVo templateVo) {
        this.deleteIndex(templateVo);
        this.addIndex(templateVo);
    }


    public static void main(String[] args) {
        TemplateVo templateVo = new TemplateVo();
        templateVo.setId("1");
        templateVo.setContentId("11");
        BeanMap dataRow = BeanMap.create(templateVo);
        System.out.println(dataRow.get("id"));
        System.out.println(dataRow.get("contentId"));
    }

}
