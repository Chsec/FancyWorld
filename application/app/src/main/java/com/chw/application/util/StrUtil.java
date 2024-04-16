package com.chw.application.util;

import java.util.Objects;
import java.util.UUID;

public class StrUtil {

    /**
     * 获取唯一字符串
     *
     * @return TAG_时间戳
     */
    public static String generalTag() {
        return "TAG_" + UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 获取当前时间戳
     *
     * @return TAG_时间戳
     */
    public static String generalNam() {
        return "IMG_" + System.currentTimeMillis();
    }


    /**
     * 查找字符串元素在字符串数组中的索引，不存在返回-1
     *
     * @param array  需要获取元素索引的字符串数组
     * @param target 需要获取索引的字符串元素
     * @return 索引值
     */
    public static int findIndex(String[] array, String target) {
        for (int i = 0; i < array.length; i++) {
            if (Objects.equals(array[i], target)) {
                return i;
            }
        }
        return -1;
    }
}
