package ch.bernmobil.vibe.staticdata.communication;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmqpConfiguration {
    @Value("${bernmobil.amqp.fanout-queue}")
    private String fanoutQueueName;

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(fanoutQueueName);
    }
}
