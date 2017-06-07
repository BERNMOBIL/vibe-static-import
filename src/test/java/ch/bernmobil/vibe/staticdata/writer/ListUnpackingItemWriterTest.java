package ch.bernmobil.vibe.staticdata.writer;

import org.assertj.core.util.Lists;
import org.junit.Test;
import org.springframework.batch.item.support.ListItemWriter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;


public class ListUnpackingItemWriterTest {
    @Test
    @SuppressWarnings("unchecked")
    public void write() throws Exception {
        List<List<String>> nestedLists = Lists.newArrayList(Collections.nCopies(10, "1"), Collections.nCopies(10, "2"));
        ListItemWriter<String> listWriter = new ListItemWriter<>();
        ListUnpackingItemWriter<String> writer = new ListUnpackingItemWriter<>(listWriter);
        writer.write(nestedLists);
        List<String> result = (List<String>) listWriter.getWrittenItems();
        List<String> expected = new ArrayList<>(20);
        expected.addAll(Collections.nCopies(10, "1"));
        expected.addAll(Collections.nCopies(10, "2"));
        assertThat(result, equalTo(expected));
    }
}