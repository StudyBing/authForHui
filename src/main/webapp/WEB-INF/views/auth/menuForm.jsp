<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglib.jsp" %>
<!DOCTYPE HTML>
<html style="height: auto;">
<head>
	<meta charset="utf-8">
	<%@include file="/WEB-INF/include/head.jsp" %>
	<%@include file="/WEB-INF/include/validation.jsp" %>
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/lib/zTree/css/zTreeStyle/zTreeStyle.min.css" />
	<script type="text/javascript" src="${ctxStatic}/lib/zTree/js/jquery.ztree.core-3.5.min.js"></script>
	<title>菜单维护</title>
	<script type="text/javascript">
		var menuZTree = undefined;
		var setting = {
			view: {
				dblClickExpand: false
			},
			data: {
				simpleData: {
					enable: true
				}
			},
			callback: {
				onClick: onClick
			}
		};
		var zNodes =[
			<c:forEach items="${list}" var="item" varStatus="sta">
			<c:if test="${sta.index>0}">,</c:if>
			{id:"${item.id}", pId:"${item.pid}", name:"${item.name}"<c:if test="${item.pid eq '0'}">,open:true</c:if>}
			</c:forEach>
		];

		function onClick(e, treeId, treeNode) {
			var zTree = $.fn.zTree.getZTreeObj("menuTree"),
					nodes = zTree.getSelectedNodes(),
					v = "";
			if (nodes.length == 1) {
				selectNode(nodes[0]);
			}
			hideMenu();
		}

		function selectNode(node) {
			$("#menuHidden").attr("value", node.id);
			var s = "";
			while(node != null){
				if(s.length>0){
					s = " > " + s;
				}
				s = node.name + s
				node = node.getParentNode();
			}
			$("#menuPid").attr("value", s);
		}

		function initSelectNode(id) {
			var node = menuZTree.getNodeByParam('id',id);
			if (node != undefined && node != null) {
				selectNode(node);
				menuZTree.selectNode(node);
			}
		}

		function showMenu() {
			var cityObj = $("#menuPid");
			var cityOffset = $("#menuPid").position();
			$("#menuContent").css({left:cityOffset.left + "px", top:cityOffset.top + cityObj.outerHeight() + "px"}).slideDown("fast");

			$("body").bind("mousedown", onBodyMenuDown);
		}
		function hideMenu() {
			$("#menuContent").fadeOut("fast");
			$("body").unbind("mousedown", onBodyMenuDown);
		}
		function onBodyMenuDown(event) {
			if (!(event.target.id == "menuBtn" || event.target.id == "menuContent" || $(event.target).parents("#menuContent").length>0)) {
				hideMenu();
			}
		}

		var layerLoadIndex = undefined;
		function beforeCall(form, options){
			layerLoadIndex = layer.load();
			return true;
		}
		function ajaxValidationCallback(status, form, result, options){
			layer.close(layerLoadIndex);
			if(status === true){
				if (result.ok){
					var index = parent.layer.getFrameIndex(window.name);
					if(index != undefined) {
						parent.layer.msg(result.msg, {icon: 6});
						parent.layer.close(index);
					} else {
						Hui_update_iframe("菜单列表","${ctx}/menu/list");
					}
				} else {
					layer.msg(result.msg, {icon: 5});
				}
			} else {
				layer.msg('删除失败', {icon: 5});
			}
		}
		$(document).ready(function(){
			menuZTree = $.fn.zTree.init($("#menuTree"), setting, zNodes);
			<c:if test="${menu != null && not empty menu.pid}">initSelectNode("${menu.pid}");</c:if>
			jQuery('#submitForm').validationEngine({
				ajaxFormValidation: true,
				promptPosition: "centerRight",
				ajaxFormValidationMethod: 'post',
				onAjaxFormComplete: ajaxValidationCallback,
				onBeforeAjaxFormValidation: beforeCall
			});
		});
	</script>
</head>
<body>
<div class="page-container" style="margin: 10px;background-color: #fff; border: 1px solid #ddd;border-radius: 4px;">
	<form id="submitForm" action="${ctx}/menu/save" method="post" class="form form-horizontal">
		<input type="hidden" name="id" value="${menu.id}"/>
		<%@include file="/WEB-INF/include/token.jsp" %>
		<legend>菜单维护</legend>
		<c:if test="${showRoot}">
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3">父节点：</label>
			<div class="formControls col-xs-7 col-sm-5">
				<input type="hidden" id="menuHidden" name="pid" value=""/>
				<input id="menuPid" type="text" readonly class="input-text"/>
				<div id="menuContent" class="zTreeContent" >
					<ul id="menuTree" class="ztree" style="margin-top:0; width:168px;"></ul>
				</div>
			</div>
			<div class="formControls col-xs-1 col-sm-1">
				<input id="menuSelectBtn" onclick="showMenu();" class="btn btn-primary size-S radius" type="button" value="选择">
			</div>
		</div>
		</c:if>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3">菜单名称：</label>
			<div class="formControls col-xs-8 col-sm-6">
				<input name="name" value="${menu.name}" type="text" class="validate[required] input-text" placeholder="菜单名称">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3">菜单链接：</label>
			<div class="formControls col-xs-8 col-sm-6">
				<input name="href" value="${menu.href}" type="text" class="input-text" placeholder="菜单链接">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3">菜单目标：</label>
			<div class="formControls col-xs-8 col-sm-6">
				<input name="target" value="${menu.target}" type="text" class="input-text" placeholder="菜单目标">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3">菜单图标：</label>
			<div class="formControls col-xs-8 col-sm-6">
				<input name="icon" value="${menu.icon}" type="text" class="input-text" placeholder="菜单图标">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3">权限标识：</label>
			<div class="formControls col-xs-8 col-sm-6">
				<input name="permission" value="${menu.permission}" type="text" class="input-text" placeholder="权限标识">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3">是否显示：</label>
			<div class="formControls col-xs-8 col-sm-6">
				<div class="radio-box">
					<input<c:if test="${menu.display eq '1'}"> checked="checked"</c:if> type="radio" id="isShow-1" name="display" value="1" class="validate[required]">
					<label for="isShow-1">显示</label>
				</div>
				<div class="radio-box">
					<input<c:if test="${menu.display eq '0'}"> checked="checked"</c:if> type="radio" id="isShow-0" name="display" value="0" class="validate[required]">
					<label for="isShow-0">隐藏</label>
				</div>
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3">菜单排序：</label>
			<div class="formControls col-xs-8 col-sm-6">
				<input name="sort" value="${menu.sort}" type="text" class="validate[required,custom[integer],min[0]] input-text" placeholder="排序">
			</div>
		</div>
		<div class="row cl">
			<div class="col-xs-8 col-sm-9 col-xs-offset-4 col-sm-offset-3">
				<input class="btn btn-primary radius" type="submit" value="提交">
			</div>
		</div>
	</form>
</div>
</body>
</html>