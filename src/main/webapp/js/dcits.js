/* -----------神州数码自动化测试平台-------------
* dcits.js V1.0
* Created & Modified by xuwangcheng
* Date modified 2017-01.22
*
* Copyright 2015-2016 神州数码系统集成服务有限公司 All rights reserved.
* Licensed under MIT license.
* http://opensource.org/licenses/MIT
*
*/
/**
 * handlebars模板 
 */
var tableTheadTemplate;
var btnTextTemplate;
var btnIconTemplate;
var formControlTemplate;

var editHtml="";
var table;

$(function(){
	//加载对应的js文件	
	var new_element=document.createElement("script");
	new_element.setAttribute("type","text/javascript");
	var r = (window.location.pathname.split("."))[0].split("/");
	r = r[r.length-1]+".js";
	new_element.setAttribute("src",r);
	document.body.appendChild(new_element);

});

//设置jQuery Ajax全局的参数  
$.ajaxSetup({  
    error: function(jqXHR, textStatus, errorThrown){  
    	layer.closeAll('dialog');
        switch (jqXHR.status){  
            case(500):  
                layer.alert("服务器系统内部错误",{icon:5});  
                break;  
            case(401):  
            	layer.alert("未登录或者身份认证过期",{icon:5});  
                break;  
            case(403):  
            	layer.alert("你的权限不够",{icon:5});  
                break;  
            case(408):  
            	layer.alert("AJAX请求超时",{icon:5});
                break;  
            default:  
            	layer.alert("AJAX调用失败",{icon:5});
        }        
    }
});

//DT的配置常量
var CONSTANT = {
		DATA_TABLES : {	
			DEFAULT_OPTION:{
			"aaSorting": [[ 1, "desc" ]],//默认第几个排序
            "bStateSave": true,//状态保存
            "processing": false,   //显示处理状态
    		"serverSide": true,  //服务器处理
        	"autoWidth": false,   //自动宽度
            "responsive": false,   //自动响应
            "language": {
                "url": "../../js/zh_CN.txt"
            },
            "lengthMenu": [[10, 15, 100], ['10', '15', '100']],  //显示数量设置
            //行回调
            "createdRow": function ( row, data, index ){
                $(row).addClass('text-c');
            }},
            //常用的COLUMN
			COLUMNFUN:{
				//过长内容省略号替代
				ELLIPSIS:function (data, type, row, meta) {
                	data = data||"";
                	return '<span title="' + data + '">' + data + '</span>';
                }
			}
		}
};


/**
 * 初始化方式
 * 通过以下方式来设置相关参数 
 * publish.renderParams = $.extend(true,publish.renderParams,mySetting);
 * 通过publish.init();来进行初始化
 */
