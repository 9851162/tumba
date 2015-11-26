<%-- 
    Document   : menutopadmin
    Created on : 20.09.2015, 16:02:55
    Author     : bezdatiuzer
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<div class="header">
			<div class="tologo">
                            <a href="../Main/"><img id="logo" src="../img/avatar.png"></a>
                            <a href="../Main/"><img id="minilogo" src="../img/round.png"></a>
			</div>
                        <div class="toavatar">
				<div id="user_head">
                                        <div class="options" id="user_role">Администратор</div>
					<div id="user_name">${userName}</div>
					<div class="options"><a href="../Admin/administrating">управление</a></div>
                                        <div class="options" id="logout" style="position: absolute;z-index: 2;"><a href="../logout">выйти</a></div>
				</div>
				<img id="avatar" src="../img/avatar.png">
				<img id="miniavatar" src="../img/round.png">
			</div>
		</div>
