package ch.bernmobil.vibe.staticdata.idprovider;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
public class IdGeneratorConfiguration {
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    // Because each instance of an ID Generator should be independent
    public SequentialIdGenerator sequentialIdGenerator() {
        return new SequentialIdGenerator();
    }
}
