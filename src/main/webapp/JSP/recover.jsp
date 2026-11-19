<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
<head>
<meta charset="UTF-8">

<style type="text/css" media="all">
@import "/stockman/css/navbar.css";
@import "/stockman/css/pageFormat.css";
@import "/stockman/css/header.css";
</style>

<title>Credentials Recovery</title>
<link rel="icon" type="image/x-icon" href="/stockman/images/moneyman.png">
</head>
<body>

	<%@ include file="header.jsp" %>

	<div id="content">
		<div id="inputField">
		    <div style="text-align: center;">
			    <c:if test="${requestScope.RECOVERY_TYPE == 'username'}" >
		        	<h1>Recover Username</h1>
		        </c:if>	
				<div id="errors">
			       	<c:if test="${fn:length(sessionScope.UserBean.errors) > 0}">
		     			<c:forEach var="error" items="${sessionScope.UserBean.errors}">
		         			<c:out value="${error}"/><br/><br/>
		     			</c:forEach>
	   					${sessionScope.UserBean.clearErrors}
			       	</c:if>
			    </div>
		        <form action="/stockman/StockManServlet" method="post">
				    <c:if test="${requestScope.RECOVERY_TYPE == 'username'}" >
				    	<c:out value="If the email address enter here exists for StockMan users, the username associated will be emailed to you."/>
				    	<br />
				    	<br />
			            <label for="email">Enter the email address you provided for your StockMan account:</label>
			            <input type="text" id="email" name="email" size="60" maxlength="128"><br><br>
			            <input type="submit" value="Send Username" name="LoginAction">
				    </c:if>
				    <c:if test="${requestScope.RECOVERY_TYPE == 'response'}" >
				    	<br />
			            <input type="submit" value="Return to Login" name="LoginAction">
				    </c:if>
		            <p>
		            ${requestScope.userMessage}
		            </p>
		        </form>
		    </div>
	    </div>
	</div>    	    
</body>
</html>