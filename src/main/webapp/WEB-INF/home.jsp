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
	<title>Home</title>
	<!-- for Bootstrap CSS -->
	<link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.min.css" />
	<!-- YOUR own local CSS -->
	<link rel="stylesheet" href="/css/style.css"/>
	<!-- For any Bootstrap that uses JS -->
	<script src="/webjars/bootstrap/js/bootstrap.min.js"></script>
</head>
<body>	
	<div class="container">
		<div class="d-flex justify-content-between align-items-center">
			<h1>Library</h1>
			<form id="logoutForm" method="POST" action="/logout">
			    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
			    <input type="submit" value="Logout!" class="btn btn-danger" />
			</form>
		</div>
		<h3>Welcome, <c:out value="${user.firstName}" /> <c:out value="${user.lastName}" /></h3>
		<div class="d-flex my-2">
			<div class="w-50 border p-2">
				<h3 class="text-center">Available Books</h3>
				<table class="table table-borderless table-wrapper2">
					<c:forEach var="unborrowedBook" items="${books}">
						<c:if test="${unborrowedBook.borrower.id==null}">
							<tr>
								<td class="w-25"><img alt="No Cover Image" src="data:image/jpeg;base64,${unborrowedBook.base64imageFile}" class="img-thumbnail" /></td>
								<td class="w-50">
									<h4><a href="/book/${unborrowedBook.id}"><c:out value="${unborrowedBook.title}" /></a></h4>
									<h5>Author: <c:out value="${unborrowedBook.author}" /></h5>
								</td>
								<td class="w-25"><a href="/book/borrow/${unborrowedBook.id}" class="btn btn-secondary">Borrow</a></td>
							
							</tr>
						</c:if>
					</c:forEach>
				</table>
			</div>
			<div class="w-50 border p-2">
				<h3 class="text-center">Borrowed Books</h3>
				<div>
					<c:forEach var="borrowedBook" items="${books}">
						<c:if test="${borrowedBook.borrower.id==user.id}">
							<table class="table table-borderless table-wrapper2">
								<tr>
									<td class="w-25"><img alt="No Cover Image" src="data:image/jpeg;base64,${borrowedBook.base64imageFile}" width="100px" class="img-thumbnail" /></td>
									<td class="w-50">
										<h4><a href="/book/${borrowedBook.id}"><c:out value="${borrowedBook.title}" /></a></h4>
										<h5>Author: <c:out value="${borrowedBook.author}" /></h5>
									</td>
									<td class="w-25"><a href="/book/return/${borrowedBook.id}" class="btn btn-warning">Return</a></td>
								</tr>
							</table>	
						</c:if>
					</c:forEach>
				</div>
			</div>
		</div>
		
		<a href="/book/new" class="btn btn-secondary w-100">Add a Book</a>
	</div>
</body>
</html>