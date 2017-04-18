package ch.bernmobil.vibe.staticdata.idprovider;

/**
 * This interface defines methods for a testable ID Generator. Using this generator can omit writing
 * to the database and retrieving the id later. The behaviour of the implementing class should never
 * return the same number twice when calling next().
 * @param <T> T represents the type of the id
 */
public interface IdGenerator<T> {

    /**
     * This method should return the same ID until next is called.
     * @return Returns the buffered id
     */
    T getId();

    /**
     * This method should generate the next id in the sequence of IDs. It must be guaranteed that
     * it will not return the same number twice in its lifetime. This method also stores the new id
     * as it can be retrieved again calling getId().
     * @return Returns the new generated ID
     */
    T next();
}
