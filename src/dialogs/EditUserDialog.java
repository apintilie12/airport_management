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
    private JLabel warningLabel;
    private JComboBox<String> userTypeBox;
    private Connection conn;
    private User user;

    public EditUserDialog(Connection conn, User user) {
        ImageIcon appIcon = new ImageIcon("res/airport.png");
        this.setIconImage(appIcon.getImage());
        this.setTitle("Edit User");
        this.conn = conn;
        this.user = user;
        usernameField.setText(user.getUsername());
        passwordField.setText(user.getPassword());
        for(UserType u : UserType.values()) {
            userTypeBox.addItem(u.name());
        }
        userTypeBox.setSelectedItem(user.getUserType().name());
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
        String userType = (String) userTypeBox.getSelectedItem();
        warningLabel.setForeground(Color.RED);
        if(username.isEmpty()) {
            warningLabel.setText("Username cannot be empty!");
        } else if(!username.matches("^[a-zA-Z0-9]*$")) {
            warningLabel.setText("Username can only contain alphanumerics!");
        } else if(pwd.isEmpty()) {
            warningLabel.setText("Password cannot be empty!");
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
