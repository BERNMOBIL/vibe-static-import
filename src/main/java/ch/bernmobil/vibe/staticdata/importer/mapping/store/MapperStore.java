package ch.bernmobil.vibe.staticdata.importer.mapping.store;

import org.springframework.dao.DuplicateKeyException;

import java.util.*;

/**
 * A mapper store to save mappings between two ID systems.
 * @param <I> An object which acts as search index
 * @param <O> An object containing mapping information which also may include {@link I}
 */

public class MapperStore<I, O> {
    protected final Map<I, O> mappingMap = new HashMap<>();

    /**
     * Add a mapping object to the store.
     * @param id which leads to the mapping.
     * @param mapping as value for the id.
     * @throws DuplicateKeyException if the id already exists in the mapper store
     */
    public void addMapping(I id, O mapping) throws DuplicateKeyException{
        if(mappingMap.containsKey(id)) {
            throw new DuplicateKeyException(String.format("%s already exists in mapper store", id));
        }
        mappingMap.put(id, mapping);
    }

    /**
     * Get a mapping to a given id of type {@link I}.
     * @param id of which the mapping is searched.
     * @return mapping corresponding to the given id.
     */
    public O getMapping(I id) {
        return mappingMap.get(id);
    }

    /**
     * Get all mapping objects as an UnmodifiableList of {@link O}
     * @return UnmodifiableList of all stored {@link O}
     */
    public List<O> getMappings() {
        return Collections.unmodifiableList(new ArrayList<>(mappingMap.values()));
    }
}
