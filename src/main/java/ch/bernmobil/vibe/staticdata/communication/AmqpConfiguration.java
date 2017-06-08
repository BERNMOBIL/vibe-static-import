package ch.bernmobil.vibe.staticdata.communication;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Provides configuration for AMQP communication objects.
 */
@Configuration
public class AmqpConfiguration {
    @Value("${bernmobil.amqp.fanout-queue}")
    private String fanoutQueueName;

    /**
     * A {@link FanoutExchange} bound to the name configured in <em>bernmobil.amqp.fanout-queue</em>.
     * @return {@link FanoutExchange} bound to the given.
     */
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(fanoutQueueName);
    }
}
