package ch.bernmobil.vibe.staticdata.writer;

import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemWriter;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class ZipInputStreamWriter implements ItemWriter<ZipInputStream> {
    private static final Logger logger = Logger.getLogger(ZipInputStreamWriter.class);
    private static final int BUFFER = 1024;
    private static final int OFFSET = 0;

    private final File folder;

    public ZipInputStreamWriter(String folder) {
        this.folder = new File(folder);
    }

    @Override
    public void write(List<? extends ZipInputStream> items) throws Exception {
        if(!folder.exists() && !folder.mkdir()) {
            logger.warn(String.format("Folder for GTFS data could not be created at %s", folder.getAbsolutePath()));
            return;
        }
        logger.debug(String.format("Created folder for GTFS data: %s", folder.getAbsolutePath()));
        for(ZipInputStream stream: items) {
            write(stream);
            stream.close();
        }
    }

    private void write(ZipInputStream zis) throws IOException {
        ZipEntry entry;
        BufferedOutputStream dest = null;
        try {
            while ((entry = zis.getNextEntry()) != null) {
                String filename = entry.getName();
                logger.debug(String.format("Writing file: %s", filename));
                dest = new BufferedOutputStream(new FileOutputStream(folder + "/" + filename), BUFFER);
                copyStream(zis, dest);
                zis.closeEntry();
            }
        } finally {
            if(dest != null) {
                dest.close();
            }
            zis.close();
        }
    }

    private void copyStream(InputStream zis, OutputStream dest) throws IOException {
        byte[] data = new byte[BUFFER];
        int count;
        while ((count = zis.read(data, OFFSET, BUFFER)) != -1) {
            dest.write(data, OFFSET, count);
        }
        dest.close();
    }
}
