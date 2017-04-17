<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglib.jsp" %>
<!DOCTYPE HTML>
<html>
	<head>
		<meta charset="utf-8">
		<%@include file="/WEB-INF/include/head.jsp" %>
		<%@include file="/WEB-INF/include/dtgrid.jsp" %>
		<%@include file="/WEB-INF/include/select2.jsp" %>
		<title>用户列表</title>
	</head>
	<body>
		<nav class="breadcrumb"><i class="Hui-iconfont">&#xe67f;</i> 首页 <span class="c-gray en">&gt;</span> 用户列表
			<a class="btn btn-success radius r" style="margin-top:3px" href="javascript:location.replace(location.href);" title="刷新"><i class="Hui-iconfont">&#xe68f;</i></a>
			<shiro:hasPermission name="auth:user:save">
			<a class="btn btn-success radius r" style="margin-top:3px;margin-right:10px;" href="javascript:editRow();" title="新增"><i class="Hui-iconfont">&#xe604;</i></a>
			</shiro:hasPermission>
		</nav>
		<div class="page-container">
			<div class="cl pd-5 bg-1 bk-gray">
				<div class="row cl" style="margin-bottom: 5px;">
					<label class="form-label col-xs-2 col-sm-2" style="text-align: right;">机构代码：</label>
					<div class="formControls col-xs-5 col-sm-4">
						<select id="orgCode" class="select">
							<option value="">全部</option>
							<c:forEach items="${hospitals}" var="item">
								<option value="${item.code}">${item.name}</option>
							</c:forEach>
						</select>
					</div>
					<label class="form-label col-xs-2 col-sm-2" style="text-align: right;">工号：</label>
					<div class="formControls col-xs-3 col-sm-2">
						<input id="jobNo" type="text" class="input-text">
					</div>
				</div>
				<div class="row cl" style="margin-bottom: 5px;">
					<label class="form-label col-xs-2 col-sm-2" style="text-align: right;">所属角色：</label>
					<div class="formControls col-xs-3 col-sm-2">
						<select id="roleId" class="select" onchange="changeRole();">
							<option value="">请选择</option>
							<c:forEach items="${roles}" var="item">
								<option value="${item.id}">${item.name}</option>
							</c:forEach>
						</select>
					</div>
					<div class="formControls col-xs-2 col-sm-2">
						<shiro:hasPermission name="auth:role:roleAddUser">
						<input id="roleAddUserBtn" onclick="roleAddUser();" class="btn btn-primary radius" type="button" value="批量添加用户">
						</shiro:hasPermission>
					</div>
					<label class="form-label col-xs-2 col-sm-2" style="text-align: right;">登录名：</label>
					<div class="formControls col-xs-3 col-sm-2">
						<input id="username" type="text" class="input-text">
					</div>
				</div>
				<div class="row cl">
					<label class="form-label col-xs-2 col-sm-2" style="text-align: right;">医生身份证号：</label>
					<div class="formControls col-xs-5 col-sm-4">
						<input id="idcard" type="text" class="input-text">
					</div>
					<label class="form-label col-xs-2 col-sm-2" style="text-align: right;">姓名：</label>
					<div class="formControls col-xs-3 col-sm-2">
						<input id="name" type="text" class="input-text">
					</div>
					<input onclick="search();" class="btn btn-primary radius" type="button" value="搜索">
				</div>
			</div>
			<div class="mt-20">
				<div id="toolbarContainer" class="dt-grid-toolbar-container"></div>
				<div id="gridContainer" class="dt-grid-container" style="width: 100%;"></div>
				<div id="gridToolContainer" class="dt-grid-toolbar-container"></div>
			</div>
		</div>
		<script type="text/javascript">
			var grid = $.fn.DtGrid.init({
		 		lang : 'zh-cn',
		 		ajaxLoad : true,
		 		check:false,
			    loadURL : '${ctx}/user/data',
			    tableClass :'table table-border table-bordered table-hover table-bg',
		 		exportFileName : '用户列表',
		 		columns :  [
				    {id:'username', title:'登录名', type:'string', columnClass:'text-c'},
				    {id:'name', title:'姓名', type:'string', columnClass:'text-c'},
				    {id:'orgName', title:'机构代码', type:'string', columnClass:'text-c'},
				    {id:'jobNo', title:'工号', type:'string', columnClass:'text-c'},
				    {id:'idcard', title:'医生身份证号', type:'string', columnClass:'text-c'},
				    {id:'phone', title:'联系电话', type:'string', columnClass:'text-c'},
				    {id:'intro', title:'简介', type:'string', columnClass:'text-c'},
				    {id:"opter",title:"操作",columnClass:'text-c',headerStyle:'width:80px;',resolution:function(value,record,column,grid,dataNo,columnNo){
				    	var content = '';
				    	<shiro:hasPermission name="auth:user:save">
						content += ' <input onclick="editRow(\''+record.id+'\');" value="修改" class="btn btn-primary size-MINI radius" type="button">';
				    	</shiro:hasPermission>
				    	<shiro:hasPermission name="auth:user:del">
						content += ' <input onclick="deleteRow(\''+record.id+'\');" value="删除" class="btn btn-primary size-MINI radius" type="button">';
				    	</shiro:hasPermission>
				        return content;
				    }}
				],
		 		gridContainer : 'gridContainer',
				toolbarContainer : 'gridToolContainer',
				tools : 'refresh',
				pageSize : 10,
				pageSizeLimit : [10, 20, 50],
				onHeaderClick : function(title,column,grid,columnNo,header,headerRow,e) {
					var sortParameter = grid.sortParameter;
					if (grid.parameters == null || grid.parameters == undefined) {
						grid.parameters = {};
					}
					grid.parameters['sortColumn'] = column.id;
					var sortType  = sortParameter.sortType; //排序类别 [0-不排序，1-正序，2-倒序]
                    if(sortParameter.columnId != column.id){
                        sortType = 0;
                    }
					if(sortType == 2) {
						grid.parameters['sortType'] = 0;
					} else {
						grid.parameters['sortType'] = sortType + 1;
					}
					grid.refresh(true);
				}
		 	});
			$(function() {
				search();
				$("select").select2();
				changeRole();
			});
			function search(){
				if (grid.parameters == undefined || grid.parameters == null){
					grid.parameters = {};
				}
				//使用手动方式进行DtGrid进行排序
				var sortParameter = grid.sortParameter;
				grid.parameters['sortColumn'] = sortParameter.columnId;
				grid.parameters['sortType'] = sortParameter.sortType; //排序类别 [0-不排序，1-正序，2-倒序]

				grid.parameters['username'] = $("#username").val();
				grid.parameters['name'] = $("#name").val();
				grid.parameters['orgCode'] = $("#orgCode").val();
				grid.parameters['jobNo'] = $("#jobNo").val();
				grid.parameters['idcard'] = $("#idcard").val();
				grid.parameters['roleId'] = $("#roleId").val();
				grid.refresh(true);
			}

		 	function editRow(id){
				var index = layer.open({
				  type: 2,
				  content: '${ctx}/user/form?id='+id,
				  area: ['320px', '195px'],
				  maxmin: true,
				  end:function(){
				  	search();
				  }
				});
				layer.full(index);
		 	}

			function changeRole(){
				var val = $("#roleId").val();
				if($.trim(val) == ""){
					$("#roleAddUserBtn").hide();
				} else {
					$("#roleAddUserBtn").show();
				}
			}

		 	function deleteRow(id){
				layer.confirm('确认删除吗？', {
					btn: ['确认','取消'], //按钮
					icon: 5,
					title:"提示"
				}, function(){
					$.ajax({
						url: "${ctx}/user/del",
						data:{"id":id} ,
						dataType: "json",
						success: function (res) {
							if (res.ok) {
								search();
								layer.msg('删除成功', {icon: 6});
							} else {
								layer.msg("删除失败，请稍后再试！");
							}
						}
					});
				});
		 	}

			function roleAddUser() {
				var roleId = $("#roleId").val();
				if ($.trim(roleId) == ""){
					return;
				}
				var index = layer.open({
					type: 2,
					title: "选择需要添加用户",
					content: '${ctx}/role/roleAddUser?roleId='+roleId,
					area: ['500px', '250px'],
					end: function(){
						search();
					}

				});
				layer.full(index);
			}

		</script>
	</body>
</html>