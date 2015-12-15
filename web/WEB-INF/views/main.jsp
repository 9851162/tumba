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
        <!--<link href="../css/bootstrap.min.css" rel="stylesheet" type="text/css" >-->
        <link rel="stylesheet" type="text/css" href="../css/font-awesome.min.css" >
        <link rel="stylesheet" type="text/css" href="../css/style.css">
        <link rel="stylesheet" type="text/css" href="../css/animate.css">
        <link rel="stylesheet" type="text/css" href="../css/jquery-ui.min.css">

    </head>
    <body>

        <div id="wrapper">

            <%@include file="/WEB-INF/jsp/menu.jsp" %>

            <div id="search_add">
                <div class="tosearch">
                    <div class="formsearch">
                        <form id="searchForm" style="margin-bottom: 0px;" method="post" action="<c:url value="../Main/" />">
                            <input type="text" name="wish" placeholder="Впишите ваше желание" value="${wish}">
                            <button type="submit" class="" style="height: 46px;width: 200px;margin-left: 25px;">Поиск</button>
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
                    <a href="#modal1" class="open_modal" style="display: block;margin-left: 40px;height: 73px;">НОВОЕ<br> ОБЪЯВЛЕНИЕ</a>
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
                <c:if test="${!empty advancedSearchParams}">
                    <div id="advancedSearchParamsContainer">
                        <h4>Параметры</h4>

                        <table id="searchParamTable">
                            <tr><td><label class="searchParamLabel">Цена</label></td>
                                <td>
                                        <select form="searchForm" name="priceCondition"><option value="1">></option><option selected value="0">=</option><option value="-1"><</option></select>
                                    </td>
                                <td><input form="searchForm" type="text" name="searchPrice" placeholder="Цена"></td></tr>
                            <c:forEach var="searchParam" items="${advancedSearchParams}">
                                <tr>
                                    <c:if test="${searchParam.paramType==1}">
                                        <td><label class="searchParamLabel">${searchParam.name}</label></td>
                                        <td></td>
                                        <td> <input form="searchForm" type="text" name="stringVals" placeholder="${searchParam.name}"></td>
                                    <input form="searchForm" type="hidden" name="stringIds" value="${searchParam.id}">
                                </c:if>

                                <c:if test="${searchParam.paramType==2}">
                                    <td><label class="searchParamLabel">${searchParam.name} </label></td>
                                    <td>
                                        <select form="searchForm" name="numCondition"><option value="1">></option><option selected value="0">=</option><option value="-1"><</option></select>
                                    </td>
                                    <td> <input form="searchForm" type="text" name="numVals" placeholder="${searchParam.name}"></td>
                                    <input form="searchForm" type="hidden" name="numIds" value="${searchParam.id}">
                                </c:if>

                                <c:if test="${searchParam.paramType==3&&!empty searchParam.options}">
                                    <td><label class="searchParamLabel">${searchParam.name} </label></td>
                                    <td></td>
                                    <td> <select form="searchForm" name="selVals">
                                            <c:forEach var="opt" items="${searchParam.options}">
                                                <option value="${opt.id}">${opt.name}</option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                    <input form="searchForm" type="hidden" name="selIds" value="${searchParam.id}">
                                </c:if>

                                <c:if test="${searchParam.paramType==4&&!empty searchParam.options}">
                                    <td><label class="searchParamLabel">${searchParam.name} </label></td>
                                    <td></td>
                                    <c:set var="searchParamOptionSize" value="5"/>
                                    <c:if test="${searchParam.options.size()<5}">
                                        <c:set var="searchParamOptionSize" value="${searchParam.options.size()}"/>
                                    </c:if>

                                    <td> <select form="searchForm" multiple="true" size="${searchParamOptionSize}" style="vertical-align: middle;" name="multyVals">
                                            <c:forEach var="opt" items="${searchParam.options}">
                                                <option value="${searchParam.id}_${opt.id}">${opt.name}</option>
                                            </c:forEach>
                                        </select></td>
                                    <input form="searchForm" type="hidden" name="multyIds" value="${searchParam.id}">
                                </c:if>

                                <c:if test="${searchParam.paramType==5}">
                                    <td><label class="searchParamLabel">${searchParam.name} </label></td>
                                    <td></td>
                                    <td><select name="booleanVals" form="searchForm">
                                            <option value="">не выбрано</option>
                                            <option value="1">да</option>
                                            <option value="0">нет</option>
                                        </select></td>
                                    <input form="searchForm" type="hidden" name="booleanIds" value="${searchParam.id}">
                                </c:if>

                                <c:if test="${searchParam.paramType==6}">
                                    <td><label class="searchParamLabel">${searchParam.name} </label></td>
                                    <td><select form="searchForm" name="dateCondition"><option value="1">></option><option selected value="0">=</option><option value="-1"><</option></select></td>
                                    <td> <input form="searchForm" type="text" name="dateVals" class="isDatepicker" placeholder="${searchParam.name}"></td>
                                    <input form="searchForm" type="hidden" name="dateIds" value="${searchParam.id}">
                                </c:if>
                                </tr>
                            </c:forEach>
                        </table>
                    </div>

                </c:if>
                <c:if test="${!empty catNamesWithCountsMap}">
                    <div id="catInfoInSearch">
                        <c:forEach var="catName" items="${catNamesWithCountsMap.keySet()}">
                            <div class="adsFoundInCatInfo"><span style="color:#00547e;">${catName}</span> ${catNamesWithCountsMap.get(catName)}</div>
                        </c:forEach>
                    </div>
                </c:if>
            </div>





            <c:set var="choosePossible" value="choose"/>
            <c:set var="comparePossible" value="compareAdder"/>

            <div class="icons">
                <c:if test="${role=='user'||role=='admin'}">
                    <c:set var="msgPossible" value="open_modal messageSender"/>
                    <a href="<c:url value="../Main/?action=purchases" />"><div id="ico" class="ico1"><img src="../img/menu1.png"> </div></a>
                    <a href="<c:url value="../Main/?action=sales" />"><div id="ico" class="ico2"><img src="../img/menu2.png"> </div></a>
                    <a href="<c:url value="../Main/regions" />"><div id="ico" class="ico3"><img src="../img/menu3.png"> </div></a>
                        </c:if>
                        <c:if test="${empty role}">
                    <a href="#modalalert"><div id="ico" class="ico1 open_modal"><img src="../img/menu1.png"> </div></a>
                    <a href="#modalalert"><div id="ico" class="ico2 open_modal"><img src="../img/menu2.png"> </div></a>
                    <a href="#modalalert"><div id="ico" class="ico3 open_modal"><img src="../img/menu3.png"> </div></a>
                        </c:if>
                <a href="<c:url value="../Main/?action=chosen" />"><div id="ico" class="ico4"><img src="../img/menu4.png"> </div></a>
                <a href="<c:url value="../Main/comparison" />"><div id="ico" class="ico5"><img src="../img/menu5.png"> </div></a>
            </div>


            <div class="left_side">
                <c:if test="${role=='user'||role=='admin'}">
                    <a style="text-decoration: none;" href="<c:url value="../Main/?action=purchases" />"><div class="menuitem">Мои покупки ${myBuyCount}<img src="../img/strright.png"></div></a>
                    <a style="text-decoration: none;" href="<c:url value="../Main/?action=sales" />"><div class="menuitem">Мои продажи ${mySellCount}<img src="../img/strright.png"></div></a>
                    <a style="text-decoration: none;" href="<c:url value="../Main/regions" />"><div class="menuitem">Регионы<img src="../img/strright.png"></div></a>
                        </c:if>
                        <c:if test="${empty role}">
                    <a style="text-decoration: none;" href="#modalalert" class="open_modal"><div class="menuitem">Мои покупки ${myBuyCount}<img src="../img/strright.png"></div></a>
                    <a style="text-decoration: none;" href="#modalalert" class="open_modal"><div class="menuitem">Мои продажи ${mySellCount}<img src="../img/strright.png"></div></a>
                    <a style="text-decoration: none;" href="#modalalert" class="open_modal"><div class="menuitem">Регионы<img src="../img/strright.png"></div></a>
                        </c:if>
                <a style="text-decoration: none;" href="<c:url value="../Main/?action=chosen" />"><div class="menuitem">Избранное <span id="chosenCount">${chosenCount}</span><img src="../img/strright.png"></div></a>
                <a style="text-decoration: none;" href="<c:url value="../Main/comparison" />"><div class="menuitem">Сравнение <span id="compareCount">${compareCount}</span><img src="../img/strright.png"></div></a>

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
                        <a href="<c:url value="../Main/?order=show_count&wish=${param.wish}&action=${action}" />">по популярности</a>
                        <img src="../img/vertline.png">
                        <a href="<c:url value="../Main/?order=insert_date&wish=${param.wish}&action=${action}" />">по дате</a>
                        <img src="../img/vertline.png">
                        <a href="<c:url value="../Main/?order=price&wish=${param.wish}&action=${action}" />">по цене</a>
                    </div>
                </div>

                <c:if test="${not empty adList}">
                    <div id="grid">
                        <c:set var="itempos" value="0"/>
                        <c:forEach var="ad" items="${adList}">
                            <div class="item smal" item-position="${itempos}" item-next="${itempos+1}" data-ad-id="${ad.id}">
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
                                            <img id="largeImage1" class="large largeImage" src="${ad.getPathToImg(0)}">
                                        </div>
                                        <div id="thumbs1" class="thumbs miniprew">
                                            <img class="prev4change" src="${ad.getPathToImg(0)}">
                                            <img class="prev4change" src="${ad.getPathToImg(1)}">
                                            <img class="prev4change" src="${ad.getPathToImg(2)}">
                                            <img class="prev4change" src="${ad.getPathToImg(3)}">
                                            <img class="prev4change" src="${ad.getPathToImg(4)}">
                                            <img class="prev4change" src="${ad.getPathToImg(5)}">
                                            <img class="prev4change" src="${ad.getPathToImg(6)}">
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
                                            <a href="#" class="aoo"><div class="btnoo">оценить объявление</div></a>
                                        </div>
                                        <div class="col2">
                                            <h3>Дата</h3>
                                            <p><fmt:formatDate type="date" pattern="dd.MM.yyyy" value="${ad.insertDate}"/></p>
                                            <div class="price"><fmt:formatNumber value="${ad.price}"/></div>


                                            <div class="minmenu">
                                                <div style="width: 100%;height: 28px;">&nbsp;
                                                <c:if test="${ad.status==0}">
                                                    <c:if test="${!empty userAds&&!empty userAds.get(ad.id)}">
                                                        <a href="#changeAdForm" data-id="${ad.id}" class="open_modal btn-buy adChanger" style="font: 12px Arial; float: right;">изменить</a>
                                                    </c:if>
                                                        <form style="float: right;" action="<c:url value="../Ad/buy" />">
                                                            <input type="hidden" name="wish" value="${wish}">
                                                            <input type="hidden" name="adId" value="${ad.id}">
                                                            <input type="submit" class="btn-buy" value="Купить">
                                                        </form>
                                                    </c:if>
                                                    <c:if test="${role=='admin'&&ad.status!=0}">
                                                        <form style="float: right;" action="<c:url value="../Ad/changeStatus" />">
                                                            <input type="hidden" name="wish" value="${wish}">
                                                            <input type="hidden" name="adId" value="${ad.id}">
                                                            <select name="status">
                                                                <option value="2">Оплачено</option>
                                                                <option value="3">Доставлено</option>
                                                            </select>
                                                            <input type="submit" class="btn-buy" value="установить">
                                                        </form>
                                                    </c:if>
                                                        </div>
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
                                                    <c:if test="${role=='user'||role=='admin'}">
                                                    <a href="#modal4" class="${msgPossible}" data-ad-id="${ad.id}" style="cursor: pointer;"><img src="../img/dop4.png"><div>отправить сообщение</div></a>
                                                    </c:if>
                                                    <c:if test="${empty role}">
                                                    <a href="#modalalert" class="open_modal" data-ad-id="${ad.id}" style="cursor: pointer;"><img src="../img/dop4.png"><div>отправить сообщение</div></a>
                                                    </c:if>
                                                <a class="${comparePossible}" data-ad-id="${ad.id}" style="cursor: pointer;"><img class="${compClass}" src="../img/dop3.png"><div>добавить к сравнению</div></a>
                                                    <c:if test="${role=='user'||role=='admin'}">
                                                    <a><img src="../img/dop2.png"><div>открыть в новом окне</div></a>
                                                    <a href="#modal4" class="${msgPossible}" data-ad-id="${ad.id}" style="cursor: pointer;"><img src="../img/dop1.png"><div>предложить свою цену</div></a>
                                                    </c:if>
                                                    <c:if test="${empty role}">
                                                    <a href="#modalalert" class="open_modal"><img src="../img/dop2.png"><div>открыть в новом окне</div></a>
                                                    <a href="#modalalert" class="open_modal" data-ad-id="${ad.id}" style="cursor: pointer;"><img src="../img/dop1.png"><div>предложить свою цену</div></a>
                                                    </c:if>
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

                    <div class="boxtoinput">
                        <div class="num">5</div>
                        <div class="toin">
                            <label>Регионы</label>
                            <select name="regionId">
                                <option value="0">вся Россия</option>
                                <c:forEach var="region" items="${availableRegions}">
                                    <option value="${region.id}">${region.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="boxtoinput">
                        <div class="num">6</div>
                        <div class="toin todata">
                            <label>Выбор даты для размещения объявления</label>
                            <div class="minlab">c</div><input type="text" name="dateFrom" class="isDatepicker" value="${dateFrom}"><div class="minlab">по</div><input type="text" name="dateTo" class="isDatepicker" value="${dateTo}">
                        </div>
                    </div>

                    <div class="boxtoinput">
                        <div class="num">7</div>
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

                    <c:set var="nextNum" value="8"/>
                    <c:if test="${empty userId}">
                        <div class="boxtoinput">
                            <div class="num">${nextNum}</div>
                            <c:set var="nextNum" value="${nextNum+1}"/>
                            <div class="toin">
                                <label>email</label>
                                <input name="email" type="email" value="${email}">
                            </div>
                        </div>
                    </c:if>
                    <div id="boxforparams" >

                    </div>
                    <div class="form-group">
                        <button type="submit" style="margin-top:10px;" class="btn btn-primary">Добавить</button>
                    </div>
                </form>
            </div>
            <c:if test="${empty role}">
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
                            <button type="submit" class="btn" style="margin-top: 10px;">Добавить</button>
                        </div>
                    </form>
                </div>
            </c:if>

            <c:if test="${empty role}">
                <div id="modal3" class="modal_form modal_div">

                    <div class="nameform">АВТОРИЗАЦИЯ</div>
                    <form  method="post" action="../j_spring_security_check" class="login">
                        <div class="boxtoinput">
                            <div style="padding-left:55px;" class="toin">
                                <label>Логин</label>
                                <input name="j_username" type="text">
                            </div>
                        </div>
                        <div class="boxtoinput">
                            <div style="padding-left:55px;" class="toin">
                                <label>Пароль</label>
                                <input name="j_password" type="password">
                            </div>
                        </div>
                        <div style="padding-left:55px;" class="toin">
                            <button type="submit" class="btn">войти</button>
                            <a class="btn-link" style="float:right;" href="<c:url value="../User/passRecovery"/>">восстановить пароль</a>
                        </div>
                    </form>
                </div>
            </c:if>

            <div id="modal4" class="modal_form modal_div">
                <div class="nameform">отправить сообщение</div>
                <form id="sendMessage" method="post" action="<c:url value="../Message/send" />">
                    <div class="boxtoinput">
                        <div class="toin">
                                <label>Тема</label>
                            <div class="minopright">до 255 символов</div>
                            <input name="subject" type="text" value="">
                        </div>
                    </div>
                    <div class="boxtoinput">
                        <div class="toin">
                            <!--<div class="minopright">до 1000 символов</div>-->
                            <label>Сообщение</label>
                            <textarea name="message" type="textarea" value=""></textarea>
                        </div>
                    </div>
                    <input type="hidden" id="msgIdentifier" name="adId" value="">
                    <input type="hidden" name="wish" value="${wish}">
                    <div class="form-group">
                        <button type="submit" class="btn">отправить</button>
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
                                    <c:forEach var="aregion" items="${availableRegions}">
                                        <c:set var="regClass" value="btn-default"/>
                                        <c:if test="${!empty regionSet}">
                                            <c:if test="${aregion.id==regionSet}">
                                                <c:set var="regClass" value="btn-primary"/>
                                            </c:if>
                                        </c:if>
                                        <tr><td><a title="${aregion.name}" style="width: 150px;" class="btn ${regClass}" href="<c:url value="../Main/chooseRegion?regionId=${aregion.id}&wish=${wish}" />">${aregion.getShortName()}</a></td>
                                                
                                                <td><c:if test="${!empty homeSet && homeSet==aregion.id}"><i class="fa fa-home"></i></c:if></td>
                                                
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
                                    <li style="list-style-type:none;margin-left: 0;padding-left: 0;"><input disabled style="width: initial;cursor: pointer;" id="${state.id}" class="stateSelector" data-method="show" name="stateIds" type="checkbox" ${stateInReg} value="${state.id}"><label id="${state.id}" data-method="show" class="opener" style="cursor: pointer;">${state.name} (${checkedLocksInReg}/${state.getLocalities().size()})</label></li>
                                        <c:if test="${!empty state.localities}">
                                        <ul>
                                            <c:forEach var="loc" items="${state.localities}">
                                                <c:set var="locInReg" value=""/>
                                                <c:if test="${!empty locsInRegMap.get(loc.id)}">
                                                    <c:set var="locInReg" value="checked"/>
                                                </c:if>
                                                <li style="list-style-type:none;margin-left: 0;padding-left: 0;"><label style="cursor: pointer;" class="locLabel" data-method="show" data-state-id="${state.id}"><input disabled style="width: initial;cursor: pointer;" name="localIds" id="${loc.id}" class="locSelector" data-method="show" data-state-id="${state.id}" type="checkbox" ${locInReg} value="${loc.id}">${loc.name}</label></li>
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
                <c:if test="${empty role}">
                    <div>
                        <form id="settingRegion" method="post" action="<c:url value="../Main/createAndMountRegion" />">
                            <div class="toin">
                                <ul>
                                    <li style="list-style-type:none;margin-left: 0;padding-left: 0;"><input style="cursor:pointer;" name="all" data-method="set" class="allRegionsSelector" type="checkbox" value="1"><label class="allRegionsOpener" data-method="set" style="cursor: pointer;">Все</label></li>
                                            <c:forEach var="state" items="${states}">
                                                <c:set var="stateInReg" value=""/>
                                                <c:if test="${!empty statesInRegMap.get(state.id)}">
                                                    <c:set var="stateInReg" value="checked"/>
                                                </c:if>
                                    <li style="list-style-type:none;margin-left: 0;padding-left: 0;"><input style="width: initial;cursor: pointer;" id="${state.id}" class="stateSelector" data-method="set" name="stateIds" type="checkbox" ${stateInReg} value="${state.id}"><label id="${state.id}" data-method="set" class="opener" style="cursor: pointer;">${state.name} (<span class="checkedLocsCount" id="${state.id}" data-method="set">0</span>/<span data-method="set" class="locsAmount">${state.getLocalities().size()})</span></label></li>
                                            <c:if test="${!empty state.localities}">
                                            <ul>
                                                <c:forEach var="loc" items="${state.localities}">
                                                    <c:set var="locInReg" value=""/>
                                                    <c:if test="${!empty locsInRegMap.get(loc.id)}">
                                                        <c:set var="locInReg" value="checked"/>
                                                    </c:if>
                                                    <li style="list-style-type:none;margin-left: 0;padding-left: 0;"><label style="cursor: pointer;" class="locLabel hidden" data-method="set" data-state-id="${state.id}"><input style="width: initial;cursor: pointer;" name="localIds" id="${loc.id}" class="locSelector" data-method="set" data-state-id="${state.id}" type="checkbox" ${locInReg} value="${loc.id}">${loc.name}</label></li>
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
                </c:if>
            </div>

            <div id="changeAdForm" class="modal_form modal_div">

                <div class="nameform">ИЗМЕНИТЬ ОБЪЯВЛЕНИЕ</div>
                <form  method="post" action="<c:url value="../Ad/changeAd" />">

                    <div class="boxtoinput">
                        <!--<div class="num">1</div>-->
                        <div class="toin">
                            <label>Краткое название товара или услуги</label>
                            <div class="minopright">до 30 символов</div>
                            <input name="shortName" type="text" value="">
                        </div>
                    </div>

                    <div class="boxtoinput">
                        <!--<div class="num">2</div>-->
                        <div class="toin">
                            <label>Описание</label>
                            <div class="minopright">до 500 символов</div>
                            <textarea name="description" type="textarea" value=""></textarea>
                        </div>
                    </div>

                    <!--<div class="boxtoinput">
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
                    </div>-->

                    <div class="boxtoinput">
                        <!--<div class="num">4</div>-->
                        <div class="toin">
                            <label for="price">Цена</label>
                            <input class="form-control" name="price" id="price" type="text" value="">
                        </div>
                    </div>

                    <!--<div class="boxtoinput">
                        <div class="num">5</div>
                        <div class="toin">
                            <label>Регионы</label>
                            <select name="regionId">
                                <option value="0">вся Россия</option>
                    <c:forEach var="region" items="${availableRegions}">
                        <option value="${region.id}">${region.name}</option>
                    </c:forEach>
                </select>
            </div>
        </div>-->

                    <div class="boxtoinput">
                        <!--<div class="num">6</div>-->
                        <div class="toin todata">
                            <label>Выбор даты для размещения объявления</label>
                            <div class="minlab">c</div><input type="text" name="dateFrom" class="isDatepicker" value=""><div class="minlab">по</div><input type="text" name="dateTo" class="isDatepicker" value="">
                        </div>
                    </div>

                    <!--<div class="boxtoinput">
                        <div class="num">7</div>
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
        </div>-->

                    <!--<c:set var="nextNum" value="8"/>
                    <c:if test="${empty userId}">
                        <div class="boxtoinput">
                            <div class="num">${nextNum}</div>
                        <c:set var="nextNum" value="${nextNum+1}"/>
                        <div class="toin">
                            <label>email</label>
                            <input name="email" type="email" value="${email}">
                        </div>
                    </div>
                    </c:if>
                    <div id="boxforparams" >

                    </div>-->
                    <input type="hidden" name="adId" value="">
                    <input type="hidden" name="action" value="${action}">
                    <input type="hidden" name="wish" value="${wish}">
                    <div class="form-group">
                        <button type="submit" class="btn" style="margin-top: 10px;">Изменить</button>
                    </div>
                </form>
            </div>
                    
            <div id="modalalert" class="modal_form modal_div">
                <div style="text-align: center;" class="toin todata">чтобы использовать данную функцию, необходимо пройти авторизацию</div>
                <form  method="post" action="../j_spring_security_check" class="login">
                    <div class="boxtoinput">
                        <div style="padding-left:55px;" class="toin">
                            <label>Логин</label>
                            <input name="j_username" type="text">
                        </div>
                    </div>
                    <div class="boxtoinput">
                        <div style="padding-left:55px;" class="toin">
                            <label>Пароль</label>
                            <input name="j_password" type="password">
                        </div>
                    </div>
                    <div style="padding-left:55px;" class="toin">
                        <button type="submit" class="btn">Войти</button>
                        <a class="btn-link" style="float:right;" href="<c:url value="../User/passRecovery"/>">восстановить пароль</a>
                    </div>
                </form>
            </div>

            <div id="modalerror" class="modal_form modal_div">
                <div class="nameform">Ошибки</div>
                <%@include file="/WEB-INF/jsp/error.jsp" %>
            </div>
            
            <div id="modalmessage" class="modal_form modal_div">
                <div class="nameform">Сообщение</div>
                <c:if test="${! empty messages}">
                    <center>
                        <div class="" >
                            <c:forEach items="${messages}" var="msg" >
                                <p>${msg}</p>
                            </c:forEach>
                        </div>
                    </center>
                </c:if>
            </div>

        </div>

        <div id="overlay"></div>
        
        <c:if test="${param.auth==false}">
            <script>
                $('#modal3').find('div.nameform').after('<div style="color:red;text-align:center;">ошибка авторизации, неверные логин или пароль</div>');
                $('#overlay').fadeIn(400, //пoкaзывaем oверлэй
                        function () { // пoсле oкoнчaния пoкaзывaния oверлэя
                            $('#modal3') // берем стрoку с селектoрoм и делaем из нее jquery oбъект
                                    .css('display', 'block')
                                    .animate({opacity: 1, top: '0%'}, 200); // плaвнo пoкaзывaем
                        });
            </script>
        </c:if>

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
        <c:if test="${empty errors && !empty messages}">
            <script>
                $('#overlay').fadeIn(400, //пoкaзывaем oверлэй
                        function () { // пoсле oкoнчaния пoкaзывaния oверлэя
                            $('#modalmessage') // берем стрoку с селектoрoм и делaем из нее jquery oбъект
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
                                            <c:set var="paramOptionSize" value="5"/>
                                            <c:if test="${parametr.options.size()<5}">
                                                <c:set var="paramOptionSize" value="${parametr.options.size()}"/>
                                            </c:if>
                                            <br><label style="text-align: center;">${parametr.name} <select multiple="true" size="${paramOptionSize}" style="vertical-align: middle;" name="multyVals">
                                                    <c:forEach var="opt" items="${parametr.options}">
                                                        <option value="${parametr.id}_${opt.id}">${opt.name}</option>
                                                    </c:forEach>
                                                </select></label>
                                            <input type="hidden" name="multyIds" value="${parametr.id}">
                                        </c:when>

                                        <c:when test="${parametr.paramType==5}">
                                            <br><label>${parametr.name} <input type="checkbox" name="booleanVals"></label>
                                            <input type="hidden" name="booleanIds" value="${parametr.id}">
                                        </c:when>

                                        <c:when test="${parametr.paramType==6}">
                                            <br><label>${parametr.name} <input type="text" name="dateVals" class="paramDatepicker" placeholder="${parametr.name}"></label>
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
