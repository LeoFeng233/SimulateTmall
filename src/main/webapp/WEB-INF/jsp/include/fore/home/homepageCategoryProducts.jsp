<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:if test="${empty param.categorycount}">
    <c:set var="categorycount" scope="page" value="100"/>
</c:if>

<c:if test="${!empty param.categorycount}">
    <c:set var="categorycount" scope="page" value="${param.categorycount}"/>
</c:if>

<div class="homepageCategoryProducts">
    <c:forEach items="${categories}" var="category" varStatus="stc">
        <c:if test="${stc.count<=categorycount}">
            <div class="eachHomepageCategoryProducts">
                <div class="left-mark"></div>
                <span class="categoryTitle">${category.name}</span>
                <br>
                <c:forEach items="${category.products}" var="product" varStatus="st">
                    <c:if test="${st.count<=5}">
                        <div class="productItem">
                            <a href="foreproduct?pid=${product.id}"><img width="100px"
                                                                         src="resources/img/productSingle_middle/${product.firstProductImage.id}.jpg"></a>
                            <a class="productItemDescLink" href="foreproduct?pid=${product.id}">
								<span class="productItemDesc">[热销]
								${fn:substring(product.name, 0, 20)}
								</span>
                            </a>
                            <span class="productPrice">
								<fmt:formatNumber type="number" value="${product.promotePrice}" minFractionDigits="2"/>
							</span>
                        </div>
                    </c:if>
                </c:forEach>
                <div style="clear:both"></div>
            </div>
        </c:if>
    </c:forEach>


    <img id="endpng" class="endpng" src="resources/img/site/end.png">

</div>