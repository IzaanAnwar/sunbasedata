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
        <h2>Login</h2>
        <% String errorMessage = (String) request.getAttribute("errorMessage");
        if (errorMessage != null && !errorMessage.isEmpty()) { %>
        <div class="alert alert-danger">
            <strong>Error:</strong> <%= errorMessage %>
        </div>
        <% } %>
        <form action="login" method="post">
            <div class="form-group">
                <label for="login_id">Login ID:</label>
                <input type="text" class="form-control" id="login_id" name="login_id" required>
            </div>
            <div class="form-group">
                <label for="password">Password:</label>
                <input type="password" class="form-control" id="password" name="password" required>
            </div>
            <button type="submit" class="btn btn-primary">Login</button>
        </form>
    </div>
</body>
</html>
