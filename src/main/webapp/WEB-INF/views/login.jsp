<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>登陆系统</title>
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/lib/jquery/1.9.1/jquery.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/lib/layer/2.4/layer.js"></script>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/login/login.css"/>
    <script type="text/javascript">
        $(function () {
            var h = $(window).height()/2;
            $("#top_div").css("height",h+"px");
            //得到焦点
            $("#password").focus(function () {
                $("#left_hand").animate({
                    left: "150",
                    top: " -38"
                }, {
                    step: function () {
                        if (parseInt($("#left_hand").css("left")) > 140) {
                            $("#left_hand").attr("class", "left_hand");
                        }
                    }
                }, 2000);
                $("#right_hand").animate({
                    right: "-64",
                    top: "-38px"
                }, {
                    step: function () {
                        if (parseInt($("#right_hand").css("right")) > -70) {
                            $("#right_hand").attr("class", "right_hand");
                        }
                    }
                }, 2000);
            });
            //失去焦点
            $("#password").blur(function () {
                $("#left_hand").attr("class", "initial_left_hand");
                $("#left_hand").attr("style", "left:100px;top:-12px;");
                $("#right_hand").attr("class", "initial_right_hand");
                $("#right_hand").attr("style", "right:-112px;top:-12px");

                var password = $("#password").val();
                if ($.trim(password) == "") {
                    $("#password").css("border-color", "red");
                    layer.tips('密码不能为空', '#password');
                } else {
                    $("#password").css("border-color", "#d3d3d3");
                }
            });
            $("#username").blur(function () {
                var username = $("#username").val();
                if ($.trim(username) == "") {
                    layer.tips('用户名不能为空', '#username');
                    $("#username").css("border-color", "red");
                } else {
                    $("#username").css("border-color", "#d3d3d3");
                }
            });

            //默认message提醒
            if ($.trim('${message}') != '') {
                layer.msg('${message}', {icon: 5});
            }

            $('#password').keydown(function(e){
                if(e.keyCode==13){
                    login();
                }
            });
        });
        function login() {
            var username = $("#username").val();
            if ($.trim(username) == "") {
                layer.tips('用户名不能为空', '#username');
                return;
            }
            var password = $("#password").val();
            if ($.trim(password) == "") {
                layer.tips('密码不能为空', '#password');
                return;
            }
            $("form").submit();
        }
    </script>
</head>

<body>
<div id="top_div" class="top_div"></div>
<div style="background: rgb(255, 255, 255); margin: -100px auto auto; border: 1px solid rgb(231, 231, 231); border-image: none; width: 400px; height: 200px; text-align: center;">
    <form method="post" action="${pageContext.request.contextPath}/login">
        <div style="width: 165px; height: 96px; position: absolute;">
            <div class="tou"></div>
            <div class="initial_left_hand" id="left_hand" style="left:100px;top:-12px;"></div>
            <div class="initial_right_hand" id="right_hand" style="right:-112px;top:-12px"></div>
        </div>
        <p style="padding: 30px 0px 10px; position: relative;">
            <span class="u_logo"></span>
            <input class="ipt" id="username" name="username" type="text" placeholder="请输入用户名">
        </p>
        <p style="position: relative;">
            <span class="p_logo"></span>
            <input class="ipt" id="password" name="password" type="password" placeholder="请输入密码">
        </p>
        <div style="height: 50px; line-height: 50px; margin-top: 30px; border-top-color: rgb(231, 231, 231); border-top-width: 1px; border-top-style: solid;">
            <p style="margin: 0px 35px 20px 45px;">
           <span style="float: right;">
              <input type="button" class="btn" value="登录" onclick="login();"/>
           </span>
            </p>
        </div>
    </form>
</div>
</body>

</html>
