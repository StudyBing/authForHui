<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglib.jsp" %>
<!DOCTYPE HTML>
<html style="height: auto;">
<head>
	<meta charset="utf-8">
	<%@include file="/WEB-INF/include/head.jsp" %>
	<%@include file="/WEB-INF/include/validation.jsp" %>
	<title>菜单维护</title>
	<script type="text/javascript">
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
						Hui_update_iframe("字典列表","${ctx}/dict/list");
					}
				} else {
					layer.msg(result.msg, {icon: 5});
				}
			} else {
				layer.msg('保存失败', {icon: 5});
			}
		}
		$(document).ready(function(){
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
	<form id="submitForm" action="${ctx}/dict/save" method="post" class="form form-horizontal">
		<input type="hidden" name="id" value="${dict.id}"/>
		<%@include file="/WEB-INF/include/token.jsp" %>
		<legend>字典维护</legend>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3">类型：</label>
			<div class="formControls col-xs-8 col-sm-6">
				<input name="type" value="${dict.type}" type="text" class="validate[maxSize[100],required] input-text" placeholder="类型">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3">标签名：</label>
			<div class="formControls col-xs-8 col-sm-6">
				<input name="label" value="${dict.label}" type="text" class="validate[maxSize[100],required] input-text" placeholder="标签名">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3">数据值：</label>
			<div class="formControls col-xs-8 col-sm-6">
				<input name="value" value="${dict.value}" type="text" class="validate[maxSize[100],required] input-text" placeholder="数据值">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3">排序（升序）：</label>
			<div class="formControls col-xs-8 col-sm-6">
				<input name="sort" value="${dict.sort}" type="text" class="validate[custom[integer],required] input-text" placeholder="排序（升序）">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3">备注信息：</label>
			<div class="formControls col-xs-8 col-sm-6">
				<input name="remarks" value="${dict.remarks}" type="text" class="validate[maxSize[200]] input-text" placeholder="备注信息">
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