var publish = {
     //模块初始化入口
     init : function(){
    	 var that = this;
    	//模板渲染只有一次
		 var df1 = $.Deferred();
		 df1.done(function(){
			 var df = $.Deferred();
	    	 df.done(function(){
	    		 that.renderData(that.renderParams.customCallBack);
	    		 //防止事件被绑定多次
	    		 (that.renderParams.ifFirst==true) && (that.initListeners(that.renderParams.eventList));
	    	 }); 
	    	 if(that.renderParams.renderType=="list"){
	    		 that.renderParams.listPage.beforeInit(df);
	    	 }else if(that.renderParams.renderType=="edit"){
	    		 that.renderParams.editPage.beforeInit(df);
	    	 }	    	 	   
		 });
		 if(that.renderParams.ifFirst==true){
			 that.renderTemplate(df1,that.renderParams.templateCallBack); 
		 }else{
			 df1.resolve();
		 }	
     },
     /**
      * 渲染所需参数
      * 不可选：
      * ifFirst: true 当前是否为本页面第一次初始化  防止事件被重复绑定 默认为true
      * 可选:
      * customCallBack:数据渲染 不是list  edit页面的自定义数据渲染方法 或者二次渲染 
      * templateCallBack:默认的模板渲染之后 自定义的二次渲染
      * eventList:页面event绑定 默认{}
      * renderType: 当前渲染模式 listPage还是editPage 默认为list 可选edit
      * userDefaultRender:是否使用默认的数据渲染  默认为true  可选 false为不使用,仅使用使用提供customCallBack回调方法中的渲染
      * userDefaultTemplate:是否使用默认的模板渲染  默认为true 可选false为不使用,仅使用templateCallBack回调中的默认渲染
      * 
      * 
      * list页面
      * 必须：
      * listUrl: Datatables请求数据地址 必要
      * tableObj:对应的DT容器jquery对象 必要
      * columnsSetting:列的设置
      * 
      * 可选:
      * beforeInit:执行init方法之前需要执行的方法  请在该方法最后调用df.resolve();   
      * columnsJson:不参与排序的列
      * 
      * edit页面
      * 必须：
      * editUrl:编辑请求地址 必要
      * ifUseValidate:是否使用jqueryValidate插件  默认true  可选false  如果不使用,需要自己定义提交事件
      * 可选：
      * beforeInit:执行init方法之前需要执行的参数  请在该方法最调用df.resolve();
      * formObj:对应的form容器 默认为 ".form-horizontal" 可选
      * modeFlag:指定时add还是edit操作  默认为0-add  可选 1-edit
      * objId: get实体时需要的id
      * getUrl:获取实体对象时的请求地址
      * rules:rules规则 定义验证规则
      * messages 提示 定义验证时展示的提示信息
      * closeFlag:成功提交并返回之后是否关闭当前编辑窗口  默认为true  可选false
      * ajaxCallbackFun: ajax提交中的回调函数  如传入null,则使用默认
      * renderCallback:function(obj){}  如果默认的渲染结果不是完整或者正确的,可以传入该回调重新或者附加渲染  obj 实体对象
      * 
      * 
      * templateParams参数：
      * tableTheads:必须的  传入字符串数组,渲染成指定的表头  
      * btnTools:表格上方的工具栏 带文字和图标样式按钮
	  * formControls: edit页面的表单控件渲染,参见下面的数据格式  需要某种类型的控件 必须需要将对应子节点下的flag设置为true
	  *		   [{
				edit:flase,
				required:false,
				label:"",   	//如果没有label 该控件为隐藏input 同时需要填写下面的name
				name:"",
				objText:"",
				input:[{	
					flag:flase,
					hidden:false,
					value:"",
					placeholder:"",
					name:""	
					}],
				textarea:[{
					flag:false,
					placeholder:"",
					value:"",
					name:""	
					}],
				select:[{
					flag:false,
					name:""	,
					option:[{
						value:"",
						text:"",
						selected:""  //可选"selected"
						}]
					}],
				button:[{
					flag:false,
					style:"",
					markClass:"",
					value:""		
					}]	
			}]
      * 
      */
     renderParams:{
    	 renderType:"list",
    	 userDefaultRender:true,    
    	 userDefaultTemplate:true,
    	 customCallBack:function(p){},
    	 templateCallBack:function(df){
    		 df.resolve();
    	 },
    	 eventList:{},
    	 ifFirst: true,
    	 listPage:{
    		 listUrl:"",
    		 beforeInit:function(df){
        		 df.resolve();
        	 },
        	 tableObj:null,
        	 columnsSetting:{},
        	 columnsJson:[],
        	 dt:null
    	 },
    	 editPage:{
    		 beforeInit:function(df){
        		 df.resolve();
        	 },
        	 ifUseValidate:true,
    		 modeFlag:0,
    		 objId:null,
    		 editUrl:"",
    		 saveUrl:"",
        	 formObj:".form-horizontal",
        	 getUrl:"",
        	 rules:{},
        	 messages:{},
        	 closeFlag:true,
        	 ajaxCallbackFun:null,
        	 renderCallback:function(obj){} 
    	 },  
    	 templateParams:{
    		 tableTheads:[],
    		 btnTools:[],
    		 formControls:[]
    	 }
     },
     /**
      * 在进行数据渲染之前先进行静态的模板渲染
      * @param callback
      */
     renderTemplate:function(df,callback){
    	 if(this.renderParams.userDefaultTemplate){
    		 var t = this.renderParams.templateParams;
        	 var html = "";
        	//预编译handlebars模板
    		$("#template-page").load("../template/pageTemplate.htm",function(){
    			
    			registHelper();
    			
    			btnTextTemplate = Handlebars.compile($("#btn-text-template").html());
    			btnIconTemplate = Handlebars.compile($("#btn-icon-template").html());
    			
    			if($("#table-thead")){
    				tableTheadTemplate = Handlebars.compile($("#table-thead-template").html());
    				//渲染表头
    		    	$("#table-thead").append(tableTheadTemplate(t.tableTheads));
    			}
    			
    			if($("#btn-tools")){
    				//渲染表格上方工具栏
    		    	 $("#btn-tools").append(btnTextTemplate(t.btnTools));
    			}
    			
    			if($("#edit-page")){
    				 formControlTemplate = Handlebars.compile($("#form-control-template").html());       			
	       	    	 //渲染editPage的表单控件
	       	    	 editHtml = formControlTemplate(t.formControls);
    			}
    			
    	    	 //传入回调进行二次的渲染如果对editPage做过自定义的渲染  那么必须重新定义全局变量 editHtml	    	
    	    	 callback(df);
    		}); 
    	 }else{
    		 callback(df);
    	 }

     },
     /**
      * 内部所用的函数-渲染数据 不同的页面的渲染模式  通用为list(列表页) edit(编辑增加页) 其他类型自己扩展
      * @param callback  
      */
     renderData : function(callback){ 
    	 var p = this.renderParams;
    	 
    	 //默认渲染
    	 if(p.userDefaultRender==true){
    		 if(p.renderType=="list"){
    			 var l = p.listPage;
    			 table = initDT(l.tableObj,l.listUrl,l.columnsSetting,l.columnsJson);
    		 }    		 
    		 if(p.renderType=="edit"){
    			 var e = p.editPage;
    			 var sUrl = e.editUrl;
    			 if(e.modeFlag==1){
    				 ObjectEditPage(e.objId,e.getUrl,e.renderCallback);
    			 }else{
    				 (e.saveUrl != null && e.saveUrl != "") && (sUrl = e.saveUrl);
    			 }    			 
    			 e.ifUseValidate && formValidate(e.formObj,e.rules,e.messages,sUrl,e.closeFlag,e.ajaxCallbackFun);
    		 }
    	 }	 
		 callback(p);		 
     },
     /**
      * 统一绑定监听器
      * @param eventList  事件列表 jsonObject
      * 没有传入{}
      * 格式: 
      * '.btn' : function(){}  点击事件
      * '.checkbox' : {'change' : function(){}}  其他事件  请参照jquery的event
      */
     initListeners : function(eventList){
         $(document.body).delegates(eventList);
         this.renderParams.ifFirst = false;
         this.renderParams.renderType = "edit";
     }
};



