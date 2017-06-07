package ch.bernmobil.vibe.staticdata.importer.mapping.store;

import ch.bernmobil.vibe.shared.mapping.JourneyMapping;
import ch.bernmobil.vibe.staticdata.testenvironment.testdata.mapping.JourneyMappingTestData;
import ch.bernmobil.vibe.staticdata.testenvironment.testdata.TestData;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class JourneyMapperStoreTest {
    @Test
    public void addMappingNonExistingKey() {
        TestData<JourneyMapping> testData = new JourneyMappingTestData();
        JourneyMapping journeyMapping = testData.get(0);

        JourneyMapperStore store = new JourneyMapperStore();
        store.addMapping(journeyMapping.getGtfsTripId(), journeyMapping);

        List<JourneyMapping> list = store.getMappings();

        assertThat(list, hasSize(1));
        assertThat(list, contains(journeyMapping));
    }

    @Test
    public void addMappingExistingKey() {
        TestData<JourneyMapping> testData = new JourneyMappingTestData();
        List<JourneyMapping> mappings = testData.getDataSource().stream().limit(2).collect(Collectors.toList());

        JourneyMapperStore store = new JourneyMapperStore();

        for (JourneyMapping mapping : mappings) {
            store.addMapping(mapping.getGtfsTripId(), mapping);
        }

        List<JourneyMapping> list = store.getMappings();

        assertThat(list, hasSize(2));
        assertThat(list, equalTo(mappings));
    }

    @Test
    public void getMappingsByServiceId() {
        TestData<JourneyMapping> testData = new JourneyMappingTestData();
        List<JourneyMapping> mappings = testData.getDataSource();
        String gtfsServiceId = mappings.get(0).getGtfsServiceId();

        JourneyMapperStore store = new JourneyMapperStore();

        for (JourneyMapping mapping : mappings) {
            store.addMapping(mapping.getGtfsTripId(), mapping);
        }

        List<JourneyMapping> list = store.getMappingsByServiceId(gtfsServiceId);
        List<JourneyMapping> expected = mappings.stream()
                .filter(m -> m.getGtfsServiceId().equals(gtfsServiceId))
                .collect(Collectors.toList());

        assertThat(list, hasSize(2));
        assertThat(list, equalTo(expected));
    }

}