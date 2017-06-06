package ch.bernmobil.vibe.staticdata.processor;

import static org.mockito.Mockito.mock;
import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

import ch.bernmobil.vibe.staticdata.idprovider.UuidGenerator;
import ch.bernmobil.vibe.staticdata.importer.mapping.store.JourneyMapperStore;
import ch.bernmobil.vibe.staticdata.importer.mapping.store.MapperStore;
import ch.bernmobil.vibe.staticdata.importer.mapping.store.StopMapperStore;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;

@Configuration
@Profile("testConfiguration")
public class TestConfiguration {

    @Bean
    @Primary
    @Scope(value = SCOPE_PROTOTYPE)
    public UuidGenerator sequentialIdGenerator() {
        return mock(UuidGenerator.class);
    }

    @Bean
    @Primary
    @Scope(value = SCOPE_PROTOTYPE)
    @SuppressWarnings("unchecked")
    public<I, O> MapperStore<I, O> mapperStore() {
        return mock(MapperStore.class);
    }

    @Bean
    @Primary
    @Scope(value = SCOPE_PROTOTYPE)
    public JourneyMapperStore journeyMapperStore() {
        return mock(JourneyMapperStore.class);
    }

    @Bean
    @Primary
    @Scope(value = SCOPE_PROTOTYPE)
    public StopMapperStore stopMapperStore() {
        return mock(StopMapperStore.class);
    }

}
