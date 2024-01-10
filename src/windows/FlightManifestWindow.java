import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

public class FlightManifestWindow {
    private JPanel content;
    private JTabbedPane tabbedPane1;
    private JLabel infoLabel;
    private JButton removePassengerButton;
    private JButton addPassengerButton;
    private JButton exitButton;
    private JList<Passenger> passengerList;
    private JButton removeBaggageButton;
    private JList<Baggage> baggageList;
    private JLabel paxCapLabel;
    private JLabel bagCapLabel;
    private JComboBox<String> paxOrderComboBox;
    private JComboBox<String> bagOrderComboBox;
    private JFrame previousFrame;
    private Connection connection;
    private Flight flight;
    private JFrame currentFrame;
    private int cargoFill;


    FlightManifestWindow(JFrame previousFrame, Connection connection, Flight flight) {
        this.previousFrame = previousFrame;
        this.connection = connection;
        this.flight = flight;
        cargoFill = 0;
        previousFrame.setVisible(false);

        init();

        updatePassengers();
        updateBaggages();

        passengerList.setCellRenderer(new PassengerRenderer());
        baggageList.setCellRenderer(new BaggageRenderer());

        infoLabel.setText("Flight manifest for: " + flight.getFlightNumber());
        infoLabel.setBorder(new EmptyBorder(10, 0, 10, 0));


        currentFrame = new JFrame("AMS");
        currentFrame.setFocusable(true);
        currentFrame.setContentPane(content);
        currentFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        currentFrame.setMinimumSize(new Dimension(800, 800));
        currentFrame.pack();
        currentFrame.setLocationRelativeTo(null);

        currentFrame.setVisible(true);

        currentFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                exit();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exit();
            }
        });

        tabbedPane1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if(tabbedPane1.getSelectedIndex() == 0) {
                    updatePassengers();
                } else {
                    updateBaggages();
                }
            }
        });

        addPassengerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPassenger();
            }
        });
        removeBaggageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Baggage> selectedItems = baggageList.getSelectedValuesList();
                for(Baggage b : selectedItems) {
                    removeBag(b);
                }
                updateBaggages();
            }
        });
        removePassengerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Passenger> selectedItems = passengerList.getSelectedValuesList();
                for(Passenger p : selectedItems) {
                    removePassenger(p);
                }
                updatePassengers();
            }
        });
    }

    private void init() {
        String[] paxSort = {"DEFAULT", "FIRST NAME ASC", " FIRST NAME DESC", "LAST NAME ASC", "LAST NAME DESC", "PHONE NO"};
        for(String val : paxSort) {
            paxOrderComboBox.addItem(val);
        }
        paxOrderComboBox.setSelectedItem("DEFAULT");
        paxOrderComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED) {
                    updatePassengers();
                }
            }
        });

        String[] bagSort = {"DEFAULT", "WEIGHT ASC", "WEIGHT DESC", "TYPE ASC", "TYPE DESC"};
        for(String val : bagSort) {
            bagOrderComboBox.addItem(val);
        }
        bagOrderComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED) {
                    updateBaggages();
                }
            }
        });
    }

    private void removePassenger(Passenger p) {
        String sql = "DELETE FROM baggage WHERE pid = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, p.getPid());
            statement.execute();
            sql = "DELETE FROM passengers where pid = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, p.getPid());
            statement.execute();
            statement.close();
        }catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void removeBag(Baggage b) {
        String sql = "DELETE FROM baggage WHERE bid = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, b.getBid());
            statement.execute();
            statement.close();
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void addPassenger() {
        NewPassengerDialog dialog = new NewPassengerDialog(this.connection, this.flight);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        updatePassengers();
    }

    private void updateBaggages() {
        flight.loadBaggages(connection, (String) bagOrderComboBox.getSelectedItem());
        baggageList.setListData(flight.getBaggages());
        cargoFill = 0;
        for(Baggage b : flight.getBaggages()) {
            cargoFill += b.getWeight();
        }
        updateBagsLabel();
    }

    private void updatePassengers() {
        flight.loadPassengers(connection, (String) paxOrderComboBox.getSelectedItem());
        passengerList.setListData(flight.getPassengers());
        updatePaxLabel();
    }

    private void updatePaxLabel() {
        paxCapLabel.setText("Filled seats: " + passengerList.getModel().getSize() + "/" + flight.getMaxPax());
    }

    private void updateBagsLabel() {
        bagCapLabel.setText("Cargo hold fill: " + cargoFill + "/" + flight.getMaxBags() + " KG");
    }

    private void exit() {
        this.previousFrame.setVisible(true);
        this.currentFrame.dispose();
    }
}
