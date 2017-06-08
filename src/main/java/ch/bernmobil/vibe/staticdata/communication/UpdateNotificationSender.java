package ch.bernmobil.vibe.staticdata.communication;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Provides functionality so send a message via {@link FanoutExchange} to a RabbitMQ Broker.
 *
 * @author Oliviero Chiodo
 * @author Matteo Patisso
 */
@Component
public class UpdateNotificationSender {
    private final Logger logger = Logger.getLogger(UpdateNotificationSender.class);
    private final RabbitTemplate rabbitTemplate;
    private final FanoutExchange fanoutExchange;

    /**
     * Constructor taking a {@link RabbitTemplate} which serializes objects to send, and a {@link FanoutExchange}
     * to send the notifications.
     * @param rabbitTemplate helper to send messages to a RabbitMQ Broker.
     * @param fanoutExchange where the messages should be delivered.
     */
    @Autowired
    public UpdateNotificationSender(RabbitTemplate rabbitTemplate, FanoutExchange fanoutExchange) {
        this.rabbitTemplate = rabbitTemplate;
        this.fanoutExchange = fanoutExchange;
        logger.debug(String.format("UpdateNotificationSender uses %s as FanoutExchange", fanoutExchange.getName()));
    }

    /**
     * Send a predefined message to the given {@link FanoutExchange}.
     */
    public void sendNotification() {
        logger.info("Send notification update");
        rabbitTemplate.convertAndSend(fanoutExchange.getName(), "", "update successful");
    }
}
