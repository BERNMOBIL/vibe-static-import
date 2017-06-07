package ch.bernmobil.vibe.staticdata.importer.mapping.store;

import ch.bernmobil.vibe.shared.mapping.StopMapping;

/**
 * Provides a {@link StopMapping} specific functionality, to search for a {@link ch.bernmobil.vibe.shared.entitiy.Stop}
 * by its name.
 *
 * @author Oliviero Chiodo
 * @author Matteo Patisso
 */
public class StopMapperStore extends MapperStore<String, StopMapping> {
    /**
     * Find a {@link ch.bernmobil.vibe.shared.entitiy.Stop} by its name, which must be unique.
     * @param stopName of the desired {@link ch.bernmobil.vibe.shared.entitiy.Stop}
     * @return {@link StopMapping} for the stop with the searched stopname.
     */
    public StopMapping getStopByStopName(String stopName) {
        return mappingMap.values()
                .parallelStream()
                .filter(s -> s.getName().equals(stopName))
                .findFirst()
                .orElse(null);

    }
}
