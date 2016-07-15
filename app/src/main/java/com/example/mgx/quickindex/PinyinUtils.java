package com.example.mgx.quickindex;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * Created by mgx on 2016/6/30.
 */
public class PinyinUtils {

    public static String getPinyin(String string) {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        //不需要音标
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        //设置输出为大写
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);

        StringBuffer sb = new StringBuffer();

        char[] chars = string.toCharArray();
        for (int i = 0; i<chars.length;i++) {
            char c = chars[i];
            if (Character.isWhitespace(c)) {
                continue;
            }
            if (c > -128 && c <= 127) {
                //不是汉字，则直接拼接
                sb.append(c);
            }
            else {
                try {
                    String s = PinyinHelper.toHanyuPinyinStringArray(c, format)[0];
                    sb.append(s);
                } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                    badHanyuPinyinOutputFormatCombination.printStackTrace();
                }
            }
        }
        return sb.toString();
    }
}
