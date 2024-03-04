package tests.fileReader;

import org.testng.annotations.Test;

import java.io.File;

import java.io.FileInputStream;

import java.io.FileNotFoundException;

import java.io.IOException;

import java.util.Properties;

public class  ConfigFileReader {

    private Properties properties;

    private final String configFilePath= "config//config.properties";

    public ConfigFileReader() {

        File  ConfigFile=new File(configFilePath);

        try {

            FileInputStream configFileReader=new FileInputStream(ConfigFile);

            properties = new Properties();

            try {

                properties.load(configFileReader);

                configFileReader.close();

            } catch (IOException e)

            {

                System.out.println(e.getMessage());

            }

        }  catch (FileNotFoundException e)

        {

            System.out.println(e.getMessage());
            throw new RuntimeException("config.properties not found at config path " + configFilePath);

        }

    }

    public String getApplicationUrl() {

        String applicationurl= properties.getProperty("url.base");

        if(applicationurl != null)

            return applicationurl;

        else

            throw new RuntimeException("Application URL not found");

    }

    public String getToken() {

        String username= properties.getProperty("bearer.token");


        if(username != null)

            return username;

        else

            throw new RuntimeException("Bearer token not found");

    }

    @Test
    public void testConfigFileReader() {
        ConfigFileReader reader = new ConfigFileReader();

        String applicationUrl = reader.getApplicationUrl();
        String token = reader.getToken();

        System.out.println("Application URL: " + applicationUrl);
        System.out.println("Bearer Token: " + token);
    }
}
