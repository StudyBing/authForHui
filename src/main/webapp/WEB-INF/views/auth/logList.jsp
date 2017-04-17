<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglib.jsp" %>
<!DOCTYPE HTML>
<html>
	<head>
		<meta charset="utf-8">
		<%@include file="/WEB-INF/include/head.jsp" %>
		<%@include file="/WEB-INF/include/dtgrid.jsp" %>
		<title>日志列表</title>
	</head>
	<body>
		<nav class="breadcrumb"><i class="Hui-iconfont">&#xe67f;</i> 首页 <span class="c-gray en">&gt;</span> 日志列表
			<a class="btn btn-success radius r" style="margin-top:3px" href="javascript:location.replace(location.href);" title="刷新"><i class="Hui-iconfont">&#xe68f;</i></a>
		</nav>
		<div class="page-container">
			<div class="cl pd-5 bg-1 bk-gray">
				<div class="row cl">
					<label class="form-label col-xs-2 col-sm-2" style="text-align: right;">创建时间：</label>
					<div class="formControls col-xs-5 col-sm-3">
						<input id="beginCreateDate" value="" onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'endCreateDate\')||\'%y-%M-%d\'}'})" type="text" class="input-text Wdate" style="width: 100px;">-
						<input id="endCreateDate" value="" onfocus="WdatePicker({minDate:'#F{$dp.$D(\'beginCreateDate\')}',maxDate:'%y-%M-%d'})" type="text" class="input-text Wdate" style="width: 100px;">
					</div>
					<div class="formControls col-xs-5 col-sm-4">
						<label for="type">
							<input id="type" name="type" type="checkbox" value="2" /> 发生异常
						</label>
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
			    loadURL : '${ctx}/log/data',
			    tableClass :'table table-border table-bordered table-hover table-bg',
		 		exportFileName : '日志列表',
		 		columns :  [
					{id:'title', title:'标题目录', type:'string', columnClass:'text-c'},
				    {id:'type', title:'日志类型', type:'string', columnClass:'text-c',resolution:function(value,record,column,grid,dataNo,columnNo){
						if(value == "1") {
							return "<span class='label label-success radius'>正常</span>";
						} else if(value == "2"){
							return "<span class='label label-danger radius'>异常</span>";
						}
						return "";
					}},
				    {id:'userId', title:'创建者', type:'string', columnClass:'text-c',resolution:function(value,record,column,grid,dataNo,columnNo){
						return value;
					}},
				    {id:'createDateStr', title:'创建时间', type:'string', columnClass:'text-c'},
				    {id:'remoteIp', title:'操作IP地址', type:'string', columnClass:'text-c'},
				    {id:'requestUri', title:'请求URI', type:'string', columnClass:'text-c'},
				    {id:'method', title:'操作方式', type:'string', columnClass:'text-c'},
				    {id:'runTimeStr', title:'耗时', type:'string', columnClass:'text-c'},
				    {id:"opter",title:"操作",columnClass:'text-c',resolution:function(value,record,column,grid,dataNo,columnNo){
				    	var content = '';
				    	<shiro:hasPermission name="auth:log:view">
				    	content += ' <input onclick="viewRow(\''+record.id+'\');" value="详情" class="btn btn-primary size-MINI radius" type="button">';
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
			});
			function search(){
				if (grid.parameters == undefined || grid.parameters == null){
					grid.parameters = {};
				}
				//使用手动方式进行DtGrid进行排序
				var sortParameter = grid.sortParameter;
				grid.parameters['sortColumn'] = sortParameter.columnId;
				grid.parameters['sortType'] = sortParameter.sortType; //排序类别 [0-不排序，1-正序，2-倒序]

				grid.parameters['type'] = $("input[name='type']:checked").val();
				grid.parameters['beginCreateDate'] = $("#beginCreateDate").val();
				grid.parameters['endCreateDate'] = $("#endCreateDate").val();
				grid.refresh(true);
			}


		 	function viewRow(id){
				var index = layer.open({
				  type: 2,
				  content: '${ctx}/log/form?id='+id,
				  area: ['320px', '195px'],
				  maxmin: true
				});
				layer.full(index);
		 	}

		</script>
	</body>
</html>