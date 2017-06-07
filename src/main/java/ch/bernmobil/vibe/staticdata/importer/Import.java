package ch.bernmobil.vibe.staticdata.importer;

import org.jooq.Insert;
import org.jooq.Record;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;

import javax.sql.DataSource;

/**
 * This class summarises common fields and methods of every class which defines steps to import GTFS data into the
 * own defined schema. Since very step in the import job requires a reader and a writer which does the same thing,
 * its specific behaviour can be configured by inheriting from this class. It provides a {@link FlatFileItemReader} which
 * reads data from a CSV file and converts the lines into a POJO using a custom {@link FieldSetMapper}. It also implements
 * a {@link JdbcBatchItemWriter} which uses a {@link ItemPreparedStatementSetter} to save the converted entities to the
 * database.
 * @param <TIn> This type should match with a type holding GTFS information @see ch.bernmobil.vibe.staticdata.gtfs.entity.
 * @param <TOut> This type should be an entity which can be saved into the database.
 */
public abstract class Import<TIn, TOut> {
    private final DataSource dataSource;
    private final String[] fieldNames;
    private final String filePath;
    private final FieldSetMapper<TIn> fieldSetMapper;
    private final ItemPreparedStatementSetter<TOut> itemPreparedStatementSetter;

    /**
     * Constructor which demands all common objects from an import configuration.
     * @param dataSource A fully configured datasource to which the converted entities can be written.
     * @param fieldNames An array containing all columns of the CSV file. Basicly just a copy of the first line in the
     *                   corresponding CSV file in a Java array. If there are fewer or more columns in the CSV an exception
     *                   will occur when Spring Batch tries to read from the file.
     * @param filePath An absolute path to the file, which contains the GTFS data to be imported.
     * @param fieldSetMapper A class implementing the {@link FieldSetMapper} to convert every line into a instance of
     *                       {@link TIn}.
     * @param itemPreparedStatementSetter A class implementing {@link ItemPreparedStatementSetter} to fill the insert
     *                                    query with data from an {@link TOut}.
     */
    public Import(DataSource dataSource,
            String[] fieldNames,
            String filePath,
            FieldSetMapper<TIn> fieldSetMapper,
            ItemPreparedStatementSetter<TOut> itemPreparedStatementSetter)
    {
        this.dataSource = dataSource;
        this.fieldNames = fieldNames;
        this.filePath = filePath;
        this.fieldSetMapper = fieldSetMapper;
        this.itemPreparedStatementSetter = itemPreparedStatementSetter;
    }

    /**
     * Any implementing method must return a query for inserting {@link TOut} into the database. The resulting query
     * must be compatible with {@link ItemPreparedStatementSetter} so it can fill in the values from {@link TOut}.
     * @return A JOOQ insert query object which can be used by the {@link JdbcBatchItemWriter} to save {@link TOut} objects.
     */
    abstract Insert<Record> insertQuery();

    /**
     * This method configures and returns a {@link FlatFileItemReader} of {@link TIn}. The item reader reads the file from
     * {@link #filePath}, skips the first line (this is usually the header of the CSV file), sets the column names from
     * {@link #fieldNames} and passes the {@link #fieldSetMapper}.
     * @return A fully constructed {@link FlatFileItemReader} which reads {@link TIn}.
     */
    @Bean
    @StepScope
    public ItemReader<TIn> reader(){
        FlatFileItemReader<TIn> reader = new FlatFileItemReader<>();
        reader.setResource(new FileSystemResource(filePath));
        reader.setLinesToSkip(1);
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames(fieldNames);
        DefaultLineMapper<TIn> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        reader.setLineMapper(lineMapper);
        return reader;
    }

    /**
     * This method configures and returns a {@link JdbcBatchItemWriter} of {@link TOut}. The item writer uses the query
     * from {@link #insertQuery()} and the {@link #itemPreparedStatementSetter} to save objects of {@link TOut} into
     * {@link #dataSource}.
     * @return A fully constructed {@link JdbcBatchItemWriter} which writes {@link TOut}.
     */
    @Bean
    @StepScope
    public ItemWriter<TOut> writer() {
        JdbcBatchItemWriter<TOut> writer = new JdbcBatchItemWriter<>();
        writer.setSql(insertQuery().getSQL());
        writer.setItemPreparedStatementSetter(itemPreparedStatementSetter);
        writer.setDataSource(dataSource);
        return writer;
    }
}
