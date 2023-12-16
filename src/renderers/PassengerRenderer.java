import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PassengerRenderer extends JLabel implements ListCellRenderer<Passenger> {

    PassengerRenderer(){
        this.setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Passenger> list, Passenger value, int index, boolean isSelected, boolean cellHasFocus) {
        String text = "NAME: " + value.getFirstName() + " " + value.getLastName() + " PHONE NUMBER: " +
                (value.getPhoneNumber() == null ? "N/A" : value.getPhoneNumber());
        this.setText(text);

        this.setVerticalTextPosition(CENTER);
        this.setBorder(new EmptyBorder(5, 5, 5, 0));
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        return this;
    }
}
