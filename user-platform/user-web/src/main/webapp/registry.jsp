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

			<label for="inputUsername" >用户名</label>
			<input type="text" id="inputUsername" name="name" class="form-control" placeholder="请输入用户名" required autofocus onblur="checkUserName();"/>
			<div id="showUserNameMsg"></div>
			<br/>

			<label for="inputPassword" >密 码</label>
			<input type="password" id="inputPassword" name="password" class="form-control" placeholder="请输入密码" required onblur="checkPassword();"/>
			<div id="showPasswordMsg"></div>
			<br/>

			<label for="inputEmail" >Email</label>
			<input type="email" id="inputEmail" name="email" class="form-control" placeholder="请输入电子邮件" required onblur="checkEmail();"/>
			<div id="showEmailMsg"></div>
			<br />

			<label for="inputPhoneNumber" >手机号码</label>
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
        if(flag){
           return;
        }
        var username = $("#inputUsername").val().trim();
        var password = $("#inputPassword").val().trim();
        var email = $("#inputEmail").val().trim();
        var phoneNumber = $("#inputPhoneNumber").val().trim();
        $.getJSON("${contextPath}/doRegistry",
                    { name: username,
                      password: password,
                      email: email,
                      phoneNumber: phoneNumber},
                    function(json){
                        if(json.code == "1"){
                            location.href = '${contextPath}/registry-success';
                        }else{
                            alert("注册失败");
                        }
                    });
    };

    function checkAll(){
        var showUserNameMsg = $("#showUserNameMsg").html();
        var showPasswordMsg = $("#showPasswordMsg").html();
        var showEmailMsg = $("#showEmailMsg").html();
        var showPhoneNumberMsg = $("#showPhoneNumberMsg").html();
        var flag =  showUserNameMsg != null || showPasswordMsg != null || showEmailMsg != null || showPhoneNumberMsg != null;
        return !flag;
    };

    function checkUserName(){
        var name = $("#inputUsername").val().trim();
        if(name == ""){
            $("#showUserNameMsg").html("用户名不能为空").css("color","red");
            return;
        }
        $.getJSON("${contextPath}/checkUserName", { name: name }, function(json){
            if(json.code == "1"){
                $("#showUserNameMsg").html("");
            }else{
                $("#showUserNameMsg").html(json.message).css("color","red");
            }
        });
    };

    function checkPassword(){
            var password = $("#inputPassword").val().trim();
            if(password == ""){
                $("#showPasswordMsg").html("密码不能为空").css("color","red");
                return;
            }
            $.getJSON("${contextPath}/validatePassword", { password: password }, function(json){
                if(json.code == "1"){
                    $("#showPasswordMsg").html("");
                }else{
                    $("#showPasswordMsg").html(json.message).css("color","red");
                }
            });
        };

   function checkEmail(){
            var email = $("#inputEmail").val().trim();
            if(email == ""){
                 $("#showEmailMsg").html("Email不能为空").css("color","red");
                 return;
            }
           $.getJSON("${contextPath}/validateEmail", { email: email }, function(json){
               if(json.code == "1"){
                   $("#showEmailMsg").html("");
               }else{
                   $("#showEmailMsg").html(json.message).css("color","red");
               }
           });


       };

   function checkPhoneNumber(){
            var phoneNumber = $("#inputPhoneNumber").val().trim();
            if(phoneNumber == ""){
                 $("#showPhoneNumberMsg").html("手机号码不能为空").css("color","red");
                 return;
            }
           $.getJSON("${contextPath}/validatePhoneNumber", { phoneNumber: phoneNumber }, function(json){
               if(json.code == "1"){
                   $("#showPhoneNumberMsg").html("");
               }else{
                   $("#showPhoneNumberMsg").html(json.message).css("color","red");
               }
           });
       };
</script>