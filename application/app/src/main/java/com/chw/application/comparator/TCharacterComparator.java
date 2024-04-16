package com.chw.application.comparator;

import com.chw.application.model.table.TCharacter;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;
import java.util.Objects;

/**
 * TCharacter表顺序比较器
 */
public class TCharacterComparator implements Comparator<TCharacter> {
    Comparator<Object> comparator = Collator.getInstance(Locale.CHINA);

    @Override
    public int compare(TCharacter s, TCharacter t1) {
        String sl = s.getCharLetter().substring(0, 1);
        String tl = t1.getCharLetter().substring(0, 1);
        //置顶和置底处理
        if (Objects.equals(sl, "☆") || Objects.equals(tl, "#")) {
            // ☆符号置顶：前者为"☆"或后者为"#"则不交换顺序
            return -1;
        } else if (sl.equals("#") || tl.equals("☆")) {
            // #符号置底：前者为"#"或后者为"☆"则交换顺序
            return 1;
        } else if (java.lang.Character.isDigit(sl.charAt(0))) {
            // 其他的按照字母大小进行比较
            return comparator.compare(s.getCharLetter(), t1.getCharLetter());
        }
        return 0;
    }

}
