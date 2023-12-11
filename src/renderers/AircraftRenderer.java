import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Objects;

public class AircraftRenderer extends JLabel implements ListCellRenderer<Aircraft>{

    public AircraftRenderer(){
        setOpaque(true);
    }
    @Override
    public Component getListCellRendererComponent(JList<? extends Aircraft> list, Aircraft value, int index, boolean isSelected, boolean cellHasFocus) {
        String text = "REGISTRATION: " + value.getAircraftRegistration() + " TYPE: " + value.getType() + " PAX CAPACITY: " +
                String.valueOf(value.getPaxCapacity()) + " HOLD CAPACITY: " + String.valueOf(value.getHoldCapacity()) +
                " NOTES: " + (value.getNotes() == null ? "NONE" : value.getNotes());
        ImageIcon img = new ImageIcon("res/" + value.getType() + ".png");
        this.setIcon(img);
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
