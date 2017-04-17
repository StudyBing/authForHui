package com.bing.water.common.utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.util.Properties;

/**
 * Created by xuguobing on 2016/11/17 0017.
 */
public class ConfigUtil {

    private static Properties properties = new Properties();

    static {
        Resource r = new DefaultResourceLoader().getResource("application.properties");
        try {
            properties.load(r.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String getConfig(String key) {
        return properties.getProperty(key);
    }
}
