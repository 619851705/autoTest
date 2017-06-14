package com.dcits.business.message.service;

import java.util.List;

import com.dcits.business.base.service.BaseService;
import com.dcits.business.message.bean.MessageScene;
import com.dcits.business.message.bean.TestSet;

/**
 * 
 * @author xuwangcheng
 * @version 1.0.0.0,20170518
 *
 */

public interface TestSetService extends BaseService<TestSet> {
	
	/**
	 * 获取不在指定测试集下的测试场景<br>
	 * 同时该场景对应的测试接口和测试报文状态均为0(正常)
	 * @param setId
	 * @return
	 */
	List<MessageScene> getEnableAddScenes(Integer setId);
}
