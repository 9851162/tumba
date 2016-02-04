<%-- 
    Document   : regions4Users
    Created on : 31.01.2016, 2:32:54
    Author     : bezdatiuzer
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="boxtoitem">
    <!--<div style="background-color: #FFFFFF; padding-left: 10px;">-->
    <c:if test="${!empty role}">
        <div style="width: 740px;margin-left:10px; background-color: #FFFFFF;float: left;">

            <c:if test="${empty availableRegions}">
                <div style="padding-left: 10px;padding-top: 10px;">
                    Пока нет созданных регионов
                </div>
            </c:if>
            <table style="padding-top: 10px;float:left; padding-left: 10px;padding-top: 10px;">
                <tr><td><a href="#modal7" class="open_modal btn btn-primary" style="width: 150px;"><i class="fa fa-plus"></i></a></td></tr>
                            <c:if test="${!empty availableRegions}">

                    <c:forEach var="region" items="${availableRegions}">
                        <c:set var="regClass" value="btn-default"/>
                        <c:set var="homeClass" value="btn-default"/>
                        <c:if test="${!empty regionForShow && regionForShow.id==region.id}">
                            <c:set var="regClass" value="btn-primary"/>
                        </c:if>
                        <c:if test="${!empty homeSet && homeSet==region.id}">
                            <c:set var="homeClass" value="btn-primary"/>
                        </c:if>
                        <tr><td><a title="${region.name}" style="width: 150px;" class="btn ${regClass}" href="<c:url value="../Main/?action=regions&regionForShowId=${region.id}&wish=${wish}" />">${region.getShortName()}</a></td>

                            <td><a title="сделать домашним" class="btn ${homeClass}" href="<c:url value="../Main/setHomeRegion?regionId=${region.id}&regionForShowId=${regionForShow.id}&wish=${wish}" />"><i class="fa fa-home"></i></a></td>
                            <!--<td><a title="изменить" class="open_modal btn btn-default regionChanger" data-region-id="${region.id}" href="#modal8"><i class="fa fa-pencil"></i></a></td>-->
                            <td><a title="удалить" class="btn btn-default" href="<c:url value="../Main/deleteRegion?regionId=${region.id}&regionForShowId=${regionForShow.id}&wish=${wish}" />"><i class="fa fa-remove"></i></a></td></tr>
                                </c:forEach>

                </c:if>
            </table>

            <c:if test="${!empty regionForShow}">


                <ul style="float:left;margin-top:0;padding-top: 10px;">
                    <form id="changingRegion" method="post" action="<c:url value="../Main/changeRegionStructure" />">
                        <li style="list-style-type:none;margin-left: 0;padding-left: 0;"><input style="cursor: pointer;" name="all" data-method="changeRegion" class="allRegionsSelector" type="checkbox" value="1"><label class="allRegionsOpener" data-method="changeRegion" style="cursor: pointer;">Все</label></li>
                            <c:forEach var="state" items="${states}">
                                <c:set var="stateInReg" value=""/>
                                <c:set var="checkedLocksInReg" value="0"/>
                                <c:if test="${!empty statesInReg4ShowMap.get(state.id)}">
                                    <c:set var="stateInReg" value="checked"/>
                                    <c:set var="checkedLocksInReg" value="${statesInReg4ShowMap.get(state.id)}"/>
                                </c:if>
                            <li style="list-style-type:none;margin-left: 0;padding-left: 0;"><input  style="cursor: pointer;" id="${state.id}" class="stateSelector" data-method="changeRegion" name="stateIds" type="checkbox" ${stateInReg} value="${state.id}"><label id="${state.id}" class="opener" data-method="changeRegion" style="cursor: pointer;">${state.name} (<span class="checkedLocsCount" data-state-id="${state.id}" data-method="changeRegion">${checkedLocksInReg}</span>/<span data-method="changeRegion" data-state-id="${state.id}" class="locsAmount">${state.getLocalities().size()}</span>)</label></li>
                                <c:if test="${!empty state.localities}">
                                <ul>
                                    <c:forEach var="loc" items="${state.localities}">
                                        <c:set var="locInReg" value=""/>
                                        <c:if test="${!empty locsInReg4ShowMap.get(loc.id)}">
                                            <c:set var="locInReg" value="checked"/>
                                        </c:if>
                                        <li style="list-style-type:none;margin-left: 0;padding-left: 0;"><label style="cursor: pointer;" class="locLabel" data-method="changeRegion" data-state-id="${state.id}"><input  style="width: initial;cursor: pointer;" name="localIds" id="${loc.id}" class="locSelector" data-method="changeRegion" data-state-id="${state.id}" type="checkbox" ${locInReg} value="${loc.id}">${loc.name}</label></li>
                                            </c:forEach>
                                </ul>
                            </c:if>
                        </c:forEach>
                        <input type="hidden" name="wish" value="${wish}">
                        <input type="hidden" name="regionId" value="${regionForShow.id}">
                        <button type="submit" data-method="changeRegion" id="regionChanger" class="btn btn-primary" disabled="disabled" style="margin-top:10px;margin-bottom:10px;">Сохранить</button>
                    </form>
                </ul>


            </c:if>
        </div>
    </c:if>
    <!--</div>-->
    <script>
        $('input[type=checkbox][data-method=changeRegion]').on('change',function(){
            $('button[type=submit][data-method=changeRegion]').prop('disabled','');
        });
    </script>
</div>

