<%-- 
    Document   : messages
    Created on : 29.09.2015, 19:44:13
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
                <div class="title">сообщения</div>
                <c:if test="${empty inboxMessages}">
                    <div>пока Вам никто не писал</div>
                </c:if>
                <c:if test="${!empty inboxMessages}">
                    <c:forEach var="msg" items="${inboxMessages}">
                        <a href="<c:url value="../Main/?action=${showMessage}&msgId=${msg.id}" />">
                            <table class="onemes">
                                <tbody><tr>
                                        <td class=" namemes">${msg.sender.name}</td>  
                                        <td class=" headmes">${msg.getShortSubject()}</td>  
                                        <td class=" timemes"><fmt:formatDate type="both" pattern="dd.MM.yyyy H:mm" value="${msg.insertDate}"/></td> 

                                    </tr>
                                </tbody></table>
                        </a>
                                        
                    </c:forEach>
                </c:if>
            </div>
        </div>
    </c:if>
</div>
