import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Vector;
import java.util.List;

public class AdminHomeWindow {

    private JTabbedPane tabbedPane1;
    private JLabel welcomeLabel;
    private JPanel content;
    private JList<User> userList;
    private JButton newUserButton;
    private JButton editUserButton;
    private JButton removeUserButton;
    private JButton logoutButton;
    private JList flightList;
    private JButton addFlightButton;
    private JButton editFlightButton;
    private JButton removeFlightButton;
    private JFrame currentFrame;
    private JFrame previousFrame;
    private User user;
    private final Connection conn;

    private Vector<User> users;
    private Vector<Flight> flights;

    public AdminHomeWindow(JFrame previousFrame, Connection conn, User currentUser) {
        this.user = currentUser;
        this.conn = conn;
        this.previousFrame = previousFrame;
        this.previousFrame.setVisible(false);


        currentFrame = new JFrame("AMS");
        currentFrame.setFocusable(true);
        currentFrame.setContentPane(content);
        currentFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        currentFrame.setMinimumSize(new Dimension(800, 800));
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
                for (User usr : selectedValuesList) {
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
                if (selectedValuesList.size() > 1) {
                    JOptionPane.showMessageDialog(currentFrame, "Cannot edit multiple users simultaneously!", "ERROR", JOptionPane.ERROR_MESSAGE);
                } else if (selectedValuesList.size() == 1) {
                    EditUserDialog dialog = new EditUserDialog(conn, selectedValuesList.get(0));
                    dialog.pack();
                    dialog.setLocationRelativeTo(null);
                    dialog.setVisible(true);
                    selectedValuesList.get(0).saveToDatabase(conn);
                    loadUsers();
                }
            }
        });
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentFrame.dispose();
                previousFrame.setVisible(true);
            }
        });
        addFlightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Flight nf = new Flight();
                NewFlightDialog dialog = new NewFlightDialog(conn, nf);
                dialog.pack();
                dialog.setLocationRelativeTo(null);
                dialog.setMinimumSize(new Dimension(310, 310));
                dialog.setVisible(true);
                if(nf.getFlightNumber() != null) {
                    nf.saveToDatabase(conn);
                    nf.loadFromDatabase(conn, nf.getFlightNumber());
                    flights.add(nf);
                    flightList.setListData(flights);
                }
            }
        });
        removeFlightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Flight> selectedValuesList = flightList.getSelectedValuesList();
                for (Flight fl : selectedValuesList) {
                    removeFlight(fl);
                    flights.remove(fl);
                }
                flightList.setListData(flights);
            }
        });
        editFlightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Flight> selectedValuesList = flightList.getSelectedValuesList();
                if (selectedValuesList.size() > 1) {
                    JOptionPane.showMessageDialog(currentFrame, "Cannot edit multiple flights simultaneously!", "ERROR", JOptionPane.ERROR_MESSAGE);
                } else if (selectedValuesList.size() == 1) {
                    EditFlightDialog dialog = new EditFlightDialog(conn, selectedValuesList.get(0));
                    dialog.pack();
                    dialog.setLocationRelativeTo(null);
                    dialog.setVisible(true);
                    dialog.setMinimumSize(new Dimension(300, 400));
                    selectedValuesList.get(0).saveToDatabase(conn);
                    loadFlights();
                }
            }
        });
        userList.addMouseListener(new MouseAdapter() {
        });
        flightList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(e.getClickCount() == 2) {
                    List<Flight> selectedValuesList = flightList.getSelectedValuesList();
                    if (selectedValuesList.size() > 1) {
                        JOptionPane.showMessageDialog(currentFrame, "Cannot edit multiple flights simultaneously!", "ERROR", JOptionPane.ERROR_MESSAGE);
                    } else if (selectedValuesList.size() == 1) {
                        EditFlightDialog dialog = new EditFlightDialog(conn, selectedValuesList.get(0));
                        dialog.pack();
                        dialog.setLocationRelativeTo(null);
                        dialog.setVisible(true);
                        dialog.setMinimumSize(new Dimension(300, 400));
                        selectedValuesList.get(0).saveToDatabase(conn);
                        loadFlights();
                    }
                }
            }
        });
    }

    private void removeFlight(Flight fl) {
        String sql = "DELETE FROM flights WHERE fid = ?";
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, fl.getFid());
            statement.execute();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public AdminHomeWindow(JFrame previousFrame, Connection conn, User currentUser, String defaultTab) {
        this(previousFrame, conn, currentUser);
        if(defaultTab.equals("usr")) {
            tabbedPane1.setSelectedIndex(0);
        } else if(defaultTab.equals("fl")) {
            tabbedPane1.setSelectedIndex(1);
        }
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
        flights = Flight.getFlightVector(conn);
        flightList.setListData(flights);
        flightList.setCellRenderer(new FlightRenderer());
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
