package io.hancaihaoyun.homework01;

import io.hancaihaoyun.homework01.auto.AutoWiringExample;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

// 自动注入测试需要添加注解
/**
 * JUnit 4
 *
 * @RunWith(SpringRunner.class)
 * @SpringBootTest public class ApplicationTests {
 * @Test public void contextLoads() {
 * }
 * }
 */

/**
 * JUnit 5
 * @ExtendWith(SpringExtension.class)
 * @SpringBootTest
 * public class ApplicationTests {
 *     @Test
 *     public void contextLoads() {
 *     }
 * }
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = AutoWiringExample.class)
public class AutoWiringExampleTest {

    @Autowired
    private AutoWiringExample autoWiringExample;

    @Test
    public void test() {
        autoWiringExample.info();
    }
}
