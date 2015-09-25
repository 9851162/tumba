<%-- 
    Document   : category
    Created on : 24.09.2015, 16:46:09
    Author     : bezdatiuzer
--%>

<%@ attribute name="id" type="Long" required="true" %>
<%@ attribute name="map" type="java.util.Map" required="true" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="myTags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        
            <ul type="disc">
                <li><a href="#modal" data-id="${id}" class="open_modal add_cat" style="text-decoration: none;">+</a></li>
            <c:forEach var="cat" items="${map.get(id)}">
            <li>${cat.name} <a href="../Admin/deleteCat?catId=${cat.id}" style="text-decoration: none;">x</a></li>
                <myTags:category id="${cat.id}" map="${map}"/>
            </c:forEach>
            </ul>
        


