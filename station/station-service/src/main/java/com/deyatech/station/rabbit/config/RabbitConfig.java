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
     * 索引数据任务队列
     *
     * @return
     */
    @Bean
    public Queue indexTaskQueue() {
        return new Queue(RabbitMQConstants.QUEUE_NAME_INDEX_TASK);
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