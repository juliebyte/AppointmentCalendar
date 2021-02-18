/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Utilities.DBQuery;
import Utilities.DBConnection;
import Model.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author 
 */
/*  */
public class UserDaoImpl {
    

     public static User getSpecificUserByName (String userNameInput, String passwordInput) throws SQLException, Exception{
        // type is name or phone, value is the name or the phone #
         DBConnection.getConnection();
        //String sqlStatement = "select * FROM user WHERE userName  = '" + userNameInput + "'" + " AND password = '" + passwordInput + "'" ;
        String sqlStatement = "select * FROM user WHERE userName  = '" + userNameInput + "'  AND password = '" + passwordInput + "'";  
               
        
         //  String sqlStatement="select FROM address";
        DBQuery.makeQuery(sqlStatement);
           User userResult;
           ResultSet result = DBQuery.getResult();
           while(result.next()){
                int userid = result.getInt("userid");
                String userNameResult = result.getString("userName");
                String passwordResult = result.getString("password");                
                userResult = new User(userid, userNameResult, passwordResult);
                return userResult;
           }
        return null;
    }
     
     
     public static User getSpecificUserById (int userIdInput) throws SQLException, Exception{
        
         DBConnection.getConnection();
          
        String sqlStatement = "SELECT * FROM user WHERE userId  = '" + userIdInput + "'";  
         
        DBQuery.makeQuery(sqlStatement);
           User userResult;
           ResultSet result = DBQuery.getResult();
           while(result.next()){
                int userid = result.getInt("userid");
                String userNameResult = result.getString("userName");
                String passwordResult = result.getString("password");                
                userResult = new User(userid, userNameResult, passwordResult);
                return userResult;
           }
        return null;
     }
     
    public static ObservableList<User> getAllUsers() throws SQLException, Exception{
        ObservableList<User> allUsers = FXCollections.observableArrayList();    
        DBConnection.getConnection();
            String sqlStatement = "select * from user";          
            DBQuery.makeQuery(sqlStatement);
            ResultSet result = DBQuery.getResult();
             while(result.next()){
                int userid = result.getInt("userid");
                String userNameResult = result.getString("userName");
                String password = result.getString("password");              
             
                User userResult = new User(userid, userNameResult, password);
                allUsers.add(userResult);                
            }
        return allUsers;
        }
    
    public static ObservableList<User> getFirstLineUser() throws SQLException, Exception{
        ObservableList<User> firstLineUser = FXCollections.observableArrayList();               
                int userid = -1;
                String userName = "All Users";
                String password = "password";              
                User userResult = new User(userid, userName, password);
                firstLineUser.add(userResult);           
        return firstLineUser;
        }  
    
    } 







    