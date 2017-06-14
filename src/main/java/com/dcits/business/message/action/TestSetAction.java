package com.dcits.business.message.action;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.dcits.business.base.action.BaseAction;
import com.dcits.business.message.bean.TestSet;
import com.dcits.business.message.service.TestSetService;
import com.dcits.business.user.bean.User;
import com.dcits.constant.ReturnCodeConsts;
import com.dcits.util.StrutsUtils;

/**
 * 接口自动化<br>
 * 测试集Action
 * @author xuwangcheng
 * @version 1.0.0.0,20170518
 *
 */
@Controller
@Scope("prototype")
public class TestSetAction extends BaseAction<TestSet> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private TestSetService testSetService;
	
	@Autowired
	public void setTestSetService(TestSetService testSetService) {
		super.setBaseService(testSetService);
		this.testSetService = testSetService;
	}

	@Override
	public String edit() {
		// TODO Auto-generated method stub
		if (model.getSetId() == null) {
			model.setCreateTime(new Timestamp(System.currentTimeMillis()));
			model.setUser((User)(StrutsUtils.getSessionMap().get("user")));
		} else {
			model.setMs(testSetService.get(model.getSetId()).getMs());
		}
		
		testSetService.edit(model);
		
		jsonMap.put("returnCode", ReturnCodeConsts.SUCCESS_CODE);
		return SUCCESS;
	}
	
	
	
}
