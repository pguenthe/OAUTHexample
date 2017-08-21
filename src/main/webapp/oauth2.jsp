<%--
  Created by IntelliJ IDEA.
  User: peter
  Date: 8/20/17
  Time: 3:29 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Oauth2</title>
</head>
<body>
<h1>OAUTH 2</h1>
Success! At least we made it this far<br />
<h2>Step 1: Get user's permission and generate a code</h2>
${code}<br />
<h2>Step 2: Use the code to generate a token</h2>
Request 1 HTTP Status: ${status}<br />
Response 1: ${response}<br />
Token: ${token}<br />
<h2>Step 3: Use the token to make a request for info</h2>
Request 2 HTTP Status: ${status2}<br />
Response 2: ${response2}<br />
GitHub Username: ${username}<br />
GitHub Name: ${name}<br />
</body>
</html>
