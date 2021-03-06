package com.dcits.coretest.message.parse;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import com.dcits.business.message.bean.ComplexParameter;
import com.dcits.business.message.bean.Parameter;
import com.dcits.constant.MessageKeys;

/**
 * 接口自动化<br>
 * xml格式报文相关的解析等方法实现
 * @author xuwangcheng
 * @version 2017.04.11,1.0.0.0
 *
 */
public class XMLMessageParse extends MessageParse {
	
	
	protected XMLMessageParse() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public ComplexParameter parseMessageToObject(String message, List<Parameter> params) {
		// TODO Auto-generated method stub
		
		return null;
	}

	@Override
	public String depacketizeMessageToString(ComplexParameter complexParameter, String paramsData) {
		// TODO Auto-generated method stub
		//return MessageKeys.XML_MESSAGE_HEAD_STRING + parseXmlMessage(complexParameter, new StringBuilder(""), messageData).toString();
		return null;
	}

	@Override
	public String checkParameterValidity(List<Parameter> params, String message) {
		// TODO Auto-generated method stub
		return "无法解析xml报文";
	}
	
	private StringBuilder parseXmlMessage(ComplexParameter parameter, StringBuilder message, Map<String, Object> messageData) {		
		
		String parameterType = parameter.getSelfParameter().getType().toUpperCase();
		String nodeName = findValidParameterIdentify(parameter);
		boolean flag = Pattern.matches(MessageKeys.MESSAGE_PARAMETER_TYPE_ARRAY_IN_ARRAY + "|" 
				+ MessageKeys.MESSAGE_PARAMETER_TYPE_ARRAY + "|" + MessageKeys.MESSAGE_PARAMETER_TYPE_OBJECT, parameterType)
				|| (nodeName == null);
		
		if (!flag) {
			message.append("<" + nodeName + ">");
		}
				
		if (Pattern.matches(MessageKeys.MESSAGE_PARAMETER_TYPE_STRING + "|" 
				+ MessageKeys.MESSAGE_PARAMETER_TYPE_NUMBER, parameterType)) {	
			message.append(findParameterValue(parameter.getSelfParameter(), messageData));							
		} else {
			for (ComplexParameter p:parameter.getChildComplexParameters()) {
				parseXmlMessage(p, message, messageData);
			}
			
		}
		if (!flag) {
			message.append("</" + nodeName + ">");
		}
				
		return message;
	}
	
	private String findValidParameterIdentify(ComplexParameter parameter) {
		
		if (!parameter.getSelfParameter().getParameterIdentify().isEmpty()) {
			return parameter.getSelfParameter().getParameterIdentify();
		}
		
		if (parameter.getSelfParameter().getType().equalsIgnoreCase(MessageKeys.MESSAGE_PARAMETER_TYPE_OBJECT) 
				&& parameter.getParentComplexParameter() == null) {
			//return Keys.XML_MESSAGE_DEFAULT_ROOT_NODE;
			return null;
		}
		
		return findValidParameterIdentify(parameter.getParentComplexParameter());
	}

	@Override
	public boolean messageFormatValidation(String message) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Set<Parameter> importMessageToParameter(String message) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String parameterReplaceByNodePath(String message, String str) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getObjectByPath(String message, String path) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
