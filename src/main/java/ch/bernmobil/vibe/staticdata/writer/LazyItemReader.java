package ch.bernmobil.vibe.staticdata.writer;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.support.ListItemReader;

import java.util.List;
import java.util.function.Supplier;

public class LazyItemReader<T> implements ItemReader<T> {
    private Supplier<List<T>> mappingSupplier;
    private ListItemReader<T> mappingReader;

    public LazyItemReader(Supplier<List<T>> mappingSupplier) {
        this.mappingSupplier = mappingSupplier;
    }

    @Override
    public T read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if(mappingReader == null) {
            mappingReader = new ListItemReader<>(mappingSupplier.get());
        }
        return mappingReader.read();
    }
}
