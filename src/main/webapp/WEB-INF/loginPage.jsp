<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- c:out ; c:forEach etc. --> 
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!-- Formatting (dates) --> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"  %>
<!-- form:form -->
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!-- for rendering errors on PUT routes -->
<%@ page isErrorPage="true" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
	<title>Registration and Login</title>
	<!-- for Bootstrap CSS -->
	<link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.min.css" />
	<!-- YOUR own local CSS -->
	<link rel="stylesheet" href="/css/style.css"/>
	<!-- For any Bootstrap that uses JS -->
	<script src="/webjars/bootstrap/js/bootstrap.min.js"></script>
</head>
<body>
	<div class="container">    
		<h1>Library</h1>
	    <c:if test="${logoutMessage != null}">
	    	<p class="text-primary"><c:out value="${logoutMessage}"></c:out></p>
		</c:if>
		<div class="d-flex justify-content-evenly">
			<form:form action="/register" method="post" modelAttribute="user">
				<table class="table table-borderless">
					<thead>
				    	<tr>
				            <td colspan="2" class="text-center"><strong>Register</strong></td>
				        </tr>
				    </thead>
				    <thead>
				    	<tr>
				            <td class="float-left">First Name:</td>
				            <td class="float-left">
				            	<form:errors path="firstName" class="text-danger"/>
								<form:input class="input" path="firstName"/>
				            </td>
				        </tr>
				        <tr>
				            <td class="float-left">Last Name:</td>
				            <td class="float-left">
				            	<form:errors path="lastName" class="text-danger"/>
								<form:input class="input" path="lastName"/>
				            </td>
				        </tr>
				        <tr>
				            <td class="float-left">Email:</td>
				            <td class="float-left">
				            	<form:errors path="email" class="text-danger"/>
								<form:input class="input" path="email"/>
				            </td>
				        </tr>
				        <tr>
				            <td class="float-left">Password:</td>
				            <td class="float-left">
				            	<form:errors path="password" class="text-danger"/>
								<form:input class="input" path="password"/>
				            </td>
				        </tr>
				        <tr>
				            <td class="float-left">Confirm PW:</td>
				            <td class="float-left">
				            	<form:errors path="confirm" class="text-danger"/>
								<form:input class="input" path="confirm"/>
				            </td>
				        </tr>
				        <tr>
				        	<td colspan=2><input class="input" class="button" type="submit" value="Submit"/></td>
				        </tr>
				    </thead>
				</table>
			</form:form>
			<form:form action="/login" method="post" modelAttribute="user">
				<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
				<table class="table table-borderless">
					<thead>
				    	<tr>
				            <td colspan="2" class="text-center"><strong>Login</strong></td>
				        </tr>
				    <thead>
				        <tr>
				            <td class="float-left">Email:</td>
				            <td class="float-left">
								<form:input class="input" path="email"/>
				            </td>
				        </tr>
				        <tr>
				            <td class="float-left">Password:</td>
				            <td class="float-left">
								<form:input class="input" path="password"/>
				            </td>
				        </tr>
				        <tr>
				        	<td colspan=2><input class="input" class="button" type="submit" value="Submit"/></td>
				        </tr>
				    </thead>
				</table>
			</form:form>
		</div>
		<hr>
		<c:if test="${errorMessage != null}">
		    <p class="text-danger"><c:out value="${errorMessage}"></c:out></p>
		</c:if>
	</div>	
</body>
</html>