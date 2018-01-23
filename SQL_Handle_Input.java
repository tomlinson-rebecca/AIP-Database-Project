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
 
    //get name from ID
    public String getName(int employeeId)  {      
        ResultSet rs = null;
        Connection connection = null;
        Statement statement = null; 
         
        String name = null;
        String query = "SELECT * FROM beans WHERE id=" + employeeId;
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
}