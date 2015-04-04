package com.youlongnet.lulu.ui.widget.slidebar;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class Hanyu {
    private static Hanyu hanyu = new Hanyu();
    private HanyuPinyinOutputFormat format = null;
    private String[] pinyin;

    private Hanyu() {
        format = new HanyuPinyinOutputFormat();
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        pinyin = null;
    }

    public static Hanyu getInstance() {
        return hanyu;
    }

    // 转换单个字符
    public String getCharacterPinYin(char c) {
        try {
            pinyin = PinyinHelper.toHanyuPinyinStringArray(c, format);
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }
        // 如果c不是汉字，toHanyuPinyinStringArray会返回null
        if (pinyin == null)
            return null;
        // 只取一个发音，如果是多音字，仅取第一个发音
        return pinyin[0];
    }

    // 转换一个字符串
    public String getStringPinYin(String str) {
        StringBuilder sb = new StringBuilder();
        String tempPinyin = null;
        int len = str.length();
        for (int i = 0; i < len; ++i) {
            tempPinyin = getCharacterPinYin(str.charAt(i));
            if (tempPinyin == null) {
                // 如果str.charAt(i)非汉字，则保持原样
                sb.append(str.charAt(i));
            } else {
                sb.append(tempPinyin);
            }
        }
        return sb.toString();
    }

    // 转换一个字符串首字母
    public char getFirstStringPinYin(String str) {
        StringBuilder sb = new StringBuilder();
        String tempPinyin = null;
        tempPinyin = getCharacterPinYin(str.charAt(0));
        if (tempPinyin == null) {
            // 如果str.charAt(i)非汉字，则保持原样
            sb.append(str.charAt(0));
        } else {
            sb.append(tempPinyin);
        }
        char sub = sb.toString().charAt(0);
        return sub;
    }
}