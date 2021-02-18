/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view_controller;

import DAO.AddressDaoImpl;
import static DAO.AddressDaoImpl.getMaxAddressId;
import DAO.CityDaoImpl;
import DAO.CustomerDaoImpl;
import Model.Address;
import Model.City;
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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author julie
 */
public class AddNewCustomerController implements Initializable {
    
    Stage stage;
    Parent scene;

    @FXML
    private TextField customerNameTextField;
    @FXML
    private TextField addressTextField;
    @FXML
    private TextField phoneTextField;
    @FXML
    private Button backButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button exitButton;
    @FXML
    private ComboBox<City> cityComboBox;
    @FXML
    private TextField postalCodeTextField;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // Fill up the city combo box with all cities available in the database
        loadCityComboBox();
    }    
    
    private void loadCityComboBox() {        
        
        try {            
            ObservableList<City> cities = CityDaoImpl.getAllCities(); 
            // Fill up the combo box with available cities.
            cityComboBox.setItems(cities); 
    
        } catch (Exception ex) {
            Logger.getLogger(AddNewCustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }  
    }

    @FXML
    private void onClickBack(ActionEvent event) {        
        
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        try {
            scene = FXMLLoader.load(getClass().getClassLoader().getResource("view_controller/AllCustomers.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    private void onClickSave(ActionEvent event) throws Exception {
        
        // Edit all input fields
        // Save everything to the database
        // Put up alert box to show done.  
        
        // Get all of the input data.
        int selectedCityIndex = cityComboBox.getSelectionModel().getSelectedIndex();
        String customerNameInput = customerNameTextField.getText().trim();
        String addressInput = addressTextField.getText().trim();
        String phoneInput = phoneTextField.getText().trim();
        String postalCodeInput = postalCodeTextField.getText().trim();
        
        if (!checkCustomer(customerNameInput) 
            || !checkAddress(addressInput)
                || !checkCity(selectedCityIndex)
                || !checkPostalCode(postalCodeInput)
                || !checkPhone(phoneInput)) {
            return;
        }
        int chosenCityId = -1;
        int chosenCountryId = -1;
        int tempAddressId = 1;
        int tempCustomerId = 1;
        
        City chosenCity = cityComboBox.getSelectionModel().getSelectedItem();
        chosenCityId = chosenCity.getCityId();  
        chosenCountryId = chosenCity.getCountryId();
        
        // At this point, all input data has been verified. Add the address, then add the customer.         
        Address newAddress = new Address(tempAddressId, addressInput, chosenCity.getCityId(), postalCodeInput, phoneInput);        
        
        if (AddressDaoImpl.addNewAddress(newAddress)) {
            // Insert was successful
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error adding new Customer's address");
            alert.setHeaderText("Customer not added");
            alert.setContentText("Please try again.");
            Optional<ButtonType> result = alert.showAndWait();         
            return;
        }
        
        int maxAddressId = Integer.parseInt(getMaxAddressId());
        
        Customer newCustomer = new Customer (tempCustomerId, customerNameInput, maxAddressId);
        
        if (CustomerDaoImpl.addNewCustomer(newCustomer)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("New Customer Added");
            alert.setHeaderText("Success");
            alert.setContentText("Customer " + newCustomer.getCustomerName() + " added.");
            Optional<ButtonType> result = alert.showAndWait();         
            return;
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error adding new Customer");
            alert.setHeaderText("Customer not added");
            alert.setContentText("Please try again.");
            Optional<ButtonType> result = alert.showAndWait();         
            return;
        }
    }
    
    /**
     * Verify all input data.
     * @param customerName
     * @return 
     */
    public static boolean checkCustomer(String customerName) {
        //if (customerName != null) {
        if (!(customerName.isEmpty())) {
            return true; 
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Missing information");
            alert.setHeaderText("Enter Customer Name");
            alert.setContentText("Please enter the customer's name.");
            Optional<ButtonType> result = alert.showAndWait();         
            return false;
            }
}
       
        public static boolean checkAddress(String addressInput) {
        if (!(addressInput.isEmpty())) {
            return true; 
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Missing information");
            alert.setHeaderText("Enter Customer Address");
            alert.setContentText("Please enter the customer's address.");
            Optional<ButtonType> result = alert.showAndWait();         
            return false;
            }
}      
        
    public static boolean checkCity(int selectedCityIndex) {
    if (selectedCityIndex > -1) {
        return true; 
    }
    else {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Missing information");
        alert.setHeaderText("Select a City");
        alert.setContentText("Please select the customer's city.");
        Optional<ButtonType> result = alert.showAndWait();         
        return false;
    }
}    
    
        public static boolean checkPhone(String phoneInput) {
        if (!(phoneInput.isEmpty())) {
            return true; 
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Missing information");
            alert.setHeaderText("Enter Customer's Phone Number");
            alert.setContentText("Please enter the customer's phone number.");
            Optional<ButtonType> result = alert.showAndWait();         
            return false;
            }
    }

        
        public static boolean checkPostalCode(String postalCodeInput) {
        if (!(postalCodeInput.isEmpty())) {
            return true; 
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Missing information");
            alert.setHeaderText("Enter Customer's Postal Code");
            alert.setContentText("Please enter the customer's postal Code.");
            Optional<ButtonType> result = alert.showAndWait();         
            return false;
            }
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
