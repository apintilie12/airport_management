import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {

//    private static ArrayList<Aircraft> aircraft;
//    private static ArrayList<Passenger> passengers;
//    private static ArrayList<Flight> flights;
//    private static ArrayList<Baggage> baggage;
//    private static ArrayList<User> users;

    private static Map<String, ArrayList<Persistable>> data;

    public static void main(String[] args) {
        System.out.println("Hello world!");
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        ConsoleApp.run(getDBConnection());
        //initialise();
//        String url = "jdbc:sqlite:db/test.db";
//        try {
//            Connection connection = DriverManager.getConnection(url);
//            DatabaseMetaData md = connection.getMetaData();
//            String[] types = {"TABLE"};
//            ResultSet res = md.getTables(null, null, "%",types);
//            while (res.next()) {
//                System.out.println(res.getString("TABLE_NAME"));
//            }
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
    }

    private static Connection getDBConnection() {
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

    private static void populateList(Connection connection, ArrayList<Persistable> list, String tableName) {

        String query = "SELECT * FROM " + tableName + ";";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet != null) {
                Persistable persistable;
                switch (tableName) {
                    default:
                        persistable = new User();
                        break;
                }

                while (resultSet.next()) {
                    list.add(persistable.loadFromDatabase(resultSet));
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

//        try {
//            String query = null;
//            PreparedStatement statement = connection.prepareStatement(query);
//            //statement.setString(1, listIdent);
//            ResultSet result = statement.executeQuery();
//            users = new ArrayList<User>();
//            User user;
//            while (result.next()) {
//                int uid = result.getInt("uid");
//                String username = result.getString("username");
//                String password = result.getString("password");
//                String userType = result.getString("user_type");
//
//                user = new User(uid, username, password, userType);
//                users.add(user);
//
//            }
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
    }

    private static void getDataFromDB(Connection connection) {
        if (connection == null) {
            System.out.println("Invalid connection");
            return;
        }
        try {
            data = new HashMap<>();
            ArrayList<String> tableNames = new ArrayList<>();
            DatabaseMetaData md = connection.getMetaData();
            String[] types = {"TABLE"};
            ResultSet res = md.getTables(null, null, "%", types);
            while (res.next()) {
                tableNames.add(res.getString("TABLE_NAME"));
                System.out.println(res.getString("TABLE_NAME"));
            }
            for (String name : tableNames) {
                ArrayList<Persistable> list = new ArrayList<>();
                System.out.println(name);
                populateList(connection, list, name);
                data.put(name, new ArrayList<>());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private static void initialise() {
        Connection connection;
        connection = getDBConnection();
        if (connection == null) {
            System.out.println("Connection to database could not be established.\nExiting");
            return;
        } else {
            System.out.println("Connection was established");
        }
        getDataFromDB(connection);
        System.out.println(data);
    }
}
