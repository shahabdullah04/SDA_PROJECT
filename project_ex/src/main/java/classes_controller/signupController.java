package classes_controller;


import classes_controller.Donor;
import classes_controller.Patient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;

public class signupController {

    public Label registerLabel;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private PasswordField passwordPasswordField;
    @FXML
    private PasswordField passwordPasswordField1;
    @FXML
    private TextField bloodGroupTextField;
    @FXML
    private Button backButton;
    Donor donor;
 //  public void registerButtonOnAction(ActionEvent e)
   // {
//        if(!nameTextField.getText().isBlank() && !passwordPasswordField.getText().isBlank())
//        {
//            if(passwordPasswordField1.getText().equals(passwordPasswordField.getText()))
//            {
//                registerLabel.setText("Registration Successful");
//            }
//            else
//            {
//                registerLabel.setText("Passwords do not match");
//            }
//
//        }
//        else
//        {
//            registerLabel.setText("Enter Role, Username and Password");
//        }
  // }
    public void registerDonorButtonOnAction(ActionEvent e)
    {
        Donor donor = new Donor(bloodGroupTextField.getText(),nameTextField.getText(),emailTextField.getText(),passwordPasswordField.getText());
        if(donor.registerDonor()) {
            registerLabel.setText("Registered Successfully");

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/project_ex/login.fxml"));
                Parent root = loader.load();

                loginController controller = loader.getController();
                if (controller == null) {
                    throw new RuntimeException("Failed to get controller from FXML loader");
                }

                //controller.initData(donor);

                Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }
    public void registerPatientButtonOnAction(ActionEvent e)
    {
        Patient patient = new Patient(bloodGroupTextField.getText(),nameTextField.getText(),emailTextField.getText(),passwordPasswordField.getText());
        if(patient.registerPatient())
        {
            registerLabel.setText("Registered Successfully");

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/project_ex/patientHome.fxml"));
                Parent root = loader.load();

                patientHomeController controller = loader.getController();
                if (controller == null) {
                    throw new RuntimeException("Failed to get controller from FXML loader");
                }

                controller.initData(patient);

                Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }



    }

    public void backButtonOnAction(ActionEvent e){
        /*Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
        */

        try {
            // Load the login.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/project_ex/login.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) backButton.getScene().getWindow();

            // Set the scene to the login page
            stage.setScene(new Scene(root));

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public void loginBtnOnAction(ActionEvent e)
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
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



}
