
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <title>Login</title>
</head>
<body>	
	<div class="container text-center my-3">
        <h1 class="font-weight-bold text-primary">SunbaseData</h1>
    </div>
     <div class="container">
        <% String errorMessage = (String) request.getAttribute("errorMessage");
        if (errorMessage != null && !errorMessage.isEmpty()) { %>
        	<div class="alert alert-danger">
            	<strong>Error:</strong> <%= errorMessage %>
        	</div>
        <% } %>
        <% String successMessage = (String) request.getAttribute("successMessage");
	   			if (successMessage != null && !successMessage.isEmpty()) { %>
	    			<div class="alert alert-success mt-3">
	        			<%= successMessage %>
	    			</div>
		<% } %>
		<% String uuid = request.getParameter("uuid");
        String firstName = request.getParameter("first_name");
        String lastName = request.getParameter("last_name");
        String email = request.getParameter("email"); %>
        <form action="update" method="post">
        <input type="hidden" name="uuid" value="<%= uuid %>">
            <div class="form-group">
                <label for="first_name">First Name:</label>
                <input type="text" class="form-control" id="first_name" value="<%= firstName %>" name="first_name" required>
            </div>
            <div class="form-group">
                <label for="last_name">Last Name:</label>
                <input type="text" class="form-control" id="last_name" value="<%= lastName %>" name="last_name" required>
            </div>
            <div class="form-group">
                <label for="street">Street:</label>
                <input type="text" class="form-control" id="street" name="street">
            </div>
            <div class="form-group">
                <label for="address">Address:</label>
                <input type="text" class="form-control" id="address" name="address">
            </div>
            <div class="form-group">
                <label for="city">City:</label>
                <input type="text" class="form-control" id="city" name="city">
            </div>
            <div class="form-group">
                <label for="state">State:</label>
                <input type="text" class="form-control" id="state" name="state">
            </div>
            <div class="form-group">
                <label for="email">Email:</label>
                <input type="email" class="form-control" id="email" value="<%= email %>" name="email">
            </div>
            <div class="form-group">
                <label for="phone">Phone:</label>
                <input type="tel" class="form-control" id="phone" name="phone">
            </div>
            <button type="submit" class="btn btn-primary">Update</button>
            
        </form>
    </div>
</body>
</html>
