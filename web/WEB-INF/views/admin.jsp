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
        <title>Управление</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="Cache-Control" content="no-cache">
        <!--<link href="./css/bootstrap.min.css" rel="stylesheet" type="text/css" >-->
        <link rel="stylesheet" type="text/css" href="../css/style.css">
        <link rel="stylesheet" type="text/css" href="../css/animate.css">
    </head>
    <body style="overflow: scroll;">
        <div id="wrapper">
            <script src="<c:url value='/js/jquery-1.11.2.min.js'/>"> </script>
            <c:if test="${role=='admin'}">
                <%@include file="/WEB-INF/jsp/menutopadmin.jsp" %>
            </c:if>
            <h1>Управление</h1>
        </div>
    </body>
</html>
