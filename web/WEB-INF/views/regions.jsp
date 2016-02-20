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
            <%@include file="/WEB-INF/jsp/menu.jsp" %>
            <%@include file="/WEB-INF/jsp/error.jsp" %>
            <a href="<c:url value='/Admin/cats'/>">Категории</a>
            <a href="<c:url value='/Admin/params'/>">Параметры</a>
            <a href="<c:url value='/Admin/regions?countryId=1'/>">Регионы</a>
            <a href="<c:url value='/Admin/users'/>">Пользователи</a>
            <a href="<c:url value='/Main/'/>">Главная</a>
            <h3>Регионы</h3>
            <!--<div style="width: 33%; float: left;">
                <form  method="post" action="../Admin/addCountry" >
                    <div class="boxtoinput">
                        <div class="">
                            <input name="name" type="text" placeholder="Страна" style="margin-top: 36px;">
                        </div>
                    </div>
                    <input name="countryId" type="hidden" value="${param.countryId}">
                    <input name="stateId" type="hidden" value="${param.stateId}">
                    <div class="form-group">
                        <button type="submit" class="btn">Добавить</button>
                    </div>
                </form>
                <c:if test="${!empty countries}">
                    <div>
                        <ul>
                            <c:forEach var="country" items="${countries}">
                                <li><a href="<c:url value='../Admin/regions?countryId=${country.id}'/>">${country.name}</a> ${country.getStates().size()} <a href="#modalDelCountry" class="open_modal deletingCountry" data-id="${country.id}" style="cursor: pointer;">x</a></li>
                                </c:forEach>
                        </ul>
                    </div>
                </c:if>

            </div>-->
            <div style="width: 49%; float: left;">
                <c:if test="${!empty param.countryId}">
                    <form  method="post" action="../Admin/addState" >
                        <div class="boxtoinput">
                            <div class="">
                                <!--<label>Страна: ${country.name}</label>-->
                                <!--<label>Адм. округ</label>-->
                                <input style="margin-top:37px;" name="name" type="text" placeholder="Адм. округ">
                            </div>
                        </div>
                        <input name="countryId" type="hidden" value="${param.countryId}">
                        <input name="stateId" type="hidden" value="${param.stateId}">
                        <div class="form-group">
                            <button type="submit" class="btn btn-primary">Добавить</button>
                        </div>
                    </form>
                    <c:if test="${!empty states}">
                        <div>
                            <ul>
                                <c:forEach var="state" items="${states}">
                                    <li><a href="<c:url value='../Admin/regions?countryId=${param.countryId}&stateId=${state.id}'/>">${state.name}</a> ${state.getLocalities().size()} <a href="#modalDelState" class="open_modal deletingState" data-id="${state.id}" style="cursor: pointer;">x</a></li>
                                    </c:forEach>
                            </ul>
                        </div>
                    </c:if>
                </c:if>
            </div>
            <div style="width: 49%; float: right;">
                <c:if test="${!empty param.countryId && !empty param.stateId}">
                    <form  method="post" action="../Admin/addLocality" >
                        <div class="boxtoinput">
                            <div class="">
                                <label>Адм.Окр.: ${state.name}</label>
                                <!--<label>Нас. пункт</label>-->
                                <input name="name" type="text" placeholder="Нас. пункт">
                            </div>
                        </div>
                        <input name="countryId" type="hidden" value="${param.countryId}">
                        <input name="stateId" type="hidden" value="${param.stateId}">
                        <div class="form-group">
                            <button type="submit" class="btn btn-primary">Добавить</button>
                        </div>
                    </form>
                    <c:if test="${!empty localities}">
                        <div>
                            <ul>
                                <c:forEach var="locality" items="${localities}">
                                    <li><a>${locality.name} </a><a href="#modalDelLocality" class="open_modal deletingLocality" data-id="${locality.id}" style="cursor: pointer;">x</a></li>
                                    </c:forEach>
                            </ul>
                        </div>
                    </c:if>
                </c:if>
            </div>

            <div id="modalDelCountry" class="modal_form modal_div">
                <div class="nameform">Удалить страну <span id="countryName"></span> со всеми административными единицами?</div>
                <form  method="post" action="<c:url value='../Admin/deleteCountry'/>" >
                    <div class="form-group">
                        <input type="hidden" name="deletingCountryId" value="">
                        <input type="hidden" name="stateId" value="${param.stateId}">
                        <input type="hidden" name="countryId" value="${param.countryId}">
                        <button type="submit" class="btn">Удалить</button>
                    </div>
                </form>
            </div>

            <div id="modalDelState" class="modal_form modal_div">
                <div class="nameform">Удалить административный округ <span id="stateName"></span> со всеми входящими населенныйми пунктами?</div>
                <form  method="post" action="<c:url value='../Admin/deleteState'/>" >
                    <div class="form-group">
                        <input type="hidden" name="deletingStateId" value="">
                        <input type="hidden" name="stateId" value="${param.stateId}">
                        <input type="hidden" name="countryId" value="${param.countryId}">
                        <button type="submit" class="btn">Удалить</button>
                    </div>
                </form>
            </div>

            <div id="modalDelLocality" class="modal_form modal_div">
                <div class="nameform">Удалить населенный пункт <span id="localityName"></span>?</div>
                <form  method="post" action="<c:url value='../Admin/deleteLocality'/>" >
                    <div class="form-group">
                        <input type="hidden" name="deletingLocalityId" value="">
                        <input type="hidden" name="stateId" value="${param.stateId}">
                        <input type="hidden" name="countryId" value="${param.countryId}">
                        <button type="submit" class="btn">Удалить</button>
                    </div>
                </form>
            </div>

        </div>





        <div id="overlay"></div>
        <script>
            $('.deletingCountry').click(function () {
                $('[name = deletingCountryId]').val($(this).attr('data-id'));
                $('#countryName').html($(this).parent().find('a:first').text());
            });
            $('.deletingState').click(function () {
                $('[name = deletingStateId]').val($(this).attr('data-id'));
                $('#stateName').html($(this).parent().find('a:first').text());
            });
            $('.deletingLocality').click(function () {
                $('[name = deletingLocalityId]').val($(this).attr('data-id'));
                $('#localityName').html($(this).parent().find('a:first').text());
            });
        </script>
    </body>
</html>