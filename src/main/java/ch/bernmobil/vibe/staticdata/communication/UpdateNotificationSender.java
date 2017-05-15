package ch.bernmobil.vibe.staticdata.communication;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UpdateNotificationSender {
    private final Logger logger = Logger.getLogger(UpdateNotificationSender.class);
    private final RabbitTemplate rabbitTemplate;
    private final FanoutExchange fanoutExchange;

    @Autowired
    public UpdateNotificationSender(RabbitTemplate rabbitTemplate, FanoutExchange fanoutExchange) {
        this.rabbitTemplate = rabbitTemplate;
        this.fanoutExchange = fanoutExchange;
        logger.debug(String.format("UpdateNotificationSender uses %s as FanoutExchange", fanoutExchange.getName()));
    }

    public void sendNotification() {
        logger.info("Send notification update");
        rabbitTemplate.convertAndSend(fanoutExchange.getName(), "", "update successful");
    }
}
