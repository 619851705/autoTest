package com.dcits.interceptor;

import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import com.dcits.business.user.bean.OperationInterface;
import com.dcits.business.user.bean.User;
import com.dcits.util.StrutsUtils;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.interceptor.Interceptor;

/**
 * ÿ���û������κ�һ�������ӿڶ����뾭����������
 * 
 * @author xwc
 *
 */
@SuppressWarnings("serial")
@Controller
public class CallMethodInterceptor extends ActionSupport implements Interceptor {
	
	private static Logger logger = Logger.getLogger(CallMethodInterceptor.class);
	
	@Override
	public void destroy() {
	}

	@Override
	public void init() {

	}

	@SuppressWarnings("unchecked")
	@Override
	public String intercept(ActionInvocation arg0) throws Exception {
		String timeTag = String.valueOf(System.currentTimeMillis());
		//����ӿ�·��
		String actionName = arg0.getProxy().getActionName();
		//��ǰ���нӿ���Ϣ
		List<OperationInterface> ops = (List<OperationInterface>) StrutsUtils.getApplicationMap().get("ops");
		logger.info("["+timeTag+"]"+"��ʼ���ýӿ�:"+actionName+",����Ȩ����֤!");
		//�жϸýӿ��Ƿ�Ϊͨ�ýӿ�(��������ӿ��б��м���Ϊ��ͨ�ýӿ�,����Ҫ�κ���֤�Ϳ��Ե���)
		int isCommon = 0;
		OperationInterface currOp = null;
		for(OperationInterface op:ops){
			if(actionName.equals(op.getCallName())){
				isCommon = 1;
				currOp = op;
				break;
			}
		}
		if(isCommon==0){
			logger.info("["+timeTag+"]"+"�ӿ�"+actionName+"δ�ڽӿ��б���,Ϊͨ�ýӿ�,�������!");
			return arg0.invoke();
		}
		
		
		//�ж��û��Ƿ��¼
		//��ȡ��ǰ��¼�û�
		User user = (User) StrutsUtils.getSessionMap().get("user");
		if(user==null){
			logger.info("["+timeTag+"]"+"�û�δ��¼,���ýӿ�"+actionName+"ʧ��!");
			return "usernotlogin";
		}
		String userTag = "["+"�û���:"+user.getUsername()+",ID="+user.getUserId()+"]";
		//�жϸýӿ��Ƿ������ɵ���
		if(!currOp.getStatus().equals("0")){
			logger.info("["+timeTag+"]"+userTag+"��ǰ�ӿ�"+actionName+"�ѱ�����!");
			return "opisdisable";
		}
		
		//�жϵ�ǰ�û��Ƿ�ӵ�е���Ȩ��
		//��ȡ��¼�û�������ɫ��Ȩ����Ϣ
		Set<OperationInterface> ops1 = user.getRole().getOis();
		int isPrivilege = 1;
		for(OperationInterface op:ops1){
			if(op.getCallName().equals(actionName)){
				isPrivilege = 0;
				break;
			}
		}
		
		if(isPrivilege==1){
			logger.info("["+timeTag+"]"+userTag+"�û�û�е��ýӿ�"+actionName+"��Ȩ��,����ʧ��!");
			return "usernotpower";
		}
		
		logger.info("["+timeTag+"]"+userTag+"��ǰ�ӿ�"+actionName+"Ȩ����֤ͨ��!");
		return arg0.invoke();

	}
}
