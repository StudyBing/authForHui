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
                    parent.layer.msg(result.msg, {icon: 6});
                    var index = parent.layer.getFrameIndex(window.name);
                    parent.layer.close(index);
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
    <form id="submitForm" action="${ctx}/log/save" method="post" class="form form-horizontal">
        <input type="hidden" name="id" value="${log.id}"/>
        <legend>日志维护</legend>
        <div class="row cl">
            <label class="form-label col-xs-2 col-sm-2">标题：</label>
            <div class="formControls col-xs-10 col-sm-10">
                <pre class="layui-code">${log.title}</pre>
            </div>
        </div>
        <div class="row cl">
            <label class="form-label col-xs-2 col-sm-2">日志类型：</label>
            <div class="formControls col-xs-10 col-sm-10">
                <c:choose>
                    <c:when test="${log.type eq '1'}">
                        <span class='label label-success radius'>正常</span>
                    </c:when>
                    <c:when test="${log.type eq '2'}">
                        <span class='label label-danger radius'>异常</span>
                    </c:when>
                </c:choose>
            </div>
        </div>
        <div class="row cl">
            <label class="form-label col-xs-2 col-sm-2">创建者：</label>
            <div class="formControls col-xs-10 col-sm-10">
                <pre class="layui-code">${user.name}</pre>
            </div>
        </div>
        <div class="row cl">
            <label class="form-label col-xs-2 col-sm-2">操作IP地址：</label>
            <div class="formControls col-xs-10 col-sm-10">
                <pre class="layui-code">${log.remoteIp}</pre>
            </div>
        </div>
        <div class="row cl">
            <label class="form-label col-xs-2 col-sm-2">用户标识：</label>
            <div class="formControls col-xs-10 col-sm-10">
                <pre class="layui-code">${log.userAgent}</pre>
            </div>
        </div>
        <div class="row cl">
            <label class="form-label col-xs-2 col-sm-2">请求URI：</label>
            <div class="formControls col-xs-10 col-sm-10">
                <pre class="layui-code">${log.requestUri}</pre>
            </div>
        </div>
        <div class="row cl">
            <label class="form-label col-xs-2 col-sm-2">操作方式：</label>
            <div class="formControls col-xs-10 col-sm-10">
                <span class='label label-success radius'>${log.method}</span>
            </div>
        </div>
        <div class="row cl">
            <label class="form-label col-xs-2 col-sm-2">提交的数据：</label>
            <div class="formControls col-xs-10 col-sm-10">
                <pre class="layui-code">${log.params}</pre>
            </div>
        </div>
        <div class="row cl">
            <label class="form-label col-xs-2 col-sm-2">异常信息：</label>
            <div class="formControls col-xs-10 col-sm-10">
                <pre class="layui-code">${log.exception}</pre>
            </div>
        </div>
        <div class="row cl">
            <label class="form-label col-xs-2 col-sm-2">耗时：</label>
            <div class="formControls col-xs-10 col-sm-10">
                <pre class="layui-code">${log.runTimeStr}</pre>
            </div>
        </div>
    </form>
</div>
</body>
</html>