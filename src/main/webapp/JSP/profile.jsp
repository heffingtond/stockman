<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://stockman.com" prefix="myfn" %>

<html>
<head>

<style type="text/css" media="all">
@import "/stockman/css/pageFormat.css";
@import "/stockman/css/header.css";
</style>

<meta charset="UTF-8">
<title>Company Profile</title>
<link rel="icon" type="image/x-icon" href="/stockman/images/moneyman.png">
</head>
<body>
	<%@ include file="header.jsp" %>
	<div id="content">
		<div id="errors">
	       	<c:if test="${fn:length(sessionScope.UserBean.errors) > 0}">
	   			<c:forEach var="error" items="${sessionScope.UserBean.errors}">
	       			<c:out value="${error}"/><br/><br/>
	   			</c:forEach>
	   			${sessionScope.UserBean.clearErrors}
	       	</c:if>
	    </div>   	
	
		<h2 align="center">Company Profile</h2>
		<div id="inputField">
			<p align="center">
				<img class="header-logo" src="${requestScope.PROFILE.image}" alt="Company Logo">
			</p>
			<p align="center">
				<button type="button" onclick="window.close();" style="justify-content: center;">Close Window</button>
			</p>
			<br>
			<table class="center">
				<tr>
					<th>
					    Symbol:
					</th>
					<td>
						${requestScope.PROFILE.symbol}
					</td>
				</tr>
				<tr>
					<th>
					    Company Name:
					</th>
					<td>
						${requestScope.PROFILE.companyName}
					</td>
				</tr>
				<tr>
					<th>
					    Current Price:
					</th>
					<td>
						${myfn:formatToDollar(requestScope.PROFILE.price)}
					</td>
				</tr>
				<tr>
					<th>
					    Range:
					</th>
					<td>
						${requestScope.PROFILE.range}
					</td>
				</tr>
				<tr>
					<th>
					    Exchange:
					</th>
					<td>
						${requestScope.PROFILE.exchange}
					</td>
				</tr>
				<tr>
					<th>
					    Industry:
					</th>
					<td>
						${requestScope.PROFILE.industry}
					</td>
				</tr>
				<tr>
					<th>
					    Sector:
					</th>
					<td>
						${requestScope.PROFILE.sector}
					</td>
				</tr>
				<tr>
					<th>
					    City:
					</th>
					<td>
						${requestScope.PROFILE.city}
					</td>
				</tr>
				<tr>
					<th>
					    State:
					</th>
					<td>
						${requestScope.PROFILE.state}
					</td>
				</tr>
				<tr>
					<th>
					    Country:
					</th>
					<td>
						${requestScope.PROFILE.country}
					</td>
				</tr>
				<tr>
					<th>
					    Web Site:
					</th>
					<td>
						<a href="${requestScope.PROFILE.website}" target="_blank">${requestScope.PROFILE.website}</a>
					</td>
				</tr>
			</table>
		    <p align="center">
				<button type="button" onclick="window.close();" style="justify-content: center;">Close Window</button>
			</p>
	    </div>
	    <div id="description">
	    	<br>
		    <strong>
		    Description:<br><br>
		    </strong>
		    ${requestScope.PROFILE.description}
	    	<br>
	    </div>
    </div>
</body>
</html>