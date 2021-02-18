/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view_controller;

import DAO.AppointmentDaoImpl;
import Model.Appointment;
import Model.Customer;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import static view_controller.AllAppointmentsController.passAppointment;

/**
 * FXML Controller class
 *
 * @author julie
 */
public class ScheduleReportMenuController implements Initializable {
    
    Stage stage;
    Parent scene;
    static String passContact; 

    @FXML
    private ComboBox<String> selectConsultantComboBox;
    @FXML
    private Button goToReportButton;
    @FXML
    private Button backButton;
    @FXML
    private Button exitButton;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        loadConsultantComboBox();
    }
   public static String getPassContact() {
        return passContact;
    }
     

    private void loadConsultantComboBox() {
        try {
            // Empty the ComboBox before loading up the consultants (use "contact" column). 
            ObservableList<String> allConsultants = AppointmentDaoImpl.getAllConsultants();           
            // Fill up the rest of the combo box with unique consultants (contacts).
            selectConsultantComboBox.setItems(allConsultants); 
        } catch (Exception ex) {
            Logger.getLogger(AllAppointmentsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
    
    
    @FXML
    private void goToReport(ActionEvent event) {
        
        
         if (selectConsultantComboBox.getSelectionModel().getSelectedItem() != null) {
            passContact = selectConsultantComboBox.getSelectionModel().getSelectedItem();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a consultant to see their schedule.");
            alert.setTitle("Select a consultant");
            Optional<ButtonType> result = alert.showAndWait();
            return;                    
        }
        
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        try {
            scene = FXMLLoader.load(getClass().getClassLoader().getResource("view_controller/ScheduleReport.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(AllAppointmentsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        stage.setScene(new Scene(scene));
        stage.setTitle("Report: Consultant Schedule");
        stage.show();
    }

    @FXML
    private void onClickBack(ActionEvent event) {
        
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        try {
            scene = FXMLLoader.load(getClass().getClassLoader().getResource("view_controller/ReportMenu.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    private void onClickExit(ActionEvent event) {
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
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
