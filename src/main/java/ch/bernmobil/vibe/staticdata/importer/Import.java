package ch.bernmobil.vibe.staticdata.importer;

import java.util.List;
import javax.sql.DataSource;
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

public abstract class Import<TIn, TOut> {

    private final DataSource dataSource;
    private final String[] fieldNames;
    private final String filePath;
    private final FieldSetMapper<TIn> fieldSetMapper;
    private final String preparedStatementString;
    private final ItemPreparedStatementSetter<TOut> itemPreparedStatementSetter;

    public Import(DataSource dataSource,
            String[] fieldNames,
            String filePath,
            FieldSetMapper<TIn> fieldSetMapper,
            String sqlQuery,
            ItemPreparedStatementSetter<TOut> itemPreparedStatementSetter)
    {
        this.dataSource = dataSource;
        this.fieldNames = fieldNames;
        this.filePath = filePath;
        this.fieldSetMapper = fieldSetMapper;
        this.preparedStatementString = sqlQuery;
        this.itemPreparedStatementSetter = itemPreparedStatementSetter;
    }

    @Bean
    @StepScope
    public ItemReader<TIn> reader(){
        FlatFileItemReader<TIn> reader = new FlatFileItemReader<>();
        reader.setResource(new FileSystemResource(filePath));
        reader.setLinesToSkip(1);
        reader.setLineMapper(new DefaultLineMapper<TIn>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(fieldNames);
            }});
            setFieldSetMapper(fieldSetMapper);
        }});
        return reader;
    }

    @Bean
    @StepScope
    public ItemWriter<TOut> writer() {
        JdbcBatchItemWriter<TOut> writer = new JdbcBatchItemWriter<>();
        writer.setSql(preparedStatementString);
        writer.setItemPreparedStatementSetter(itemPreparedStatementSetter);
        writer.setDataSource(dataSource);
        return writer;
    }
}
