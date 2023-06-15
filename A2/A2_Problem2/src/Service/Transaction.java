package Service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class Transaction implements Runnable {
    //This transaction only operates on a faculty_member table when a faculty_member is promoted.
    private List<String> readQueries;
    private List<String> writeQueries;
    private DatabaseConnection localDB;

    public Transaction(List<String> readQueries, List<String> writeQueries) {
        this.readQueries = readQueries;
        this.writeQueries = writeQueries;
        this.localDB = DatabaseConnection.getInstance();
    }

    @Override
    public void run() {
        operation();
    }

    public void operation() {
        try(Connection connection = localDB.getConnection();
            Statement statement = connection.createStatement()
        ) {
            try {
                //Lock tables for transaction
                acquireLocks(statement);

                //Transactions
                System.out.println("-----------------------------------");
                System.out.println("       TRANSACTION STARTED       ");
                System.out.println("-----------------------------------");

                //Execute read queries
                if (readQueries != null) {
                    for(String query : readQueries) {
                        ResultSet results = statement.executeQuery(query);
                        printQueryResults(results);
                    }
                }

                //Execute all write queries
                if(writeQueries != null) {
                    for(String query : writeQueries) {
                        statement.execute(query);
                    }
                }
                //Commit
                connection.commit();
                //Release locks and commit
                releaseLocks(statement);
                System.out.println();
                System.out.println("Locks released.");
                System.out.println("-----------------------------------");
                System.out.println("       TRANSACTION COMMITTED       ");
                System.out.println("-----------------------------------");
                System.out.println();
            } catch (SQLException e) {
                System.out.println("Something went wrong while executing the transaction.");
                connection.rollback();
            }
        } catch (SQLException e) {
            System.out.println("There was a problem executing transaction");
            System.out.println(e);
        }
    }

    public void acquireLocks(Statement statement) {
        try{
            statement.execute("LOCK TABLES FACULTY_MEMBER WRITE;");
            System.out.println("Transaction acquired locks");
        } catch(SQLException e) {
            System.out.println("Error acquiring locks!");
            System.out.println(e);
        }
    }

    public void releaseLocks(Statement statement) {
        try{
            statement.execute("UNLOCK TABLES;");
        } catch(SQLException e) {
            System.out.println("Error releasing locks");
            System.out.println(e);
        }
    }

    public void printQueryResults(ResultSet results) throws SQLException {
        while (results.next()) {
            System.out.println("Employee ID: " + results.getInt(1));
            System.out.println("Faculty ID: " + results.getInt(2));
            System.out.println("Faculty Rank: " + results.getString(3));
        }
    }
}
