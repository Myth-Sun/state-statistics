package com.sun.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class PropertiesUtil {
    private final static String CONF_NAME = "config.properties";
    private static Properties properties;

    static {
        InputStream inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream(CONF_NAME);
        properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            log.info("加载配置文件失败");
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static Properties getKafkaProperties(String groupId) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", getProperty("bootstrap.servers"));
        properties.put("zookeeper.connect", getProperty("zookeeper.connect"));
        properties.put("group.id", groupId);
        properties.put("auto.offset.reset", "latest");
        properties.put("key.deserializer", getProperty("kafka.stringDeserializer"));
        properties.put("value.deserializer", getProperty("kafka.stringDeserializer"));
        return properties;
    }
}
