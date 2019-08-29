package com.deyatech.interview.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * RabbitMQ 直播配置
 *
 * @author ycx
 */
@Configuration
public class RabbitMQLiveConfig {
    public static final String FANOUT_EXCHANGE_LIVE_MESSAGE = "fanout-exchange-live-message";
    public static final String FANOUT_EXCHANGE_LIVE_IMAGE = "fanout-exchange-image";

    public static final String QUEUE_LIVE_MESSAGE = "queue-live-message";
    public static final String QUEUE_LIVE_IMAGE = "queue-live-image";

    public static final String ROUTING_KEY_LIVE_MESSAGE = "routing-key-live-message";
    public static final String ROUTING_KEY_LIVE_IMAGE = "routing-key-live-image";

    /**
     * 交换器
     *
     * @return
     */
    @Bean
    public FanoutExchange fanoutExchangeLiveMessage() {
        return new FanoutExchange(FANOUT_EXCHANGE_LIVE_MESSAGE, true, false);
    }
    @Bean
    public FanoutExchange fanoutExchangeLiveImage() {
        return new FanoutExchange(FANOUT_EXCHANGE_LIVE_IMAGE, true, false);
    }

    /**
     * 队列
     *
     * @return
     */
    @Bean
    public Queue queueLiveMessage() {
        return new Queue(QUEUE_LIVE_MESSAGE, true, false, false);
    }
    @Bean
    public Queue queueLiveImage() {
        return new Queue(QUEUE_LIVE_IMAGE, true, false, false);
    }

    /**
     * 绑定
     *
     * @return
     */
    @Bean
    public Binding bindingLiveMessage() {
        return BindingBuilder.bind(queueLiveMessage()).to(fanoutExchangeLiveMessage());
    }
    @Bean
    public Binding bindingLiveImage() {
        return BindingBuilder.bind(queueLiveImage()).to(fanoutExchangeLiveImage());
    }

}
