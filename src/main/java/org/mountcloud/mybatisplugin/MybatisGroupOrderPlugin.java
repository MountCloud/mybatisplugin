package org.mountcloud.mybatisplugin;

import org.mountcloud.mybatisplugin.utils.MybatisPluginUtil;
import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.AbstractJavaGenerator;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * 用来给Mybatis添加group和order的插件,此插件会让Example添加一个group by属性和order by属性，并且会添加一个返回map（selectCustomByExample）的自定义查询
 * @author zhanghaishan
 * @version V1.0
 */
public class MybatisGroupOrderPlugin extends PluginAdapter{
	@Override
	public boolean validate(List<String> warnings) {
		return true;
	}



	/**
	 * 添加groupby与orderby属性
	 */
	@Override
	public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		
		MybatisPluginUtil.addField(context,topLevelClass,introspectedTable,"groupBy",PrimitiveTypeWrapper.getStringInstance(),true);//groupBy属性

		MybatisPluginUtil.addField(context,topLevelClass,introspectedTable,"columnCustom",PrimitiveTypeWrapper.getStringInstance(),true);//自定义列
		
		return super.modelExampleClassGenerated(topLevelClass, introspectedTable);
	}
	
	/**
	 * 为selectByExample添加groupby
	 */
	@Override
	public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
		
		XmlElement groupByIsNotNullElement = new XmlElement("if");
		groupByIsNotNullElement.addAttribute(new Attribute("test","groupBy != null"));
		groupByIsNotNullElement.addElement(new TextElement("group by ${groupBy}"));
		element.addElement(element.getElements().size()-1,groupByIsNotNullElement);

		return super.sqlMapSelectByExampleWithoutBLOBsElementGenerated(element,introspectedTable);
	}

	/**
	 * 为selectByExampleWithBLOBs添加groupby
	 */
	@Override
	public boolean sqlMapSelectByExampleWithBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {

		XmlElement groupByIsNotNullElement = new XmlElement("if");
		groupByIsNotNullElement.addAttribute(new Attribute("test","groupBy != null"));
		groupByIsNotNullElement.addElement(new TextElement("group by ${groupBy}"));
		element.addElement(element.getElements().size()-1,groupByIsNotNullElement);

		return super.sqlMapSelectByExampleWithBLOBsElementGenerated(element,introspectedTable);
	}
	
	/**
	 * 添加自定义查询XML
	 */
	@Override
	public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
		
		String exampleType = introspectedTable.getExampleType();//获取Example
		String tableName = introspectedTable.getFullyQualifiedTableNameAtRuntime();
		
		
		XmlElement parentElement = document.getRootElement(); 
		
		//主select
		XmlElement selectCustomByExampleXml = new XmlElement("select");
		selectCustomByExampleXml.addAttribute(new Attribute("id", "selectCustomByExample"));
		selectCustomByExampleXml.addAttribute(new Attribute("resultType", "java.util.HashMap"));
		selectCustomByExampleXml.addAttribute(new Attribute("parameterType",exampleType));
		
		selectCustomByExampleXml.addElement(new TextElement("select "));

		
		//distinct判断条件
		XmlElement distinctIfXml = new XmlElement("if");
		distinctIfXml.addAttribute(new Attribute("test","distinct"));
		distinctIfXml.addElement(new TextElement("distinct"));
		selectCustomByExampleXml.addElement(distinctIfXml);
		
		//判断是不是有自定义属性
		XmlElement columnCustomXml = new XmlElement("choose");
		
		//相当于if
		XmlElement columnCustomWhenXml = new XmlElement("when");
		columnCustomWhenXml.addAttribute(new Attribute("test","columnCustom!=null"));
		columnCustomWhenXml.addElement(new TextElement("${columnCustom}"));
		columnCustomXml.addElement(columnCustomWhenXml);
		
		//相当于else
		XmlElement columnCustomOtherwiseXml = new XmlElement("otherwise");
		XmlElement columnCustomOtherwiseIncludeXml = new XmlElement("include");
		columnCustomOtherwiseIncludeXml.addAttribute(new Attribute("refid", "Base_Column_List"));//引用基本
		columnCustomOtherwiseXml.addElement(columnCustomOtherwiseIncludeXml);
		columnCustomXml.addElement(columnCustomOtherwiseXml);
		
		selectCustomByExampleXml.addElement(columnCustomXml);
		
		selectCustomByExampleXml.addElement(new TextElement(" from "+tableName+" "));
		
		//添加判断条件
		XmlElement whereIfXml = new XmlElement("if");
		whereIfXml.addAttribute(new Attribute("test", "_parameter != null"));
		XmlElement whereIfIncludeXml = new XmlElement("include");
		whereIfIncludeXml.addAttribute(new Attribute("refid", "Example_Where_Clause"));
		whereIfXml.addElement(whereIfIncludeXml);
		
		selectCustomByExampleXml.addElement(whereIfXml);
		
		//添加group by
		XmlElement groupByIsNotNullElement = new XmlElement("if");
		groupByIsNotNullElement.addAttribute(new Attribute("test","groupBy != null"));
		groupByIsNotNullElement.addElement(new TextElement("group by ${groupBy}"));
		selectCustomByExampleXml.addElement(groupByIsNotNullElement);
		
		//添加order by
		XmlElement orderByIsNotNullElement = new XmlElement("if");
		orderByIsNotNullElement.addAttribute(new Attribute("test","orderByClause != null"));
		orderByIsNotNullElement.addElement(new TextElement("order by ${orderByClause}"));
		selectCustomByExampleXml.addElement(orderByIsNotNullElement);
		
		//将查询添加进xml中
		parentElement.addElement(selectCustomByExampleXml);
		
		return super.sqlMapDocumentGenerated(document, introspectedTable);
	}
	
	/**
	 * 为自定义查询添加接口
	 */
	@Override
	public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();  
		
		Method selectCustomByExampleMethod = new Method();  
        selectCustomByExampleMethod.setVisibility(JavaVisibility.PUBLIC);  
        
        //参数类型和返回类型
        FullyQualifiedJavaType parameterType = new FullyQualifiedJavaType(  
                introspectedTable.getExampleType());  
        
        FullyQualifiedJavaType returnType = FullyQualifiedJavaType.getNewListInstance();
        
        
        //设置类型
        selectCustomByExampleMethod.addParameter(new Parameter(parameterType, "example"));
        selectCustomByExampleMethod.setReturnType(returnType);  

        //设置名字
        selectCustomByExampleMethod.setName("selectCustomByExample");  
        //addMapperAnnotations(interfaze, selectCustomByExampleMethod);
        
        //设置注释
        String tableName = introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime();
        selectCustomByExampleMethod.addJavaDocLine("/**");
        selectCustomByExampleMethod.addJavaDocLine("  * ");
        selectCustomByExampleMethod.addJavaDocLine("  * This method was generated by MyBatis Generator.");
        selectCustomByExampleMethod.addJavaDocLine("  * This method corresponds to the database table "+tableName);
        selectCustomByExampleMethod.addJavaDocLine("  * ");
        selectCustomByExampleMethod.addJavaDocLine("  */");
        
        importedTypes.add(parameterType);
        importedTypes.add(returnType);
        
        interfaze.addImportedTypes(importedTypes);
		interfaze.addMethod(selectCustomByExampleMethod);
		
		return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
	}

}
