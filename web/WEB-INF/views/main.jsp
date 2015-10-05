<%-- 
    Document   : login
    Created on : 16.03.2015, 19:17:08
    Author     : Юрий
--%>
<%@page session="true" import="java.util.*" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
    <body >
        <script src="<c:url value='/js/jquery-1.11.2.min.js'/>"></script>
        <!--<script src="<c:url value='/js/bootstrap.js'/>"> </script>-->
        <div id="wrapper">

            <%@include file="/WEB-INF/jsp/menu.jsp" %>
            <div id="search_add">
                <div class="tosearch">
                    <div class="formsearch">
                        <form style="margin-bottom: 0px;">
                            <input type="text" placeholder="Впишите ваше желание">
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

            <!--<div id="search_add">
                <div class="tosearch">
                        <div class="formsearch">
                                    <form method="post" action="Ad/list">
                                            <input type="text" placeholder="Впишите ваше желание">
                                            <button type="submit" class="">Поиск</button>
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
            </div>-->

            <div id="advanced_search">
                <h1>Расширенный поиск</h1>
                <div class="cat">категория<a href="" class="spoiler_links"><div class="tostrel"></div></a>
                    <div class="spoiler_body " style="display:none;">
                        <div>категория</div>
                        <div>категория</div>
                        <div>категория</div>
                    </div>
                </div>
                <div class="dob">добавить<a href="#"><img src="../img/plus.png"></a> </div>
            </div>


            <div class="icons">
                <div id="ico" class="ico1"><img src="../img/menu1.png"> </div>
                <div id="ico" class="ico2"><img src="../img/menu2.png"> </div>
                <div id="ico" class="ico3"><img src="../img/menu3.png"> </div>
                <div id="ico" class="ico4"><img src="../img/menu4.png"> </div>
                <div id="ico" class="ico5"><img src="../img/menu5.png"> </div>
            </div>

            <div class="left_side  ">
                <div class="menuitem">Мои покупки<img src="../img/strright.png"></div>
                <div class="menuitem">Мои продажи<img src="../img/strright.png"></div>
                <div class="menuitem">Регионы<img src="../img/strright.png"></div>
                <div class="menuitem">Избранное<img src="../img/strright.png"></div>
                <div class="menuitem">Сравнение<img src="../img/strright.png"></div>

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
                        <div>результатов:100500</div>
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

                    <c:set var="chosenAd" value="${adList.get(adList.size()-1)}"/>
                    <div id="grid">
                        <!--<div class="item medium" item-position="0" item-next="1">
                                    <div class="toramka divall">
                                            <div class="toblockimg">
                                                    <div id="panel" class="prewimg">
                                                            <img id="largeImage1" class="largeImage" src="img/tov/tov16.jpg">
                                                    </div>
                                                    <div id="thumbs1" class="thumbs miniprew">
                                                            <img src="img/tov/tov11.jpg">
                                                            <img src="img/tov/tov12.jpg">
                                                            <img src="img/tov/tov13.jpg">
                                                            <img src="img/tov/tov14.jpg">
                                                            <img src="img/tov/tov15.jpg">
                                                            <img src="img/tov/tov16.jpg">
                                                    </div>
                                            </div>
                                    <div class="opisanie">
                                            <div class="col1">
                                                    <h3>Продавец</h3>
                                                    <p>"Имя"</p>
                                                    <h3>Товар</h3>
                                                    <p>"Наименование"</p>
                                            </div>
                                        <div class="col3">
                                                    <h3>Описание</h3>
                                                    <p class="minitext"> "Описание" </p>
                                                    <p class="maxtext"> ${ad.description} </p>
                                            </div>
                                        <div class="col2">
                                                    <h3>Дата</h3>
                                                    <p>23 мая 23:23</p>
                                                    <div class="price">38,67 ₽</div>
                                                    <div class="minmenu">
                                                            <a href="#"><img src="img/dop5.png"><div>добавить в избранное</div></a>
                                                            <a href="#"><img src="img/dop4.png"><div>отправить сообщение</div></a>
                                                            <a href="#"><img src="img/dop3.png"><div>добавить к сравнению</div></a>
                                                            <a href="#"><img src="img/dop2.png"><div>открыть в новом окне</div></a>
                                                            <a href="#"><img src="img/dop1.png"><div>предложить свою цену</div></a>
                                                    </div>
                                            </div>
                                    </div>
                                    </div>
                                    <div class="button_expand hidden"><div class="boxssilka"><img src="img/whiteniz.png"><div>развернуть</div><img src="img/whiteniz.png"></div></div>
                                    <div class="button_rollUp hidden"><div class="boxssilka"><img src="img/whiteverh.png"><div>cвернуть</div><img src="img/whiteverh.png"></div></div>
                        </div>--><c:set var="itempos" value="0"/>
                        <c:forEach var="ad" items="${adList}">
                            <c:if test="${ad!=adList.get(adList.size()-1)}"> 
                                <div class="item smal" item-position="${itempos}" item-next="${itempos+1}">
                                    <c:set var="itempos" value="${itempos+1}"/>
                                    <div class="toramka divall">
                                        <div class="toblockimg">
                                            <div id="panel" class="prewimg">
                                                <img id="largeImage1" class="large largeImage" src="../Images/?id=${ad.id}&name=0">
                                            </div>
                                            <div id="thumbs1" class="thumbs miniprew">
                                                <img class="prev4change" src="../Images/?id=${ad.id}&name=0">
                                                <img class="prev4change" src="../Images/?id=${ad.id}&name=1">
                                                <img class="prev4change" src="../Images/?id=${ad.id}&name=2">
                                                <img class="prev4change" src="../Images/?id=${ad.id}&name=3">
                                                <img class="prev4change" src="../Images/?id=${ad.id}&name=4">
                                                <img class="prev4change" src="../Images/?id=${ad.id}&name=5">
                                                <img class="prev4change" src="../Images/?id=${ad.id}&name=6">
                                            </div>
                                        </div>
                                        <div class="opisanie">
                                            <div class="col1">
                                                <h3>Продавец</h3>
                                                <p>${ad.author.name}</p>
                                                <h3>Товар</h3>
                                                <p>${ad.cat.name}</p>
                                            </div>
                                            <div class="col3">
                                                <h3>Описание</h3>
                                                <p class="minitext"> ${ad.name} </p>
                                                <p class="maxtext"> ${ad.description} </p>
                                            </div>
                                            <div class="col2">
                                                <h3>Дата</h3>
                                                <p><fmt:formatDate type="date" value="${ad.insertDate}"/></p>
                                                <div class="price">${ad.price}</div>
                                                <div class="minmenu">
                                                    <a href="#"><img src="../img/dop5.png"><div>добавить в избранное</div></a>
                                                    <a href="#"><img src="../img/dop4.png"><div>отправить сообщение</div></a>
                                                    <a href="#"><img src="../img/dop3.png"><div>добавить к сравнению</div></a>
                                                    <a href="#"><img src="../img/dop2.png"><div>открыть в новом окне</div></a>
                                                    <a href="#"><img src="../img/dop1.png"><div>предложить свою цену</div></a>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="button_expand hidden"><div class="boxssilka"><img src="../img/whiteniz.png"><div>развернуть</div><img src="../img/whiteniz.png"></div></div>
                                    <div class="button_rollUp hidden"><div class="boxssilka"><img src="../img/whiteverh.png"><div>cвернуть</div><img src="../img/whiteverh.png"></div></div>
                                </div>
                            </c:if>
                        </c:forEach>
                        <div class="item medium"  item-position="${itempos}" item-next="${itempos+1}">
                            <div class="toramka divall">
                                <div class="toblockimg">
                                    <div id="panel" class="prewimg">
                                        <img id="largeImage9" class="large largeImage" src="../Images/?id=${chosenAd.id}&name=0">
                                    </div>
                                    <div id="thumbs9" class="thumbs miniprew">
                                        <img class="prev4change" src="../Images/?id=${chosenAd.id}&name=0">
                                        <img class="prev4change" src="../Images/?id=${chosenAd.id}&name=1">
                                        <img class="prev4change" src="../Images/?id=${chosenAd.id}&name=2">
                                        <img class="prev4change" src="../Images/?id=${chosenAd.id}&name=3">
                                        <img class="prev4change" src="../Images/?id=${chosenAd.id}&name=4">
                                        <img class="prev4change" src="../Images/?id=${chosenAd.id}&name=5">
                                        <img class="prev4change" src="../Images/?id=${chosenAd.id}&name=6">
                                    </div>
                                </div>
                                <div class="opisanie">
                                    <div class="col1">
                                        <h3>Продавец</h3>
                                        <p>${chosenAd.author.name}</p>
                                        <h3>Товар</h3>
                                        <p>${chosenAd.cat.name}</p>
                                    </div>
                                    <div class="col3">
                                        <h3>Описание</h3>
                                        <p class="minitext"> ${chosenAd.name} </p>
                                        <p class="maxtext"> ${chosenAd.description} </p>
                                    </div>
                                    <div class="col2">
                                        <h3>Дата</h3>
                                        <p><fmt:formatDate type="date" value="${chosenAd.insertDate}"/></p>
                                        <div class="price">${chosenAd.price}</div>
                                        <div class="minmenu">
                                            <a href="#"><img src="../img/dop5.png"><div>добавить в избранное</div></a>
                                            <a href="#"><img src="../img/dop4.png"><div>отправить сообщение</div></a>
                                            <a href="#"><img src="../img/dop3.png"><div>добавить к сравнению</div></a>
                                            <a href="#"><img src="../img/dop2.png"><div>открыть в новом окне</div></a>
                                            <a href="#"><img src="../img/dop1.png"><div>предложить свою цену</div></a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="button_expand hidden"><div class="boxssilka"><img src="../img/whiteniz.png"><div>развернуть</div><img src="../img/whiteniz.png"></div></div>
                            <div class="button_rollUp hidden"><div class="boxssilka"><img src="../img/whiteverh.png"><div>cвернуть</div><img src="../img/whiteverh.png"></div></div>
                        </div>
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
        </div>
        <div id="modal1" class="modal_form modal_div">
            <div class="nameform"></div>
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


                            <!--<button type="button" onchange='$("#upload-file-info").html($(this).val());' style="border-radius: 10px;width: 78px;height: 78px; background-image: url(../img/plusimg.png);background-repeat: no-repeat;cursor: pointer;border:0px;"></button>-->
                            <!--<input type="file" multiple name="previews" size="40" src="../img/plusimg.png"  onchange='$("#upload-file-info").html($(this).val());'>-->
                            <span class='label label-info' id="upload-file-info" ></span>
                        </div>
                        <!--<img src="../img/plusimg.png">-->
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

                            <!--<button type="button" class="addCat"/>-->

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

        <div id="modalerror" class="modal_form modal_div">
            <div class="nameform">Ошибки</div>
            <%@include file="/WEB-INF/jsp/error.jsp" %>

        </div>


        <div id="overlay"></div>
        <script src="../js/jquery.min.js"></script>
        <script src="../js/seller_scripts/script.js"></script><!---->
        <script src="../js/seller_scripts/magic.js">

        </script>
        <script>
            $('.categoryChanger').change(function () {
                var catId = $(this).val();
                $('#boxforparams').html($('.catParamsDiv[data-cat-id='+catId+']').clone())
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
                                            <br><label>${parametr.name} <input type="text" name="paramVals" placeholder="${parametr.name}"></label>
                                            </c:when>
                                            <c:when test="${parametr.paramType==2}">
                                            <br><label>${parametr.name} <input type="text" name="paramVals" placeholder="${parametr.name}"></label>
                                            </c:when>
                                            <c:when test="${parametr.paramType==3&&!empty parametr.options}">
                                            <br><label>${parametr.name} <select name="paramVals">
                                                    <c:forEach var="opt" items="${parametr.options}">
                                                        <option value="${opt.id}">${opt.name}</option>
                                                    </c:forEach>
                                                </select></label>
                                            </c:when>
                                            <c:when test="${parametr.paramType==4&&!empty parametr.options}">
                                                <br><label style="text-align: center;">${parametr.name} <select multiple="true" style="vertical-align: middle;" name="paramVals">
                                            <c:forEach var="opt" items="${parametr.options}">
                                                <option value="${opt.id}">${opt.name}</option>
                                            </c:forEach>
                                        </select></label>
                                </c:when>

                                        <c:when test="${parametr.paramType==5}">
                                            <br><label>${parametr.name} <select name="paramVals">
                                                    <option value="1">ДА</option>
                                                    <option value="0">НЕТ</option>
                                                </select></label>
                                            </c:when>
                                            <c:when test="${parametr.paramType==6}">
                                            <br><label>${parametr.name} <input type="date" name="paramVals" placeholder="${parametr.name}"></label>
                                            </c:when>

                                    </c:choose>
                                    <input type="hidden" name="paramIds" value="${parametr.id}">
                                </c:forEach>
                                <br>
                                </div>
                    </div>
                            </c:if>
                        
                </div>
            </c:forEach>
        </div>



    </body></html>
