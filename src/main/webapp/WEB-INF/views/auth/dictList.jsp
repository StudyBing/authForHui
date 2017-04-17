<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglib.jsp" %>
<!DOCTYPE HTML>
<html>
	<head>
		<meta charset="utf-8">
		<%@include file="/WEB-INF/include/head.jsp" %>
		<%@include file="/WEB-INF/include/dtgrid.jsp" %>
		<%@include file="/WEB-INF/include/select2.jsp" %>
		<title>字典列表</title>
	</head>
	<body>
		<nav class="breadcrumb"><i class="Hui-iconfont">&#xe67f;</i> 首页 <span class="c-gray en">&gt;</span> 字典列表
			<a class="btn btn-success radius r" style="margin-top:3px" href="javascript:location.replace(location.href);" title="刷新"><i class="Hui-iconfont">&#xe68f;</i></a>
			<shiro:hasPermission name="auth:dict:save">
			<a class="btn btn-success radius r" style="margin-top:3px;margin-right:10px;" href="javascript:editRow();" title="新增"><i class="Hui-iconfont">&#xe604;</i></a>
			</shiro:hasPermission>
		</nav>
		<div class="page-container">
			<div class="cl pd-5 bg-1 bk-gray">
				<div class="row cl">
					<label class="form-label col-xs-2 col-sm-2" style="text-align: right;">类型：</label>
					<div class="formControls col-xs-4 col-sm-3">
						<select id="type" class="select">
							<option value="">全部</option>
							<c:forEach items="${types}" var="t">
							<option value="${t}">${t}</option>
							</c:forEach>
						</select>
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
			    loadURL : '${ctx}/dict/data',
			    tableClass :'table table-border table-bordered table-hover table-bg',
		 		exportFileName : '字典列表',
		 		columns :  [
				    {id:'type', title:'类型', type:'string', columnClass:'text-c'},
				    {id:'label', title:'标签名', type:'string', columnClass:'text-c'},
				    {id:'value', title:'值', type:'string', columnClass:'text-c'},
				    {id:'sort', title:'排序', type:'string', columnClass:'text-c'},
				    {id:'remarks', title:'备注信息', type:'string', columnClass:'text-c'},
				    {id:"opter",title:"操作",columnClass:'text-c',resolution:function(value,record,column,grid,dataNo,columnNo){
				    	var content = '';
				    	<shiro:hasPermission name="auth:dict:save">
						content += ' <input onclick="editRow(\''+record.id+'\');" value="修改" class="btn btn-primary size-MINI radius" type="button">';
				    	</shiro:hasPermission>
				    	<shiro:hasPermission name="auth:dict:del">
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
			});
			function search(){
				if (grid.parameters == undefined || grid.parameters == null){
					grid.parameters = {};
				}
				//使用手动方式进行DtGrid进行排序
				var sortParameter = grid.sortParameter;
				grid.parameters['sortColumn'] = sortParameter.columnId;
				grid.parameters['sortType'] = sortParameter.sortType; //排序类别 [0-不排序，1-正序，2-倒序]

				grid.parameters['type'] = $("#type").val();
				grid.refresh(true);
			}

		 	function viewRow(id){
				if (id == undefined){
					id = "";
				}
				var index = layer.open({
				  type: 2,
				  content: '${ctx}/dict/form?id='+id,
				  area: ['320px', '195px'],
				  maxmin: true
				});
				layer.full(index);
		 	}

		 	function editRow(id){
				if (id == undefined){
					id = "";
				}
				var index = layer.open({
				  type: 2,
				  content: '${ctx}/dict/form?id='+id,
				  area: ['320px', '195px'],
				  maxmin: true,
				  end:function(){
				  	search();
				  }
				});
				layer.full(index);
		 	}

		 	function deleteRow(id){
				if (id == undefined){
					id = "";
				}
				layer.confirm('确认删除吗？', {
					btn: ['确认','取消'], //按钮
					icon: 5,
					title:"提示"
				}, function(){
					$.ajax({
						url: "${ctx}/dict/del",
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

		</script>
	</body>
</html>