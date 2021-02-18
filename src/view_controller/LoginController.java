/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view_controller;

import static DAO.AppointmentDaoImpl.getLimitedAppointments;
import static DAO.AppointmentDaoImpl.getUpcomingAppointment;
import DAO.UserDaoImpl;
import Model.Appointment;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import Model.User;
import Utilities.ActivityLog;
import Utilities.AppointmentAlertInterface;
import Utilities.HeadingInterface;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;

/**
 * FXML Controller class
 *
 * @author julie
 */
public class LoginController implements Initializable {

    Stage stage;
    Parent scene;
    public static int loggedInUserId = 0;
    public static String loggedInUserName = "";
    
    @FXML
    private Label passwordLabel;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label messageLabel;
    @FXML
    private TextField loginIdTextField;
    @FXML
    private Label userNameLabel;
    @FXML
    private Button loginButtonLabel;
    @FXML
    private Button exitButtonLabel;
    @FXML
    private Label languageMessageLabel;
    
    private String dialogHeader;
    private String dialogTitle;
    private String dialogText;
    

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {        
        
        //Locale.setDefault(new Locale("de")); 
        // Detect the user's geographic location according to their system. 
        rb = ResourceBundle.getBundle("languages/Messages", Locale.getDefault());
        
        userNameLabel.setText(rb.getString("username"));
        passwordLabel.setText(rb.getString("password"));
        loginButtonLabel.setText(rb.getString("login"));
        exitButtonLabel.setText(rb.getString("exit"));
        messageLabel.setText(rb.getString("pleaseEnterMessage"));
        languageMessageLabel.setText(rb.getString("currentLanguage"));
        dialogHeader = rb.getString("errorHeader");
        dialogTitle = rb.getString("errorName");
        dialogText = rb.getString("invalidUserAndPassword"); 
    }    
    
    @FXML
    private void onClickLogin(ActionEvent event) throws Exception {
        
        // Get the input UserID and Password
        String userNameInput = loginIdTextField.getText().trim();
        String userPasswordInput = passwordField.getText().trim();
        User currentUser = UserDaoImpl.getSpecificUserByName(userNameInput, userPasswordInput);
        
        if ( currentUser == null) {    
            // Log the unsuccessful login attempt           
            ActivityLog.createActivityLog(userNameInput, false);
            // Put up a dialog box showing an error
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle(dialogTitle);
            alert.setHeaderText(dialogHeader);
            alert.setContentText(dialogText);
            alert.showAndWait();            
            return;
        }
        else   {          
            // Log the successful login           
            ActivityLog.createActivityLog(userNameInput, true);
            loggedInUserId = currentUser.getUserId();
            loggedInUserName = currentUser.getUserName();
            }                         
        
        try {
            checkForUpcomingAppointment();
        } catch (Exception ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        try {
            scene = FXMLLoader.load(getClass().getClassLoader().getResource("view_controller/Menu.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        stage.setScene(new Scene(scene));
        stage.show();
    }
    
    
    private void checkForUpcomingAppointment() throws Exception  {
    
        AllAppointmentsController.minStartDateTime = LocalDateTime.now();
        AllAppointmentsController.maxStartDateTime = LocalDateTime.now().plusMinutes(15);
        
        Appointment upcomingAppointment = getUpcomingAppointment(); 
        
        
        if (upcomingAppointment == null) {
            // No upcoming appointment within 15 minutes found
        }
        else { 
            LocalTime time = upcomingAppointment.getStart().toLocalTime();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Upcoming Meeting");
            alert.setHeaderText("Meeting alert");
            //alert.setContentText("You have a meeting coming up soon at " + time + " with contact / consultant " + upcomingAppointment.getContact() + ".");
            
            // Lambda used to construct heading message.
            // This is a more efficient way of constructing a message
            // because the interface can be used repeatedly.
            
            AppointmentAlertInterface apptAlertMsg2 = (msg1, msg2) -> "You have a meeting coming up soon at " + msg1 + " with contact / consultant " + msg2;
            alert.setContentText(apptAlertMsg2.apptAlertMsg(time, upcomingAppointment.getContact()));
            
            Optional<ButtonType> result = alert.showAndWait();   
            if (result.isPresent() && result.get() != ButtonType.OK) {
                return;
            }
        } 
    }
    
    
    @FXML
    private void onClickExit(ActionEvent event) {Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Close");
        alert.setHeaderText("Close Program?");
        alert.setContentText("Are you sure you want to exit?");
        Optional<ButtonType> result = alert.showAndWait();   
        if (result.isPresent() && result.get() != ButtonType.OK) {
            return;
        }
        System.exit(0); 
    }
    
}
