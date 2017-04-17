<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglib.jsp" %>
<!DOCTYPE HTML>
<html style="height: auto;">
	<head>
		<meta charset="utf-8">
		<%@include file="/WEB-INF/include/head.jsp" %>
		<%@include file="/WEB-INF/include/dtgrid.jsp" %>
		<%@include file="/WEB-INF/include/select2.jsp" %>
		<title>角色用户列表</title>
	</head>
	<body>
		<div class="page-container">
			<div class="cl pd-5 bg-1 bk-gray">
				<div class="row cl" style="margin-bottom: 5px;">
					<label class="form-label col-xs-2 col-sm-2" style="text-align: right;">机构代码：</label>
					<div class="formControls col-xs-5 col-sm-4">
						<input type="hidden" id="roleId" value="${roleId}" />
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
					<label class="form-label col-xs-2 col-sm-2" style="text-align: right;">登录名：</label>
					<div class="formControls col-xs-5 col-sm-4">
						<input id="username" type="text" class="input-text">
					</div>
					<label class="form-label col-xs-2 col-sm-2" style="text-align: right;">姓名：</label>
					<div class="formControls col-xs-3 col-sm-2">
						<input id="name" type="text" class="input-text">
					</div>
					<input onclick="search();" class="btn btn-primary radius" type="button" value="搜索">
					<input onclick="roleAddUser();" class="btn btn-primary radius" type="button" value="添加用户">
				</div>
			</div>
			<div>
				<div id="toolbarContainer" class="dt-grid-toolbar-container"></div>
				<div id="gridContainer" class="dt-grid-container" style="width: 100%;"></div>
				<div id="gridToolContainer" class="dt-grid-toolbar-container"></div>
			</div>
		</div>
		<script type="text/javascript">
			var grid = $.fn.DtGrid.init({
		 		lang : 'zh-cn',
		 		ajaxLoad : true,
		 		check : false,
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
					{id:"opter",title:"操作",columnClass:'text-c',headerStyle:'width:60px;',resolution:function(value,record,column,grid,dataNo,columnNo){
						var content = '';
						content += ' <input onclick="yichu(\''+record.id+'\');" value="移除" class="btn btn-primary size-MINI radius" type="button">';
						return content;
					}}
				],
		 		gridContainer : 'gridContainer',
				toolbarContainer : 'gridToolContainer',
				tools : 'refresh',
				pageSize : 10,
				pageSizeLimit : [10, 20, 50]
			});
			$(function() {
				search();
				$("select").select2();
			});

			function search(){
				if (grid.parameters == undefined || grid.parameters == null){
					grid.parameters = {};
				}
				//使用手动方式进行DtGrid进行排序
				var sortParameter = grid.sortParameter;
				grid.parameters['sortColumn'] = sortParameter.columnId;
				grid.parameters['sortType'] = sortParameter.sortType; //排序类别 [0-不排序，1-正序，2-倒序]

				grid.parameters['roleId'] = $("#roleId").val();
				grid.parameters['username'] = $("#username").val();
				grid.parameters['name'] = $("#name").val();
				grid.parameters['orgCode'] = $("#orgCode").val();
				grid.parameters['jobNo'] = $("#jobNo").val();
				grid.refresh(true);
			}

			function yichu(userId){
				layer.confirm('确认从角色中移除？', {
					btn: ['确认','取消'], //按钮
					icon: 5,
					title:"提示"
				}, function(){
					$.ajax({
						url: "${ctx}/role/roleDelUser",
						data:{"userId":userId,"roleId":"${roleId}"} ,
						dataType: "json",
						success: function (res) {
							if (res.ok) {
								search();
								layer.msg('移除角色成功', {icon: 6});
							} else {
								layer.msg("移除角色失败，请稍后再试！");
							}
						}
					});
				});
			}

			function roleAddUser() {
				var index = layer.open({
					type: 2,
					title: "选择需要添加用户",
					content: '${ctx}/role/roleAddUser?roleId=${roleId}',
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