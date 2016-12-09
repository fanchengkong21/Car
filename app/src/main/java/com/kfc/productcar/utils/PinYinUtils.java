package com.kfc.productcar.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * Created by jackiechan on 16/1/12.
 */
public class PinYinUtils {
    /**
     * 通过映射获取对应的拼音存在效率问题,不应该多次被调用
     * @param hanzi
     * @return
     */
    public static String getPinyin(String hanzi) {
        String result = "";
        HanyuPinyinOutputFormat hanyuPinyinOutputFormat = new HanyuPinyinOutputFormat();
        hanyuPinyinOutputFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);//设置输出的拼音大写
        //千锋--->QIANFENG
        hanyuPinyinOutputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);//不带音标
        char[] chars = hanzi.toCharArray();//将汉字转成数组
        for (int i = 0; i < chars.length; i++) {
            char cha = chars[i];
            if (Character.isWhitespace(cha)) continue;//如果是空格的话就直接中断进行下一次
           if (cha>127){//理论上可以认为是一个汉字
               try {
                   String[] strings = PinyinHelper.toHanyuPinyinStringArray(cha, hanyuPinyinOutputFormat);//解析拼音
                   if (strings == null) {//如果是全角字符
                        result+=cha;
                   }else{
                       result += strings[0];
                   }
               } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                   result+=cha;//如果转换失败,也追加在结果里面
                   badHanyuPinyinOutputFormatCombination.printStackTrace();

               }
           }else{
               result += cha;//如果不是汉字 直接在结果上面追加
           }
        }
        return result;
    }
}
