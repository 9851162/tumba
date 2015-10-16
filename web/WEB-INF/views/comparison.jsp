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
                        <div>категория</div>
                        <div>категория</div>
                        <div>категория</div>
                    </div>
                </div>
                <div class="dob">добавить<a href="#"><img src="../img/plus.png"></a> </div>
            </div>

            <c:if test="${role=='user'||role=='admin'}">
                <c:set var="choosePossible" value="choose"/>
                <div class="icons icons_expand">
                    <a href="<c:url value="../Main/purchases" />"><div id="ico" class="ico1"><img src="../img/menu1.png"> </div></a>
                    <a href="<c:url value="../Main/sales" />"><div id="ico" class="ico2"><img src="../img/menu2.png"> </div></a>
                    <a href="<c:url value="../Main/" />"><div id="ico" class="ico3"><img src="../img/menu3.png"> </div></a>
                    <a href="<c:url value="../Main/chosen" />"><div id="ico" class="ico4"><img src="../img/menu4.png"> </div></a>
                    <a href="<c:url value="../Main/comparison" />"><div id="ico" class="ico5"><img src="../img/menu5.png"> </div></a>
                </div>
            </c:if>
                            <c:if test="${empty compList}">
                                <div class="boxtoitem boxtoitembig whitepod" style="padding-left: 20px;">Ooops... Нечего сравнивать, добавьте объявления для сравнения.</div>
                            </c:if>   
                            
                            <c:if test="${!empty compList}">
        <div class="boxtoitem boxtoitembig whitepod">
			<div style="    width: 168px;;    float: left;">
				<div class="oneatr firstatr">
						Показать<br>отличия
				</div>
				<div class="oneatr">
						Атрибут 1
					</div>
				<div class="oneatr">
						Атрибут 2
				</div>			
			</div>
			<div class="tabsrav">
				<div class="onestr">
 
					<div class="prewtov">
						<img class="sravimg" src="img/tov/tov16.jpg">
						<div class="sravopisanie">
							<div class="sravmin">
								<h3>Продавец</h3>
								<p>dfsfd sdfd</p>
								<h3>Товар</h3>
								<p>sыавыа</p>
							</div>
							<div class="sravprice">
								<div class="price">38,67 ₽</div>
							</div>
						</div>
					</div>
					<div class="prewtov">
						<img class="sravimg" src="img/tov/tov16.jpg">
						<div class="sravopisanie">
							<div class="sravmin">
								<h3>Продавец</h3>
								<p>dfsfd sdfd</p>
								<h3>Товар</h3>
								<p>sыавыа</p>
							</div>
							<div class="sravprice">
								<div class="price">38,67 ₽</div>
							</div>
						</div>
					</div>
					<div class="prewtov">
						<img class="sravimg" src="img/tov/tov16.jpg">
						<div class="sravopisanie">
							<div class="sravmin">
								<h3>Продавец</h3>
								<p>dfsfd sdfd</p>
								<h3>Товар</h3>
								<p>sыавыа</p>
							</div>
							<div class="sravprice">
								<div class="price">38,67 ₽</div>
							</div>
						</div>
					</div>
					<div class="prewtov">
						<img class="sravimg" src="img/tov/tov16.jpg">
						<div class="sravopisanie">
							<div class="sravmin">
								<h3>Продавец</h3>
								<p>dfsfd sdfd</p>
								<h3>Товар</h3>
								<p>sыавыа</p>
							</div>
							<div class="sravprice">
								<div class="price">38,67 ₽</div>
							</div>
						</div>
					</div>
					<div class="prewtov">
						<img class="sravimg" src="img/tov/tov16.jpg">
						<div class="sravopisanie">
							<div class="sravmin">
								<h3>Продавец</h3>
								<p>dfsfd sdfd</p>
								<h3>Товар</h3>
								<p>sыавыа</p>
							</div>
							<div class="sravprice">
								<div class="price">38,67 ₽</div>
							</div>
						</div>
					</div> 
					</div>	 
				<div class="onestr">
	
					<div>фывафывафывафвафва
					</div>
					<div>фывафывафывафвафва
					</div>
					<div>фывафывафывафвафва
					</div>
					<div>фывафывафывафвафва
					</div>
					<div>фывафывафывафвафва
					</div>
				</div>				
				<div class="onestr">
	
					<div>фывафывафывафвафва
					</div>
					<div>фывафывафывафвафва
					</div>
					<div>фывафывафывафвафва
					</div>
					<div>фывафывафывафвафва
					</div>
					<div>фывафывафывафвафва
					</div>
				</div>				
			
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

    <div id="modalerror" class="modal_form modal_div">
        <div class="nameform">Ошибки</div>
        <%@include file="/WEB-INF/jsp/error.jsp" %>

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