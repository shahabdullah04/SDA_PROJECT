package classes_controller;

import sample.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.*;


public class Patient extends User{



    private String bloodGroup;
    private int patient_id;

    public Patient(){}


    public Patient getPatient(int userId) {
        Patient patient = new Patient();
        patient.user_id = userId;

        String patientQuery = "SELECT * FROM lifeflow.patient WHERE user_id = ?";
        String userQuery = "SELECT * FROM users WHERE user_id = ?";

        try {
            // Load the MySQL driver explicitly
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
                 PreparedStatement patientStmt = connection.prepareStatement(patientQuery);
                 PreparedStatement userStmt = connection.prepareStatement(userQuery)) {

                // Fetch patient details from `patient` table
                patientStmt.setInt(1, patient.user_id);
                try (ResultSet patientResultSet = patientStmt.executeQuery()) {
                    if (patientResultSet.next()) {
                        patient.patient_id = patientResultSet.getInt("patient_id");
                        patient.bloodGroup = patientResultSet.getString("blood_group");
                    } else {
                        System.out.println("No patient record found for user_id: " + userId);
                    }
                }

                // Fetch user details from `users` table
                userStmt.setInt(1, patient.user_id);
                try (ResultSet userResultSet = userStmt.executeQuery()) {
                    if (userResultSet.next()) {
                        patient.name = userResultSet.getString("Name");
                        patient.email = userResultSet.getString("Email");
                        patient.password = userResultSet.getString("password");
                    } else {
                        System.out.println("No user record found for user_id: " + userId);
                    }
                }

            }
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Error fetching patient details: " + e.getMessage());
            e.printStackTrace();
        }

        return patient;
    }

/*
    public Patient getPatient(int user_id)
    {
        Patient patient = new Patient();
        patient.user_id = user_id;

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connection = connectNow.getConnection();
            String query1 = "select * from patient where user_id = ?";
            String query2 = "select * from users where user_id = ?";

            PreparedStatement statement = connection.prepareStatement(query1);
            statement.setInt(1,patient.user_id);

            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next())
            {
                patient.patient_id = resultSet.getInt("patient_id");
                patient.bloodGroup = resultSet.getString("blood_Group");

            }
            statement = connection.prepareStatement(query2);
            statement.setInt(1,patient.user_id);
            resultSet = statement.executeQuery();
            if(resultSet.next())
            {
                patient.name = resultSet.getString("Name");
                patient.email = resultSet.getString("Email");
                patient.password = resultSet.getString("password");

            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return patient;

    }
*/
    public Patient(String bloodGroup, String name, String email, String password)
    {
        this.bloodGroup = bloodGroup;
        this.name = name;
        this.email = email;
        this.password= password;
    }
    public String getName(){
        return this.name;

    }
    public int getPatientID()
    {
        return this.patient_id;
    }

    public String getBloodGroup() {
        return this.bloodGroup;
    }
    /*
    public boolean registerPatient()
    {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connection = connectNow.getConnection();

        String query1 = "Insert into users (Name, Email, password,role) VALUES (?,?,?,?)";
        //String query1 = "Insert into users (Name, Email, password) VALUES ('babar','babar@gmail.com','babar')";
        String query2 = "SELECT user_id FROM users ORDER BY user_id DESC LIMIT 1";



        try {
            PreparedStatement statement = connection.prepareStatement(query1);
            statement.setString(1, this.name);
            statement.setString(2, this.email);
            statement.setString(3, this.password);
            statement.setString(4, "donor");
            //ResultSet resultSet = statement.executeQuery(query1);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Data inserted");
            } else {
                System.out.println("Data insertion failed");

            }


            ResultSet resultset = statement.executeQuery(query2);
            if (resultset.next()) {
                int userid = resultset.getInt("user_id");
                System.out.println(userid);
                String query3 = "INSERT INTO hemohub.patient (blood_group, user_id) VALUES (?,?)";
                statement = connection.prepareStatement(query3);
                statement.setString(1, this.bloodGroup);
                statement.setInt(2, userid);
                statement.executeUpdate();
                return true;

            } else {
                System.out.println("No rows found");
                return false;
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }

        return false;

    }*/

    public boolean registerPatient() {
        String userInsertQuery = "INSERT INTO users (Name, Email, password, role) VALUES (?, ?, ?, ?)";
        String getUserIdQuery = "SELECT user_id FROM users ORDER BY user_id DESC LIMIT 1";
        String patientInsertQuery = "INSERT INTO lifeflow.patient (blood_group, user_id) VALUES (?, ?)";

        try {
            // Load the MySQL driver explicitly
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
                 PreparedStatement userInsertStmt = connection.prepareStatement(userInsertQuery);
                 PreparedStatement getUserIdStmt = connection.prepareStatement(getUserIdQuery);
                 PreparedStatement patientInsertStmt = connection.prepareStatement(patientInsertQuery)) {

                // Insert user details
                userInsertStmt.setString(1, this.name);
                userInsertStmt.setString(2, this.email);
                userInsertStmt.setString(3, this.password);
                userInsertStmt.setString(4, "patient");
                int rowsInserted = userInsertStmt.executeUpdate();

                if (rowsInserted > 0) {
                    System.out.println("User details inserted successfully.");
                } else {
                    System.out.println("User details insertion failed.");
                    return false;
                }

                // Fetch the latest user_id
                ResultSet rs = getUserIdStmt.executeQuery();
                if (rs.next()) {
                    int userId = rs.getInt("user_id");
                    System.out.println("Fetched user ID: " + userId);

                    // Insert patient details
                    patientInsertStmt.setString(1, this.bloodGroup);
                    patientInsertStmt.setInt(2, userId);
                    int patientRowsInserted = patientInsertStmt.executeUpdate();

                    if (patientRowsInserted > 0) {
                        System.out.println("Patient details inserted successfully.");
                        return true;
                    } else {
                        System.out.println("Patient details insertion failed.");
                        return false;
                    }
                } else {
                    System.out.println("Failed to fetch user ID.");
                    return false;
                }
            }
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found: " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (SQLException e) {
            System.out.println("Error during patient registration: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


}
