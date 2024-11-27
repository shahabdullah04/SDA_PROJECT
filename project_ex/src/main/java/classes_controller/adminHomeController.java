package classes_controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

//import javax.swing.*;
import java.io.IOException;
import java.time.LocalDate;

public class adminHomeController {

    Admin admin;
    @FXML
    private Label welcomeLabel;
    @FXML
    private Label promptLabel;
    @FXML
    private Label deleteLabel;
    @FXML
    private Label promptLabel2;

    @FXML
    private Label requestLabel;
    @FXML
    private AnchorPane addUserAnchor;
    @FXML
    private AnchorPane manageUsersAnchor;
    @FXML
    private AnchorPane viewTableAnchor;
    @FXML
    private AnchorPane manageRequestsAnchor;
    @FXML
    private AnchorPane manageInventoryAnchor;
    @FXML
    private AnchorPane deleteUserAnchor;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private TextField roleTextField;
    @FXML
    private TextField bloodGroupTextField;
    @FXML
    private TextField requestTextField;
    @FXML
    private TextField deleteTextField;
    @FXML
    private TextField inventoryIdTextField;
    @FXML
    private TextField quantityTextField;
    @FXML
    private TableView viewUsersTable;
    /*
    @FXML
    private TableColumn<User, String> nameColumn;
    @FXML
    private TableColumn<User, String> emailColumn;
    @FXML
    private TableColumn<User, String> passwordColumn;
    @FXML
    private TableColumn<User, String> roleColumn;
    @FXML
    private TableColumn<User, Integer> userIdColumn;*/

    @FXML
    private TableColumn<User, String> nameColumn;

    @FXML
    private TableColumn<User, String> emailColumn;

    @FXML
    private TableColumn<User, String> passwordColumn;

    @FXML
    private TableColumn<User, String> roleColumn;

    @FXML
    private TableColumn<User, Integer> userIdColumn;

    @FXML
   private TableView<Request> viewRequestTable;
    @FXML
    private TableColumn<Request,Integer> patientIDColumn;
    @FXML
    private TableColumn<Request,Integer> requestIdColumn;
    @FXML
    private TableColumn<Request, LocalDate> dateColumn;
    @FXML
    private TableColumn<Request, String> statusColumn;
    @FXML
    private TableColumn<Request, Integer> quantityColumn;
    @FXML
    private TableColumn<Request, String> bloodGroupColumn;
    @FXML
    private TableView<Inventory> inventoryTable;
    @FXML
    private TableColumn<Request,Integer> inventoryIdColumn;
    @FXML
    private TableColumn<Request, Integer> quantityColumn1;
    @FXML
    private TableColumn<Request, String> bloodGroupColumn1;
/*
*/



    public void initData(Admin admin) {
        this.admin = admin;
        welcomeLabel.setText("Welcome, " + admin.getName());
    }

    // ----------------- Manage Users ----------------------
    public void manageUsersButtonOnAction(ActionEvent e)
    {
        manageUsersAnchor.setVisible(true);
        addUserAnchor.setVisible(false);
        viewTableAnchor.setVisible(false);
        manageInventoryAnchor.setVisible(false);
        manageRequestsAnchor.setVisible(false);
        deleteUserAnchor.setVisible(false);

    }

    public void addUserButtonOnAction(ActionEvent e)
    {
        addUserAnchor.setVisible(true);
        viewTableAnchor.setVisible(false);
        deleteUserAnchor.setVisible(false);

    }

    public void deleteUserButtonOnAction(ActionEvent actionEvent) {

        manageUsersAnchor.setVisible(true);
        addUserAnchor.setVisible(false);
        viewTableAnchor.setVisible(false);
        manageRequestsAnchor.setVisible(false);
        deleteUserAnchor.setVisible(true);
        deleteLabel.setText("");


    }


