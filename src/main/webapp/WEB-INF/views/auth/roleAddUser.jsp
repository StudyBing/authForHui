<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglib.jsp" %>
<!DOCTYPE HTML>
<html style="height: auto;">
	<head>
		<meta charset="utf-8">
		<%@include file="/WEB-INF/include/head.jsp" %>
		<%@include file="/WEB-INF/include/dtgrid.jsp" %>
		<%@include file="/WEB-INF/include/select2.jsp" %>
		<title>角色添加用户</title>
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
					<input onclick="saveUsers();" class="btn btn-primary radius" type="button" value="保存选择">
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
		 		check : true,
			    loadURL : '${ctx}/user/data',
			    tableClass :'table table-border table-bordered table-hover table-bg',
		 		exportFileName : '用户列表',
		 		columns :  [
				    {id:'username', title:'登录名', type:'string', columnClass:'text-c'},
				    {id:'name', title:'姓名', type:'string', columnClass:'text-c'},
				    {id:'orgName', title:'机构代码', type:'string', columnClass:'text-c'},
				    {id:'jobNo', title:'工号', type:'string', columnClass:'text-c'},
				    {id:'idcard', title:'医生身份证号', type:'string', columnClass:'text-c'},
				    {id:'phone', title:'联系电话', type:'string', columnClass:'text-c'}
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

				grid.parameters['notInRole'] = $("#roleId").val();
				grid.parameters['username'] = $("#username").val();
				grid.parameters['name'] = $("#name").val();
				grid.parameters['orgCode'] = $("#orgCode").val();
				grid.parameters['jobNo'] = $("#jobNo").val();
				grid.refresh(true);
			}
			function saveUsers() {
				var rows = grid.getCheckedRecords();
				var userIds = "";
				for (var i = 0; i < rows.length; i++) {
					if (i > 0) {
						userIds += ",";
					}
					userIds += rows[i].id;
				}
                $.ajax({
                    url: "${ctx}/role/roleSaveUser",
                    data: {roleId: "${roleId}", userIds: userIds},
                    type: "POST",
                    success: function (result) {
                        if (result.ok){
                            parent.layer.msg(result.msg, {icon: 6});
                            var index = parent.layer.getFrameIndex(window.name);
                            parent.layer.close(index);
                        } else {
                            layer.msg(result.msg, {icon: 5});
                        }
                    }
                })
			}

		</script>
	</body>
</html>