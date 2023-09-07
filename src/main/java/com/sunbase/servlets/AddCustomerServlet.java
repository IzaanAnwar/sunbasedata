package com.sunbase.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * Servlet implementation class AddCustomerServlet
 * 
 * This servlet is responsible for handling the addition of a new customer to a web application.
 * It receives customer details via an HTTP POST request, validates the user's authentication token,
 * and sends the customer data to an external API for creation. Depending on the API response,
 * the servlet either redirects to a customer list page or displays error messages on the add-customer page.
 *
 */
@WebServlet("/add-customer")
public class AddCustomerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	/**
     * Handles HTTP POST requests for adding a new customer.
     *
     * @param request  The HttpServletRequest object containing the customer data.
     * @param response The HttpServletResponse object used for rendering the response.
     * @throws ServletException If a servlet-specific error occurs.
     * @throws IOException      If an I/O error occurs while processing the request.
     */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 String firstName = request.getParameter("first_name");
		 String lastName = request.getParameter("last_name");
	     String street = request.getParameter("street");
	     String address = request.getParameter("address");
	     String city = request.getParameter("city");
	     String state = request.getParameter("state");
	     String email = request.getParameter("email");
	     String phone = request.getParameter("phone");
	     
	     String bearerToken = (String) request.getSession().getAttribute("access_token");
	     String createCustomerUrl = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp?cmd=create";
	     
	     try(CloseableHttpClient httpClient = HttpClients.createDefault()){
	    	 if (bearerToken == null || bearerToken.isEmpty()) {
	             request.setAttribute("errorMessage", "You are not authenticated. Please log in.");
	             request.getRequestDispatcher("/login.jsp").forward(request, response);
	             return; 
	         }
	    	 HttpPost httpPost = new HttpPost(createCustomerUrl);
	    	 String customerDetails = "{"
	                    + "\"first_name\":\"" + firstName + "\","
	                    + "\"last_name\":\"" + lastName + "\","
	                    + "\"street\":\"" + street + "\","
	                    + "\"address\":\"" + address + "\","
	                    + "\"city\":\"" + city + "\","
	                    + "\"state\":\"" + state + "\","
	                    + "\"email\":\"" + email + "\","
	                    + "\"phone\":\"" + phone + "\""
	                    + "}";
	    	 httpPost.setHeader("Content-Type", "application/json");
	         httpPost.setHeader("Authorization", "Bearer " + bearerToken);
	         httpPost.setEntity(new StringEntity(customerDetails));
	         
	         HttpResponse createCustomerRes = httpClient.execute(httpPost);
	         int statusCode = createCustomerRes.getStatusLine().getStatusCode();
	         if (statusCode == 201) {
	                response.sendRedirect(request.getContextPath() + "/customer-list");
	         } else if (statusCode == 400) {
	                request.setAttribute("errorMessage", "Invalid request. Please check your data.");
	                request.getRequestDispatcher("/add-customer.jsp").forward(request, response);
	         } else {
	                request.setAttribute("errorMessage", "Customer creation failed. Please try again.");
	                request.getRequestDispatcher("/add-customer.jsp").forward(request, response);
	         }
	     } catch (Exception e) {
	            e.printStackTrace();
	            request.setAttribute("errorMessage", "An error occurred. Please try again later.");
	            request.getRequestDispatcher("/add-customer.jsp").forward(request, response);
	        }
	     
	}

}
