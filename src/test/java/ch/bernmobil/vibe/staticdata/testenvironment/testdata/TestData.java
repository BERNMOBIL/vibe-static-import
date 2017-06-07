package ch.bernmobil.vibe.staticdata.testenvironment.testdata;

import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

public abstract class TestData<T> implements Iterable<T> {
    protected List<T> dataSource;

    protected abstract T create(int index);

    public T get(int index) {
        return dataSource.get(index);
    }

    public List<T> getDataSource() {
        return dataSource;
    }

    @Override
    public Iterator<T> iterator() {
        return dataSource.iterator();
    }


    @Override
    public void forEach(Consumer<? super T> action) {
        dataSource.forEach(action);
    }


    @Override
    public Spliterator<T> spliterator() {
        return dataSource.spliterator();
    }
}
