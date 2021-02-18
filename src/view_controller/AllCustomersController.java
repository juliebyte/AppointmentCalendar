/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view_controller;

import DAO.AddressDaoImpl;
import DAO.AppointmentDaoImpl;
import DAO.CityDaoImpl;
import DAO.CountryDaoImpl;
import DAO.CustomerDaoImpl;
import Model.Address;
import Model.City;
import Model.Country;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import Model.Customer;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author julie
 */
public class AllCustomersController implements Initializable {
    
    Stage stage;
    Parent scene; 
    
    static Customer passCustomer;
    
    @FXML
    private TableView<Customer> customerTableView;
    @FXML
    private TableColumn<Customer, Integer> customerIdColumn;
    @FXML
    private TableColumn<Customer, String> customerNameColumn;
    @FXML
    private Button addNewCustomerButton;
    @FXML
    private Button backButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;
    
    ObservableList<Customer> Customers = FXCollections.observableArrayList();
    @FXML
    private TableColumn<?, ?> addressIdColumn;
    @FXML
    private Button exitButton;
    @FXML
    private TextField customerIdTextField;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField addressTextField;
    @FXML
    private TextField cityTextField;
    @FXML
    private TextField phoneTextField;
    @FXML
    private TextField countryTextField;
    @FXML
    private Label messageLabel;
    @FXML
    private Button detailsButton;
    @FXML
    private TextField postalCodeTextField;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {        
       
       customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
       customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
       addressIdColumn.setCellValueFactory(new PropertyValueFactory<>("addressId"));

       // Fillup the tableBox       
       refreshCustomersTableView(); 
    }    

    public static Customer getPassCustomer() {
        return passCustomer;
    }

    
    // Get the appoointments data to fill the TableBox. 
    private void refreshCustomersTableView() {    
        try {
            // Empty the TableView before loading up the customers.
            customerTableView.getItems().clear();
            Customers.addAll(CustomerDaoImpl.getAllCustomers());  
        } catch (Exception ex) {
            Logger.getLogger(AllCustomersController.class.getName()).log(Level.SEVERE, null, ex);
        }
            customerTableView.setItems(Customers);    
}
    
    @FXML
    private void onClickBack(ActionEvent event) {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        try {
            scene = FXMLLoader.load(getClass().getClassLoader().getResource("view_controller/Menu.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
        stage.setScene(new Scene(scene));
        stage.show();
    }

    

    @FXML
    private void onClickUpdate(ActionEvent event) {
        
        // Make sure a customer was selected.
        passCustomer = customerTableView.getSelectionModel().getSelectedItem();
        
        
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        try {
            scene = FXMLLoader.load(getClass().getClassLoader().getResource("view_controller/ModifyExistingCustomer.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
        stage.setScene(new Scene(scene));
        stage.show();
        
    }

    @FXML
    private void onClickDelete(ActionEvent event) {
        if(customerTableView.getSelectionModel().getSelectedItem() == null 
                || customerTableView.getSelectionModel().getSelectedIndex() == -1 ) {            
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please click on the customer you want to delete.");
            alert.setTitle("Select a Customer");
            Optional<ButtonType> result = alert.showAndWait();
            return;        
            }   
        
        Customer customerToDelete = customerTableView.getSelectionModel().getSelectedItem();
        
        // Give the user a chance to change their minds about deleting this customer. 
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete Customer");
        alert.setHeaderText("Delete " + customerToDelete.getCustomerName() + " ?");
        alert.setContentText("Are you sure you want to delete " + customerToDelete.getCustomerName() + " ?");
        Optional<ButtonType> result = alert.showAndWait();   
        if (result.isPresent() && result.get() != ButtonType.OK) {
            return;
        }       
        
        try {
            AppointmentDaoImpl.deleteAppointmentsForCustomer(customerToDelete);
        } catch (Exception ex) {
            Logger.getLogger(AllCustomersController.class.getName()).log(Level.SEVERE, null, ex);
        }        
        
        try {
            CustomerDaoImpl.deleteCustomer(customerToDelete);
        } catch (Exception ex) {
            Logger.getLogger(AllCustomersController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        refreshCustomersTableView();
        alert = new Alert(Alert.AlertType.INFORMATION, ("Customer " + customerToDelete.getCustomerName() + " has been DELETED"));
            alert.setTitle("Customer Deleted");
            result = alert.showAndWait();
            return;        
                
    }

    @FXML
    private void onClickAddNew(ActionEvent event) {
        
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        try {
            scene = FXMLLoader.load(getClass().getClassLoader().getResource("view_controller/AddNewCustomer.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
        stage.setScene(new Scene(scene));
        stage.show();
        
    }
    
   
    @FXML
    private void onClickDetails(ActionEvent event) throws Exception {        
        
        if(customerTableView.getSelectionModel().getSelectedItem() == null 
                || customerTableView.getSelectionModel().getSelectedIndex() == -1 ) {            
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please click on the customer you want to see details for.");
            alert.setTitle("Select a Customer");
            Optional<ButtonType> result = alert.showAndWait();
            return;        
            }           

        // Save the selected customer data in an object. 
        Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();        
        Address selectedAddress = AddressDaoImpl.getSpecificAddress(selectedCustomer);         
        City selectedCity = CityDaoImpl.getSpecificCity(selectedAddress);       
        Country selectedCountry = CountryDaoImpl.getSpecificCountry(selectedCity);  
        
        customerIdTextField.setText(Integer.toString(selectedCustomer.getCustomerId()));
        nameTextField.setText(selectedCustomer.getCustomerName());
        addressTextField.setText(selectedAddress.getAddress());
        cityTextField.setText(selectedCity.getCity());
        postalCodeTextField.setText(selectedAddress.getPostalCode());
        phoneTextField.setText(selectedAddress.getPhone());
        countryTextField.setText(selectedCountry.getCountry());
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
