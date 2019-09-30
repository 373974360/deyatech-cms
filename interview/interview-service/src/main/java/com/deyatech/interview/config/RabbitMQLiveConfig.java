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
    public static final String FANOUT_EXCHANGE_LIVE_IMAGE = "fanout-exchange-live-image";

    /**
     * 交换器
     *
     * @return
     */
    @Bean
    public FanoutExchange fanoutExchangeLiveMessage() { return new FanoutExchange(FANOUT_EXCHANGE_LIVE_MESSAGE); }
    @Bean
    public FanoutExchange fanoutExchangeLiveImage() { return new FanoutExchange(FANOUT_EXCHANGE_LIVE_IMAGE); }

    /**
     * 队列
     *
     * @return
     */
    @Bean
    public Queue queueLiveMessage() {
        return new AnonymousQueue();
    }
    @Bean
    public Queue queueLiveImage() {
        return new AnonymousQueue();
    }

    /**
     * 绑定
     *
     * @return
     */
    @Bean
    public Binding bindingLiveMessage() { return BindingBuilder.bind(queueLiveMessage()).to(fanoutExchangeLiveMessage()); }
    @Bean
    public Binding bindingLiveImage() {
        return BindingBuilder.bind(queueLiveImage()).to(fanoutExchangeLiveImage());
    }

}
