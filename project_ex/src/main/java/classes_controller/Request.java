package classes_controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.DatabaseConnection;

import java.time.LocalDate;
import java.sql.*;

public class Request {

    String DB_URL = "jdbc:mysql://localhost:3306/LifeFlow";  // Ensure your database name is correct
    String USER = "root";
    String PASS = "Admin@123";
    private LocalDate requestDate;



    private Patient patient;
    private int quantity;
    private int request_id;
    private String status;
    private String bloodGroup;


    private int patient_id;

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }


    public int getRequest_id() {
        return request_id;
    }

    public void setRequest_id(int request_id) {
        this.request_id = request_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDate getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }

    public int getPatient_id() { return patient_id;}


    public Request() {
    }

    public Request(LocalDate requestDateDate, Patient patient, int quantity) {
        this.requestDate = requestDateDate;
        this.patient = patient;
        this.quantity = quantity;
    }
    public Request(LocalDate requestDateDate, Patient patient,int quantity, String status, String bloodGroup) {
        this.requestDate = requestDateDate;
        this.patient = patient;
        this.quantity = quantity;
        this.status = status;
        this.bloodGroup = bloodGroup;
    }
    public Request(LocalDate requestDate, Patient patient, int quantity, int request_id, String status, String bloodGroup, int patient_id) {
        this.requestDate = requestDate;
        this.patient = patient;
        this.quantity = quantity;
        this.request_id = request_id;
        this.status = status;
        this.bloodGroup = bloodGroup;
        this.patient_id = patient_id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }


    public boolean setRequest(String status) {
        Date sqlDate = Date.valueOf(this.requestDate); // Convert LocalDate to SQL Date
        System.out.println("Patient ID: " + patient.getPatientID());

        String requestInsertQuery = "INSERT INTO request (date, patient_id, quantity, blood_group, status) VALUES (?, ?, ?, ?, ?)";

        try {
            // Load the MySQL driver explicitly
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
                 PreparedStatement requestStmt = connection.prepareStatement(requestInsertQuery)) {

                // Set the parameters for the query
                requestStmt.setDate(1, sqlDate);
                requestStmt.setInt(2, patient.getPatientID());
                requestStmt.setInt(3, this.quantity);
                requestStmt.setString(4, patient.getBloodGroup());
                requestStmt.setString(5, status);

                // Execute the query
                int rowsAffected = requestStmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Request data inserted successfully.");
                    return true;
                } else {
                    System.out.println("Request data insertion failed.");
                }
            }
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Error inserting request data: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }


