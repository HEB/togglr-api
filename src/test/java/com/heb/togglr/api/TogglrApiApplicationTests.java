package com.heb.togglr.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@Profile({"test"})
@ComponentScan({"com.heb.togglr"})
@SpringBootTest
public class TogglrApiApplicationTests {

    @Test
    public void contextLoads() {
    }

}
