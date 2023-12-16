import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class BaggageRenderer extends JLabel implements ListCellRenderer<Baggage> {
    BaggageRenderer(){
        this.setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Baggage> list, Baggage value, int index, boolean isSelected, boolean cellHasFocus) {

        String text = "OWNER: " + value.getOwnerName() + " WEIGHT: " + value.getWeight() + " KG";
        ImageIcon img;
        if(value.getCategory().equals("HOLD")) {
            img = new ImageIcon("res/hold.png");
        } else {
            img = new ImageIcon("res/cabin.png");
        }
        this.setText(text);
        this.setIcon(img);
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
