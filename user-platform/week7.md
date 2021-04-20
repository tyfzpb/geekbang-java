## 第七周作业说明：
### 使用SpringBoot或者Sevlet来实现一个整合Gitee/或者Github OAuth2 认证
### 完成情况：
- 已完成Gitee和Github OAuth2认证实现。

  - Gitee认证流程：https://gitee.com/api/v5/oauth_doc#/list-item-2
    - 1、重定向到gitee三方认证页面（GET请求）
        - 实现：访问http://localhost:8080/login/gitee重定向https://gitee.com/oauth/authorize?client_id={client_id}&redirect_uri={redirect_uri}&response_type=code
       ```java 
      @Path("/login/gitee")
      public void loginGitee(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String result = getAuthReqeust("gitee").authorize();
        response.sendRedirect(result);
      }
      ```
    - 2、用户对应用进行授权
    - 3、gitee认证服务器通过回调地址{redirect_uri}将 用户授权码 传递给 应用服务器
        - 实现：回调地址http://localhost:8080/login/oauth/code/gitee
    - 4、应用服务器使用 access_token API向gitee认证服务器发送post请求传入 用户授权码 以及 回调地址（ POST请求 ）
    - 5、gitee认证服务器返回access_token，应用通过access_token访问Open API使用用户数据
        ```java
        
      @Path("/login/oauth2/code/gitee")
        public String loginCallbackGitee(String code,HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("source","gitee");
        return "oauthCallback.jsp";
      }
      
        @Path("/login/oauth")
        public Object oauth(String code,String source,String state) {
            AuthReqeust authReqeust = getAuthReqeust(source);
            return authReqeust.login(AuthCallback.builder().code(code).state(state).build());
        }
      
        public AuthResponse login(AuthCallback authCallback) {
            try {
                AuthToken authToken = this.getAccessToken(authCallback);
                AuthUser authUser = this.getUserInfo(authToken);
                return AuthResponse.builder().code(200).data(authUser).build();
            } catch (Exception e) {
                return errorResponse(e);
            }
        }
      
        ```
    
  - github认证流程：https://docs.github.com/en/developers/apps/authorizing-oauth-apps
    - 1、重定向到github三方认证页面（GET请求）GET 
      - 实现：访问http://localhost:8080/login/github重定向https://github.com/login/oauth/authorize
        ```java 
         @Path("/login/github")
        public void loginGithub(HttpServletRequest request, HttpServletResponse response) throws IOException {
          String result = getAuthReqeust("github").authorize();
          response.sendRedirect(result);
        }
        ```
    - 2、用户对应用进行授权
    - 3、github认证服务器通过回调地址{redirect_uri}将 用户授权码 传递给 应用服务器
      - 实现：回调地址http://localhost:8080/login/oauth/code/github
    - 4、应用服务器使用 access_token API向gitee认证服务器发送post请求传入 用户授权码 以及 回调地址（ POST请求 ）
    - 5、github认证服务器返回access_token，应用通过access_token访问Open API使用用户数据
      ```shell
      curl -H "Authorization: token OAUTH-TOKEN" https://api.github.com/user
      ```
      ```java
      @Path("/login/oauth2/code/github")
        public String loginCallbackGithub(String code,HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("source","github");
        return "oauthCallback.jsp";
      }
      @Path("/login/oauth")
        public Object oauth(String code,String source,String state) {
            AuthReqeust authReqeust = getAuthReqeust(source);
            return authReqeust.login(AuthCallback.builder().code(code).state(state).build());
        }
      
      protected AuthToken getAccessToken(AuthCallback authCallback) {
        String response = doPostAuthorizationCode(authCallback.getCode());
        Map<String, String> params = AuthUtils.parseStringToMap(response);
        this.checkResponse(params.containsKey("error"), params.get("error_description"));
        return AuthToken.builder()
                .accessToken(params.get("access_token"))
                .scope(params.get("scope"))
                .tokenType(params.get("token_type"))
                .build();
      }
      protected AuthUser getUserInfo(AuthToken authToken) {
        String url = source.userInfo();
        String userInfo = HttpUtils.get(url,"Authorization","token " + authToken.getAccessToken());
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode node = objectMapper.readTree(userInfo);
            this.checkResponse(node.has("error"), node.get("error_description"));
            return AuthUser.builder()
                    .rawUserInfo(node)
                    .uuid(node.get("id").asText())
                    .userName(node.get("login").asText())
                    .avatar(node.get("avatar_url").asText())
                    .blog(node.get("blog").asText())
                    .nickName(node.get("name").asText())
                    .email(node.get("email").asText())
                    .company(node.get("company").asText())
                    .location(node.get("location").asText())
                    .remark(node.get("bio").asText())
                    .token(authToken)
                    .source(source.toString())
                    .build();
        } catch (IOException e) {
            throw new AuthException(e.getMessage());
        }
      }
    ```
   
    - 测试页面http://localhost:8080/login 选择其他方式登录：gitee账号登录 或者 github账号登录
  
    