package classes_controller;

import sample.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

public class Admin extends User {

    private int admin_id;

    public Admin()
    {

    }
    public Admin(int admin_id)
    {
        this.admin_id = admin_id;
    }
    /*public Admin getAdmin(int user_id)
    {
        Admin admin = new Admin();
        admin.user_id = user_id;

        try {
            DatabaseConnection connectNow = DatabaseConnection.getInstance();
            Connection connection = connectNow.getConnection();
            String query1 = "select * from admin where user_id = ?";
            String query2 = "select * from users where user_id = ?";

            PreparedStatement statement = connection.prepareStatement(query1);
            statement.setInt(1,admin.user_id);

            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next())
            {
                admin.admin_id = resultSet.getInt("admin_id");

            }
            statement = connection.prepareStatement(query2);
            statement.setInt(1,admin.user_id);
            resultSet = statement.executeQuery();
            if(resultSet.next())
            {
                admin.name = resultSet.getString("Name");
                admin.email = resultSet.getString("Email");
                admin.password = resultSet.getString("password");

            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return admin;

    }*/

    public Admin getAdmin(int user_id) {
        String query1 = "SELECT * FROM admin WHERE user_id = ?";
        String query2 = "SELECT * FROM users WHERE user_id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement statement1 = connection.prepareStatement(query1);
             PreparedStatement statement2 = connection.prepareStatement(query2)) {

            Admin admin = new Admin();
            admin.user_id = user_id;

            // Query for admin details
            statement1.setInt(1, admin.user_id);
            try (ResultSet resultSet1 = statement1.executeQuery()) {
                if (resultSet1.next()) {
                    admin.admin_id = resultSet1.getInt("admin_id");
                }
            }

            // Query for user details
            statement2.setInt(1, admin.user_id);
            try (ResultSet resultSet2 = statement2.executeQuery()) {
                if (resultSet2.next()) {
                    admin.name = resultSet2.getString("Name");
                    admin.email = resultSet2.getString("Email");
                    admin.password = resultSet2.getString("password");
                }
            }

            return admin;

        } catch (SQLException e) {
            System.out.println("Error fetching admin details: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


    /*public boolean addUser(String name, String email, String password, String role, String bloodGroup)
    {
        DatabaseConnection connectNow = DatabaseConnection.getInstance();
        Connection connection = connectNow.getConnection();

        String query1 = "Insert into users (Name, Email, password,role) VALUES (?,?,?,?)";
        //String query1 = "Insert into users (Name, Email, password) VALUES ('Usman Afzal','usman@gmail.com','usman')";
        String query2 = "SELECT user_id FROM users ORDER BY user_id DESC LIMIT 1";



        try{
            PreparedStatement statement = connection.prepareStatement(query1);
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, password);
            statement.setString(4,role);

            //ResultSet resultSet = statement.executeQuery(query1);
            int rowsAffected = statement.executeUpdate();
            if(rowsAffected > 0)
            {
                System.out.println("Data inserted");
            }
            else {
                System.out.println("Data insertion failed");
            }


            ResultSet resultset = statement.executeQuery(query2);
            if(resultset.next())
            {
                this.user_id= resultset.getInt("user_id");
                System.out.println(this.user_id);
                String query3 = "INSERT INTO " + role + "(blood_group, user_id) VALUES (?,?)";
                statement = connection.prepareStatement(query3);
                //statement.setString(1, role);
                statement.setString(1, bloodGroup);
                statement.setInt(2, user_id);
                statement.executeUpdate();
                return true;

            }
            else {
                System.out.println("No rows found");
                return false;

            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }*/
    public boolean addUser(String name, String email, String password, String role, String bloodGroup) {
        String query1 = "INSERT INTO users (Name, Email, password, role) VALUES (?, ?, ?, ?)";
        String query2 = "SELECT user_id FROM users ORDER BY user_id DESC LIMIT 1";
        String query3 = "INSERT INTO %s (blood_group, user_id) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement statement1 = connection.prepareStatement(query1);
             PreparedStatement statement2 = connection.prepareStatement(query2)) {

            // Insert user details
            statement1.setString(1, name);
            statement1.setString(2, email);
            statement1.setString(3, password);
            statement1.setString(4, role);

            int rowsInserted = statement1.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("User data inserted successfully.");
            } else {
                System.out.println("User data insertion failed.");
                return false;
            }

            // Fetch the latest user_id
            ResultSet resultSet = statement2.executeQuery();
            if (resultSet.next()) {
                int userId = resultSet.getInt("user_id");
                System.out.println("Fetched user ID: " + userId);

                // Insert data into role-specific table (e.g., donor, admin)
                String formattedQuery = String.format(query3, role);  // Use role as part of the query
                try (PreparedStatement statement3 = connection.prepareStatement(formattedQuery)) {
                    statement3.setString(1, bloodGroup);
                    statement3.setInt(2, userId);

                    int rowsAffected = statement3.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Role-specific data inserted successfully.");
                        return true;
                    } else {
                        System.out.println("Role-specific data insertion failed.");
                        return false;
                    }
                }
            } else {
                System.out.println("No user ID fetched.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error during user insertion: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

  /*  public boolean deleteUser(int userId) {
        DatabaseConnection connectNow = DatabaseConnection.getInstance();
        Connection connection = connectNow.getConnection();
        try {
            // Execute SQL query to delete the user from the users table based on their user ID
            String query = "DELETE FROM users WHERE user_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
*/

    public boolean deleteUser(int userId) {
        String query1 = "SELECT role FROM users WHERE user_id = ?";
        String query2 = "DELETE FROM users WHERE user_id = ?";
        String query3 = "DELETE FROM %s WHERE user_id = ?";  // Dynamically handle role-specific table deletion

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement statement1 = connection.prepareStatement(query1);
             PreparedStatement statement2 = connection.prepareStatement(query2)) {

            // Check the role of the user first to determine which table to delete data from
            statement1.setInt(1, userId);
            ResultSet resultSet = statement1.executeQuery();

            String role = null;
            if (resultSet.next()) {
                role = resultSet.getString("role");
            } else {
                System.out.println("User not found.");
                return false;
            }

            // Delete from the role-specific table (e.g., donor, admin)
            if (role != null) {
                String formattedQuery = String.format(query3, role);  // Format query for role-specific table
                try (PreparedStatement statement3 = connection.prepareStatement(formattedQuery)) {
                    statement3.setInt(1, userId);
                    int rowsAffectedRole = statement3.executeUpdate();
                    if (rowsAffectedRole > 0) {
                        System.out.println("Role-specific data deleted successfully.");
                    }
                }
            }

            // Now delete the user from the users table
            statement2.setInt(1, userId);
            int rowsAffected = statement2.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Error during user deletion: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


   /* public boolean deleteAppointmentByUserId(int userId) {

        DatabaseConnection connectNow = DatabaseConnection.getInstance();
        Connection connection = connectNow.getConnection();

        try {
            // Retrieve the donor_id associated with the provided userId
            String donorIdQuery = "SELECT donor_id FROM donor WHERE user_id = ?";
            PreparedStatement donorIdStatement = connection.prepareStatement(donorIdQuery);
            donorIdStatement.setInt(1, userId);
            ResultSet donorIdResult = donorIdStatement.executeQuery();

            int donorId;
            if (donorIdResult.next()) {
                donorId = donorIdResult.getInt("donor_id");

                // Execute SQL query to delete data from the appointment table based on the donor_id
                String deleteQuery = "DELETE FROM appointment WHERE donor_id = ?";
                PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                deleteStatement.setInt(1, donorId);
                int rowsAffected = deleteStatement.executeUpdate();
                return rowsAffected > 0;
            } else {
                // Donor with the given user ID does not exist
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }*/

    public boolean deleteAppointmentByUserId(int userId) {
        String donorIdQuery = "SELECT donor_id FROM donor WHERE user_id = ?";
        String deleteQuery = "DELETE FROM appointment WHERE donor_id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement donorIdStatement = connection.prepareStatement(donorIdQuery);
             PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {

            // Retrieve the donor_id associated with the provided userId
            donorIdStatement.setInt(1, userId);
            try (ResultSet donorIdResult = donorIdStatement.executeQuery()) {
                int donorId;
                if (donorIdResult.next()) {
                    donorId = donorIdResult.getInt("donor_id");

                    // Execute SQL query to delete data from the appointment table based on the donor_id
                    deleteStatement.setInt(1, donorId);
                    int rowsAffected = deleteStatement.executeUpdate();
                    return rowsAffected > 0;
                } else {
                    // Donor with the given user ID does not exist
                    System.out.println("Donor with the provided user ID does not exist.");
                    return false;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error during appointment deletion: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /*public boolean deleteDonorByUserId(int userId) {
        DatabaseConnection connectNow = DatabaseConnection.getInstance();
        Connection connection = connectNow.getConnection();
        try {
            // Execute SQL query to delete data from the donor table based on the user ID
            String query = "DELETE FROM donor WHERE user_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }*/


    public boolean deleteDonorByUserId(int userId) {
        String query1 = "SELECT donor_id FROM donor WHERE user_id = ?";
        String query2 = "DELETE FROM donor WHERE user_id = ?";
        String query3 = "DELETE FROM users WHERE user_id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement statement1 = connection.prepareStatement(query1);
             PreparedStatement statement2 = connection.prepareStatement(query2);
             PreparedStatement statement3 = connection.prepareStatement(query3)) {

            // Retrieve the donor_id associated with the user_id
            statement1.setInt(1, userId);
            ResultSet resultSet = statement1.executeQuery();

            if (resultSet.next()) {
                int donorId = resultSet.getInt("donor_id");

                // Delete the donor record based on donor_id
                statement2.setInt(1, userId);
                int rowsAffectedDonor = statement2.executeUpdate();
                if (rowsAffectedDonor > 0) {
                    System.out.println("Donor data deleted successfully.");
                }

                // Now delete the user from the users table
                statement3.setInt(1, userId);
                int rowsAffectedUser = statement3.executeUpdate();
                return rowsAffectedUser > 0;
            } else {
                System.out.println("No donor found with the given user_id.");
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Error during donor deletion: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

   /*public String getUserRole(int userId) {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connection = connectNow.getConnection();
        try {
            String query = "SELECT role FROM users WHERE user_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
//            System.out.println("result set:");
//            System.out.println(resultSet);
            if (resultSet.next()) {
                return resultSet.getString("role");
            } else {
                // User with the given user ID does not exist
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }*/

    /*public String getUserRole(int userId) {
        String query = "SELECT role FROM users WHERE user_id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set the userId to the prepared statement
            preparedStatement.setInt(1, userId);

            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();

            // Check if result is available and return the role
            if (resultSet.next()) {
                return resultSet.getString("role");
            } else {
                // If no user is found, return null
                return null;
            }

        } catch (SQLException e) {
            System.out.println("Error during getting user role: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }*/

    public String getUserRole(int userId) {
        String query = "SELECT role FROM users WHERE user_id = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set the userId to the prepared statement
            preparedStatement.setInt(1, userId);

            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();

            // Check if result is available and return the role
            if (resultSet.next()) {
                return resultSet.getString("role");
            } else {
                System.out.println("No user found with user_id: " + userId);
                return null;
            }

        } catch (SQLException e) {
            System.out.println("Error during getting user role: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


    /*public boolean deleteRequestByUserId(int userId) {
        DatabaseConnection connectNow = DatabaseConnection.getInstance();
        Connection connection = connectNow.getConnection();

        try {
            // Retrieve the patient_id associated with the provided userId
            String patientIdQuery = "SELECT patient_id FROM patient WHERE user_id = ?";
            PreparedStatement patientIdStatement = connection.prepareStatement(patientIdQuery);
            patientIdStatement.setInt(1, userId);
            ResultSet patientIdResult = patientIdStatement.executeQuery();

            List<Integer> patientIds = new ArrayList<>();
            while (patientIdResult.next()) {
                int patientId = patientIdResult.getInt("patient_id");
                patientIds.add(patientId);
            }

            if (!patientIds.isEmpty()) {
                // Construct the SQL query to delete requests based on patient_ids
                String deleteQuery = "DELETE FROM request WHERE patient_id IN (";
                for (int i = 0; i < patientIds.size(); i++) {
                    deleteQuery += "?";
                    if (i < patientIds.size() - 1) {
                        deleteQuery += ",";
                    }
                }
                deleteQuery += ")";

                // Execute the constructed SQL query
                PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                for (int i = 0; i < patientIds.size(); i++) {
                    deleteStatement.setInt(i + 1, patientIds.get(i));
                }
                int rowsAffected = deleteStatement.executeUpdate();
                return rowsAffected > 0;
            } else {
                // Patient with the given user ID does not exist
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }*/


    public boolean deleteRequestByUserId(int userId) {
        String patientIdQuery = "SELECT patient_id FROM patient WHERE user_id = ?";
        String deleteQuery = "DELETE FROM request WHERE patient_id IN (";

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement patientIdStatement = connection.prepareStatement(patientIdQuery)) {

            // Set the userId to the prepared statement for fetching patient_id
            patientIdStatement.setInt(1, userId);

            // Execute the query to retrieve the patient_id(s)
            ResultSet patientIdResult = patientIdStatement.executeQuery();

            List<Integer> patientIds = new ArrayList<>();
            while (patientIdResult.next()) {
                int patientId = patientIdResult.getInt("patient_id");
                patientIds.add(patientId);
            }

            // If there are patient IDs, proceed with the deletion
            if (!patientIds.isEmpty()) {
                // Construct the SQL query with placeholders for patient_ids
                StringBuilder queryBuilder = new StringBuilder(deleteQuery);
                for (int i = 0; i < patientIds.size(); i++) {
                    queryBuilder.append("?");
                    if (i < patientIds.size() - 1) {
                        queryBuilder.append(",");
                    }
                }
                queryBuilder.append(")");

                try (PreparedStatement deleteStatement = connection.prepareStatement(queryBuilder.toString())) {
                    // Set patient_ids in the delete statement
                    for (int i = 0; i < patientIds.size(); i++) {
                        deleteStatement.setInt(i + 1, patientIds.get(i));
                    }

                    // Execute the delete query
                    int rowsAffected = deleteStatement.executeUpdate();
                    return rowsAffected > 0;
                }
            } else {
                // No patient records were found for the provided userId
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error during deleting requests: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

   /* public boolean deletePatientByUserId(int userId) {
//        DatabaseConnection connectNow = new DatabaseConnection();
//        Connection connection = connectNow.getConnection();
        DatabaseConnection connectNow = DatabaseConnection.getInstance();
        Connection connection = connectNow.getConnection();
        try {
            // Execute SQL query to delete data from the patient table based on the user ID
            String query = "DELETE FROM patient WHERE user_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }*/
   public boolean deletePatientByUserId(int userId) {
       String query = "DELETE FROM patient WHERE user_id = ?";

       try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {

           // Set the userId to the prepared statement for deletion
           preparedStatement.setInt(1, userId);

           // Execute the query to delete the patient
           int rowsAffected = preparedStatement.executeUpdate();
           return rowsAffected > 0;
       } catch (SQLException e) {
           System.out.println("Error during patient deletion: " + e.getMessage());
           e.printStackTrace();
           return false;
       }
   }

}
