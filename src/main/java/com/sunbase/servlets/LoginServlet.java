package com.sunbase.servlets;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


/**
 * Servlet implementation for handling user login.
 * This servlet processes user login credentials and communicates with the authentication API.
 * Upon successful login, it retrieves and stores an access token in the session and redirects the user to the customer list page.
 * In case of authentication failure, appropriate error messages are forwarded to the login page.
 *
 * @author Izaan Anwar
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	/**
     * Handles POST requests for user login.
     *
     * @param request  The HttpServletRequest object containing the login credentials.
     * @param response The HttpServletResponse object used for sending responses.
     * @throws ServletException If there's an issue with servlet processing.
     * @throws IOException      If there's an issue with input or output.
     */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String loginId = request.getParameter("login_id");
		String passwd = request.getParameter("password");
		
		String loginApiUrl = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment_auth.jsp";
		
		try (CloseableHttpClient httpClient = HttpClients.createDefault()){
			String loginReqBody = "{\"login_id\":\"" + loginId + "\",\"password\":\"" + passwd + "\"}";
			
			HttpPost httpPost = new HttpPost(loginApiUrl);
			httpPost.setHeader("Content-Type", "application/json");
			httpPost.setEntity(new StringEntity(loginReqBody));
			
			HttpResponse loginRes = httpClient.execute(httpPost);
			
			int statusCode = loginRes.getStatusLine().getStatusCode();
			if (statusCode == 200) {
				HttpEntity resEntity = loginRes.getEntity();
				String loginResContent = EntityUtils.toString(resEntity);
				
				String bearerToken = parseBearerTokenFromResponse(loginResContent);
				if(bearerToken == null) {
					  request.setAttribute("errorMessage", "Something went wrong please try agian.");
		              request.getRequestDispatcher("/login.jsp").forward(request, response);
				}
				request.getSession().setAttribute("access_token", bearerToken);
				response.sendRedirect(request.getContextPath() + "/customer-list.jsp");
				
			} else if (statusCode == 401) {
                request.setAttribute("errorMessage", "Invalid Authorization");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            } else {
                request.setAttribute("errorMessage", "Authentication failed. Please try again.");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
				
		} catch (Exception  e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "An error occurred. Please try again later.");
			request.getRequestDispatcher("/login.jsp").forward(request, response);
		}
		
	}

	/**
     * Parses the access token from the authentication API response.
     *
     * @param responseBody The response body from the authentication API.
     * @return The access token as a string, or null if not found or an error occurred.
     */
	private String parseBearerTokenFromResponse(String responseBody) {
		try {
			JsonParser parser = new JsonParser();
			JsonObject jsonResponse = parser.parse(responseBody).getAsJsonObject();
		
			if (jsonResponse.has("access_token")) {
				String bearerToken = jsonResponse.get("access_token").getAsString();
				return bearerToken;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		
		
		
	}

}
