package ch.bernmobil.vibe.staticdata.testenvironment.testdata;

import ch.bernmobil.vibe.shared.entity.Schedule;

import java.sql.Time;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ScheduleTestData extends TestData<Schedule> {

    private final UUID[] idList = {
            UUID.fromString("92e1a0ef-91ef-4850-baa6-4cb6e243bf95"),
            UUID.fromString("a015ff16-dc7d-4ac8-bd41-9fa7664f8f9b"),
            UUID.fromString("1b50cc76-83be-4aa0-bde9-74fc188a8978"),
            UUID.fromString("86deb4f8-aaa3-4734-a772-1ee38f3e0344")
    };

    private final String[] platformList = {
            "12",
            "2",
            "4",
            "43"
    };

    private final Time[] plannedArrivalList = {
            Time.valueOf("13:10:00"),
            Time.valueOf("07:32:00"),
            Time.valueOf("09:59:00"),
            Time.valueOf("19:45:00")
    };

    private final Time[] plannedDepartureList = {
            Time.valueOf("13:12:00"),
            Time.valueOf("07:36:00"),
            Time.valueOf("10:00:00"),
            Time.valueOf("19:46:00")
    };
    private final UUID[] stopIdList = {
            UUID.fromString("86deb4f8-aaa3-4734-a772-1ee38f3e0344"),
            UUID.fromString("1b50cc76-83be-4aa0-bde9-74fc188a8978"),
            UUID.fromString("a015ff16-dc7d-4ac8-bd41-9fa7664f8f9b"),
            UUID.fromString("92e1a0ef-91ef-4850-baa6-4cb6e243bf95")
    };
    private final UUID[] journeyIdList = {
            UUID.fromString("86deb4f8-aaa3-4734-a772-1ee38f3e0344"),
            UUID.fromString("1b50cc76-83be-4aa0-bde9-74fc188a8978"),
            UUID.fromString("a015ff16-dc7d-4ac8-bd41-9fa7664f8f9b"),
            UUID.fromString("92e1a0ef-91ef-4850-baa6-4cb6e243bf95")
    };

    public ScheduleTestData() {
        dataSource = IntStream.range(0, idList.length)
                .mapToObj(this::create)
                .collect(Collectors.toList());
    }

    @Override
    protected Schedule create(int index) {
        return new Schedule(idList[index], platformList[index], plannedArrivalList[index], plannedDepartureList[index], stopIdList[index], journeyIdList[index]);
    }
}
