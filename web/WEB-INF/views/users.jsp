<%-- 
    Document   : users
    Created on : 04.11.2015, 22:06:56
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
                <a href="<c:url value='/Admin/regions'/>">Регионы</a>
                <a href="<c:url value='/Admin/users'/>">Пользователи</a>
                <a href="<c:url value='/Main/'/>">Главная</a>
            
                <h3>Пользователи</h3>
                
                <div>
                    <form action="<c:url value="/Admin/users" />">
                        <input type="text" name="keyWord">
                        <input type="submit" value="Найти">
                    </form>
                </div>
                
                <div><table>
                        <tr><th>№</th><th>email</th><th>ФИО</th><th>Роль</th><th>Изменить роль</th><!--<th>Удалить</th>--></tr>
                    <c:forEach var="user" items="${users}" varStatus="myIndex">
                            <tr><td>${myIndex.count}</td><td>${user.email}</td><td>${user.name}</td><td>${user.getRusRole()}</td>
                                <td><c:if test="${user.userRole=='admin'}"><a href="<c:url value="/Admin/setRole?userId=${user.id}&role=user" />">Сделать пользователем</a></c:if>
                                    <c:if test="${user.userRole=='user'}"><a href="<c:url value="/Admin/setRole?userId=${user.id}&role=admin" />">Сделать администратором</a></c:if></td>
                                <td><!--<a href="<c:url value="/Admin/deleteUser?userId=${user.id}" />">x</a>--></td></tr>
                    </c:forEach>
                    </table>
                </div>
            
        </div>
            
            
    </body>
    
</html>
