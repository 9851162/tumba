<%-- 
    Document   : profile
    Author     : bezdatiuzer
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="myTags" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Мои настройки</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="Cache-Control" content="no-cache">
        <link rel="stylesheet" type="text/css" href="../css/style.css">
        <link rel="stylesheet" type="text/css" href="../css/animate.css">
    </head>
    <body style="overflow-y: scroll;">
        <div id="wrapper">
            <script src="<c:url value='/js/jquery-1.11.2.min.js'/>"></script>
            <script src="../js/seller_scripts/magic.js"></script>
            <%@include file="/WEB-INF/jsp/menu.jsp" %>
            <!--<h3>настройки</h3>-->
            <div id="userInfo">
                <div class="infodiv" style="height: 298px;"><div class="num">1</div>
                    <img style="float: left; width: 250px;height: 250px; margin-left: 10px;" src="${avatarPath}" alt="avatar">
                    <form enctype="multipart/form-data" method="post" action="<c:url value="uploadAvatar"/>">
                        <div style="width: 100%;float:left;margin-top:13px;margin-left: 50px;">
                            <div class="photoUpload" >
                                <button id="avatarSubmitterButton" class="btn btn-primary" style="float: left;">изменить фото</button>
                                <input id="avatarSubmitter" type="file" name="avatar" onchange="javascript:this.form.submit();">
                            </div>
                        </div>
                    </form>
                </div>
                <div class="infodiv"><div class="num">2</div>
                    <a href="#changeUserParamWindow" class="open_modal btn btn-primary paramChanger" style="float: left;margin-top:3px;margin-left: 10px;">изменить</a>
                    <label id="name">Имя:</label><span>${user.name}</span>
                </div>
                <!--<div class="infodiv"><div class="num">3</div>
                    <a href="#changeUserParamWindow" class="open_modal btn btn-primary paramChanger" style="float: left;margin-top:3px;margin-left: 10px;">изменить</a>
                    <label id="email">email:</label><span>${user.email}</span>
                </div>-->
                <div class="infodiv"><div class="num">3</div>
                    <a href="#changeUserParamWindow" class="open_modal btn btn-primary paramChanger" style="float: left;margin-top:3px;margin-left: 10px;">изменить</a>
                    <label id="phone">Телефон:</label><span>${user.phone}</span>
                </div>
                <div class="infodiv"><div class="num">4</div>
                    <a href="#changeUserPassword" class="open_modal btn btn-primary" style="float: left;margin-top:3px;margin-left: 10px;">изменить пароль</a>
                </div>    
            </div>

            <div id="changeUserParamWindow" class="modal_form modal_div">

                <div class="nameform">ИЗМЕНИТЬ</div>
                <form id="changeUserParamForm" method="post" action="<c:url value="../User/change" />">
                    <div class="boxtoinput">
                        <div class="toin">
                            <label id="rname"></label>
                            <input name="paramValue" type="text" value="">
                        </div>
                    </div>
                    <input type="hidden" name="paramName" value="">
                    <div class="form-group">
                        <button type="submit" class="btn btn-primary" style="margin-top: 10px;margin-left: 15px;">сохранить</button>
                    </div>
                </form>
            </div>
            <div id="changeUserPassword" class="modal_form modal_div">

                <div class="nameform">ИЗМЕНИТЬ ПАРОЛЬ</div>
                <form id="changeUserPassForm" method="post" action="<c:url value="../User/changePass" />">
                    <div class="boxtoinput">
                        <div class="toin">
                            <label>текущий пароль</label>
                            <input name="oldPass" type="text" value="">
                        </div>
                    </div>
                    <div class="boxtoinput">
                        <div class="toin">
                            <label>новый пароль</label>
                            <input name="newPass" type="text" value="">
                        </div>
                    </div>
                    <div class="boxtoinput">
                        <div class="toin">
                            <label>новый пароль</label>
                            <input name="checkPass" type="text" value="">
                        </div>
                    </div>
                    <div class="form-group">
                        <button type="submit" class="btn btn-primary" style="margin-top: 10px;margin-left: 15px;">изменить</button>
                    </div>
                </form>
            </div>
            <div id="modalerror" class="modal_form modal_div">
                <div class="nameform">Ошибки</div>
                <%@include file="/WEB-INF/jsp/error.jsp" %>

            </div>

        </div>
        <div id="overlay"></div>
        <script>
            $('.paramChanger').on('click', function () {
                var rname = $(this).siblings('label').text();
                var name = $(this).siblings('label').attr('id');
                var val = $(this).siblings('span').text();
                $('input[name=paramName]').val(name);
                $('input[name=paramValue]').val(val);
                $('#rname').text(rname);
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
    </body>

</html>
