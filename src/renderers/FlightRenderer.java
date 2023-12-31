import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Objects;

public class FlightRenderer extends JLabel implements ListCellRenderer<Flight>{

    public FlightRenderer(){
        setOpaque(true);
    }
    @Override
    public Component getListCellRendererComponent(JList<? extends Flight> list, Flight value, int index, boolean isSelected, boolean cellHasFocus) {
        String text;
        ImageIcon img;
        if(value.getType().equals("ARRIVAL")) {
            text = "FLIGHT: " + value.getFlightNumber() + " ORIGIN: " + value.getOrigin() + " ETA: " + value.getEta() + " AIRCRAFT: " +
                    value.getAircraftReg() + " NOTES: " + (value.getNotes() == null ? "NONE" : value.getNotes()) + " DATE: " + value.getDate();
            img = new ImageIcon("res/arrival.png");
        } else {
            text = "FLIGHT: " + value.getFlightNumber() + " DESTINATION: " + value.getDestination() + " ETD: " + value.getEtd() + " AIRCRAFT: " +
                    value.getAircraftReg() + " NOTES: " + (value.getNotes() == null ? "NONE" : value.getNotes()) + " DATE: " + value.getDate();
            img = new ImageIcon("res/departure.png");
        }
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
