/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Model.Appointment;
import Utilities.DBQuery;
import Utilities.DBConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import Model.Customer;
import Utilities.DateConversion;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.TableColumn;
import view_controller.LoginController;

/**
 *
 * @author Julie
 */

public class CustomerDaoImpl {
    static boolean isActive = false;
     public static Customer getCustomerById(TableColumn<Customer, Integer> customerIdLimit) throws SQLException, Exception{      
        DBConnection.getConnection();
        String sqlStatement = "select customerName, addressId, active FROM customer WHERE customerId  = '" + customerIdLimit + "'";
        DBQuery.makeQuery(sqlStatement);
           Customer customerResult;
           ResultSet result = DBQuery.getResult();
           while(result.next()){
                int customerId = result.getInt("customerId");
                String customerName = result.getString("customerName");
                int addressId = result.getInt("addressId");
                customerResult = new Customer (customerId, customerName, addressId);
                return customerResult;
           }
        return null;
    }
         
     
    public static ObservableList<Customer> getAllCustomers() throws SQLException, Exception{
        
        ObservableList<Customer> allCustomers = FXCollections.observableArrayList();         
        DBConnection.getConnection();        
        String sqlStatement = "SELECT * FROM customer";  
        
        PreparedStatement prepStmt = DBConnection.getConnection().prepareStatement(sqlStatement);
        
        ResultSet resSet = prepStmt.executeQuery();        
             while(resSet.next()){
                int customerId = resSet.getInt("customerId");
                String customerName = resSet.getString("customerName");
                int addressId = resSet.getInt("addressId");
                Customer customerResult = new Customer(customerId, customerName, addressId);
                allCustomers.add(customerResult);                
            }           
        return allCustomers;
        }

    public static Customer getCustomerNameById(TableColumn<Appointment, Integer> customerIdLimit) 
        throws SQLException, Exception {    
        DBConnection.getConnection();
        String sqlStatement = "select customerName, addressId, active FROM customer WHERE customerId  = '" + customerIdLimit + "'";
        DBQuery.makeQuery(sqlStatement);
           Customer customerResult;
           ResultSet result = DBQuery.getResult();
           while(result.next()){
                int customerId = result.getInt("customerId");
                String customerName = result.getString("customerName");
                int addressId = result.getInt("addressId");
                customerResult = new Customer(customerId, customerName, addressId);
                return customerResult;
           }
        return null;
    }    
     
    public static Customer getSpecificCustomer(Appointment appointment) throws SQLException, Exception{        
                
        DBConnection.getConnection();  
        String sqlStatement = "select * FROM customer WHERE customerId  = '" + appointment.getCustomerId() + "'";
        DBQuery.makeQuery(sqlStatement);
           Customer customerResult;
           ResultSet result = DBQuery.getResult();
           while(result.next()){
                int customerId = result.getInt("customerId");
                String customerName = result.getString("customerName");
                int addressId = result.getInt("addressId");
                customerResult = new Customer(customerId, customerName, addressId);
                return customerResult;
           }
        return null;
    }    
              
        
    
    public static boolean addNewCustomer(Customer customer) throws SQLException, Exception{        
                
        DBConnection.getConnection();  
        String sqlStatement = "";        
        PreparedStatement prepStmt = DBConnection.getConnection().prepareStatement(sqlStatement);         
        
        try {
                sqlStatement = "INSERT INTO customer (customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES (?, ?, ?, ?, ?, ?, ?)";        
                prepStmt = DBConnection.getConnection().prepareStatement(sqlStatement);   
                prepStmt.setString(1, customer.getCustomerName());
                prepStmt.setInt(2, customer.getAddressId());
                prepStmt.setInt(3, 1);  
                prepStmt.setTimestamp(4, DateConversion.fromLocalTimetoUTC(LocalDateTime.now()));
                prepStmt.setString(5, LoginController.loggedInUserName);
                prepStmt.setTimestamp(6, DateConversion.fromLocalTimetoUTC(LocalDateTime.now()));
                prepStmt.setString(7, LoginController.loggedInUserName); 
                prepStmt.executeUpdate();          
            } 
        catch (Exception ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }            
        return true;
    } 
     
     
    public static boolean updateCustomer(Customer customer) throws SQLException, Exception{        
                
        DBConnection.getConnection();  
        String sqlStatement = "";        
        PreparedStatement prepStmt = DBConnection.getConnection().prepareStatement(sqlStatement);         
        
        try {
                sqlStatement = "UPDATE customer SET customerName = ?, addressId = ?, lastUpdate = ?, lastUpdateBy = ? WHERE customerId = ?;";        
                prepStmt = DBConnection.getConnection().prepareStatement(sqlStatement);   
                prepStmt.setString(1, customer.getCustomerName());
                prepStmt.setInt(2, customer.getAddressId());
                prepStmt.setTimestamp(3, DateConversion.fromLocalTimetoUTC(LocalDateTime.now()));
                prepStmt.setString(4, LoginController.loggedInUserName);
                prepStmt.setInt(5, customer.getCustomerId());       
                prepStmt.executeUpdate();          
            } 
        catch (Exception ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }            
        return true;
    } 
    

    public static boolean deleteCustomer(Customer customer) 
        throws SQLException, Exception {    
       
        DBConnection.getConnection();        
        try {
            
            String sqlStatement = "DELETE FROM customer WHERE customerId = " + customer.getCustomerId()+ ";";
            DBQuery.makeQuery(sqlStatement);        
            }
        catch (Exception ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);            
            return false;            
        }         
        return true;        
        }  
    }
    
   
    