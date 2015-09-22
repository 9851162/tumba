<%-- 
    Document   : admin
    Created on : 22.09.2015, 12:04:14
    Author     : bezdatiuzer
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Управление</title>
    </head>
    <body>
        <c:if test="${role=='admin'}">
    <%@include file="/WEB-INF/jsp/menutopadmin.jsp" %>
    
</c:if>
        <h1>Управление</h1>
    </body>
</html>
