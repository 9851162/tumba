<%-- 
    Document   : cats
    Created on : 29.09.2015, 19:44:13
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
            <script src="../js/seller_scripts/magic.js"></script>
            <%@include file="/WEB-INF/jsp/menu.jsp" %>
            <%@include file="/WEB-INF/jsp/error.jsp" %>
            <a href="<c:url value='/Admin/cats'/>">Категории</a>
                <a href="<c:url value='/Admin/params'/>">Параметры</a>
                <a href="<c:url value='/Admin/regions'/>">Регионы</a>
                <a href="<c:url value='/Admin/users'/>">Пользователи</a>
                <a href="<c:url value='/Main/'/>">Главная</a>
            <h3>Категории</h3>
            <div id="categoryField" style="float: left;">
                <myTags:category id="0" map="${nestingCatsMap}"/>
            </div>
            
            <div id="modal" class="modal_form modal_div">
            <div class="nameform">Добавить категорию в каталог</div>
            <form  method="post" action="../Admin/addCat" >
                <div class="boxtoinput">
                    <div class="toin">
                        <label>Наименование</label>
                        <input name="name" type="text">
                    </div>
                </div>
                <!--to do jquery podstanovku pid-->
                <input name="parentId" type="hidden" value="0">
                <input name="catId" type="hidden" value="${param.catId}">
                <div class="form-group">
                    <button type="submit" class="btn">Создать</button>
                </div>
            </form>
        </div>
            
        </div>
        

        <div id="paramPlace" style="width: 45%;float: right;">
            <b>Категория: ${catName}</b>
            <br>
            <br>
            

            <c:if test="${!empty param.catId}">
                
                <form method="post" action="../Admin/addParam" >
                    <select name="paramId">
                        <c:if test="${!empty params}">
                        <c:forEach var="parametr" items="${params}">
                            <option value="${parametr.id}">${parametr.name} - ${paramTypeMap.get(parametr.paramType)}</option>
                        </c:forEach>
                        </c:if>
                        <c:if test="${empty params}">
                            <option value="">Нет доступных параметров</option>
                        </c:if>
                    </select>
                    <label>Обяз. <input type="checkbox" name="req"></label>
                    <input type="hidden" name="catId" value="${param.catId}">
                    <button type="submit" class="btn">Добавить параметр</button>
                </form>
                    <br><br>
            <b>Параметры: </b>
            
                <c:if test="${!empty catParamLinks}">
                    <table>
                        <tr><th>Наименование</th><th>Тип</th>
                            <th>Обязателен</th><th>Удалить</th></tr>
                                <c:forEach var="link" items="${catParamLinks}">
                            <tr><td>${link.param.name}</td><td>${paramTypeMap.get(link.param.paramType)}</td>
                                <td>${reqTypeMap.get(link.reqType)}</td><td><a href="../Admin/deleteParamFromCat?catId=${link.cat.id}&paramId=${link.param.id}">x</a></td></tr>
                            </c:forEach>
                    </table>
                </c:if>
                <c:if test="${empty catParamLinks}">
                    <br><br>
                    В категории нет добавленных параметров
                </c:if>
            </c:if>

        </div>

        <div id="overlay"></div>
        <script>$('.add_cat').click(function () {
    var cid = $(this).attr('data-id');
    $('[name = parentId]').val(cid);
});</script>
    </body>
</html>