package com.dcits.business.user.action;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.dcits.business.base.action.BaseAction;
import com.dcits.business.user.bean.Mail;
import com.dcits.business.user.bean.User;
import com.dcits.util.StrutsUtils;

@Controller
@Scope("prototype")
public class MailAction extends BaseAction<Mail>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	private Integer mailType;
	
	private String statusName;
	
	private String status;
	
	private Integer receiveUserId;
	
	//��ȡδ���ʼ�����
	public String getNoReadMailNum(){
		User user = (User) StrutsUtils.getSessionMap().get("user");
		int num = mailService.getNoReadNum(user.getUserId());
		jsonMap.put("mailNum", num);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//��ȡ�ռ����б���߷������б�
	//mailType=1�ռ����б�   mailType=2 �������б�
	public String listMails(){
		User user = (User) StrutsUtils.getSessionMap().get("user");
		List<Mail> mails = new ArrayList<Mail>();
		if(mailType==1){
			mails = mailService.findReadMails(user.getUserId());
		}else{
			mails = mailService.findSendMails(user.getUserId());
		}
		for(Mail mail:mails){
			mail.setSendUserName();
			mail.setReceiveUserName();
			if(mail.getIfValidate().equals("0")&&mailType==1){
				mail.setMailInfo("");
			}
		}
		jsonMap.put("data", mails);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//�ı��ʼ�״̬
	public String changeStatus(){
		if(statusName.equals("sendStatus")||statusName.equals("readStatus")||statusName.equals("ifValidate")){			
			if(statusName.equals("sendStatus")){
				Mail mail1 = mailService.get(model.getMailId());
				if(mail1.getReceiveUser()==null){
					jsonMap.put("returnCode", 3);
					jsonMap.put("msg", "��Ҫѡ��һ���ռ��û����ܷ���!");
					return SUCCESS;
				}
				mail1.setSendTime(new Timestamp(System.currentTimeMillis()));
				mailService.edit(mail1);
			}
			mailService.changeStatus(model.getMailId(), statusName, status);
			jsonMap.put("returnCode", 0);
		}else{
			jsonMap.put("returnCode", 2);
			jsonMap.put("msg", "��������ȷ!");
		}		
		return SUCCESS;
	}
	
	//��ȡָ��mail
	public String get(){
		model = mailService.get(model.getMailId());
		model.setReceiveUserName();
		model.setSendUserName();
		if(model.getReceiveUser()!=null){
			jsonMap.put("receiveUserId",model.getReceiveUser().getUserId());
		}		
		jsonMap.put("mail", model);
		jsonMap.put("returnCode", 0);		
		return SUCCESS;
	}
	
	//ɾ��
	//Ŀǰֻ��ɾ��δ���͵��ʼ�
	public String del(){
		mailService.delete(model.getMailId());
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}

	//�����µ��ʼ���Ϣ���߸�����Ϣ
	public String save(){
		User user = (User) StrutsUtils.getSessionMap().get("user");
		
		model.setReadStatus("1");
		model.setSendStatus("1");
		model.setSendUser(user);
		
		if(receiveUserId!=null){
			User receiveUser = new User();
			receiveUser.setUserId(receiveUserId);
			model.setReceiveUser(receiveUser);
		}
		
		if(model.getMailId()==null){
			Integer id = mailService.save(model);
			jsonMap.put("mailId", id);
		}else{
			mailService.edit(model);
		}
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	/****************************************************************************************************************/
	
	public void setMailType(Integer mailType) {
		this.mailType = mailType;
	}
	
	public void setReceiveUserId(Integer receiveUserId) {
		this.receiveUserId = receiveUserId;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

}
