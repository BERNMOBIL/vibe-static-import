package ch.bernmobil.vibe.staticdata.writer;


import org.springframework.batch.item.ItemWriter;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Provides a {@link ItemWriter} which flattens a nested {@link List} structure and writes it into a wrapped
 * {@link ItemWriter}.
 * @param <T> type of the items which are to be written.
 *
 * @author Oliviero Chiodo
 * @author Matteo Patisso
 */
public class ListUnpackingItemWriter<T> implements ItemWriter<List<T>> {
    private final ItemWriter<T> delegate;

    /**
     * Constructor demanding another {@link ItemWriter} to write the flattened lists into.
     * @param delegate {@link ItemWriter} to write the items.
     */
    public ListUnpackingItemWriter(ItemWriter<T> delegate) {
        this.delegate = delegate;
    }

    /**
     * Flatten and write a set of nested {@link List} into the wrapped {@link ItemWriter}.
     * @param lists which will be written to the wrapped {@link ItemWriter}
     */
    @Override
    public void write(final List<? extends List<T>> lists) throws Exception {
        List<T> flatList = lists
                .stream()
                .flatMap(List::stream)
                .collect(toList());
        delegate.write(flatList);
    }
}