/**
 * 自定义扩展的jquery方法
 * 已配置的方式代理事件
 */
$.fn.delegates = function(configs) {
     el = $(this[0]);
     for (var name in configs) {
          var value = configs[name];
          if (typeof value == 'function') {
               var obj = {};
               obj.click = value;
               value = obj;
          };
          for (var type in value) {
               el.delegate(name, type, value[type]);
          }
     }
     return this;
};


/**
 * 
 * @param tableObj 对应表格dom的jquery对象
 * @param ajaxUrl  ajax请求数据的地址  string
 * @param columnsSetting columns设置  jsonArray
 * @param columnsJson  不参与排序的列 jsonArray
 * @returns table 返回对应的DataTable实例
 */
function initDT(tableObj,ajaxUrl,columnsSetting,columnsJson){
	//渲染前使用自定义遮罩层
	$wrapper.spinModal();
	var table = $(tableObj)
	//发送ajax之前
	.on('xhr.dt', function ( e, settings, json, xhr ) {
        if(json.returnCode!=0){
        	layer.alert(json.msg,{icon:5});
        }
    })
    //初始化完毕
    .on( 'init.dt', function () {
    	$wrapper.spinModal(false);
    } )
	.DataTable($.extend(true,{},CONSTANT.DATA_TABLES.DEFAULT_OPTION,{
		"ajax":ajaxUrl,
        "columns":columnsSetting,                                           
         "columnDefs": [{"orderable":false,"aTargets":columnsJson}]
          }));
	
	return table;
}


