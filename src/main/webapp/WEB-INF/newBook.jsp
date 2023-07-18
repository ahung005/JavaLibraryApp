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
	<title>New Book</title>
	<!-- for Bootstrap CSS -->
	<link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.min.css" />
	<!-- YOUR own local CSS -->
	<link rel="stylesheet" href="/css/style.css"/>
	<!-- For any Bootstrap that uses JS -->
	<script src="/webjars/bootstrap/js/bootstrap.min.js"></script>
</head>
<body>
	<div class="container">
		<h1>New Book</h1>
		<h6><a href="/home">dashboard</a></h6>
		<form:form action="/book" method="POST" modelAttribute="book" class="form" enctype="multipart/form-data">
			<table class="table table-striped table-bordered">
				<tr>
					<td>
						<div><form:label for="imageFile" path="imageFile">Cover:</form:label></div>
						<div><form:errors path="imageFile" class="text-danger"/></div>
					</td>
					<td>
						<form:input type="file" path="imageFile" class="form-control"/>
					</td>
				</tr>
				<tr>
					<td>
						<div><form:label for="title" path="title">Title:</form:label></div>
						<div><form:errors path="title" class="text-danger"/></div>
					</td>
					<td>
						<form:input type="text" path="title" class="form-control"/>
					</td>
				</tr>
				<tr>
					<td>
						<div><form:label for="author" path="author">Author:</form:label></div>
						<div><form:errors path="author" class="text-danger"/></div>
					</td>
					<td>
						<form:input type="text" path="author" class="form-control"/>
					</td>
				</tr>
				<tr>
					<td>
						<div><form:label for="description" path="description">Description:</form:label></div>
						<div><form:errors path="description" class="text-danger"/></div>
					</td>
					<td>
						<form:textarea type="text" path="description" class="form-control" rows="3"/>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<input type="submit" value="Submit" class="btn btn-primary w-100"/>
					</td>
				</tr>
			</table>
			
		</form:form>
	</div>
</body>
</html>