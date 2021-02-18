/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view_controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author julie
 */
public class MenuController implements Initializable {    

    Stage stage;
    Parent scene;
    @FXML
    private Button goToAppointmentsButton;
    @FXML
    private Label shortAppointmentLabel;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void onClickGoToCustomers(ActionEvent event) {        
        
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        try {
            scene = FXMLLoader.load(getClass().getClassLoader().getResource("view_controller/AllCustomers.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
        stage.setScene(new Scene(scene));
        stage.show();
        
    }

    @FXML
    private void onClickGoToReports(ActionEvent event) {
        
        
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        try {
            scene = FXMLLoader.load(getClass().getClassLoader().getResource("view_controller/ReportMenu.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
        stage.setScene(new Scene(scene));
        stage.show(); 
    }

    @FXML
    private void onClickBack(ActionEvent event) {
        
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        try {
            scene = FXMLLoader.load(getClass().getClassLoader().getResource("view_controller/Login.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
        stage.setScene(new Scene(scene));
        stage.show();        
    }
    
    @FXML
    private void onClickGoToAppointments(ActionEvent event) {
        
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        try {
            scene = FXMLLoader.load(getClass().getClassLoader().getResource("view_controller/AllAppointments.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
        stage.setScene(new Scene(scene));
        stage.show();
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
