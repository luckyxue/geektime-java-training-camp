package io.hancaihaoyun.java8;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.*;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class GuavaDemo {

    // 创建一个Event Bus总线
    static EventBus bus = new EventBus();

    // Event Bus上注册一个生产者
    static {
        bus.register(new GuavaDemo());
    }


    @SneakyThrows
    public static void main(String[] args) throws IOException {

        List<String> lists = testString();

        List<Integer> list = testList();

        testMap(list);

        testBiMap(lists);

        testEventBus();

    }

    private static void testEventBus() {
        // EventBus
        // SPI+service loader
        // Callback/Listener
        Student student2 = new Student(2, "KK02");
        System.out.println("I want " + student2 + " run now.");
        // 向bus上发布消息
        bus.post(new AEvent(student2));
    }

    private static void testBiMap(List<String> lists) {
        BiMap<String, Integer> words = HashBiMap.create();
        words.put("First", 1);
        words.put("Second", 2);
        words.put("Third", 3);

        System.out.println(words.get("Second").intValue());
        // key 和 value 互换颠倒
        System.out.println(words.inverse().get(3));

        Map<String, String> map1 = Maps.toMap(lists.listIterator(), a -> a + "-value");
        print(map1);
    }

    private static void testMap(List<Integer> list) {
        //Map map = list.stream().collect(Collectors.toMap(a->a,a->(a+1)));
        Multimap<Integer, Integer> bMultimap = ArrayListMultimap.create();
        list.forEach(
                a -> bMultimap.put(a, a + 1)
        );
        print(bMultimap);
    }

    private static List<Integer> testList() {
        // 更强的集合操作
        // 简化 创建
        List<Integer> list = Lists.newArrayList(4, 2, 3, 5, 1, 2, 2, 7, 6);
        List<List<Integer>> list1 = Lists.partition(list, 3);
        print(list1);
        return list;
    }

    private static List<String> testString() {
        // 字符串处理
        List<String> lists = Lists.newArrayList("a", "b", "g", "8", "9");
        String result = Joiner.on(",").join(lists);
        System.out.println(result);
        String test = "34344,34,34,哈哈";
        lists = Splitter.on(",").splitToList(test);
        System.out.println(lists);
        return lists;
    }

    private static void print(Object obj) {
        System.out.println(JSON.toJSONString(obj));
    }

    // 消费者接收到消息然后处理事件
    @Subscribe
    public void handle(AEvent ae) {
        System.out.println(ae.student + " is running.");
    }

    @Data
    @AllArgsConstructor
    public static class AEvent {
        private Student student;
    }
}
