package org.mountcloud.mybatisplugin.utils;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Element;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.AbstractJavaGenerator;
import org.mybatis.generator.config.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO:
 * org.mountcloud.mybatisplugin.utils
 * 2018/8/22.
 *
 * @author zhanghaishan
 * @version V1.0
 */
public class MybatisPluginUtil {

    /**
     * 为Example添加属性和封装
     * @param context 上下文
     * @param topLevelClass topLevelClass
     * @param introspectedTable introspectedTable
     * @param name 属性名
     * @param javaType 属性类
     * @param packing 是否封装get set
     */
    public static void addField(Context context, TopLevelClass topLevelClass, IntrospectedTable introspectedTable, String name, FullyQualifiedJavaType javaType, boolean packing) {

        CommentGenerator commentGenerator = context.getCommentGenerator();

        /**
         * 创建成员变量
         * 如protected Integer limitStart;
         */
        Field field = new Field();
        field.setVisibility(JavaVisibility.PROTECTED);
        field.setType(javaType);
        field.setName(name);
        commentGenerator.addFieldComment(field, introspectedTable);
        topLevelClass.addField(field);
        /**
         * 首字母大写
         */
        char c = name.charAt(0);
        String camel = Character.toUpperCase(c) + name.substring(1);

        if(packing){

            /**
             * 添加Setter方法
             */
            Method method = new Method();
            method.setVisibility(JavaVisibility.PUBLIC);
            method.setName("set" + camel);
            method.addParameter(new Parameter(javaType, name));

            StringBuilder sb = new StringBuilder();
            sb.append("this.");
            sb.append(name);
            sb.append(" = ");
            sb.append(name);
            sb.append(";");

            /**
             * 如 this.limitStart = limitStart;
             */
            method.addBodyLine(sb.toString());

            commentGenerator.addGeneralMethodComment(method, introspectedTable);
            topLevelClass.addMethod(method);

            /**
             * 添加Getter Method 直接调用AbstractJavaGenerator的getGetter方法
             */
            Method getterMethod = AbstractJavaGenerator.getGetter(field);
            commentGenerator.addGeneralMethodComment(getterMethod,introspectedTable);
            topLevelClass.addMethod(getterMethod);
        }
    }

    /**
     * 为Example添加属性和封装
     * @param context 上下文
     * @param innerClass 类
     * @param introspectedTable introspectedTable
     * @param name 属性名
     * @param javaType java类型
     * @param packing 是否生成getset
     */
    public static void addField(Context context, InnerClass innerClass, IntrospectedTable introspectedTable, String name, FullyQualifiedJavaType javaType, boolean packing) {

        CommentGenerator commentGenerator = context.getCommentGenerator();

        /**
         * 创建成员变量
         * 如protected Integer limitStart;
         */
        Field field = new Field();
        field.setVisibility(JavaVisibility.PROTECTED);
        field.setType(javaType);
        field.setName(name);
        commentGenerator.addFieldComment(field, introspectedTable);
        innerClass.addField(field);
        /**
         * 首字母大写
         */
        char c = name.charAt(0);
        String camel = Character.toUpperCase(c) + name.substring(1);

        if(packing){

            /**
             * 添加Setter方法
             */
            Method method = new Method();
            method.setVisibility(JavaVisibility.PUBLIC);
            method.setName("set" + camel);
            method.addParameter(new Parameter(javaType, name));

            StringBuilder sb = new StringBuilder();
            sb.append("this.");
            sb.append(name);
            sb.append(" = ");
            sb.append(name);
            sb.append(";");

            /**
             * 如 this.limitStart = limitStart;
             */
            method.addBodyLine(sb.toString());

            commentGenerator.addGeneralMethodComment(method, introspectedTable);
            innerClass.addMethod(method);

            /**
             * 添加Getter Method 直接调用AbstractJavaGenerator的getGetter方法
             */
            Method getterMethod = AbstractJavaGenerator.getGetter(field);
            commentGenerator.addGeneralMethodComment(getterMethod,introspectedTable);
            innerClass.addMethod(getterMethod);
        }
    }


    /***
     * 查询element
     * @param element xml元素
     * @param name 名称
     * @return 查询到的元素
     */
    public static List<XmlElement> getElement(XmlElement element, String name){

        List<XmlElement> result = new ArrayList<XmlElement>();

        List<Element> elements = element.getElements();
        for(int i=0;i<elements.size();i++){
            Element tempElement = elements.get(i);
            if(tempElement instanceof XmlElement){
                XmlElement xmlElement = (org.mybatis.generator.api.dom.xml.XmlElement) tempElement;
                if(xmlElement.getName().equals(name)){
                    result.add(xmlElement);
                }
            }
        }
        return result;
    }

    /**
     * 查询attribute
     * @param xmlElement xml元素
     * @param name 属性名
     * @return 查询到的元素
     */
    public static Attribute getAttribute(XmlElement xmlElement,String name){
        List<Attribute> elementAttributes = xmlElement.getAttributes();
        for(int i=0;i<elementAttributes.size();i++){
            Attribute attribute = elementAttributes.get(i);
            if(attribute.getName().equals(name)){
                return attribute;
            }
        }
        return null;
    }

}
