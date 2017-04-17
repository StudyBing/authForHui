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
	<script type="text/javascript" src="${ctxStatic}/lib/zTree/js/jquery.ztree.core-3.5.min.js"></script>
	<title>菜单维护</title>
	<script type="text/javascript">
		$.extend($.validationEngineLanguage.allRules,{
			onlyUser: {
				'url': '${ctx}/user/onlyUser', /* 验证程序地址 */
				'extraData': 'id=${user.id}', /* 额外参数 */
				'alertTextOk': '',
				'alertText': '登录名已经存在',
				'alertTextLoad': '正在验证登录名唯一性...'
			},
			onlyOrgAndJobNo: {
				'url': '${ctx}/user/onlyOrgAndJobNo', /* 验证程序地址 */
				'extraData': 'id=${user.id}', /* 额外参数 */
				'extraDataDynamic': '#jobNo,#orgCode', /* 额外动态参数 */
				'alertTextOk': '',
				'alertText': '工号不唯一',
				'alertTextLoad': '正在验证工号唯一性...'
			},
			onlyPhone: {
				'url': '${ctx}/user/onlyUser', /* 验证程序地址 */
				'extraData': 'id=${user.id}', /* 额外参数 */
				'alertTextOk': '',
				'alertText': '手机已经存在',
				'alertTextLoad': '正在验证手机唯一性...'
			}
		});

		function idcardValidator(field, rules, i, options) {
			if(field.val().trim() == "") {
				return;
			}
			var code = field.val().toUpperCase();
			field.val(code);
			var city={11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",23:"黑龙江 ",31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",42:"湖北 ",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",51:"四川",52:"贵州",53:"云南",54:"西藏 ",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外 "};
			if(!code || !/^\d{6}(18|19|20)?\d{2}(0[1-9]|1[012])(0[1-9]|[12]\d|3[01])\d{3}(\d|X)$/i.test(code)){
				return "身份证号格式错误";
			} else if (!city[code.substr(0,2)]) {
				return "地址编码错误";
			} else {
				//18位身份证需要验证最后一位校验位
				if(code.length == 18){
					code = code.split('');
					//∑(ai×Wi)(mod 11)
					//加权因子
					var factor = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 ];
					//校验位
					var parity = [ 1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2 ];
					var sum = 0;
					var ai = 0;
					var wi = 0;
					for (var i = 0; i < 17; i++)
					{
						ai = code[i];
						wi = factor[i];
						sum += ai * wi;
					}
					var last = parity[sum % 11];
					if(parity[sum % 11] != code[17]){
						return "身份证校验位错误";
					}
				}
			}
		}
		function phoneValidator(field, rules, i, options) {
			var internet = $("input[name='internet']:checked").val();
			var wdy = $("input[name='wdy']:checked").val();
			var value = $.trim(field.val());
			if( value == "" ) {
				if (internet == "1" || wdy == "1"){
					var cls = field.attr("class");
					if (cls.indexOf("required")<0) {
						field.attr("class","validate[maxSize[16],funcCall[phoneValidator],required,ajax[onlyPhone]] input-text");
						field.validationEngine('validate');
					}
				} else {
					field.attr("class","validate[maxSize[16],funcCall[phoneValidator],ajax[onlyPhone]] input-text");
				}
			} else {
				if (!(/^1[34578]\d{9}$/.test(value))) {
					return "联系电话格式不对";
				}
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
						Hui_update_iframe("用户列表","${ctx}/user/list");
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
				ajaxFormValidationMethod: 'post',
				onAjaxFormComplete: ajaxValidationCallback,
				onBeforeAjaxFormValidation: beforeCall
			});
		});
	</script>
	<script type="text/javascript">
		$(function(){
			setTimeout(function(){
				$("select").select2();
			},100);
		});
	</script>
