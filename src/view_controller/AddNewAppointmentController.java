/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view_controller;

import DAO.AppointmentDaoImpl;
import static DAO.AppointmentDaoImpl.addNewAppointment;
import static DAO.AppointmentDaoImpl.getMaxAppointmentId;
import DAO.CustomerDaoImpl;
import Model.Appointment;
import Model.Customer;
import static Utilities.DateConversion.combineAppointmentDateTime;
import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;  


/**
 * FXML Controller class
 *
 * @author julie
 */
public class AddNewAppointmentController implements Initializable {
    
    Stage stage;
    Parent scene;      

    @FXML
    private TextField customerNameTextField;
    @FXML
    private TextField postalCodeTextField;
    @FXML
    private TextField countryTextField;
    
    @FXML
    private Button backButton;
    @FXML
    private Button addNewAppointmentButton;
    @FXML
    private Button exitButton;
    @FXML
    private DatePicker appointmentDatePicker;
    @FXML
    private ComboBox startTimeComboBox;    
    @FXML
    private ComboBox selectTypeComboBox;
    @FXML
    private ComboBox<Customer> selectCustomerComboBox;
    private TextField consultantName;    
    
    private final ObservableList<String> allowedTimes = FXCollections.observableArrayList("08:00 AM", "09:00 AM", "10:00 AM", "11:00 AM", "12:00 PM", "01:00 PM", "02:00 PM", "03:00 PM", "04:00 PM");
    private final ObservableList<String> allowedTypes = FXCollections.observableArrayList("Design", "Presentation", "Approval", "Scrum", "Coffee");
    private final ObservableList<String> allowedContacts = FXCollections.observableArrayList("Sonic the Hedgehog", "Tails", "Amy Rose", "Shadow", "Dr. Eggman", "Mickey Mouse", "Donald Duck", "Pluto");  
    
