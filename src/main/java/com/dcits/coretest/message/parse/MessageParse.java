package com.dcits.coretest.message.parse;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.dcits.business.message.bean.ComplexParameter;
import com.dcits.business.message.bean.Parameter;
import com.dcits.constant.MessageKeys;

/**
 * 报文解析工具类
 * 新格式报文继承此类即可
 * @author xuwangcheng
 * @version 2017.04.06,1.0.0.0
 *
 */

public abstract class MessageParse {
	
	public static final Logger LOGGER = Logger.getLogger(MessageParse.class.getName());
	
	private static JSONMessageParse jsonParse;
	private static XMLMessageParse xmlParse;
	private static URLMessageParse urlParse;
	
	
	/**
	 * 美化指定格式的报文
	 * @param message
	 * @return 
	 */
	public String messageFormatBeautify (String message) {
		return message;
	};
	
	/**
	 * 验证报文是否为指定的格式
	 * @param message 验证的报文串
	 * @return boolean 是否为合法的指定格式
	 */
	public abstract boolean messageFormatValidation (String message);
	
	/**
	 * 根据传入的不同格式报文，获取各个参数属性
	 * @param message 导入的报文串
	 * @return List&lt;Parameter&gt  解析指定的参数列表
	 */
	public abstract Set<Parameter> importMessageToParameter (String message);
	
	/**
	 * 将报文解析成ComplexParameter对象
	 * @param message 报文串
	 * @param params 参数列表
	 * @return ComplexParameter 根节点对应的复杂参数对象 
	 */
	public abstract ComplexParameter parseMessageToObject(String message, List<Parameter> params);
	
	/**
	 * 根据提供的根节点对应的复杂参数对象生成报文入参串
	 * @param complexParameter 根节点对应的复杂参数对象
	 * @param paramsData 提供的填充参数数据，如果传入的为null，则使用默认值 为json字符串
	 * @return String 解析成的报文串
	 */
	public abstract String depacketizeMessageToString(ComplexParameter complexParameter, String paramsData);
	
	/**
	 * 验证指定的报文串是否符合接口参数要求
	 * <br>默认要求在报文串中出现的节点名称都必须存在于接口的参数库中<br>
	 * 并且节点的类型也必须一样<br>
	 * 同名节点通过path路径来区别
	 * @param params 接口下的参数列表
	 * @param message 需要验证的报文串
	 * @return String 返回字符串'true'验证成功,其他内容为错误信息
	 */
	public abstract String checkParameterValidity(List<Parameter> params, String message);
	
	/**
	 * 在提供的报文中查找指定路径的节点值，并替换字符串中需要替换的参数
	 * @param message 指定格式报文
	 * @param str 
	 * @return
	 */
	public abstract String parameterReplaceByNodePath(String message, String str);
	
	/**
	 * 根据路径获取指定格式报文中的值
	 * @param message
	 * @param path 路径
	 * @return
	 */
	public abstract String getObjectByPath(String message, String path);
	
	/**
	 * 将已经美化过的报文或者其他格式的报文转换成一行
	 * @param message
	 * @return
	 */
	public String parseMessageToSingleRow(String message) {
		 if(message!=null && !"".equals(message)) {      
	           Pattern p = Pattern.compile("\\s*|\t|\r|\n");      
	           Matcher m = p.matcher(message);      
	           String strNoBlank = m.replaceAll("");      
	           return strNoBlank;      
	       }else {      
	           return message;      
	        } 
	};
	
	/**
	 * 根据参数名和参数路径在参数列表中查找是否存在
	 * @param params
	 * @param parameterName
	 * @param parameterPath
	 * @return
	 */
	protected Parameter findParamter (List<Parameter> params, String parameterName, String parameterPath ) {
		for (Parameter p:params) {
			if (p.getParameterIdentify().equalsIgnoreCase(parameterName) 
					&& p.getPath().equalsIgnoreCase(parameterPath)) {
				return p;
			}
		}
				
		return null;
	}
	
	/**
	 * 根据提供的参数信息在TestData的数据信息中查找指定的value,如果没有查询到则使用这个参数的默认值
	 * <br>需要注意的是对于Array类型参数下的String和Number参数可能有同名同路径的参数,取值的时候需要判断是否是List存储的value
	 * @param param
	 * @param messageData
	 * @return
	 */
	protected String findParameterValue (Parameter param, Map<String, Object> messageData) {
		
		if (messageData == null) {
			return param.getDefaultValue();
		}
		String path = param.getPath() + "." + param.getParameterIdentify();
		if (messageData.get(path) == null) {
			return param.getDefaultValue();
		}
		
		if (messageData.get(path) instanceof String) {
			return  messageData.get(path).toString();
		}
		
		//List类型，多个相同的值的话，用一个就删除一个
		List<String> values = (List<String>) messageData.get(path);
		String value = values.get(0);
		
		values.remove(0);
		
		return value;
		
	}	
	
	public String findParameterValue2 (Parameter param, Map<String, Object> messageData) {
		if (messageData == null) {
			return "";
		}
		String path = param.getPath() + "." + param.getParameterIdentify();
		if (messageData.get(path) == null) {
			return "";
		}
		
		return  messageData.get(path).toString();
	}
	
	
	/**
	 * 获取对应的解析实例
	 * @param type
	 * @return
	 */
	public static MessageParse getParseInstance(String type) {
		switch (type.toUpperCase()) {
		case MessageKeys.MESSAGE_TYPE_JSON:
			
			if (jsonParse == null) {
				jsonParse = new JSONMessageParse();
			}
			return jsonParse;
		case MessageKeys.MESSAGE_TYPE_XML:
			
			if (xmlParse == null) {
				xmlParse = new XMLMessageParse();
			}
			return xmlParse;
		case MessageKeys.MESSAGE_TYPE_URL:
			
			if (urlParse == null) {
				urlParse = new URLMessageParse();
			}
			return urlParse;
		default:
			break;
		}
		return null;
	}
	
}
