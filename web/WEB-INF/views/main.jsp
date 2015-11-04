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
        <script src="<c:url value='/js/jquery-1.11.2.min.js'/>"></script>
        <div id="wrapper">

            <%@include file="/WEB-INF/jsp/menu.jsp" %>
            <div id="search_add">
                <div class="tosearch">
                    <div class="formsearch">
                        <form style="margin-bottom: 0px;" action="<c:url value="../Main/" />">
                            <input type="text" name="wish" placeholder="Впишите ваше желание" value="${wish}">
                            <button type="submit" class="btn btn-success">Поиск</button>
                        </form>
                    </div>
                    <div class="controlsearch">
                        <a href="#">вся россия</a>
                        <a href="#">домашний регион</a>
                        <a href="#">выбор региона</a>
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
                <c:set var="msgPossible" value="open_modal messageSender"/>
                <c:set var="comparePossible" value="compareAdder"/>

                <div class="icons">
                    <a href="<c:url value="../Main/purchases" />"><div id="ico" class="ico1"><img src="../img/menu1.png"> </div></a>
                    <a href="<c:url value="../Main/sales" />"><div id="ico" class="ico2"><img src="../img/menu2.png"> </div></a>
                    <a href="<c:url value="../Main/" />"><div id="ico" class="ico3"><img src="../img/menu3.png"> </div></a>
                    <a href="<c:url value="../Main/chosen" />"><div id="ico" class="ico4"><img src="../img/menu4.png"> </div></a>
                    <a href="<c:url value="../Main/comparison" />"><div id="ico" class="ico5"><img src="../img/menu5.png"> </div></a>
                </div>
            </c:if>

            <div class="left_side  ">
                <c:if test="${role=='user'||role=='admin'}">    
                    <a style="text-decoration: none;" href="<c:url value="../Main/purchases" />"><div class="menuitem">Мои покупки ${myBuyCount}<img src="../img/strright.png"></div></a>
                    <a style="text-decoration: none;" href="<c:url value="../Main/sales" />"><div class="menuitem">Мои продажи ${mySellCount}<img src="../img/strright.png"></div></a>
                    <a style="text-decoration: none;" href="<c:url value="../Main/" />"><div class="menuitem">Регионы<img src="../img/strright.png"></div></a>
                    <a style="text-decoration: none;" href="<c:url value="../Main/chosen" />"><div class="menuitem">Избранное <span id="chosenCount">${chosenCount}</span><img src="../img/strright.png"></div></a>
                    <a style="text-decoration: none;" href="<c:url value="../Main/comparison" />"><div class="menuitem">Сравнение <span id="compareCount">${compareCount}</span><img src="../img/strright.png"></div></a>
                        </c:if>
                <div class="promo"> </div>
                <div class="promo"> </div>
            </div>

            <div class="boxtoitem">
                <div id="grid_navigation">
                    <div class="expand">
                        <img src="../img/greystrleft.png"><div>развернуть результат</div><img src="../img/greystrup.png">
                    </div>

                    <div class="rollup invisible">
                        <img src="../img/greystrright.png">
                        <div>свернуть результат</div>
                        <img src="../img/greystrdown.png">
                    </div>

                    <div class="torez">
                        <img style="    margin: 1px 0 0 0;" src="../img/vertline.png">
                        <div>результатов: ${resCount}</div>
                    </div>
                    <div class="tosort">
                        <img src="../img/vertline.png">
                        <a>по популярности</a>
                        <img src="../img/vertline.png">
                        <a>по дате</a>
                        <img src="../img/vertline.png">
                        <a>по цене</a>
                    </div>
                </div>

                <c:if test="${not empty adList}">
                    <div id="grid">
                        <c:set var="itempos" value="0"/>
                        <c:forEach var="ad" items="${adList}">
                            <div class="item smal" item-position="${itempos}" item-next="${itempos+1}">
                                <c:set var="itempos" value="${itempos+1}"/>
                                <c:if test="${ad.status==1}">
                                    <div class="moz"></div>
                                </c:if>
                                <c:if test="${ad.status==2}">
                                    <div class="mop"></div>
                                </c:if>
                                <c:if test="${ad.status==3}">
                                    <div class="mdos"></div>
                                </c:if>
                                <div class="toramka divall">
                                    <div class="toblockimg">
                                        <div id="panel" class="prewimg">
                                            <img id="largeImage1" class="large largeImage" src="../imgs/${ad.id}/0">
                                        </div>
                                        <div id="thumbs1" class="thumbs miniprew">
                                            <img class="prev4change" src="../imgs/${ad.id}/0">
                                            <img class="prev4change" src="../imgs/${ad.id}/1">
                                            <img class="prev4change" src="../imgs/${ad.id}/2">
                                            <img class="prev4change" src="../imgs/${ad.id}/3">
                                            <img class="prev4change" src="../imgs/${ad.id}/4">
                                            <img class="prev4change" src="../imgs/${ad.id}/5">
                                            <img class="prev4change" src="../imgs/${ad.id}/6">
                                        </div>
                                    </div>
                                    <div class="opisanie">
                                        <div class="col1">
                                            <h3>Продавец</h3>
                                            <p>${ad.author.name}</p>
                                            <h3>Товар</h3>
                                            <p>${ad.name}</p>
                                        </div>
                                        <div class="col3">
                                            <h3>Описание</h3>
                                            <p class="minitext"> ${ad.getSmallDesc()} </p>
                                            <p class="maxtext"> ${ad.description} </p>
                                        </div>
                                        <div class="col2">
                                            <h3>Дата</h3>
                                            <p><fmt:formatDate type="date" pattern="dd.MM.yyyy" value="${ad.insertDate}"/></p>
                                            <div class="price">${ad.price}</div>
                                            <c:if test="${ad.status==0}">
                                                <form action="<c:url value="../Ad/buy" />">
                                                    <input type="hidden" name="wish" value="${wish}">
                                                    <input type="hidden" name="adId" value="${ad.id}">
                                                    <input type="submit" class="btn-buy" value="Купить">
                                                </form>
                                            </c:if>

                                            <div class="minmenu">
                                                <c:if test="${role=='admin'&&ad.status!=0}">
                                                    <div><form action="<c:url value="../Ad/changeStatus" />">
                                                            <input type="hidden" name="wish" value="${wish}">
                                                            <input type="hidden" name="adId" value="${ad.id}">
                                                            <select name="status">
                                                                <option value="2">Оплачено</option>
                                                                <option value="3">Доставлено</option>
                                                            </select>
                                                            <input type="submit" class="btn-buy" value="установить">
                                                        </form></div>
                                                    </c:if>
                                                    <c:set var="chosenImg" value="../img/dop5.png"/>
                                                    <c:set var="imgClass" value=""/>
                                                    <c:if test="${!empty chosenAdsMap.get(ad.id)}">
                                                        <c:set var="chosenImg" value="../img/dop5v2.png"/>
                                                        <c:set var="imgClass" value="chosen"/>
                                                    </c:if>
                                                    <c:set var="compClass" value=""/>
                                                    <c:if test="${!empty comparingAdsMap.get(ad.id)}">
                                                        <c:set var="compClass" value="comparing"/>
                                                    </c:if>
                                                <a class="${choosePossible}" data-ad-id="${ad.id}" style="cursor: pointer;"><img class="${imgClass}" src=${chosenImg}><div>добавить в избранное</div></a>
                                                <a href="#modal4" class="${msgPossible}" data-ad-id="${ad.id}" style="cursor: pointer;"><img src="../img/dop4.png"><div>отправить сообщение</div></a>
                                                <a class="${comparePossible}" data-ad-id="${ad.id}" style="cursor: pointer;"><img class="${compClass}" src="../img/dop3.png"><div>добавить к сравнению</div></a>
                                                <a><img src="../img/dop2.png"><div>открыть в новом окне</div></a>
                                                <a><img src="../img/dop1.png"><div>предложить свою цену</div></a>

                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="button_expand hidden"><div class="boxssilka"><img src="../img/whiteniz.png"><div>развернуть</div><img src="../img/whiteniz.png"></div></div>
                                <div class="button_rollUp hidden"><div class="boxssilka"><img src="../img/whiteverh.png"><div>cвернуть</div><img src="../img/whiteverh.png"></div></div>
                            </div>
                        </c:forEach>
                    </div>
                </c:if>

            </div>
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
                    <!--<div class="boxtoinput">
                            <div class="num">4</div>
                            <div class="toin">
                                    <label>Выбор категории для объявления</label>
                        <div class="dob">добавить<img src="./img/plus.png"> </div>
                            </div>
                    </div>-->
                    <!--<div class="boxtoinput">
                            <div class="num">5</div>
                            <div class="toin">
                                    <label>Выбор регионов</label>
                        <div class="dob">добавить<img src="./img/plus.png"> </div>
                            </div>
                    </div>-->
                    <!--<div class="boxtoinput">
                            <div class="num">6</div>
                            <div class="toin todata">
                                    <label>Выбор даты для размещения объявления</label>
                                    <div class="minlab">c</div><input type="date"><div class="minlab">по</div><input type="date">
                            </div>
                    </div>-->
                    <div class="form-group">
                        <button type="submit" class="btn btn-success">Добавить</button>
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

                    <!--<div class="boxtoinput">
                            <div class="num">4</div>
                            <div class="toin">
                                    <label>Добавление фото</label>
                         <img src="../img/plusimg.png">
                            </div>
                    </div>
                    <div class="boxtoinput">
                            <div class="num">5</div>
                            <div class="toin">
                                    <label for="region">Выбор домашнего региона</label>
                                <div class="dob">добавить<img src="../img/plus.png"> </div>
                            </div>
                    </div>-->
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

            <div id="modal4" class="modal_form modal_div">
                <div class="nameform">Отправить сообщение</div>
                <form id="sendMessage" method="post" action="<c:url value="../Message/send" />">
                    <div class="boxtoinput">
                        <div class="toin">
                            <div class="minopright">до 500 символов</div>
                            <textarea name="message" type="textarea" value=""></textarea>
                        </div>
                    </div>
                    <input type="hidden" id="msgIdentifier" name="adId" value="">
                    <input type="hidden" name="wish" value="${wish}">
                    <div class="form-group">
                        <button type="submit" class="btn">Отправить</button>
                    </div>
                </form>
            </div>

            <div id="modalerror" class="modal_form modal_div">
                <div class="nameform">Ошибки</div>
                <%@include file="/WEB-INF/jsp/error.jsp" %>

            </div>
        </div>





        <div id="overlay"></div>
        <script src="../js/jquery.min.js"></script>
        <script src="../js/seller_scripts/script.js"></script>
        <script src="../js/seller_scripts/ajaxscript.js"></script>
        <script src="../js/seller_scripts/magic.js"></script>
        <script>
                                        $('.categoryChanger').change(function () {
                                            var catId = $(this).val();
                                            $('#boxforparams').html($('.catParamsDiv[data-cat-id=' + catId + ']').clone())
                                        });
        </script>
        <script>
            $('.messageSender').on('click', function () {
                var adId = $(this).attr('data-ad-id');
                $('#msgIdentifier').val(adId);
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
