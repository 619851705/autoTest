package com.dcits.business.user.action;

import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.dispatcher.SessionMap;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.dcits.business.base.action.BaseAction;
import com.dcits.business.user.bean.Role;
import com.dcits.business.user.bean.User;
import com.dcits.util.MD5Util;
import com.dcits.util.StrutsUtils;

@Controller
@Scope("prototype")
public class UserAction extends BaseAction<User>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;	
	
	private String mode;
	
	private Integer roleId;

	private static Logger logger = Logger.getLogger(UserAction.class);
	
	//�û���½
	public String toLogin(){
	
		model = userService.login(model.getUsername(), MD5Util.code(model.getPassword()));
		User user = (User)StrutsUtils.getSessionMap().get("user");
		int returnCode;
		String msg;
		if(model!=null){
			if(user!=null&&user.getUserId()==model.getUserId()){
				jsonMap.put("returnCode", 4);
				jsonMap.put("msg", "���ѵ�¼���˺�,���л�����ͬ���˺�!");
				return SUCCESS;
			}
			if(model.getStatus().equals("0")){
				returnCode=0;
				msg="";
				//���û���Ϣ����session��
				StrutsUtils.getSessionMap().put("user", model);			
				model.setLastLoginTime(new Timestamp(System.currentTimeMillis()));
				userService.edit(model);
				logger.info("�û�"+model.getRealName()+"[ID="+model.getUserId()+"]"+"��¼�ɹ�!");
			}else{
				returnCode=2;
				msg="����˺��ѱ�����,����ϵ����Ա���н�����";
			}
			
		}else{
			returnCode=1;
			msg="�˺Ż����벻��ȷ,����������!";
		}
		jsonMap.put("returnCode", returnCode);
		jsonMap.put("msg", msg);
		return SUCCESS;
		
	}
	
	//�û��ǳ�
	@SuppressWarnings("rawtypes")
	public String logout(){
		logger.info("�û�"+((User)StrutsUtils.getSessionMap().get("user")).getRealName()+"�ѵǳ�!");
		((SessionMap)StrutsUtils.getSessionMap()).invalidate();
		jsonMap.put("returnCode", 0);			
		return SUCCESS;
	}
	
	//�ж��û��Ƿ��½
	public String judgeLogin(){
		User user=(User)StrutsUtils.getSessionMap().get("user");
		if(user!=null){
			jsonMap.put("returnCode", 0);
		}else{
			jsonMap.put("returnCode", 1);
		}
		return SUCCESS;

	}
	
	//��ȡ�ѵ�¼�����û��Ļ�����Ϣ
	public String getLoginUserInfo(){
		
		User user=(User)StrutsUtils.getSessionMap().get("user");		
		
		if(user!=null){
			jsonMap.put("data", user);			
			jsonMap.put("returnCode", 0);
		}else{
			jsonMap.put("returnCode", 1);
		}
		return SUCCESS;
	}
	
	//��¼�û��޸��Լ�����ʵ����
	public String editMyName(){
		User user = (User)StrutsUtils.getSessionMap().get("user");		
		userService.updateRealName(model.getRealName(), user.getUserId());
		user.setRealName(model.getRealName());
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//��֤��ǰ����
	public String verifyPasswd(){
		User user=(User)StrutsUtils.getSessionMap().get("user");
		if(user.getPassword().equals(MD5Util.code(model.getPassword()))){
			jsonMap.put("returnCode", 0);			
		}else{
			jsonMap.put("returnCode", 2);
			jsonMap.put("msg", "������֤ʧ��!");
		}		
		return SUCCESS;
	}

	//�޸�����
	public String modifyPasswd(){
		User user=(User)StrutsUtils.getSessionMap().get("user");
		userService.resetPasswd(user.getUserId(), MD5Util.code(model.getPassword()));
		user.setPassword(MD5Util.code(model.getPassword()));
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//ɾ��ָ���û�
	@Override
	public String del(){
		if(userService.get(id).getUsername().equals("admin")){
			jsonMap.put("returnCode", 2);
			jsonMap.put("msg", "����ɾ��Ԥ�ù���Ա�û�!");
			return SUCCESS;
		}
		userService.delete(id);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//�������߽����û�
	public String lock(){
		if(model.getUsername().equals("admin")){
			jsonMap.put("returnCode", 2);
			jsonMap.put("msg", "��������Ԥ�ù���Ա�û�!");
			return SUCCESS;
		}
		userService.lockUser(model.getUserId(), mode);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
		
	}
	
	//��������
	public String resetPwd(){
		userService.resetPasswd(model.getUserId(),MD5Util.code("111111"));
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	
	//�û��༭
	@Override
	public String edit(){
		User u1 = userService.validateUsername(model.getUsername(),model.getUserId());
		if(u1!=null){
			jsonMap.put("returnCode", 2);
			jsonMap.put("msg", "�û����Ѵ���!");
			return SUCCESS;
		}
		Role r = new Role();
		r.setRoleId(roleId);
		model.setRole(r);
		if(model.getUserId()==null){
			//����
			model.setIfNew("1");
			model.setCreateTime(new Timestamp(System.currentTimeMillis()));
			model.setPassword(MD5Util.code("111111"));
			model.setStatus("0");
			model.setLastLoginTime(new Timestamp(System.currentTimeMillis()));
		}else{
			//�޸�
			User u2 = userService.get(model.getUserId());
			model.setIfNew(u2.getIfNew());
			model.setPassword(u2.getPassword());			
		}
		userService.edit(model);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	
	//������ѯ
	public String filter(){
		List<User> users = userService.findByRealName(model.getRealName());
		if(users.size()==0){
			jsonMap.put("returnCode", 2);
			jsonMap.put("msg", "û�в�ѯ��ָ�����û�");
		}else{
			jsonMap.put("returnCode", 0);		
		}
		jsonMap.put("data",users );
		return SUCCESS;
	}
	
	//����
	/*public String quartzTest(){
		HttpServletRequest request = ServletActionContext.getRequest();
		ServletContext sc=request.getSession().getServletContext();
		ApplicationContext ac = WebApplicationContextUtils.getRequiredWebApplicationContext(sc);
		JobManager obj = (JobManager) ac.getBean("jobManager");
		AutoTask task = new AutoTask("����", "0", 0, "0 4 19 * * ?", 0, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), "0");
    	task.setTaskId(1);
		try {
			obj.stopTask(task);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		jsonMap.put("returnCode", 0);
		return SUCCESS;
		
	}*/
	
	/*****************************GET-SET******************************************************/
	
	public void setMode(String mode) {
		this.mode = mode;
	}
	
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
}
