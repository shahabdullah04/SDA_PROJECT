/*
package classes_controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.DatabaseConnection;



import java.sql.*;
import java.time.LocalDate;
*/

package classes_controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.DatabaseConnection;

import java.time.LocalDate;
import java.sql.*;


public class Inventory {

    private int inventory_id;
    private int quantity;
    private String bloodGroup;
    String DB_URL = "jdbc:mysql://localhost:3306/LifeFlow";  // Ensure your database name is correct
    String USER = "root";
    String PASS = "Admin@123";


    public Inventory()
    {

    }
    public Inventory(int inventory_id, int quantity, String bloodGroup) {
        this.inventory_id = inventory_id;
        this.quantity = quantity;
        this.bloodGroup = bloodGroup;
    }

    public int getInventory_id() {
        return inventory_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

   /* public boolean addToInventory(Appointment appointment)
    {
        Donor donor = appointment.getDonor();
        String query1 = "INSERT INTO inventory (quantity, blood_group)\n" +
                "VALUES (?, ?)\n" +
                "ON DUPLICATE KEY UPDATE quantity = quantity + " + appointment.getQuantity();

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connection = connectNow.getConnection();



        try{
            PreparedStatement statement = connection.prepareStatement(query1);
            statement.setInt(1, appointment.getQuantity());
            statement.setString(2, donor.getBloodGroup());

            int rowsAffected = statement.executeUpdate();
            if(rowsAffected > 0)
            {
                System.out.println("Data inserted");
            }
            else {
                System.out.println("Data insertion failed");
            }

        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    */
   public boolean addToInventory(Appointment appointment) {
       Donor donor = appointment.getDonor();
       String insertQuery = "INSERT INTO inventory (quantity, blood_group) VALUES (?, ?) " +
               "ON DUPLICATE KEY UPDATE quantity = quantity + ?";

       // Use try-with-resources for automatic closing of resources
       try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
            PreparedStatement statement = connection.prepareStatement(insertQuery)) {

           // Set the values for the query
           statement.setInt(1, appointment.getQuantity());      // Quantity to insert
           statement.setString(2, donor.getBloodGroup());      // Donor's blood group
           statement.setInt(3, appointment.getQuantity());     // Quantity to update in case of duplicate key

           // Execute the query
           int rowsAffected = statement.executeUpdate();
           if (rowsAffected > 0) {
               System.out.println("Data inserted/updated successfully.");
               return true;
           } else {
               System.out.println("Data insertion failed.");
           }

       } catch (SQLException e) {
           System.out.println("Error during inventory update: " + e.getMessage());
           e.printStackTrace();
       }
       return false;
   }



    /*public ObservableList<Inventory> getInventoryList() {
        ObservableList<Inventory> list = FXCollections.observableArrayList();

        String query = "SELECT * FROM inventory";
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connection = connectNow.getConnection();
        Date sqlDate;


        try {
            PreparedStatement statement = connection.prepareStatement(query);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int inventoryID = resultSet.getInt("inventory_id");
                int quantity = resultSet.getInt("quantity");
                String bloodGroup = resultSet.getString("blood_group");

                Inventory inventory = new Inventory(inventoryID, quantity,bloodGroup);
                list.add(inventory);
            }
            return list;


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }*/
    public ObservableList<Inventory> getInventoryList() {
        ObservableList<Inventory> list = FXCollections.observableArrayList();
        String query = "SELECT * FROM inventory";

        // Use try-with-resources for automatic closing of resources
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int inventoryID = resultSet.getInt("inventory_id");
                int quantity = resultSet.getInt("quantity");
                String bloodGroup = resultSet.getString("blood_group");

                Inventory inventory = new Inventory(inventoryID, quantity, bloodGroup);
                list.add(inventory);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching inventory list: " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }


    /*public boolean updateInventory(int inventory_id, int quantity)
    {
        String query = "UPDATE inventory\n" +
                "SET quantity = " + quantity + "\n" +
                "WHERE inventory_id = " + inventory_id + " ;\n";

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connection = connectNow.getConnection();



        try{
            PreparedStatement statement = connection.prepareStatement(query);

            int rowsAffected = statement.executeUpdate();

            if(rowsAffected > 0)
            {
                System.out.println("Data inserted");
                return true;
            }
            else {
                System.out.println("Data insertion failed");
                return false;
            }

        }
        catch(SQLException e) {
            e.printStackTrace();
        }


        return false;
    }*/


    public boolean updateInventory(int inventory_id, int quantity) {
        String query = "UPDATE inventory SET quantity = ? WHERE inventory_id = ?";

        // Use try-with-resources for automatic closing of resources
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Set the parameters
            statement.setInt(1, quantity);
            statement.setInt(2, inventory_id);

            // Execute the update
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Inventory updated successfully.");
                return true;
            } else {
                System.out.println("Inventory update failed.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error during inventory update: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

}
