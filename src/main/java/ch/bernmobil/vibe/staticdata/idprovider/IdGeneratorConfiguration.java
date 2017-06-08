package ch.bernmobil.vibe.staticdata.idprovider;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
public class IdGeneratorConfiguration {

    @Bean
    @Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
    public UuidGenerator uuidGenerator() {
        return new UuidGenerator();
    }
}
