package com.java.mk.driving.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AddUserServlet extends HttpServlet {

	HttpSession session;

	public void doGet(HttpServletRequest request,
			HttpServletResponse response)
					throws IOException, ServletException
					{
		
					}

	public void doPost(HttpServletRequest request,
			HttpServletResponse response)
					throws IOException, ServletException
					{
			
		try 
		{ 
			MyDBConnection conn = MyDBConnection.getInstance();
			conn.validateRegistration(request);
			response.sendRedirect("next.jsp?msg=Registered successfully");
		} 
		catch(Exception e) 
		{ 
			 e.printStackTrace(); 
		}
		
	}
		/*session = request.getSession(true);
		session.setAttribute("userName", request.getParameter("myname"));
		doGet(request, response);*/

}