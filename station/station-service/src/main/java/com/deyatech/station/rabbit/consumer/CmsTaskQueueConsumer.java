package com.deyatech.station.rabbit.consumer;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.deyatech.station.rabbit.constants.RabbitMQConstants;
import com.deyatech.station.index.IndexService;
import com.deyatech.station.vo.TemplateVo;
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

    /**
     * 处理生成静态页面任务
     * @param templateVo
     */
    @RabbitListener(queues = RabbitMQConstants.QUEUE_NAME_STATIC_PAGE_TASK)
    public void handleCmsStaticTask(TemplateVo templateVo) {
        log.info(String.format("处理生成静态页面任务：%s", JSONUtil.toJsonStr(templateVo)));

        // 立即生成静态页面 TODO
//        templateContextUtils.genStaticContentPage(templateVo);
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
//            this.addIndex(templateVo);
        } else if (RabbitMQConstants.MQ_CMS_INDEX_COMMAND_UPDATE.equalsIgnoreCase(messageCode)) {
//            this.updateIndex(templateVo);
        } else if (RabbitMQConstants.MQ_CMS_INDEX_COMMAND_DELETE.equalsIgnoreCase(messageCode)) {
//            this.deleteIndex(templateVo);
        } else {
            log.warn("未知的索引任务: %s ", JSONUtil.toJsonStr(templateVo));
        }
    }

    private void addIndex(TemplateVo templateVo) {
        BeanMap dataRow = BeanMap.create(templateVo);
        HashMap<Object, Object> objectObjectHashMap = CollectionUtil.newHashMap();
        objectObjectHashMap.putAll(dataRow);
        objectObjectHashMap.put("id", templateVo.getId());
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
        // 增加内容模型的英文名称，方便在搜索结果中根据内容模型的类型渲染对应的格式
        objectObjectHashMap.put("contentModelName", templateVo.getContentModelName());
        // 生成索引 TODO
        indexService.addData(templateVo.getIndex(), templateVo.getId(), objectObjectHashMap);
    }

    private void deleteIndex(TemplateVo templateVo) {
        // 删除索引 TODO
        indexService.deleteData(templateVo.getIndex(), templateVo.getId());
    }

    private void updateIndex(TemplateVo templateVo) {
        this.deleteIndex(templateVo);
        this.addIndex(templateVo);
    }


}
