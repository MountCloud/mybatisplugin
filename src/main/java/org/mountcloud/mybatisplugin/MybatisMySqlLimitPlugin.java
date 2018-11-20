package org.mountcloud.mybatisplugin;

import org.mountcloud.mybatisplugin.exception.MybatisMySqlLimitPluginException;
import org.mountcloud.mybatisplugin.utils.MybatisPluginUtil;
import org.mountcloud.mybatisplugin.utils.ObjectUtil;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.*;

import java.util.List;

/**
 * TODO: 扩展分页插件
 * com.ugirls.storagemanager.mybatisplugin
 * 2018/8/22.
 *
 * @author zhanghaishan
 * @version V1.0
 */
public class MybatisMySqlLimitPlugin extends PluginAdapter {
    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    /**
     * 添加limit Class 和 limit 属性
     * @param topLevelClass 类
     * @param introspectedTable 添加的项
     * @return 添加结果
     */
    @Override
    public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
//
//        //如果父类中有limit属性并合规的话就不生成class了
//        if(checkSuperClasslimit(topLevelClass)){
//            return super.modelExampleClassGenerated(topLevelClass, introspectedTable);
//        }

        //添加limit class
        InnerClass limitClass = new InnerClass("Limit");
        limitClass.setStatic(true);
        limitClass.setVisibility(JavaVisibility.PUBLIC);

        MybatisPluginUtil.addField(context,limitClass,introspectedTable,"start",PrimitiveTypeWrapper.getIntegerInstance(),true);
        MybatisPluginUtil.addField(context,limitClass,introspectedTable,"size",PrimitiveTypeWrapper.getIntegerInstance(),true);

        topLevelClass.addInnerClass(limitClass);

        //添加limit属性
        MybatisPluginUtil.addField(context,topLevelClass,introspectedTable,"limit",new FullyQualifiedJavaType("Limit"),true);

        return super.modelExampleClassGenerated(topLevelClass, introspectedTable);
    }

    /**
     * 为select添加limit
     * @param document 总xml
     * @param introspectedTable 添加项
     * @return 添加结果
     */
    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        XmlElement parentElement = document.getRootElement();

        List<XmlElement> elements = MybatisPluginUtil.getElement(parentElement,"select");

        if(elements.size()>0){
            for(int i=0;i<elements.size();i++){
                XmlElement xmlElement = elements.get(i);
                Attribute attribute = MybatisPluginUtil.getAttribute(xmlElement,"id");
                if(attribute!=null&&attribute.getValue().contains("ByExample")){
                    appendLimit(xmlElement);
                }
            }
        }

        return super.sqlMapDocumentGenerated(document, introspectedTable);
    }

    /**
     * 给xml节点添加limit
     * @param element 需要添加limit的xml
     */
    private void appendLimit(XmlElement element){
        XmlElement limitElement = new XmlElement("if");
        limitElement.addAttribute(new Attribute("test","limit != null and limit.start != null and limit.size != null"));
        limitElement.addElement(new TextElement("limit ${limit.start},${limit.size}"));
        element.addElement(element.getElements().size(),limitElement);
    }

    /**
     * 检查父类是否存在limit属性，并且limit是否存在start和size两个Integer或int属性
     * @param topLevelClass 检查项
     */
    private boolean checkSuperClasslimit(TopLevelClass topLevelClass){
        FullyQualifiedJavaType fullyQualifiedJavaType = topLevelClass.getSuperClass();

        boolean state = true;

        if(fullyQualifiedJavaType!=null){
            String fullyQualifiedName = fullyQualifiedJavaType.getFullyQualifiedName();
            try {
                Class<?> superClass = Class.forName(fullyQualifiedName);

                //获取limit属性
                java.lang.reflect.Field limitField = ObjectUtil.getField(superClass,"limit");
                if(limitField!=null){
                    //如果存在limit属性则获取limit的class
                    Class<?> limitClass = limitField.getType();
                    java.lang.reflect.Field limitStartField = ObjectUtil.getField(limitClass,"start");
                    if(limitStartField==null){
                        throw new MybatisMySqlLimitPluginException(fullyQualifiedName+" not found start field!");
                    }else{
                        if(!limitStartField.getType().equals(Integer.class)&&!limitStartField.getType().getName().equals("int")){
                            throw new MybatisMySqlLimitPluginException(fullyQualifiedName+" start field type not is Integer or int!");
                        }
                    }
                    java.lang.reflect.Field limitSizeField = ObjectUtil.getField(limitClass,"size");
                    if(limitSizeField==null){
                        throw new MybatisMySqlLimitPluginException(fullyQualifiedName+" not found size field!");
                    }else{
                        if(!limitSizeField.getType().equals(Integer.class)&&!limitSizeField.getType().getName().equals("int")){
                            throw new MybatisMySqlLimitPluginException(fullyQualifiedName+" size field type not is Integer or int!");
                        }
                    }
                }else{
                    state = false;
                }

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return state;
    }
}
