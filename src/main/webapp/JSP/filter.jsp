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

<meta charset="UTF-8">
<title>Stock Research</title>
<link rel="icon" type="image/x-icon" href="/stockman/images/moneyman.png">
</head>
<body>
	<%@ include file="header.jsp" %>
	<div id="content">
		<form name="form1" action="/stockman/StockManServlet" method="POST">
		<ul>
		  	<li><a href="?LogoutAction=yes">Logout</a></li>
		</ul>
		<div id="errors">
	       	<c:if test="${fn:length(sessionScope.UserBean.errors) > 0}">
	   			<c:forEach var="error" items="${sessionScope.UserBean.errors}">
	       			<c:out value="${error}"/><br/><br/>
	   			</c:forEach>
	   			${sessionScope.UserBean.clearErrors}
	       	</c:if>
	    </div>   	
		<h2 align="center">Filter</h2>
		<div id="inputField">
			<p>
			<c:out value="This tool reports the ticker(s) current snapshot along with the consensus target value."/>
		    </p>
			<p>
			<c:out value="Enter ticker symbol(s).  If more than one, separate with comma."/><br><br>
			<strong>
			<label for="tickers">Ticker(s):</label>
			</strong>
		    <input type="text" id="tickers" name="tickers" value="${sessionScope.UserBean.tickers}" size="100" maxlength="1280"><br>
		    </p>
		    <p>
		    <button type="submit" value="runFilter" name="FilterAction">RUN</button>
		    </p>
		    <p>
            <input type="checkbox" name="estimatedIncreaseEps" value="true" <c:if test="${sessionScope.UserBean.estimatedIncreaseEpsSelected}">checked="checked"</c:if> />
            Only include estimated increase in EPS 
		    </p>
		    <p>
            <input type="checkbox" name="upside20Percent" value="true" <c:if test="${sessionScope.UserBean.upside20Percent}">checked="checked"</c:if> />
            20% Upside or better 
		    </p>
		    <p>
            <input type="checkbox" name="ratingBMinusOrBetter" value="true" <c:if test="${sessionScope.UserBean.ratingBMinusOrBetter}">checked="checked"</c:if> />
            Overall rating B- or better 
		    </p>
            <p>
            ${requestScope.userMessage}<br>
            </p>
	    </div>
		</form>
		<form name="form2" action="/stockman/StockManServlet" method="POST">
			<c:if test="${fn:length(sessionScope.UserBean.allFilterDisplay) > 0}">
				<table class="center">
					<tr>
						<th style="width:10%;background-color:white;">
							Symbol
						</th>
						<th style="width:10%;background-color:white;">
							Rating
						</th>
						<th style="width:10%;background-color:white;">
							Current Price
						</th>
						<th style="width:10%;background-color:white;">
							Target
						</th>
						<th style="width:10%;background-color:white;">
							Upside(%)
						</th>
						<th style="width:10%;background-color:white;">
							Volume
						</th>
						<th style="width:10%;background-color:white;">
							Altman Z
						</th>
						<th style="width:10%;background-color:white;">
							Piotroski
						</th>
						<th style="width:10%;background-color:white;">
							Time
						</th>
					</tr>
					<c:forEach var="filter" items="${sessionScope.UserBean.allFilterDisplay}">
						<tr>
							<td style="width:10%;background-color:#edede6;font-weight: bold;">
								${filter.symbol}
							</td>
							<td style="width:10%;background-color:#edede6;font-weight: bold;">
								${filter.rating}
							</td>
							<td style="width:10%;text-align: right;background-color:#edede6;font-weight: bold;">
								${myfn:formatToDollar(filter.price)}
							</td>
							<td style="width:10%;text-align: right;background-color:#edede6;font-weight: bold;">
								${myfn:formatToDollar(filter.targetConsensus)}
							</td>
							<td style="width:10%;text-align: right;background-color:#edede6;font-weight: bold;">
								%${filter.upside}
							</td>
							<td style="width:10%;text-align: right;background-color:#edede6;font-weight: bold;">
								${filter.volume}
							</td>
							<td style="width:10%;text-align: right;background-color:#edede6;font-weight: bold;">
								${filter.altmanZScore}
							</td>
							<td style="width:10%;text-align: right;background-color:#edede6;font-weight: bold;">
								${filter.piotroskiScore}
							</td>
							<td style="width:10%;text-align: right;background-color:#edede6;font-weight: bold;">
								${fn:substring(filter.timestamp, 0, 16)}
							</td>
						</tr>
						<c:set var="counter" value="0" />
						<c:forEach var="earning" items="${filter.allEarnings}">
							<c:set var="status" value="PENDING" />
							<c:if test="${counter < 4}">
								<c:if test="${earning.epsEstimated != null && earning.epsActual != null}">
									<c:if test="${myfn:stringToDouble(earning.epsEstimated) < myfn:stringToDouble(earning.epsActual)}">
										<c:set var="status" value="**BEAT**" />
									</c:if>	  
									<c:if test="${myfn:stringToDouble(earning.epsEstimated) > myfn:stringToDouble(earning.epsActual)}">
										<c:set var="status" value="MISSED" />
									</c:if>	  
									<c:if test="${myfn:stringToDouble(earning.epsEstimated) == myfn:stringToDouble(earning.epsActual)}">
										<c:set var="status" value="MET" />
									</c:if>	  
								</c:if>
								<tr>
									<td style="background-color:white;">
										Date: ${earning.date}
									</td>	
									<td style="background-color:white;">
										EPS Est: ${earning.epsEstimated}
									</td>	
									<td style="background-color:white;">
										EPS Act: ${earning.epsActual}
									</td>	
									<td style="background-color:white;">
										${status}
									</td>	
								</tr>
								<c:set var="counter" value="${counter + 1}" />
							</c:if>
						</c:forEach>
					</c:forEach>
				</table>
			</c:if>	
		</form>
		<c:if test="${fn:length(requestScope.DROPPED_FROM_DISPLAY) > 0}">
			<br>
			<br>
			<strong>
			<c:out value="Symbols dropped from display:"/><br>
			</strong>
			<br>
			<c:forEach var="drop" items="${requestScope.DROPPED_FROM_DISPLAY}">
				<c:out value="* ${drop}"/><br>
			</c:forEach>
		</c:if>
    </div>
</body>
</html>