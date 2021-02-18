package Utilities;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Julie Walin
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.ZoneId;




/**
 *
 * @author carolyn.sher
 */
public class DBConnection {
    private static final String databaseName="U06115";
    private static final String DB_URL="jdbc:mysql://3.227.166.251/"+databaseName;
    private static final String username="U06115";
    private static final String password="53688668229";
    private static final String driver="com.mysql.jdbc.Driver";
    public  static final ZoneId DB_TIME_ZONE = ZoneId.of("UTC");
    
    static Connection conn;
    
    public static Connection makeConnection()throws ClassNotFoundException, SQLException, Exception
    {
        try {
            Class.forName(driver);    
            conn=(Connection) DriverManager.getConnection(DB_URL,username,password);
            System.out.println("Connection Successful");
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("Error: " + e.getMessage());
        }
        catch (SQLException e)
        {
            System.out.println("Error: " + e.getMessage());
        } 
        
        return conn;
    }
    
    public static Connection getConnection() {
        return conn; 
    }   
    
    public static void closeConnection()throws ClassNotFoundException, SQLException, Exception{
        conn.close();
        System.out.println("Connection Closed");
    }

    
    
}
