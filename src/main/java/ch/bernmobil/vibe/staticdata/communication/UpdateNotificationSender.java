package ch.bernmobil.vibe.staticdata.communication;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.apache.log4j.Logger;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UpdateNotificationSender {
    private static Logger logger = Logger.getLogger(UpdateNotificationSender.class);
    private RabbitTemplate rabbitTemplate;
    private FanoutExchange fanoutExchange;

    @Autowired
    public UpdateNotificationSender(RabbitTemplate rabbitTemplate, FanoutExchange fanoutExchange) {
        this.rabbitTemplate = rabbitTemplate;
        this.fanoutExchange = fanoutExchange;
    }

    public void send(String newUpdateString) {
        JsonObject json = new JsonObject();
        json.add("update", new JsonPrimitive(newUpdateString));
        logger.info(String.format("Sending update string: %s", json.toString()));
        rabbitTemplate.convertAndSend(fanoutExchange.getName(), json);
    }
}
