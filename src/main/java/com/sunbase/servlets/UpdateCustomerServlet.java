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
/**
 * Servlet implementation class UpdateCustomerServlet
 * 
 * This servlet is responsible for handling customer data updates in a web application.
 * It receives updated customer details via an HTTP POST request, validates the user's authentication token,
 * and sends the updated data to an external API for modification. Depending on the API response,
 * the servlet either redirects to the customer list page or displays error messages on the update-customer page.
 * Servlet implementation class UpdateCustomerServlet
 */
@WebServlet("/update")
public class UpdateCustomerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	/**
     * Handles HTTP POST requests for updating customer data.
     *
     * @param request  The HttpServletRequest object containing the updated customer data.
     * @param response The HttpServletResponse object used for rendering the response.
     * @throws ServletException If a servlet-specific error occurs.
     * @throws IOException      If an I/O error occurs while processing the request.
     */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String bearerToken = (String) request.getSession().getAttribute("access_token");
	    if (bearerToken == null || bearerToken.isEmpty()) {
	        request.setAttribute("errorMessage", "You are not authenticated. Please log in.");
	        request.getRequestDispatcher("/login.jsp").forward(request, response);
	        return;
	    }
		
		String customerId = request.getParameter("uuid");
        String firstName = request.getParameter("first_name");
        String lastName = request.getParameter("last_name");
        String street = request.getParameter("street");
        String address = request.getParameter("address");
        String city = request.getParameter("city");
        String state = request.getParameter("state");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        
        if (customerId == null || customerId.isEmpty()) {
            request.setAttribute("errorMessage", "UUID is missing.");
            request.getRequestDispatcher("/update-customer.jsp").forward(request, response);
            return;
        }
        String updateCustomerApiUrl = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp?cmd=update&uuid="+customerId;
        String jsonPayload = "{\"first_name\":\"" + firstName + "\",\"last_name\":\"" + lastName + 
        		"\",\"street\":\"" + street + "\",\"address\":\"" + address + "\",\"city\":\"" + 
        		city + "\",\"state\":\"" + state + "\",\"email\":\"" + email + "\",\"phone\":\"" + 
        		phone + "\"}";
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(updateCustomerApiUrl);
            httpPost.setHeader("Authorization", "Bearer " + bearerToken);
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setEntity(new StringEntity(jsonPayload));

            HttpResponse updateResponse = httpClient.execute(httpPost);
            int statusCode = updateResponse.getStatusLine().getStatusCode();

            if (statusCode == 200) {
                response.sendRedirect(request.getContextPath() + "/customer-list");
            } else if (statusCode == 500) {
                request.setAttribute("errorMessage", "UUID not found.");
                request.getRequestDispatcher("/update-customer.jsp").forward(request, response);
            } else if (statusCode == 400) {
                request.setAttribute("errorMessage", "Body is empty.");
                request.getRequestDispatcher("/update-customer.jsp").forward(request, response);
            } else {
                request.setAttribute("errorMessage", "Something went wrong. Please try again.");
                request.getRequestDispatcher("/update-customer.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred. Please try again later.");
            request.getRequestDispatcher("/update-customer.jsp").forward(request, response);
        
        }
	}

}
