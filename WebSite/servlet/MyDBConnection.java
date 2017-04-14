package com.java.mk.driving.servlet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;





public class MyDBConnection {

	private String dbUsername = "root";
	private String dbPassword = "root";
	private String dbDriver = "com.mysql.jdbc.Driver";
	private String dbConnectionString = "jdbc:mysql://localhost:3306/safe_driving_db";
	
	private Connection connection=null; 
	PreparedStatement preparestatement=null; 
	static MyDBConnection ref = null;
   static String str=null;
	MyDBConnection()
	{
		try
		{ 
			Class.forName(dbDriver); 
			connection=DriverManager.getConnection(dbConnectionString,dbUsername,dbPassword);	
		}
		catch(ClassNotFoundException e)
		{ 
			System.out.println(e); 
		} 
		catch(SQLException e) 
		{ 
			System.out.println(e); 
		} 
	}

	public static MyDBConnection getInstance()
	{
		if(ref == null)
		{
			ref = new MyDBConnection();
		}
		return ref;
	}

	public void closeConnection()
	{
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String validateUser(String username, String password, HttpSession session) 
	{
		try {

			preparestatement=connection.prepareStatement("Select * from usertable where user_name=? and password=?");
			preparestatement.setString(1,username);
			preparestatement.setString(2,password);
			ResultSet rs= preparestatement.executeQuery();
			if(rs.next())
			{
			    return "true";
			}
			return null;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return null;
	}


	public String validateEmail(String email)
	{
		String secureQues=null;
		try
		{
			preparestatement=connection.prepareStatement("Select security_question from user_info_table where email =? ");
			preparestatement.setString(1,email);
			ResultSet rs=preparestatement.executeQuery();
			if (rs.next())
			{	 
				secureQues=rs.getString("security_question");
			}	
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return secureQues;



	}

	public void validateRegistration(HttpServletRequest request)
	{
		String username=request.getParameter("username");
		String password=request.getParameter("password");
		String questionfield=request.getParameter("questionfield");
		String answerTextBox=request.getParameter("answerTextBox");
		String firstname=request.getParameter("firstname");
		String lastname=request.getParameter("lastname");
		String mobile=request.getParameter("mobile");
		String email=request.getParameter("email");
		String address=request.getParameter("address");
		System.out.println(address);
		try
		{
			preparestatement=connection.prepareStatement("Insert into usertable(user_name,password,userName,userAddress,userPhoneNumber,userEmailId)values(?,?,?,?,?,?)"); 

			preparestatement.setString(1,username); 
			preparestatement.setString(2,password);
			preparestatement.setString(3,firstname);
			preparestatement.setString(4,address);
			preparestatement.setString(5,mobile);
			preparestatement.setString(6,email);
			preparestatement.executeUpdate(); 

		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void UpdatePassword(String email, String secureAnswer, String newPassword)
	{
		try
		{System.out.println(email+secureAnswer+newPassword);
			preparestatement=connection.prepareStatement("update user_info_table set password='"+newPassword+"' where email=? and security_answer=?");
			preparestatement.setString(1,email);
			preparestatement.setString(2,secureAnswer);
		    preparestatement.executeUpdate();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	


public int insertProfessor(String firstname,String lastname,String userAddress,String userPhoneNumber,String userEmailId,String user_name,String password)
{
	String query = "insert into professor(professor_first_name,professor_last_name,professor_email_id,professor_mobile_no,professor_contact_address,professor_username,professor_password) values('"+firstname+"','"+ lastname+"','"+userEmailId+"','"+userPhoneNumber+"','"+userAddress+"','"+user_name+"','"+password+"')";
	try {
		Statement statement = connection.createStatement();
		return statement.executeUpdate(query);
	} catch (Exception e) {
		e.printStackTrace();
	}
	return 0;
}
public int insertStudentGroup(String Group_Name,String Group_Size)
{
	String query = "insert into student_group(student_group_name,student_group_size) values('"+Group_Name+"','"+Group_Size +"')";
	try {
		Statement statement = connection.createStatement();
		return statement.executeUpdate(query);
	} catch (Exception e) {
		e.printStackTrace();
	}
	return 0;
}

public int insertCourse(String Course_Name,String Course_Description)
{
	String query = "insert into course(course_name,course_description) values('"+Course_Name+"','"+Course_Description +"')";
	try {
		Statement statement = connection.createStatement();
		return statement.executeUpdate(query);
	} catch (Exception e) {
		e.printStackTrace();
	}
	return 0;
}


public int insertClassroom(String classroom_name,String  classroom_size,String  Lab)
{
	String query = "insert into classroom(classroom_name,classroom_size,classroom_islab) values('"+classroom_name+"','"+ classroom_size+"','"+Lab+"')";
	try {
		Statement statement = connection.createStatement();
		return statement.executeUpdate(query);
	} catch (Exception e) {
		e.printStackTrace();
	}
	return 0;
}

public int insertClass(String Professor_name,String courseName,String groupName,String Duration,String Lab)
{
	String query = "insert into class(prof_id,course_id,student_group_id,duration,lab) values('"+Professor_name+"','"+courseName +"','"+groupName+"','"+Duration+"','"+Lab+"')";
	try {
		Statement statement = connection.createStatement();
		return statement.executeUpdate(query);
	} catch (Exception e) {
		e.printStackTrace();
	}
	return 0;
}


public int deleteCourse(String courseId)
{
	String query = "DELETE from course where course_id='"+courseId+"'";
	try {
		Statement statement = connection.createStatement();
		return statement.executeUpdate(query);
	} catch (Exception e) {
		e.printStackTrace();
	}
	return 0;
}

public int deleteClassroom(String ClassroomId)
{
	String query = "DELETE from classroom where classroom_id='"+ ClassroomId+"'";
	try {
		Statement statement = connection.createStatement();
		return statement.executeUpdate(query);
	} catch (Exception e) {
		e.printStackTrace();
	}
	return 0;
}

public int deleteTeacher(String Teacher_id)
{
	String query = "DELETE from professor where prof_id='"+ Teacher_id+"'";
	try {
		Statement statement = connection.createStatement();
		return statement.executeUpdate(query);
	} catch (Exception e) {
		e.printStackTrace();
	}
	return 0;
}
public int deleteStudentGroup(String StudentGroupId)
{
	String query = "DELETE from student_group where student_group_id='"+StudentGroupId +"'";
	try {
		Statement statement = connection.createStatement();
		return statement.executeUpdate(query);
	} catch (Exception e) {
		e.printStackTrace();
	}
	return 0;
}

public int deleteClass(String ClassId)
{
	String query = "DELETE from class where class_id='"+ ClassId+"'";
	try {
		Statement statement = connection.createStatement();
		return statement.executeUpdate(query);
	} catch (Exception e) {
		e.printStackTrace();
	}
	return 0;
}

public int updateCourse(String courseId,String NewCourseName,String NewCourseDescription)
{
	String query = "update course set course_name = '"+ NewCourseName+"', course_description = '"+ NewCourseDescription +"' where  course_id= '"+courseId+"'";
	try {
		Statement statement = connection.createStatement();
		return statement.executeUpdate(query);
	} catch (Exception e) {
		e.printStackTrace();
	}
	return 0;
}

public int updateClassroom(String classroomId,String classroom_name,String  classroom_size,String  Lab)
{
	String query = "update classroom set classroom_name='"+classroom_name+"',classroom_size='"+ classroom_size+"',classroom_islab='"+Lab+"' where classroom_id='"+classroomId+"'";
	try {
		Statement statement = connection.createStatement();
		return statement.executeUpdate(query);
	} catch (Exception e) {
		e.printStackTrace();
	}
	return 0;
}


public int updateProfessor(String ProfessorId, String firstname,String lastname,String userAddress,String userPhoneNumber,String userEmailId,String user_name,String password)
{
	String query="update professor set professor_first_name='"+firstname+"',professor_last_name='"+lastname+"', professor_email_id='"+userEmailId+"',professor_mobile_no='"+userPhoneNumber+"',professor_contact_address='"+userAddress+"',professor_username='"+user_name+"',professor_password='"+password+"' where prof_id='"+ProfessorId+"' ";
	
	try {
		Statement statement = connection.createStatement();
		return statement.executeUpdate(query);
	} catch (Exception e) {
		e.printStackTrace();
	}
	return 0;
}
public int updateStudentGroup(String GroupId,String Group_Name,String Group_Size)

{
	String query="update student_group set student_group_name='"+Group_Name+"',student_group_size='"+Group_Size+"' where student_group_id='"+GroupId+"' ";
	try
	{
		Statement statement=connection.createStatement();
		return statement.executeUpdate(query);
	} catch (Exception e) {
		e.printStackTrace();
	}
	return 0;
	
}

public int updateClass(String class_id,String professorName,String courseName,String groupName,String Duration,String Lab)

{
	String query="update class set prof_id='"+professorName+"',course_id='"+courseName+"',student_group_id='"+groupName+"',duration='"+Duration+"',lab='"+Lab+"'  where class_id='"+class_id+"' ";
	try
	{
		Statement statement=connection.createStatement();
		return statement.executeUpdate(query);
	} catch (Exception e) {
		e.printStackTrace();
	}
	return 0;
	
}
	


}