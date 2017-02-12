package com.dcits.business.user.action;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.dcits.business.base.action.BaseAction;
import com.dcits.business.user.bean.Mail;
import com.dcits.business.user.bean.User;
import com.dcits.constant.ReturnCodeConstant;
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
	
	//获取未读邮件数量
	public String getNoReadMailNum(){
		User user = (User) StrutsUtils.getSessionMap().get("user");
		int num = mailService.getNoReadNum(user.getUserId());
		jsonMap.put("mailNum", num);
		jsonMap.put("returnCode", ReturnCodeConstant.SUCCESS_CODE);
		return SUCCESS;
	}
	
	//获取收件箱列表或者发件箱列表
	//mailType=1收件箱列表   mailType=2 发件箱列表
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
		jsonMap.put("returnCode", ReturnCodeConstant.SUCCESS_CODE);
		return SUCCESS;
	}
	
	//改变邮件状态
	public String changeStatus(){
		if(statusName.equals("sendStatus")||statusName.equals("readStatus")||statusName.equals("ifValidate")){			
			if(statusName.equals("sendStatus")){
				Mail mail1 = mailService.get(model.getMailId());
				if(mail1.getReceiveUser()==null){
					jsonMap.put("returnCode", ReturnCodeConstant.MAIL_MISS_RECEIVER_CODE);
					jsonMap.put("msg", "需要选定一个收件用户才能发送!");
					return SUCCESS;
				}
				mail1.setSendTime(new Timestamp(System.currentTimeMillis()));
				mailService.edit(mail1);
			}
			mailService.changeStatus(model.getMailId(), statusName, status);
			jsonMap.put("returnCode", ReturnCodeConstant.SUCCESS_CODE);
		}else{
			jsonMap.put("returnCode", ReturnCodeConstant.MISS_PARAM_CODE);
			jsonMap.put("msg", "参数不正确!");
		}		
		return SUCCESS;
	}
	
	//获取指定mail
	public String get(){
		model = mailService.get(model.getMailId());
		model.setReceiveUserName();
		model.setSendUserName();
		if(model.getReceiveUser()!=null){
			jsonMap.put("receiveUserId",model.getReceiveUser().getUserId());
		}		
		jsonMap.put("mail", model);
		jsonMap.put("returnCode", ReturnCodeConstant.SUCCESS_CODE);		
		return SUCCESS;
	}
	
	//删除
	//目前只能删除未发送的邮件
	public String del(){
		mailService.delete(model.getMailId());
		jsonMap.put("returnCode", ReturnCodeConstant.SUCCESS_CODE);
		return SUCCESS;
	}

	//保存新的邮件信息或者更新信息
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
		jsonMap.put("returnCode", ReturnCodeConstant.SUCCESS_CODE);
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
