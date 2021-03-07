<head>
	<jsp:directive.include file="/WEB-INF/jsp/prelude/include-head-meta.jspf" />
	<jsp:directive.include file="/WEB-INF/jsp/prelude/include-js.jspf"/>
	<%
            String contextPath = request.getContextPath();
        %>
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

      @media (min-width: 768px) {
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
			<input type="text" id="inputUsername" name="name" class="form-control" placeholder="请输入用户名" required>
			<br/>

			<label for="inputPassword" class="sr-only">Password</label>
			<input type="password" id="inputPassword" name="password" class="form-control" placeholder="请输入密码" required>
			<br/>

			<label for="inputEmail" class="sr-only">请输入电子邮件</label>
			<input type="email" id="inputEmail" name="email" class="form-control" placeholder="请输入电子邮件" required autofocus>
			<br />

			<label for="inputPhoneNumber" class="sr-only">Password</label>
			<input type="tel" id="inputPhoneNumber" name="phoneNumber" class="form-control" placeholder="请输入手机号" required>
			<br />

			<button class="btn btn-lg btn-primary btn-block" type="button" onclick="submitRegistryForm();">确定</button>
		</form>
	</div>
</body>
<script>
    function submitRegistryForm(){
        var username = $("#inputUsername").val();
        			var password = $("#inputPassword").val();
        			var email = $("#inputEmail").val();
        			var phoneNumber = $("#inputPhoneNumber").val();
        			$.ajax({
                                 type: "POST",
                                 url: "${contextPath}/doRegistry",
                                 data: {
                                       name: username,
                                       password: password,
                                       email: email,
                                       phoneNumber: phoneNumber
                                 },
                                 success: function(data){
                                          location.href = '${contextPath}/registry-success';
                                 }
                             });

    }
</script>