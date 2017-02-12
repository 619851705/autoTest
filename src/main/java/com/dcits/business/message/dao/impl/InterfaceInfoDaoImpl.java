package com.dcits.business.message.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dcits.business.base.dao.impl.BaseDaoImpl;
import com.dcits.business.message.bean.InterfaceInfo;
import com.dcits.business.message.dao.InterfaceInfoDao;
import com.dcits.util.PracticalUtils;

@Repository("interfaceInfoDao")
public class InterfaceInfoDaoImpl extends BaseDaoImpl<InterfaceInfo> implements InterfaceInfoDao{
	
	public List<InterfaceInfo> findInterfaceByCondition(String condition){
		String hql = "";
		if(PracticalUtils.isNumeric(condition)){
			hql = "From InterfaceInfo t where t.interfaceId=:id or t.interfaceName like :name or t.interfaceCnName like :cnName";
			return getSession().createQuery(hql).setInteger("id",Integer.parseInt(condition)).setString("name","%"+condition+"%").setString("cnName", "%"+condition+"%").list();
		}else{
			hql = "From InterfaceInfo t where t.interfaceName like :name or t.interfaceCnName like :cnName";
			return getSession().createQuery(hql).setString("name","%"+condition+"%").setString("cnName", "%"+condition+"%").list();
		}
	}

	@Override
	public void changeStatus(int id, String status) {
		String hql = "update InterfaceInfo t set t.status=:status where t.interfaceId=:id";
		getSession().createQuery(hql).setString("status", status).setInteger("id", id).executeUpdate();
		
	}

	@Override
	public InterfaceInfo findInterfaceByName(String interfaceName) {
		String hql = "From InterfaceInfo i where i.interfaceName=:interfaceName";		
		return (InterfaceInfo) getSession().createQuery(hql).setString("interfaceName", interfaceName).uniqueResult();
	}
}
