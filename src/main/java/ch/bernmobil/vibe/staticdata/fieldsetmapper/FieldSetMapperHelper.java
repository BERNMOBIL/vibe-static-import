package ch.bernmobil.vibe.staticdata.fieldsetmapper;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import org.apache.log4j.Logger;

public class FieldSetMapperHelper {

    public static URL parseUrl(String urlString, Logger logger) {
        URL url = null;
        if(urlString.isEmpty()){
            return url;
        }
        try {
            url = new URL(urlString);
        } catch (MalformedURLException ex) {
            logger.error("URL: \"" + urlString + "\" is not a valid URL. Nested exception [" + ex.getClass().toString() + ": " + ex.getLocalizedMessage() + "]");
        }
        return url;
    }

    public static Optional<Long> parseLong(String longString, Logger logger) {
        Optional<Long> optionalId = Optional.empty();
        if(longString.isEmpty()){
            return optionalId;
        }
        try {
            long id = Long.parseLong(longString);
            optionalId = Optional.of(id);
        } catch (NumberFormatException ex) {
            logger.error("Unparseable number: \"" + longString + "\". Nested exception [" + ex.getClass().toString() + ": " + ex.getLocalizedMessage() + "]");
        }
        return optionalId;
    }

    public static Optional<Integer> parseInteger(String intString, Logger logger){
        Optional<Integer> optionalId = Optional.empty();
        if(intString.isEmpty()){
            return optionalId;
        }
        try {
            int id = Integer.parseInt(intString);
            optionalId = Optional.of(id);
        } catch (NumberFormatException ex) {
            logger.error("Unparseable number: \"" + intString + "\". Nested exception [" + ex.getClass().toString() + ": " + ex.getLocalizedMessage() + "]");
        }
        return optionalId;
    }
}
