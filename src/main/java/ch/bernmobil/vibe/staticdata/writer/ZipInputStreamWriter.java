package ch.bernmobil.vibe.staticdata.writer;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Class writes files from a {@link ZipInputStream} to files on the filesystem.
 *
 * @author Oliviero Chiodo
 * @author Matteo Patisso
 */
public class ZipInputStreamWriter implements ItemStreamWriter<ZipInputStream> {
    private static final Logger logger = Logger.getLogger(ZipInputStreamWriter.class);
    private final File folder;

    /**
     * Constructor demands a folder, where the zipped files will be saved.
     * @param folder where the files will be written.
     */
    public ZipInputStreamWriter(String folder) {
        this.folder = new File(folder);
    }

    /**
     * Writes a {@link List} of {@link ZipInputStream} to a file.
     * @param items which are written to the filesystem.
     * @throws Exception will be thrown if there is a {@link IOException} while writing a file.
     */
    @Override
    public void write(List<? extends ZipInputStream> items) throws Exception {
        for(ZipInputStream stream: items) {
            write(stream);
            stream.close();
        }
    }

    /**
     * Write from the content of a {@link ZipInputStream} to files using {@link FileOutputStream}.
     * @param zis containing the zipped files which are written to the filesystem.
     * @throws IOException if there is an exception while writing the files.
     */
    private void write(ZipInputStream zis) throws IOException {
        ZipEntry entry;
        try {
            while ((entry = zis.getNextEntry()) != null) {
                String filename = entry.getName();
                logger.debug(String.format("Writing file: %s", filename));
                try(FileOutputStream destination = new FileOutputStream(folder + "/" + filename)) {
                    IOUtils.copy(zis, destination);
                }
            }
        } finally {
            zis.close();
        }
    }

    /**
     * Check if the folder exists, or can be created. If it exists it is also checked if the runtime is able to
     * write to the folder.
     * @throws ItemStreamException if the folder is not writable, or does not exists and cannot be created
     */
    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        if(folder.exists() && folder.canWrite()) {
            return;
        }
        if(folder.mkdir()) {
            logger.debug(String.format("Created folder for GTFS data: %s", folder.getAbsolutePath()));
            return;
        }
        throw new ItemStreamException(String.format("Folder for GTFS data could not be created at %s", folder.getAbsolutePath()));
    }

    /**
     * There are no changes to the resources of this class therefore this method does nothing.
     */
    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        // Resources in class never change so this method does nothing.
    }

    /**
     * The class does not need to close resources, therefore this method does nothing.
     */
    @Override
    public void close() throws ItemStreamException {
        // No un-managed resources in this class, so nothing must be explicitly closed.
    }
}
