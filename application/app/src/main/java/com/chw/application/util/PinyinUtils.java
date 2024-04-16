package com.chw.application.util;

import androidx.annotation.NonNull;
import com.github.stuxuhai.jpinyin.ChineseHelper;
import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;

/**
 * 拼音工具
 */
public class PinyinUtils {

    /**
     * 若传入字符串为数字，则以“#+数字”作为标志。
     * 若传入字符串为汉字，则以汉字拼音首字母大写作为标志。
     * 若传入字符串为英文，则以英文首字母大写作为标志。
     * 若传入字符串为等其他特殊符号，则以#作为标志。
     *
     * @param hanzi 待转换字符串
     * @return 字符串字符串大写全拼
     * @throws PinyinException 发生异常
     */
    public static String getSortLetter(@NonNull String hanzi) throws PinyinException {
        if (!hanzi.isEmpty()) {
            char firstChar = hanzi.charAt(0);
            if (java.lang.Character.isUpperCase(firstChar) || java.lang.Character.isLowerCase(firstChar)) {
                // 处理英文，使用isLetter方法会将中文判断成字母
                return java.lang.Character.toString(firstChar).toUpperCase();
            } else if (ChineseHelper.isChinese(firstChar)) {
                // 处理汉字
                String pinyin = PinyinHelper.convertToPinyinString(hanzi.substring(0, 1), "", PinyinFormat.WITHOUT_TONE);
                return pinyin.substring(0, 1).toUpperCase();
            } else if (java.lang.Character.isDigit(firstChar)) {
                // 处理数字
                String number = extractDigit(hanzi);
                return "#" + number;
            } else {
                return "#";
            }

        }
        return "";
    }

    /**
     * 提取字符串中开头连续的数字
     *
     * @param str 检索的字符串
     * @return 开头连续数字
     */
    public static String extractDigit(String str) {
        StringBuilder number = new StringBuilder();
        for (char c : str.toCharArray()) {
            if (java.lang.Character.isDigit(c)) {
                number.append(c);
            } else {
                break;
            }
        }
        return number.toString();
    }

}
