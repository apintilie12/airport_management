import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.util.List;
import java.util.Vector;

public class WorkerHomeWindow extends JFrame {
    private JLabel welcomeLabel;
    private JButton logoutButton;
    private JList<Persistable> contentList;
    private JButton openItemButton;
    private JPanel content;
    private Vector<Persistable> items;
    private Connection conn;
    private JFrame previousFrame;
    private UserType mode;
    private JFrame currentFrame;
    private User currentUser;

    WorkerHomeWindow(JFrame previousFrame, Connection conn, UserType mode, User currentUser) {
        this.conn = conn;
        this.previousFrame = previousFrame;
        this.mode = mode;
        this.currentUser = currentUser;
        previousFrame.setVisible(false);
        this.items = new Vector<>();

        currentFrame = new JFrame("AMS");
        currentFrame.setFocusable(true);
        currentFrame.setContentPane(content);
        currentFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        currentFrame.setMinimumSize(new Dimension(800, 800));
        currentFrame.pack();
        currentFrame.setLocationRelativeTo(null);

        init();

        WindowAdapter adapter = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                askSaveChanges(true);
            }
        };
        currentFrame.addWindowListener(adapter);

        currentFrame.setVisible(true);


        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                askSaveChanges(false);
            }
        });
        openItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                interactWithItem();
            }
        });
        contentList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(e.getClickCount() == 2) {
                    interactWithItem();
                }
            }
        });
    }

    private void interactWithItem() {
        List<Persistable> selectedItems = contentList.getSelectedValuesList();
        if(selectedItems.size() > 1) {
            JOptionPane.showMessageDialog(currentFrame, "Cannot open multiple items simultaneously!", "ERROR", JOptionPane.ERROR_MESSAGE);
        } else if(selectedItems.size() == 1) {
            if(mode == UserType.GROUND) {
                //TODO
                System.out.println("Did ground interaction");
            } else {
                //TODO
                System.out.println("Did checkin interaction");
                Flight flight = (Flight) selectedItems.get(0);
                FlightManifestWindow fm = new FlightManifestWindow(this.currentFrame, conn, flight);
            }
        }
    }

    private void askSaveChanges(boolean quitOnExit) {
        int option = JOptionPane.showConfirmDialog(null, "Save changes?");
        if(option == 2) {
            return;
        }
        if(option == 0) {
//            saveChanges();
            System.out.println("Saved changes");
        } else if(option == 1) {
            System.out.println("Didn't save changes");
        }
        if(quitOnExit) {
            System.exit(0);
        }
        previousFrame.setVisible(true);
        currentFrame.dispose();
    }

    private void saveChanges() {
        for(Persistable p : items) {
            p.saveToDatabase(conn);
        }
    }

    private void init() {
        welcomeLabel.setText("Currently logged in as: " + currentUser.getUsername());
        if(mode == UserType.GROUND) {
            openItemButton.setText("Open aircraft load window");
            loadAircraft();
        } else {
            openItemButton.setText("Open flight manifest window");
            loadFlights();
        }
    }

    private void loadFlights() {
        Vector<Flight> flights = Flight.getFlightVector(conn, "departure");
        for(Flight fl : flights) {
            items.add((Persistable) fl);
        }
        contentList.setListData(items);
        contentList.setCellRenderer(new PersistableRenderer("flight"));
//        contentList.setCellRenderer(new FlightRenderer());
    }

    private void loadAircraft() {
        Vector<Aircraft> aircrafts = Aircraft.getAircraftVector(conn);
        for(Aircraft air : aircrafts) {
            items.add((Persistable) air);
        }
        contentList.setListData(items);
        contentList.setCellRenderer(new PersistableRenderer("aircraft"));
//        contentList.setCellRenderer(new AircraftRenderer());
    }
}
