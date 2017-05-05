package ch.bernmobil.vibe.staticdata.processor;

import ch.bernmobil.vibe.staticdata.idprovider.IdGenerator;
import java.util.UUID;
import javax.sql.DataSource;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public abstract class Processor<TIn, TOut> implements ItemProcessor<TIn, TOut> {
    protected IdGenerator<UUID> idGenerator;
    private JdbcTemplate jdbcTemplate;

    @Override
    public abstract TOut process(TIn item) throws Exception;

    @Autowired
    public void setIdGenerator(IdGenerator<UUID> idGenerator){
        this.idGenerator = idGenerator;
    }

    @Autowired
    public void setJdbcTemplate(@Qualifier("PostgresDataSource")DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

}
