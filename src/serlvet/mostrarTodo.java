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

import spell.SpellCheck;
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
		
		String text = request.getParameter("text");
		
    	//Spell Check
		ArrayList <String> word_checked =new ArrayList <String>();
		String resp;
		try {
			resp = SpellCheck.check(text);
			System.out.println(resp);
			JSONParser parser = new JSONParser();
	    	JSONObject json = (JSONObject) parser.parse(resp);
	    	JSONArray fgt = (JSONArray)json.get("flaggedTokens");
	    	for(Object obj: fgt){
	    		JSONObject o = (JSONObject) obj;
	    		JSONArray sg = (JSONArray)o.get("suggestions");
	    		String originalWord = (String)o.get("token");
	    		//Take most likely
	    		String sMax="<EMPTY>";
	    		Double scoreMax=0.0;
	    		for(Object obj2: sg){
		    		JSONObject o2 = (JSONObject) obj2;
		    		String s = (String)o2.get("suggestion");
		    		Double score = (Double)o2.get("score");
		    		//If max score -> update
		    		if(score>scoreMax) {
		    			scoreMax=score;
		    			sMax=s;
		    		}
		    	}
	    		System.out.println(originalWord);
	    		System.out.println(sMax);
	    		text = text.replace(originalWord, sMax);
	    	
	    	}
	    	
	    	//Split Words
	    	for(String s: text.split(" ")) {
		    	word_checked.add(s);
	    	}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
    	
    	//Traductor
    	ArrayList <String> words_trans=new ArrayList <String>();
    	try {
    		for(String word: word_checked) {
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
    	
    	
    	
    	//Base de datos
    	sqlDAO	dao= sqlDAO.getDao();
    	ArrayList <String> words=null;
    	try {
    		dao.con = sqlDAO.createConnection();
    		
    		for(String s: words_trans) {
    			System.out.println(s);
    			dao.saveText(s);
    		}
    		words= dao.getAllWords();
    		
    	}
    	catch(Exception e) {
    		System.out.println("Error");
    		e.printStackTrace();
    	}
    	
		request.setAttribute("list", words);
    	
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
