import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;

public class NewUserDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField usernameTextField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JLabel warningLabel;
    private JComboBox<String> userTypeBox;
    private User newUser;
    private Connection conn;

    public NewUserDialog(Connection connection, User newUser) {
        this.conn = connection;
        this.newUser = newUser;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        for(UserType u : UserType.values()) {
            userTypeBox.addItem(u.name());
        }

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        String username = usernameTextField.getText();
        String pwd = new String(passwordField.getPassword());
        String confirmPwd = new String(confirmPasswordField.getPassword());
        String userType = (String) userTypeBox.getSelectedItem();
        warningLabel.setForeground(Color.RED);
        if (username.isEmpty()) {
            warningLabel.setText("Username cannot be empty!");
        } else if (pwd.isEmpty()) {
            warningLabel.setText("Password cannot be empty!");
//        } else if (userType.isEmpty()) {
//            warningLabel.setText("User type cannot be empty!");
        } else if (!pwd.equals(confirmPwd)) {
            warningLabel.setText("Passwords do not match!");
        } else {
            if (newUser.isInDatabase(conn, username)) {
                warningLabel.setText("User already in database!");
                return;
            }
            newUser.setUsername(username);
            newUser.setPassword(pwd);
            newUser.setUserType(UserType.valueOf(userType));
            dispose();
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

//    public static void main(String[] args) {
//        NewUserDialog dialog = new NewUserDialog();
//        dialog.pack();
//        dialog.setVisible(true);
//        System.exit(0);
//    }
}
