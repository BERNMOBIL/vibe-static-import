package ch.bernmobil.vibe.staticdata.importer.mapping.store;

import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.Is.isA;
import static org.junit.Assert.*;


public class MapperStoreTest {

    @Test
    public void addMapping() {
        MapperStore<Integer, String> mapperStore = new MapperStore<>();
        mapperStore.addMapping(1, "1");

        List<String> list = mapperStore.getMappings();

        assertThat(list, hasSize(1));
        assertThat(list.get(0), is("1"));
    }

    @Test
    public void getMapping() {
        MapperStore<Integer, String> mapperStore = new MapperStore<>();
        mapperStore.addMapping(1, "1");

        String result = mapperStore.getMapping(1);

        assertThat(result, is("1"));
    }

    @Test
    public void getAllMappings() {
        MapperStore<Integer, String> mapperStore = new MapperStore<>();
        for (int i = 0; i < 10; i++) {
            mapperStore.addMapping(i, Integer.toString(i));
        }
        List<String> list = mapperStore.getMappings();

        assertThat(list, hasSize(10));
        List<String> expected = IntStream.range(0, 10).mapToObj(Integer::toString).collect(Collectors.toList());
        assertThat(list, equalTo(expected));
   }

}