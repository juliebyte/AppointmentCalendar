/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Utilities.DBQuery;
import Utilities.DBConnection;
import Model.Appointment;
import Model.ApptType;
import Model.Customer;
import Utilities.DateConversion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import view_controller.LoginController;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;
import view_controller.AllAppointmentsController;

/**
 *
 * @author JulieW
 */

public class AppointmentDaoImpl {
    
     public static Appointment getSpecificAppointment(int appointmentIdInput) throws SQLException, Exception{
        // type is name or phone, value is the name or the phone #
        DBConnection.getConnection();        
        String sqlStatement = "select * FROM appointment WHERE appointmentId  = '" + appointmentIdInput + "'";  
               
        
         //  String sqlStatement="select FROM address";
        DBQuery.makeQuery(sqlStatement);
           Appointment appointmentResult;
           ResultSet resultData = DBQuery.getResult();
           while(resultData.next()){               
                int appointmentId = resultData.getInt("appointmentId");
                int customerId = resultData.getInt("customerId");
                int userId = resultData.getInt("userId");
                String title = resultData.getString("title"); 
                String description = resultData.getString("description");
                String location = resultData.getString("location");
                String contact = resultData.getString("contact");
                String type = resultData.getString("type"); 
                LocalDateTime start = DateConversion.fromUTCToLocalTime(resultData.getTimestamp("start")); 
                LocalDateTime end = DateConversion.fromUTCToLocalTime(resultData.getTimestamp("end")); 
                String createdBy = resultData.getString("createdBy"); 
                appointmentResult = new Appointment(appointmentId, customerId, userId, title, description, location, contact, type, start, end, createdBy);                
                
                return appointmentResult;
           }
        return null;
    }
     
     
     public static Appointment getUpcomingAppointment() throws SQLException, Exception{
         
        DBConnection.getConnection();
        String sqlStatement = "";        
        
        //PreparedStatement prepStmt;  
        PreparedStatement prepStmt = DBConnection.getConnection().prepareStatement(sqlStatement); 
        
        sqlStatement = "SELECT * FROM appointment WHERE start > ? and start < ?";  
        prepStmt = DBConnection.getConnection().prepareStatement(sqlStatement); 
        prepStmt.setTimestamp(1, DateConversion.fromLocalTimetoUTC(AllAppointmentsController.minStartDateTime));  
        prepStmt.setTimestamp(2, DateConversion.fromLocalTimetoUTC(AllAppointmentsController.maxStartDateTime)); 
         
        ResultSet resultData = prepStmt.executeQuery(); 
        
        Appointment appointmentResult;
           while(resultData.next()){               
                int appointmentId = resultData.getInt("appointmentId");
                int customerId = resultData.getInt("customerId");
                int userId = resultData.getInt("userId");
                String title = resultData.getString("title"); 
                String description = resultData.getString("description");
                String location = resultData.getString("location");
                String contact = resultData.getString("contact");
                String type = resultData.getString("type");
                LocalDateTime start = DateConversion.fromUTCToLocalTime(resultData.getTimestamp("start"));  
                LocalDateTime end = DateConversion.fromUTCToLocalTime(resultData.getTimestamp("end")); 
                String createdBy = resultData.getString("createdBy"); 
                appointmentResult = new Appointment(appointmentId, customerId, userId, title, description, location, contact, type, start, end, createdBy); 
                return appointmentResult;
           }
        return null;
    }
     
     
    public static ObservableList<Appointment> getAllAppointments() throws SQLException, Exception{
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();    
        DBConnection.getConnection();
            String sqlStatement = "select appointmentId, customerId, userId, title, description, location, contact, type, start, end, createdBy from appointment";         
            DBQuery.makeQuery(sqlStatement);
            ResultSet resultData = DBQuery.getResult();
             while(resultData.next()){                
                int appointmentId = resultData.getInt("appointmentId");
                int customerId = resultData.getInt("customerId");
                int userId = resultData.getInt("userId");
                String title = resultData.getString("title");
                String description = resultData.getString("description");
                String location = resultData.getString("location");
                String contact = resultData.getString("contact");
                String type = resultData.getString("type");
                LocalDateTime start = DateConversion.fromUTCToLocalTime(resultData.getTimestamp("start"));
                LocalDateTime end = DateConversion.fromUTCToLocalTime(resultData.getTimestamp("end"));
                String createdBy = resultData.getString("createdBy"); 
                Appointment appointment = new Appointment(appointmentId, customerId, userId, title, description, location, contact, type, start, end, createdBy);
                allAppointments.add(appointment);                  
            }           
        return allAppointments;
        }
    
