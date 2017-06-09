package ch.bernmobil.vibe.staticdata.testenvironment.testdata;

import ch.bernmobil.vibe.shared.entity.Route;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RouteTestData extends TestData<Route> {

    public RouteTestData() {
        dataSource = IntStream.range(0, idList.length)
                .mapToObj(this::create)
                .collect(Collectors.toList());
    }

    private final UUID[] idList = {
            UUID.fromString("92e1a0ef-91ef-4850-baa6-4cb6e243bf95"),
            UUID.fromString("a015ff16-dc7d-4ac8-bd41-9fa7664f8f9b"),
            UUID.fromString("1b50cc76-83be-4aa0-bde9-74fc188a8978"),
            UUID.fromString("86deb4f8-aaa3-4734-a772-1ee38f3e0344")
    };

    private final int[] typeList = {
            3,
            2,
            3,
            4
    };

    private final String[] lineList = {
            "M12",
            "1",
            "995",
            "8"
    };


    @Override
    protected Route create(int index) {
        return new Route(idList[index], typeList[index], lineList[index]);
    }
}
