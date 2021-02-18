/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view_controller;

import DAO.AppointmentDaoImpl;
import static DAO.AppointmentDaoImpl.modifyExistingAppointment;
import DAO.CustomerDaoImpl;
import Model.Appointment;
import Model.Customer;
import static Utilities.DateConversion.combineAppointmentDateTime;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
public class ChangeAppointmentController implements Initializable {
    
    Stage stage;
    Parent scene;
    Appointment passAppointment;
    Appointment updateAppointment;
    Customer passCustomer;

    @FXML
    private TextField customerNameTextField;
    @FXML
    private TextField postalCodeTextField;
    @FXML
    private TextField countryTextField;
    @FXML
    private ComboBox<String> selectTypeComboBox;
    @FXML
    private DatePicker appointmentDatePicker;
    @FXML
    private ComboBox<String> startTimeComboBox;
    @FXML
    private ComboBox<Customer> selectCustomerComboBox;
    @FXML
    private ComboBox<String> selectContactComboBox;
    @FXML
    private Label oldCustomerLabel;
    @FXML
    private Label oldConsultantLabel;
    @FXML
    private Label oldTypeLabel;
    @FXML
    private Label oldDateLabel;
    @FXML
    private Label oldStartLabel;
    @FXML
    private Label messageLabel;
    @FXML
    private Button backButton;
    @FXML
    private Button exitButton;
    
    private final ObservableList<String> allowedTimes = FXCollections.observableArrayList("08:00 AM", "09:00 AM", "10:00 AM", "11:00 AM", "12:00 PM", "01:00 PM", "02:00 PM", "03:00 PM", "04:00 PM");
    private final ObservableList<String> allowedTypes = FXCollections.observableArrayList("Design", "Presentation", "Approval", "Scrum", "Coffee");
    private final ObservableList<String> allowedContacts = FXCollections.observableArrayList("Sonic the Hedgehog", "Tails", "Amy Rose", "Shadow", "Dr. Eggman", "Mickey Mouse", "Donald Duck", "Pluto");  
    @FXML
    private Button saveButton;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        passAppointment = AllAppointmentsController.getPassAppointment();
        try {
            passCustomer = CustomerDaoImpl.getSpecificCustomer(passAppointment);
        } catch (Exception ex) {
            System.out.println("Error in ChangeAppointmentController  " + ex.getMessage());             
        }
        
        oldCustomerLabel.setText(passCustomer.getCustomerName());
        oldConsultantLabel.setText(passAppointment.getContact());
        oldTypeLabel.setText(passAppointment.getType());
        //oldStartLabel.setText(toString(passAppointment.getStart()));
        //oldDateLabel.setText = dateConversion.something
        
        LocalDateTime dateTime = passAppointment.getStart();
        LocalDate date = dateTime.toLocalDate();
        LocalTime time = dateTime.toLocalTime();
        oldStartLabel.setText(dateTime.toLocalTime().toString());
        oldDateLabel.setText(dateTime.toLocalDate().toString());
        
        
        //appointmentDatePicker.setValue(LocalDate.MIN);
        
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
        
