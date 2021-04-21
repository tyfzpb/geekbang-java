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
                <h1 class="h3 mb-3 font-weight-normal" id="showMsg"></h1>
            </form>
    </div>
</body>
<script>
    $.getJSON("${contextPath}/login/oauth",
            {code : "${param.code}",
            source : "${source}",
            state : "${param.state}"},
            function(json){
                if(json.code == "200" && json.data != ""){
                    $("#showMsg").html("欢迎：" + json.data.userName + "<img src=" + json.data.avatar + " height=50 width=50 />");
                }else{
                    $("#showMsg").html("出错了：" + json.msg);
                }
            });

</script>