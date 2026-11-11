<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://stockman.com" prefix="myfn" %>

<html>
<head>

<style type="text/css" media="all">
@import "/stockman/css/navbar.css";
@import "/stockman/css/pageFormat.css";
@import "/stockman/css/header.css";
</style>

<style>
    /* Hide the honeypot field from real users */
    .bot-field {
        display: none !important;
        visibility: hidden;
    }
</style>

<meta charset="UTF-8">
<link rel="icon" type="image/x-icon" href="/stockman/images/moneyman.png">
<title>Stock Man Customer Profile</title>
</head>
<body>
	<%@ include file="header.jsp" %>
	<div id="content">
		<form name="form1" action="/stockman/StockManServlet" method="POST">
		<ul>
		  	<li><a href="?LogoutAction=yes">Logout</a></li>
		</ul>
		<div id="errors">
	       	<c:if test="${fn:length(sessionScope.NEW_CUSTOMER_ERRORS) > 0}">
	   			<c:forEach var="error" items="${sessionScope.NEW_CUSTOMER_ERRORS}">
	       			<c:out value="${error}"/><br/><br/>
	   			</c:forEach>
	       	</c:if>
	    </div>   	
		<h2 align="center">Authentication Profile</h2>
		<div id="inputField">
			<p>
			Stock Man User Profile
	    	</p>
		    <p>
			<label for="userId">User ID:</label>
		    <input type="text" id="userId" name="userId" value="${sessionScope.NEW_CUSTOMER_PROFILE.userId}" size="30" maxlength="128"><br>
		    </p>
		    <p>
			<label for="textPassword">Password:</label>
		    <input type="text" id="textPassword" name="textPassword" value="${sessionScope.NEW_CUSTOMER_PROFILE.textPassword}" size="30" maxlength="128"><br>
		    </p>
		    <p>
			<label for="firstName">First Name:</label>
		    <input type="text" id="firstName" name="firstName" value="${sessionScope.NEW_CUSTOMER_PROFILE.firstName}" size="50" maxlength="128"><br>
		    </p>
		    <p>
			<label for="lastName">Last Name:</label>
		    <input type="text" id="lastName" name="lastName" value="${sessionScope.NEW_CUSTOMER_PROFILE.lastName}" size="50" maxlength="128"><br>
		    </p>
		    <p>
			<label for="email">Email:</label>
		    <input type="text" id="email" name="email" value="${sessionScope.NEW_CUSTOMER_PROFILE.email}" size="50" maxlength="128"><br>
		    </p>
		    <p>
			<label for="mobilePhone">Mobile Phone:</label>
		    <input type="text" id="mobilePhone" name="mobilePhone" value="${sessionScope.NEW_CUSTOMER_PROFILE.mobilePhone}" size="12" maxlength="12"><br>
		    </p>
		    <p>
			<label for="officePhone">Office Phone:</label>
		    <input type="text" id="officePhone" name="officePhone" value="${sessionScope.NEW_CUSTOMER_PROFILE.officePhone}" size="12" maxlength="12"><br>
		    </p>
		    <p>
			<label for="officePhoneExt">Office Phone Extension:</label>
		    <input type="text" id="officePhoneExt" name="officePhoneExt" value="${sessionScope.NEW_CUSTOMER_PROFILE.officePhoneExt}" size="10" maxlength="10"><br>
		    </p>
		    <p>
			<label for="homePhone">Home Phone:</label>
		    <input type="text" id="homePhone" name="homePhone" value="${sessionScope.NEW_CUSTOMER_PROFILE.homePhone}" size="12" maxlength="12"><br>
		    </p>
<!--
		    <p>
			<label for="verificationCodeMethod">Verification Code Method:</label>
			<select name="verificationCodeMethod" id="verificationCodeMethod">
				<option value="SELECT">SELECT</option>
				<c:forEach var="method" items="${sessionScope.VERIFICATION_METHODS}">
					<c:if test="${sessionScope.NEW_CUSTOMER_PROFILE.verificationCodeMethod == method}">
						<option selected value="${method}">${method}</option>
					</c:if>
					<c:if test="${sessionScope.NEW_CUSTOMER_PROFILE.verificationCodeMethod != method}">
						<option value="${method}">${method}</option>
					</c:if>
				</c:forEach>
			</select>
		    </p>
-->

	        <!-- HONEYPOT FIELD -->
	        <div class="bot-field">
	            <label for="website_confirm">Leave this field blank:</label>
	            <input type="text" id="website_confirm" name="website_confirm" value="">
	        </div>
        		    
		    <p>
		    <button type="submit" value="addCustomerProfile" name="CustomerProfileAction">Add Profile</button>
		    <input type="hidden" name="newCustomerKey" value="${sessionScope.NEW_CUSTOMER_KEY}">
		    </p>
	    </div>
		</form>
		
    </div>
	
</body>
</html>