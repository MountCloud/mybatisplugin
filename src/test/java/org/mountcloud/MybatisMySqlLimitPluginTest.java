package org.mountcloud;

import org.junit.Test;
import org.mountcloud.mybatisplugin.utils.ObjectUtil;

import java.lang.reflect.Field;

/**
 * TODO:
 * org.mountcloud
 * 2018/8/22.
 *
 * @author zhanghaishan
 * @version V1.0
 */
public class MybatisMySqlLimitPluginTest {

    private Integer integerField;
    private int intField;
    private String strField;

    @Test
    public void test(){
        Field field = ObjectUtil.getField(MybatisMySqlLimitPluginTest.class,"strField");
        if(field.getType().equals(Integer.class)||field.getType().getName().equals("int")){
            System.out.println("is int");
        }else{
            System.out.println("not is int");
        }
    }

    public Integer getIntegerField() {
        return integerField;
    }

    public void setIntegerField(Integer integerField) {
        this.integerField = integerField;
    }

    public int getIntField() {
        return intField;
    }

    public void setIntField(int intField) {
        this.intField = intField;
    }
}
