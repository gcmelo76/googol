package com.common;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {
    
    /** 
     * @param field
     * @return String
     */
    public static String read(String field){
        try (InputStream input = new FileInputStream("../config.properties")) {
            if (input == null){
                System.err.println("File not found");
                return null;
            }

            Properties prop = new Properties();
            prop.load(input);

            if (prop.getProperty(field) != null) {
                System.out.println("Got property: " + prop.getProperty(field));
                return prop.getProperty(field);
            }else {
                System.err.println("Unable to find property");
                return null;
            }

        } catch (Exception e) {
            System.err.println("Error reading file" + e.getMessage());
            return null;
        }
    }
}
