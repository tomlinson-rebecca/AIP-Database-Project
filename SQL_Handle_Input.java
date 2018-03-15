//mySql command line 5.7, pw root. show databases; connect aip_....; show tables;
/**
 * Parses user commands to insert/lookup in a hardcodedly selected database.
 * TODO: Make the multiple databases for each type "sync" upon startup
 */


//TODO have interface for all input handlers! Duhhhh!
//
package default_package;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;


public class SQL_Handle_Input implements DB_Handle_Input{
	String database = "todoList1";
    
    //inser this task into the SQL database.
    public void insertTask(TaskItem t){ 
    	 ResultSet rs = null; //TODO must we connect to database like this every time we want to access?
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

	public void removeTask(String taskName) {
		ResultSet rs = null;
		String query = "DELETE FROM "+ database +" WHERE taskName='" + taskName + "'";
		Connection connection = null;
        Statement statement = null; 
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
		
	public void printList() {
		printList("1");
	}

	
	public void printList(String sortedBy) {
		String sorted = "";
		if(sortedBy.equals("2")){ //sort by due date
			sorted = " order by dueDate";
		} 
		if(sortedBy.equals("3")){ //sort by priority
			sorted = " order by(case when priority='very high' then 1 when priority='high' then 2 when priority='medium' then 3"+
					" when priority='low' then 4 end)";
		} 
		
		ResultSet rs = null;
		String query = "SELECT * FROM "+database+sorted;
		Connection connection = null;
        Statement statement = null; 
        try {           
            connection = SQL_Connection.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            
            while (rs.next()) {
            	//Print one row          
            	for(int i = 1 ; i <= columnsNumber; i++){
            	      System.out.print(rs.getString(i) + " | "); //Print one element of a row
            	}
            	  System.out.println();//Move to the next line to print the next row.           
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
      
	}
}