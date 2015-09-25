<%-- 
    Document   : admin
    Created on : 22.09.2015, 12:04:14
    Author     : bezdatiuzer
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="myTags" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Управление</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="Cache-Control" content="no-cache">
        <link rel="stylesheet" type="text/css" href="../css/style.css">
        <link rel="stylesheet" type="text/css" href="../css/animate.css">
    </head>
    <body style="overflow: scroll;">
        <div id="wrapper">
            <script src="<c:url value='/js/jquery-1.11.2.min.js'/>"> </script>
            <script src="../js/magic.js">

	</script>
            <c:if test="${role=='admin'}">
                <%@include file="/WEB-INF/jsp/menutopadmin.jsp" %>
            </c:if>
            <h1>Управление</h1>
            
            <div id="categoryPlace" style="width: 45%;float: left;">
                <b>Категории: </b>
                <myTags:category id="0" map="${catMap}"/>
            </div>
            <div id="categoryPlace" style="width: 45%;float: right;">
                <b>Параметры:  </b>
                Категория: ${catName};
            </div>
        </div>
            
            <div id="modal" class="modal_form modal_div">
                <div class="nameform">Добавить категорию в каталог</div>
                <form  method="post" action="../Admin/addCat" >
                    <div class="boxtoinput">
                            <div class="toin">
                                <label>Наименование</label>
                                <input name="name" type="text">
                            </div>
                    </div>
                    <!--to do jquery podstanovku pid-->
                    <input name="parentId" type="hidden" value="0">
                    <div class="form-group">
                        <button type="submit" class="btn">Создать</button>
                    </div>
                </form>
            </div>
            <div id="overlay"></div>
            <script>$('.add_cat').click(function(){var cid = $(this).attr('data-id');$('[name = parentId]').val(cid);});</script>
    </body>
    
</html>
