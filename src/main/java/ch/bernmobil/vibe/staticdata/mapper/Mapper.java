package ch.bernmobil.vibe.staticdata.mapper;

import javax.sql.DataSource;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.context.annotation.Bean;

public abstract class Mapper<T> {
    private DataSource dataSource;
    private String preparedStatement;
    private ItemPreparedStatementSetter<T> preparedStatementSetter;

    public Mapper(DataSource dataSource, String preparedStatement,
            ItemPreparedStatementSetter<T> preparedStatementSetter) {
        this.dataSource = dataSource;
        this.preparedStatement = preparedStatement;
        this.preparedStatementSetter = preparedStatementSetter;
    }

    @Bean
    @StepScope
    public abstract ItemReader<T> reader();

    @Bean
    @StepScope
    public JdbcBatchItemWriter<T> writer(){
        JdbcBatchItemWriter<T> writer = new JdbcBatchItemWriter<>();
        writer.setSql(this.preparedStatement);
        writer.setItemPreparedStatementSetter(preparedStatementSetter);
        writer.setDataSource(dataSource);
        return writer;
    }
}
