package com.dcits.business.base.action;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.struts2.json.annotations.JSON;
import org.springframework.beans.factory.annotation.Autowired;

import com.dcits.business.base.bean.PageModel;
import com.dcits.business.base.service.BaseService;
import com.dcits.business.system.service.GlobalSettingService;
import com.dcits.business.user.service.MailService;
import com.dcits.business.user.service.OperationInterfaceService;
import com.dcits.business.user.service.RoleService;
import com.dcits.business.user.service.UserService;
import com.dcits.util.StrutsUtils;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**
 * ͨ��Action��
 * @author dcits
 *
 * @param <T>
 */

public class BaseAction<T> extends ActionSupport implements ModelDriven<T>{

	private static final long serialVersionUID = 1L;
	
	protected BaseService<T> baseService;
	
	//ajax���÷��ص�map
	protected Map<String,Object> jsonMap=new HashMap<String,Object>();

	protected Class clazz;
	
	protected T model;
	
	protected Integer id;
		
	//dataTable�������������
	//���������֤������ǰ������չʾ������˳��
	protected Integer draw;
	//�������ݿ�ʼ�±�
	protected Integer start;
	//������������ݳ���
	protected Integer length;
	
	@Autowired
	protected UserService userService;
	@Autowired
	protected RoleService roleService;
	@Autowired
	protected OperationInterfaceService operationInterfaceService;
	@Autowired
	protected MailService mailService;
	@Autowired
	protected GlobalSettingService globalSettingService;
	
	
	/**
	 * ͨ�� list����
	 * ��ҳչʾ��Ӧʵ��ļ���
	 * @return
	 */
	public String list(){
		Map<String,Object>  dt = StrutsUtils.getDTParameters();
		PageModel<T> pu = baseService.findByPager(start, length,(String)dt.get("orderDataName"),(String)dt.get("orderType"),(String)dt.get("searchValue"),(List<String>)dt.get("dataParams"));
		jsonMap.put("draw", draw);
		jsonMap.put("data", pu.getDatas());
		jsonMap.put("recordsTotal", pu.getRecordCount());
		if(!((String)dt.get("searchValue")).equals("")){
			jsonMap.put("recordsFiltered", pu.getDatas().size());
		}else{
			jsonMap.put("recordsFiltered", pu.getRecordCount());
		}
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	/**
	 * ͨ�÷��� listAll
	 * ��ȡ���еĶ�Ӧʵ�弯��
	 * @return
	 */
	public String listAll(){
		List<T> ts = baseService.findAll();
		jsonMap.put("data", ts);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	
	/**
	 * ͨ�÷��� del
	 * ���ݴ����idɾ����Ӧʵ��
	 * @return
	 */
	public String del(){
		baseService.delete(id);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	/**
	 * ͨ�÷��� get
	 * ����id��ȡָ��ʵ����Ϣ
	 * @return
	 */
	public String get(){
		jsonMap.put("returnCode", 0);
		jsonMap.put("object", baseService.get(id));
		return SUCCESS;
	}
	
	/**
	 * ͨ�÷��� edit
	 * �༭ʵ�� ��ʹ�ø����д˷��� ��֤��ǰ̨����������������ģ�����������������д�÷���
	 * @return
	 */
	public String edit(){
		baseService.edit(model);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	/**
	 * ͨ�÷��� save
	 * ��֤�µ�ʵ�����  ͬedit() ��֤�����������������  ����������������д�÷���
	 * @return
	 */
	public String save(){
		baseService.save(model);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	/**
	 * ͨ�����䶯̬�Ĵ�������
	 */
	public BaseAction() {
		ParameterizedType type = (ParameterizedType) this.getClass()
				.getGenericSuperclass();
		clazz = (Class) type.getActualTypeArguments()[0];
		try {
			model = (T) clazz.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * ��ʼ����baseService �滻Ϊָ����***Service
	 * ����˵���ο�BaserServiceImpl�е�init����
	 * @throws Exception
	 */
	@PostConstruct
	public void init() throws Exception
	{
		String clazzName = clazz.getSimpleName();
		String clazzServiceName = clazzName.substring(0,1).toLowerCase() + clazzName.substring(1) + "Service";//toLowerCase����ĸСд
		Field serviceNameField = this.getClass().getSuperclass().getDeclaredField(clazzServiceName);
		Object object = serviceNameField.get(this);
		Field baseServiceNameField = this.getClass().getSuperclass().getDeclaredField("baseService");
		baseServiceNameField.set(this, object);		
	}
	
	/************************************GET-SET***********************************************/
	public void setDraw(Integer draw) {
		this.draw = draw;
	}
	
	public void setStart(Integer start) {
		this.start = start;
	}
	
	public void setLength(Integer length) {
		this.length = length;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	@JSON(serialize=false)
	@Override
	public T getModel() {
		// TODO Auto-generated method stub
		return model;
	}
	
	public Map<String, Object> getJsonMap() {
		return jsonMap;
	}

}
