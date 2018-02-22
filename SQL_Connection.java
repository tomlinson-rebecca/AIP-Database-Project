package default_package;
/**
 * Manages connecting our JDBC and java project to a MySQL database we have previously created.
 * TODO: See if you can create whole new database from the Java project, but for now,
 * have a single database and user can only access single table.
 * Goal for today: Allow user to enter new entries in the database, and search for them.
 * @author Beaky
 *
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQL_Connection{
	 //static reference to itself
    private static SQL_Connection instance = new SQL_Connection();     //WHY DOES THIS NOT CAUSE AN INFINTIE LOOP? bc static? Only one...
    public static String URL = "jdbc:mysql://localhost/aip_database_project";
    public static final String USER = "root";
    public static final String PASSWORD = "root";
    public static final String DRIVER_CLASS = "com.mysql.jdbc.Driver"; 
     
    //private constructor
    private SQL_Connection() { //maybe change this to take in a name for the database wanted
        try {
            //Step 2: Load MySQL Java driver
            Class.forName(DRIVER_CLASS);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    //custom DBname
    private SQL_Connection(String dbName) { 
        URL = "jdbc:mysql://localhost/"+dbName;
    	try {
            //Step 2: Load MySQL Java driver
            Class.forName(DRIVER_CLASS);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
     
    private Connection createConnection() {
 
        Connection connection = null;
        try {
            //Step 3: Establish Java MySQL connection
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("ERROR: Unable to Connect to Database.");
        }
        return connection;
    }   
     
    public static Connection getConnection() {
        return instance.createConnection();
    }
	
	
}