import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.util.Vector;

public class NewPassengerDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField phoneNoField;
    private JPanel infoPane;
    private JComboBox<Integer> bagNoBox;
    private JLabel warningLabel;
    private JPanel bagPane;
    private final int maxBags = 5;
    private Vector<JPanel> bagList;
    private Vector<JTextField> textFields;
    private JDialog currentDialog;
    private Flight flight;
    private Connection conn;

    public NewPassengerDialog(Connection conn, Flight flight) {
        ImageIcon appIcon = new ImageIcon("res/airport.png");
        this.setIconImage(appIcon.getImage());
        this.setTitle("Add Passenger");
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        warningLabel.setForeground(Color.RED);
        this.flight = flight;
        this.conn = conn;
        bagList = new Vector<>();
        textFields = new Vector<>();
        currentDialog = this;
        bagPane.setLayout(new GridLayout(0, 1));
        for(int i = 0; i <= maxBags; i++) {
            bagNoBox.addItem(i);
        }


        bagNoBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED) {
                    for(JPanel p : bagList) {
                        bagPane.remove(p);
                    }
                    bagList.clear();
                    textFields.clear();
                    int nrBox = (int) bagNoBox.getSelectedItem();
//                    int textFieldWidth = firstNameField.getColumns();
                    for(int i = 1; i <= nrBox; i++) {
                        JPanel panel = new JPanel();
                        panel.setLayout(new GridBagLayout());
                        JLabel label = new JLabel(String.format("Bag %d weight:", i));
                        JTextField textField = new JTextField(11);
                        panel.add(label);
                        panel.add(textField);
                        bagPane.add(panel);
                        bagList.add(panel);
                        textFields.add(textField);
                    }
                    currentDialog.revalidate();
                    currentDialog.pack();
                    currentDialog.repaint();

                }
            }
        });
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

    private void onOK() {
        // add your code here
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String phoneNumber = phoneNoField.getText();
        if(firstName.isEmpty()) {
            warningLabel.setText("First name cannot be empty!");
            this.pack();
        } else if(lastName.isEmpty()) {
            warningLabel.setText("Last name cannot be empty!");
            this.pack();
        } else {
            Vector<Baggage> bags = new Vector<>();
            for(JTextField tf : textFields) {
                String val = tf.getText();
                if(val.isEmpty()) {
                    warningLabel.setText("Bag weight cannot be empty!");
                    this.pack();
                    return;
                }
                try {
                    int weight = Integer.parseInt(val);
                    if(weight <= 0) {
                        warningLabel.setText("Bag weight must be strictly positive!");
                        this.pack();
                        return;
                    }
                    if(weight > 32) {
                        warningLabel.setText("Bag weight cannot exceed 32 kg!");
                        this.pack();
                        return;
                    }
                    Baggage bag = new Baggage();
                    bag.setWeight(weight);
                    bag.autoSetCategory();
                    bag.setFid(flight.getFid());
                    bags.add(bag);
                }catch(Exception e) {
                    warningLabel.setText("Bag weight must be integer!");
                    this.pack();
                    return;
                }
            }
            Passenger newPax = new Passenger();
            newPax.setFirstName(firstName);
            newPax.setLastName(lastName);
            newPax.setPhoneNumber(phoneNumber.isEmpty() ? null : phoneNumber);
            newPax.setFlightId(flight.getFid());
            newPax.saveToDatabase(conn);
            newPax.loadFromDatabase(conn);
            for(Baggage b : bags) {
                b.setPid(newPax.getPid());
                flight.addBaggage(b);
                b.saveToDatabase(conn);
            }
            dispose();
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
