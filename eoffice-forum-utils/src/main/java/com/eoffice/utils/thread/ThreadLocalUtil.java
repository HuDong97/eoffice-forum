package com.eoffice.utils.thread;


import java.util.Map;

@SuppressWarnings("all")

// 用于管理 ThreadLocal 变量的实用工具类。
public class ThreadLocalUtil {

    // ThreadLocal 对象，用于为每个线程独立存储值
    private static final ThreadLocal<Map<String, Object>> THREAD_LOCAL = new ThreadLocal<>();


    //获取与当前线程执行关联的值,从线程中获取。
    public static <T> T getUser(String key) {
        Map<String, Object> map = THREAD_LOCAL.get();
        return map != null ? (T) map.get(key) : null;
    }


    // 设置与当前线程执行关联的值,存入线程中。
    public static void setUser(Map<String, Object> value) {
        THREAD_LOCAL.set(value);
    }


    //清除 ThreadLocal，防止潜在的内存泄漏，它移除与当前线程关联的值。
    public static void remove() {
        THREAD_LOCAL.remove();
    }
}