/*
    public boolean setRequest(String status)
    {
        Date sqlDate = Date.valueOf(this.requestDate);
        System.out.println("this is : " + patient.getPatientID());

        String query = "INSERT INTO request (date, patient_id, quantity, blood_group, status) VALUES (?, ?, ?, ?, ?)";

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connection = connectNow.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setDate(1, sqlDate);
            statement.setInt(2, patient.getPatientID());
            statement.setInt(3, this.quantity);
            statement.setString(4, patient.getBloodGroup());
            statement.setString(5, status); // Set the status parameter

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Data inserted");
                return true;
            } else {
                System.out.println("Data insertion failed");
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return false;
    }
*/

    public ObservableList<Request> getRequestsList(int patientID) {
        ObservableList<Request> list = FXCollections.observableArrayList();

        String requestQuery = "SELECT * FROM request WHERE patient_id = ?";

        try {
            // Load the MySQL driver explicitly
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
                 PreparedStatement requestStmt = connection.prepareStatement(requestQuery)) {

                // Set the parameter for the patient ID
                requestStmt.setInt(1, patientID);

                // Execute the query and process the results
                ResultSet resultSet = requestStmt.executeQuery();
                while (resultSet.next()) {
                    // Extract data from the result set
                    Date sqlDate = resultSet.getDate("date");
                    LocalDate date = sqlDate.toLocalDate(); // Convert to LocalDate
                    int quantity = resultSet.getInt("quantity");
                    String status = resultSet.getString("status");
                    String bloodGroup = resultSet.getString("blood_group");

                    // Debugging output (optional)
                    System.out.println(date + " " + quantity + " " + status + " " + bloodGroup);

                    // Create a Request object and add it to the list
                    Request request = new Request(date, this.patient, quantity, status, bloodGroup);
                    list.add(request);
                }
            }
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Error fetching request data: " + e.getMessage());
            e.printStackTrace();
        }

        return list; // Return the populated list or an empty list in case of an error
    }




    /*
    public ObservableList<Request> getRequestsList(int patientID) {
        ObservableList<Request> list = FXCollections.observableArrayList();

        String query = "SELECT * FROM request WHERE patient_id = ?";
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connection = connectNow.getConnection();
        Date sqlDate;


        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, patientID);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                sqlDate = resultSet.getDate("date");
                LocalDate date = sqlDate.toLocalDate();
                int quantity = resultSet.getInt("quantity");
                String status = resultSet.getString("status");
                String bloodGroup = resultSet.getString("blood_group");
                System.out.println(date + " " + quantity + " " + status + " " + bloodGroup);

                Request request = new Request(date, this.patient, quantity, status, bloodGroup);
                list.add(request);
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    */


    public ObservableList<Request> getAllRequestsList() {
        ObservableList<Request> list = FXCollections.observableArrayList();

        String query = "SELECT * FROM request";

        try {
            // Load the MySQL driver explicitly
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
                 PreparedStatement statement = connection.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {

                // Iterate through the result set and populate the list
                while (resultSet.next()) {
                    int reqID = resultSet.getInt("request_id");
                    int patientID = resultSet.getInt("patient_id");
                    Date sqlDate = resultSet.getDate("date");
                    LocalDate date = sqlDate.toLocalDate(); // Convert to LocalDate
                    int quantity = resultSet.getInt("quantity");
                    String status = resultSet.getString("status");
                    String bloodGroup = resultSet.getString("blood_group");

                    // Debugging output (optional)
                    System.out.println(date + " " + quantity + " " + status + " " + bloodGroup);

                    // Create a Request object and add it to the list
                    Request request = new Request(date, this.patient, quantity, reqID, status, bloodGroup, patientID);
                    list.add(request);
                }
            }
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Error fetching all request data: " + e.getMessage());
            e.printStackTrace();
        }

        return list; // Return the populated list or an empty list in case of an error
    }


    /*public ObservableList<Request> getAllRequestsList() {
        ObservableList<Request> list = FXCollections.observableArrayList();

        String query = "SELECT * FROM request";
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connection = connectNow.getConnection();
        Date sqlDate;


        try {
            PreparedStatement statement = connection.prepareStatement(query);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int reqID = resultSet.getInt("request_id");
                int patientID = resultSet.getInt("patient_id");
                sqlDate = resultSet.getDate("date");
                LocalDate date = sqlDate.toLocalDate();
                int quantity = resultSet.getInt("quantity");
                String status = resultSet.getString("status");
                String bloodGroup = resultSet.getString("blood_group");
                System.out.println(date + " " + quantity + " " + status + " " + bloodGroup);

                Request request = new Request(date, this.patient, quantity, reqID,status, bloodGroup,patientID);
                list.add(request);
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }*/

    /*public boolean acceptRequest(int request_id)
    {
        if(checkRequest(request_id))
        {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connection = connectNow.getConnection();

            String query = "UPDATE inventory\n" +
                    "SET quantity = quantity - " + this.quantity + "\n" +
                    "WHERE blood_group = '" + this.bloodGroup + "' ;\n";

            String query2 = "UPDATE request\n" +
                    "SET status = 'accepted' \n" +
                    "WHERE blood_group = '" + this.bloodGroup + "' ;\n";
            try {
                PreparedStatement statement = connection.prepareStatement(query);

                int rowsAffected = statement.executeUpdate();
                statement = connection.prepareStatement(query2);
                rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Data inserted");
                    return true;
                } else {
                    System.out.println("Data insertion failed");
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

        }
        System.out.println("false");
        return false;

    }*/


