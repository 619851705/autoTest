package com.dcits.business.base.dao;

import java.util.List;

import com.dcits.business.base.bean.PageModel;
/**
 * ͨ��DAO�ӿ�
 * @author dcits
 *
 * @param <T>
 */

public interface BaseDao<T> {
	/**
	 * ����һ��ʵ��
	 * @param entity Ҫ������ʵ�� 
	 */
	 Integer save(T entity);
	
	/**
	 * ��������ɾ��һ��ʵ�� 
	 * @param id ����
	 */
	 void delete(int id);
	
	/**
	 * �༭ָ��ʵ������ϸ��Ϣ
	 * @param entity ʵ�� 
	 */
	 void edit(T entity);
	
	/**
	 * ����������ȡ��Ӧ��ʵ�� 
	 * @param id ����ֵ
	 * @return �����ѯ�ɹ������ط���������ʵ��;�����ѯʧ�ܣ�����null
	 */
	 T get(Integer id);
	
	/**
	 * ����������ȡ��Ӧ��ʵ�� 
	 * @param id ����ֵ
	 * @return �����ѯ�ɹ������ط���������ʵ��;�����ѯʧ�ܣ��׳���ָ���쳣
	 */
	 T load(Integer id);
	
	/**
	 * ��ȡ����ʵ��ʵ���б�
	 * @return ����������ʵ���б�
	 */
	 List<T> findAll();
	
	/**
	 * ͳ����ʵ��ʵ��������
	 * @return ������
	 */
	 int totalCount();
	
	/**
	 * ��ȡ��ҳ�б�
	 * @param pageNo ��ǰҳ��
	 * @param pageSize ÿҳҪ��ʾ�ļ�¼��
	 * @return ���Ϸ�ҳ�����ķ�ҳģ��ʵ��
	 */
	 PageModel<T> findByPager(int dataNo, int pageSize,String orderDataName,String orderType,String searchValue,List<String> dataParams);
	
	/**
	 * ����ָ����SQL���Ͳ���ִֵ�и������ݵĲ���
	 * @param sql SQL���
	 * @param paramValues ����ֵ����
	 */
	 void update(String sql);
	
	/**
	 * ����ָ����SQL���Ͳ���ִֵ�е�������Ĳ�ѯ����
	 * @param sql SQL���
	 * @param paramValues ����ֵ
	 * @return ����������ʵ�����
	 */
	 T findUnique(String sql);
}
