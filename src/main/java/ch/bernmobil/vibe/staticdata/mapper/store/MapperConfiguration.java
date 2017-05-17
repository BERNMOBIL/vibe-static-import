package ch.bernmobil.vibe.staticdata.mapper.store;

import ch.bernmobil.vibe.shared.mapping.AreaMapping;
import ch.bernmobil.vibe.shared.mapping.CalendarDateMapping;
import ch.bernmobil.vibe.shared.mapping.RouteMapping;
import ch.bernmobil.vibe.shared.mapping.StopMapping;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfiguration {

    @Bean("areaMapperStore")
    public MapperStore<String, AreaMapping> areaMapperStore() {
        return new MapperStore<>();
    }

    @Bean("calendarDateMapperStore")
    public MapperStore<String, CalendarDateMapping> calendarDateMapperStore() {
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
