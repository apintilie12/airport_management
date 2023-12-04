import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class FlightRenderer extends JLabel implements ListCellRenderer<Flight>{

    public FlightRenderer(){}
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
        return this;
    }
}
