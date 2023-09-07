package com.sunbase.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


/**
 * Servlet implementation class CustomerListServlet
 * 
 * This servlet is responsible for handling the retrieval and management of the customer list in a web application.
 * It performs actions such as fetching the customer list, displaying it to the user, and handling customer deletion requests.
 * The servlet communicates with an external API to retrieve and manipulate customer data.
 * 
 */
@WebServlet("/customer-list")
public class CustomerListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	/**
     * Handles HTTP GET requests for displaying the customer list.
     * Retrieves customer data from an external API, parses it, and forwards it to the customer-list.jsp view for rendering.
     *
     * @param request  The HttpServletRequest object used for handling the GET request.
     * @param response The HttpServletResponse object used for rendering the response.
     * @throws ServletException If a servlet-specific error occurs.
     * @throws IOException      If an I/O error occurs while processing the request.
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String bearerToken = (String) request.getSession().getAttribute("access_token");
        String getCustomerListApiUrl = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp?cmd=get_customer_list";
        if (bearerToken == null || bearerToken.isEmpty()) {
            request.setAttribute("errorMessage", "You are not authenticated. Please log in.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
        	
        	
            HttpGet httpGet = new HttpGet(getCustomerListApiUrl);
            httpGet.setHeader("Authorization", "Bearer " + bearerToken);

            HttpResponse customerListResponse = httpClient.execute(httpGet);

            int statusCode = customerListResponse.getStatusLine().getStatusCode();

            if (statusCode == 200) {
                String customerListJson = EntityUtils.toString(customerListResponse.getEntity());
                List<JsonObject> customerList = parseCustomerListFromResponse(customerListJson);
                request.setAttribute("customerList", customerList);
                request.getRequestDispatcher("/customer-list.jsp").forward(request, response);
            } else {
                request.setAttribute("errorMessage", "Failed to retrieve the customer list.");
                request.getRequestDispatcher("/customer-list.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred. Please try again later.");
            request.getRequestDispatcher("/customer-list.jsp").forward(request, response);
        }
	}
	
	/**
     * Handles HTTP POST requests for deleting a customer.
     * Deletes a customer using their UUID and forwards the request to the appropriate response view.
     *
     * @param request  The HttpServletRequest object used for handling the POST request.
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
		if (customerId == null || customerId.isEmpty()) {
			request.setAttribute("errorMessage", "Something went wrong, please try again");
         	request.getRequestDispatcher("/customer-list").forward(request, response);
		}
		 
        String deleteCustomerApiUrl = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp?cmd=delete&uuid=" + customerId;
      

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(deleteCustomerApiUrl);
            httpPost.setHeader("Authorization", "Bearer " + bearerToken);

            HttpResponse deleteResponse = httpClient.execute(httpPost);

            int statusCode = deleteResponse.getStatusLine().getStatusCode();
            if(statusCode == 200) {
            	response.sendRedirect(request.getContextPath() + "/customer-list");
            } else if (statusCode == 500) {
            	request.setAttribute("errorMessage", "Error! Not deleted");
            	request.getRequestDispatcher("/customer-list").forward(request, response);
            } else if (statusCode == 400) {
            	request.setAttribute("errorMessage", "UUID not found");
            	request.getRequestDispatcher("/customer-list").forward(request, response);
            } else {
            	request.setAttribute("errorMessage", "Something went wrong, please try again");
            	request.getRequestDispatcher("/customer-list").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Something went wrong, please try again");
        }
	        
	}

	 /**
     * Parses the customer list JSON response and returns a list of JsonObject representing customers.
     *
     * @param responseBody The JSON response containing customer data.
     * @return A List of JsonObject representing customers.
     */
	private List<JsonObject> parseCustomerListFromResponse(String responseBody) {
        List<JsonObject> customerList = new ArrayList<>();
        try {
            JsonParser parser = new JsonParser();
            JsonArray jsonCustomers = parser.parse(responseBody).getAsJsonArray();

            for (int i = 0; i < jsonCustomers.size(); i++) {
                JsonObject jsonCustomer = jsonCustomers.get(i).getAsJsonObject();
                customerList.add(jsonCustomer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customerList;
    }
	
	
	

}
