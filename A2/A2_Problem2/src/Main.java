import Service.DatabaseConnection;
import Service.Transaction;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        DatabaseConnection dbConnection = DatabaseConnection.getInstance();
        dbConnection.loadDrivers();
        dbConnection.testConnection();

        List<String> readQueries = new ArrayList<>();
        List<String> t1WriteQueries = new ArrayList<>();
        List<String> t2WriteQueries = new ArrayList<>();

        //T1 Transactions
        String t1Read = "SELECT * FROM FACULTY_MEMBER;";
        String t1UpdateRank = "UPDATE FACULTY_MEMBER SET FACULTY_RANK = 'PROFESSOR' WHERE EMPLOYEE_ID = 501100;";
        String t1UpdateSalary = "UPDATE FACULTY_MEMBER SET FACULTY_SALARY = 260000 WHERE EMPLOYEE_ID = 501100;";

        //T2 Transactions.
        //T2 reads from the same table.
        String t2UpdateRank = "UPDATE FACULTY_MEMBER SET FACULTY_RANK = 'RESEARCHER' WHERE EMPLOYEE_ID = 501100;";
        String t2Delete = "DELETE FROM FACULTY_MEMBER WHERE EMPLOYEE_ID = 501100";

        readQueries.add(t1Read);
        t1WriteQueries.add(t1UpdateRank);
        t1WriteQueries.add(t1UpdateSalary);
        t2WriteQueries.add(t2UpdateRank);
        t2WriteQueries.add(t2Delete);

       Thread t1 = new Thread(new Transaction(readQueries, t1WriteQueries));
       Thread t2 = new Thread(new Transaction(readQueries, t1WriteQueries));
       t1.start();
       t2.start();

    }
}