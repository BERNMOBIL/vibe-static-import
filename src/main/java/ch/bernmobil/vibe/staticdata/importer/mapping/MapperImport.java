package ch.bernmobil.vibe.staticdata.importer.mapping;

import ch.bernmobil.vibe.staticdata.importer.mapping.store.MapperStore;
import ch.bernmobil.vibe.staticdata.writer.LazyItemReader;
import javax.sql.DataSource;

import org.jooq.Insert;
import org.jooq.Record;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.context.annotation.Bean;

/**
 * Class to summarise common behaviour of all mapping import classes. It holds a {@link DataSource} which is used to
 * save mappings into the database, an {@link ItemPreparedStatementSetter} to write the content of a {@link T} object
 * into a query and a {@link MapperStore} as a temporary storage for all mappings of type {@link T}.
 * @param <T> The type of the Mapping which is written into the database.
 */
public abstract class MapperImport<T> {
    private final DataSource dataSource;
    private final ItemPreparedStatementSetter<T> preparedStatementSetter;
    private final MapperStore<?, T> mapperStore;

    /**
     * Constructor which demands all needed objects to construct {@link ItemReader} and {@link ItemWriter}. A
     * {@link DataSource} which holds a connection to the mapping database must be passed, as well as an {@link ItemPreparedStatementSetter}
     * which is used to build SQL queries. The storage of mappings to save must also be passed.
     * @param dataSource A datasource holding the connection to corresponding mapping database
     * @param preparedStatementSetter The {@link ItemPreparedStatementSetter} which constructs the correct queries to insert
     *                                {@link T} into the database.
     * @param mapperStore The storage of mappings which will be saved into the database.
     */
    public MapperImport(DataSource dataSource,
                        ItemPreparedStatementSetter<T> preparedStatementSetter,
                        MapperStore<?, T> mapperStore) {
        this.dataSource = dataSource;
        this.preparedStatementSetter = preparedStatementSetter;
        this.mapperStore = mapperStore;
    }

    /**
     * Any implementing method must return a query for inserting {@link T} into the database. The resulting query
     * must be compatible with {@link ItemPreparedStatementSetter} so it can fill in the values from {@link T}.
     * @return A JOOQ insert query object which can be used by the {@link JdbcBatchItemWriter} to save {@link T} objects.
     */
    abstract Insert<Record> insertQuery();

    /**
     * A reader for the step which saves all mappings in {@link #mapperStore} into the database. Since the the
     * {@link MapperStore} are filled while processing the GTFS entities, a {@link LazyItemReader} is required to make
     * sure, all the mappings are passed to the reader.
     * @return A {@link LazyItemReader} which holds a reference to the mappings in the {@link #mapperStore}.
     */
    @Bean
    @StepScope
    public ItemReader<T> reader() {
        return new LazyItemReader<>(mapperStore::getMappings);
    }

    /**
     * This method configures and returns a {@link JdbcBatchItemWriter} of {@link T}. The item writer uses the query
     * from {@link #insertQuery()} and the {@link #preparedStatementSetter} to save objects of {@link T} into
     * {@link #dataSource}.
     * @return A fully constructed {@link JdbcBatchItemWriter} which writes {@link T}.
     */
    @Bean
    @StepScope
    public ItemWriter<T> writer() {
        JdbcBatchItemWriter<T> writer = new JdbcBatchItemWriter<>();
        writer.setSql(insertQuery().getSQL());
        writer.setItemPreparedStatementSetter(preparedStatementSetter);
        writer.setDataSource(dataSource);
        return writer;
    }
}
