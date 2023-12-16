import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;

public class EditUserDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField userTypeField;
    private JLabel warningLabel;
    private Connection conn;
    private User user;

    public EditUserDialog(Connection conn, User user) {
        this.conn = conn;
        this.user = user;
        usernameField.setText(user.getUsername());
        passwordField.setText(user.getPassword());
        userTypeField.setText(user.getUserType().name());
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

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
        String username = usernameField.getText();
        String pwd = new String(passwordField.getPassword());
        String userType = userTypeField.getText();
        warningLabel.setForeground(Color.RED);
        if (username.isEmpty()) {
            warningLabel.setText("Username cannot be empty!");
        } else if (pwd.isEmpty()) {
            warningLabel.setText("Password cannot be empty!");
        } else if (userType.isEmpty()) {
            warningLabel.setText("User type cannot be empty!");
        } else {
            user.setUsername(username);
            user.setPassword(pwd);
            user.setUserType(UserType.valueOf(userType));
            dispose();
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
