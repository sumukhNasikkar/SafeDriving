package com.java.mk.driving.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginServlet extends HttpServlet {

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
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		System.out.println(username);
		System.out.println(password);

		if(username!=null) 
		{ 
			try 
			{ 
				MyDBConnection conn = MyDBConnection.getInstance();
				String str=conn.validateUser(username, password, session);

				if(str!=null)
				{
					HttpSession session = request.getSession(true);
					session.setAttribute("UserName", username);

					response.sendRedirect("welcome.jsp");					
				}
				else 
				{
					response.sendRedirect("error.jsp?msg=invalid user...");
				}								
			} 
			catch(Exception e) 
			{ 
				e.printStackTrace(); 
			}
		}
	}
}

