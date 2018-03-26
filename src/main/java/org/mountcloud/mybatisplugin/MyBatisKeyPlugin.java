package org.mountcloud.mybatisplugin;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;

import java.util.List;

/**
 * Mysql主键优化插件
 * @author zhanghaishan
 * @version V1.0
 */
public class MyBatisKeyPlugin extends PluginAdapter {

	@Override
	public boolean sqlMapInsertElementGenerated(XmlElement element,
			IntrospectedTable introspectedTable) {
		List<IntrospectedColumn> columns = introspectedTable
				.getPrimaryKeyColumns();
		IntrospectedColumn columnIden = null;
		if ((columns != null) && (columns.size() != 0)) {
			for (IntrospectedColumn column : columns) {
				if (("id".equals(column.getJavaProperty()))
						&& ("INTEGER".equals(column.getJdbcTypeName()))) {
					columnIden = column;
					break;
				}
			}
		}
		if (columnIden != null) {
			element.addAttribute(new Attribute("useGeneratedKeys", "true"));
			element.addAttribute(new Attribute("keyProperty", columnIden
					.getJavaProperty()));
		}
		return super.sqlMapInsertElementGenerated(element, introspectedTable);
	}

	@Override
	public boolean validate(List<String> arg0) {
		// TODO Auto-generated method stub
		return true;
	}

}
