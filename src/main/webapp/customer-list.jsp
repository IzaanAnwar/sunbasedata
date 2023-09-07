<%@ page import="com.google.gson.JsonObject" %>
<%@ page import="java.util.List" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">


<title>Customer List</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
	<div class="container text-center my-3">
        <h1 class="font-weight-bold text-primary">SunbaseData</h1>
    </div>
  <div class="container">
  		<form action="/sunbase/add-customer.jsp" method="post">
	        <button type="submit" class="mt-5 btn btn-primary btn-md">Add Customer</button>
  		

	        <% String errorMessage = (String) request.getAttribute("errorMessage");
	           if (errorMessage != null && !errorMessage.isEmpty()) { %>
	            <div class="alert alert-danger mt-3">
	                <strong>Error:</strong> <%= errorMessage %>
	            </div>
	        <% } %>
		</form>

       <table class="table table-bordered mt-3">
    		<thead class="thead-dark">
        		<tr>
		            <th>First Name</th>
		            <th>Last Name</th>
		            <th>Street</th>
		            <th>Address</th>
		            <th>City</th>
		            <th>State</th>
		            <th>Email</th>
		            <th>Phone</th>
		            <th>Action</th>
        		</tr>
    		</thead>
    	<tbody>
	        <% Object customerListObj = request.getAttribute("customerList");
	        if (customerListObj != null && customerListObj instanceof List<?>) {
	            List<?> customerList = (List<?>) customerListObj;
	            for (Object customerObj : customerList) {
	                if (customerObj instanceof JsonObject) {
	                    JsonObject customer = (JsonObject) customerObj;
	        %>
	        <tr>
	            <td><%= customer.get("first_name").getAsString() %></td>
	            <td><%= customer.get("last_name").getAsString() %></td>
	            <td><%= customer.get("street").getAsString() %></td>
	            <td><%= customer.get("address").getAsString() %></td>
	            <td><%= customer.get("city").getAsString() %></td>
	            <td><%= customer.get("state").getAsString() %></td>
	            <td><%= customer.get("email").getAsString() %></td>
	            <td><%= customer.get("phone").getAsString() %></td>
	            <td>
	            	<div class="d-flex justify-content-between">
		            	<form action="customer-list" method="post" class="">
		            		<input type="hidden" name="uuid" value="<%= customer.get("uuid").getAsString() %>">
	                        <button type="submit" class="btn btn-danger btn-sm" title="Delete Customer">D</button>	            	
	                    </form>
		            	<form action="/sunbase/update-customer.jsp" method="post">
		            		<input type="hidden" name="uuid" value="<%= customer.get("uuid").getAsString() %>">
        					<input type="hidden" name="first_name" value="<%= customer.get("first_name").getAsString() %>">
        					<input type="hidden" name="last_name" value="<%= customer.get("last_name").getAsString() %>">
        					<input type="hidden" name="email" value="<%= customer.get("email").getAsString() %>">			
		            		<button class="btn btn-sm btn-primary" type="submit">U</button>
		            	</form>
	            	</div>
	            </td>
	            
	        </tr>
	        <%
	                }
	            }
	        } %>
    	</tbody>
</table>
    </div>
</body>
</html>
