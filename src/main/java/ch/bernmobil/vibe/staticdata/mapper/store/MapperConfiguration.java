package ch.bernmobil.vibe.staticdata.mapper.store;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MapperConfiguration {

    @Bean
    public<I, O> MapperStore<I, O> mapperStore() {
        return new MapperStore<>();
    }

    @Bean
    @Primary
    public JourneyMapperStore journeyMapperStore() {
        return new JourneyMapperStore();
    }

}
