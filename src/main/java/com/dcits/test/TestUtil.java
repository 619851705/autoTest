package com.dcits.test;

import java.util.HashSet;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import com.dcits.business.message.bean.ComplexParameter;
import com.dcits.business.message.bean.Parameter;
import com.dcits.coretest.message.parse.MessageParse;
import com.dcits.util.message.XmlUtil;

/**
 * Test相关
 * @author xuwangcheng
 * @version 1.0.0.0,2017.2.14
 */

public class TestUtil {
	
	@Test
	public void testJsonUtil() throws Exception{
		
		Parameter p1 = new Parameter("ROOT", "", "", "", "Map");
		p1.setParameterId(1);
		
		Parameter p2 = new Parameter("ReturnCode", "", "0000", "", "String");
		p2.setParameterId(2);
		
		Parameter p3 = new Parameter("msg", "", "ok", "", "String");
		p3.setParameterId(3);
		
		Parameter p4 = new Parameter("data", "", "", "", "Array");
		p4.setParameterId(4);
		
		Parameter p5 = new Parameter("userid", "", "11", "", "Number");
		p5.setParameterId(5);
		
		Parameter p6 = new Parameter("username", "", "aa", "", "String");
		p5.setParameterId(6);
		
		Parameter p7 = new Parameter("", "", "", "", "array_map");
		p7.setParameterId(7);
		
		Parameter p8 = new Parameter("", "", "", "", "array_array");
		p8.setParameterId(8);
		
		Parameter p9 = new Parameter("user", "", "", "", "Map");
		p9.setParameterId(9);
		
		Parameter p10 = new Parameter("", "", "", "", "Object");
		p10.setParameterId(10);
		
		ComplexParameter complexParam10 = new ComplexParameter(10, p10, new HashSet<ComplexParameter>(), null);
		
		ComplexParameter complexParam1 = new ComplexParameter(1, p1, new HashSet<ComplexParameter>(), complexParam10);		
						
		ComplexParameter complexParam2 = new ComplexParameter(2, p2, new HashSet<ComplexParameter>(), complexParam1);
		
		ComplexParameter complexParam3 = new ComplexParameter(3, p3, new HashSet<ComplexParameter>(), complexParam1);
		
		ComplexParameter complexParam4 = new ComplexParameter(4, p4, new HashSet<ComplexParameter>(), complexParam1);
		
		ComplexParameter complexParam7 = new ComplexParameter(7, p7, new HashSet<ComplexParameter>(), complexParam4);
		
		ComplexParameter complexParam5 = new ComplexParameter(5, p5, new HashSet<ComplexParameter>(), complexParam7);
		
		ComplexParameter complexParam6 = new ComplexParameter(6, p6, new HashSet<ComplexParameter>(), complexParam7);	
		
		ComplexParameter complexParam9 = new ComplexParameter(9, p9, new HashSet<ComplexParameter>(), complexParam4);
		
		
		
		complexParam10.addChildComplexParameter(complexParam1);
		
	/*	complexParam10.addChildComplexParameter(complexParam2);
		complexParam10.addChildComplexParameter(complexParam3);
		complexParam10.addChildComplexParameter(complexParam4);*/
		
		complexParam1.addChildComplexParameter(complexParam2);
		complexParam1.addChildComplexParameter(complexParam3);
		complexParam1.addChildComplexParameter(complexParam4);	
		
		complexParam7.addChildComplexParameter(complexParam5);
		complexParam7.addChildComplexParameter(complexParam6);
		
		complexParam4.addChildComplexParameter(complexParam7);
		complexParam4.addChildComplexParameter(complexParam7);
		
		/*complexParam9.addChildComplexParameter(complexParam5);
		complexParam9.addChildComplexParameter(complexParam6);*/
		
		System.out.println(MessageParse.getParseInstance("json").depacketizeMessageToString(complexParam10));
		System.out.println(MessageParse.getParseInstance("xml").depacketizeMessageToString(complexParam10));
		System.out.println(MessageParse.getParseInstance("url").depacketizeMessageToString(complexParam10));
	}
	
	@Test
	public void someTest() throws Exception {
		String xmlStr = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>	<ROOT>		<data>			<username>aa</username>		</data>		<data>			<username>aa</username>		</data>		<ReturnCodeXXX>0000</ReturnCodeXXX>	</ROOT>";
		String jsonStr = "{\"ROOT\":{\"data\":[{\"username\":\"aa\"},{\"username\":\"aa\"}],\"ReturnCodeXXX\":\"0000\"}}";
		Map map = XmlUtil.Dom2Map(xmlStr);
		System.out.println(map.toString());
		ObjectMapper mapper = new ObjectMapper();
		System.out.println(mapper.readValue(jsonStr, Map.class));
	}
}	
