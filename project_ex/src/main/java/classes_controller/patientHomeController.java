package classes_controller;


import classes_controller.Appointment;
import classes_controller.Patient;
import classes_controller.Request;
//import com.sun.javafx.binding.DoubleConstant;
//import com.sun.net.httpserver.Headers;

//import com.sun.net.httpserver.Request;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.DatabaseConnection;


//import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.sql.*;
import java.time.LocalDate;


public class patientHomeController {


    private Patient patient;
    @FXML
    private Button laterButton;
    @FXML
    private DatePicker requestDatePicker;
    @FXML
    private AnchorPane RequestBloodAnchor;
    @FXML
    private AnchorPane ViewRequestsAnchor;
    @FXML
    private AnchorPane ScheduleAnchor;
    @FXML
    private AnchorPane UrgentRequestAnchor;
    @FXML
    private Label messageLabel;
    @FXML
    private TextField quantityTextField;
    @FXML
    private TextField quantityTextField1;
    @FXML
    private Label welcomeLabel1;
    @FXML
    private Label messageLabel1;
    @FXML
    private TableView<Request> viewRequestTable;
    @FXML
    private TableColumn<Request, LocalDate> dateColumn;
    @FXML
    private TableColumn<Request, String> statusColumn;
    @FXML
    private TableColumn<Request, Integer> quantityColumn;
    @FXML
    private TableColumn<Request, String> bloodGroupColumn;



    public void initData(Patient patient) {
        this.patient = patient;
        welcomeLabel1.setText("Welcome, " + patient.getName());
    }
    public void requestButtonOnAction(ActionEvent e){

        RequestBloodAnchor.setVisible(true);
        ViewRequestsAnchor.setVisible(false);
        ScheduleAnchor.setVisible(false);
        UrgentRequestAnchor.setVisible(false);

    }



    //-------------First Page of Patient--------------

    public void urgentButtonOnAction (ActionEvent e){

        RequestBloodAnchor.setVisible(false);
        ViewRequestsAnchor.setVisible(false);
        ScheduleAnchor.setVisible(false);
        UrgentRequestAnchor.setVisible(true);
    }

    public void laterButtonOnAction (ActionEvent e){

        RequestBloodAnchor.setVisible(false);
        ViewRequestsAnchor.setVisible(false);
        ScheduleAnchor.setVisible(true);
        UrgentRequestAnchor.setVisible(false);

    }

    public boolean checkIfExistsInDatabase(LocalDate date)
    {
        String query = "SELECT COUNT(*) FROM request WHERE date = ?";
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connection = connectNow.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setDate(1, Date.valueOf(date));
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

    //-----------------Urgent Blood Request---------------


    public void confirmButtonOnAction(ActionEvent e)
    {
        LocalDate currentDate = LocalDate.now();

        if (quantityTextField.getText().isEmpty()) {
            messageLabel.setText("Please enter the quantity.");
        } else
        {
            int quantity = Integer.parseInt(quantityTextField.getText());
            Request request = new Request(currentDate, patient, quantity);
            request.setRequest("urgent");

            messageLabel.setText("Blood will be Received Shortly");
        }


    }




    //-----------------Schedule Blood Request---------------

    public void confirm1ButtonOnAction(ActionEvent e)
    {
        LocalDate date = requestDatePicker.getValue();

        if(quantityTextField1.getText().isEmpty())
        {
            messageLabel1.setText("Please enter the quantity.");
        } else{
            int quantity = Integer.parseInt(quantityTextField1.getText());
            Request request = new Request(date, patient, quantity);
            request.setRequest("pending");
            messageLabel1.setText("Blood Will Be Received Soon");
        }
    }



    //-------------- Left Anchor Buttons --------------------

    public void viewRequestButtonOnAction(ActionEvent e){

        dateColumn.setCellValueFactory(new PropertyValueFactory<>("requestDate"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        bloodGroupColumn.setCellValueFactory(new PropertyValueFactory<>("bloodGroup"));

        RequestBloodAnchor.setVisible(false);
        ViewRequestsAnchor.setVisible(true);
        ScheduleAnchor.setVisible(false);
        UrgentRequestAnchor.setVisible(false);
        Request request = new Request();
        ObservableList<Request> list = request.getRequestsList(patient.getPatientID());
        viewRequestTable.setItems(list);

    }
    public void logoutButtonOnAction(ActionEvent e)
    {/*
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
        }*/
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
