<%@ page import="org.springframework.security.core.context.SecurityContextHolder" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Title</title>
</head>
<body>
<h2>订单首页Hello World!</h2>
<h2>用户名security：<%=SecurityContextHolder.getContext().getAuthentication().getName()%></h2>
<a href="/logout/cas">aaaaa</a>
<h2>aaaaa</h2>
</body>
</html>
