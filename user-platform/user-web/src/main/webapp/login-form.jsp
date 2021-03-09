<head>
    <%
                String contextPath = request.getContextPath();
            %>
	<jsp:directive.include file="/WEB-INF/jsp/prelude/include-head-meta.jspf" />
	<jsp:directive.include file="/WEB-INF/jsp/prelude/include-js.jspf"/>
	<title>My Registry Page</title>
    <style>
      .bd-placeholder-img {
        font-size: 1.125rem;
        text-anchor: middle;
        -webkit-user-select: none;
        -moz-user-select: none;
        -ms-user-select: none;
        user-select: none;
      }

      @media (min-width: 300px) {
        .bd-placeholder-img-lg {
          font-size: 3.5rem;
        }
      }
    </style>
</head>
<body>
	<div class="container">
		<form class="form-signin">
			<h1 class="h3 mb-3 font-weight-normal">登录</h1>

			<label for="inputUsername" class="sr-only">用户名</label>
			<input type="text" id="inputUsername" name="name" class="form-control" placeholder="请输入用户名" required autofocus onblur="checkUserName();"/>
			<div id="showUserNameMsg"></div>
			<br/>

			<label for="inputPassword" class="sr-only">密码</label>
			<input type="password" id="inputPassword" name="password" class="form-control" placeholder="请输入密码" required onblur="checkPassword();"/>
			<div id="showPasswordMsg"></div>
			<br/>

			<button class="btn btn-lg btn-primary btn-block" type="button" onclick="submitLoginForm();">登录</button>
		</form>
	</div>
</body>
<script>
    function submitLoginForm(){
        var username = $("#inputUsername").val().trim();
        var password = $("#inputPassword").val().trim();
        $.getJSON("${contextPath}/doLogin",
                    { name: username,
                      password: password},
                    function(json){
                        if(json.code == "1"){
                            location.href = '${contextPath}/login-success';
                        }else{
                             $("#showPasswordMsg").html(json.message).css("color","red");
                        }
                    });
    };


    function checkUserName(){
        var name = $("#inputUsername").val().trim();
        if(name == ""){
            $("#showUserNameMsg").html("用户名不能为空").css("color","red");
            $("#inputUsername").focus();
        }else{
            $("#showUserNameMsg").html("");
        }
    };

    function checkPassword(){
            var password = $("#inputPassword").val().trim();
            if(password == ""){
                $("#showPasswordMsg").html("密码不能为空").css("color","red");
                $("#inputPassword").focus();
            }else{
                $("#showPasswordMsg").html("");
            }
        };
</script>