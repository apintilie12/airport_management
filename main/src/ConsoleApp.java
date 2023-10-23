import java.sql.Connection;
import java.util.ArrayList;
import java.util.Scanner;

public class ConsoleApp {

    private static final Scanner inp = new Scanner(System.in);
    private static boolean loggedIn = false;
    private static User currentUser = new User();
    private static String selection = "0";
    private static Connection conn;
    private static boolean runEnabled = true;

    public static void run(Connection connection) {

        System.out.println("Welcome to AMS!");
        conn = connection;

        while (true && runEnabled) {
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
                        System.out.println("To be implemented");
                        break;
                    case "4":
                        System.out.println("To be implemented");
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
        ArrayList<User> users = User.getUserList(conn);
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
        String password = inp.nextLine();
        consolePrompt();

        System.out.println("User type:");
        String userType = inp.nextLine();
        consolePrompt();

        User user = new User(username, password, userType);
        user.saveToDatabase(conn);

    }

}
