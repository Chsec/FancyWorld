package com.chw.application.comparator;

import com.chw.application.model.table.TCharacter;

import java.util.Comparator;

/**
 * TCharacter表顺序比较器(数字部分比较)
 */
public class TCharacterDigitComparator implements Comparator<TCharacter> {
    @Override
    public int compare(TCharacter tCharacter, TCharacter t1) {
        String sl = tCharacter.getCharLetter();
        String tl = t1.getCharLetter();
        if (sl.length() == 1 || tl.length() == 1) {
            return 0;
        } else {
            return Integer.parseInt(sl.substring(1)) - Integer.parseInt(tl.substring(1));
        }
    }
}
