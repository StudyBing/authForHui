package com.bing.water.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.bing.water.Application;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by xuguobing on 2016/3/4.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class BaseTest {

    protected static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    public BaseTest() {
        System.setProperty("spring.profiles.active", "de");
    }
}
