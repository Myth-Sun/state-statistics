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
        Properties kafkaProperties = new Properties();
        kafkaProperties.put("bootstrap.servers", getProperty("kafka.bootstrap.servers"));
        kafkaProperties.put("zookeeper.connect", getProperty("kafka.zookeeper.connect"));
        kafkaProperties.put("group.id", groupId);
        kafkaProperties.put("auto.offset.reset", getProperty("kafka.auto.offset.reset"));
        kafkaProperties.put("key.deserializer", getProperty("kafka.stringDeserializer"));
        kafkaProperties.put("value.deserializer", getProperty("kafka.stringDeserializer"));
        return kafkaProperties;
    }
}