//ERROR
    public boolean acceptRequest(int request_id) {
        if (checkRequest(request_id)) {
            String updateInventoryQuery =
                    "UPDATE inventory SET quantity = quantity - ? WHERE blood_group = ?";
            String updateRequestQuery =
                    "UPDATE request SET status = 'accepted' WHERE request_id = ?";

            try {
                // Load the MySQL driver explicitly
                Class.forName("com.mysql.cj.jdbc.Driver");

                try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
                     PreparedStatement updateInventoryStmt = connection.prepareStatement(updateInventoryQuery);
                     PreparedStatement updateRequestStmt = connection.prepareStatement(updateRequestQuery)) {

                    // Set parameters for inventory update
                    updateInventoryStmt.setInt(1, this.quantity);
                    updateInventoryStmt.setString(2, this.bloodGroup);

                    // Execute inventory update
                    int inventoryRowsAffected = updateInventoryStmt.executeUpdate();

                    // Set parameters for request update
                    updateRequestStmt.setInt(1, request_id);

                    // Execute request update
                    int requestRowsAffected = updateRequestStmt.executeUpdate();

                    // Check if both updates succeeded
                    if (inventoryRowsAffected > 0 && requestRowsAffected > 0) {
                        System.out.println("Request accepted and inventory updated.");
                        return true;
                    } else {
                        System.out.println("Failed to update inventory or request.");
                    }
                }
            } catch (ClassNotFoundException e) {
                System.out.println("MySQL JDBC Driver not found: " + e.getMessage());
                e.printStackTrace();
            } catch (SQLException e) {
                System.out.println("Error while accepting request: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("Request validation failed.");
        }

        return false;
    }

   /* public boolean checkRequest(int request_id)    //this will check if there is enough blood in the inventory
    {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connection = connectNow.getConnection();
        int quantity = 0;
        int quantityInventory = 0;
        String blood_group = "";

        String query = "select quantity, blood_group from request where request_id = ? ";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, request_id);


            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next())
            {
                quantity = resultSet.getInt("quantity");
                blood_group = resultSet.getString("blood_group");
                this.bloodGroup = blood_group;
                this.quantity = quantity;
                System.out.printf("request quantity: " + quantity);

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String query2 = "select quantity from inventory where blood_group = '" + blood_group + "'";
        try {
            System.out.println("second try");
            PreparedStatement statement = connection.prepareStatement(query2);
            // statement.setString(1, blood_group);


            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next())
            {
                quantityInventory = resultSet.getInt("quantity");
                System.out.println("inventory quantity: " + quantityInventory);

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if(quantityInventory > quantity)
        {
            return true;
        }

        return false;
    }*/


    //ERROR
    public boolean checkRequest(int request_id) {
        int quantityRequested = 0;
        int inventoryQuantity = 0;

        String fetchRequestQuery = "SELECT quantity, blood_group FROM request WHERE request_id = ?";
        String fetchInventoryQuery = "SELECT quantity FROM inventory WHERE blood_group = ?";

        try {
            // Load the MySQL driver explicitly
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
                 PreparedStatement fetchRequestStmt = connection.prepareStatement(fetchRequestQuery)) {

                // Fetch request details
                fetchRequestStmt.setInt(1, request_id);
                try (ResultSet requestResultSet = fetchRequestStmt.executeQuery()) {
                    if (requestResultSet.next()) {
                        quantityRequested = requestResultSet.getInt("quantity");
                        this.bloodGroup = requestResultSet.getString("blood_group");
                        this.quantity = quantityRequested; // Save for later use
                        System.out.println("Request Quantity: " + quantityRequested);
                    } else {
                        System.out.println("Request not found for request_id: " + request_id);
                        return false;
                    }
                }

                // Fetch inventory details
                try (PreparedStatement fetchInventoryStmt = connection.prepareStatement(fetchInventoryQuery)) {
                    fetchInventoryStmt.setString(1, this.bloodGroup);
                    try (ResultSet inventoryResultSet = fetchInventoryStmt.executeQuery()) {
                        if (inventoryResultSet.next()) {
                            inventoryQuantity = inventoryResultSet.getInt("quantity");
                            System.out.println("Inventory Quantity: " + inventoryQuantity);
                        } else {
                            System.out.println("No inventory found for blood group: " + this.bloodGroup);
                            return false;
                        }
                    }
                }

            }
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found: " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (SQLException e) {
            System.out.println("Error while checking request: " + e.getMessage());
            e.printStackTrace();
            return false;
        }

        // Check if inventory has sufficient quantity
        if (inventoryQuantity >= quantityRequested) {
            return true;
        }

        System.out.println("Insufficient inventory for blood group: " + this.bloodGroup);
        return false;
    }


    /*public boolean declineRequest(int request_id) {

        System.out.println(status + "\n");
        if(!getStatus(request_id))
        {
            try
            {
                System.out.println("in try 1\n");
                DatabaseConnection connectNow = new DatabaseConnection();
                Connection connection = connectNow.getConnection();
                String query2 = "UPDATE request\n" +
                        "SET status = 'declined' \n" +
                        "WHERE request_id = " + request_id + " ;\n";

                PreparedStatement statement = connection.prepareStatement(query2);
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0)
                {
                    System.out.println("Data inserted");
                    return true;
                }
                else
                {
                    System.out.println("Data insertion failed");

                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }



        }
        else {
            System.out.printf("false");
        }
        return false;

    }*/
    public boolean declineRequest(int request_id) {
        System.out.println("Status before decline: " + status);

        // Check the current status before proceeding with the decline
        if (!getStatus(request_id)) {
            try {
                System.out.println("Processing decline request...");

                // Establish database connection
                DatabaseConnection connectNow = new DatabaseConnection();
                try (Connection connection = connectNow.getConnection()) {

                    // Query to update the status to 'declined'
                    String query = "UPDATE request SET status = 'declined' WHERE request_id = ?";

                    // Prepare and execute the statement
                    try (PreparedStatement statement = connection.prepareStatement(query)) {
                        statement.setInt(1, request_id);  // Set the request_id parameter

                        int rowsAffected = statement.executeUpdate();
                        if (rowsAffected > 0) {
                            System.out.println("Request declined successfully.");
                            return true;
                        } else {
                            System.out.println("No rows were affected. Decline failed.");
                        }
                    }

                }
            } catch (SQLException e) {
                System.out.println("SQL Error: " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Error while declining request", e);
            }
        } else {
            System.out.println("Request is already processed or cannot be declined.");
        }
        return false;
    }


    /*public boolean getStatus(int request_id)
    {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connection = connectNow.getConnection();

        String query = "select status from request where request_id = ? ";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, request_id);


            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next())
            {
                this.status = resultSet.getString("status");
                System.out.println(this.status + "\n");

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String status = "accepted";
        if(this.status.equals(status))
        {
            return true;
        }
        return false;
    }*/

    public boolean getStatus(int request_id) {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connection = connectNow.getConnection();

        String query = "SELECT status FROM request WHERE request_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, request_id);  // Set the request_id parameter

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {  // Check if the result set has any rows
                this.status = resultSet.getString("status");  // Assign the status
                System.out.println("Status: " + this.status);

                // Return true if the status is 'accepted'
                if ("accepted".equals(this.status)) {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error while fetching status", e);
        }

        return false;  // If no matching status was found or an exception occurred
    }




}
