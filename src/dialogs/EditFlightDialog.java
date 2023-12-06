import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;

public class EditFlightDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel timeLabel;
    private JTextField timeTextField;
    private JTextField flightNumberField;
    private JTextField airRegField;
    private JTextField notesField;
    private JRadioButton arrivalRadioButton;
    private JRadioButton departureRadioButton;
    private JLabel warningLabel;
    private JTextField dateField;
    private JLabel cityLabel;
    private JTextField cityField;
    private Flight flightToEdit;

    public EditFlightDialog(Connection conn, Flight flightToEdit) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        warningLabel.setForeground(Color.RED);
        this.flightToEdit = flightToEdit;
        if(flightToEdit.getType().equals("ARRIVAL")) {
            arrivalRadioButton.setSelected(true);
            cityLabel.setText("Origin:");
            cityField.setText(flightToEdit.getOrigin());
            timeLabel.setText("ETA:");
            timeTextField.setText(String.valueOf(flightToEdit.getEta()));
        } else {
            departureRadioButton.setSelected(true);
            cityLabel.setText("Destination:");
            cityField.setText(flightToEdit.getDestination());
            timeLabel.setText("ETD:");
            timeTextField.setText(String.valueOf(flightToEdit.getEtd()));
        }
        flightNumberField.setText(flightToEdit.getFlightNumber());
        airRegField.setText(flightToEdit.getAircraftReg());
        notesField.setText(flightToEdit.getNotes() == null ? "" : flightToEdit.getNotes());
        dateField.setText(flightToEdit.getDate());

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
                cityLabel.setText("Origin:");
                timeLabel.setText("ETA:");
            }
        });
        departureRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cityLabel.setText("Destination:");
                timeLabel.setText("ETD:");
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
        } else if(city.isEmpty()) {
            warningLabel.setText((arrSelected ? "Origin" : "Destination") + " cannot be empty!");
        } else if(time.isEmpty()) {
            warningLabel.setText((arrSelected ? "ETA" : "ETD") + " cannot be empty!");
        } else if(aircraftReg.isEmpty()) {
            warningLabel.setText("Aircraft Registration cannot be empty!");
        } else if(date.isEmpty()) {
            warningLabel.setText("Date cannot be empty!");
        } else {
            flightToEdit.setFlightNumber(flightNo);
            flightToEdit.setAircraftReg(aircraftReg);
            flightToEdit.setNotes(notes.isEmpty() ? null : notes);
            flightToEdit.setDate(date);
            if(arrSelected) {
                flightToEdit.setType("ARRIVAL");
                flightToEdit.setEta(Integer.parseInt(time));
                flightToEdit.setOrigin(city);
                flightToEdit.setEtd(-1);
                flightToEdit.setDestination(null);
            } else {
                flightToEdit.setType("DESTINATION");
                flightToEdit.setEtd(Integer.parseInt(time));
                flightToEdit.setDestination(city);
                flightToEdit.setEta(-1);
                flightToEdit.setOrigin(null);
            }
            dispose();
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
