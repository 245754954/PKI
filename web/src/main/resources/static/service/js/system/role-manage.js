$(function(){
    $.jgrid.defaults.styleUI = 'Bootstrap';

    var user = {
	    id: "userManageTable",	//表格id
	    seItem: null,		//选中的条目
	    table: null,
	    layerIndex: -1
	};

	/**
	 * 初始化表格的列
	 */
	user.initColumn = function () {
	    var columns = [
	    		{name: 'id', index: 'id', sortable: false, align: 'center', hidden:true,width: '15%'},
	            {name : 'name',index : 'name',width : 90},
	            {name : 'description',index : 'description',width : 100},
	            {label: '详情', name: '',index: 'oper', width: 100, align: 'center', sortable: false,
	            	formatter:function (cellvalue, options, rowObject) {
	                var d = "<a class='btn btn-primary btn-xs' href='javascript:void(0)' onclick='javascript:detail(\""+rowObject.id+"\")'>编辑权限</a>";
	            	    return d;
	              }
	           }
	        ]
	    return columns;
	};

	user.initNames = function () {
	    var names = ['id', '角色名称','角色描述', '操作'];
	    return names;
	}
	
	detail = function(id){	// 跳转查看详情页,也可自行弹窗显示
    	window.location.href= user.url.detailUrl + id;
    }
	
	// 接口地址
	user.url = {
		searchUrl : '/ser/searchRole',	// 查询地址
		deleteUrl : '/micromvc/uc/roleList/delInfo', 			// 删除地址
		createUrl : '/ser/addRole', 		// 创建地址
		updateUrl : '/micromvc/uc/roleList/updateInfo', 		// 更新地址
		detailUrl : '/ser/toRolePermissionPage?role_id='		// 编辑权限地址
	}
	var table_list = $("#table_list");	// 弹出窗口id
	
	pageInit();
	
	function pageInit(){
	    var column = user.initColumn();
	    var name = user.initNames();
	    var loading;// 遮罩层
	    table_list.jqGrid(
	        {
	        	beforeRequest: function () {
	        		loading = layer.load();	// 开启加载遮罩
	                table_list.jqGrid("clearGridData");
	            },	//请求前的函数
	            url: user.url.searchUrl,
	            loadui: "Disable",
	            datatype: "json",
	            deepempty: true,
	            sortable: false,
	            height: 450,
	            autowidth: true,
	            shrinkToFit: true,
	            colNames : name,
	            colModel : column,
	            rowNum : 20,
	            rowList : [ 10, 20, 30 ],
	            sortname: 'id',
	            sortorder: 'desc',
	            pager : '#pager',
	            viewrecords : true,
	            rownumbers:true,//添加左侧行号
	            jsonReader: {
	                rows: "rows", //json中的viewJsonData，对应Page中的 viewJsonData。
	                page: "page", //json中curPage，当前页号,对应Page中的curPage。
	                total: "total",//总的页数，对应Page中的pageSizes
	                records: "records",//总记录数，对应Page中的totalRecords
	                repeatitems: false,
	            },
	            loadComplete: function () {//使用loadComplete方法替换gridComplete，gridComplete会被其他事件(clearGridData等)触发
		            var ids = table_list.jqGrid('getDataIDs');
		            for (var i = 0; i < ids.length; i++) {
		                var curRowData = table_list.jqGrid('getRowData', ids[i]);
		                var id = curRowData['id'];
		                console.info(id);
		            }
		            layer.close(loading);//数据加载完成后，取消遮罩
	            },
	            gridComplete: function () {
	            },
	            hidegrid: false
	        }
	    );
	
	    // Add selection
	    table_list.setSelection(4, true);
	
	    // Add responsive to jqGrid
	    $(window).bind('resize', function () {
	        var width = $('.jqGrid_wrapper').width();
	        table_list.setGridWidth(width);
	    });
	}
	
	function getContextPath() {
	    var contextPath = document.location.pathname;
	    var index = contextPath.substr(1).indexOf("/");
	    contextPath = contextPath.substr(0, index + 1);
	    delete index;
	    return contextPath;
	}
	
	//loading
	$("#search").click(function () {
	    doSerch();
	});
	
	doSerch = function () {
		var role_id = $("#s_role_id").val();
	    var role_name = $("#s_role_name").val();
	    table_list.jqGrid('setGridParam', {
	        url: user.url.searchUrl,
	        postData: {'role_id': role_id,'role_name':role_name},
	        page: 1
	    }).trigger("reloadGrid");
	}
	
	//清空查询form，重新查询
	$("#reSearch").click(function () {
		$("#s_role_id").val('');
	    $("#s_role_name").val('');
		doSerch();
	})
	
	//添加按钮
	$("#add").click(function() {
	    clearForm();
	    $(this).attr("href","#modal-form");
	    $("#div_title").text("新建角色");
	    $("#modal-form :input").removeAttr("readonly");
	    $("#close").hide();
    	$("#modal-form :submit").show();
	});


	// 请空新增文本编辑框
	clearForm = function(){
		$("#id").val('');
		$("#role_id").val('');
	    $("#role_name").val('');
	    $("#role_type").val('');
	    validator.resetForm();	// 重置表单验证
	}
	
	//修改按钮
    $("#modify").click(function(){
        clearForm();
        var rowData = getRowData($(this), table_list);
        if (rowData != "") {
            $(this).attr("href","#modal-form");
            setDataToForm(rowData);
            $("#div_title").text("修改角色");
            if(rowData.role_type == "system"){
            	$("#modal-form :input").attr("readonly","");
            	$("#close").show();
            	$("#modal-form :submit").hide();
            }else{
            	$("#modal-form :input").removeAttr("readonly");
            	$("#role_id").attr("readonly","");
            	$("#close").hide();
            	$("#modal-form :submit").show();
            }
        }
    });
    
    //获取列表行数据
    function getRowData(target, table_list) {
        var id = table_list.jqGrid("getGridParam", "selrow");
        if (id != null && id.length != 0) {
            return table_list.jqGrid('getRowData', id);
        }else{
        	target.attr("href","");	// 防止在没选中数据的时候弹出窗口
            swal("请选择一行记录！", "", "");
            return "";
        }
    }

    //将后台获取到的数据，写到页面中
    function setDataToForm(data) {
        $('#id').val(data.id);
        $('#role_id').val(data.role_id);
        $('#role_name').val(data.role_name);
        $('#role_type').val(data.role_type);
    }
    
    // 删除数据角色,和其子项
    $("#delete").click(function() {
		var rowData = getRowData($(this), table_list);
		if(rowData==null || rowData=="" || typeof(rowData) == "undefined"){
			return;
		}
		swal({ 
	        title: "警告！",  
	        text: "您确定要删除该角色项？",  
	        type: "warning", 
	        showCancelButton: true,
	        confirmButtonText: "确定",
	        cancelButtonText: "取消",
	        closeOnConfirm: false,
	        confirmButtonColor: "#ec6c62" 
	    }, function() {
	        if (rowData && rowData.id) {
	        	$.ajax({
	                type: "POST",
	                url: user.url.deleteUrl,
	                data: {id:rowData.id, role_id:rowData.role_id},
	                dataType: "json",
	                success: function (obj) {
                        if (obj.resultCode == "000"){
	                        swal("删除成功！", "", "success");
	                    }else{
	                    	swal("删除失败！", "", "error");
	                    }
	                    table_list.trigger("reloadGrid");
	                },
	                error: function (xhr) {
	                }
	            });
	        }
	    });
	});
    
    // validate signup form on keyup and submit 添加文本框验证与提交
    var icon = "<i class='fa fa-times-circle'></i> ";
    var validator = $("#editForm").validate({
        rules: {
            role_id: {
                required: true,
                minlength: 1,
                maxlength: 50
            },
            role_name: {
                required: true,
                minlength: 1,
                maxlength: 50
            },
            role_type: {
                required: true
            }
        },
        messages: {
            role_id: {
                required: icon + "请输入角色标识",
                minlength: icon + "角色标识必须2个字符以上",
                maxlength: icon + "角色标识必须50个字符以内"
            },
            role_name: {
                required: icon + "请输入角色名称",
                minlength: icon + "角色名称必须2个字符以上",
                maxlength: icon + "角色名称必须50个字符以内"
            },
            role_type: {
            	required: icon + "请选择角色类型",
            }
        },
        submitHandler: function (form) {
        	var formUrl = user.url.createUrl;
        	var id=$("#id").val();
        	if(id!=null && id!=0 && id.length>1){
        		formUrl = user.url.updateUrl;
        	}
            $.ajax({
                type: "POST",
                url: formUrl,
                data: $(form).serialize(),
                dataType: "json",
                success: function (obj) {
                    if (obj.resultCode == "000") {
                        clearForm();
                        $("#modal-form-closer").click();
                        swal("保存成功！", "", "success");
                        table_list.trigger("resetSelection");	// 取消选中
                        table_list.trigger("reloadGrid");		// 重新加载数据
                    }else{
                    	swal("保存失败！", obj.msg, "error");
                    }
                },
                error: function (xhr) {
                }
            });
            return false;
        }
    });
});
