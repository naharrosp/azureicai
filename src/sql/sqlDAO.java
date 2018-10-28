package sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;


public class sqlDAO {
	
	public Connection con;
	
	private static sqlDAO dao;
	
	private sqlDAO() {
	}
	
	public static sqlDAO getDao() {
		
		if(dao==null) {
			dao=new sqlDAO();
		}
		
		return dao;
	}
	
	public static Connection createConnection ()  throws Exception
    {
        // Initialize connection variables. 
        String host = "mysqlcomillas.mysql.database.azure.com";
        String database = "psanchez";
        String user = "PabloSICAI@mysqlcomillas";
        String password = "@qwerty12345";

        // check that the driver is installed
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            throw new ClassNotFoundException("MySQL JDBC driver NOT detected in library path.", e);
        }

        System.out.println("MySQL JDBC driver detected in library path.");

        Connection connection = null;

        // Initialize connection object
        try
        {
            String url = String.format("jdbc:mysql://%s/%s", host, database);

            // Set connection properties.
            Properties properties = new Properties();
            properties.setProperty("user", user);
            properties.setProperty("password", password);
            properties.setProperty("useSSL", "false");
            properties.setProperty("verifyServerCertificate", "false");
            properties.setProperty("requireSSL", "false");

            // get connection
            connection = DriverManager.getConnection(url, properties);
        }
        catch (SQLException e)
        {
            throw new SQLException("Failed to create connection to database.", e);
        }
        
        return connection;
        
    }
	
	public ArrayList<String> getAllWords() throws Exception{
		
		if (con == null) 
        { 
			sqlDAO.createConnection();
        }
        System.out.println("Successfully created connection to database.");

        ArrayList <String> array = new ArrayList <String> ();
        // Perform some SQL queries over the connection.
        try
        {
            PreparedStatement preparedStatement = con.prepareStatement("select word from psanchez.list");
            ResultSet rs = preparedStatement.executeQuery();
            
            while (rs.next()) {

				String word = rs.getString("word");
				array.add(word);

			}
        }
        catch (SQLException e)
        {
            throw new SQLException("Encountered an error when executing given sql statement.", e);
        }       
        
        System.out.println("Execution finished.");
        
        return array;
	}

}
