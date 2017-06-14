package com.dcits.business.message.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.dcits.business.message.bean.MessageScene;
import com.dcits.business.message.bean.TestResult;
import com.dcits.business.message.service.MessageSceneService;
import com.dcits.business.message.service.TestConfigService;
import com.dcits.business.message.service.TestDataService;
import com.dcits.business.message.service.TestResultService;
import com.dcits.business.user.bean.User;
import com.dcits.constant.MessageKeys;
import com.dcits.constant.ReturnCodeConsts;
import com.dcits.coretest.message.test.MessageAutoTest;
import com.dcits.util.StrutsUtils;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 接口自动化
 * 接口测试Action
 * @author xuwangcheng
 * @version 1.0.0.0,2017.2.13
 */

@Controller
@Scope("prototype")
public class AutoTestAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * LOGGER
	 */
	private static final Logger LOGGER = Logger.getLogger(AutoTestAction.class.getName());
	
	private Map<String,Object> jsonMap = new HashMap<String,Object>();
	
	@Autowired
	private MessageSceneService messageSceneService;
	@Autowired
	private TestDataService testDataService;
	@Autowired
	private TestResultService testResultService;
	@Autowired
	private TestConfigService testConfigService;
	
	private Integer messageSceneId;
	
	private Integer dataId;
	
	private String requestUrl;
	
	private String requestMessage;
	
	
	/**
	 * 单场景测试
	 * @return
	 */
	public String sceneTest() {
		User user = (User) StrutsUtils.getSessionMap().get("user");
		
		MessageScene scene = messageSceneService.get(messageSceneId);
		
		TestResult result = MessageAutoTest.singleTest(requestUrl, requestMessage, scene, testConfigService.getConfigByUserId(user.getUserId()));
		
		testResultService.save(result);
		
		if (scene.getMessage().getInterfaceInfo().getInterfaceType().equalsIgnoreCase(MessageKeys.INTERFACE_TYPE_SL) 
				&& result.getRunStatus().equals("0")) {
			testDataService.updateDataValue(dataId, "status", "1");
		}
		
		jsonMap.put("result", result);	
		jsonMap.put("returnCode", ReturnCodeConsts.SUCCESS_CODE);
		
		return SUCCESS;
	}
	
	/*********************************GET-SET**************************************************/
	public void setMessageSceneId(Integer messageSceneId) {
		this.messageSceneId = messageSceneId;
	}
	
	public void setDataId(Integer dataId) {
		this.dataId = dataId;
	}
	
	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}
	
	public void setRequestMessage(String requestMessage) {
		this.requestMessage = requestMessage;
	}
	
	public Map<String, Object> getJsonMap() {
		return jsonMap;
	}
}
