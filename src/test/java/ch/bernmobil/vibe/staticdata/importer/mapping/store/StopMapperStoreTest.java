package ch.bernmobil.vibe.staticdata.importer.mapping.store;

import ch.bernmobil.vibe.shared.mapping.StopMapping;
import ch.bernmobil.vibe.staticdata.testenvironment.testdata.mapping.StopMappingTestData;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class StopMapperStoreTest {
    @Test
    public void getByStopName() {
        StopMappingTestData testData = new StopMappingTestData();

        StopMapperStore store = new StopMapperStore();
        for(StopMapping mapping: testData) {
            store.addMapping(mapping.getGtfsId(), mapping);
        }

        StopMapping expected = testData.get(0);
        StopMapping result = store.getStopByStopName(expected.getName());

        assertThat(result, is(expected));
    }
}