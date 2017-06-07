package ch.bernmobil.vibe.staticdata.reader;

import org.junit.Test;
import org.springframework.batch.item.ItemReader;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class OneTimeReaderTest {

    @Test
    @SuppressWarnings("unchecked")
    public void readOnce() throws Exception {
        ItemReader reader = mock(ItemReader.class);
        String expected = "Hello";
        when(reader.read()).thenReturn(expected);
        OneTimeReader<String> otr = new OneTimeReader(reader);
        String result = otr.read();
        assertThat(result, is(expected));
        verify(reader, times(1)).read();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void readTwice() throws Exception {
        ItemReader reader = mock(ItemReader.class);
        String expected = "Hello";
        when(reader.read()).thenReturn(expected);
        OneTimeReader<String> otr = new OneTimeReader<>(reader);
        otr.read();
        String result = otr.read();
        assertThat(result, is(nullValue()));
        verify(reader, times(1)).read();
    }
}