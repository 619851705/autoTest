/**
 * 
 */
package com.dcits.business.base.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dcits
 * ��ҳģ��
 */
public class PageModel<T> {
	
	//��ǰ��Ҫ�����������
	private String orderDataName;
	//����ʽ,Ĭ��asc
	private String orderType = "asc";
	//ȫ����������
	private String searchValue = "";
	//��ǰչʾ���ֶ���
	private List<String> dataParams = new ArrayList<String>();
	//��ǰ��ʼ���ݵ�λ��
	private int dataNo=0;
	//ÿҳ��ʾ�ļ�¼��
	private int pageSize=10;
	//�ܼ�¼��
	private int recordCount;
	//��ҳ��
	private int pageCount;
	//��ŷ�ҳ���ݵļ���
	private List<T> datas;
	
	public PageModel(){
		
	}
	
	public PageModel(String orderDataName, String orderType,
			String searchValue, List<String> dataParams, int dataNo,
			int pageSize) {
		super();
		this.orderDataName = orderDataName;
		this.orderType = orderType;
		this.searchValue = searchValue;
		this.dataParams = dataParams;
		this.dataNo = dataNo;
		this.pageSize = pageSize;
	}







	public List<String> getDataParams() {
		return dataParams;
	}

	public void setDataParams(List<String> dataParams) {
		this.dataParams = dataParams;
	}

	public String getOrderDataName() {
		return orderDataName;
	}

	public void setOrderDataName(String orderDataName) {
		this.orderDataName = orderDataName;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getSearchValue() {
		return searchValue;
	}

	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}

	public int getDataNo() {
		return dataNo;
	}

	public void setDataNo(int dataNo) {
		this.dataNo = dataNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}

	public int getPageCount() {
		if(this.getRecordCount()<=0){
			return 0;
		}else{
			pageCount=(recordCount+pageSize-1)/pageSize;
		}
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public List<T> getDatas() {
		return datas;
	}

	public void setDatas(List<T> datas) {
		this.datas = datas;
	}
	
	
}
