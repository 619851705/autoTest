package com.dcits.business.message.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dcits.business.base.dao.impl.BaseDaoImpl;
import com.dcits.business.message.bean.MessageScene;
import com.dcits.business.message.bean.TestSet;
import com.dcits.business.message.dao.TestSetDao;

/**
 * 
 * @author xuwangcheng
 * @version 1.0.0.0,20170518
 *
 */

@Repository("testSetDao")
public class TestSetDaoImpl extends BaseDaoImpl<TestSet> implements TestSetDao {

	@Override
	public List<MessageScene> getEnableAddScenes(Integer setId) {
		// TODO Auto-generated method stub
		/*String hql = "From MessageScene m where m.message.status='0' and m.message.interfaceInfo.status='0'";
		List<MessageScene> allScenes = getSession().createQuery(hql).setCacheable(true).list();*/
		String sql = "select * from at_message_scene where scene_id not in (select message_scene_id from at_set_scene where set_id=:setId)";
		
		
		return null;
	}

}
