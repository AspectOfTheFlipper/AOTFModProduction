package dev.icheppy.aotf.config;

import dev.icheppy.aotf.AspectOfTheFlipper;
import net.minecraftforge.fml.common.Loader;

import java.io.*;
import java.util.Properties;

public class Config {

    public String key = "0000000000000000000000000000000000";
    public int minProfit = 1000000;
    public double minROI = 10;
    public int maxPrice = 0;

    public final String configName = "\\AspectOfTheFlipper.cfg";

    public Config() throws IOException {
        File f = new File(
                Loader.instance().getConfigDir() + configName
        );

        if (f.createNewFile()){
            write();
        } else{
            read();
        }
    }

    public void write() throws IOException {


        FileInputStream inputStream = new FileInputStream(
                Loader.instance().getConfigDir() + configName
        );

        Properties properties = new Properties();

        properties.setProperty("key", key);
        properties.setProperty("minProfit", String.valueOf(minProfit));
        properties.setProperty("minROI", String.valueOf(minROI));
        properties.setProperty("maxPrice", String.valueOf(maxPrice));

        inputStream.close();

        File configFile = new File(
                Loader.instance().getConfigDir() + configName
        );

        OutputStream outputStream = new FileOutputStream(configFile);

        properties.store(outputStream, "AOTF config");
    }


    public void read() throws IOException {
        FileInputStream inputStream = new FileInputStream(
                Loader.instance().getConfigDir() + configName
        );
        Properties properties = new Properties();
        properties.load(inputStream);
        System.out.println(properties.getProperty("key"));
        System.out.println(properties.getProperty("key"));
        System.out.println(properties.getProperty("key"));
        System.out.println(properties.getProperty("key"));

        this.key = properties.getProperty("key");
        this.minProfit = Integer.parseInt(properties.getProperty("minProfit"));
        this.minROI = Float.parseFloat(properties.getProperty("minROI"));
        this.maxPrice = Integer.parseInt(properties.getProperty("maxPrice"));

        inputStream.close();
    }


}
