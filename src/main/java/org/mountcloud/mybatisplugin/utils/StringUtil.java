package org.mountcloud.mybatisplugin.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * @author zhanghaishan
 * @version V1.0
 */
public class StringUtil {

    /**
     * 转换工具
     */
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 首字母转大写
     * @param s 字符串
     * @return 转换后的字符串
     */
    public static String toUpperCaseFirstOne(String s){
        if(Character.isUpperCase(s.charAt(0))) {
            return s;
        }else {
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }

    /**
     * Object to Integer
     * @param obj replace Object
     * @return success is Integer, fail is null
     */
    public static Integer toInteger(Object obj){
        if(obj == null){
            return null;
        }
        return Integer.parseInt(obj.toString());
    }

    /**
     * Object to Float
     * @param obj replace Object
     * @return success is Float, fail is null
     */
    public static Float toFloat(Object obj){
        if(obj == null){
            return null;
        }
        return Float.parseFloat(obj.toString());
    }

    /**
     * Object to Double
     * @param obj replace Object
     * @return success is Double, fail is null
     */
    public static Double toDouble(Object obj){
        if(obj == null){
            return null;
        }
        return Double.parseDouble(obj.toString());
    }

    /**
     * Object to Long
     * @param obj replace Object
     * @return success is Long, fail is null
     */
    public static Long toLong(Object obj){
        if(obj == null){
            return null;
        }
        return Long.parseLong(obj.toString());
    }

    /**
     * Object to Boolean
     * @param obj replace Object
     * @return success is Boolean, fail is null
     */
    public static Boolean toBoolean(Object obj){
        if(obj == null){
            return null;
        }
        return Boolean.parseBoolean(obj.toString());
    }

    /**
     * Object to String
     * @param obj replace Object
     * @return success is String, fail is null
     */
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
     * @param str old string
     * @param map replace prms,key is old str,value is new str
     * @return new string
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
