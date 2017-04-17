<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglib.jsp" %>
<!DOCTYPE HTML>
<html>
<head>
<meta charset="utf-8">
<%@include file="/WEB-INF/include/head.jsp" %>
<title>后台管理</title>
</head>
<body>
<header class="navbar-wrapper">
	<div class="navbar navbar-fixed-top">
		<div class="container-fluid cl">
            <a class="logo navbar-logo f-l mr-10 hidden-xs" href="/aboutHui.shtml">H-ui.admin</a>
            <a class="logo navbar-logo-m f-l mr-10 visible-xs" href="/aboutHui.shtml">H-ui</a>
			<span class="logo navbar-slogan f-l mr-10 hidden-xs">v3.0</span>
			<a aria-hidden="false" class="nav-toggle Hui-iconfont visible-xs" href="javascript:;">&#xe667;</a>
			<nav class="nav navbar-nav">
				<ul class="cl">
					<li class="dropDown dropDown_hover">
                        <a href="javascript:;" class="dropDown_A"><i class="Hui-iconfont">&#xe600;</i> 新增 <i class="Hui-iconfont">&#xe6d5;</i></a>
						<ul class="dropDown-menu menu radius box-shadow">
							<li><a href="javascript:;" onclick="article_add('添加资讯','article-add.html')"><i class="Hui-iconfont">&#xe616;</i> 资讯</a></li>
						</ul>
					</li>
				</ul>
			</nav>
			<nav id="Hui-userbar" class="nav navbar-nav navbar-userbar hidden-xs">
				<ul class="cl">
					<li class="dropDown dropDown_hover">
						<a href="#" class="dropDown_A">${user.name} <i class="Hui-iconfont">&#xe6d5;</i></a>
						<ul class="dropDown-menu menu radius box-shadow">
							<li id="updatePassword"><a href="#">修改密码</a></li>
							<li><a href="${ctx}/logout">退出</a></li>
						</ul>
					</li>
<%--
					<li id="Hui-msg"> <a href="#" title="消息"><span class="badge badge-danger">1</span><i class="Hui-iconfont" style="font-size:18px">&#xe68a;</i></a> </li>
--%>
					<li id="Hui-skin" class="dropDown right dropDown_hover"> <a href="javascript:;" class="dropDown_A" title="换肤"><i class="Hui-iconfont" style="font-size:18px">&#xe62a;</i></a>
						<ul class="dropDown-menu menu radius box-shadow">
							<li><a href="javascript:;" data-val="default" title="默认（黑色）">默认（黑色）</a></li>
							<li><a href="javascript:;" data-val="blue" title="蓝色">蓝色</a></li>
							<li><a href="javascript:;" data-val="green" title="绿色">绿色</a></li>
							<li><a href="javascript:;" data-val="red" title="红色">红色</a></li>
							<li><a href="javascript:;" data-val="yellow" title="黄色">黄色</a></li>
							<li><a href="javascript:;" data-val="orange" title="橙色">橙色</a></li>
						</ul>
					</li>
				</ul>
			</nav>
		</div>
	</div>
</header>
<aside class="Hui-aside">
    <div class="menu_dropdown bk_2">
        <c:forEach items="${menus}" var="item" varStatus="sta">
            <dl id="menu_${sta}">
                <dt <c:if test="${sta.index == 0}">class="selected"</c:if>><i class="Hui-iconfont">&#xe637;</i> ${item.name}<i class="Hui-iconfont menu_dropdown-arrow">&#xe6d5;</i></dt>
                <dd <c:if test="${sta.index == 0}">style="display: block;"</c:if>>
                    <ul>
                        <c:forEach items="${item.children}" var="menu">
                            <c:if test="${menu.display eq '1'}">
                                <c:if test="${firstShow == null}">
                                    <c:set var="firstShow" value="${menu}" />
                                </c:if>
                                <li><a data-href="${ctx}${menu.href}" data-title="${menu.name}" href="javascript:void(0)">${menu.name}</a></li>
                            </c:if>
                        </c:forEach>
                    </ul>
                </dd>
            </dl>
        </c:forEach>
    </div>
