package ch.bernmobil.vibe.staticdata.writer;


import static java.util.stream.Collectors.toList;

import java.util.List;
import org.springframework.batch.item.ItemWriter;

public class ListUnpackingItemWriter<T> implements ItemWriter<List<T>> {

    private ItemWriter<T> delegate;

    public ListUnpackingItemWriter(ItemWriter<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public void write(final List<? extends List<T>> lists) throws Exception {
        List<T> flatList = lists
                .parallelStream()
                .flatMap(List::stream)
                .collect(toList());
        delegate.write(flatList);
    }
}
