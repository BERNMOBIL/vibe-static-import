package ch.bernmobil.vibe.staticdata;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import net.lingala.zip4j.core.ZipFile;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
@EnableAutoConfiguration
public class StaticImport {
    public static void main(String[] args) {
        //String gtfsDataUrl = "https://wp-test.bernmobil.ch/gtfs/827/static.zip?apikey=b4059f45-9b52-4511-y68f-0fdfd0fa11c1";
        //String destinationPath = "gtfsstatic";
        //downloadGtfsData(gtfsDataUrl, destinationPath);
        SpringApplication.run(StaticImport.class, args);
    }

    public static void copy(InputStream input, OutputStream output, int bufferSize) throws IOException {
        byte[] buf = new byte[bufferSize];
        int n = input.read(buf);
        while (n >= 0) {
            output.write(buf, 0, n);
            n = input.read(buf);
        }
        output.flush();
    }

    private static void downloadGtfsData(String urlString, String destinationPath) {
        try {
            String zipSource = "gtfsStatic.zip";

            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream in = connection.getInputStream();
            FileOutputStream out = new FileOutputStream("gtfsStatic.zip");
            copy(in, out, 1024);
            out.close();

            File destinationFolder = new File(destinationPath);

            String[]entries = destinationFolder.list();
            for(String s: entries){
                File currentFile = new File(destinationFolder.getPath(),s);
                currentFile.delete();
            }

            new ZipFile(zipSource).extractAll(destinationFolder.getPath());

            new File(zipSource).delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
