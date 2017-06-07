package ch.bernmobil.vibe.staticdata.reader;

import org.springframework.batch.item.ItemReader;

/**
 * An {@link ItemReader} which guarantees that a given resource is only read once.
 * @param <T> type of the resource which will be read.
 *
 * @author Oliviero Chiodo
 * @author Matteo Patisso
 */
public class OneTimeReader<T> implements ItemReader<T> {
    private final ItemReader<T> delegate;
    private boolean read;

    /**
     * Constructor taking another {@link ItemReader} to provide access to the resource.
     * @param delegate {@link ItemReader} which should be used only once.
     */
    public OneTimeReader(ItemReader<T> delegate) {
        this.delegate = delegate;
    }

    /**
     * Reads from the wrapped {@link ItemReader} once. After it has been read it will always return null.
     * @return {@link T} if never has been read from this resource, otherwise null.
     * @throws Exception will be thrown if the wrapped {@link ItemReader} throws an exception or a {@link RuntimeException}
     * occurs.
     */
    @Override
    public T read() throws Exception {
        if(read) {
            return null;
        }
        T item = delegate.read();
        read = true;
        return item;
    }
}
