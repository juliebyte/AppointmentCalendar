/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Model.Address;
import Model.Appointment;
import Model.Customer;
import Utilities.DBConnection;
import Utilities.DBQuery;
import Utilities.DateConversion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import view_controller.LoginController;

/**
 *
 * @author julie
 */
public class AddressDaoImpl {
    
    public static Address getSpecificAddress (Customer customer) throws SQLException, Exception{
        DBConnection.getConnection();
        String sqlStatement = "select * FROM address WHERE addressId  = '" + customer.getAddressId() + "'";  
        
        DBQuery.makeQuery(sqlStatement);
           Address addressResult;
           ResultSet resultData = DBQuery.getResult();
           while(resultData.next()){               
                int addressId = resultData.getInt("addressId");
                String address = resultData.getString("address");
                int cityId = resultData.getInt("cityId");
                String postalCode = resultData.getString("postalCode");
                String phone = resultData.getString("phone");
                addressResult = new Address (addressId, address, cityId, postalCode, phone);                
                
                return addressResult;
           }
        return null;
    }
    
       
    
    public static String getMaxAddressId() throws SQLException, Exception{
        ObservableList<String> maxAddress = FXCollections.observableArrayList();  
        String maxAddressId = "0";
        DBConnection.getConnection();
            String sqlStatement = "SELECT MAX(addressId) from address";         
            DBQuery.makeQuery(sqlStatement);
            ResultSet resultData = DBQuery.getResult();
             while(resultData.next()){                
                maxAddressId = resultData.getString("MAX(addressId)");  
                maxAddress.add(maxAddressId);
            }           
        return maxAddressId;
        }
    
    
    /**
     * addNewAddress inserts a new address row into the address table.
     * @param address
     * @return
     * @throws SQLException
     * @throws Exception 
     */
    public static boolean addNewAddress(Address address) throws SQLException, Exception{        
                
        DBConnection.getConnection();  
        String sqlStatement = "";
        
        PreparedStatement prepStmt = DBConnection.getConnection().prepareStatement(sqlStatement);         
        
        try {
                sqlStatement = "INSERT INTO address (address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";        
                prepStmt = DBConnection.getConnection().prepareStatement(sqlStatement);   
                prepStmt.setString(1, address.getAddress());
                prepStmt.setString(2, " ");                
                prepStmt.setInt(3, address.getCityId());
                prepStmt.setString(4, address.getPostalCode());                
                prepStmt.setString(5, address.getPhone());
                prepStmt.setTimestamp(6, DateConversion.fromLocalTimetoUTC(LocalDateTime.now()));
                prepStmt.setString(7, LoginController.loggedInUserName);
                prepStmt.setTimestamp(8, DateConversion.fromLocalTimetoUTC(LocalDateTime.now()));
                prepStmt.setString(9, LoginController.loggedInUserName);
                prepStmt.executeUpdate();       
            } 
        catch (Exception ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }            
        return true;  
    } 
    
    public static boolean updateAddress(Address address) throws SQLException, Exception{        
                
        DBConnection.getConnection();  
        String sqlStatement = "";
        
        PreparedStatement prepStmt = DBConnection.getConnection().prepareStatement(sqlStatement);         
        
        try {
                sqlStatement = "UPDATE address SET address = ?, cityId = ?, postalCode = ?, phone = ?, lastUpdate = ?, lastUpdateBy = ? WHERE addressId = ?";        
                prepStmt = DBConnection.getConnection().prepareStatement(sqlStatement);   
                prepStmt.setString(1, address.getAddress());
                prepStmt.setInt(2, address.getCityId());
                prepStmt.setString(3, address.getPostalCode());                
                prepStmt.setString(4, address.getPhone());                
                prepStmt.setTimestamp(5, DateConversion.fromLocalTimetoUTC(LocalDateTime.now()));
                prepStmt.setString(6, LoginController.loggedInUserName);
                prepStmt.setInt(7, address.getAddressId());
                prepStmt.executeUpdate(); 
            } 
        catch (Exception ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }            
        return true;  
       
    } 
}
