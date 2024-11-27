package classes_controller;


import classes_controller.Appointment;
import classes_controller.Donor;
import classes_controller.Inventory;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.event.ActionEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import static java.sql.DriverManager.getConnection;

public class donorHomeController {
    private Donor donor;
    @FXML
    private DatePicker appointmentDatePicker;
    @FXML
    private Label welcomeLabel;
    @FXML
    private TextField quantityTextField;
    @FXML
    private Label messageLabel;
    @FXML
    private AnchorPane ScheduleAppointmentAnchor;
    @FXML
    private AnchorPane ViewHistoryAnchor;
    @FXML
    private TableView<Appointment> viewAppointmentsTable;
    @FXML
    private TableColumn<Appointment, LocalDate> dateColumn;
    @FXML
    private TableColumn<Appointment, Integer> quantityColumn;

    public void initData(Donor donor) {
        this.donor = donor;
        welcomeLabel.setText("Welcome, " + donor.getName());
    }
    public void scheduleButtonOnAction(ActionEvent e)
    {
        ScheduleAppointmentAnchor.setVisible(true);
        ViewHistoryAnchor.setVisible(false);

    }
    public void viewHistoryButtonOnAction(ActionEvent e)
    {
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        ScheduleAppointmentAnchor.setVisible(false);
        ViewHistoryAnchor.setVisible(true);

        Appointment appointment = new Appointment();
        ObservableList<Appointment> list = appointment.getAppointmentsList(donor.getDonorID());
        viewAppointmentsTable.setItems(list);


    }
    public boolean checkAvailabilityButtonOnAction(ActionEvent e)
    {
        LocalDate date = appointmentDatePicker.getValue();
        if(!checkIfExistsInDatabase(date))
        {
            messageLabel.setText("Date is available. You may confirm.");
            messageLabel.setVisible(true);
            return true;
        }
        else {
            messageLabel.setText("You already have an appointment on that date. Please choose another date.");
            messageLabel.setVisible(true);
            return false;
        }



    }
    public boolean checkIfExistsInDatabase(LocalDate date)
    {
        String query = "SELECT COUNT(*) FROM appointment WHERE date = ?";
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connection = connectNow.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setDate(1,Date.valueOf(date));
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next())
            {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;

    }

    public void confirmButtonOnAction(ActionEvent e) {
        LocalDate date = appointmentDatePicker.getValue();
        int quantity = Integer.parseInt(quantityTextField.getText());
        Appointment appointment = new Appointment(date,donor,quantity);
        if(appointment.setAppointment())
        {
            Inventory inventory = new Inventory();
            if(inventory.addToInventory(appointment))
            {
                System.out.println("added in inventory");
            }
            messageLabel.setText("Appointment has been set.");
        };

    }
    public void logoutButtonOnAction(ActionEvent e)
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/project_ex/login.fxml"));
            Parent root = loader.load();

            loginController controller = loader.getController();
            if (controller == null) {
                throw new RuntimeException("Failed to get controller from FXML loader");
            }


            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


}
