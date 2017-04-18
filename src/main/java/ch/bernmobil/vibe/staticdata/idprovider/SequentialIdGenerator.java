package ch.bernmobil.vibe.staticdata.idprovider;

import java.util.concurrent.atomic.AtomicLong;

public class SequentialIdGenerator implements IdGenerator<Long> {
    private final AtomicLong currentId = new AtomicLong();

    @Override
    public Long getId() {
        return currentId.get();
    }

    @Override
    public Long next() {
        return currentId.incrementAndGet();
    }
}
