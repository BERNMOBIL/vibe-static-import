package ch.bernmobil.vibe.staticdata.communication;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqConfiguration {
    private Environment environment;

    @Autowired
    public RabbitMqConfiguration(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public Queue registrationQueue() {
        return new Queue(environment.getProperty("bernmobil.amqp.registration-queue"));
    }

    @Bean
    @Autowired
    public UpdateNotificationSender updateNotificationSender(RabbitTemplate rabbitTemplate, FanoutExchange fanoutExchange) {
        return new UpdateNotificationSender(rabbitTemplate, fanoutExchange);
    }
}
