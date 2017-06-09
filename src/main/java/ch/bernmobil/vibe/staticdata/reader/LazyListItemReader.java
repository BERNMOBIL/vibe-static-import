package ch.bernmobil.vibe.staticdata.reader;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.support.ListItemReader;

import java.util.List;
import java.util.function.Supplier;

/**
 * Allows to create a {@link ListItemReader} before the containing list is ready to be read from, for example if it is
 * empty or the values are not yet valid. It uses the behaviour of {@link ItemStream} to create the wrapped {@link ListItemReader}
 * when Spring Batch opens the reader, which happens before the step which uses the {@link LazyListItemReader} is started.
 * @param <T> type of the {@link ItemReader} and {@link List}.
 *
 * @see org.springframework.batch.item.ItemReader
 * @see ItemStream
 *
 * @author Oliviero Chiodo
 * @author Matteo Patisso
 */
public class LazyListItemReader<T> implements ItemReader<T>, ItemStream{
    private final Supplier<List<T>> listSupplier;
    private ListItemReader<T> listReader;

    /**
     * Constructor which takes a {@link Supplier} of a {@link List}, which acts as delegate to gain access to the list
     * once it must be read from it.
     * @param listSupplier {@link Supplier} holding a reference to a list which will be read when ready.
     */
    public LazyListItemReader(Supplier<List<T>> listSupplier) {
        this.listSupplier = listSupplier;
    }

    /**
     * Read from the encapsulated {@link List} if it is ready.
     * @return {@link T} of the {@link List}
     * @throws Exception if either the wrapped {@link ListItemReader} throws an exception or a {@link IllegalStateException}
     * if {@link #open(ExecutionContext)} has not been called yet or the {@link ListItemReader} is null.
     */
    @Override
    public T read() throws Exception {
        if(listReader == null) {
            throw new IllegalStateException("Cannot read before instance context has not been opened");
        }
        return listReader.read();
    }

    /**
     * Creates the {@link ListItemReader} with the {@link List} which is encapsulated in the {@link Supplier}.
     */
    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        listReader = new ListItemReader<>(listSupplier.get());
    }

    /**
     * This reader never changes its resources, once its opened, so this method does nothing.
     */
    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        // Resources in class never change so this method does nothing.
    }

    /**
     * No un-managed resources must be closed, so this method does nothing.
     */
    @Override
    public void close() throws ItemStreamException {
        // No un-managed resources in this class, so nothing must be explicitly closed.
    }
}
