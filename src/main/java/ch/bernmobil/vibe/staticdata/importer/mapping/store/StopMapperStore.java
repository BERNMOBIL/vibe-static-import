package ch.bernmobil.vibe.staticdata.importer.mapping.store;

import ch.bernmobil.vibe.shared.mapping.StopMapping;

public class StopMapperStore extends MapperStore<String, StopMapping> {
    public StopMapping getStopByStopName(String stopName) {
        return mappingMap.values()
                .parallelStream()
                .filter(s -> s.getName().equals(stopName))
                .findFirst()
                .orElse(null);

    }
}
