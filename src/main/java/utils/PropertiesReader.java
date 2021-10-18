package utils;

import exceptions.PropertyReadingException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PropertiesReader {

    public static Properties properties;

    public static Properties readProperties() {
        FileReader reader = null;

        try {
            reader = new FileReader("configuration.properties");
        } catch (FileNotFoundException fileNotFoundException) {
            throw new PropertyReadingException("File does not exist!");
        }

        Properties props = new Properties();

        try {
            props.load(reader);
        } catch (IOException e) {
            throw new PropertyReadingException("File does not exist!");
        }

        return props;
    }

    public static Properties getProperties() {
        return properties == null ?
                readProperties() :
                properties;
    }
}
