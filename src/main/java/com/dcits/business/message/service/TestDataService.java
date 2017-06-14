package com.dcits.business.message.service;

import com.dcits.business.base.service.BaseService;
import com.dcits.business.message.bean.TestData;

public interface TestDataService extends BaseService<TestData>{
	
	/**
	 * 更新某个属性
	 * @param dataId
	 * @param dataName 属性名
	 * @param dataValue 要更新的值
	 */
	void updateDataValue(Integer dataId, String dataName, String dataValue);
	
	/**
	 * 通过数据标记来查找测试数据
	 * @param dataDisrc
	 * @param messageSceneId
	 * @return
	 */
	TestData findByDisrc(String dataDiscr, Integer messageSceneId);
}
