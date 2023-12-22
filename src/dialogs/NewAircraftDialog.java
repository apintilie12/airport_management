import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.RescaleOp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

public class NewAircraftDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField aircraftRegField;
    private JTextField notesField;
    private JComboBox<String> typeBox;
    private JLabel warningLabel;

    private Connection conn;
    private Aircraft aircraft;
    private String types;

    public NewAircraftDialog(Connection conn, Aircraft aircraft) {
        this.conn = conn;
        this.aircraft = aircraft;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        warningLabel.setForeground(Color.RED);
        getTypes();

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

    private void getTypes() {
        String sql = "SELECT name FROM aircraft_type";
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                typeBox.addItem(rs.getString("name"));
            }
            statement.close();


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void onOK() {
        // add your code here
        String aircraftRegistration = aircraftRegField.getText();
        String type = (String) typeBox.getSelectedItem();
        String notes = notesField.getText();
        if(aircraftRegistration.isEmpty()) {
            warningLabel.setText("Aircraft Registration cannot be empty!");
        } else if(!aircraftRegistration.matches("[A-Z0-9]{2}-[A-Z0-9]{3}")) {
            warningLabel.setText("Aircraft Registration format: XX-XXX!");
        }
        else {
            aircraft.setAircraftRegistration(aircraftRegistration);
            aircraft.setType(type);
            aircraft.setNotes(notes.isEmpty() ? null : notes);
            if(aircraft.isInDatabase(conn, aircraftRegistration)) {
                warningLabel.setText("Aircraft already in database!");
            } else {
                aircraft.saveToDatabase(conn);
                aircraft.loadFromDatabase(conn, aircraftRegistration);
                dispose();
            }
        }
    }

    private void onCancel() {
        // add your code here if necessary
        aircraft.setAircraftRegistration(null);
        dispose();
    }
}
