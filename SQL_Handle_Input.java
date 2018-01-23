/**
 * Parses user commands to insert/lookup in a hardcodedly selected database
 */


//TODO have interface for all input handlers! Duhhhh!
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;


public class SQL_Handle_Input {
	String database = "todoList1";
	
    //get name from ID
    public String getName(int employeeId)  {      
        ResultSet rs = null;
        Connection connection = null;
        Statement statement = null; 
         
        String name = null;
        String query = "SELECT * FROM "+database+" WHERE id=" + employeeId;
        try {           
            connection = SQL_Connection.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
             
            if (rs.next()) {
               
               name = (rs.getString("name"));
               
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return name;
    }
    
    //inser this task into the SQL database.
    public void insertTask(TaskItem t){
    	 ResultSet rs = null;
         Connection connection = null;
         Statement statement = null; 
          
         String name = null;				// ID int NOT NULL AUTO_INCREMENT autoincrements ID
         									//DATETIME datatype  YYYY-MM-DD HH:MI:SS
         String query = "INSERT INTO "+database+"(taskName, dueDate, priority) VALUES('"+t.name+"','"+t.date+"','"+t.priority+"')";
         //String query = "INSERT INTO "+database+" VALUES(2, 'hihi')";
         try {           
             connection = SQL_Connection.getConnection();
             statement = connection.createStatement();
             statement.executeUpdate(query);
              
         } catch (SQLException e) {
             e.printStackTrace();
         } finally {
             if (connection != null) {
                 try {
                     connection.close();
                 } catch (SQLException e) {
                     e.printStackTrace();
                 }
             }
         }
    	
    }
}