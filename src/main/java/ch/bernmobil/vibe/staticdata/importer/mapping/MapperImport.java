package ch.bernmobil.vibe.staticdata.importer.mapping;

import ch.bernmobil.vibe.staticdata.importer.mapping.store.MapperStore;
import ch.bernmobil.vibe.staticdata.writer.LazyItemReader;
import javax.sql.DataSource;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.context.annotation.Bean;

/**
 *
 * @param <T>
 */
public abstract class MapperImport<T> {
    private final DataSource dataSource;
    private final String preparedStatement;
    private final ItemPreparedStatementSetter<T> preparedStatementSetter;
    private final MapperStore<?, T> mapperStore;

    public MapperImport(DataSource dataSource,
            String preparedStatement,
            ItemPreparedStatementSetter<T> preparedStatementSetter,
            MapperStore<?, T> mapperStore) {
        this.dataSource = dataSource;
        this.preparedStatement = preparedStatement;
        this.preparedStatementSetter = preparedStatementSetter;
        this.mapperStore = mapperStore;
    }

    @Bean
    @StepScope
    public ItemReader<T> reader() {
        return new LazyItemReader<>(mapperStore::getMappings);
    }

    @Bean
    @StepScope
    public ItemWriter<T> writer() {
        JdbcBatchItemWriter<T> writer = new JdbcBatchItemWriter<>();
        writer.setSql(this.preparedStatement);
        writer.setItemPreparedStatementSetter(preparedStatementSetter);
        writer.setDataSource(dataSource);
        return writer;
    }
}
