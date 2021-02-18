/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view_controller;

import DAO.AppointmentDaoImpl;
import DAO.CustomerDaoImpl;
import Model.Appointment;
import Model.ApptType;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author julie
 */
public class TypeReportController implements Initializable {
    
    Stage stage;
    Parent scene;

    private TableColumn<ApptType, String> appointmentTypeColumn;
    @FXML
    private TableColumn<ApptType, Integer> countColumn;
    @FXML
    private Button backButton;
    @FXML
    private Button exitButton;
    @FXML
    private TableColumn<ApptType, String> yearColumn;
    @FXML
    private TableColumn<ApptType, String> monthColumn;
    @FXML
    private Button refreshButton;
    @FXML
    private TableView<ApptType> typeTableView;
    @FXML
    private TableColumn<ApptType, String> typeColumn;
    
    ObservableList<ApptType> ApptTypes = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
       yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
       monthColumn.setCellValueFactory(new PropertyValueFactory<>("month"));
       typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
       countColumn.setCellValueFactory(new PropertyValueFactory<>("countType"));

       // Fillup the tableBox       
       loadTypeTableView(); 
       
    }    


    private void loadTypeTableView() {
        
        try {
            // Empty the TableView before loading up the customers.
            typeTableView.getItems().clear();
            ApptTypes.addAll(AppointmentDaoImpl.getCountOfTypes());  
        } catch (Exception ex) {
            Logger.getLogger(AllCustomersController.class.getName()).log(Level.SEVERE, null, ex);
        }
            typeTableView.setItems(ApptTypes);
         
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

    @FXML
    private void refreshTypeTableView(ActionEvent event) {
        
        loadTypeTableView(); 
    }
    
}
