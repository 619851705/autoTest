package com.dcits.business.base.service.impl;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.dcits.business.base.bean.PageModel;
import com.dcits.business.base.dao.BaseDao;
import com.dcits.business.base.service.BaseService;
import com.dcits.business.system.dao.GlobalSettingDao;
import com.dcits.business.user.dao.MailDao;
import com.dcits.business.user.dao.OperationInterfaceDao;
import com.dcits.business.user.dao.RoleDao;
import com.dcits.business.user.dao.UserDao;

/**
 * ͨ��serviceʵ����
 * @author dcits
 *
 * @param <T>
 */
public class BaseServiceImpl<T> implements BaseService<T>{
	
	private Class clazz=null;
	
	protected BaseDao<T> baseDao;
	
	@Autowired
	protected UserDao userDao;
	@Autowired
	protected GlobalSettingDao globalSettingDao;
	@Autowired
	protected OperationInterfaceDao operationInterfaceDao;
	@Autowired
	protected MailDao mailDao;
	@Autowired
	protected RoleDao roleDao;
	
	
	public BaseServiceImpl(){
		ParameterizedType type=(ParameterizedType)this.getClass().getGenericSuperclass();
		clazz=(Class)type.getActualTypeArguments()[0];
	}

	/**
	 * ����������ڹ��캯����spring����ע��֮��ִ��
	 * @Title: init
	 * @Description: TODO(ͨ��������ʵ����baseDao)
	 * @param @throws Exception �趨�ļ�
	 * @return void ��������
	 */
	@PostConstruct
	public void init() throws Exception
	{
		// ������Ӧ��clazz,����Ӧ  ****Dao ��ֵ��BaseDao����
		// 1: ��ȡ��ǰclazz������,Ȼ���ȡ��Ӧ��������
		String clazzName = clazz.getSimpleName();
		// 2:Account===>account===>account+Dao  Category====>CategoryDao
		String clazzDaoName = clazzName.substring(0,1).toLowerCase() + clazzName.substring(1) + "Dao";//toLowerCase����ĸСд
		// 3: ͨ��clazzDaoName��ȡ��Ӧ Field�ֶ�    this.getClass().getSuperclass():��ȡ����ӦBaseServiceImpl
		Field daoNameField = this.getClass().getSuperclass().getDeclaredField(clazzDaoName);
		Object object = daoNameField.get(this);
		// 4: ��ȡbaseDao �ֶ�
		Field baseDaoNameField = this.getClass().getSuperclass().getDeclaredField("baseDao");
		baseDaoNameField.set(this, object);		
	}
	
	@Override
	public Integer save(T entity) {
		// TODO Auto-generated method stub
		return baseDao.save(entity);
	}

	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub
		baseDao.delete(id);		
	}

	@Override
	public void edit(T entity) {
		// TODO Auto-generated method stub
		baseDao.edit(entity);		
	}

	@Override
	public T get(Integer id) {
		// TODO Auto-generated method stub
		return baseDao.get(id);
	}

	@Override
	public T load(Integer id) {
		// TODO Auto-generated method stub
		return baseDao.load(id);
	}

	@Override
	public List<T> findAll() {
		// TODO Auto-generated method stub
		return baseDao.findAll();
	}

	@Override
	public int totalCount() {
		// TODO Auto-generated method stub
		return baseDao.totalCount();
	}

	@Override
	public PageModel<T> findByPager(int dataNo, int pageSize,String orderDataName,String orderType,String searchValue,List<String> dataParams) {
		// TODO Auto-generated method stub
		return baseDao.findByPager(dataNo, pageSize,orderDataName,orderType,searchValue,dataParams);
	}
	
	
}
