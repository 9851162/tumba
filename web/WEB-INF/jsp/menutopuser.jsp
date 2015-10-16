<%-- 
    Document   : menutopuser
    Created on : 20.09.2015, 16:03:06
    Author     : bezdatiuzer
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<div class="header">
			<div class="tologo">
				<img id="logo" src="../img/avatar.png">
				<img id="minilogo" src="../img/round.png">
			</div>
                        <div class="toavatar">
				<div id="user_head">
                                        <div id="user_role">Пользователь</div>
					<div id="user_name">${userName}</div>
					<!--<div id="options"><a href="#" class="open_modal">настройки</a></div>-->
                                        <div id="logout"><a href="../logout">выйти</a></div>
				</div>
				<img id="avatar" src="../img/avatar.png">
				<img id="miniavatar" src="../img/round.png">
			</div>
		</div>
