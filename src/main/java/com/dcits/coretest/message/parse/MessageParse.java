package com.dcits.coretest.message.parse;

import java.util.List;

import org.apache.log4j.Logger;

import com.dcits.business.message.bean.ComplexParameter;
import com.dcits.business.message.bean.Parameter;
import com.dcits.constant.Keys;

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
	 * 将报文解析成ComplexParameter对象
	 * @param message 报文串
	 * @param params 参数列表
	 * @return 根节点对应的复杂参数对象 
	 */
	public abstract ComplexParameter parseMessageToObject(String message, List<Parameter> params);
	
	/**
	 * 根据提供的根节点对应的复杂参数对象生成报文入参串
	 * @param complexParameter 根节点对应的复杂参数对象
	 * @return 解析成的报文串
	 */
	public abstract String depacketizeMessageToString(ComplexParameter complexParameter);
	
	/**
	 * 验证指定的报文串是否符合接口参数要求
	 * <br>默认要求在报文串中出现的节点名称都必须存在于接口的参数库中<br>
	 * 并且节点的类型也必须一样<br>
	 * 同名节点通过path路径来区别
	 * @param params 接口下的参数列表
	 * @param message 需要验证的报文串
	 * @return 返回字符串'true'验证成功,其他内容为错误信息
	 */
	public abstract String checkParameterValidity(List<Parameter> params, String message);
	
	public Parameter findParamter (List<Parameter> params, String parameterName, String parameterPath ) {
		for (Parameter p:params) {
			if (p.getParameterIdentify().equalsIgnoreCase(parameterName) 
					&& p.getPath().equalsIgnoreCase(parameterPath)) {
				return p;
			}
		}
				
		return null;
	}
	
	/**
	 * 获取对应的解析实例
	 * @param type
	 * @return
	 */
	public static MessageParse getParseInstance(String type) {
		switch (type) {
		case Keys.MESSAGE_TYPE_JSON:
			
			if (jsonParse == null) {
				jsonParse = new JSONMessageParse();
			}
			return jsonParse;
		case Keys.MESSAGE_TYPE_XML:
			
			if (xmlParse == null) {
				xmlParse = new XMLMessageParse();
			}
			return xmlParse;
		case Keys.MESSAGE_TYPE_URL:
			
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
