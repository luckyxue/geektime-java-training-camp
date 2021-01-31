package io.hancaihaoyun.homework01;

import io.hancaihaoyun.homework01.dynamicproxy.ExampleService;
import io.hancaihaoyun.homework01.dynamicproxy.ExampleServiceImpl;
import io.hancaihaoyun.homework01.dynamicproxy.ProxyHandler;
import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class DynamicProxyTest {

    @Test
    public void test() {
        ExampleServiceImpl exampleService = new ExampleServiceImpl();
        ClassLoader classLoader = exampleService.getClass().getClassLoader();
        Class[] interfaces = exampleService.getClass().getInterfaces();
        InvocationHandler handler = new ProxyHandler(exampleService);

        ExampleService proxy = (ExampleService) Proxy.newProxyInstance(classLoader, interfaces, handler);
        proxy.info();
    }
}