       loadConsultantComboBox();
       loadCustomerComboBox(); 
       loadTypeComboBox();
       loadStartTimeComboBox();
    }    

    private void loadConsultantComboBox() {
        try {
            // Empty the ComboBox before loading up the consultants (use "contact" column). 
            ObservableList<String> allConsultants = AppointmentDaoImpl.getAllConsultants();           
            // Fill up the rest of the combo box with unique consultants (contacts).
            selectContactComboBox.setItems(allConsultants); 
        } catch (Exception ex) {
            Logger.getLogger(AllAppointmentsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    private void loadCustomerComboBox() {
        try {            
            ObservableList<Customer> customers = CustomerDaoImpl.getAllCustomers();             
            // Fill up the combo box with actual customers.
            selectCustomerComboBox.setItems(customers); 
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
    private void onClickSave(ActionEvent event) throws ParseException, Exception {
        
        // See if any fields were changed. 
        // If a column is not changed, use the previous value. 
        // Validate the date and time picked for not a duplicate time slot.
        // If ok, save the changes. 
        
        
        String selectedTimeHHMM = "";
        
        
        // Get all of the input data.
        int selectedContactIndex = selectContactComboBox.getSelectionModel().getSelectedIndex();
        int selectedTypeIndex = selectTypeComboBox.getSelectionModel().getSelectedIndex();        
        int selectedTimeIndex = startTimeComboBox.getSelectionModel().getSelectedIndex(); 
        LocalDate selectedLocalDate; 
        
        boolean needUpdate = false; 
        
        // initialize the updateAppointment object to be the same as the original object. 
        updateAppointment = passAppointment;
        
        if (checkContact(selectedContactIndex) 
                || checkType(selectedTypeIndex)) {
               needUpdate = true;
            }
                
        // If either date or time was entered, require both. 
        // Then pass both into the date routine to get LocalDateTime.  
        
        if (selectedTimeIndex > -1 && appointmentDatePicker.getValue() != null) {
            // Both date and time were chosen
            // combine them into LocalDateTime for updating
            needUpdate = true;
        } 
        else {
            if (selectedTimeIndex == -1 && appointmentDatePicker.getValue() != null) { 
                // Error message please pick the appointment date
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Missing input data.");
                alert.setContentText("Please choose the appointment time, too.");
                alert.showAndWait(); 
                return;
            }
           else {
            if (selectedTimeIndex > -1 && appointmentDatePicker.getValue() == null) { 
                // Error message please pick the appointment time
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Missing input data.");
                alert.setContentText("Please choose the appointment date, too.");
                alert.showAndWait();  
                return;
            } 
            else {
                // No change to date and time of the appointment.
                }
            }
        }
                  
        if (selectedTimeIndex > -1) {
            selectedTimeHHMM = (String)startTimeComboBox.getValue();
            needUpdate = true;
        }
        
        if (selectCustomerComboBox.getSelectionModel().getSelectedItem() != null) {
            Customer chosenCustomer = selectCustomerComboBox.getSelectionModel().getSelectedItem();
            passAppointment.setCustomerId(chosenCustomer.getCustomerId()); 
            needUpdate = true;
        }
        
     // If any of the input is missing, put up a dialog box and return. 
     if (!needUpdate) {
         // Put up a dialog box showing an error adding the new appointment
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No data was changed." );
            alert.setHeaderText("No changes were requested.");
            alert.setContentText("No changes were requested.");
            alert.showAndWait(); 
            return;        
     }
      
        
        
    if (appointmentDatePicker.getValue() != null) {
            selectedLocalDate = appointmentDatePicker.getValue(); 
            needUpdate = true; 
            LocalDateTime newStartLDT = combineAppointmentDateTime(selectedLocalDate, selectedTimeHHMM);    
            LocalDateTime newEndLDT = newStartLDT.plusHours(1); 
            
            if (checkOverlappingAppointments(newStartLDT)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Appointment not updated.");
                    alert.setHeaderText("Start time error");
                    alert.setContentText("Time conflicts with another appointment");
                    alert.showAndWait(); 
                    return;       
                    }
            else {
                updateAppointment.setStart(newStartLDT);
                updateAppointment.setEnd(newEndLDT);
            }
       
    }
     
        if (modifyExistingAppointment(updateAppointment)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Update successful");
            alert.setHeaderText("Appointment updated");
            alert.setContentText("Appointment has been updated");
            alert.showAndWait(); 
            return;
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Update error");
            alert.setContentText("Error Updating Appointment - Appointment not updated");
            alert.showAndWait(); 
            return;
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
    
    
    /**
     * Check to see if the contact was changed. If it was, put the new contact 
     * into the updateAppointment object and return true meaning yes an update 
     * to the appointment table is required.
     * @param selectedContactIndex
     * @return 
     */
    // See if a Consultant / Contact was entered
    public boolean checkContact(int selectedContactIndex) { 
        
        if (selectedContactIndex > -1) {            
            updateAppointment.setContact(selectContactComboBox.getValue());
            return true;
        }
        else {
            // contact wasn't changed so don't change updateAppointment
            return false;
        }
    }
    /**
     * Check to see if the type was changed. If it was, put the new type 
     * into the updateAppointment object and return true, meaning yes an update 
     * to the appointment table is required.
     * @param selectedTypeIndex
     * @return 
     */
    // Verify that a meeting type was selected    
    public boolean checkType(int selectedTypeIndex) {        
                
        if (selectedTypeIndex > -1) {
            updateAppointment.setType(selectTypeComboBox.getValue());
            return true;
        }
        else {
            // type wasn't changed so don't change updateAppointment
            return false;
        }
    }
    
    @FXML
    private void onClickBack(ActionEvent event) {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        try {
            scene = FXMLLoader.load(getClass().getClassLoader().getResource("view_controller/AllAppointments.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(ChangeAppointmentController.class.getName()).log(Level.SEVERE, null, ex);
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
