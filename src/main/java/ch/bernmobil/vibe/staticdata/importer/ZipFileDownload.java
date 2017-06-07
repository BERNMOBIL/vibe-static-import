package ch.bernmobil.vibe.staticdata.importer;

import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemReader;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.zip.ZipInputStream;

public class ZipFileDownload implements ItemReader<ZipInputStream> {
    private final Logger logger = Logger.getLogger(ZipFileDownload.class);
    private final String fileSource;

    private boolean firstRun = true;
    private ZipInputStream zip;

    public ZipFileDownload(String fileSource) {
        this.fileSource = fileSource;
    }

    private void downloadZip() throws IOException, URISyntaxException {
        URL url = new URL(fileSource);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        InputStream in = connection.getInputStream();
        zip = new ZipInputStream(new BufferedInputStream(in));
        firstRun = false;
    }

    //TODO: document why return null
    @Override
    public ZipInputStream read() throws Exception {
        if(!firstRun) {
            return null;
        }
        downloadZip();
        logger.debug(String.format("InputStream from ZIP file created from %s", fileSource));
        return zip;
    }

    @Override
    protected void finalize() throws Throwable {
        zip.close();
    }
}
