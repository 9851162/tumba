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
            <a href="<c:url value='/Main/'/>">Главная</a>
            <h3>Регионы</h3>
            <div style="width: 33%; float: left;">
                <form  method="post" action="../Admin/addCountry" >
                    <div class="boxtoinput">
                        <div class="">
                            <label>Страна</label>
                            <input name="name" type="text">
                        </div>
                    </div>
                    <input name="countryId" type="hidden" value="${countryId}">
                    <input name="stateId" type="hidden" value="${stateId}">
                    <div class="form-group">
                        <button type="submit" class="btn">Добавить</button>
                    </div>
                </form>
                <c:if test="${!empty countries}">
                    <div>
                        <ul>
                            <c:forEach var="country" items="${countries}">
                                <li><a href="<c:url value='../Admin/regions?countryId=${countryId}'/>">${country.name}</a></li>
                                </c:forEach>
                        </ul>
                    </div>
                </c:if>

            </div>
            <div style="width: 33%; float: left;">
                <c:if test="${!empty countryId}">
                    <form  method="post" action="../Admin/addState" >
                        <div class="boxtoinput">
                            <div class="">
                                <label>Адм. округ</label>
                                <input name="name" type="text">
                            </div>
                        </div>
                        <input name="countryId" type="hidden" value="${countryId}">
                        <input name="stateId" type="hidden" value="${stateId}">
                        <div class="form-group">
                            <button type="submit" class="btn">Добавить</button>
                        </div>
                    </form>
                    <c:if test="${!empty states}">
                        <div>
                            <ul>
                                <c:forEach var="state" items="${states}">
                                    <li><a href="<c:url value='../Admin/regions?countryId=${countryId}&stateId=${state.id}'/>">${state.name}</a></li>
                                    </c:forEach>
                            </ul>
                        </div>
                    </c:if>
                </c:if>
            </div>
            <div style="width: 33%; float: left;">
                <c:if test="${!empty countryId && !empty stateId}">
                    <form  method="post" action="../Admin/addLocality" >
                        <div class="boxtoinput">
                            <div class="">
                                <label>Нас. пункт</label>
                                <input name="name" type="text">
                            </div>
                        </div>
                        <input name="countryId" type="hidden" value="${countryId}">
                        <input name="stateId" type="hidden" value="${stateId}">
                        <div class="form-group">
                            <button type="submit" class="btn">Добавить</button>
                        </div>
                    </form>
                    <c:if test="${!empty localities}">
                        <div>
                            <ul>
                                <c:forEach var="state" items="${states}">
                                    <li>${state.name}</li>
                                    </c:forEach>
                            </ul>
                        </div>
                    </c:if>
                </c:if>
            </div>



        </div>


        <div id="modal" class="modal_form modal_div">
            <div class="nameform">Добавить</div>
            <form  method="post" action="../Admin/addCountry" >
                <div class="boxtoinput">
                    <div class="toin">
                        <label>Название</label>
                        <input name="name" type="text">
                    </div>
                </div>
                <div class="form-group">
                    <button type="submit" class="btn">Добавить</button>
                </div>
            </form>
        </div>



        <div id="overlay"></div>
        <script>$('.add_cat').click(function () {
                var cid = $(this).attr('data-id');
                $('[name = parentId]').val(cid);
            });</script>
    </body>
</html>