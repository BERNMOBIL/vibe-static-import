package ch.bernmobil.vibe.staticdata.idprovider;

import java.util.concurrent.atomic.AtomicLong;

public class IdGenerator {
    private final AtomicLong currentId = new AtomicLong();

    public long getId() {
        return currentId.get();
    }

    public long next() {
        return currentId.incrementAndGet();
    }
}
