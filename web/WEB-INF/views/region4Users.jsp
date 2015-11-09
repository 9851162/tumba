<%-- 
    Document   : regions
    Created on : 29.09.2015, 19:44:13
    Author     : bezdatiuzer
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="myTags" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Выбор региона</title>
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
            
            <div class="nameform">Выбор региона</div>
                <div style="width: 30%;float: left;">Мои регионы<br><br>
                    <c:if test="${empty availableRegions}">
                        Пока нет созданных регионов
                    </c:if>
                    <c:if test="${!empty availableRegions}">
                    <table>
                    <c:forEach var="region" items="${availableRegions}">
                        <tr><td>${region.name}</td><td><a href="<c:url value="../Main/chooseRegion?regionId=${region.id}&wish=${wish}" />">Выбрать</a></td>
                            <td><a href="<c:url value="../Main/setHomeRegion?regionId=${region.id}&wish=${wish}" />">Сделать домашним</a></td>
                            <td><a href="<c:url value="../Main/deleteRegion?regionId=${region.id}&wish=${wish}" />">x</a></td></tr>
                    </c:forEach>
                    </table>
                    </c:if>
                </div>
                <div style="width: 69%;float: right;">
                <form id="settingRegion" method="post" action="<c:url value="../Main/createAndMountRegion" />">
                    <div class="toin">
                        <label>Название<input type="text" name="name" placeholder="свой регион"></label>
                        <label style="padding-bottom: 3px;font-family: HelveticaNeueThin;font-size: 30px;display: block;width: 100%;">Регионы</label>
                        <div><table><tr><td style="text-align: left;vertical-align: top;">
                                        <input style="width: initial;" id="allRegionsSelector" type="checkbox"><label for="allRegionsSelector">Все</label>
                                            <c:forEach var="state" items="${states}">
                                            <br><label><input style="width: initial;" id="${state.id}" class="stateSelector" name="stateIds" type="checkbox" value="${state.id}">${state.name}</label>
                                            </c:forEach>
                                    </td>
                                    <td style="text-align: left;vertical-align: top;">
                                        <c:forEach var="state" items="${states}">
                                            <c:forEach var="loc" items="${state.localities}">
                                                <label><input style="width: initial;" name="localIds" id="${loc.id}" class="locSelector" data-state-id="${state.id}" type="checkbox" value="${loc.id}">${loc.name}(${state.name})</label><br>
                                                </c:forEach>
                                            </c:forEach>
                                    </td>
                                </tr></table>
                        </div>
                    </div>
                    <input type="hidden" name="wish" value="${wish}">
                    <div class="form-group">
                        <button type="submit" class="btn">Создать</button>
                    </div>
                </form>
                </div>

        </div>

        <div id="overlay"></div>
        <script>
            
        </script>
    </body>
</html>