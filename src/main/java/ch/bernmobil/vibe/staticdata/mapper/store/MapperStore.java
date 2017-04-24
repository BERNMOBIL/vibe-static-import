package ch.bernmobil.vibe.staticdata.mapper.store;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * A mapper store to save mappings between two ID systems
 * @param <I> Id which should be act as search index
 * @param <O> An object containing the mapping information
 */

@Component
public class MapperStore<I, O> {
    protected final Map<I, O> mappingMap = new HashMap<>();

    public void addMapping(I id, O mapping) {
        mappingMap.put(id, mapping);
    }

    public O getMapping(I id) {
        return mappingMap.get(id);
    }

    public List<O> getMappings() {
        return Collections.unmodifiableList(new ArrayList<>(mappingMap.values()));
    }
}
