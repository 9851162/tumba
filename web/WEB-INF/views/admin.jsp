<%-- 
    Document   : admin
    Created on : 22.09.2015, 12:04:14
    Author     : bezdatiuzer
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="myTags" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Управление</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="Cache-Control" content="no-cache">
        <link rel="stylesheet" type="text/css" href="../css/style.css">
        <link rel="stylesheet" type="text/css" href="../css/animate.css">
    </head>
    <body style="overflow: scroll;">
        <div id="wrapper">
            <script src="<c:url value='/js/jquery-1.11.2.min.js'/>"> </script>
            <script src="../js/seller_scripts/magic.js"></script>
                <%@include file="/WEB-INF/jsp/menu.jsp" %>
                <%@include file="/WEB-INF/jsp/error.jsp" %>
            
                <a href="<c:url value='/Admin/cats'/>">Категории</a>
                <a href="<c:url value='/Admin/params'/>">Параметры</a>
                <a href="<c:url value='/Main/'/>">Главная</a>
            
            
        </div>
            
            
    </body>
    
</html>
