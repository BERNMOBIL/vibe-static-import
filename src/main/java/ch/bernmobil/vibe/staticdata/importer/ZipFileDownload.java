package ch.bernmobil.vibe.staticdata.importer;

import org.apache.log4j.Logger;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.zip.ZipInputStream;

/**
 * Class provides an {@link ItemReader} which downloads a ZIP file from an URL. It is used to download the static GTFS
 * data ZIP, which is downloaded once for each execution of the import job.
 *
 * @see ItemStream
 *
 * @author Oliviero Chiodo
 * @author Matteo Patisso
 */
public class ZipFileDownload implements ItemReader<ZipInputStream>, ItemStream {
    private final Logger logger = Logger.getLogger(ZipFileDownload.class);
    private final String fileSource;
    private ZipInputStream zip;

    /**
     * This class needs a source to download the ZIP file.
     * @param fileSource which determines where to download the ZIP from. This {@link String} must be a valid
     *                   HTTP URL.
     */
    public ZipFileDownload(String fileSource) {
        this.fileSource = fileSource;
    }

    private void downloadZip() throws IOException, URISyntaxException {
        URL url = new URL(fileSource);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        InputStream in = connection.getInputStream();
        zip = new ZipInputStream(new BufferedInputStream(in));
    }

    /**
     * Returns the {@link ZipInputStream} which contains the data of the given {@link #fileSource}.
     * @return {@link ZipInputStream} with a ZIP file.
     * @throws Exception if there is a {@link RuntimeException} while reading.
     *
     * @see ItemReader
     */
    @Override
    public ZipInputStream read() throws Exception {
        logger.debug(String.format("InputStream from ZIP file created from %s", fileSource));
        return zip;
    }

    /**
     * Download the file before the step, which uses this reader, starts to read.
     * @throws ItemStreamException will be thrown if an {@link IOException} or {@link URISyntaxException} occurs when
     * trying to download the ZIP file.
     *
     * @see ItemStream
     */
    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        try {
            downloadZip();
        } catch (IOException | URISyntaxException e) {
            throw new ItemStreamException(e);
        }
    }

    /**
     * Method provides behaviour after a new chunk of data will be processed. This class never updates or changes
     * any resources, so nothing happens in this method.
     *
     * @see ItemStream
     */
    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {

    }

    /**
     * Close all resources of this class after ending the step, in which this reader is used.
     * @throws ItemStreamException will be thrown if there occurs an {@link IOException} while closing the resources.
     *
     * @see ItemStream
     */
    @Override
    public void close() throws ItemStreamException {
        try {
            zip.close();
        } catch (IOException e) {
            throw new ItemStreamException(e);
        }
    }

}
