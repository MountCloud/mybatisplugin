# A collection of plugins
MybatisExampleRootPlugin ：Allow Example to inherit

MyBatisKeyPlugin ：Primary key repair

MybatisGroupOrderPlugin ：Automatic generation of grouping and sorting

MybatisMySqlLimitPlugin ：Create limit by Example

# Please keep an eye on the dynamics of this plugin^_^!

# Use

## POM.xml

	<dependency>
		<groupId>org.mountcloud</groupId>
		<artifactId>mybatisplugin</artifactId>
		<version>1.1</version>
	</dependency>

## GeneratorConfig.xml
	<plugin type="org.mountcloud.mybatisplugin.MyBatisKeyPlugin"></plugin>
	<plugin type="org.mountcloud.mybatisplugin.MybatisExampleRootPlugin">
            <property name="exampleRootClass" value="com.xxx.common.entity.BaseExample"/>
	</plugin>
	<plugin type="org.mountcloud.mybatisplugin.MybatisGroupOrderPlugin"></plugin>
	<plugin type="org.mountcloud.mybatisplugin.MybatisMySqlLimitPlugin"></plugin>

# Install
	mvn install
