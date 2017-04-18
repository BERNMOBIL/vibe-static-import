package ch.bernmobil.vibe.staticdata.idprovider;

import java.util.concurrent.atomic.AtomicLong;

public class SequentialIdGenerator implements IdGenerator {
    private final AtomicLong currentId = new AtomicLong();

    @Override
    public long getId() {
        return currentId.get();
    }

    @Override
    public long next() {
        return currentId.incrementAndGet();
    }
}
