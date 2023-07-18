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
	<title>Admin Dashboard</title>
	<!-- for Bootstrap CSS -->
	<link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.min.css" />
	<!-- YOUR own local CSS -->
	<link rel="stylesheet" href="/css/style.css"/>
	<!-- For any Bootstrap that uses JS -->
	<script src="/webjars/bootstrap/js/bootstrap.min.js"></script>
</head>
<body>
	<div class="container">
		<div class="d-flex justify-content-between my-1">
			<h1>Admin: ${currentUser.firstName} ${currentUser.lastName}</h1>
			<form id="logoutForm" method="POST" action="/logout">
			    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
			    <input type="submit" value="Logout!" class="btn btn-danger"/>
			</form>
		</div>
		<div class="d-flex justify-content-evenly my-2">
			<div class="w-50 border p-2">
				<h3 class="text-center">All Users</h3>
					<table class="table table-border table-wrapper2">
				    	<c:forEach var="user" items="${users}">
				    	<c:if test = "${!user.roles.get(0).name.contains('ROLE_SUPER_ADMIN')}">
					        <tr>
								<td>${user.firstName} ${user.lastName}</td>
								<td>${user.email}</td>
								<c:if test = "${currentUser.roles.get(0).name.contains('ROLE_SUPER_ADMIN')}">
							        <c:if test = "${user.roles.get(0).name.contains('ROLE_USER')}">
							        	<td><p><a href="/delete/${user.id}">Delete</a></p> <p><a href="/admin/${user.id}">Make Admin</a></p></td>
							    	</c:if>
							    	<c:if test = "${user.roles.get(0).name.contains('ROLE_ADMIN')}">
							        	<td>
							        		<p><a href="/delete/${user.id}">Delete</a></p>
							        		<p><a href="/user/${user.id}">Make User</a></p>
							        	</td>
							    	</c:if>
							    </c:if>
							    <c:if test = "${currentUser.roles.get(0).name.contains('ROLE_ADMIN')}">        
							        <c:if test = "${user.roles.get(0).name.contains('ROLE_USER')}">
							        	<td>
							        		<a href="/admin/${user.id}">Make Admin</a>
							        	</td>
							    	</c:if>
							    	<c:if test = "${user.roles.get(0).name.contains('ROLE_ADMIN')}">
							        	<td>Admin</td>
							    	</c:if>
							    </c:if>
							</tr>
					    </c:if>	
					</c:forEach>
			    </table>
			</div>
			<div class="w-50 border p-2">
				<h3 class="text-center">All Books</h3>
					<table class="table table-border table-wrapper2">
				    	<c:forEach var="book" items="${books}">
				    		<tr>
				    			<td>
				    				<img alt="Cover Image" src="data:image/jpeg;base64,${book.base64imageFile}" width="50px" class="img-thumbnail" />
				    			</td>
				    			<td>
				    				<p><strong><c:out value="${book.title}" /></strong> by <c:out value="${book.author}" /></p>	
				    			</td>
				    			<td class="justify-content-evenly">
				    				<p><a href="/book/${book.id}/edit">Edit</a></p>
				    				<p><a href="/book/${book.id}/delete">Delete</a></p>
				    			</td>
				    		</tr>
				    	</c:forEach>
				</table>
			</div>
		</div>
		<a href="/book/new" class="btn btn-secondary w-100">Add a Book</a>
	</div>
</body>
</html>