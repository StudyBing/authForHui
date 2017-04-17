<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglib.jsp" %>
<!DOCTYPE HTML>
<html style="height: auto;">
<head>
	<meta charset="utf-8">
	<%@include file="/WEB-INF/include/head.jsp" %>
	<%@include file="/WEB-INF/include/validation.jsp" %>
	<title>机构维护</title>
	<script type="text/javascript">

		$.extend($.validationEngineLanguage.allRules,{
			onlyName: {
				'url': '${ctx}/organ/onlyName', /* 验证程序地址 */
				'extraData': 'id=${organ.id}', /* 额外参数 */
				'alertTextOk': '',
				'alertText': '机构名称已经存在',
				'alertTextLoad': '正在验证机构名称唯一性...'
			},
			onlyCode: {
				'url': '${ctx}/organ/onlyCode', /* 验证程序地址 */
				'extraData': 'id=${organ.id}', /* 额外参数 */
				'alertTextOk': '',
				'alertText': '机构代码已经存在',
				'alertTextLoad': '正在验证机构代码唯一性...'
			}
		});

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
						Hui_update_iframe("机构列表","${ctx}/organ/list");
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
	<form id="submitForm" action="${ctx}/organ/save" method="post" class="form form-horizontal">
		<input type="hidden" name="id" value="${organ.id}"/>
		<%@include file="/WEB-INF/include/token.jsp" %>
		<legend>机构维护</legend>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3">机构代码：</label>
			<div class="formControls col-xs-8 col-sm-6">
				<input name="code" value="${organ.code}" type="text" class="validate[maxSize[32],required,ajax[onlyCode]] input-text" placeholder="">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3">机构名称：</label>
			<div class="formControls col-xs-8 col-sm-6">
				<input name="name" value="${organ.name}" type="text" class="validate[maxSize[32],required,ajax[onlyName]] input-text" placeholder="">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3">备注：</label>
			<div class="formControls col-xs-8 col-sm-6">
				<input name="remarks" value="${organ.remarks}" type="text" class="validate[maxSize[64]] input-text" placeholder="">
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