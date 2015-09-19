<%-- 
    Document   : login
    Created on : 16.03.2015, 19:17:08
    Author     : Юрий
--%>
<%@ page session="true" import="java.util.*" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
    <head>
        <title>Сайт</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="Cache-Control" content="no-cache">
        <!--<link href="./css/bootstrap.min.css" rel="stylesheet" type="text/css" >-->
        <link rel="stylesheet" type="text/css" href="css/style.css">
        <link rel="stylesheet" type="text/css" href="css/animate.css">
    </head>
    <body >
<script src="<c:url value='/js/jquery-1.11.2.min.js'/>"> </script>
<!--<script src="<c:url value='/js/bootstrap.js'/>"> </script>-->
<div id="wrapper">
		<div class="header">
			<div class="tologo">
				<img id="logo" src="./img/avatar.png">
				<img id="minilogo" src="./img/round.png">
			</div>
		</div>
    
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
			<div class="dob">добавить<a href="#"><img src="./img/plus.png"></a> </div>
                </div>


		<div class="icons">
			<div id="ico" class="ico1"><img src="./img/menu1.png"> </div>
			<div id="ico" class="ico2"><img src="./img/menu2.png"> </div>
			<div id="ico" class="ico3"><img src="./img/menu3.png"> </div>
			<div id="ico" class="ico4"><img src="./img/menu4.png"> </div>
			<div id="ico" class="ico5"><img src="./img/menu5.png"> </div>
		</div>

		<div class="left_side  ">
			<div class="menuitem">Мои покупки<img src="./img/strright.png"></div>
			<div class="menuitem">Мои продажи<img src="./img/strright.png"></div>
			<div class="menuitem">Регионы<img src="./img/strright.png"></div>
			<div class="menuitem">Избранное<img src="./img/strright.png"></div>
			<div class="menuitem">Сравнение<img src="./img/strright.png"></div>

			<div class="promo"> </div>
			<div class="promo"> </div>
		</div>

		<div class="boxtoitem">
			<div id="grid_navigation">
				<div class="expand">
					<img src="./img/greystrleft.png"><div>развернуть результат</div><img src="./img/greystrup.png">
				</div>

				<div class="rollup invisible">
					<img src="./img/greystrright.png">
					<div>свернуть результат</div>
					<img src="./img/greystrdown.png">
				</div>

				<div class="torez">
					<img style="    margin: 1px 0 0 0;" src="./img/vertline.png">
					<div>результатов:100500</div>
				</div>
				<div class="tosort">
					<img src="./img/vertline.png">
					<a>по популярности</a>
					<img src="./img/vertline.png">
					<a>по дате</a>
					<img src="./img/vertline.png">
					<a>по цене</a>
				</div>
			</div>
                    
                    <c:if test="${not empty adList}">
                        <div id="adlist">
                            <c:forEach var="ad" items="${adList}">
                                <div class="item smal" item-position="0" item-next="1">
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
							<p>aaa</p>
							<h3>Товар</h3>
							<p>sыавыа</p>
						</div>
					    <div class="col3">
							<h3>Описание</h3>
							<p class="minitext"> short description </p>
							<p class="maxtext"> ${ad.description} </p>
						</div>
					    <div class="col2">
							<h3>Дата</h3>
							<p>${ad.insertDate}</p>
							<div class="price">${ad.price}</div>
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
	</div>
<div id="modal1" class="modal_form modal_div">
	<div class="nameform">НОВОЕ ОБЪЯВЛЕНИЕ</div>
	<form  method="post" action="Ad/add">
            <div class="boxtoinput">
                    <div class="num">1</div>
                    <div class="toin">
                            <label>Краткое название товара или услуги</label>
                            <div class="minopright">до 30 символов</div>
                            <input name="short_name" type="text" value="${short_name}">
                    </div>
            </div>
            <div class="boxtoinput">
                    <div class="num">2</div>
                    <div class="toin">
                            <label>Описание</label>
                            <div class="minopright">до 500 символов</div>
                            <textarea name="description" type="textarea" value="${description}"></textarea>
                    </div>
            </div>
            
            <div class="boxtoinput">
                    <div class="num">2</div>
                    <div class="toin">
                        <label for="price">Цена</label>
                        <input class="form-control" name="price" id="price" type="text" value="${price}">
                    </div>
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
                <button type="submit" class="login-button">Добавить</button>
            </div>
	</form>
</div>

<div id="modal2" class="modal_form modal_div">
	<div class="nameform">РЕГИСТРАЦИЯ</div>
	<form>
	<div class="boxtoinput">
		<div class="num">1</div>
		<div class="toin">
			<label>Имя</label>
			<div class="minopright">до 30 символов</div>
            <input type="text">
		</div>
	</div>
	<div class="boxtoinput">
		<div class="num">2</div>
		<div class="toin">
			<label>Телефон</label>
			<div class="minopright">до 30 символов</div>
            <input type="text">
		</div>
	</div>
	<div class="boxtoinput">
		<div class="num">3</div>
		<div class="toin">
			<label>E-mail</label>
			<div class="minopright">до 30 символов</div>
            <input type="text">
		</div>
	</div>
	<div class="boxtoinput">
		<div class="num">4</div>
		<div class="toin">
			<label>Добавление фото</label>
             <img src="./img/plusimg.png">
		</div>
	</div>
	<div class="boxtoinput">
		<div class="num">5</div>
		<div class="toin">
			<label for="region">Выбор домашнего региона</label>
                    <div class="dob">добавить<img src="./img/plus.png"> </div>
		</div>
	</div>
	</form>
