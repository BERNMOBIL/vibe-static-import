package ch.bernmobil.vibe.staticdata.processor;

import ch.bernmobil.vibe.staticdata.idprovider.IdGenerator;
import java.util.UUID;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class Processor<TIn, TOut> implements ItemProcessor<TIn, TOut> {
    protected IdGenerator<UUID> idGenerator;

    @Override
    public abstract TOut process(TIn item) throws Exception;

    @Autowired
    public void setIdGenerator(IdGenerator<UUID> idGenerator){
        this.idGenerator = idGenerator;
    }

}
