package classes_controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.DatabaseConnection;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.sql.*;
import java.time.*;


public class Appointment
{
    String DB_URL = "jdbc:mysql://localhost:3306/LifeFlow";  // Ensure your database name is correct
    String USER = "root";
    String PASS = "Admin@123";
    private LocalDate appointmentDate;
    private Donor donor;
    private int quantity;

    public Appointment()
    {

    }

    public Appointment(LocalDate appointmentDate, Donor donor, int quantity)
    {
        this.appointmentDate = appointmentDate;
        this.donor = donor;
        this.quantity = quantity;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public Donor getDonor() {
        return donor;
    }

    public void setDonor(Donor donor) {
        this.donor = donor;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean setAppointment() {
        String query = "INSERT INTO appointment (date, donor_id, quantity) VALUES (?, ?, ?)";
        Date sqlDate = Date.valueOf(this.appointmentDate);

        try {
            // Load the MySQL driver explicitly
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish connection and execute query
            try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
                 PreparedStatement statement = connection.prepareStatement(query)) {

                // Set parameters for the prepared statement
                statement.setDate(1, sqlDate);
                statement.setInt(2, donor.getDonorID());
                statement.setInt(3, this.quantity);

                // Execute the update and check for success
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Appointment data inserted successfully.");
                    return true;
                } else {
                    System.out.println("Appointment data insertion failed.");
                    return false;
                }
            }
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found: " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (SQLException e) {
            System.out.println("Error while inserting appointment: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    /*
    public boolean setAppointment()
    {
        Date sqlDate = Date.valueOf(this.appointmentDate);
        System.out.println("this is : " + donor.getDonorID());
        //Appointment appointment = new Appointment(date, donor,quantity);
        String query = "Insert into appointment(date,donor_id,quantity) VALUES (?,?,?)";

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connection = connectNow.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setDate(1, sqlDate);
            statement.setInt(2,donor.getDonorID());
            statement.setInt(3,this.quantity);

            int rowsAffected = statement.executeUpdate();
            if(rowsAffected > 0)
            {
                System.out.println("Data inserted");
                return true;
            }
            else {
                System.out.println("Data insertion failed");
            }

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return false;
    }

    */
    /*
    public ObservableList<Appointment> getAppointmentsList(int donorID)
    {
        ObservableList<Appointment> list = FXCollections.observableArrayList();

        String query = "Select * from Appointment where donor_id = ?";
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connection = connectNow.getConnection();
        Date sqlDate;

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1,donorID);

            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next())
            {
                sqlDate = resultSet.getDate("date");
                LocalDate date = sqlDate.toLocalDate();
                int quantity = resultSet.getInt("quantity");
                System.out.println(date + " " + quantity + " " );
                Appointment appointment = new Appointment(date,this.donor,quantity);
                list.add(appointment);

            }


            return list;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }



    }

*/
    public ObservableList<Appointment> getAppointmentsList(int donorID) {
        ObservableList<Appointment> list = FXCollections.observableArrayList();

        String query = "SELECT * FROM Appointment WHERE donor_id = ?";

        try {
            // Load the MySQL driver explicitly
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish connection and execute query
            try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
                 PreparedStatement statement = connection.prepareStatement(query)) {

                // Set parameter for donor ID
                statement.setInt(1, donorID);

                // Execute the query and process results
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        // Retrieve and process data
                        Date sqlDate = resultSet.getDate("date");
                        LocalDate date = sqlDate.toLocalDate();
                        int quantity = resultSet.getInt("quantity");
                        System.out.println(date + " " + quantity);

                        // Create Appointment object and add it to the list
                        Appointment appointment = new Appointment(date, this.donor, quantity);
                        list.add(appointment);
                    }
                }

            }
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Error while retrieving appointments: " + e.getMessage());
            e.printStackTrace();
        }

        // Return the list (empty if an exception occurs)
        return list;
    }



}
