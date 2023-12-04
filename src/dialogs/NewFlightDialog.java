import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;

public class NewFlightDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel timeLabel;
    private JTextField timeTextField;
    private JTextField flightNumberField;
    private JLabel cityLabel;
    private JTextField cityField;
    private JTextField airRegField;
    private JTextField notesField;
    private JRadioButton arrivalRadioButton;
    private JRadioButton departureRadioButton;
    private JLabel warningLabel;
    private JTextField dateField;
    private Flight fl;

    public NewFlightDialog(Connection conn, Flight newFlight) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        arrivalRadioButton.setSelected(true);
        timeLabel.setText("ETA:");
        cityLabel.setText("Origin:");
        fl = newFlight;
        warningLabel.setForeground(Color.RED);

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
        arrivalRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeLabel.setText("ETA:");
                cityLabel.setText("Origin:");
            }
        });
        departureRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeLabel.setText("ETD:");
                cityLabel.setText("Destination:");
            }
        });
    }

    private void onOK() {
        // add your code here
        String flightNo = flightNumberField.getText();
        String aircraftReg = airRegField.getText();
        String notes = notesField.getText();
        String time = timeTextField.getText();
        String city = cityField.getText();
        String date = dateField.getText();
        boolean arrSelected = arrivalRadioButton.isSelected();
        if(flightNo.isEmpty()) {
            warningLabel.setText("Flight Number cannot be empty!");
        } else if(time.isEmpty()) {
            warningLabel.setText((arrSelected ? "ETA" : "ETD") + " cannot be empty!");
        } else if(city.isEmpty()) {
            warningLabel.setText((arrSelected ? "Origin" : "Destination") + " cannot be empty!");
        } else if(aircraftReg.isEmpty()) {
            warningLabel.setText("Aircraft Registration cannot be empty!");
        } else if(date.isEmpty()) {
            warningLabel.setText("Date cannot be empty!");
        } else {
            fl.setFlightNumber(flightNo);
            fl.setAircraftReg(aircraftReg);
            fl.setNotes(notes.isEmpty() ? null : notes);
            fl.setDate(date);
            if(arrSelected) {
                fl.setType("ARRIVAL");
                fl.setEta(Integer.parseInt(time));
                fl.setOrigin(city);
                fl.setEtd(-1);
                fl.setDestination(null);
            } else {
                fl.setType("DESTINATION");
                fl.setEtd(Integer.parseInt(time));
                fl.setDestination(city);
                fl.setEta(-1);
                fl.setOrigin(null);
            }
            dispose();
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