    public static ObservableList<String> getAllConsultants() throws SQLException, Exception{
        ObservableList<String> allConsultants = FXCollections.observableArrayList();    
        DBConnection.getConnection();
            String sqlStatement = "SELECT DISTINCT contact from appointment";         
            DBQuery.makeQuery(sqlStatement);
            ResultSet resultData = DBQuery.getResult();
             while(resultData.next()){                
                String contact = resultData.getString("contact");
                allConsultants.add(contact);                  
            }           
        return allConsultants;
        }
    
    public static ObservableList<ApptType> getCountOfTypes() throws SQLException, Exception{
        ObservableList<ApptType> countOfTypes = FXCollections.observableArrayList();    
        DBConnection.getConnection();
            //String sqlStatement = "SELECT YEAR(start) AS selYear, MONTH(start) AS selMonth, type, count(*) as countType FROM appointment GROUP BY YEAR(start), MONTH(start), type; "; 
            String sqlStatement = "SELECT YEAR(start) AS year, MONTH(start) AS month, type, count(*) as countType FROM appointment GROUP BY year, month, type; ";
            DBQuery.makeQuery(sqlStatement);
            ResultSet resultData = DBQuery.getResult();
             while(resultData.next()){  
                String year = resultData.getString("year");
                String month = resultData.getString("month");
                String type = resultData.getString("type");
                int countType = (resultData.getInt("countType"));
                ApptType listOfTypes = new ApptType (year, month, type, countType); 
                countOfTypes.add(listOfTypes); 
            }          
        return countOfTypes;
        }
    
    public static String getMaxAppointmentId() throws SQLException, Exception{
        ObservableList<String> maxAppointment = FXCollections.observableArrayList();  
        String maxAppointmentId = "0";
        DBConnection.getConnection();
            String sqlStatement = "SELECT MAX(appointmentId) from appointment";         
            DBQuery.makeQuery(sqlStatement);
            ResultSet resultData = DBQuery.getResult();
             while(resultData.next()){                
                maxAppointmentId = resultData.getString("MAX(appointmentId)");  
                maxAppointment.add(maxAppointmentId);
            }           
        return maxAppointmentId;
        }
    

     public static boolean modifyExistingAppointment(Appointment appointment) throws SQLException, Exception{        
         
        boolean isModified = true;
        DBConnection.getConnection(); 
        try {
                String sqlStatement = "UPDATE appointment SET customerId = '" + appointment.getCustomerId()
                    + "', userId = '" + appointment.getUserId()
                    + "', title = '" + appointment.getTitle()
                    + "', description = '" + appointment.getDescription()
                    + "', location = '" + appointment.getLocation()
                    + "', contact = '" + appointment.getContact()
                    + "', type = '" + appointment.getType()
                    + "', start = '" + DateConversion.fromLocalTimetoUTC(appointment.getStart()) + "', end = '"
                    + DateConversion.fromLocalTimetoUTC(appointment.getEnd())
                    + "', createdBy = '" + appointment.getCreatedBy() 
                    +  "' WHERE appointmentId  = '" + appointment.getAppointmentId() + "'";  
        DBQuery.makeQuery(sqlStatement);        
            } 
      catch (Exception ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            isModified = false;
        }            
        return isModified;  
     }  
     
     

