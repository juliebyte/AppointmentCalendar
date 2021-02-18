/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view_controller;

import DAO.AppointmentDaoImpl;
import DAO.CityDaoImpl;
import Model.ApptType;
import Model.CityCountry;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 * This report only shows city and country pairs. If a country exists in the database but 
 * does not currently have a city assigned, it will not appear in the report. 
 *
 * @author julie
 */
public class CityCountryReportController implements Initializable {
    
    Stage stage;
    Parent scene;

    @FXML
    private TableView<CityCountry> reportTableView;
    @FXML
    private TableColumn<CityCountry, String> cityColumn;
    @FXML
    private TableColumn<CityCountry, String> countryColumn;
    @FXML
    private Button backButton;
    @FXML
    private Button exitButton;
    ObservableList<CityCountry> CitiesCountries = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
       cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
       countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));

       // Fillup the tableBox       
       loadReportTableView(); 
    }    

    private void loadReportTableView() {
        
        try {
            // Empty the TableView before loading up the customers.
            reportTableView.getItems().clear();
            CitiesCountries.addAll(CityDaoImpl.getCitiesCountries());  
        } catch (Exception ex) {
            Logger.getLogger(AllCustomersController.class.getName()).log(Level.SEVERE, null, ex);
        }
            reportTableView.setItems(CitiesCountries);
         
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
