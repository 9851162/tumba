<%-- 
    Document   : category
    Created on : 24.09.2015, 16:46:09
    Author     : bezdatiuzer
--%>

<%@ attribute name="id" type="Long" required="true" %>
<%@ attribute name="map" type="java.util.Map" required="true" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="myTags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <c:if test="${!empty map.get(id)}">
            <ul>
            <c:forEach var="catId" items="${map.get(id)}">
                <li>${catId}<a href="#modal" class="open_modal">+</a></li>
                <myTags:category id="${catId}" map="${map}"/>
            </c:forEach>
            </ul>
        </c:if>


