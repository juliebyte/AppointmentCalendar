/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import static Utilities.DBConnection.conn;

/**
 *
 * @author carolyn.sher
 */
public class DBQuery {
    private static String query;
    private static Statement statement;
    private static ResultSet resultData;
    
    public static void makeQuery(String createdQuery){
        query = createdQuery;
        try{
            statement = conn.createStatement();
            // determine query execution
            if(query.toLowerCase().startsWith("select"))
                resultData = statement.executeQuery(createdQuery);
             if(query.toLowerCase().startsWith("delete")|| 
                query.toLowerCase().startsWith("insert")|| 
                query.toLowerCase().startsWith("update"))
                    statement.executeUpdate(createdQuery);            
        }
        catch(Exception ex){
            System.out.println("Error in DBQuery in makeQuery  " + ex.getMessage());
            //System.out.println("Error: " + ex.getMessage());
        }
    }
    
    public static ResultSet getResult(){
        return resultData;
    }
      
}



 
