package ch.bernmobil.vibe.staticdata.writer;

import java.util.List;
import java.util.function.Supplier;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.support.ListItemReader;

//TODO: check if ItemStream could replace null check
public class LazyItemReader<T> implements ItemReader<T>, ItemStream{
    private Supplier<List<T>> mappingSupplier;
    private ListItemReader<T> mappingReader;

    public LazyItemReader(Supplier<List<T>> mappingSupplier) {
        this.mappingSupplier = mappingSupplier;
    }

    @Override
    public T read() throws Exception {
        return mappingReader.read();
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        mappingReader = new ListItemReader<>(mappingSupplier.get());
    }

    //TODO: document
    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {

    }

    @Override
    public void close() throws ItemStreamException {

    }
}
