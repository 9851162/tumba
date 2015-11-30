<%-- 
    Document   : login
    Created on : 16.03.2015, 19:17:08
    Author     : Юрий
--%>
<%@page session="true" import="java.util.*" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Сайт с объявлениями</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="Cache-Control" content="no-cache">
        <!--<link href="./css/bootstrap.min.css" rel="stylesheet" type="text/css" >-->
        <link rel="stylesheet" type="text/css" href="../css/style.css">
        <link rel="stylesheet" type="text/css" href="../css/animate.css">
    </head>
    <body>
        <div id="wrapper">

            <%@include file="/WEB-INF/jsp/menu.jsp" %>
            <div id="search_add">
                <div class="tosearch">
                    <div class="formsearch">
                        <form style="margin-bottom: 0px;" action="<c:url value="../Main/" />">
                            <input type="text" name="wish" placeholder="Впишите ваше желание" value="${wish}">
                            <button type="submit" class="">Поиск</button>
                        </form>
                    </div>
                    <div class="controlsearch">
                        <c:if test="${region.isAllRussia()}">
                            <c:set var="arHREFChosen" value="regChosen"/>
                        </c:if>
                        <c:if test="${region.isHomeRegion()}">
                            <c:set var="drHREFChosen" value="regChosen"/>
                        </c:if>
                        <c:set var="regionName" value="выбор региона"/>
                        <c:if test="${!region.isHomeRegion()&&!region.isAllRussia()}">
                            <c:set var="rHREFChosen" value="regChosen"/>
                            <c:set var="regionName" value="свой регион"/>
                            <c:if test="${!empty region.name}">
                                <c:set var="regionName" value="${region.name}"/>
                            </c:if>
                        </c:if>

                        <a class="${arHREFChosen}" href="<c:url value="../Main/createAndMountRegion?all=1&wish=${wish}" />">вся россия</a>

                        <c:if test="${role=='user'||role=='admin'}">
                            <c:if test="${empty homeSet}">
                                <a href="#modal5" class="open_modal ${drHREFChosen}">домашний регион</a>
                            </c:if>
                            <c:if test="${!empty homeSet}">
                                <a href="<c:url value="../Main/chooseRegion?wish=${wish}&regionId=${homeSet}" />" class="${drHREFChosen}">домашний регион</a>
                            </c:if>
                            <a href="#modal5" class="open_modal ${rHREFChosen}">${regionName}</a>
                        </c:if>
                        <c:if test="${role!='user'&&role!='admin'}">
                            <a href="#modal6" class="open_modal ${drHREFChosen}">домашний регион</a>
                            <a href="#modal6" class="open_modal ${rHREFChosen}">${regionName}</a>
                        </c:if>
                    </div>
                </div>
                <div class="toobnov">
                    <a href="#modal1" class="open_modal">НОВОЕ<br> ОБЪЯВЛЕНИЕ</a>
                </div>
            </div>

            <div id="advanced_search">
                <h1>Расширенный поиск</h1>
                <div class="cat">категория<a href="" class="spoiler_links"><div class="tostrel"></div></a>
                    <div class="spoiler_body " style="display:none;">
                        <c:forEach var="cat" items="${notSelectedCats}">
                            <div style="cursor: pointer;" value="${cat.id}"><a style="text-decoration: none;color: #00547e;" href="<c:url value="../Main/addCat4Search?catId=${cat.id}&wish=${param.wish}" />">
                                    ${cat.name}</a>
                            </div>
                        </c:forEach>
                    </div>
                </div>
                <div id="dobContainer">
                <c:forEach var="cat" items="${selectedCats}">
                    <div class="dob">${cat.name}<a href="<c:url value="../Main/removeCat4Search?catId=${cat.id}&wish=${param.wish}" />"><img src="../img/plus.png"></a>
                    </div>
                </c:forEach>
                </div>
            </div>

            <c:if test="${role=='user'||role=='admin'}">
                <c:set var="choosePossible" value="choose"/>
                <div class="icons icons_expand">
                    <a href="<c:url value="../Main/?action=purchases" />"><div id="ico" class="ico1"><img src="../img/menu1.png"> </div></a>
                    <a href="<c:url value="../Main/?action=sales" />"><div id="ico" class="ico2"><img src="../img/menu2.png"> </div></a>
                    <a href="<c:url value="../Main/regions" />"><div id="ico" class="ico3"><img src="../img/menu3.png"> </div></a>
                    <a href="<c:url value="../Main/?action=chosen" />"><div id="ico" class="ico4"><img src="../img/menu4.png"> </div></a>
                    <a href="<c:url value="../Main/comparison" />"><div id="ico" class="ico5"><img src="../img/menu5.png"> </div></a>
                </div>
            </c:if>
            <c:if test="${empty compAds}">
                <div class="boxtoitem boxtoitembig whitepod" style="padding-left: 20px;">Ooops... Нечего сравнивать, добавьте объявления для сравнения.</div>
            </c:if>   

            <c:if test="${!empty compAds}">
                <div class="boxtoitem boxtoitembig whitepod">
                    <div style="    width: 168px;;    float: left;">
                        <div class="oneatr firstatr">
                            
                        </div>
                        <c:forEach var="attr" items="${compMap.keySet()}">
                            <div class="oneatr">
                                ${attr.name}
                            </div>
                        </c:forEach>
                        <!--<div class="oneatr">
                                        Атрибут 1
                                </div>
                        <div class="oneatr">
                                        Атрибут 2
                        </div>	-->		
                    </div>
                    <div class="tabsrav">
                        <div class="onestr">
                            <c:forEach var="ad" items="${compAds}">
                                <div class="prewtov">
                                  <a href="<c:url value="../Ad/removeFromComparison?adId=${ad.id}&wish=${param.wish}" />"><div class="ad_top_bubble" title="убрать из сравнения">
                                      <div class="ad_bubble_delete_wrap">
                                      <div class="ad_bubble_delete"></div>
                                      </div>
                                    </div></a>
                                    <img class="sravimg" src="../imgs/${ad.id}/0">
                                    <div class="sravopisanie">
                                        <div class="sravmin">
                                            <h3>Продавец</h3>
                                            <p>${ad.author.name}</p>
                                            <h3>Товар</h3>
                                            <p>${ad.cat.name}</p>
                                        </div>
                                        <div class="sravprice">
                                            <div class="price"><fmt:formatNumber value="${ad.price}"/></div>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                        
                        <c:forEach var="key" items="${compMap.keySet()}">
                            <div class="onestr" style="width:100%;">
                                <c:forEach var="paramVal" items="${compMap.get(key)}">
                                    <c:if test="${empty paramVal}"><c:set var="paramVal" value="-"/></c:if>
                                    <div>${paramVal}
                                    </div>
                                </c:forEach>
                            </div>
                        </c:forEach>


                        		

                    </div>
                </div>
            </c:if>


            <!--</div>-->
            <footer>
                <div class="tofotmin"></div>
                <div class="tofotmin"><a href="#">наш сервис</a></div>
                <div class="tofotmin"><a href="#">информация</a></div>
                <div class="tofotmin"><a href="#">о компании</a></div>
                <div class="tofotmin"></div>
            </footer>

            <div id="modal1" class="modal_form modal_div">
                <div class="nameform">НОВОЕ ОБЪЯВЛЕНИЕ</div>
                <form  method="post" enctype="multipart/form-data" action="<c:url value="../Ad/add" />">

                    <div class="boxtoinput">
                        <div class="num">1</div>
                        <div class="toin">
                            <label>Краткое название товара или услуги</label>
                            <div class="minopright">до 30 символов</div>
                            <input name="shortName" type="text" value="${shortName}">
                        </div>
                    </div>
                    <div class="boxtoinput">
                        <div class="num">2</div>
                        <div class="toin">
                            <label>Описание</label>
                            <div class="minopright">до 500 символов</div>
                            <textarea name="description" type="textarea" value="">${description}</textarea>
                        </div>
                    </div>


                    <div class="boxtoinput">
                        <div class="num">3</div>
                        <div class="toin">
                            <label>Добавление фото</label>
                            <div class="form-group">
                                <div class="file_upload">
                                    <button type="button"></button>
                                    <input type="file" multiple name="previews" onchange='$("#upload-file-info").html($(this).val());'>
                                </div>
                                <span class='label label-info' id="upload-file-info" ></span>
                            </div>
                        </div>
                    </div>

                    <div class="boxtoinput">
                        <div class="num">4</div>
                        <div class="toin">
                            <label for="price">Цена</label>
                            <input class="form-control" name="price" id="price" type="text" value="${price}">
                        </div>
                    </div>

                    <c:if test="${!empty catList}">
                        <div class="boxtoinput">
                            <div class="num">5</div>
                            <div class="toin">
                                <label for="catId">Выбор категории для объявления</label>
                                <select class="categoryChanger" name="catId">
                                    <option value="">Не выбрана</option>
                                    <c:forEach var="cat" items="${catList}">
                                        <c:set var="prefix" value="${cat.getPrefix()}"/>
                                        <option value="${cat.id}">
                                            ${prefix}${cat.name}
                                        </option>
                                    </c:forEach>
                                </select>

                            </div>

                        </div>
                    </c:if>

                    <c:set var="nextNum" value="6"/>
                    <c:if test="${empty userId}">
                        <c:set var="nextNum" value="7"/>
                        <div class="boxtoinput">
                            <div class="num">6</div>
                            <div class="toin">
                                <label>email</label>
                                <input name="email" type="email" value="${email}">
                            </div>
                        </div>
                    </c:if>
                    <div id="boxforparams" >

                    </div>
                    
                    <div class="form-group">
                        <button type="submit" class="btn">Добавить</button>
                    </div>
                </form>
            </div>

            <div id="modal2" class="modal_form modal_div">
                <div class="nameform">РЕГИСТРАЦИЯ</div>
                <form  method="post" action="../Main/registration">
                    <div class="boxtoinput">
                        <div class="num">1</div>
                        <div class="toin">
                            <label>Имя</label>
                            <div class="minopright">до 30 символов</div>
                            <input name="name" type="text">
                        </div>
                    </div>
                    <div class="boxtoinput">
                        <div class="num">2</div>
                        <div class="toin">
                            <label>Телефон</label>
                            <div class="minopright">до 30 символов</div>
                            <input name="phone" type="text">
                        </div>
                    </div>
                    <div class="boxtoinput">
                        <div class="num">3</div>
                        <div class="toin">
                            <label>E-mail</label>
                            <div class="minopright">до 30 символов</div>
                            <input name="email" type="email">
                        </div>
                    </div>
                    <div class="boxtoinput">
                        <div class="num">4</div>
                        <div class="toin">
                            <label>Пароль</label>
                            <div class="minopright">до 30 символов</div>
                            <input name="password" type="password">
                        </div>
                    </div>
                    <div class="boxtoinput">
                        <div class="num">5</div>
                        <div class="toin">
                            <label>Подтверждение пароля</label>
                            <div class="minopright">до 30 символов</div>
                            <input name="passconfirm" type="password">
                        </div>
                    </div>

                    <div class="form-group">
                        <button type="submit" class="btn">Добавить</button>
                    </div>
                </form>
            </div>

            <div id="modal3" class="modal_form modal_div">
                <div class="nameform">АВТОРИЗАЦИЯ</div>
                <form  method="post" action="../j_spring_security_check" class="login">
                    <div class="boxtoinput">
                        <div class="toin">
                            <label>Логин</label>
                            <input name="j_username" type="text">
                        </div>
                    </div>
                    <div class="boxtoinput">
                        <div class="toin">
                            <label>Пароль</label>
                            <input name="j_password" type="password">
                        </div>
                    </div>
                    <div class="form-group">
                        <button type="submit" class="btn">Войти</button>
                    </div>
                </form>
            </div>
                    
            <!--выбор региона с авторизацией-->
            <div id="modal5" class="modal_form modal_div">
                <div class="nameform">Выбор региона</div>
                <c:if test="${!empty role}">
                    <div style="width: 30%;float: left;">
                        <div class="toin">Мои регионы<br><br>
                            <c:if test="${empty availableRegions}">
                                Пока нет созданных регионов
                            </c:if>
                            <c:if test="${!empty availableRegions}">
                                <table>
                                    <c:forEach var="region" items="${availableRegions}">
                                        <c:set var="regClass" value="btn-default"/>
                                        <c:if test="${!empty regionSet}">
                                            <c:if test="${region.id==regionSet}">
                                                <c:set var="regClass" value="btn-primary"/>
                                            </c:if>
                                        </c:if>
                                        <tr><td><a title="${region.name}" style="width: 150px;" class="btn ${regClass}" href="<c:url value="../Main/chooseRegion?regionId=${region.id}&wish=${wish}" />">${region.getShortName()}</a></td>
                                                <c:if test="${!empty homeSet && homeSet==region.id}">
                                                <td><i class="fa fa-home"></i></td>
                                                </c:if>
                                        </c:forEach>
                                </table>
                            </c:if>
                        </div>
                    </div>
                    <div style="width: 69%;float: right;">
                        <div class="">
                            <ul>
                                <c:forEach var="state" items="${states}">
                                    <c:set var="stateInReg" value=""/>
                                    <c:set var="checkedLocksInReg" value="0"/>
                                    <c:if test="${!empty statesInRegMap.get(state.id)}">
                                        <c:set var="stateInReg" value="checked"/>
                                        <c:set var="checkedLocksInReg" value="${statesInRegMap.get(state.id)}"/>
                                    </c:if>
                                    <li style="list-style-type:none;margin-left: 0;padding-left: 0;"><input disabled style="width: initial;cursor: pointer;" id="${state.id}" data-method="show" class="stateSelector" name="stateIds" type="checkbox" ${stateInReg} value="${state.id}"><label id="${state.id}" class="opener" data-method="show" style="cursor: pointer;">${state.name} (${checkedLocksInReg}/${state.getLocalities().size()})</label></li>
                                        <c:if test="${!empty state.localities}">
                                        <ul>
                                            <c:forEach var="loc" items="${state.localities}">
                                                <c:set var="locInReg" value=""/>
                                                <c:if test="${!empty locsInRegMap.get(loc.id)}">
                                                    <c:set var="locInReg" value="checked"/>
                                                </c:if>
                                                <li style="list-style-type:none;margin-left: 0;padding-left: 0;"><label style="cursor: pointer;" data-method="show" class="locLabel" data-state-id="${state.id}"><input disabled style="width: initial;cursor: pointer;" name="localIds" id="${loc.id}" class="locSelector" data-method="show" data-state-id="${state.id}" type="checkbox" ${locInReg} value="${loc.id}">${loc.name}</label></li>
                                                    </c:forEach>
                                        </ul>
                                    </c:if>
                                </c:forEach>
                            </ul>
                        </div>
                    </div>
                </c:if>
            </div>


            <!--выбор региона без авторизации-->
            <div id="modal6" class="modal_form modal_div">
                <div class="nameform">Выбор региона</div>
                <div>
                    <form id="settingRegion" method="post" action="<c:url value="../Main/createAndMountRegion" />">
                        <div class="toin">
                            <ul>
                                <li style="list-style-type:none;margin-left: 0;padding-left: 0;"><label id="allRegionsOpener" data-method="set" style="cursor: pointer;"><input style="width: initial;cursor: pointer;" name="all" class="allRegionsSelector" data-method="set" type="checkbox" value="1">Все</label></li>
                                        <c:forEach var="state" items="${states}">
                                            <c:set var="stateInReg" value=""/>
                                            <c:if test="${!empty statesInRegMap.get(state.id)}">
                                                <c:set var="stateInReg" value="checked"/>
                                            </c:if>
                                    <li style="list-style-type:none;margin-left: 0;padding-left: 0;"><input style="width: initial;cursor: pointer;" id="${state.id}" data-method="set" class="stateSelector" name="stateIds" type="checkbox" ${stateInReg} value="${state.id}"><label id="${state.id}" data-method="set" class="opener" style="cursor: pointer;">${state.name} (<span class="checkedLocsCount" id="${state.id}" data-method="set">0</span>${state.getLocalities().size()})</label></li>
                                        <c:if test="${!empty state.localities}">
                                        <ul>
                                            <c:forEach var="loc" items="${state.localities}">
                                                <c:set var="locInReg" value=""/>
                                                <c:if test="${!empty locsInRegMap.get(loc.id)}">
                                                    <c:set var="locInReg" value="checked"/>
                                                </c:if>
                                                <li style="list-style-type:none;margin-left: 0;padding-left: 0;"><label style="cursor: pointer;" class="locLabel hidden" data-method="set" data-state-id="${state.id}"><input style="width: initial;cursor: pointer;" name="localIds" id="${loc.id}" data-method="set" class="locSelector" data-state-id="${state.id}" type="checkbox" ${locInReg} value="${loc.id}">${loc.name}</label></li>
                                                    </c:forEach>
                                        </ul>
                                    </c:if>
                                </c:forEach>
                            </ul>
                        </div>
                        <input type="hidden" name="wish" value="${wish}">
                        <div class="form-group" style="padding-left: 15px;">
                            <button type="submit" class="btn">Выбрать</button>
                        </div>
                    </form>
                </div>
            </div>

            <div id="modalerror" class="modal_form modal_div">
                <div class="nameform">Ошибки</div>
                <%@include file="/WEB-INF/jsp/error.jsp" %>

            </div>     
        </div>



        <div id="overlay"></div>
        <script>
                        $('.categoryChanger').change(function () {
                            var catId = $(this).val();
                            $('#boxforparams').html($('.catParamsDiv[data-cat-id=' + catId + ']').clone())
                        });
        </script>
        <c:if test="${!empty errors}">
            <script>
                $('#overlay').fadeIn(400, //пoкaзывaем oверлэй
                        function () { // пoсле oкoнчaния пoкaзывaния oверлэя
                            $('#modalerror') // берем стрoку с селектoрoм и делaем из нее jquery oбъект
                                    .css('display', 'block')
                                    .animate({opacity: 1, top: '0%'}, 200); // плaвнo пoкaзывaем
                        });
            </script>
        </c:if>


        <div style="float:left;" class="hidden">
            <div data-cat-id="" class="catParamsDiv"></div>
            <c:forEach var="catId" items="${catParamsMap.keySet()}">
                <div data-cat-id="${catId}" class="catParamsDiv">

                    <c:if test="${!empty catParamsMap.get(catId)}">

                        <div class="">
                            <div class="num">${nextNum}</div>
                            <div class="toin">
                                <label id="boxforparamslabel">Параметры</label>


                                <c:forEach var="parametr" items="${catParamsMap.get(catId)}">
                                    <c:choose>

                                        <c:when test="${parametr.paramType==1}">
                                            <br><label>${parametr.name} <input type="text" name="stringVals" placeholder="${parametr.name}"></label>
                                            <input type="hidden" name="stringIds" value="${parametr.id}">
                                        </c:when>

                                        <c:when test="${parametr.paramType==2}">
                                            <br><label>${parametr.name} <input type="text" name="numVals" placeholder="${parametr.name}"></label>
                                            <input type="hidden" name="numIds" value="${parametr.id}">
                                        </c:when>

                                        <c:when test="${parametr.paramType==3&&!empty parametr.options}">
                                            <br><label>${parametr.name} <select name="selVals">
                                                    <c:forEach var="opt" items="${parametr.options}">
                                                        <option value="${opt.id}">${opt.name}</option>
                                                    </c:forEach>
                                                </select></label>
                                            <input type="hidden" name="selIds" value="${parametr.id}">
                                        </c:when>

                                        <c:when test="${parametr.paramType==4&&!empty parametr.options}">
                                            <br><label style="text-align: center;">${parametr.name} <select multiple="true" style="vertical-align: middle;" name="multyVals">
                                                    <c:forEach var="opt" items="${parametr.options}">
                                                        <option value="${parametr.id}_${opt.id}">${opt.name}</option>
                                                    </c:forEach>
                                                </select></label>
                                            <input type="hidden" name="multyIds" value="${parametr.id}">
                                        </c:when>

                                        <c:when test="${parametr.paramType==5}">
                                            <br><label>${parametr.name} <input type="checkbox" name="booleanVals">
                                                <input type="hidden" name="booleanIds" value="${parametr.id}">
                                            </c:when>

                                            <c:when test="${parametr.paramType==6}">
                                                <br><label>${parametr.name} <input type="date" name="dateVals" placeholder="${parametr.name}"></label>
                                                <input type="hidden" name="dateIds" value="${parametr.id}">
                                            </c:when>

                                        </c:choose>

                                    </c:forEach>
                                    <br>
                                    </div>
                                    </div>
                                </c:if>

                                </div>
                            </c:forEach>
                            </div>



                            </body></html>
