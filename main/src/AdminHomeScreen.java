import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Vector;
import java.util.List;

public class AdminHomeScreen {

    private JTabbedPane tabbedPane1;
    private JLabel welcomeLabel;
    private JPanel content;
    private JList<User> userList;
    private JButton newUserButton;
    private JButton editUserButton;
    private JButton removeUserButton;
    private JFrame currentFrame;
    private User user;
    private final Connection conn;

    private Vector<User> users;
    private Vector<Flight> flights;

    public AdminHomeScreen(JFrame previousFrame, Connection conn, User currentUser) {
        this.user = currentUser;
        this.conn = conn;
        previousFrame.dispose();

        currentFrame = new JFrame("AMS");
        currentFrame.setFocusable(true);
        currentFrame.setContentPane(content);
        currentFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        currentFrame.setMinimumSize(new Dimension(600, 600));
        currentFrame.pack();
        currentFrame.setLocationRelativeTo(null);

        init();

        currentFrame.setVisible(true);
        tabbedPane1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (tabbedPane1.getSelectedIndex() == 0) {
                    loadUsers();
                } else {
                    loadFlights();
                }
            }
        });
        newUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                User newUser = new User();
                NewUserDialog dialog = new NewUserDialog(conn, newUser);
                dialog.pack();
                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);
                if (newUser.getUsername() != null) {
                    newUser.saveToDatabase(conn);
                    newUser.loadFromDatabase(conn, newUser.getUsername());
                    users.add(newUser);
                    userList.setListData(users);
                }
            }
        });
        removeUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<User> selectedValuesList = userList.getSelectedValuesList();
                for(User usr : selectedValuesList) {
                    removeUser(usr);
                    users.remove(usr);
                }
                userList.setListData(users);
            }
        });
        editUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<User> selectedValuesList = userList.getSelectedValuesList();
                if(selectedValuesList.size() > 1) {
                    JOptionPane.showMessageDialog(currentFrame, "Cannot edit multiple users simultaneously!", "ERROR", JOptionPane.ERROR_MESSAGE);
                } else if(selectedValuesList.size() == 1) {
                    EditUserDialog dialog = new EditUserDialog(conn, selectedValuesList.get(0));
                    dialog.pack();
                    dialog.setLocationRelativeTo(null);
                    dialog.setVisible(true);
                    selectedValuesList.get(0).saveToDatabase(conn);
                }
            }
        });
    }

    private void init() {
        welcomeLabel.setText("Welcome " + user.getUsername() + "!");
        loadUsers();
        loadFlights();
    }

    private void loadUsers() {
        users = User.getUserVector(conn);
        userList.setListData(users);
    }

    private void loadFlights() {
        System.out.println("Flights Loaded");
    }

    private void removeUser(User userToRemove) {
        String sql = "DELETE FROM users WHERE uid = ?";
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, userToRemove.getUid());
            statement.execute();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
