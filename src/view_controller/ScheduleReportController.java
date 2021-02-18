/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view_controller;

import DAO.AppointmentDaoImpl;
import Model.Appointment;
import Utilities.HeadingInterface;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author julie
 */
public class ScheduleReportController implements Initializable {
    
    Stage stage;
    Parent scene;
    String passContact;

    @FXML
    private Label titleLabel;
    @FXML
    private TableView<Appointment> reportTableView;
    @FXML
    private TableColumn<Appointment, String> appointmentTypeColumn;
    @FXML
    private TableColumn<Appointment, Integer> customerIdColumn;
    @FXML
    private TableColumn<Appointment, String> contactColumn;
    @FXML
    private TableColumn<Appointment, String> startColumn;
    @FXML
    private TableColumn<Appointment, String> endColumn;
    @FXML
    private Button backButton;
    @FXML
    private Button exitButton;
    
    ObservableList<Appointment> Appointments = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
       appointmentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
       customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
       contactColumn.setCellValueFactory(new PropertyValueFactory<>("contact"));
       startColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
       endColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
       
       passContact = ScheduleReportMenuController.getPassContact();       

       // Fillup the tableBox       
       loadReportTableView(); 
        
    }   
    
    private void loadReportTableView() {
        
        try {
            // Empty the TableView before loading up the customers.
            reportTableView.getItems().clear();
            Appointments.addAll(AppointmentDaoImpl.getAppointmentsForContact(passContact));  
        } catch (Exception ex) {
            Logger.getLogger(AllCustomersController.class.getName()).log(Level.SEVERE, null, ex);
        }
            reportTableView.setItems(Appointments);
           
            // Lambda used to construct heading message.
            // This is a more efficient way of constructing a message
            // because the interface can be used repeatedly.
            HeadingInterface message = msg -> "Schedule for consultant " + msg; 
            titleLabel.setText(message.getMessage(passContact));
    }
    

    @FXML
    private void onClickBack(ActionEvent event) {        
        
        
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        try {
            scene = FXMLLoader.load(getClass().getClassLoader().getResource("view_controller/ScheduleReportMenu.fxml"));
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
