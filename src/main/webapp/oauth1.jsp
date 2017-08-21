<%--
  Created by IntelliJ IDEA.
  User: peter
  Date: 8/20/17
  Time: 3:31 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>OAUTH Client Side</title>
</head>
<body>
<a href="https://github.com/login/oauth/authorize?client_id=${clientID}&scope=user&state=12345&redirect_uri=${redirect}">
    Log In to GitHub</a>
<br />
</body>
</html>
