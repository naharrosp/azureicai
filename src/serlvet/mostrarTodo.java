package serlvet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.JsonObject;

import sql.sqlDAO;
import translator.Translate;

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
		
		
		//Base de datos
    	sqlDAO	dao= sqlDAO.getDao();
    	ArrayList <String> words=null;
    	try {
    		dao.con = sqlDAO.createConnection();
    		
    		words= dao.getAllWords();
    		
    	}
    	catch(Exception e) {
    		System.out.println("Error");
    		e.printStackTrace();
    	}
    	
    	//Traductor
    	ArrayList <String> words_trans=new ArrayList <String>();
    	try {
    		for(String word: words) {
		    	String translatedText = Translate.TranslateText (word);
		    	
		    	JSONParser parser = new JSONParser();
		    	JSONObject json = (JSONObject) parser.parse(translatedText);
		    	
		    	JSONArray trans = (JSONArray)json.get("translations");
		    	for(Object obj: trans){
		    		JSONObject o = (JSONObject) obj;
		    		String w = (String)o.get("text");
		    		words_trans.add(w);
		    	}
    		}

	    		
    	}catch(Exception e) {
    		System.out.println("Error");
    		e.printStackTrace();
    	}
    	
		request.setAttribute("list", words_trans);
    	
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
