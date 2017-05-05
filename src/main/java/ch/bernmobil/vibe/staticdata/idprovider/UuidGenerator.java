package ch.bernmobil.vibe.staticdata.idprovider;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class UuidGenerator implements IdGenerator<UUID> {
    private final AtomicReference<UUID> currentUuid = new AtomicReference<>(UUID.randomUUID());
    @Override
    public UUID getId() {
        return currentUuid.get();
    }

    @Override
    public UUID next() {
        return currentUuid.accumulateAndGet(UUID.randomUUID(), (oldId, newId) -> newId);
    }
}
