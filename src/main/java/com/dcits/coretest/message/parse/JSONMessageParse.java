package com.dcits.coretest.message.parse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

import com.dcits.business.message.bean.ComplexParameter;
import com.dcits.business.message.bean.Parameter;
import com.dcits.constant.Keys;
import com.dcits.constant.SystemConsts;
import com.dcits.util.message.JsonUtil;

/**
 * 接口自动化<br>
 * json格式报文相关的解析等方法实现
 * @author xuwangcheng
 * @version 2017.04.11,1.0.0.0
 *
 */
public class JSONMessageParse extends MessageParse{	
	
	private static ObjectMapper mapper = new ObjectMapper();
	
	protected JSONMessageParse() {
		// TODO Auto-generated constructor stub
	} 
	
	@SuppressWarnings("rawtypes")
	@Override
	public ComplexParameter parseMessageToObject(String message, List<Parameter> params) {
		// TODO Auto-generated method stub
		
		Map maps = null;
		try {
			maps = mapper.readValue(message, Map.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.error("json串解析失败:" + message, e);
			return null;
		}
		
		if (maps.isEmpty()) {
			return null;
		}
		
		return viewJsonTree(maps, new ComplexParameter(new Parameter(SystemConsts.PARAMETER_OBJECT_ID, Keys.MESSAGE_PARAMETER_TYPE_OBJECT), 
				new HashSet<ComplexParameter>(), null), params, new StringBuilder(Keys.MESSAGE_PARAMETER_DEFAULT_ROOT_PATH));
	}

	@Override
	public String depacketizeMessageToString(ComplexParameter complexParameter) {
		// TODO Auto-generated method stub
		return paraseJsonMessage(complexParameter, new StringBuilder("")).toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String checkParameterValidity(List<Parameter> params, String message) {
		// TODO Auto-generated method stub
		
		Object[] o = null;
		try {
			o = (Object[]) JsonUtil.getJsonList(message, 3);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("解析json失败:" + message, e);
			return "解析json失败";
		}
		
		if (o == null) {
			return "不是合法的json格式或者无内容!";
		}
		
		List<String> paramTypes = (List<String>) o[1];
		List<String> paramNames = (List<String>) o[0];
		List<String> paramPaths = (List<String>) o[2];
		
		boolean paramCorrectFlag = false;
		boolean allCorrectFlag = true;
		
		String returnMsg = "入参节点:";
		
		for (int i = 0; i < paramNames.size(); i++) {
			for (Parameter p:params) {
				if (p.getParameterIdentify().equalsIgnoreCase(paramNames.get(i)) 
						&& p.getType().equalsIgnoreCase(paramTypes.get(i))
						&& p.getPath().equalsIgnoreCase(paramPaths.get(i))) {
					paramCorrectFlag = true;
				}
				
				if (!paramCorrectFlag) {
					allCorrectFlag = false;
					returnMsg += "[" + paramNames.get(i) + "] ";
				} else {
					paramCorrectFlag = false;
				}
			}
		}
		
		if (!allCorrectFlag) {
			return returnMsg + "未在接口参数中定义或者类型和路径不匹配,请检查!";
		} 
		
		return "true";
	}
	
	
	
	private StringBuilder paraseJsonMessage(ComplexParameter parameter, StringBuilder message) {	
		
		Parameter param = parameter.getSelfParameter();
		
		if (param == null) {
			return null;
		}
		
		List<ComplexParameter> childParams = new ArrayList<ComplexParameter>(parameter.getChildComplexParameters());
		String parameterType = param.getType();
		
		if (parameterType == null) {
			parameterType = "";
		}
		
		if (!Keys.MESSAGE_PARAMETER_TYPE_ARRAY_IN_ARRAY.equalsIgnoreCase(parameterType) 
				&& !Keys.MESSAGE_PARAMETER_TYPE_MAP_IN_ARRAY.equalsIgnoreCase(parameterType)
				&& !Keys.MESSAGE_PARAMETER_TYPE_OBJECT.equalsIgnoreCase(parameterType)) {
			message.append("\"" + param.getParameterIdentify()).append("\":");			
		}
		
		switch (parameterType.toUpperCase()) {
		case Keys.MESSAGE_PARAMETER_TYPE_OBJECT:;
		case Keys.MESSAGE_PARAMETER_TYPE_MAP_IN_ARRAY:;
		case Keys.MESSAGE_PARAMETER_TYPE_MAP:
			message.append("{");
			
			for (int i = 0; i < childParams.size(); i++) {
				paraseJsonMessage(childParams.get(i), message);
				
				if (i < childParams.size() - 1) {
					message.append(",");
				}
			}
			
			message.append("}");
			break;
		case Keys.MESSAGE_PARAMETER_TYPE_LIST:;
		case Keys.MESSAGE_PARAMETER_TYPE_ARRAY_IN_ARRAY:;
		case Keys.MESSAGE_PARAMETER_TYPE_ARRAY:
			message.append("[");
			
			for (int i = 0; i < childParams.size(); i++) {
				paraseJsonMessage(childParams.get(i), message);
				
				if (i < childParams.size() - 1) {
					message.append(",");
				}
			}
						
			message.append("]");
			break;
		case Keys.MESSAGE_PARAMETER_TYPE_STRING:
			message.append("\"" + param.getDefaultValue() + "\"");
			break;
		case Keys.MESSAGE_PARAMETER_TYPE_NUMBER:
			message.append(param.getDefaultValue());
			break;
		default:
			break;
		}
		
		return message;
	}
	
	@SuppressWarnings("rawtypes")
	private ComplexParameter viewJsonTree(Object obj, ComplexParameter parentComplexParameter, List<Parameter> params, StringBuilder parentNodePath) {
		
		ComplexParameter selfComplexParameter = null;
		if (obj != null) {
			Map mp = null;
            List ls = null;
            
            
            if (obj instanceof Map || obj instanceof LinkedHashMap) {
            	
            	mp = (LinkedHashMap)obj;           	           	
            	
            	for (Iterator ite = mp.entrySet().iterator();ite.hasNext();) {
            		Map.Entry entry = (Map.Entry) ite.next();
            		
            		selfComplexParameter = new ComplexParameter(findParamter(params, entry.getKey().toString(), parentNodePath.toString())
            				, new HashSet<ComplexParameter>(), parentComplexParameter);
            		parentComplexParameter.addChildComplexParameter(selfComplexParameter);
            		
            		if (entry.getValue() instanceof ArrayList || entry.getValue() instanceof LinkedHashMap) {
            			viewJsonTree(entry.getValue(), selfComplexParameter, params, parentNodePath.append(".").append(entry.getKey().toString()));
            		}
            		
            		
            	}
            }
            
            if (obj instanceof List || obj instanceof ArrayList) {
            	ls = (ArrayList)obj;
            	for (int i = 0;i < ls.size();i++) {     		
            		selfComplexParameter = null;
                    if (ls.get(i) instanceof LinkedHashMap) {
                    	selfComplexParameter = new ComplexParameter(new Parameter(SystemConsts.PARAMETER_MAP_IN_ARRAY_ID, Keys.MESSAGE_PARAMETER_TYPE_MAP_IN_ARRAY)
                				, new HashSet<ComplexParameter>(), parentComplexParameter);
                    }   
                    
                    if (ls.get(i) instanceof ArrayList) {
                    	selfComplexParameter = new ComplexParameter(new Parameter(SystemConsts.PARAMETER_ARRAY_IN_ARRAY_ID, Keys.MESSAGE_PARAMETER_TYPE_ARRAY_IN_ARRAY)
                				, new HashSet<ComplexParameter>(), parentComplexParameter);
                		
                    }
                    parentComplexParameter.addChildComplexParameter(selfComplexParameter);
	               	
                    viewJsonTree(ls.get(i), selfComplexParameter, params, parentNodePath);
                }
            }
			
			
		}
		return parentComplexParameter;
	}

}
