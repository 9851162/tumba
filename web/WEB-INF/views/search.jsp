<%-- 
    Document   : search
    Created on : 31.01.2016, 2:32:54
    Author     : bezdatiuzer
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
                                <img id="largeImage1" class="large largeImage" src="${ad.getFirstPreviewPath()}">
                            </div>
                            <div id="thumbs1" class="thumbs miniprew">
                                <c:forEach var="previewPath" items="${ad.getPreviewpaths()}">
                                    <img class="prev4change" src="${previewPath}">
                                </c:forEach>
                            </div>
                        </div>

                        <div class="opisanie">
                            <div class="col1">
                                <h3>Продавец</h3>
                                <p>${ad.author.name}</p>
                                <h3>Товар</h3>
                                <p>${ad.name}</p>
                                <a href="" class="ashow">Контакты продавца</a>
                                <div class="abody" style="display:none;">${ad.phone}<br><br>${ad.email}</div>
                            </div>
                            <div class="col3">
                                <h3>Описание</h3>
                                <p class="minitext"> ${ad.getSmallDesc()} </p>
                                <p class="maxtext"> ${ad.description} </p>
                                <a href="#" class="aoo"><div class="btnoo">оценить объявление</div></a>
                            </div>
                            <div class="col2">
                                <div class="datetov">
                                    Дата <fmt:formatDate type="date" pattern="dd.MM.yyyy" value="${ad.insertDate}"/>
                                </div>
                                <div class="price"><fmt:formatNumber value="${ad.price}"/>руб.</div>

                                <div class="formtodo">

                                        <c:if test="${(!empty userAds&&!empty userAds.get(ad.id)&&ad.status==0)||role=='admin'}">
                                            <a class="btn-del todo-btn" href="<c:url value="../Ad/delete?adId=${ad.id}&wish=${param.wish}&action=${param.action}"/>">Удалить</a>
                                        </c:if>
                                        <c:if test="${role=='admin'||(!empty userAds&&!empty userAds.get(ad.id)&&ad.status==0)}">
                                            <a href="#changeAdForm" data-id="${ad.id}" class="open_modal btn-chen adChanger todo-btn">Изменить</a>
                                        </c:if>
                                        <a class="btn-buy todo-btn" href="<c:url value="../Ad/buy?adId=${ad.id}&wish=${param.wish}&action=${param.action}"/>">Купить</a>  
                                        
                                    <c:if test="${role=='admin'&&ad.status!=0}">
                                        <form style="float: right;" action="<c:url value="../Ad/changeStatus" />">
                                            <input type="hidden" name="wish" value="${wish}">
                                            <input type="hidden" name="adId" value="${ad.id}">
                                            <select name="status">
                                                <option value="2">Оплачено</option>
                                                <option value="3">Доставлено</option>
                                            </select>
                                            <input type="submit" class="btn-chen" value="установить">
                                        </form>
                                    </c:if>
                                </div>

                                <div class="minmenu">
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
                                    <a class="choose" data-ad-id="${ad.id}" style="cursor: pointer;"><img class="${imgClass}" src=${chosenImg}><div>добавить в избранное</div></a>
                                        <c:if test="${role=='user'||role=='admin'}">
                                        <a href="#modal4" class="${msgPossible}" data-ad-id="${ad.id}" style="cursor: pointer;"><img src="../img/dop4.png"><div>отправить сообщение</div></a>
                                        </c:if>
                                        <c:if test="${empty role}">
                                        <a href="#modalalert" class="open_modal" data-ad-id="${ad.id}" style="cursor: pointer;"><img src="../img/dop4.png"><div>отправить сообщение</div></a>
                                        </c:if>
                                    <a class="compareAdder" data-ad-id="${ad.id}" style="cursor: pointer;"><img class="${compClass}" src="../img/dop3.png"><div>добавить к сравнению</div></a>
                                    <a href="../Main/?action=showoneitem&adId=${ad.id}" target="_blank" style="cursor: pointer;"><img src="../img/dop2.png"><div>открыть в новом окне</div></a>
                                        <c:if test="${role=='user'||role=='admin'}">
                                        <a href="#modal4" class="${msgPossible}" data-ad-id="${ad.id}" style="cursor: pointer;"><img src="../img/dop1.png"><div>предложить свою цену</div></a>
                                        </c:if>
                                        <c:if test="${empty role}">
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
