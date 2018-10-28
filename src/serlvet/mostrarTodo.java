package serlvet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sql.sqlDAO;

/**
 * Servlet implementation class mostrarTodo
 */
@WebServlet("/mostrarTodo")
public class mostrarTodo extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public mostrarTodo() {
        // TODO Auto-generated constructor stub
    	
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		

    	sqlDAO	dao= sqlDAO.getDao();
    	try {
    		dao.con = sqlDAO.createConnection();
    		
    		request.setAttribute("list", dao.getAllWords());
    		
    	}
    	catch(Exception e) {
    		System.out.println("Error");
    		e.printStackTrace();
    	}
    	
    	System.out.println(request.getAttribute("list"));
    	
    	String nextJSP = "/list.jsp";
    	RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
    	dispatcher.forward(request,response);
    	
    	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
