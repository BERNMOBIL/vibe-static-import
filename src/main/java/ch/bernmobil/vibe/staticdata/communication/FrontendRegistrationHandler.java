package ch.bernmobil.vibe.staticdata.communication;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.log4j.Logger;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FrontendRegistrationHandler {
    private FanoutExchange fanoutExchange;
    private static Logger logger = Logger.getLogger(FrontendRegistrationHandler.class);

    @Autowired
    public FrontendRegistrationHandler(FanoutExchange fanoutExchange) {
        this.fanoutExchange = fanoutExchange;
    }

    @RabbitListener(queues = "#{registrationQueue.name}")
    public void recieve(String message, int reciever) {
        logger.debug(String.format("Message from %s recieved: %s", reciever, message));
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(message).getAsJsonObject();
        RegistrationMessage registration = new Gson().fromJson(json, RegistrationMessage.class);
        if(registration.action == RegistrationMessage.Action.REGISTER){
            logger.info(String.format("Registered %s to %s", reciever, registration.queueName));
            registerQueue(registration.queueName);
        }
    }

    private void registerQueue(String queueName) {
        Queue queue = new Queue(queueName);
        BindingBuilder.bind(queue).to(fanoutExchange);
    }

    private static class RegistrationMessage {
        private enum Action { REGISTER }
        private Action action;
        private String queueName;

        public RegistrationMessage(String queueName, Action action) {
            this.queueName = queueName;
            this.action = action;
        }
    }
}
