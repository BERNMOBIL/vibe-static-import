package ch.bernmobil.vibe.staticdata.importer.mapping.store;

import ch.bernmobil.vibe.shared.mapping.AreaMapping;
import ch.bernmobil.vibe.shared.mapping.CalendarDateMapping;
import ch.bernmobil.vibe.shared.mapping.RouteMapping;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for {@link MapperStore} and its extensions. Because Java uses type erasure it is necessary to
 * define a bean for all combination of generic parameters.
 *
 * @author Oliviero Chiodo
 * @author Matteo Patisso
 */
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
    public StopMapperStore stopMapperStore() {
        return new StopMapperStore();
    }

    @Bean("calendarListMapperStore")
    public ListMapperStore<String, CalendarDateMapping> calendarListMapperStore() {
        return new ListMapperStore<>();
    }

}
