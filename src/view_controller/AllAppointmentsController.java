/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view_controller;

import DAO.AppointmentDaoImpl;
import DAO.CustomerDaoImpl;
import Model.Appointment;
import Model.Customer;
import Model.User;
import static Utilities.DateConversion.combineAppointmentDateTime;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import static java.time.temporal.TemporalAdjusters.nextOrSame;
import static java.time.temporal.TemporalAdjusters.previousOrSame;
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
public class AllAppointmentsController implements Initializable {
    
    Stage stage;
    Parent scene;  
    public static final DateTimeFormatter dateFormatterNoSecs = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); 
    static Appointment passAppointment; 
    public static LocalDateTime minStartDateTime;
    public static LocalDateTime maxStartDateTime;
    

    @FXML
    private TableView<Appointment> appointmentTableView;
    @FXML
    private TableColumn<Appointment, Integer> appointmentIdColumn;
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
    private Button updateButton;
    @FXML
    private Button addNewAppointmentButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button exitButton;
    @FXML
    private ToggleGroup timePeriodToggleGroup;
    @FXML
    private RadioButton allTimeRadioButton;
    @FXML
    private RadioButton monthRadioButton;
    @FXML
    private RadioButton weekRadioButton;
    @FXML
    private Button clearSelectionsButton;
    @FXML
    private ComboBox<String> selectConsultantComboBox;
    @FXML
    private ComboBox<Customer> selectCustomerComboBox;
    @FXML
    private DatePicker appointmentDatePicker;
    @FXML
    private TableColumn<Appointment, Integer> userIdColumn;
    
    ObservableList<Appointment> Appointments = FXCollections.observableArrayList();
    ObservableList<User> Users = FXCollections.observableArrayList();   
    ObservableList<Customer> Customers = FXCollections.observableArrayList();    
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
       appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
       customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
       userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
       appointmentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
       contactColumn.setCellValueFactory(new PropertyValueFactory<>("contact"));
       startColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
       endColumn.setCellValueFactory(new PropertyValueFactory<>("end"));        
       
       allTimeRadioButton.setSelected(true); 
        try {
            setDateChoice();
        } catch (ParseException ex) {
            Logger.getLogger(AllAppointmentsController.class.getName()).log(Level.SEVERE, null, ex);
        }

       // Fillup the tableBox       
       refreshAppointmentsTableView(); 
       
       //loadUserComboBox();
       loadConsultantComboBox();
       loadCustomerComboBox();      
    }   

    public static Appointment getPassAppointment() {
        return passAppointment;
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
    
    private void loadCustomerComboBox() {
        try {            
            ObservableList<Customer> customers = CustomerDaoImpl.getAllCustomers();             
            // Fill up the combo box with actual customers.
            selectCustomerComboBox.setItems(customers); 
        } catch (Exception ex) {
            Logger.getLogger(AllAppointmentsController.class.getName()).log(Level.SEVERE, null, ex);
        }       
    }
   
    // Get the appoointments data to fill the TableBox. 
    private void refreshAppointmentsTableView() {    
        try {
            // Empty the TableView before loading up the appointments.
            appointmentTableView.getItems().clear();
            Appointments.addAll(AppointmentDaoImpl.getAllAppointments());  
        } catch (Exception ex) {
            Logger.getLogger(AllAppointmentsController.class.getName()).log(Level.SEVERE, null, ex);
        }
            appointmentTableView.setItems(Appointments);    
}
    
    @FXML
    private void onClickBack(ActionEvent event) {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        try {
            scene = FXMLLoader.load(getClass().getClassLoader().getResource("view_controller/Menu.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(AllAppointmentsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    private void onClickUpdate(ActionEvent event) {
        
        if(appointmentTableView.getSelectionModel().getSelectedItem() == null 
                || appointmentTableView.getSelectionModel().getSelectedIndex() == -1 ) {            
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please click on the appointment that you want to change.");
            alert.setTitle("Select an appointment");
            Optional<ButtonType> result = alert.showAndWait();
            return;        
            }           
        
        passAppointment = appointmentTableView.getSelectionModel().getSelectedItem();  
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        try {
            scene = FXMLLoader.load(getClass().getClassLoader().getResource("view_controller/ChangeAppointment.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(AllAppointmentsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        stage.setScene(new Scene(scene));
        stage.setTitle("Update an Appointment");
        stage.show();
    }

    @FXML
    private void onClickAdd(ActionEvent event) { 
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        try {
            scene = FXMLLoader.load(getClass().getClassLoader().getResource("view_controller/AddNewAppointment.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(AllAppointmentsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        stage.setScene(new Scene(scene));
        stage.setTitle("Add an Appointment");
        stage.show();       
    }

    @FXML
    private void onClickDelete(ActionEvent event) throws Exception {         
            
        if(appointmentTableView.getSelectionModel().getSelectedItem() == null) {            
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select the appointment you want to delete.");
            alert.setTitle("Missing selection");
            //Optional<ButtonType> userResponse = alert.showAndWait();
        } else {             
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to DELETE this appointment?");
            alert.setTitle("CAUTION - DELETING");

            Optional<ButtonType> userResponse = alert.showAndWait();

            if (userResponse.isPresent() && userResponse.get() == ButtonType.OK) {
                AppointmentDaoImpl.deleteAppointment(appointmentTableView.getSelectionModel().getSelectedItem());
                
                // Refresh the tableBox contents
                refreshAppointmentsTableView();
            }        
        }                
    }

    @FXML
    private void selectByChoices(ActionEvent event) {
        
        // Clear the display of all appointments first.
        appointmentTableView.getItems().clear();
        
        
        String chosenConsultant = "";
        int chosenCustomerId = 0; 
        LocalDateTime minStartDate;
        LocalDateTime maxStartDate;
        
        if (allTimeRadioButton.isSelected()) {
            // No need for dates
        }
        else {
            if (appointmentDatePicker.getValue() != null) {
                // Dates have been successfully selected and assigned
            }
            else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Need more information");
                alert.setHeaderText("Missing selection");
                alert.setContentText("Please click on a date on the calendar.");
                Optional<ButtonType> result = alert.showAndWait();  
                return; 
            }
        }
        
        if (selectConsultantComboBox.getSelectionModel().getSelectedItem() != null) {
            chosenConsultant = selectConsultantComboBox.getSelectionModel().getSelectedItem();
        }
        else {
            chosenConsultant = "ALL";            
        }
        
        if (selectCustomerComboBox.getSelectionModel().getSelectedItem() != null) {
            Customer chosenCustomer = selectCustomerComboBox.getSelectionModel().getSelectedItem();
            chosenCustomerId = chosenCustomer.getCustomerId();            
        }
        else {
            chosenCustomerId = -1;            
        }        
            
        try {
            // Test data
            Appointments.addAll(AppointmentDaoImpl.getLimitedAppointments(chosenConsultant, chosenCustomerId));       
  
        } catch (Exception ex) {
            Logger.getLogger(AllAppointmentsController.class.getName()).log(Level.SEVERE, null, ex);
        }
            appointmentTableView.setItems(Appointments);         
        
    }

    @FXML
    private void clearSelections(ActionEvent event) {

        selectConsultantComboBox.getSelectionModel().clearSelection();
        selectCustomerComboBox.getSelectionModel().clearSelection();
        allTimeRadioButton.isSelected();
        refreshAppointmentsTableView(); 
        
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

   

    private void setMinDateChoice(ActionEvent event) throws ParseException {  
        
        setDateChoice();
    }

    @FXML
    private void setDateChoice() throws ParseException {
        
       // First and last date of week and month found on stackoverflow
        // https://stackoverflow.com/questions/3083781/start-and-end-date-of-a-current-month
        
        LocalDate selectedLocalDate;
        LocalDateTime selectedLocalDateTime;
        String selectedMinTimeHHMM = "07:59 AM";
        String selectedMaxTimeHHMM = "04:01 PM";
               
        if (allTimeRadioButton.isSelected()) {
            String str = "1900-01-01 00:00"; 
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); 
            minStartDateTime = LocalDateTime.parse(str, formatter);
            str = "2200-01-01 00:00";             
            maxStartDateTime = LocalDateTime.parse(str, formatter);
            return;
        }
        
        if (appointmentDatePicker.getValue() != null) {
            selectedLocalDate = appointmentDatePicker.getValue();
            selectedLocalDateTime = combineAppointmentDateTime(selectedLocalDate, selectedMinTimeHHMM);
        }
        else {
            return;
        }
        if (weekRadioButton.isSelected()) {
            // find the last day of previous or current Sunday and the date of the next or current Saturday            
            LocalDate first = selectedLocalDate.with(previousOrSame(DayOfWeek.SUNDAY));
            LocalDate last = selectedLocalDate.with(nextOrSame(DayOfWeek.SATURDAY));            
            minStartDateTime = combineAppointmentDateTime(first, selectedMinTimeHHMM);
            maxStartDateTime = combineAppointmentDateTime(last, selectedMaxTimeHHMM);
        }
        else
        {
            LocalDate first = selectedLocalDate.withDayOfMonth(1);
            LocalDate last = selectedLocalDate.plusMonths(1).withDayOfMonth(1).minusDays(1);
            minStartDateTime = combineAppointmentDateTime(first, selectedMinTimeHHMM);
            maxStartDateTime = combineAppointmentDateTime(last, selectedMaxTimeHHMM);
        } 
    }
}