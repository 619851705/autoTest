package com.dcits.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.dcits.business.system.bean.GlobalSetting;
import com.opensymphony.xwork2.ActionContext;

/**
 * struts2 ������
 * @author Administrator
 *
 */
public class StrutsUtils{
	@SuppressWarnings("unchecked")
	/*
	 * ��ȡrequestMap
	 */
	public static Map<String,Object> getRequestMap(){
		return (Map<String, Object>)ActionContext.getContext().get("request");
	}
	
	/*
	 * ��ȡsessionMap
	 */
	public static Map<String,Object> getSessionMap(){
		return ActionContext.getContext().getSession();
	}
	
	/*
	 * ��ȡapplicationMap
	 */
	public static Map<String,Object> getApplicationMap(){
		return ActionContext.getContext().getApplication();
	}
	
	/*
	 * ��ȡparameterMap
	 */
	public static Map<String,Object> getParametersMap(){
		//((String[])parameters.get("name"))[0]
		return ActionContext.getContext().getParameters();
	}
	
	/*
	 * ��ȡDT���͵Ĳ���
	 * 
	 */
	public static Map<String,Object> getDTParameters(){
		Map<String,Object> returnMap = new HashMap<String,Object>();		
		//�������һ��λ��
		String orderColumnNum = ServletActionContext.getRequest().getParameter("order[0][column]");
		//����ʽ asc����desc
		String orderType = ServletActionContext.getRequest().getParameter("order[0][dir]");
		//ȫ����������
		String searchValue = ServletActionContext.getRequest().getParameter("search[value]");
		//��Ҫ�������һ����������
		String orderDataName = ServletActionContext.getRequest().getParameter("columns["+orderColumnNum+"][data]");
		//��ȡ��ǰ���е�չʾ�ֶ�
		Map<String, String[]> params = ServletActionContext.getRequest().getParameterMap();
		List<String> dataParams = new ArrayList<String>();
		for(Map.Entry<String, String[]> entry:params.entrySet()){
			if(entry.getKey().indexOf("][data]")!=-1){
				String a = (params.get(entry.getKey()))[0];
				if(!a.equals("")){
					dataParams.add(a);
				}
			}
		}
		returnMap.put("orderDataName", orderDataName);
		returnMap.put("orderType", orderType);
		returnMap.put("searchValue", searchValue);
		returnMap.put("dataParams", dataParams);
		return returnMap;
	}
	
	/*
	 * ��ȡactionName
	 */
	public static String getActionName(){
		return ActionContext.getContext().getName();
	}
	
	/*
	 * ��ȡspring������
	 * 
	 */
	public static ApplicationContext getApplicationContext(){
		HttpServletRequest request = ServletActionContext.getRequest();
		ServletContext sc=request.getSession().getServletContext();
		return WebApplicationContextUtils.getRequiredWebApplicationContext(sc);
	}
	
	/*
	 * ��ȡȫ��������
	 * 
	 */
	@SuppressWarnings("unchecked")
	public static String getSettingValue(String settingKey){
		Map<String,GlobalSetting> settingMap = (Map<String, GlobalSetting>) StrutsUtils.getApplicationMap().get("settingMap");
		GlobalSetting setting = settingMap.get(settingKey);
		if(setting!=null){
			return setting.getSettingValue()==null?setting.getDefaultValue():setting.getSettingValue();
		}else{
			return null;
		}
		
	}
	
	/*
	 * ��ȡ��Ŀ��·��
	 * 
	 */
	public static String getProjectPath(){
		ActionContext ac = ActionContext.getContext();
        ServletContext sc = (ServletContext) ac.get(ServletActionContext.SERVLET_CONTEXT);
        return sc.getRealPath("");
	}
}
