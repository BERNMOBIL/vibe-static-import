package ch.bernmobil.vibe.staticdata.testenvironment.testdata;

import ch.bernmobil.vibe.shared.entitiy.Route;
import ch.bernmobil.vibe.shared.entitiy.Stop;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StopTestData extends TestData<Stop> {

    public StopTestData() {
        dataSource = IntStream.range(0, idList.length)
                .mapToObj(this::create)
                .collect(Collectors.toList());
    }

    private UUID[] idList = {
            UUID.fromString("92e1a0ef-91ef-4850-baa6-4cb6e243bf95"),
            UUID.fromString("a015ff16-dc7d-4ac8-bd41-9fa7664f8f9b"),
            UUID.fromString("1b50cc76-83be-4aa0-bde9-74fc188a8978"),
            UUID.fromString("86deb4f8-aaa3-4734-a772-1ee38f3e0344")
    };

    private String[] nameList = {
            "Rapperswil",
            "Zürich HB",
            "Bern",
            "Biel/Bienne"
    };

    private UUID[] areaIdList = {
            UUID.fromString("86deb4f8-aaa3-4734-a772-1ee38f3e0344"),
            UUID.fromString("1b50cc76-83be-4aa0-bde9-74fc188a8978"),
            UUID.fromString("a015ff16-dc7d-4ac8-bd41-9fa7664f8f9b"),
            UUID.fromString("92e1a0ef-91ef-4850-baa6-4cb6e243bf95")
    };


    @Override
    protected Stop create(int index) {
        return new Stop(idList[index], nameList[index], areaIdList[index]);
    }
}
