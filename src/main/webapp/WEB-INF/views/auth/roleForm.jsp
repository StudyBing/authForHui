<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglib.jsp" %>
<!DOCTYPE HTML>
<html style="height: auto;">
<head>
	<meta charset="utf-8">
	<%@include file="/WEB-INF/include/head.jsp" %>
	<%@include file="/WEB-INF/include/validation.jsp" %>
	<%@include file="/WEB-INF/include/select2.jsp" %>
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/lib/zTree/css/zTreeStyle/zTreeStyle.min.css" />
	<script type="text/javascript" src="${ctxStatic}/lib/zTree/js/jquery.ztree.all-3.5.min.js"></script>
	<title>角色维护</title>
	<script type="text/javascript">

		$.extend($.validationEngineLanguage.allRules,{
			onlyName: {
				'url': '${ctx}/role/onlyName', /* 验证程序地址 */
				'extraData': 'id=${role.id}', /* 额外参数 */
				'alertTextOk': '',
				'alertText': '角色名称已经存在',
				'alertTextLoad': '正在验证角色名称唯一性...'
			}
		});

		var menuZTree = undefined;
		var organTree = undefined;
		var setting = {
			check:{enable:true,nocheckInherit:true,chkboxType:{ "Y" : "ps", "N" : "s" }},
			view:{selectedMulti:false},
			data:{simpleData:{enable:true}},
			callback:{
				beforeClick:function(id, node){
					menuZTree.checkNode(node, !node.checked, true, true);
					return false;
				},
				onCheck:function(event, treeId, treeNode){
					var nodes = menuZTree.getCheckedNodes(true);
					var html1 = "";
					for(var i=0; i<nodes.length; i++) {
						html1 += "<input type='hidden' name='menuIds' value='"+nodes[i].id+"'/>";
					}
					$("#menuIdsDiv").html(html1);
				},
				onExpand:function(event, treeId, treeNode) {
					menuZTree.expandNode(treeNode,true,true,true,false);
				}
			}
		};
		var setting2 = {
			check:{enable:true,nocheckInherit:true,chkboxType:{ "Y" : "ps", "N" : "ps" }},
			view:{
				selectedMulti:false,
				addDiyDom: addDiyDom
			},
			data:{simpleData:{enable:true}},
			callback:{
				beforeClick:function(id, node){
					organTree.checkNode(node, !node.checked, true, true);
					return false;
				},
				onCheck:function(event, treeId, treeNode){
					var nodes = organTree.getNodesByFilter(function(node){
						return node.id != "pid" && node.checked;
					});
					var html2 = "";
					for(var i=0; i<nodes.length; i++) {
						html2 += "<input type='hidden' name='organIds' value='"+nodes[i].id+"'/>";
					}
					$("#organIdsDiv").html(html2);
				}
			}
		};
		var zNodes =[
			<c:forEach items="${list}" var="item" varStatus="sta">
			<c:if test="${sta.index>0}">,</c:if>
			{id:"${item.id}", pId:"${item.pid}", name:"${item.name}"<c:if test="${fns:contains(role.menuIds, item.id)}">,checked:true</c:if>}
			</c:forEach>
		];
		var zNodes2 =[
			{id:"pid", pId:"0", name:"机构树"}
			<c:forEach items="${organs}" var="item" varStatus="sta">
			,{id:"${item.id}", pId:"pid", name:"${item.name}"<c:if test="${fns:contains(role.organIds, item.id)}">,checked:true</c:if>}
			</c:forEach>
		];

		function addDiyDom(treeId, treeNode) {
			if (treeNode.parentNode && treeNode.parentNode.id!=2) return;
			var node = $("#" + treeNode.tId + "_a");
			if (treeNode.id == "pid") {
				var input = "<input type='text' id='searchOrgan'/>"
				node.after(input);
				$("#searchOrgan").bind("keyup",function (event) {
					var value = $(this).val();
					if(organTree == null) {
						return;
					}
					var nodes = organTree.getNodesByParam("isHidden", true);
					organTree.showNodes(nodes);
					if(value == ""){
						return;
					}
					if(!/^[\u4e00-\u9fa5]+$/i.test(value)){
						return;
					}
					var filterNodes = organTree.getNodesByFilter(function(node){
						return node.level == 1 && node.name.indexOf(value) == -1;
					});
					organTree.hideNodes(filterNodes);

				});
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
						Hui_update_iframe("角色列表","${ctx}/role/list");
					}
				} else {
					layer.msg(result.msg, {icon: 5});
				}
			} else {
				layer.msg('保存失败', {icon: 5});
			}
		}
		function showOrgan(){
			var value = $("#dataScope").val();
			if(value == "2"){
				$("#organDiv").show();
			} else {
				$("#organDiv").hide();
			}
		}
		$(document).ready(function(){
			menuZTree = $.fn.zTree.init($("#menuTree"), setting, zNodes);
			menuZTree.expandAll(true);
			organTree = $.fn.zTree.init($("#organTree"), setting2, zNodes2);
			organTree.expandAll(true);
			jQuery('#submitForm').validationEngine({
				ajaxFormValidation: true,
				promptPosition: "centerRight",
				ajaxFormValidationMethod: 'post',
				validateNonVisibleFields:true,
				onAjaxFormComplete: ajaxValidationCallback,
				onBeforeAjaxFormValidation: beforeCall
			});
			showOrgan();
		});
	</script>
</head>
<body>
<div class="page-container" style="margin: 10px;background-color: #fff; border: 1px solid #ddd;border-radius: 4px;">
	<form id="submitForm" action="${ctx}/role/save" method="post" class="form form-horizontal">
		<input type="hidden" name="id" value="${role.id}"/>
		<%@include file="/WEB-INF/include/token.jsp" %>
		<legend>角色维护</legend>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3">角色名称：</label>
			<div class="formControls col-xs-8 col-sm-6">
				<input name="name" value="${role.name}" type="text" class="validate[maxSize[64],required,ajax[onlyName]] input-text" placeholder="角色名称">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3">数据范围：</label>
			<div class="formControls col-xs-8 col-sm-6">
				<span class="select-box">
					<select id="dataScope" name="dataScope" onchange="showOrgan();" class="validate[required] select">
						<option value="1" <c:if test="${role.dataScope eq '1'}">selected="selected"</c:if>>所在机构数据</option>
						<option value="2" <c:if test="${role.dataScope eq '2'}">selected="selected"</c:if>>自定义机构数据</option>
					</select>
				</span>
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3">角色授权：</label>
			<div class="formControls col-xs-4 col-sm-3">
				<div id="menuIdsDiv">
				<c:forEach items="${role.menuIds}" var="menuId">
					<input type='hidden' name='menuIds' value='${menuId}'/>
				</c:forEach>
				</div>
				<ul id="menuTree" class="ztree" style="margin-top:0;"></ul>
			</div>
			<div id="organDiv" class="formControls col-xs-4 col-sm-3">
				<div id="organIdsDiv">
				<c:forEach items="${role.organIds}" var="organId">
					<input type='hidden' name='organIds' value='${organId}'/>
				</c:forEach>
				</div>
				<ul id="organTree" class="ztree" style="margin-top:0;"></ul>
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3">备注：</label>
			<div class="formControls col-xs-8 col-sm-6">
				<input name="remarks" value="${role.remarks}" type="text" class="validate[maxSize[64]] input-text" placeholder="备注">
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