/**
 * 返回DT中checkbox的html
 * @param name  name属性,对象的name或者其他
 * @param val   value属性,对象的id
 * @param className  class属性,一般为select+对象
 */
function checkboxHmtl(name,val,className){
	return '<input type="checkbox" name="'+name+'" value="'+val+'" class="'+className+'">';
}


/**
 * 单个删除功能，通用
 * tip 确认提示
 * url 删除请求地址
 * id 删除的实体ID
 * obj 表格删除对应的内容
 */
function delObj(tip,url,id,obj){
	layer.confirm(tip,function(index){
		$wrapper.spinModal();
		$.post(url,{id:id},function(data){
    		if(data.returnCode==0){
    			$wrapper.spinModal(false);
    			table.row($(obj).parents('tr')).remove().draw();
                layer.msg('已删除',{icon:1,time:1500});
    		}else{
    			$wrapper.spinModal(false);
    			layer.alert(data.msg, {icon: 5});
    		}
    	});
	});
}

/**
 * 批量删除方法-表格为DT时
 * @param checkboxList  checkBox被选中的列表
 * @param url   删除url
 * @param tableObj   TD对象，默认名为table
 * @returns {Boolean}
 */
function batchDelObjs(checkboxList,url,tableObj){
	if(checkboxList.length<1){
		return false;
	}
	layer.confirm('确认删除选中的'+checkboxList.length+'条记录?',function(index){
		layer.close(index);
		$wrapper.spinModal();
		var delCount = 0;
		var errorTip = "";
			$.each(checkboxList,function(i,n){
			objId=$(n).val();//获取id
			objName=$(n).attr("name");	//name属性为对象的名称	
			layer.msg("正在删除"+objName+"...",{time:999999});    
				$.ajax({
					type:"POST",
					url:url,
					data:{id:objId},
					async:false,
					success:function(data){
						if(data.returnCode!=0){	
							layer.msg("删除"+objName+"失败!",{time:999999});
							errorTip += "["+objName+"]";
						}else{
							delCount = i+1;
							layer.msg("删除"+objName+"成功!",{time:999999});
						}
					}
					});			
			});
			layer.closeAll('dialog');
			refreshTable();
			$wrapper.spinModal(false);
			if(errorTip!=""){
				errorTip="在删除"+errorTip+"数据时发生了错误,请查看错误日志!";
				layer.alert(errorTip,{icon:5},function(index){
					layer.close(index);
					layer.msg("共删除"+delCount+"条数据!",{icon:1,time:2000});
				});
			}else{
				layer.msg("共删除"+delCount+"条数据!",{icon:1,time:2000});
			}

	});
		
}

/**
 * 编辑页面 当modeflag为1(编辑)时 默认的页面渲染
 * @param id  指定实体的id
 * @param ajaxUrl  ajax地址
 * @param callback(function(obj){}) 如果默认的渲染结果不是完整或者正确的,可以传入该回调重新渲染  obj 实体对象
 */
function ObjectEditPage(id,ajaxUrl,callback){
	$(".form-horizontal").spinModal();
	//编辑模式时将某些隐藏的控件展示
	$(".editFlag").css("display","block");
	$.post(ajaxUrl,{id:id},function(data){
		if(data.returnCode==0){
			var o=data.object;
			//默认的渲染  object中同名id 控件 以及id名为 idText
			iterObj(o);		
			//该回调可以自行渲染默认没有渲染完整的控件
			callback(o);	
			$(".form-horizontal").spinModal(false);
		}else{
			layer.alert(data.msg,{icon:5});
		}
	});
	
	
}

/**
 * 迭代循环遍历json对象中属性
 * @param jsonObj
 * @param parentName 
 */
