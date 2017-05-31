package com.abcs.haiwaigou.utils;

import java.text.DecimalFormat;

/**
 * Created by zjz on 2016/1/20.
 */
public class NumberUtils {
    //格式化价格，强制保留两位小数
    public static String formatPrice(double price) {
        DecimalFormat df=new DecimalFormat("0.00");
        String format = "¥"+df.format(price);
        return format;
    }
    public static String formatPriceOuYuan(double price) {
        DecimalFormat df=new DecimalFormat("0.00");
        String format = "€"+df.format(price);
        return format;
    }
    //格式化价格，强制保留两位小数
    public static String formatPriceNo(double price) {
        DecimalFormat df=new DecimalFormat("0.00");
        String format = df.format(price);
        return format;
    }
}
