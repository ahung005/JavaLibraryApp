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
	<title>Book Detail</title>
	<!-- for Bootstrap CSS -->
	<link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.min.css" />
	<!-- YOUR own local CSS -->
	<link rel="stylesheet" href="/css/style.css"/>
	<!-- For any Bootstrap that uses JS -->
	<script src="/webjars/bootstrap/js/bootstrap.min.js"></script>
</head>
<body>
	<div class="container">
		<h1><c:out value="${book.title}" /></h1>
		<h6><a href="/home">dashboard</a></h6>
		<div class="my-3">
			<div class="d-flex gap-3">
				<img alt="Cover Image" src="data:image/jpeg;base64,${book.base64imageFile}" width="200px" class="img-thumbnail" />
				<table class="table table-borderless">
					<tr>
						<td><h3>Title: </h3></td>
						<td class="float-left w-75"><h4><c:out value="${book.title}" /></h4></td>
					</tr>
					<tr>
						<td><h3>Author: </h3></td>
						<td class="float-left"><h4><c:out value="${book.author}" /></h4></td>
					</tr>
					<tr>
						<td><h3>Description: </h3></td>
						<td class="float-left"><h4><c:out value="${book.description}" /></h4></td>
					</tr>
					<tr>
						<td>
							<c:if test="${isLiked==true}">
								<a href="/book/${book.id}/unlike" class="btn btn-danger">unlike</a>
							</c:if>
							<c:if test="${isLiked==false}">
								<a href="/book/${book.id}/like" class="btn btn-primary">Like</a>
							</c:if>
						</td>
						<td class="float-left">
							<h5><c:out value="${likes}" /> like(s)</h5>
						</td>
					</tr>
				</table>
			</div>
		</div>
		<hr>
		<div class="my-3" >
			<form:form action="/book/${book.id}/addComment" modelAttribute="newComment" method="POST" class="form">
				<h3 class="text-primary">Add Comments</h3>
				<table class="table table-borderless table-primary">
					<tr>
						<td>
							<div><form:label for="writing" path="writing">Comment:</form:label></div>
							<div><form:errors path="writing" class="text-danger"/></div>
						</td>
						<td>
							<form:input path="writing" class="form-control"/>
						</td>
						<td>
							<input type="submit" value="Submit" class="btn btn-primary"/>
						</td>
					</tr>
					<form:input type="hidden" path="reader" value="${user.id}" />
					<form:input type="hidden" path="book" value="${book.id}" />
				</table>			
			</form:form>
		</div>
		<div class="my-3">
			<ul>
				<c:forEach var="singleComment" items="${comments}">
					<li>
						<strong><c:out value="${singleComment.reader.firstName}" /></strong> at <c:out value="${singleComment.createdAt}" />:
						<p>
							<c:out value="${singleComment.writing}" />
							<c:if test="${singleComment.reader.id==user.id}">
								 <a href="/book/${book.id}/delete/${singleComment.id}">delete</a>
							</c:if>
						</p>
					</li>
				</c:forEach>
			</ul>
		</div>
	</div>
</body>
</html>