function iterObj(jsonObj,parentName){
	$.each(jsonObj,function(k,v){
		if(parentName!=null&&parentName!=""){
			k = parentName + "\\." + k;
		}
		if(!(v instanceof Object)){	
			if($("#"+k)){
				$("#"+k).val(v);
			}
			if($("#"+k+"Text")){
				$("#"+k+"Text").text(v);
			}
		}else{
			iterObj(v,k);
		}
	});	
}



/**
 * 所有实体edit页面的jqueryValidate方法
 * @param formObj  表单obj
 * @param rules   rules规则 定义验证规则
 * @param messages 提示 定义验证时展示的提示信息
 * @param ajaxUrl  验证成功 ajax提交地址
 * @param closeFlag  成功提交并返回之后是否关闭当前窗口  true  false
 * @param ajaxCallbackFun  ajax提交中的回调函数  如传入null,则使用默认
 */
function formValidate(formObj,rules,messages,ajaxUrl,closeFlag,ajaxCallbackFun){
	console.log(messages);
	var callbackFun = function(data){
		if(data.returnCode==0){	
			refreshTable();
			if(closeFlag){
				//关闭当前的所有页面层
				layer.closeAll('page');
			}					
		}else{
			layer.alert(data.msg, {icon: 5});
		}			
	};
	if(ajaxCallbackFun!=null){
		callbackFun=ajaxCallbackFun;
	}
	$(formObj).validate({
		rules:rules,
		messages:messages,
		ignore: "",
		onkeyup:false,
		focusCleanup:true,
		success:"valid",
		submitHandler:function(form){
			var formData = $(form).serialize();
			$.post(ajaxUrl,formData,callbackFun);			
		}
	});
}

/**
 * 刷新表格
 * 所有表格页面都是的DT对象名称都要命名为table
 */
function refreshTable(){
	table.ajax.reload(null,false);

}

/**
 * 获取json长度
 * @param jsonData
 * @returns {Number}
 */
function getJsonLength(jsonData){
	var jsonLength = 0;
	for(var item in jsonData){
		jsonLength++;
	}
	return jsonLength;
}

/**
 * 注册需要的handlerbars的helper
 */
function registHelper(){	
	//比对用的helper
	//使用{{#compare people.name '==' 'peter'}} {{else}} {{/compare}}
	Handlebars.registerHelper('compare', function(left, operator, right, options) {
		if (arguments.length < 3) {
			throw new Error('Handlerbars Helper "compare" needs 2 parameters');
		}
		var operators = {
			'==': function(l, r) {return l == r; },
			'===': function(l, r) {return l === r; },
			'!=': function(l, r) {return l != r; },
			'!==': function(l, r) {return l !== r; },
			'&lt;': function(l, r) {return l < r; },
			'&gt;': function(l, r) {return l > r; },
			'&lt;=': function(l, r) {return l <= r; },
			'&gt;=': function(l, r) {return l >= r; },
			'typeof': function(l, r) {return typeof l == r; }
		};
		if (!operators[operator]) {
			throw new Error('Handlerbars Helper "compare" doesn\'t know the operator ' + operator);
		}
		var result = operators[operator](left, right);
		if (result) {
			return options.fn(this);
		} else {
			return options.inverse(this);
		}});
}

/**
 * 生成标签模板
 * 
 * @param option   
 * {
	"0":{
		btnStyle:"success",
		status:"正常"
		},
	"1":{
		btnStyle:"default",
		status:"失败"
		}
	}
 以上是默认的配置,你也可以传入自定义的配置
 * @param data   ["1","0"] 数组或者 字符串 number都可以
 * @returns {String}
 */
function labelCreate(data,option){
	var html = '';
	var datas = [];
	if(!(data instanceof Array)){
		datas.push(data);
	}
	$.each(datas,function(i,n){
        if(option && option[n]){
            html += '<span class="label label-'+option[n]["btnStyle"]+' radius">'+option[n]["status"]+'</span>';
        }else{
            if(n=="0"){
            	html += '<span class="label label-success radius">正常</span>';
            }
            if(n=="1"){
            	html += '<span class="label label-danger radius">禁用</span>';;
            }
        }
        if(datas.length>(i+1)){
        	html +="&nbsp;";
        }
    });
	return html;
}
