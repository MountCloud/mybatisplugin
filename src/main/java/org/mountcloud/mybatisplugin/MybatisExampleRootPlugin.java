package org.mountcloud.mybatisplugin;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.List;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

/**
 * 允许继承的插件
 * @author zhanghaishan
 * @version V1.0
 */
public class MybatisExampleRootPlugin extends PluginAdapter
// =================================================
{
	private String baseClassName;

	public MybatisExampleRootPlugin() {
	}

	@Override
	public boolean validate(List<String> warnings)
	// --------------------------------------------
	{
		baseClassName = properties.getProperty("exampleRootClass");
		boolean isValid = stringHasValue(baseClassName);
		if (!isValid)
			warnings.add("ExampleClassRoot plugin: exampleRootClass property not found");
		return isValid;
	}

	@Override
	public boolean modelExampleClassGenerated(TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable)
	// ---------------------------------------------------------------------------------------------------------
	{
		if (baseClassName != null) {
			FullyQualifiedJavaType superClass = new FullyQualifiedJavaType(
					baseClassName);
			topLevelClass.addImportedType(superClass);
			topLevelClass.setSuperClass(superClass);
			System.out.println("Setting Example class "
					+ topLevelClass.getType().getFullyQualifiedName()
					+ " super class to " + superClass.getFullyQualifiedName());
		}
		return true;
	}
}