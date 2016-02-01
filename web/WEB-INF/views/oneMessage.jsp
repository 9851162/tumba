<%-- 
    Document   : oneMessage
    Created on : 01.02.2016, 17:57:03
    Author     : bezdatiuzer
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="boxtoitem">
    <c:if test="${!empty role}">
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
                <div>результатов: ${msgCount}</div>
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

        <div id="grid">
            <div class="whiteboxmes">
                <div class="title">сообщение</div>
                <c:if test="${!empty inboxMessage}">
                    <div class=" "> 
                        <div class="mess">
                            <div class="mesname"><div class="messicon"><img src="../img/che.png"></div>${inboxMessage.sender.name}</div>  
                            <div class="mestime"><div class="messicon"><img src="../img/date.png"></div><fmt:formatDate type="both" pattern="dd.MM.yyyy H:mm" value="${inboxMessage.insertDate}"/></div> 
                            <div class="meshead" title="${inboxMessage.subject}"><div class="messicon"><img src="../img/blackmaik.png"></div>${inboxMessage.getShortSubject()}</div>  
                        </div>  
                        <div class="mestext">
                            <p>${inboxMessage.text}
                        </div>  
                        <div class="mess">
                            <form class="otvetform" method="post" action="<c:url value="../Message/send" />">
                                тема <input style="margin-bottom: 10px;" name="subject" type="text" value="Re (${inboxMessage.getShortSubject()})">
                                <textarea name="text" placeholder="Нажмите здесь,чтобы ответить..."></textarea>
                                <input type="hidden" id="msgIdentifier" name="adId" value="${inboxMessage.ad.id}">
                                <input class="btnotpravka btn-buy" type="submit" value="Отправить">
                            </form>
                        </div>  								

                    </div>
                </c:if>
            </div>
        </div>
    </c:if>
</div>
