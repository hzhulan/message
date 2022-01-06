<%--
  Created by IntelliJ IDEA.
  User: FH
  Date: 2022/1/4
  Time: 19:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>12</title>
</head>
<body>
    <a href="${ctx}/album?albumId=1">布丁</a>
    <a href="${ctx}/album?albumId=2">测试</a>
</body>
</html>
