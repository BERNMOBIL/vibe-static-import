package ch.bernmobil.vibe.staticdata.processor;

import ch.bernmobil.vibe.staticdata.QueryBuilder;
import ch.bernmobil.vibe.staticdata.idprovider.SequentialIdGenerator;
import javax.sql.DataSource;
import javax.xml.crypto.Data;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public abstract class Processor<TIn, TOut> implements ItemProcessor<TIn, TOut> {
    private SequentialIdGenerator idGenerator;
    private static JdbcTemplate jdbcTemplate;

    private void loadSequentialIdGenerator(String tableName) {
        String query = new QueryBuilder().Select("max(id)", tableName).getQuery();
        Integer maxId = jdbcTemplate.queryForObject(query, Integer.class);
        this.idGenerator = new SequentialIdGenerator(maxId == null ? 0L : maxId + 1L);
    }

    public SequentialIdGenerator getIdGenerator(String tableName) {
        if(idGenerator == null) {
            loadSequentialIdGenerator(tableName);
        }
        return idGenerator;
    }

    @Override
    public abstract TOut process(TIn item) throws Exception;

    @Autowired
    public void set_JdbcTemplate(@Qualifier("PostgresDataSource")DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

}
