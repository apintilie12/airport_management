import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;

public class FlightManifestWindow {
    private JPanel content;
    private JTabbedPane tabbedPane1;
    private JLabel infoLabel;
    private JButton removePassengerButton;
    private JButton addPassengerButton;
    private JButton exitButton;
    private JList<Passenger> passengerList;
    private JButton addBaggageButton;
    private JButton removeBaggageButton;
    private JList<Baggage> baggageList;
    private JFrame previousFrame;
    private Connection connection;
    private Flight flight;
    private JFrame currentFrame;


    FlightManifestWindow(JFrame previousFrame, Connection connection, Flight flight) {
        this.previousFrame = previousFrame;
        this.connection = connection;
        this.flight = flight;
        previousFrame.setVisible(false);
        this.flight.loadBaggages(connection);
        this.flight.loadPassengers(connection);

        passengerList.setListData(flight.getPassengers());
        passengerList.setCellRenderer(new PassengerRenderer());

        baggageList.setListData(flight.getBaggages());
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

    }

    private void exit() {
        this.previousFrame.setVisible(true);
        this.currentFrame.dispose();
    }
}
