package ch.bernmobil.vibe.staticdata.processor;

import ch.bernmobil.vibe.staticdata.entity.Area;
import ch.bernmobil.vibe.staticdata.gtfsmodel.GtfsStop;
import ch.bernmobil.vibe.staticdata.idprovider.SequentialIdGenerator;
import ch.bernmobil.vibe.staticdata.importer.AreaImport;
import ch.bernmobil.vibe.staticdata.mapper.sync.AreaMapper;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class AreaProcessor extends Processor<GtfsStop, Area> {

    @Override
    public Area process(GtfsStop item) throws Exception {
        SequentialIdGenerator idGenerator = getIdGenerator(AreaImport.getTableName());
        String name = item.getStopName();
        String parentStation = item.getParentStation();
        if(parentStation.isEmpty()) {
            Area area = new Area(idGenerator.getId(), name);
            AreaMapper.addMapping(item.getStopId(), idGenerator.getId());
            idGenerator.next();
            return area;
        }
        return null;
    }



}