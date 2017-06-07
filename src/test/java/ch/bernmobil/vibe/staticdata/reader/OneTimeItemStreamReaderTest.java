package ch.bernmobil.vibe.staticdata.reader;

import org.junit.Test;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class OneTimeItemStreamReaderTest {

    @Test
    @SuppressWarnings("unchecked")
    public void readOnce() throws Exception {
        ItemStreamReader reader = mock(ItemStreamReader.class);
        String expected = "Hello";
        when(reader.read()).thenReturn(expected);
        OneTimeItemStreamReader<String> otr = new OneTimeItemStreamReader(reader);
        String result = otr.read();
        assertThat(result, is(expected));
        verify(reader, times(1)).read();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void readTwice() throws Exception {
        ItemStreamReader reader = mock(ItemStreamReader.class);
        String expected = "Hello";
        when(reader.read()).thenReturn(expected);
        OneTimeItemStreamReader<String> otr = new OneTimeItemStreamReader<>(reader);
        otr.read();
        String result = otr.read();
        assertThat(result, is(nullValue()));
        verify(reader, times(1)).read();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void open() throws ItemStreamException {
        ItemStreamReader reader = mock(ItemStreamReader.class);
        OneTimeItemStreamReader<String> otr = new OneTimeItemStreamReader<>(reader);
        otr.open(null);
        verify(reader, times(1)).open(any());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void update() throws ItemStreamException {
        ItemStreamReader reader = mock(ItemStreamReader.class);
        OneTimeItemStreamReader<String> otr = new OneTimeItemStreamReader<>(reader);
        otr.update(null);
        verify(reader, times(1)).update(any());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void close() throws ItemStreamException {
        ItemStreamReader reader = mock(ItemStreamReader.class);
        OneTimeItemStreamReader<String> otr = new OneTimeItemStreamReader<>(reader);
        otr.close();
        verify(reader, times(1)).close();
    }
}