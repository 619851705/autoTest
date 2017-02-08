package com.dcits.business.system.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.dcits.business.base.action.BaseAction;
import com.dcits.business.system.bean.GlobalSetting;
import com.dcits.util.StrutsUtils;

@Controller
@Scope("prototype")
public class GlobalSettingAction extends BaseAction<GlobalSetting>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;	
	
	//��ȡ��ǰ������Ϣ
	@SuppressWarnings("unchecked")
	public String getWebSettings(){
		Map<String,GlobalSetting> settingMap = (Map<String, GlobalSetting>) StrutsUtils.getApplicationMap().get("settingMap");
		
		for(GlobalSetting setting:settingMap.values()){
			jsonMap.put(setting.getSettingName(), setting.getSettingValue()==null?setting.getDefaultValue():setting.getSettingValue());
		}
		jsonMap.put("returnCode", 0);
		return SUCCESS;
		
	}
	
	//��ȡ�б�
	public String list(){
		List<GlobalSetting> settings = globalSettingService.findAll();
		for(GlobalSetting g:settings){
			if(g.getSettingValue()==null){
				g.setSettingValue("");
			}
		}
		jsonMap.put("data", settings);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//�༭������
	public String edit(){
		for (Map.Entry<String, Object> entry:StrutsUtils.getParametersMap().entrySet()){
			globalSettingService.updateSetting(entry.getKey(), ((String[])entry.getValue())[0]);
		}
		List<GlobalSetting> settings = globalSettingService.findAll();
		Map<String,GlobalSetting> globalSettingMap = new HashMap<String,GlobalSetting>();
		for (GlobalSetting g:settings){
			globalSettingMap.put(g.getSettingName(), g);
		}
		StrutsUtils.getApplicationMap().put("settingMap", globalSettingMap);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}

}
