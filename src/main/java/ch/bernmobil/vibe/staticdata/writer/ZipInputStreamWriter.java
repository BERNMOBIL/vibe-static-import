package ch.bernmobil.vibe.staticdata.writer;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.springframework.batch.item.ItemWriter;


public class ZipInputStreamWriter implements ItemWriter<ZipInputStream> {

    private static final int BUFFER = 1024;
    private static final int OFFSET = 0;

    private String folder;

    public ZipInputStreamWriter(String folder) {
        this.folder = folder;
    }

    @Override
    public void write(List<? extends ZipInputStream> items) throws Exception {
        File gtfsFolder = new File(folder);
        if(!gtfsFolder.mkdir()) {
            return;
        }
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
                int count;
                byte data[] = new byte[BUFFER];
                String filename = entry.getName();
                dest = new BufferedOutputStream(new FileOutputStream(folder + filename), BUFFER);
                while ((count = zis.read(data, OFFSET, BUFFER)) != -1) {
                    dest.write(data, OFFSET, count);
                }
                dest.flush();
                dest.close();
                zis.closeEntry();
            }
        } finally {
            if(dest != null) {
                dest.close();
            }
            zis.close();
        }
    }
}
