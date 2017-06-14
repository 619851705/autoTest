package com.dcits.business.message.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.struts2.json.annotations.JSON;

import com.dcits.constant.MessageKeys;

public class ComplexParameter implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	private Parameter selfParameter;
	
	private Set<ComplexParameter> childComplexParameters = new HashSet<ComplexParameter>();
	
	private ComplexParameter parentComplexParameter;
	
	private Message message;
	

	public ComplexParameter(Integer id, Parameter selfParameter,
			Set<ComplexParameter> childComplexParameters,
			ComplexParameter parentComplexParameter) {
		super();
		this.id = id;
		this.selfParameter = selfParameter;
		this.childComplexParameters = childComplexParameters;
		this.parentComplexParameter = parentComplexParameter;
	}
	
	public ComplexParameter(Parameter selfParameter,
			Set<ComplexParameter> childComplexParameters,
			ComplexParameter parentComplexParameter) {
		super();
		this.selfParameter = selfParameter;
		this.childComplexParameters = childComplexParameters;
		this.parentComplexParameter = parentComplexParameter;
	}




	public ComplexParameter() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	public List<Parameter> getEnableSettingDataParameter(List<Parameter> params) {
		
		if (params == null) {
			params = new ArrayList<Parameter>();
		}
		
		String parameterType = this.selfParameter.getType();
		if (parameterType.equalsIgnoreCase(MessageKeys.MESSAGE_PARAMETER_TYPE_NUMBER)
				 || parameterType.equalsIgnoreCase(MessageKeys.MESSAGE_PARAMETER_TYPE_STRING)) {
			params.add(this.selfParameter);
		} else {
			for (ComplexParameter cp:this.getChildComplexParameters()) {
				cp.getEnableSettingDataParameter(params);
			}
		}
		return params;
	}
	
	@JSON(serialize=false)
	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}
	
	@JSON(serialize=false)
	public ComplexParameter getParentComplexParameter() {
		return parentComplexParameter;
	}

	public void setParentComplexParameter(ComplexParameter parentComplexParameter) {
		this.parentComplexParameter = parentComplexParameter;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Parameter getSelfParameter() {
		return selfParameter;
	}

	public void setSelfParameter(Parameter selfParameter) {
		this.selfParameter = selfParameter;
	}
	
	@JSON(serialize=false)
	public Set<ComplexParameter> getChildComplexParameters() {
		return childComplexParameters;
	}

	public void setChildComplexParameters(
			Set<ComplexParameter> childComplexParameters) {
		this.childComplexParameters = childComplexParameters;
	}

	public void addChildComplexParameter(ComplexParameter p) {
		if (p != null) {
			this.childComplexParameters.add(p);
		}
	}
	
	@Override
	public String toString() {
		return "ComplexParameter [id=" + id + ", selfParameter=" + selfParameter + ", childComplexParameters="
				+ childComplexParameters + "]";
	}
	
	
}
