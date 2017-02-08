package com.dcits.interceptor;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@Controller
public class InterceptorAction extends ActionSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static Logger logger = Logger.getLogger(InterceptorAction.class);
	//ajax���÷��ص�map
	private Map<String,Object> jsonMap=new HashMap<String,Object>();
	
		
	public String noLogin(){
		jsonMap.put("returnCode", 7);
		jsonMap.put("msg", "�㻹û�е�½���ߵ�½��ʧЧ,�����µ�½");
		logger.info("�û�û�е�¼,����ͨ��!");
		return SUCCESS;
		
	}
		
	public String noPower(){
		jsonMap.put("returnCode", 8);
		jsonMap.put("msg", "��û��Ȩ�޽��д˲���");
		logger.info("�û�Ȩ�޲���,����ͨ��!");
		return SUCCESS;
	}
		
	public String error(){
		jsonMap.put("returnCode", 1);
		jsonMap.put("msg", "ϵͳ�ڲ�����,���Ժ�����");
		logger.error(ActionContext.getContext().getValueStack().findValue("exception"));
		logger.error("ϵͳ�ڲ�����,����ʧ��!");
		return SUCCESS;
	}
	
	public String opDisable(){
		jsonMap.put("returnCode", 11);
		jsonMap.put("msg", "�ò����ӿ��ѱ����ý�ֹ����!");
		logger.info("�ò����ӿ��ѱ����ý�ֹ����!");
		return SUCCESS;
	}
	
	public String opNotfound(){
		jsonMap.put("returnCode", 13);
		jsonMap.put("msg", "δ����Ĳ����ӿ�");
		logger.info("�����ڸýӿ�!");
		return SUCCESS;
	}
	
	public String scriptUpload(){
		jsonMap.put("returnCode", 10);
		jsonMap.put("msg", "�ļ��ϴ��ɹ�!");
		return SUCCESS;
	}
	
	public Map<String, Object> getJsonMap() {		
		return jsonMap;
	}	
}
