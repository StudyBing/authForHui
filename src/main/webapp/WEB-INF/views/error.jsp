<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>执行错误</title>
</head>
<body style="background-color: #F5F5F5;text-align: center;">
    <img src="${ctxStatic}/img/error.png">
    <div>
        <a href="javascript:history.go(-1);" style="margin-right: 25px;"><img src="${ctxStatic}/img/btn_back.png"></a>
        <a href="${ctx}" style="margin-left: 25px;"><img src="${ctxStatic}/img/btn_index.png"></a>
    </div>
</body>
</html>
