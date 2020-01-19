package com.deyatech.station.rabbit.config;

import com.deyatech.station.rabbit.constants.RabbitMQConstants;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Rabbit配置信息
 * @Author csm
 * @Date 2019/08/13
 */
@Configuration
public class RabbitConfig {
    /**
     * 交换器
     *
     * @return
     */
    @Bean
    public FanoutExchange fanoutExchangeResetIndexCode() { return new FanoutExchange(RabbitMQConstants.FANOUT_EXCHANGE_RESET_INDEX_CODE); }
    /**
     * 队列
     *
     * @return
     */
    @Bean
    public Queue queueResetIndexCode() {return new AnonymousQueue();}
    /**
     * 绑定
     *
     * @return
     */
    @Bean
    public Binding bindingResetIndexCode() { return BindingBuilder.bind(queueResetIndexCode()).to(fanoutExchangeResetIndexCode()); }

    @Bean
    public TopicExchange csmTaskTopicExchange() {
        return new TopicExchange(RabbitMQConstants.CMS_TASK_TOPIC_EXCHANGE);
    }

    /**
     * 待处理生成静态页面的任务队列
     *
     * @return
     */
    @Bean
    public Queue staticPageTaskQueue() {
        return new Queue(RabbitMQConstants.QUEUE_NAME_STATIC_PAGE_TASK);
    }

    /**
     * 待处理索引列表页的任务队列
     *
     * @return
     */
    @Bean
    public Queue listPageTaskQueue() {
        return new Queue(RabbitMQConstants.QUEUE_NAME_LIST_PAGE_TASK);
    }

    /**
     * 索引数据任务队列
     *
     * @return
     */
    @Bean
    public Queue indexTaskQueue() {
        return new Queue(RabbitMQConstants.QUEUE_NAME_INDEX_TASK);
    }


    /**
     * 内容状态切换处理
     *
     * @return
     */
    @Bean
    public Queue queueContentStatusSwitchHandle() {
        return new Queue(RabbitMQConstants.QUEUE_CONTENT_STATUS_SWITCH_HANDLE);
    }

    /**
     * 聚合关联关系-栏目规则变更
     *
     * @return
     */
    @Bean
    public Queue queueAggregationCatalogChange() {
        return new Queue(RabbitMQConstants.QUEUE_AGGREGATION_CATALOG_CHANGE);
    }

    /**
     * 聚合关联关系-内容变更
     *
     * @return
     */
    @Bean
    public Queue queueAggregationTemplateChange() {
        return new Queue(RabbitMQConstants.QUEUE_AGGREGATION_TEMPLATE_CHANGE);
    }

    /**
     * 定时发布
     *
     * @return
     */
    @Bean
    public Queue queueTimingPublishTemplate() {
        return new Queue(RabbitMQConstants.QUEUE_TIMING_PUBLISH_TEMPLATE);
    }

    /**
     * 自动删除匿名队列
     * @param csmTaskTopicExchange 广播交换器
     * @param staticPageTaskQueue 自动删除队列
     * @return
     */
    @Bean
    public Binding bindingStaticPageTask(TopicExchange csmTaskTopicExchange, Queue staticPageTaskQueue) {
        return BindingBuilder.bind(staticPageTaskQueue).to(csmTaskTopicExchange).with(RabbitMQConstants.QUEUE_NAME_STATIC_PAGE_TASK);
    }

    /**
     * 自动删除匿名队列
     * @param csmTaskTopicExchange 广播交换器
     * @param listPageTaskQueue 自动删除队列
     * @return
     */
    @Bean
    public Binding bindingListPageTask(TopicExchange csmTaskTopicExchange, Queue listPageTaskQueue) {
        return BindingBuilder.bind(listPageTaskQueue).to(csmTaskTopicExchange).with(RabbitMQConstants.QUEUE_NAME_LIST_PAGE_TASK);
    }

    /**
     * 自动删除匿名队列
     * @param csmTaskTopicExchange 广播交换器
     * @param indexTaskQueue 自动删除队列
     * @return
     */
    @Bean
    public Binding bindingIndexTask(TopicExchange csmTaskTopicExchange, Queue indexTaskQueue) {
        return BindingBuilder.bind(indexTaskQueue).to(csmTaskTopicExchange).with(RabbitMQConstants.QUEUE_NAME_INDEX_TASK);
    }

    /**
     * 内容状态切换处理: 静态页、索引、工作流
     *
     * @param csmTaskTopicExchange
     * @param queueContentStatusSwitchHandle
     * @return
     */
    @Bean
    public Binding bindingContentStatusSwitchHandle(TopicExchange csmTaskTopicExchange, Queue queueContentStatusSwitchHandle) {
        return BindingBuilder.bind(queueContentStatusSwitchHandle).to(csmTaskTopicExchange).with(RabbitMQConstants.QUEUE_CONTENT_STATUS_SWITCH_HANDLE);
    }

    /**
     * 聚合关联关系-栏目规则变更
     *
     * @return
     */
    @Bean
    public Binding bindingAggregationCatalogChange() {
        return BindingBuilder.bind(queueAggregationCatalogChange()).to(csmTaskTopicExchange()).with(RabbitMQConstants.QUEUE_AGGREGATION_CATALOG_CHANGE);
    }

    /**
     * 聚合关联关系-栏目规则变更
     *
     * @return
     */
    @Bean
    public Binding bindingAggregationTemplateChange() {
        return BindingBuilder.bind(queueAggregationTemplateChange()).to(csmTaskTopicExchange()).with(RabbitMQConstants.QUEUE_AGGREGATION_TEMPLATE_CHANGE);
    }

    /**
     * 定时发布
     *
     * @return
     */
    @Bean
    public Binding bindingTimingPublishTemplate() {
        return BindingBuilder.bind(queueTimingPublishTemplate()).to(csmTaskTopicExchange()).with(RabbitMQConstants.QUEUE_TIMING_PUBLISH_TEMPLATE);
    }
}
