/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view_controller;

import DAO.AddressDaoImpl;
import DAO.CityDaoImpl;
import DAO.CountryDaoImpl;
import static DAO.CountryDaoImpl.getSpecificCountry;
import DAO.CustomerDaoImpl;
import Model.Address;
import Model.City;
import Model.Country;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author julie
 */
public class ModifyExistingCustomerController implements Initializable {
    
    Stage stage;
    Parent scene;
    Customer passCustomer;
    Address passAddress;
    City passCity;
    Country passCountry;
    

    @FXML
    private Label customerIdLabel;
    @FXML
    private Label MessageLabel;
    @FXML
    private TextField addressTextField;
    @FXML
    private TextField postalCodeTextField;
    @FXML
    private TextField phoneTextField;
    @FXML
    private TextField customerNameTextField;
    @FXML
    private ComboBox<City> cityComboBox;
    @FXML
    private Button backButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button exitButton;
    @FXML
    private Label cityLabel;
    @FXML
    private TextField countryTextField;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        passCustomer = AllCustomersController.getPassCustomer();
        
        customerIdLabel.setText(String.valueOf(passCustomer.getCustomerId()));
        customerNameTextField.setText(passCustomer.getCustomerName());
        
        try {
            passAddress = AddressDaoImpl.getSpecificAddress(passCustomer);
        } catch (Exception ex) {
            System.out.println("Error in ModifyExistingCustomer  " + ex.getMessage());       
            return;            
        }        
        
        addressTextField.setText(passAddress.getAddress());
        postalCodeTextField.setText(passAddress.getPostalCode());
        phoneTextField.setText(passAddress.getPhone());
        
        try {
            passCity = CityDaoImpl.getSpecificCity(passAddress);            
        } catch (Exception ex) {
            Logger.getLogger(ModifyExistingCustomerController.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        cityLabel.setText("City " + passCity.getCity());
        loadCityComboBox(passCity);
                
        try {
            passCountry = CountryDaoImpl.getSpecificCountry(passCity);            
        } catch (Exception ex) {
            Logger.getLogger(ModifyExistingCustomerController.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        countryTextField.setText(passCountry.getCountry());        
    }    
    
    private void loadCityComboBox(City passCity) {        
        
        try {            
            ObservableList<City> cities = CityDaoImpl.getAllCities(); 
                        
            // Fill up the combo box with available cities.
            cityComboBox.setItems(cities); 
            cityComboBox.getSelectionModel().select(passCity);
            
        } catch (Exception ex) {
            Logger.getLogger(ModifyExistingCustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }  
    }


    @FXML
    private void onClickBack(ActionEvent event) throws IOException  {
        
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getClassLoader().getResource("view_controller/AllCustomers.fxml"));        
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    private void onClickSave(ActionEvent event) throws Exception {
       
        int selectedCityIndex = cityComboBox.getSelectionModel().getSelectedIndex();
        //int selectedCountryIndex = countryComboBox.getSelectionModel().getSelectedIndex();
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
        //int chosenCountryId = -1;
        //int tempAddressId = 1;
        //int tempCustomerId = 1;
        
        City chosenCity = cityComboBox.getSelectionModel().getSelectedItem();
        chosenCityId = chosenCity.getCityId(); 
        
        if (addressInput.equals(passAddress.getAddress()) 
                && phoneInput.equals(passAddress.getAddress())
                && postalCodeInput.equals(passAddress.getPostalCode())
                && chosenCityId == passAddress.getCityId()
                && customerNameInput.equals(passCustomer.getCustomerName()))  {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No changes were requested");
            alert.setHeaderText("No changes made");
            alert.setContentText("No changes were requested.");
            Optional<ButtonType> result = alert.showAndWait();         
            return;
            }
        
        
        // At this point, all input data has been verified. Update the address, then update the customer. 
        Address changedAddress = new Address(passAddress.getAddressId(), addressInput, chosenCityId, postalCodeInput, phoneInput);        
        
        if (AddressDaoImpl.updateAddress(changedAddress)) {
            // Update was successful
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Customer's address changed");
            alert.setHeaderText("Success");
            alert.setContentText("Customer's address was updated.");
            Optional<ButtonType> result = alert.showAndWait();         
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error updating Customer's address");
            alert.setHeaderText("Address not updated");
            alert.setContentText("Please try again.");
            Optional<ButtonType> result = alert.showAndWait();         
            return;
        }
        
        if (customerNameInput.equals(passCustomer.getCustomerName()))  {
            // Customer name was not changed so no need to update the customer record in customer table. 
        }
        else {  
            Customer updateCustomer = new Customer (passCustomer.getCustomerId(), customerNameInput, passCustomer.getAddressId());
            if (CustomerDaoImpl.updateCustomer(updateCustomer)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Customer name changed");
                alert.setHeaderText("Success");
                alert.setContentText("Customer name changed from " + passCustomer.getCustomerName() + " to " + updateCustomer.getCustomerName());
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
            
/**
 * Every time a city in the cityComboBox is clicked, this is triggered to get the 
 * corresponding country to display. 
 * @param event
 * @throws Exception 
 */
    @FXML
    private void onClickCityPopulateCountry(ActionEvent event) throws Exception {
        
        City chosenCity = cityComboBox.getSelectionModel().getSelectedItem();
        int chosenCityId = chosenCity.getCityId();         
        Country chosenCountry = getSpecificCountry(chosenCity);        
        countryTextField.setText(chosenCountry.getCountry());
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