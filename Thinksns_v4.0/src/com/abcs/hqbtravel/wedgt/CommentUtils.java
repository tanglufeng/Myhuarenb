package com.abcs.hqbtravel.wedgt;

/**
 * Created by Administrator on 2016/10/18.
 */
public class CommentUtils {

    public static String divPhoneifHas(String phone){
        String []divsPhone;
        if(phone.contains("/")){
            divsPhone=phone.split("/");
            if(divsPhone!=null&&divsPhone.length>0){
                StringBuffer buffer=new StringBuffer();
                for (int i = 0; i < divsPhone.length; i++) {
                    buffer.append(divsPhone[i]+",");
                }
                return buffer.substring(0,buffer.lastIndexOf(","));
            }
        }
        return null;
    }
}
