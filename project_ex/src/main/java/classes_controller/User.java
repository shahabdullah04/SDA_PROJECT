package classes_controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;

public class User {

    String DB_URL = "jdbc:mysql://localhost:3306/LifeFlow";  // Ensure your database name is correct
    String USER = "root";
    String PASS = "Admin@123";

    protected String name;
    protected String email;
    protected String password;
    protected String role;
    protected int user_id;

    public User()
    {

    }

    public User(String name, String email, String password, String role, int user_id) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.user_id = user_id;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getters and Setters for email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getters and Setters for password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Getters and Setters for role
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // Getters and Setters for user_id
    public int getUserId() {
        return user_id;
    }

    public void setUserId(int user_id) {
        this.user_id = user_id;
    }

    public int getUserIdFromDb(String email, String password)
    {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        String verifyLoginQuery = "Select * FROM users where Email = ? AND password = ?";
        try {
            PreparedStatement statement = connectDB.prepareStatement(verifyLoginQuery);
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet queryResult = statement.executeQuery();


            while(queryResult.next())
            {
                this.user_id = queryResult.getInt("user_id");
                System.out.println(this.user_id);

            }

        } catch(Exception e){
            e.printStackTrace();
        }
        return this.user_id;
    }
/*
    public ObservableList<User> getAllUsers(){

        ObservableList<User> list = FXCollections.observableArrayList();
        String query = "Select * from users";
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connection = connectNow.getConnection();
        Date sqlDate;

        try {
            PreparedStatement statement = connection.prepareStatement(query);

            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next())
            {
                String name = resultSet.getString("Name");
                String email = resultSet.getString("Email");
                String password = resultSet.getString("password");
                String role = resultSet.getString("role");
                int user_id = resultSet.getInt("user_id");

                User user = new User(name,email,password,role,user_id);
                list.add(user);

            }
            return list;


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

 */


    public ObservableList<User> getAllUsers() {
        ObservableList<User> userList = FXCollections.observableArrayList();
        String query = "SELECT * FROM users";

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            // Iterate through the result set and populate the user list
            while (resultSet.next()) {
                String name = resultSet.getString("Name");
                String email = resultSet.getString("Email");
                String password = resultSet.getString("password");
                String role = resultSet.getString("role");
                int userId = resultSet.getInt("user_id");

                // Create User object and add to the list
                User user = new User(name, email, password, role, userId);
                userList.add(user);
            }

            System.out.println("Successfully retrieved all users.");
            return userList;

        } catch (SQLException e) {
            System.out.println("Error retrieving users: " + e.getMessage());
            e.printStackTrace();
            return userList; // Return an empty list if an error occurs
        }
    }

}
