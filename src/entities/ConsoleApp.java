import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;
import java.util.Vector;

public class ConsoleApp {

    private static final Scanner inp = new Scanner(System.in);
    private static boolean loggedIn = false;
    private static User currentUser = new User();
    private static String selection = "0";
    private static Connection conn = null;
    private static boolean runEnabled = true;

    public static void run(Connection connection) {

        System.out.println("Welcome to AMS!");
        conn = connection;

        while (runEnabled) {
            if (!loggedIn) {
                login();
            } else if (currentUser.getUserType().equals("admin")) {
                showAdminMenu();
                switch (selection) {
                    case "1":
                        listUsers();
                        break;
                    case "2":
                        addUser();
                        break;
                    case "3":
                        removeUser();
                        break;
                    case "4":
                        editUser();
                        break;
                    case "5":
                        currentUser = new User();
                        loggedIn = false;
                        break;
                    default:
                        System.out.println("Invalid selection!");
                        break;
                }
            } else {
                System.out.println("Current user type is: " + currentUser.getUserType());
                System.out.println("Exiting");
                return;
            }
        }


    }

    private static void listUsers() {
        Vector<User> users = User.getUserVector(conn);
        for(User user : users) {
            System.out.println(user);
        }
        waitOnUser();
    }

    private static void consolePrompt() {
        System.out.print("|> ");
    }

    private static void waitOnUser() {
        System.out.print("Press enter to continue...");
        String input;
        do {
            input = inp.nextLine();
        }while (input.equals("\n"));
    }

    private static void login() {
        System.out.println("1.Log in");
        System.out.println("2.Exit");
        consolePrompt();
        selection = inp.nextLine();
        if (selection.equals("2")) {
            runEnabled = false;
        }
        if (selection.equals("1")) {
            System.out.println("Username:");
            consolePrompt();
            String username = inp.nextLine();

            System.out.println("Password:");
            consolePrompt();
            String password = inp.nextLine();

            User user = new User();
            user.loadFromDatabase(conn, username);
            if (password.equals(user.getPassword())) {
                loggedIn = true;
                currentUser = user;
            } else {
                System.out.println("Incorrect username or password!");
            }
        } else {
            System.out.println("Invalid selection!");
        }
    }

    private static void showAdminMenu() {
        System.out.println("1. List users");
        System.out.println("2. Add user");
        System.out.println("3. Remove user");
        System.out.println("4. Edit user");
        System.out.println("5. Log-out");
        consolePrompt();
        selection = inp.nextLine();
    }

    private static void addUser() {
        System.out.println("Username:");
        consolePrompt();
        String username = inp.nextLine();

        System.out.println("Password:");
        consolePrompt();
        String password = inp.nextLine();

        System.out.println("User type:");
        consolePrompt();
//        UserType userType = inp.nextLine();

//        User user = new User(username, password, userType);
//        user.saveToDatabase(conn);

    }

    private static void removeUser() {
        System.out.println("Username for user to remove:");
        consolePrompt();
        String username = inp.nextLine();

        User user = new User();
        user.loadFromDatabase(conn, username);

        if(user.getUid() == -1) {
            System.out.println("User does not exist!");
            return;
        } else {
            String sql = "DELETE FROM users WHERE uid = ?";
            try {
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setInt(1, user.getUid());
                statement.execute();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println("User " + username + " of type " + user.getUserType() + " removed");
        waitOnUser();

    }

    private static void editUser() {
        System.out.println("Username for user to edit:");
        consolePrompt();
        String username = inp.nextLine();

        User user = new User();
        user.loadFromDatabase(conn, username);

        if(user.getUid() == -1) {
            System.out.println("User does not exist!");
            waitOnUser();
        } else {
            boolean done = false;
            User newUser = new User(user.getUid(), user.getUsername(), user.getPassword(), user.getUserType());
            while(!done) {
                System.out.println("Select field to edit for user " + username + ":");
                System.out.println("1. Username");
                System.out.println("2. Password");
                System.out.println("3. User type");
                System.out.println("4. Save and exit");
                System.out.println("5. Cancel");
                consolePrompt();
                selection = inp.nextLine();
                switch (selection) {
                    case "1":
                        System.out.println("Enter new username:");
                        consolePrompt();
                        newUser.setUsername(inp.nextLine());
                        break;
                    case "2":
                        System.out.println("Enter new password:");
                        consolePrompt();
                        newUser.setPassword(inp.nextLine());
                        break;
                    case "3":
                        System.out.println("Enter new user type:");
                        consolePrompt();
//                        newUser.setUserType(inp.nextLine());
                        break;
                    case "4":
                        newUser.saveToDatabase(conn);
                        done = true;
                        break;
                    case "5":
                        return;
                    default:
                        System.out.println("Invalid selection");
                        break;
                }
            }

        }
    }

}
