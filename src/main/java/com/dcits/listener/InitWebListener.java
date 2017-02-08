package com.dcits.listener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.dcits.business.system.bean.GlobalSetting;
import com.dcits.business.system.service.GlobalSettingService;
import com.dcits.business.user.bean.OperationInterface;
import com.dcits.business.user.service.OperationInterfaceService;

/**
 * ��ʼ��Web����-���ص�ǰ�����ӿ��б�������վȫ������
 * @author Administrator
 *
 */
public class InitWebListener implements ServletContextListener{
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		ServletContext context = arg0.getServletContext();
		  //ȡ��appliction������
		ApplicationContext ctx =WebApplicationContextUtils.
		getRequiredWebApplicationContext(context);
		  //ȡ���ض�bean
		OperationInterfaceService opService =(OperationInterfaceService)ctx.getBean("operationInterfaceService");
		GlobalSettingService settingService = (GlobalSettingService) ctx.getBean("globalSettingService");
		//��ȡ��ǰϵͳ�����нӿ���Ϣ  
		List<OperationInterface> ops = opService.findAll();
		for(OperationInterface op:ops){
			op.setParentOpId();
		}
		context.setAttribute("ops", ops);
		
		//��ȡ��վȫ��������Ϣ
		List<GlobalSetting> settings = settingService.findAll();
		Map<String,GlobalSetting> globalSettingMap = new HashMap<String,GlobalSetting>();
		for(GlobalSetting g:settings){
			globalSettingMap.put(g.getSettingName(), g);
		}
		context.setAttribute("settingMap", globalSettingMap);		
		
	}
	
}
