package org.mountcloud.mybatisplugin.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 对象工具类
 * @author zhanghaishan
 * @version V1.0
 */
@SuppressWarnings("rawtypes")
public class ObjectUtil {

    /**
     * 获取函数列表
     * @param t object class
     * @param methods added list
     * @param index depth index
     */
    public static void getMethods(Class t, List<Method> methods, Integer index){

        methods.addAll(Arrays.asList(t.getMethods()));

        if(index!=null){
            index = index -1;
            if(index<=0){
                return;
            }
        }

        Class t1 = t.getSuperclass();
        if (t1.getSimpleName().equals("Object"))
            return;
        getMethods(t1, methods,index);
    }

    /**
     * 将map里的值更改为String类型
     * @param map replace map
     * @return replace new map
     */
    @SuppressWarnings("unchecked")
	public static Map mapValueToString(Map map){
        Set keys = map.keySet();
        for(Object key : keys){
            Object value = map.get(key);
            if(value==null){
                continue;
            }
            if(!(value instanceof String)){
                map.put(key,value.toString());
            }
        }
        return map;
    }

    /**
     * 获取Object的属性列表
     * @param t 需要获取的Cladd
     * @param fields 属性集合
     * @param index 深度：深入父类一次index则减1，index为0时方法退出。如果获取本身和直系继承的父类属性的话，index为2，只获取自己的属性话 null或者1
     */
    public static void getFields(Class t, List<Field> fields,Integer index) {

        fields.addAll(Arrays.asList(t.getDeclaredFields()));

        if(index!=null){
            index = index -1;
            if(index<=0){
                return;
            }
        }

        Class t1 = t.getSuperclass();//获取父类class
        if (t1.getSimpleName().equals("Object")) {//到Object就到头了，不需要获取Object的属性了
            return;
        }
        getFields(t1, fields,index);
    }

    /**
     * 查询属性
     * @param cls 需要查询的类
     * @param fieldName 属性名字
     * @return 查询到的属性
     */
    public static Field getField(Class<?> cls,String fieldName){
        List<Field> fields = new ArrayList<Field>();
        getFields(cls,fields,null);

        for(int i=0;i<fields.size();i++){
            Field field = fields.get(i);
            if(field.getName().equals(fieldName)){
                return field;
            }
        }
        return null;
    }

}
