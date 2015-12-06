<%-- 
    Document   : menu
    Created on : 30.03.2015, 16:42:28
    Author     : Юрий
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:if test="${empty role}">
    <%@include file="/WEB-INF/jsp/menutopanon.jsp" %>
</c:if>

<c:if test="${role=='user'}">
    <%@include file="/WEB-INF/jsp/menutopuser.jsp" %>
</c:if>

<c:if test="${role=='admin'}">
    <%@include file="/WEB-INF/jsp/menutopadmin.jsp" %>
</c:if>

<script src="<c:url value='/js/jquery-1.11.2.min.js'/>"></script>
<script src="<c:url value='/js/jquery-ui.min.js'/>"></script>
<script src="<c:url value='/js/datepicker-ru.js'/>"></script>
<script src="../js/seller_scripts/script.js"></script>
<script src="../js/seller_scripts/ajaxscript.js"></script>
<script src="../js/seller_scripts/magic.js"></script>



