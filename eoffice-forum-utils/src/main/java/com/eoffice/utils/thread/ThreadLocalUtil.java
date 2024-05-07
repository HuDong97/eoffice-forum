package com.eoffice.utils.thread;


@SuppressWarnings("all")

// 用于管理 ThreadLocal 变量的实用工具类。
public class ThreadLocalUtil {

    // ThreadLocal 对象，用于为每个线程独立存储值
    private static final ThreadLocal THREAD_LOCAL = new ThreadLocal();


    //获取与当前线程执行关联的值。
    public static <T> T get() {
        return (T) THREAD_LOCAL.get();
    }


    // 设置与当前线程执行关联的值。
    public static void set(Object value) {
        THREAD_LOCAL.set(value);
    }


    //清除 ThreadLocal，防止潜在的内存泄漏，它移除与当前线程关联的值。
    public static void remove() {
        THREAD_LOCAL.remove();
    }
}
