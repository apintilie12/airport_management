import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class LoginScreen {
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

    public LoginScreen() {
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
        mainFrame.setMinimumSize(new Dimension(600, 600));
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
        conn = Main.getDBConnection();
        SwingUtilities.getRootPane(loginButton).setDefaultButton(loginButton);
    }

    private void doLogin() {
        username = usernameField.getText();
        password = new String(passwordField.getPassword());
        try {
            login();
            if(currentUser.getUserType().equals("admin")) {
                new AdminHomeScreen(mainFrame, currentUser);
            } else if (currentUser.getUserType().equals("ground")) {

            } else {

            }
//            JOptionPane.showMessageDialog(null, "Current user: " + currentUser);
        } catch (Exception exc) {
            errorLabel.setText("Invalid username or password!");
            errorLabel.setForeground(Color.RED);
            usernameField.setText("");
            passwordField.setText("");
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
