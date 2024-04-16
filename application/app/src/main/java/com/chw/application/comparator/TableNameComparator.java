package com.chw.application.comparator;

import com.chw.application.model.table.TableTemplate;
import com.chw.application.util.PinyinUtils;
import com.github.stuxuhai.jpinyin.PinyinException;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

/**
 * TableTemplate表的tableName顺序比较器
 */
public class TableNameComparator implements Comparator<TableTemplate> {
    @Override
    public int compare(TableTemplate front, TableTemplate back) {
        try {
            Comparator<Object> comparator = Collator.getInstance(Locale.CHINA);
            String f = PinyinUtils.getSortLetter(front.getTableName());
            String b = PinyinUtils.getSortLetter(back.getTableName());
            if (b.charAt(0) == '#' && f.charAt(0) != '#') {
                // 后者为"#"则不交换顺序
                return 1;
            } else if (b.charAt(0) != '#' && f.charAt(0) == '#') {
                // 前者为"#"则交换顺序
                return -1;
            } else if (b.charAt(0) == '#' && f.charAt(0) == '#') {
                return Integer.parseInt(b.substring(1)) - Integer.parseInt(f.substring(1));
            } else if (java.lang.Character.isDigit(f.charAt(0))) {
                // 其他的按照字母大小进行比较
                return comparator.compare(f, b);
            }
            return 0;
        } catch (PinyinException e) {
            throw new RuntimeException(e);
        }
    }
}