</aside>
<div class="dislpayArrow hidden-xs"><a class="pngfix" href="javascript:void(0);" onClick="displaynavbar(this)"></a></div>
<section class="Hui-article-box">
    <div id="Hui-tabNav" class="Hui-tabNav hidden-xs">
        <div class="Hui-tabNav-wp">
            <ul id="min_title_list" class="acrossTab cl">
                <li class="active">
                    <span title="${firstShow.name}" data-href="${ctx}${firstShow.href}">${firstShow.name}</span>
                    <em></em>
                </li>
            </ul>
        </div>
        <div class="Hui-tabNav-more btn-group">
            <a id="js-tabNav-prev" class="btn radius btn-default size-S" href="javascript:;">
                <i class="Hui-iconfont">&#xe6d4;</i>
            </a>
            <a id="js-tabNav-next" class="btn radius btn-default size-S" href="javascript:;">
                <i class="Hui-iconfont">&#xe6d7;</i>
            </a>
        </div>
    </div>
    <div id="iframe_box" class="Hui-article">
        <div class="show_iframe">
            <div style="display:none" class="loading"></div>
            <iframe scrolling="yes" frameborder="0" src="${ctx}${firstShow.href}"></iframe>
        </div>
    </div>
</section>
<div class="contextMenu" id="Huiadminmenu">
    <ul>
        <li id="closethis">关闭当前</li>
        <li id="closeall">关闭全部</li>
    </ul>
</div>
<script type="text/javascript">
	$(document).ready(function(){
		$("#updatePassword").click(function(){
			layer.open({
				type: 1,
				shade: false,
				area: ['500px', '300px'],
				title: false, //不显示标题
				content: $('#changeTemplate').html()
			});
		});
	});

	function submitForm() {
		var oldPw = $("#oldPw").val();
		var newPw = $("#newPw").val();
		var rePw = $("#rePw").val();
		if ($.trim(oldPw) == "") {
			layer.tips('原密码不能为空', '#oldPw');
			return;
		}
		if ($.trim(newPw) == "") {
			layer.tips('新密码不能为空', '#newPw');
			return;
		}
		if ($.trim(rePw) == "") {
			layer.tips('确认密码不能为空', '#rePw');
			return;
		}
		if (newPw != rePw){
			layer.tips('确认密码不同密码', '#rePw');
			return;
		}
		$.ajax({
			url:"${ctx}/user/changePw",
			data:{
				oldPw:oldPw,
				newPw:newPw
			},
			success:function(rs){
				if(rs.ok){
					layer.closeAll();
					layer.msg(rs.msg, {icon: 6});
				} else {
					layer.msg(rs.msg, {icon: 5});
				}
			}
		});
	}
</script>

<script type="text/template" id="changeTemplate">
<form id="changeForm" action="${ctx}/user/changePw" style="width: 450px;padding: 20px;" method="post"
	  class="form form-horizontal">
	<legend>修改密码</legend>
	<div class="row cl">
		<label class="form-label col-xs-4 col-sm-3">原密码：</label>
		<div class="formControls col-xs-5 col-sm-6">
			<input id="oldPw" name="oldPw" type="password" class="input-text">
		</div>
	</div>
	<div class="row cl">
		<label class="form-label col-xs-4 col-sm-3">新密码：</label>
		<div class="formControls col-xs-5 col-sm-6">
			<input id="newPw" name="newPw" type="password" class="input-text">
		</div>
	</div>
	<div class="row cl">
		<label class="form-label col-xs-4 col-sm-3">确认新密码：</label>
		<div class="formControls col-xs-5 col-sm-6">
			<input id="rePw" type="password" class="input-text">
		</div>
	</div>
	<div class="row cl">
		<div class="col-xs-12 col-sm-9 col-xs-offset-4 col-sm-offset-3">
			<input class="btn btn-primary radius" type="button" onclick="submitForm();" value="提交">
		</div>
	</div>
</form>
</script>
</body>
</html>