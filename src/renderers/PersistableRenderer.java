import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PersistableRenderer extends JLabel implements ListCellRenderer<Persistable> {
    private final String type;

    PersistableRenderer(String type) {
        this.type = type;
        this.setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Persistable> list, Persistable value, int index, boolean isSelected, boolean cellHasFocus) {
        String text;
        ImageIcon img;
        if(this.type.equals("flight")) {
            Flight flight = (Flight) value;
            if(flight.getType().equals("ARRIVAL")) {
                text = "FLIGHT: " + flight.getFlightNumber() + " ORIGIN: " + flight.getOrigin() + " ETA: " + flight.getEta() + " AIRCRAFT: " +
                        flight.getAircraftReg() + " NOTES: " + (flight.getNotes() == null ? "NONE" : flight.getNotes()) + " DATE: " + flight.getDate();
                img = new ImageIcon("res/arrival.png");
            } else {
                text = "FLIGHT: " + flight.getFlightNumber() + " DESTINATION: " + flight.getDestination() + " ETD: " + flight.getEtd() + " AIRCRAFT: " +
                        flight.getAircraftReg() + " NOTES: " + (flight.getNotes() == null ? "NONE" : flight.getNotes()) + " DATE: " + flight.getDate();
                img = new ImageIcon("res/departure.png");
            }
            
        } else {
            Aircraft aircraft = (Aircraft)value; 
            text = "REGISTRATION: " + aircraft.getAircraftRegistration() + " TYPE: " + aircraft.getType() + " PAX CAPACITY: " +
                    String.valueOf(aircraft.getPaxCapacity()) + " HOLD CAPACITY: " + String.valueOf(aircraft.getHoldCapacity()) +
                    " NOTES: " + (aircraft.getNotes() == null ? "NONE" : aircraft.getNotes());
            img = new ImageIcon("res/" + aircraft.getType() + ".png");
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