</head>
<body>
<div class="page-container" style="margin: 10px;background-color: #fff; border: 1px solid #ddd;border-radius: 4px;">
	<form id="submitForm" action="${ctx}/user/save" method="post" class="form form-horizontal">
		<%@include file="/WEB-INF/include/token.jsp" %>
		<input type="hidden" name="id" value="${user.id}"/>
		<legend>用户维护</legend>
		<div class="row cl">
			<label class="form-label col-xs-2 col-sm-2">姓名：</label>
			<div class="formControls col-xs-3 col-sm-3">
				<input name="name" value="${user.name}" type="text" class="validate[maxSize[32],required] input-text" placeholder="名称">
			</div>
			<label class="form-label col-xs-2 col-sm-2">登录名：</label>
			<div class="formControls col-xs-3 col-sm-3">
				<input id="username" name="username" value="${user.username}" type="text" class="validate[maxSize[32],minSize[4],required,custom[onlyLetterNumber],ajax[onlyUser]] input-text" placeholder="登录名">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-2 col-sm-2">密码：</label>
			<div class="formControls col-xs-3 col-sm-3">
				<input id="password" name="password" type="password" class="validate[maxSize[64],minSize[6],custom[onlyLetterNumber]<c:if test="${empty user.id}">,required</c:if>] input-text" placeholder="密码">
			</div>
			<label class="form-label col-xs-2 col-sm-2">密码确认：</label>
			<div class="formControls col-xs-3 col-sm-3">
				<input id="repassword" type="password" class="validate[equals[password]<c:if test="${empty user.id}">,required</c:if>] input-text" placeholder="密码确认">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-2 col-sm-2">机构代码：</label>
			<div class="formControls col-xs-3 col-sm-3">
				<select id="orgCode" name="orgCode" value="${user.orgCode}" class="validate[required] select">
					<c:forEach items="${hospitals}" var="item">
						<option value="${item.code}" <c:if test="${user.orgCode eq item.code}">selected="selected"</c:if>>${item.name}</option>
					</c:forEach>
				</select>
			</div>
			<label class="form-label col-xs-2 col-sm-2">工号：</label>
			<div class="formControls col-xs-3 col-sm-3">
				<input id="jobNo" name="jobNo" value="${user.jobNo}" type="text" class="validate[maxSize[32],required,ajax[onlyOrgAndJobNo]] input-text" placeholder="工号">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-2 col-sm-2">身份证号：</label>
			<div class="formControls col-xs-3 col-sm-3">
				<input name="idcard" value="${user.idcard}" type="text" class="validate[maxSize[32],funcCall[idcardValidator]] input-text" placeholder="医生身份证号">
			</div>
			<label class="form-label col-xs-2 col-sm-2">联系电话：</label>
			<div class="formControls col-xs-3 col-sm-3">
				<input name="phone" value="${user.phone}" type="text" class="validate[maxSize[16],funcCall[phoneValidator],ajax[onlyPhone]] input-text" placeholder="联系电话">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-2 col-sm-2">简介：</label>
			<div class="formControls col-xs-10 col-sm-7">
				<input name="intro" value="${user.intro}" type="text" class="validate[maxSize[500]] input-text" placeholder="医生简介">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-2 col-sm-2">角色授权：</label>
			<div class="formControls col-xs-10 col-sm-7">
				<c:forEach items="${roles}" var="role" varStatus="sta">
					<div class="check-box">
						<input name="roleIds" <c:if test="${fns:contains(user.roleIds, role.id)}"> checked="checked"</c:if> value="${role.id}" type="checkbox" id="checkbox-${sta.index}">
						<label for="checkbox-${sta.index}">${role.name}</label>
					</div>
				</c:forEach>
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-2 col-sm-2">备注：</label>
			<div class="formControls col-xs-10 col-sm-7">
				<input name="remarks" value="${user.remarks}" type="text" class="validate[maxSize[64]] input-text" placeholder="">
			</div>
		</div>
		<div class="row cl">
			<div class="col-xs-12 col-sm-9 col-xs-offset-4 col-sm-offset-3">
				<input class="btn btn-primary radius" type="submit" value="提交">
			</div>
		</div>
	</form>
</div>
</body>
</html>