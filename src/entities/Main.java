import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.sql.*;

public class Main {
    public static void main(String[] args) {
        normalRun();
//        test();
    }

    private static void test() {
//        String date = ""
    }

    private static void normalRun() {
        //        System.out.println("Hello world!");
//        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        Connection conn = getDBConnection();
        setAppWideFontSize(14);
//        User user = new User();
//        user.loadFromDatabase(conn, "Mr. Stark");
        LoginWindow mainApp = new LoginWindow();
//        AdminHomeWindow adm = new AdminHomeWindow(new JFrame(""), conn, user, "air");
//        ConsoleApp.run(getDBConnection());
    }

    private static void setAppWideFontSize(int size) {
        UIDefaults defaults = UIManager.getDefaults();

        // Iterate over all keys and update the font size for Font values
        for (Object key : defaults.keySet()) {
            Object value = defaults.get(key);

            if (value instanceof FontUIResource fontUIResource) {
                Font newFont = fontUIResource.deriveFont((float) size);
                defaults.put(key, new FontUIResource(newFont));
            }
        }

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
