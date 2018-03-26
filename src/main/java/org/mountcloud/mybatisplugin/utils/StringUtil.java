package org.mountcloud.mybatisplugin.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * @author zhanghaishan
 * @version V1.0
 * @Package org.mountcloud.mybatisplugin.utils
 * @Description: TODO 字符串工具集
 * @date 2018/1/18.
 */
public class StringUtil {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //首字母转大写
    public static String toUpperCaseFirstOne(String s){
        if(Character.isUpperCase(s.charAt(0))) {
            return s;
        }else {
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }

    public static Integer toInteger(Object obj){
        if(obj == null){
            return null;
        }
        return Integer.parseInt(obj.toString());
    }

    public static Float toFloat(Object obj){
        if(obj == null){
            return null;
        }
        return Float.parseFloat(obj.toString());
    }

    public static Double toDouble(Object obj){
        if(obj == null){
            return null;
        }
        return Double.parseDouble(obj.toString());
    }

    public static Long toLong(Object obj){
        if(obj == null){
            return null;
        }
        return Long.parseLong(obj.toString());
    }

    public static Boolean toBoolean(Object obj){
        if(obj == null){
            return null;
        }
        return Boolean.parseBoolean(obj.toString());
    }

    public static String objToString(Object obj){
        if(obj == null){
            return null;
        }
        return obj.toString();
    }

    public static String dataToDate(Date date){
        return sdf.format(date);
    }

    /**
     * 根据map替换
     * @param str
     * @param map
     * @return
     */
    public static String replaceByMap(String str,Map<String,String> map){
        if(map!=null){
            Set<String> keys = map.keySet();
            for(String key : keys){
                String val = map.get(key);
                str = str.replaceAll(key,val);
            }
        }
        return str;
    }

}
