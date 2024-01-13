import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class LoginWindow {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JPanel mainPanel;
    private JLabel errorLabel;

    private String username;
    private String password;

    private JFrame mainFrame;

    private Connection conn;

    private User currentUser;

    public LoginWindow() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doLogin();
            }
        });

        init();

    }

    private void init() {
        mainFrame = new JFrame("AMS");
        mainFrame.setFocusable(true);
        mainFrame.setContentPane(mainPanel);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImageIcon appIcon = new ImageIcon("res/airport.png");
        mainFrame.setIconImage(appIcon.getImage());
        mainFrame.setMinimumSize(new Dimension(500, 500));
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        usernameField.requestFocusInWindow();
        mainFrame.setFocusTraversalPolicy(new LayoutFocusTraversalPolicy());
        mainFrame.setVisible(true);
        conn = Main.getDBConnection();
        SwingUtilities.getRootPane(loginButton).setDefaultButton(loginButton);
    }

    private void doLogin() {
        username = usernameField.getText();
        password = new String(passwordField.getPassword());
        try {
            login();
            usernameField.setText("");
            passwordField.setText("");
            errorLabel.setText("");
            if (currentUser.getUserType() == UserType.ADMIN) {
                new AdminHomeWindow(mainFrame, conn, currentUser);
                usernameField.requestFocusInWindow();
            } else {
                new WorkerHomeWindow(mainFrame, conn, currentUser.getUserType(), currentUser);
                usernameField.requestFocusInWindow();
            }
        } catch (Exception exc) {
            errorLabel.setText("Invalid username or password!");
            errorLabel.setForeground(Color.RED);
            usernameField.setText("");
            passwordField.setText("");
            usernameField.requestFocusInWindow();
        }
    }

    private void login() throws Exception {
        User user = new User();
        user.loadFromDatabase(conn, username);
        if (password.equals(user.getPassword())) {
            currentUser = user;
        } else {
            throw new Exception("Invalid Credentials");
        }

    }
}
