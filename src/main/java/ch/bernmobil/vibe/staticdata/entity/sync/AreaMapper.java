package ch.bernmobil.vibe.staticdata.entity.sync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.support.ListItemReader;

public class AreaMapper {
    private static HashMap<String, AreaMapper> mappings = new HashMap<>();

    private String gtfsId;
    private Long id;

    private AreaMapper(String gtfsId, Long id) {
        this.gtfsId = gtfsId;
        this.id = id;
    }

    public String getGtfsId() {
        return gtfsId;
    }

    public Long getId() {
        return id;
    }

    public static void addMapping(String gtfsId, Long id) {
        mappings.put(gtfsId, new AreaMapper(gtfsId, id));
    }

    public static AreaMapper getMappingByStopId(String gtfsId) {
        return mappings.get(gtfsId);
    }

    public static List<AreaMapper> getAll() {
        return new ArrayList<>(mappings.values());
    }

    public static class BatchReader implements ItemReader<AreaMapper> {
        ListItemReader<AreaMapper> reader;

        @Override
        public AreaMapper read() throws Exception{
            if(reader == null) {
                reader = new ListItemReader<>(AreaMapper.getAll());
            }

            return reader.read();
        }
    }
}
