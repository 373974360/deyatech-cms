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
    /**
     * MQ 消息代码，生成静态页
     */
    public static final String MQ_CMS_STATIC_PAGE_CODE_ADD = "add";
    /**
     * MQ 消息代码，更新静态页
     */
    public static final String MQ_CMS_STATIC_PAGE_CODE_UPDATE = "update";
    /**
     * MQ 消息代码，更新静态页
     */
    public static final String MQ_CMS_STATIC_PAGE_CODE_DELETE = "delete";


    public static final String TOPIC_ADMIN = "ecp";
    public static final String TOPIC_CMS = "cms";
    /**
     * TopicExchange
     */
    public static final String CMS_TASK_TOPIC_EXCHANGE = "cms-task-topic-exchange";
    /**
     * 静态页队列名称-带进度条
     */
    public static final String QUEUE_NAME_STATIC_PROGRESS_PAGE_TASK = "staticPageProgressTaskQueue";
    /**
     * 静态页队列名称
     */
    public static final String QUEUE_NAME_STATIC_PAGE_TASK = "staticPageTaskQueue";
    /**
     * 索引任务队列名称
     */
    public static final String QUEUE_NAME_INDEX_TASK = "indexTaskQueue";
}
