package ch.bernmobil.vibe.staticdata.processor;

import ch.bernmobil.vibe.staticdata.idprovider.UuidGenerator;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Class which holds an {@link UuidGenerator} which is used by all instances inheriting from this class.
 * @param <I> The type of which is converted by the processor.
 * @param <O> The type in which the processor is converted.
 */
@Component
public abstract class Processor<I, O> implements ItemProcessor<I, O> {
    /**
     * The {@link UuidGenerator} which is used by all instances of {@link Processor}. This generator must return unique
     * ids for every call.
     */
    protected UuidGenerator idGenerator;

    /**
     * Process the provided item, returning a potentially modified or new item for continued
     * processing.  If the returned result is null, it is assumed that processing of the item
     * should not continue.
     * @see org.springframework.batch.item.ItemProcessor
     *
     * @param item to be processed
     * @return potentially modified or new item for continued processing, null if processing of the
     *  provided item should not continue.
     * @throws Exception if there is any error during processing {@link I}.
     */
    @Override
    public abstract O process(I item) throws Exception;

    @Autowired
    public void setIdGenerator(UuidGenerator idGenerator){
        this.idGenerator = idGenerator;
    }

}
