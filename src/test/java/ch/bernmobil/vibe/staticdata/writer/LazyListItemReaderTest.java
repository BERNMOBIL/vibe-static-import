package ch.bernmobil.vibe.staticdata.writer;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class LazyListItemReaderTest {
    @Test
    public void readAfterOpen() throws Exception {
        String expected = "Hello";
        List<String> list = Collections.nCopies(10, expected);
        LazyListItemReader<String> reader = new LazyListItemReader<>(() -> list);
        reader.open(null);
        List<String> result = new ArrayList<>(10);
        String temp;
        while((temp = reader.read()) != null) {
            result.add(temp);
        }
        assertThat(result, equalTo(list));
    }

    @Test(expected = IllegalStateException.class)
    public void readBeforeOpen() throws Exception {
        String expected = "Hello";
        List<String> list = Collections.nCopies(10, expected);
        LazyListItemReader<String> reader = new LazyListItemReader<>(() -> list);
        reader.read();
    }
}