    @FXML
    private Label messageLabel;
    @FXML
    private ComboBox selectContactComboBox;
    

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // Found this technique at https://stackoverflow.com/questions/53592561/disable-all-dates-not-first-of-month-or-week        
        appointmentDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate appointmentDatePicker, boolean empty) {
                super.updateItem(appointmentDatePicker, empty);
                setDisable(
                    appointmentDatePicker.getDayOfWeek() == DayOfWeek.SATURDAY || 
                    appointmentDatePicker.getDayOfWeek() == DayOfWeek.SUNDAY ||
                    appointmentDatePicker.isBefore(LocalDate.now()) || empty );
                if(appointmentDatePicker.getDayOfWeek() == DayOfWeek.SATURDAY || appointmentDatePicker.getDayOfWeek() == DayOfWeek.SUNDAY || appointmentDatePicker.isBefore(LocalDate.now())) {
                    setStyle("-fx-background-color: #ffc0cb;");
                }
            }
        });        
        loadCustomerComboBox();
        loadContactsComboBox();
        loadTypeComboBox(); 
        loadStartTimeComboBox();      
        
    }   
    
    private void loadCustomerComboBox() {
        try {            
            ObservableList<Customer> customers = CustomerDaoImpl.getAllCustomers();           
            // Fill up the combo box with actual customers.
            selectCustomerComboBox.setItems(customers); 
            // Default to select the first row. 
            selectCustomerComboBox.getSelectionModel().selectFirst();             
        } catch (Exception ex) {
            Logger.getLogger(AllAppointmentsController.class.getName()).log(Level.SEVERE, null, ex);
        }       
    }
    private void loadContactsComboBox() {
        try {
            // Empty the ComboBox before loading up the allowed Contacts.             
            selectContactComboBox.getItems().clear(); 
            selectContactComboBox.getItems().addAll(allowedContacts);        
            // Default to select the first row. 
            //selectContactsComboBox.getSelectionModel().selectFirst();             
        } catch (Exception ex) {
            Logger.getLogger(AllAppointmentsController.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }    
    
    
    private void loadTypeComboBox() {
        try {
            // Empty the ComboBox before loading up the appointment types.             
            selectTypeComboBox.getItems().clear(); 
            selectTypeComboBox.getItems().addAll(allowedTypes);  
            //selectTypeComboBox.getItems().addAll("Design", "Presentation", "Approval", "Scrum", "Coffee");           
            // Default to select the first row. 
            //selectTypeComboBox.getSelectionModel().selectFirst();             
        } catch (Exception ex) {
            Logger.getLogger(AllAppointmentsController.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }    
        
    private void loadStartTimeComboBox() {
        try {
            // Empty the ComboBox before loading up the appointment start times.             
            startTimeComboBox.getItems().clear();            
            startTimeComboBox.setItems(allowedTimes);            
            // Default to select the first row. 
            //startTimeComboBox.getSelectionModel().selectFirst();
             
        } catch (Exception ex) {
            Logger.getLogger(AddNewAppointmentController.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }    
        

    @FXML
    private void onClickBack(ActionEvent event) {
         stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        try {
            scene = FXMLLoader.load(getClass().getClassLoader().getResource("view_controller/AllAppointments.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    private void onClickAdd(ActionEvent event) throws Exception {
        
        int chosenCustomerId = -1;
        String selectedContact = ""; 
        String selectedType = ""; 
        String selectedTimeHHMM = "";
        
        // Get all of the input data.
        int selectedContactIndex = selectContactComboBox.getSelectionModel().getSelectedIndex();
        int selectedTypeIndex = selectTypeComboBox.getSelectionModel().getSelectedIndex();        
        int selectedTimeIndex = startTimeComboBox.getSelectionModel().getSelectedIndex(); 
        LocalDate selectedLocalDate;
        
        if (appointmentDatePicker.getValue() != null) {
            selectedLocalDate = appointmentDatePicker.getValue(); 
        }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Missing date");
                alert.setContentText("Please choose a date for the appointment");
                alert.showAndWait(); 
                return;        
                }
                
        if (selectedContactIndex > -1 && selectedTypeIndex > -1 && selectedTimeIndex > -1) {        
            selectedContact = (String)selectContactComboBox.getValue();
            selectedType = (String)selectTypeComboBox.getValue();
            selectedTimeHHMM = (String)startTimeComboBox.getValue();
        }       
        
        if (selectCustomerComboBox.getSelectionModel().getSelectedItem() != null) {
            Customer chosenCustomer = selectCustomerComboBox.getSelectionModel().getSelectedItem();
            chosenCustomerId = chosenCustomer.getCustomerId();            
        }
        else {
            chosenCustomerId = -1; 
            messageLabel.setText("Please select a customer");
        }  
                 
     // If any of the input is missing, put up a dialog box and return. 
     if (!checkCustomer(chosenCustomerId)
             || !checkContact(selectedContactIndex) 
             || !checkType(selectedTypeIndex)
             || !checkTime(selectedTimeIndex)) {
         // Put up a dialog box showing an error adding the new appointment
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Missing input");
            alert.setContentText("Please select all data");
            alert.showAndWait(); 
            return;        
     }
      
    LocalDateTime newStartLDT = combineAppointmentDateTime(selectedLocalDate, selectedTimeHHMM);    
    LocalDateTime newEndLDT = newStartLDT.plusHours(1); 
    
    if (checkOverlappingAppointments(newStartLDT)) {
        // Put up a dialog box showing a date/time error
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Appointment not added.");
            alert.setHeaderText("Start time error");
            alert.setContentText("Time conflicts with another appointment");
            alert.showAndWait(); 
            return;       
            }
     
    // Get the last appointmentId used. Is actually just a placeholder. 
    int maxAppointmentId = Integer.parseInt(getMaxAppointmentId());
    int newAppointmentId = maxAppointmentId + 1; 
    
    Appointment newAppointment = new Appointment (newAppointmentId, chosenCustomerId, LoginController.loggedInUserId, "not needed", "not needed", "not needed", selectedContact, selectedType, newStartLDT, newEndLDT, LoginController.loggedInUserName); 
     
    boolean isAdded = addNewAppointment(newAppointment);
    
    if (!isAdded) {
       // Put up a dialog box showing an error adding the new appointment
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Update error");
            alert.setContentText("Error adding New Appointment - Appointment not added");
            alert.showAndWait();             
            }   
    else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Appointment added.");
            alert.setHeaderText("Success");
            alert.setContentText("Appointment has been added to the calendar.");
            alert.showAndWait(); 
            return;   
            }
    }

public static boolean checkCustomer(int chosenCustomerId) {
    if (chosenCustomerId > -1) {
        return true; 
    }
    else {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Missing data.");
        alert.setHeaderText("Missing Customer");
        alert.setContentText("Please Select a Customer");
        alert.showAndWait();
        return false;
    }
}



    public static boolean checkOverlappingAppointments(LocalDateTime selectedLocalDate) throws IllegalArgumentException {         
        
        // Make sure appointments do not overlap
        if (AppointmentDaoImpl.findConflictingAppointments(selectedLocalDate)) {
            return true;
        }
        else{
            return false;
        }
    }
    
    
    
    // See if a Consultant / Contact was entered
    public boolean checkContact(int selectedContactIndex) {
        
        if (selectedContactIndex == -1) {            
            return false;
            }
            else {
            return true;
            }
    }
    // Verify that a meeting type was selected    
    public boolean checkType(int selectedTypeIndex) {        
        if(selectedTypeIndex == -1) { 
            return false;
        } else {
            return true;
        }        
    }
    // Verify that a meeting time was selected    
    public boolean checkTime(int selectedTimeIndex) {        
        if(selectedTimeIndex == -1) {
            return false;
        } else {
            return true;
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
