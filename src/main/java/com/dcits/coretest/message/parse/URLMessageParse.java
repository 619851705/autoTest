package com.dcits.coretest.message.parse;

import java.util.ArrayList;
import java.util.List;

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
	public String depacketizeMessageToString(ComplexParameter complexParameter) {
		// TODO Auto-generated method stub
		return paraseUrlMessage(complexParameter, new StringBuilder("")).toString().substring(1);
	}

	@Override
	public String checkParameterValidity(List<Parameter> params, String message) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private StringBuilder paraseUrlMessage(ComplexParameter parameter, StringBuilder message) {
		List<ComplexParameter> childParams = new ArrayList<ComplexParameter>(parameter.getChildComplexParameters());
		
		if (childParams.size() == 0) {
			message.append("&").append(parameter.getSelfParameter().getParameterIdentify()).append("=").append(parameter.getSelfParameter().getDefaultValue());	
			return message;
		}
		
		for (int i = 0; i < childParams.size(); i++) {
			paraseUrlMessage(childParams.get(i), message);						
		}
		
		return message;
	}

}