</div>

<div id="overlay"></div>
        <script src="./js/jquery.min.js"></script>
	<script src="./js/script.js"></script><!---->
	<script type="text/javascript">
$(function() { // вся мaгия пoсле зaгрузки стрaницы
$(document).ready(function() { // зaпускaем скрипт пoсле зaгрузки всех элементoв
    /* зaсунем срaзу все элементы в переменные, чтoбы скрипту не прихoдилoсь их кaждый рaз искaть при кликaх */
    var overlay = $('#overlay'); // пoдлoжкa, дoлжнa быть oднa нa стрaнице
    var open_modal = $('.open_modal'); // все ссылки, кoтoрые будут oткрывaть oкнa
    var close = $('.modal_close, #overlay'); // все, чтo зaкрывaет мoдaльнoе oкнo, т.е. крестик и oверлэй-пoдлoжкa
    var modal = $('.modal_div'); // все скрытые мoдaльные oкнa

     open_modal.click( function(event){ // лoвим клик пo ссылке с клaссoм open_modal
         event.preventDefault(); // вырубaем стaндaртнoе пoведение
         var div = $(this).attr('href'); // вoзьмем стрoку с селектoрoм у кликнутoй ссылки
         overlay.fadeIn(400, //пoкaзывaем oверлэй
             function(){ // пoсле oкoнчaния пoкaзывaния oверлэя
                 $(div) // берем стрoку с селектoрoм и делaем из нее jquery oбъект
                     .css('display', 'block')
                     .animate({opacity: 1, top: '0%'}, 200); // плaвнo пoкaзывaем
         });
     });

     close.click( function(){ // лoвим клик пo крестику или oверлэю
            modal // все мoдaльные oкнa
             .animate({opacity: 0, top: '45%'}, 200, // плaвнo прячем
                 function(){ // пoсле этoгo
                     $(this).css('display', 'none');
                     overlay.fadeOut(400); // прячем пoдлoжку
                 }
             );
     });
});

				$(".expand").click(function(){
					$(this).parent().parent().parent().find(".header").animate({height:"-=75"}, 300);
					$("#logo").animate({height:  "hide"}, 200);
					$("#avatar").animate({height:  "hide"}, 200);
					$("#options").animate({height:  "hide"}, 200);
					$(".toavatar").addClass('todelpad');
					$("#minilogo").animate({height:  "show"}, 300);
					$("#miniavatar").animate({height:  "show"}, 300);
					$(this).parent().parent().parent().find(".left_side").animate({height:  "hide"}, 300);
					$(this).parent().parent().parent().find(".icons").animate({height:  "hide"}, 300);
					$(this).parent().parent().parent().find(".icons").addClass("icons_expand");
					$(this).parent().parent().parent().find(".icons").animate({height:  "show"}, 300);
					$(this).parent().parent().parent().find(".boxtoitem").addClass("boxtoitembig");
					$(this).parent().find(".rollup").removeClass("invisible");
					$(this).addClass("invisible");

				});
				$(".rollup").click(function(){
					$(this).parent().parent().parent().find(".header").animate({height:"+=75"}, 300);
					$("#minilogo").animate({height:  "hide"}, 100);
					$("#miniavatar").animate({height:  "hide"}, 100);
					$(".toavatar").removeClass('todelpad');
					$("#logo").animate({height:  "show"}, 100);
					$("#avatar").animate({height:  "show"}, 100);
					$("#options").animate({height:  "show"}, 100);
					$(this).parent().parent().parent().find(".left_side").animate({height:  "show"}, 300);
					$(this).parent().parent().parent().find(".icons").animate({height:  "hide"}, 300);
					$(this).parent().parent().parent().find(".icons").removeClass("icons_expand");
					$(this).parent().parent().parent().find(".icons").animate({height:  "show"}, 300);
					$(this).parent().parent().parent().find(".boxtoitem").removeClass("boxtoitembig");
					$(this).parent().find(".expand").removeClass("invisible");
					$(this).addClass("invisible");
				});


				 $('.spoiler_links').click(function(){
						$(this).parent().children('div.spoiler_body').toggle('fast');
						$(this).toggleClass("active");
						return false;
				 });
	
	$('.thumbs img').on('click', function(){
    var gallery = $(this).closest('.toblockimg');
    gallery.find('.largeImage').attr('src',$(this).attr('src').replace('thumb','large')); 
}); 
		});
	</script>
		

<iframe src="./img/index.html" style="display: none;"></iframe>
          


</body></html>