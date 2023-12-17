import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeListener;
import java.sql.Connection;

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
    }

    private void addPassenger() {
        NewPassengerDialog dialog = new NewPassengerDialog(this.connection, this.flight);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        updatePassengers();
    }

    private void updateBaggages() {
        flight.loadBaggages(connection);
        baggageList.setListData(flight.getBaggages());
        cargoFill = 0;
        for(Baggage b : flight.getBaggages()) {
            cargoFill += b.getWeight();
        }
        updateBagsLabel();
    }

    private void updatePassengers() {
        flight.loadPassengers(connection);
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
