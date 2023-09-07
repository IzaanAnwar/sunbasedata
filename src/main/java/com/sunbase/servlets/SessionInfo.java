package com.sunbase.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class SessionInfo
 */
@WebServlet("/session-info")
public class SessionInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	 protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	        response.setContentType("text/html;charset=UTF-8");

	        HttpSession session = request.getSession();
	        String accessToken = (String) session.getAttribute("access_token");

	        response.getWriter().println("<html>");
	        response.getWriter().println("<body>");
	        response.getWriter().println("<h1>Session Information</h1>");
	        response.getWriter().println("<p>Access Token: " + accessToken + "</p>");
	        response.getWriter().println("</body>");
	        response.getWriter().println("</html>");
	    }

}
