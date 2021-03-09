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
			<h1 class="h3 mb-3 font-weight-normal">注册</h1>

			<label for="inputUsername" class="sr-only">用户名</label>
			<input type="text" id="inputUsername" name="name" class="form-control" placeholder="请输入用户名" required autofocus onblur="checkUserName();"/>
			<div id="showUserNameMsg"></div>
			<br/>

			<label for="inputPassword" class="sr-only">密码</label>
			<input type="password" id="inputPassword" name="password" class="form-control" placeholder="请输入密码" required onblur="checkPassword();"/>
			<div id="showPasswrodMsg"></div>
			<br/>

			<label for="inputEmail" class="sr-only">请输入电子邮件</label>
			<input type="email" id="inputEmail" name="email" class="form-control" placeholder="请输入电子邮件" required onblur="checkEmail();"/>
			<div id="showEmailMsg"></div>
			<br />

			<label for="inputPhoneNumber" class="sr-only">手机号码</label>
			<input type="tel" id="inputPhoneNumber" name="phoneNumber" class="form-control" placeholder="请输入手机号" required onblur="checkPhoneNumber();"/>
			<div id="showPhoneNumberMsg"></div>
			<br />

			<button class="btn btn-lg btn-primary btn-block" type="button" onclick="submitRegistryForm();">确定</button>
		</form>
	</div>
</body>
<script>
    function submitRegistryForm(){
        var flag = checkAll();
        if(!flag){
           return;
        }
        var username = $("#inputUsername").val();
        var password = $("#inputPassword").val();
        var email = $("#inputEmail").val();
        var phoneNumber = $("#inputPhoneNumber").val();
        $.getJSON("${contextPath}/doRegistry",
                    { name: username,
                      password: password,
                      email: email,
                      phoneNumber: phoneNumber},
                    function(json){
                        if(json.code == "1"){
                            location.href = '${contextPath}/registry-success';
                        }
                    });
    };

    function checkAll(){
        var username = $("#showUserNameMsg").html();
        var password = $("#showUserNameMsg").html();
        var email = $("#showEmailMsg").html();
        var phoneNumber = $("#showPhoneNumberMsg").html();
        return username != null || password != null || email != null || phoneNumber != null;
    };

    function checkUserName(){
        $.getJSON("${contextPath}/checkUserName", { name: $("#inputUsername").val() }, function(json){
            if(json.code == "1"){
                $("#showUserNameMsg").html("");
            }else{
                $("#showUserNameMsg").html(json.message).css("color","red");
            }
        });
    };

    function checkPassword(){
            $.getJSON("${contextPath}/checkPassword", { name: $("#inputPassword").val() }, function(json){
                if(json.code == "1"){
                    $("#showPasswordMsg").html("");
                }else{
                    $("#showUserNameMsg").html(json.message).css("color","red");
                }
            });
        };

   function checkEmail(){
           $.getJSON("${contextPath}/checkEmail", { name: $("#inputEmail").val() }, function(json){
               if(json.code == "1"){
                   $("#showEmailMsg").html("");
               }else{
                   $("#showEmailMsg").html(json.message).css("color","red");
               }
           });


       };

   function checkPhoneNumber(){
           $.getJSON("${contextPath}/checkPhoneNumber", { name: $("#inputUsername").val() }, function(json){
               if(json.code == "1"){
                   $("#showPhoneNumberMsg").html("");
               }else{
                   $("#showPhoneNumberMsg").html(json.message).css("color","red");
               }
           });
       };
</script>