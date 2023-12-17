import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class EditAircraftDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField aircraftRegField;
    private JTextField notesField;
    private JComboBox typeBox;
    private JLabel warningLabel;
    private Connection conn;
    private Aircraft air;

    public EditAircraftDialog(Connection connection, Aircraft air) {
        this.conn = connection;
        this.air = air;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        getTypes();
        aircraftRegField.setText(air.getAircraftRegistration());
        typeBox.setSelectedItem(air.getType());
        notesField.setText(air.getNotes() == null ? "" : air.getNotes());
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


        } catch(Exception e) {
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
        } else {
            air.setAircraftRegistration(aircraftRegistration);
            air.setType(type);
            air.setNotes(notes.isEmpty() ? null : notes);
//            air.setAid(0);
            dispose();
        }
    }

    private void onCancel() {
        // add your code here if necessary
        air.setAid(-1);
        dispose();
    }
}
