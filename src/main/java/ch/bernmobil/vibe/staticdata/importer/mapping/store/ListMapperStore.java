package ch.bernmobil.vibe.staticdata.importer.mapping.store;

import org.apache.commons.collections4.multimap.HashSetValuedHashMap;

import java.util.Set;

public class ListMapperStore<I, O> {
    private HashSetValuedHashMap<I, O> mappings = new HashSetValuedHashMap<>();

    public void addMapping(I id, O mapping) {
        mappings.put(id, mapping);
    }

    public Set<O> getMapping(I id) {
        return mappings.get(id);
    }
}
