package com.deyatech.interview.config;

import com.deyatech.interview.vo.LiveImageVo;
import com.deyatech.interview.vo.LiveMessageVo;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

/**
 * 直播消息处理
 *
 * @author ycx
 */
@Component
public class RabbitMQLiveHandler {
    private final String TOPIC_LIVE_MESSAGE = "/topic/live/message/";
    private final String TOPIC_LIVE_IMAGE = "/topic/live/image/";

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @RabbitListener(queues = RabbitMQLiveConfig.QUEUE_LIVE_MESSAGE)
    public void handlerLiveMessage(LiveMessageVo liveMessageVo) {
        messagingTemplate.convertAndSend(TOPIC_LIVE_MESSAGE, liveMessageVo);
    }

    @RabbitListener(queues = RabbitMQLiveConfig.QUEUE_LIVE_IMAGE)
    public void handlerLiveImage(LiveImageVo liveImageVo) {
        messagingTemplate.convertAndSend(TOPIC_LIVE_IMAGE, liveImageVo);
    }
}
