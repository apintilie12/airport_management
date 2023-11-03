import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        Connection conn = getDBConnection();
        User user = new User();
        user.loadFromDatabase(conn, "Mr. Stark");
//        LoginScreen mainApp = new LoginScreen();
        AdminHomeScreen adm = new AdminHomeScreen(new JFrame(""), conn, user);
//        ConsoleApp.run(getDBConnection());

    }

    public static Connection getDBConnection() {
        Connection connection;
        try {
            String url = "jdbc:sqlite:db/test.db";
            connection = DriverManager.getConnection(url);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
        return connection;
    }

}
