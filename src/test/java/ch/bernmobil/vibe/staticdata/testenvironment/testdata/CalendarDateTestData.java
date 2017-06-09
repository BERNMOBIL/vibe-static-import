package ch.bernmobil.vibe.staticdata.testenvironment.testdata;

import ch.bernmobil.vibe.shared.entity.CalendarDate;
import com.google.gson.JsonObject;

import java.sql.Date;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CalendarDateTestData extends TestData<CalendarDate> {

    private final UUID[] idList = {
            UUID.fromString("92e1a0ef-91ef-4850-baa6-4cb6e243bf95"),
            UUID.fromString("a015ff16-dc7d-4ac8-bd41-9fa7664f8f9b"),
            UUID.fromString("1b50cc76-83be-4aa0-bde9-74fc188a8978"),
            UUID.fromString("86deb4f8-aaa3-4734-a772-1ee38f3e0344")
    };

    private final Date[] validFromList = {
            Date.valueOf("2017-06-07"),
            Date.valueOf("2017-05-31"),
            Date.valueOf("2017-02-28"),
            Date.valueOf("2016-08-10")
    };

    private final Date[] validUntilList = {
            Date.valueOf("2017-06-16"),
            Date.valueOf("2017-10-01"),
            Date.valueOf("2017-07-12"),
            Date.valueOf("2017-01-29")
    };

    private final UUID[] journeyIdList = {
            UUID.fromString("86deb4f8-aaa3-4734-a772-1ee38f3e0344"),
            UUID.fromString("1b50cc76-83be-4aa0-bde9-74fc188a8978"),
            UUID.fromString("a015ff16-dc7d-4ac8-bd41-9fa7664f8f9b"),
            UUID.fromString("92e1a0ef-91ef-4850-baa6-4cb6e243bf95")
    };

    public CalendarDateTestData() {
        dataSource = IntStream.range(0, idList.length)
                .mapToObj(this::create)
                .collect(Collectors.toList());
    }

    private final JsonObject[] daysList = {
            new JsonObject(),
            new JsonObject(),
            new JsonObject(),
            new JsonObject()
    };

    @Override
    protected CalendarDate create(int index) {
        return new CalendarDate(idList[index], validFromList[index], validUntilList[index], journeyIdList[index], daysList[index]);
    }
}
