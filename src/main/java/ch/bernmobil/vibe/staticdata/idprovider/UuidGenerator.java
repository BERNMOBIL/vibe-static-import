package ch.bernmobil.vibe.staticdata.idprovider;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Provides a generation strategy for {@link UUID}. Using this generator can omit writing
 * to the database and retrieving the id later. The behaviour of the implementing class should never
 * return the same number twice when calling next().
 *
 * @author Oliviero Chiodo
 * @author Matteo Patisso
 */
public class UuidGenerator {
    private UUID currentUuid = UUID.randomUUID();

    /**
     * This method returns the currently saved {@link UUID}. It will return the same value until {@link #next()} is called.
     * @return Currently saved {@link UUID}.
     */
    public UUID getId() {
        return currentUuid;
    }

    /**
     * This method generates a new random {@link UUID} using {@link UUID#randomUUID()} . It is guaranteed that
     * it will not return the same number twice in its lifetime (usual restrictions for randomness in computer systems
     * apply). This method also stores the new id as it can be retrieved again calling {{@link #getId()}}.
     * @return Returns the new generated {@link UUID}
     */
    public UUID next() {
        currentUuid = UUID.randomUUID();
        return currentUuid;
    }
}