     public static boolean addNewAppointment(Appointment appointment) throws SQLException, Exception{        
         
        boolean isAdded = true;        
        DBConnection.getConnection(); 
        String sqlStatement = "";
        
        PreparedStatement prepStmt = DBConnection.getConnection().prepareStatement(sqlStatement); 
        
        sqlStatement = "INSERT INTO appointment (customerId, userId, title, description, location, contact, type, url, start, end, createDate, createdBy, lastUpdate, lastUpdateBy) Values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        prepStmt = DBConnection.getConnection().prepareStatement(sqlStatement);   
        prepStmt.setInt(1, appointment.getCustomerId());
        prepStmt.setInt(2, appointment.getUserId());
        prepStmt.setString(3, " ");
        prepStmt.setString(4, " ");
        prepStmt.setString(5, " ");
        prepStmt.setString(6, appointment.getContact());
        prepStmt.setString(7, appointment.getType());
        prepStmt.setString(8, " ");
        prepStmt.setTimestamp(9, DateConversion.fromLocalTimetoUTC(appointment.getStart()));
        prepStmt.setTimestamp(10, DateConversion.fromLocalTimetoUTC(appointment.getEnd()));
        prepStmt.setTimestamp(11, DateConversion.fromLocalTimetoUTC(LocalDateTime.now()));
        prepStmt.setString(12, appointment.getCreatedBy());
        prepStmt.setTimestamp(13, DateConversion.fromLocalTimetoUTC(LocalDateTime.now()));
        prepStmt.setString(14, LoginController.loggedInUserName);
        
        try {
        prepStmt.executeUpdate();     
        }
      catch (Exception ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            isAdded = false;
        }            
        return true;  
     }  
     
     
     public static boolean deleteAppointment(Appointment appointment) throws SQLException, Exception {
         
         DBConnection.getConnection();  
         boolean isDeleted = true;
        try { 
            String sqlStatement = "DELETE FROM appointment WHERE appointmentId = " + appointment.getAppointmentId() + ";";
            DBQuery.makeQuery(sqlStatement);       
            }
        catch (Exception ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);            
            isDeleted = false;            
        }         
        return isDeleted;        
        }       
     
     /**
      * 
      * @param deleteAppointmentsForCustomer
      * @return boolean isDeleted
      * @throws SQLException
      * @throws Exception 
      */
     public static boolean deleteAppointmentsForCustomer(Customer customer) throws SQLException, Exception {
         
         DBConnection.getConnection();  
         boolean isDeleted = true;
        try { 
            String sqlStatement = "DELETE FROM appointment WHERE customerId = " + customer.getCustomerId() + ";";
            DBQuery.makeQuery(sqlStatement);       
            }
        catch (Exception ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);            
            isDeleted = false;            
        }         
        return isDeleted;        
        }       
     
    
     public static ObservableList<Appointment> getLimitedAppointments(String consultantLimit, int customerIdLimit) throws SQLException, Exception{        
     
        ObservableList<Appointment> limitedAppointments = FXCollections.observableArrayList();    
        DBConnection.getConnection();
        String sqlStatement = "";        
        
        //PreparedStatement prepStmt;  
        PreparedStatement prepStmt = DBConnection.getConnection().prepareStatement(sqlStatement); 
        
        if (consultantLimit.equals("ALL")) {
                if (customerIdLimit > -1) {
                    sqlStatement = "SELECT * FROM appointment WHERE customerId = ? and start > ? and start < ?";
                    prepStmt = DBConnection.getConnection().prepareStatement(sqlStatement);   
                    prepStmt.setInt(1, customerIdLimit);
                    prepStmt.setTimestamp(2, DateConversion.fromLocalTimetoUTC(AllAppointmentsController.minStartDateTime));  
                    prepStmt.setTimestamp(3, DateConversion.fromLocalTimetoUTC(AllAppointmentsController.maxStartDateTime)); 
                }
                else {
                    sqlStatement = "SELECT * FROM appointment WHERE start > ? and start < ?";  
                    prepStmt = DBConnection.getConnection().prepareStatement(sqlStatement); 
                    prepStmt.setTimestamp(1, DateConversion.fromLocalTimetoUTC(AllAppointmentsController.minStartDateTime));  
                    prepStmt.setTimestamp(2, DateConversion.fromLocalTimetoUTC(AllAppointmentsController.maxStartDateTime)); 
                     }
                }
        else if (customerIdLimit > -1) {
                    sqlStatement = "SELECT * FROM appointment WHERE contact = ? AND customerId = ? and start > ? and start < ?";
                        prepStmt = DBConnection.getConnection().prepareStatement(sqlStatement); 
                        prepStmt.setString(1, consultantLimit);
                        prepStmt.setInt(2, customerIdLimit);
                        prepStmt.setTimestamp(3, DateConversion.fromLocalTimetoUTC(AllAppointmentsController.minStartDateTime));  
                        prepStmt.setTimestamp(4, DateConversion.fromLocalTimetoUTC(AllAppointmentsController.maxStartDateTime)); 
                    }
                    else {                        
                    sqlStatement = "SELECT * FROM appointment WHERE contact  = ? and start > ? and start < ?";
                    prepStmt = DBConnection.getConnection().prepareStatement(sqlStatement); 
                    prepStmt.setString(1, consultantLimit);
                    prepStmt.setTimestamp(2, DateConversion.fromLocalTimetoUTC(AllAppointmentsController.minStartDateTime));  
                    prepStmt.setTimestamp(3, DateConversion.fromLocalTimetoUTC(AllAppointmentsController.maxStartDateTime));
                    }  
                         
        ResultSet resSet = prepStmt.executeQuery();        
           
           while(resSet.next()){               
                int appointmentId = resSet.getInt("appointmentId");
                int customerId = resSet.getInt("customerId");
                int userId = resSet.getInt("userId");
                String title = resSet.getString("title"); 
                String description = resSet.getString("description");
                String location = resSet.getString("location");
                String contact = resSet.getString("contact");
                String type = resSet.getString("type");                 
                LocalDateTime start = DateConversion.fromUTCToLocalTime(resSet.getTimestamp("start"));                 
                LocalDateTime end = DateConversion.fromUTCToLocalTime(resSet.getTimestamp("end")); 
                String createdBy = resSet.getString("createdBy"); 
                
                Appointment appointmentResult = new Appointment (appointmentId, customerId, userId, title, description, location, contact, type, start, end, createdBy); 
                
                limitedAppointments.add(appointmentResult);               
           }
           return limitedAppointments;
       }  
     
     public static ObservableList<Appointment> getAppointmentsForContact(String contactLimit) throws SQLException, Exception{        
     
        ObservableList<Appointment> limitedAppointments = FXCollections.observableArrayList();    
        DBConnection.getConnection();
        String sqlStatement = "";        
        
        //PreparedStatement prepStmt;  
        PreparedStatement prepStmt = DBConnection.getConnection().prepareStatement(sqlStatement); 
            sqlStatement = "SELECT * FROM appointment WHERE contact = ?";
            prepStmt = DBConnection.getConnection().prepareStatement(sqlStatement);   
            prepStmt.setString(1, contactLimit);
                         
        ResultSet resSet = prepStmt.executeQuery(); 
           while(resSet.next()){ 
                int appointmentId = resSet.getInt("appointmentId");
                int customerId = resSet.getInt("customerId");
                int userId = resSet.getInt("userId");
                String title = resSet.getString("title"); 
                String description = resSet.getString("description");
                String location = resSet.getString("location");
                String contact = resSet.getString("contact");
                String type = resSet.getString("type");                 
                LocalDateTime start = DateConversion.fromUTCToLocalTime(resSet.getTimestamp("start"));                 
                LocalDateTime end = DateConversion.fromUTCToLocalTime(resSet.getTimestamp("end")); 
                String createdBy = resSet.getString("createdBy"); 
                
                Appointment appointmentResult = new Appointment (appointmentId, customerId, userId, title, description, location, contact, type, start, end, createdBy); 
                limitedAppointments.add(appointmentResult);               
           }
           return limitedAppointments;
       }  

    public static boolean findConflictingAppointments(LocalDateTime meetingStart) {

        boolean foundConflict = false;
        DBConnection.getConnection();
        String sqlStatement = "";            
        
        try {        
        PreparedStatement prepStmt = DBConnection.getConnection().prepareStatement(sqlStatement);         
        sqlStatement = "select appointmentId, start from appointment " +
                       "where userId = ? and start = ?";
        
            prepStmt = DBConnection.getConnection().prepareStatement(sqlStatement);
            prepStmt.setInt(1, LoginController.loggedInUserId); 
            prepStmt.setTimestamp(2, (DateConversion.fromLocalTimetoUTC(meetingStart)));
            
            ResultSet resultSet = prepStmt.executeQuery();
            if (resultSet.next()) {
                foundConflict = true;
                return foundConflict;
            } else
            {
                foundConflict = false;
                return foundConflict;
            } 
        } catch (Exception ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);            
            return false;
        }       
        }
    }
    

                







    