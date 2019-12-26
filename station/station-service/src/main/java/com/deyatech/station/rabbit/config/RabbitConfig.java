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
     * 待处理生成静态页面的任务队列--进度条
     *
     * @return
     */
    @Bean
    public Queue staticPageProgressTaskQueue() {
        return new Queue(RabbitMQConstants.QUEUE_NAME_STATIC_PROGRESS_PAGE_TASK);
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
     * 自动删除匿名队列--进度条
     * @param csmTaskTopicExchange 广播交换器
     * @param staticPageProgressTaskQueue 自动删除队列
     * @return
     */
    @Bean
    public Binding bindingStaticPageProgressTask(TopicExchange csmTaskTopicExchange, Queue staticPageProgressTaskQueue) {
        return BindingBuilder.bind(staticPageProgressTaskQueue).to(csmTaskTopicExchange).with(RabbitMQConstants.QUEUE_NAME_STATIC_PROGRESS_PAGE_TASK);
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
     * @param indexTaskQueue 自动删除队列
     * @return
     */
    @Bean
    public Binding bindingIndexTask(TopicExchange csmTaskTopicExchange, Queue indexTaskQueue) {
        return BindingBuilder.bind(indexTaskQueue).to(csmTaskTopicExchange).with(RabbitMQConstants.QUEUE_NAME_INDEX_TASK);
    }

}
