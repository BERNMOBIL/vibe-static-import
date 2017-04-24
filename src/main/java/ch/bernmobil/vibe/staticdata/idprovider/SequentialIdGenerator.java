package ch.bernmobil.vibe.staticdata.idprovider;

import java.util.concurrent.atomic.AtomicLong;

public class SequentialIdGenerator implements IdGenerator<Long> {
    private final AtomicLong currentId;

    public SequentialIdGenerator() {
        currentId = new AtomicLong();
    }

    public SequentialIdGenerator(long id) {
        currentId = new AtomicLong(id);
    }

    @Override
    public Long getId() {
        return currentId.get();
    }

    @Override
    public Long next() {
        return currentId.incrementAndGet();
    }
}
