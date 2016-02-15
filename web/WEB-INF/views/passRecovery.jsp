<%-- 
    Document   : passRecovery
    Author     : bezdatiuzer
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Восстановление пароля</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="Cache-Control" content="no-cache">
        <link rel="stylesheet" type="text/css" href="../css/style.css">
        <link rel="stylesheet" type="text/css" href="../css/animate.css">
    </head>
    <body style="overflow-y: scroll;">
        <div id="wrapper">
            <%@include file="/WEB-INF/jsp/menu.jsp" %>
            <!--<h3>настройки</h3>-->
            <div id="whiteList">
                <h2>восстановление пароля</h2>
                <c:if test="${empty param.hash}">
                <form action="<c:url value="../User/passRecovery"/>">
                        <div style="margin-top:10px;width: 100%;">
                            <label>email:</label>
                            <input name="email" type="text">
                            <button type="submit" class="btn">отправить</button>
                        </div>
                    </form>
                </c:if>
                <c:if test="${!empty param.hash&&!empty param.email}">
                    <form action="<c:url value="../User/passUpdate"/>">
                        <!--<div style="margin-top:10px;width: 100%;">-->
                            <label style="width: 100%; margin-top: 10px;">новый пароль:</label>
                            <div style="width: 100%; margin-top: 10px;">
                                <input name="password" type="password">
                            </div>
                            <label style="width: 100%; margin-top: 10px;">повторно новый пароль:</label>
                            <div style="width: 100%; margin-top: 10px;">
                                <input name="checkPassword" type="password">
                            </div>
                            <div style="width: 100%; margin-top: 10px;">
                                <input type="hidden" name="hash" value="${param.hash}">
                                <input type="hidden" name="email" value="${param.email}">
                                <button type="submit" class="btn">сохранить</button>
                            </div>
                        <!--</div>-->
                    </form>
                </c:if>
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
                        <button type="submit" class="btn" style="margin-top: 10px;">Добавить</button>
                    </div>
                </form>
            </div>
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
                        <button type="submit" class="btn">Войти</button>
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
    </body>

</html>
