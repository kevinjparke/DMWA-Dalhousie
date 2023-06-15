
import services.DatabaseConnection;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        //Check for loaded drivers and connect to database
        DatabaseConnection.loadDrivers();
        DatabaseConnection.testConnection();

        //Transactions
        executeTransactions();
    }
    
    private void executeTransactions() {
        String localDBURL = DatabaseConnection.getLocalDB();
        String remoteDBURL = DatabaseConnection.getRemoteDB();
        String selectStatement1 = "SELECT * FROM PARK;";
        String selectStatement2 = "SELECT * FROM EMPLOYEE";
        String updateStatement1 = "UPDATE PARK SET MANAGER_EMPLOYEE_ID = 342118 WHERE PARK_ID = 102010";
        String updateStatement2 = "UPDATE EMPLOYEE SET PARK_ID = 102010 WHERE EMPLOYEE_ID = 342118";

        /**
         * Try-with-resources block automatically closes connections
         */
        try (Connection localConnection = DriverManager.getConnection(localDBURL, "root", "Kb1nkb3qnv34");
             Connection remoteConnection = DriverManager.getConnection(remoteDBURL, "root", "lab4password1");

             //Set profile parameters of each connection
             PreparedStatement localPStmnt = localConnection.prepareStatement("set profiling=1");
             PreparedStatement remotePStmnt = remoteConnection.prepareStatement("set profiling=1");


             //Execute select statement on local repository
             PreparedStatement selectParkPStmnt = localConnection.prepareStatement(selectStatement1);

             //Execute update statement on local repository
             PreparedStatement updateParkPStmnt = localConnection.prepareStatement(updateStatement1);

             //Execute select statement on remote repository
             PreparedStatement selectEmployeePStmnt = remoteConnection.prepareStatement(selectStatement2);

             //Execute update statement on remote repository
             PreparedStatement updateEmployeePStmnt = remoteConnection.prepareStatement(updateStatement2);

             //Show profiles on server.
             PreparedStatement showLocalProfilesStmnt = localConnection.prepareStatement("show profiles;");
             PreparedStatement showRemoteProfilesStmnt = remoteConnection.prepareStatement("show profiles;")
        ) {
            //Disable autocommit
            localConnection.setAutoCommit(false);
            remoteConnection.setAutoCommit(false);

            try {
                localPStmnt.executeUpdate();
                remotePStmnt.executeUpdate();

                //Run park select statement
                ResultSet parkResultSet = selectParkPStmnt.executeQuery();
                printParkResultSet(parkResultSet);

                //Update park statement
                updateParkPStmnt.executeUpdate();

                //Run employee select statement
                ResultSet employeeResultSet = selectEmployeePStmnt.executeQuery();
                printEmployeeResultSet(employeeResultSet);

                //Run employee update statement
                updateEmployeePStmnt.executeUpdate();

                //Commit if transaction is successful
                localConnection.commit();
                remoteConnection.commit();

                //Execute show profiles statement
                ResultSet localResults = showLocalProfilesStmnt.executeQuery();
                ResultSet remoteResults = showRemoteProfilesStmnt.executeQuery();

                //print results
                printQueryExecutionTime(localResults);
                printQueryExecutionTime(remoteResults);

                System.out.println("Transaction was successful!");
            } catch (SQLException e) {
                System.out.println("Transaction Failed: ");
                System.out.println(e);

                //Rollback if any of the transactions fails.
                localConnection.rollback();
                remoteConnection.rollback();
            }
        } catch (SQLException e) {
            System.out.println("Failed to execute transaction:");
            System.out.println(e);
        }
    }

    private void printParkResultSet(ResultSet resultSet) throws SQLException{
        while (resultSet.next()) {
            Integer parkID = resultSet.getInt(1);
            String parkName = resultSet.getString(2);
            String parkDescription = resultSet.getString(3);
            String parkType = resultSet.getString(4);
            String parkRegion = resultSet.getString(5);
            Integer managerEmployeeId = resultSet.getInt(6);

            System.out.println("PARK_ID: " + parkID + "\n" +
                    "PARK_NAME: " + parkName + "\n" +
                    "PARK_DESCRIPTION: " + parkDescription + "\n" +
                    "PARK_TYPE: " + parkType + "\n" +
                    "PARK_REGION: " + parkRegion + "\n" +
                    "MANAGER_EMPLOYEE_ID: " + managerEmployeeId + "\n");
        }
    }

    private void printEmployeeResultSet(ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            Integer employeeId = resultSet.getInt(1);
            String firstName = resultSet.getString(2);
            String lastName = resultSet.getString(3);
            String email = resultSet.getString(4);
            String role = resultSet.getString(5);
            String address = resultSet.getString(6);
            Integer parkId = resultSet.getInt(7);

            System.out.println("EMPLOYEE_ID: " + employeeId + "\n" +
                    "EMPLOYEE_FNAME: " + firstName + "\n" +
                    "EMPLOYEE_LNAME: " + lastName + "\n" +
                    "EMPLOYEE_EMAIL: " + email + "\n" +
                    "EMPLOYEE_ROLE: " + role + "\n" +
                    "EMPLOYEE_ADDRESS: " + address + "\n" +
                    "PARK_ID: " + parkId + "\n");
        }
    }

    private void printQueryExecutionTime(ResultSet results) throws SQLException {
        while (results.next()){
            System.out.println("Query ID:   " + results.getInt(1));
            System.out.println("Query Time: " + results.getDouble(2) + "s");
            System.out.println("Query:      " +results.getString(3));
            System.out.println();
        }
    }
 }