    public void viewUsersButtonOnAction(ActionEvent e)
    {
        // Show the correct anchor and hide others
        viewTableAnchor.setVisible(true);
        addUserAnchor.setVisible(false);
        deleteUserAnchor.setVisible(false);

        // Set up column properties
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));

        // Fetch data and populate the TableView
        User userInstance = new User();
        ObservableList<User> userList = userInstance.getAllUsers();

        // Debugging: Print fetched data in the console
        if (userList == null || userList.isEmpty()) {
            System.out.println("No users found.");
        } else {
            for (User user : userList) {
                System.out.println("User: " + user.getName() + ", " + user.getEmail() + ", "+user.getPassword()+" ,"+user.getRole()+" , "+user.getUserId());
            }
        }

        // Set the items to the TableView
       viewUsersTable.setItems(userList);

        // Refresh the TableView to ensure it updates
       // viewUsersTable.refresh();
    }




/*
    public void viewUsersButtonOnAction(ActionEvent e)
    {
        viewTableAnchor.setVisible(true);
        addUserAnchor.setVisible(false);
        deleteUserAnchor.setVisible(false);


        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));

        User users = new User();
        ObservableList<User> list = users.getAllUsers();
        viewUsersTable.setItems(list);
    }

*/

/*
    @FXML
    public void viewUsersButtonOnAction(ActionEvent e) {
        // Show the table and hide other anchors
        viewTableAnchor.setVisible(true);
        addUserAnchor.setVisible(false);
        deleteUserAnchor.setVisible(false);

        // Ensure the TableColumn properties are correctly linked to the User class getters
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));

        // Fetch the data and populate the TableView
        User userInstance = new User();
        ObservableList<User> userList = userInstance.getAllUsers();

        if (userList == null || userList.isEmpty()) {
            System.out.println("No users found to display.");
        } else {
            viewUsersTable.setItems(userList); // Populate TableView
        }

        // Refresh the table to ensure the data is shown
        viewUsersTable.refresh();
    }*/

   /* public void removeUserButtonOnAction(ActionEvent actionEvent)
    {
        String userIdText = deleteTextField.getText();
        if (userIdText.isEmpty())
        {
            deleteLabel.setText("Field is empty. Please Enter the user ID");
        }

        try {
            int userId = Integer.parseInt(userIdText);
           // System.out.println(userIdText);

            String role = admin.getUserRole(userId);
            System.out.println("@@Role "+ role);
           *//* if (role.equals("donor")) {
                admin.deleteAppointmentByUserId(userId);
                admin.deleteDonorByUserId(userId);
            } else if (role.equals("patient"))
            {
                admin.deleteRequestByUserId(userId);
                admin.deletePatientByUserId(userId);
            }

            boolean deletedFromUsers = admin.deleteUser(userId);

            if (deletedFromUsers) {
                if (role.equals("donor")) {
                    deleteLabel.setText("Deleted donor");
                } else if (role.equals("patient")) {
                    deleteLabel.setText("Deleted patient");
                }
            } else {
                deleteLabel.setText("Failed to delete user with ID " + userId);
            }

           *//*

        } catch (NumberFormatException e) {
            deleteLabel.setText("This User doesn't Exist.");
        }


    }*/

    public void removeUserButtonOnAction(ActionEvent actionEvent) {
        String userIdText = deleteTextField.getText();
        if (userIdText.isEmpty()) {
            deleteLabel.setText("Field is empty. Please enter the user ID.");
            return;
        }

        try {
            int userId = Integer.parseInt(userIdText);

            String role = admin.getUserRole(userId);
            System.out.println("@@Role " + role);

            if (role == null) {
                deleteLabel.setText("User with ID " + userId + " does not exist.");
                return;
            }

            if (role.equals("donor")) {
                admin.deleteAppointmentByUserId(userId);
                admin.deleteDonorByUserId(userId);
            } else if (role.equals("patient")) {
                admin.deleteRequestByUserId(userId);
                admin.deletePatientByUserId(userId);
            }

            boolean deletedFromUsers = admin.deleteUser(userId);

            if (deletedFromUsers) {
                deleteLabel.setText("Deleted " + role);
            } else {
                deleteLabel.setText("Failed to delete user with ID " + userId);
            }

        } catch (NumberFormatException e) {
            deleteLabel.setText("Invalid input. Please enter a valid user ID.");
        } catch (Exception e) {
            deleteLabel.setText("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void applyButtonOnAction(ActionEvent actionEvent) {
        if(admin.addUser(nameTextField.getText(),emailTextField.getText(),passwordTextField.getText(),roleTextField.getText(),bloodGroupTextField.getText()))
        {
            promptLabel.setText(roleTextField.getText() + " has been registered successfully!");
        }
        else {
            promptLabel.setText("Cannot register user, please try again");
        }
    }

    //--------------------- Manage Requests ------------------------
    public void manageRequestButtonOnAction(ActionEvent actionEvent) {

       requestIdColumn.setCellValueFactory(new PropertyValueFactory<>("request_id"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("requestDate"));
        patientIDColumn.setCellValueFactory(new PropertyValueFactory<>("patient_id"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        bloodGroupColumn.setCellValueFactory(new PropertyValueFactory<>("bloodGroup"));

        manageUsersAnchor.setVisible(false);
        addUserAnchor.setVisible(false);
        viewTableAnchor.setVisible(false);
        manageInventoryAnchor.setVisible(false);
        manageRequestsAnchor.setVisible(true);
        deleteUserAnchor.setVisible(false);





        Request request = new Request();
        ObservableList<Request> list = request.getAllRequestsList();
        viewRequestTable.setItems(list);

    }

  /*  public void manageInventoryButtonOnAction(ActionEvent actionEvent) {


        inventoryIdColumn.setCellValueFactory(new PropertyValueFactory<>("inventory_id"));
        quantityColumn1.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        bloodGroupColumn1.setCellValueFactory(new PropertyValueFactory<>("bloodGroup"));

        manageUsersAnchor.setVisible(false);
        addUserAnchor.setVisible(false);
        viewTableAnchor.setVisible(false);
        manageRequestsAnchor.setVisible(false);
        manageInventoryAnchor.setVisible(true);


        Inventory inventory = new Inventory();
       // ObservableList<Inventory> list = inventory.getInventoryList();
       // inventoryTable.setItems(list);
    }

*/

    public void acceptRequestButtonOnAction(ActionEvent actionEvent) {
       int request_id = Integer.parseInt(requestTextField.getText());
        Request request = new Request();
        if(request.acceptRequest(request_id))
        {
            requestLabel.setText("Request accepted");
        }
        else {
            requestLabel.setText("Requested quantity is more than inventory");
        }

    }

    public void declineRequestButtonOnAction(ActionEvent actionEvent) {
        int request_id = Integer.parseInt(requestTextField.getText());
        Request request = new Request();
        if(request.declineRequest(request_id))
        {
            requestLabel.setText("Request declined");
        }
        else {
            requestLabel.setText("Request already accepted");
        }
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



    // ----------------------- Manage Inventory -----------------------
    public void manageInventoryButtonOnAction(ActionEvent actionEvent) {


        inventoryIdColumn.setCellValueFactory(new PropertyValueFactory<>("inventory_id"));
        quantityColumn1.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        bloodGroupColumn1.setCellValueFactory(new PropertyValueFactory<>("bloodGroup"));

        manageUsersAnchor.setVisible(false);
        addUserAnchor.setVisible(false);
        viewTableAnchor.setVisible(false);
        manageRequestsAnchor.setVisible(false);
        manageInventoryAnchor.setVisible(true);


        Inventory inventory = new Inventory();
        ObservableList<Inventory> list = inventory.getInventoryList();
        inventoryTable.setItems(list);
    }
    public void applyInventoryButtonOnAction(ActionEvent actionEvent)
    {
        int inv_id = Integer.parseInt(inventoryIdTextField.getText());
        int quantity = Integer.parseInt(quantityTextField.getText());
        Inventory inventory = new Inventory();
        if(inventory.updateInventory(inv_id,quantity))
        {
            promptLabel2.setText("Inventory has been updated");
        };
    }
}



