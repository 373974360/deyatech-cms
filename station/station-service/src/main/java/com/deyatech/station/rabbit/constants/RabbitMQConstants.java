package com.deyatech.station.rabbit.constants;

/**
 * rabbit常量
 * @Author csm
 * @Date 2019/08/13
 */
public class RabbitMQConstants {
    /**
     * MQ消息代码，栏目有更新
     */
    public static final String MQ_MESSAGE_CODE_CATALOG_EDIT = "catalogEdit";
    /**
     * MQ 消息代码，索引数据
     */
    public static final String MQ_CMS_INDEX_COMMAND_ADD = "add";
    /**
     * MQ 消息代码，更新数据
     */
    public static final String MQ_CMS_INDEX_COMMAND_UPDATE = "update";
    /**
     * MQ 消息代码，删除数据
     */
    public static final String MQ_CMS_INDEX_COMMAND_DELETE = "delete";


    public static final String TOPIC_ADMIN = "ecp";
    public static final String TOPIC_CMS = "cms";
    /**
     * TopicExchange
     */
    public static final String CMS_TASK_TOPIC_EXCHANGE = "cms-task-topic-exchange";
    /**
     * 静态页队列名称
     */
    public static final String QUEUE_NAME_STATIC_PAGE_TASK = "staticPageTaskQueue";
    /**
     * 索引任务队列名称
     */
    public static final String QUEUE_NAME_INDEX_TASK = "indexTaskQueue";
    /**
     * 缓存列表页队列名称
     */
    public static final String QUEUE_NAME_LIST_PAGE_TASK = "listPageTaskQueue";
    /**
     * 重置内容索引编码
     */
    public static final String FANOUT_EXCHANGE_RESET_INDEX_CODE = "fanout-exchange-reset-index-code";

    /**
     * 内容状态切换处理: 静态页、索引、工作流
     */
    public static final String QUEUE_CONTENT_STATUS_SWITCH_HANDLE = "queue.content.status.switch.handle";
}
