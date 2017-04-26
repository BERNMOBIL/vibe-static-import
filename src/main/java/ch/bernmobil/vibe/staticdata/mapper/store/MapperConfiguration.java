package ch.bernmobil.vibe.staticdata.mapper.store;

import ch.bernmobil.vibe.staticdata.mapper.sync.AreaMapping;
import ch.bernmobil.vibe.staticdata.mapper.sync.CalendarDateMapping;
import ch.bernmobil.vibe.staticdata.mapper.sync.RouteMapping;
import ch.bernmobil.vibe.staticdata.mapper.sync.StopMapping;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MapperConfiguration {

    @Bean("areaMapperStore")
    public MapperStore<String, AreaMapping> areaMapperStore() {
        return new MapperStore<>();
    }

    @Bean("calendarDateMapperStore")
    public MapperStore<Long, CalendarDateMapping> calendarDateMapperStore() {
        return new MapperStore<>();
    }

    @Bean("journeyMapperStore")
    public JourneyMapperStore journeyMapperStore() {
        return new JourneyMapperStore();
    }

    @Bean("routeMapperStore")
    public MapperStore<String, RouteMapping> routeMapperStore() {
        return new MapperStore<>();
    }

    @Bean("stopMapperStore")
    public MapperStore<String, StopMapping> stopMapperStore() {
        return new MapperStore<>();
    }

}
