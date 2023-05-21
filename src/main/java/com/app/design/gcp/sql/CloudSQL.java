package com.app.design.gcp.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CloudSQL {

    // Replace with your Cloud SQL instance connection name
    private static final String INSTANCE_CONNECTION_NAME = "my-gcp-project:us-central1:my-cloud-sql-instance";

    // Replace with your Cloud SQL instance username
    private static final String USERNAME = "myusername";

    // Replace with your Cloud SQL instance password
    private static final String PASSWORD = "mypassword";

    // Replace with your database name
    private static final String DATABASE_NAME = "mydatabase";

    // JDBC URL format: jdbc:mysql://[HOST]:[PORT]/[DB_NAME]
    private static final String JDBC_URL = String.format("jdbc:mysql://google/%s?cloudSqlInstance=%s&"
            + "socketFactory=com.google.cloud.sql.mysql.SocketFactory", DATABASE_NAME, INSTANCE_CONNECTION_NAME);

    public static void main(String[] args) throws SQLException {
        // Connect to the Cloud SQL instance
        Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);

        // Create a table
        createTable(connection);

        // Insert some data
        insertData(connection);

        // Read the data
        readData(connection);

        // Update the data
        updateData(connection);

        // Delete the data
        deleteData(connection);

        // Close the connection
        connection.close();
    }

    private static void createTable(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate("CREATE TABLE mytable (id INT PRIMARY KEY, name VARCHAR(255))");
    }

    private static void insertData(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO mytable (id, name) VALUES (?, ?)");
        statement.setInt(1, 1);
        statement.setString(2, "John Doe");
        statement.executeUpdate();
    }

    private static void readData(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM mytable");
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            System.out.println("ID: " + id + ", Name: " + name);
        }
    }

    private static void updateData(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("UPDATE mytable SET name = ? WHERE id = ?");
        statement.setString(1,"Jane Doe");
				statement.setInt(2, 1);
				statement.executeUpdate();
				}
	private static void deleteData(Connection connection) throws SQLException {
	    PreparedStatement statement = connection.prepareStatement("DELETE FROM mytable WHERE id = ?");
	    statement.setInt(1, 1);
	    statement.executeUpdate();
	}
}
