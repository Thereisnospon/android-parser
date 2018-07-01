package com.thereisnospon.util.parse.util;


/**
 * Created by yzr on 2018/6/20.
 */
public class DebugUtils {

    public static boolean DEBUG = true;

    public static void assertTrue(boolean flag) {
        if (DEBUG && !flag) {
            throw new RuntimeException("not true");
        }
    }

    public static int getResId(int packId, int resTypeId, int entryid) {
        return (((packId) << 24) | (((resTypeId) & 0xFF) << 16) | (entryid & 0xFFFF));
    }

    public static void println(Object line) {
        println(line.toString());
    }

    public static void println(String line) {
        System.out.println(line);
    }

}
