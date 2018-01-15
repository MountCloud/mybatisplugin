package pers.zhs.mybatisplugin;

import com.ugirls.statisticalservice.common.util.ObjectUtil;
import com.ugirls.statisticalservice.common.util.StringUtil;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 
* @Title: MybatisUtil.java 
* @Package com.coconet.common.util.mybatis 
* @Description: TODO Mybatis工具类，可以自动设置Example等
* @author zhanghaishan
* @date 2017年8月22日 下午5:04:25 
* @version V1.0
 */
public class MybatisUtil {
	
	
	public static void setAutoExample(Object criteria,Object entity){
		setAutoExample(criteria,entity,null);
	}
	
	/***
	 * 自动设置查询条件，查询函数为andXXEqualTo（也就是说是Equal）
	 * @param criteria
	 * @param entity
	 */
	public static void setAutoExample(Object criteria,Object entity,String ...likeAttr){
		if(criteria==null||entity==null){
			return;
		}
		Class criteriaClass = criteria.getClass();
		List<Method> criteriaMethods = new ArrayList<Method>();
		ObjectUtil.getMethods(criteriaClass, criteriaMethods,1);
		
		Class entityClass = entity.getClass(); 
		List<Field>  entityFields = new ArrayList<Field>();
		ObjectUtil.getFields(entityClass, entityFields,null);
		
		
		for(Field field:entityFields){

			try {
				
				if(field.getName().equals("serialVersionUID")){
					continue;
				}
				
	            PropertyDescriptor pd = new PropertyDescriptor(field.getName(), entityClass);  
	            //获得set方法  
//	            Method method = pd.getWriteMethod();  
//	            method.invoke(as, new Object[]{"123"});  
	            //获得get方法  
	            Method get = pd.getReadMethod();  
	            Object getValue = get.invoke(entity);
	            
	            if(getValue!=null){
	            	
	            	String fname = field.getName();
	            	String upname = StringUtil.toUpperCaseFirstOne(fname);
	            	
	            	boolean isLike = false;
	            	if(likeAttr!=null){
	            		for(int i=0;i<likeAttr.length;i++){
	            			String lka = likeAttr[i];
	            			if(lka!=null&&lka.equals(fname)){
	            				isLike = true;
	            				break;
	            			}
	            		}
	            	}
	            	
	            	
	            	String methodName = null;
	            	if(!isLike){
	            		methodName = "and"+upname+"EqualTo";
	            	}else{
	            		methodName = "and"+upname+"Like";
	            	}
	            	
	            	
	            	for(Method method : criteriaMethods){
	        			if(method.getName().equals(methodName)){
	        				try {
	        					method.invoke(criteria, getValue);
	        					break;
	        				} catch (Exception e) {
	        					// TODO Auto-generated catch block
	        					e.printStackTrace();
	        				}
	        			}
	        		}
	            	
	            	//System.out.println("field:"+field.getName()+"---getValue:"+getValue);  
	            }
			} catch (Exception e) {
				e.printStackTrace();
			}
 
        }
		
	}

}
