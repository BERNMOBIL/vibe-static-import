package ch.bernmobil.vibe.staticdata.idprovider;

import org.junit.Test;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class UuidGeneratorTest {
    @Test
    public void next() throws Exception {
        UuidGenerator generator = new UuidGenerator();
        UUID first = generator.getId();
        assertThat(first, not(nullValue()));
        UUID second = generator.next();
        assertThat(second, not(first));
        UUID other = generator.getId();
        assertThat(other, is(second));
    }

}