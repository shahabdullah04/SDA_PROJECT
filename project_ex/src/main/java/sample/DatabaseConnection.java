package sample;

import java.sql.*;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection databaseLink;

    public DatabaseConnection() {

        String url = "jdbc:mysql://localhost:3306/lifeflow";  // Ensure your database name is correct
        String databaseUser= "root";
        String databasePassword = "Admin@123";


    //        String databaseName = "hemohub";
    //      String databaseUser = "root";
    //    String databasePassword = "1466";
        //  String url = "jdbc:mysql://localhost/" + databaseName;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);
        } catch (SQLException | ClassNotFoundException var6) {
            Exception e = var6;
            ((Exception)e).printStackTrace();
        }
        /*
        String url = "jdbc:mysql://localhost:3306/lifeflow";  // Ensure your database name is correct
        String databaseUser= "root";
        String databasePassword = "Admin@123";
        try (Connection conn = DriverManager.getConnection(url, databaseUser, databasePassword);
             Statement stmt = conn.createStatement())
             //ResultSet rs = stmt.executeQuery(query))
        {

           // System.out.println("List of Cars and their Rental Status:");


        } catch (SQLException e) {
            //System.out.println("Error displaying car details: " + e.getMessage());
            //e.printStackTrace();
        }*/

    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }

        return instance;
    }

    public Connection getConnection() {
        return this.databaseLink;
    }
}
