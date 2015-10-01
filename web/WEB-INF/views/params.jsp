<%-- 
    Document   : params
    Created on : 29.09.2015, 19:43:59
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
            <script src="<c:url value='/js/jquery-1.11.2.min.js'/>"></script>
            <script src="../js/seller_scripts/magic.js">

            </script>
            <%@include file="/WEB-INF/jsp/menu.jsp" %>
            <%@include file="/WEB-INF/jsp/error.jsp" %>
            <a href="<c:url value='/Admin/cats'/>">Категории</a>
                <a href="<c:url value='/Admin/params'/>">Параметры</a>
                <a href="<c:url value='/Main/'/>">Главная</a>
            <h3>Параметры</h3>
            <div style="width: 55%;float: right;">
                <c:if test="${!empty params}">
                    <table><tr><th>№</th><th>Наименование</th><th>Тип</th><th>Обяз.</th><th>Опции</th><th>Добавить опцию</th><th>Удалить</th></tr>
                        <c:forEach var="parametr" items="${params}" varStatus="myIndex">
                            <tr><td>${myIndex.count}</td><td>${parametr.name}</td>
                                <td>${paramTypeMap.get(parametr.paramType)}</td>
                                <td>${reqTypeMap.get(parametr.reqType)}</td>
                                <td>
                                    <c:forEach var="opt" items="${parametr.options}">
                                        ${opt.name} <a href="../Admin/deleteParamOption?paramOptionId=${opt.id}">x</a><br>
                                    </c:forEach>
                                </td>
                                <td>
                                    <c:if test="${parametr.paramType==3||parametr.paramType==4}">
                                        <form method="post" action="../Admin/addParamOption">
                                            <input type="text" name="name" placeholder="опция" style="width: 110px;">
                                            <input type="hidden" name="paramId" value="${parametr.id}">
                                            <button type="submit" class="addCat"></button>
                                        </form>
                                    </c:if>
                                </td>
                                <td><a href="../Admin/deleteParam?paramId=${parametr.id}">x</a></td></tr>
                        </c:forEach>
                    </table>
                </c:if>
                <c:if test="${empty params}">
                    Добавленных параметров пока нет.
                </c:if>
            </div>
            <div style="width: 45%;float: left;">

               
                <br><br>

                <form method="post" action="../Admin/createParam">
                    <label> Наименование <input type="text" name="name"></label>
                    <label>Тип <select name="paramType">
                            <c:forEach var="id" items="${paramTypeMap.keySet()}">
                                <option value="${id}">
                                    ${paramTypeMap.get(id)}
                                </option>
                            </c:forEach>
                        </select></label>
                    <label>Обяз. <input type="checkbox" name="reqType"></label>
                    <input type="hidden" name="catId" value="${param.catId}">

                    <button type="submit" class="btn">Добавить</button>
                </form>


            </div>
        </div>
    </body>
</html>
