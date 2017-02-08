package com.dcits.business.user.action;

import java.util.List;
import java.util.Set;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.dcits.business.base.action.BaseAction;
import com.dcits.business.user.bean.OperationInterface;
import com.dcits.business.user.bean.Role;
import com.dcits.util.StrutsUtils;

@Controller
@Scope("prototype")
public class RoleAction extends BaseAction<Role>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String delOpIds;
	
	private String addOpIds;
	
	
	//չʾ��ǰ�����еĽ�ɫ
	@Override
	public String listAll(){
		List<Role> roles=roleService.findAll();
		for(Role r:roles){
			r.setOiNum();
		}
		jsonMap.put("returnCode", 0);
		jsonMap.put("data", roles);
		return SUCCESS;
	}
	
	//ɾ����ɫ,���ǲ���ɾ��Ԥ�õĹ���Ա�˻�
	public String del(){
			Role role = roleService.get(model.getRoleId());
			String roleName = role.getRoleName();
			if(roleName.equals("admin")||roleName.equals("default")){
				jsonMap.put("returnCode", 2);
				jsonMap.put("msg", "����ɾ����������Ա��ɫ����Ĭ�Ͻ�ɫ");
				return SUCCESS;
			}
			roleService.delete(model.getRoleId());
			//ɾ��������ɫ,���øý�ɫ���û������default��ɫ
			jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//����roleId����ָ����role��Ϣ
	public String get(){
		Role role = roleService.get(model.getRoleId());
		jsonMap.put("role", role);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//�༭��������role��Ϣ
	public String edit(){
		//�ж�roleName�ĺϷ���:�����ظ�
		Role r = roleService.get(model.getRoleName());
		if(r!=null){
			if((model.getRoleId()!=null&&r.getRoleId()!=model.getRoleId())||model.getRoleId()==null){
				jsonMap.put("returnCode", 2);
				jsonMap.put("msg", "�ý�ɫ���Ѵ���,�����!");
				return SUCCESS;
			}			
		}
		if(model.getRoleId()!=null){
			//�޸�
			model.setOis(roleService.get(model.getRoleId()).getOis());
		}
		roleService.edit(model);		
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//��ȡ��ǰ���еĲ����ӿ��б�
	//���ҶԵ�ǰ��ɫ��ӵ�еĲ����ӿڴ���
	@SuppressWarnings("unchecked")
	public String getNodes(){		
		List<OperationInterface> ops = (List<OperationInterface>) StrutsUtils.getApplicationMap().get("ops");				
		Role role = roleService.get(model.getRoleId());
		Set<OperationInterface> ownOps = role.getOis();
		
		for(OperationInterface op:ops){
			op.setIsOwn(false);			
			for(OperationInterface op1:ownOps){
				if((int)op1.getOpId()==(int)op.getOpId()){
					op.setIsOwn(true);
				}
			}
		}
				
		jsonMap.put("interfaces", ops);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//���½�ɫ��Ȩ����Ϣ
	//���½�ɫ������ӿڵĹ�����ϵ
	public String updateRolePower(){
		Role role = roleService.get(model.getRoleId());
		Set<OperationInterface> ops = role.getOis();
		//�������ӵ�Ȩ��
		if(addOpIds!=null&&!addOpIds.equals("")){
			String[] addOpArray = addOpIds.split(",");
			for(String s:addOpArray){
				OperationInterface o = new OperationInterface();
				o.setOpId(Integer.valueOf(s));
				ops.add(o);
			}
			
		}
		//����ɾ����Ȩ��
		if(delOpIds!=null&&!delOpIds.equals("")){
			String[] delOpArray = delOpIds.split(",");
			for(String s:delOpArray){
				OperationInterface o = new OperationInterface();
				o.setOpId(Integer.valueOf(s));
				ops.remove(o);
			}
		}
		role.setOis(ops);
		roleService.edit(role);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	/********************************************************************************************/
	public void setDelOpIds(String delOpIds) {
		this.delOpIds = delOpIds;
	}
	
	public void setAddOpIds(String addOpIds) {
		this.addOpIds = addOpIds;
	}
	
}
