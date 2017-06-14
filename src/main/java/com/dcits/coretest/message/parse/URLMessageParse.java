package com.dcits.coretest.message.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dcits.business.message.bean.ComplexParameter;
import com.dcits.business.message.bean.Parameter;

/**
 * 接口自动化<br>
 * Url格式报文相关的解析等方法实现
 * @author xuwangcheng
 * @version 2017.04.11,1.0.0.0
 *
 */
public class URLMessageParse extends MessageParse {
	
	protected URLMessageParse() {
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
		//return paraseUrlMessage(complexParameter, new StringBuilder(""), messageData).toString().substring(1);
		return null;
	}

	@Override
	public String checkParameterValidity(List<Parameter> params, String message) {
		// TODO Auto-generated method stub
		return "无法解析此Url参数报文";
	}
	
	private StringBuilder paraseUrlMessage(ComplexParameter parameter, StringBuilder message, Map<String, Object> messageData) {
		List<ComplexParameter> childParams = new ArrayList<ComplexParameter>(parameter.getChildComplexParameters());
		
		if (childParams.size() == 0) {
			message.append("&").append(parameter.getSelfParameter().getParameterIdentify()).append("=").append(findParameterValue(parameter.getSelfParameter(), messageData));	
			return message;
		}
		
		for (int i = 0; i < childParams.size(); i++) {
			paraseUrlMessage(childParams.get(i), message, messageData);						
		}
		
		return message;
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
