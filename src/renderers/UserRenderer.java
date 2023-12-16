import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class UserRenderer extends JLabel implements ListCellRenderer<User>{

    public UserRenderer(){
        setOpaque(true);
    }
    @Override
    public Component getListCellRendererComponent(JList<? extends User> list, User value, int index, boolean isSelected, boolean cellHasFocus) {
//        String text = String.format("USERNAME: %-25s TYPE: %s", value.getUsername(), value.getUserType());
        String text = "USERNAME: " + value.getUsername() + " TYPE: " + value.getUserType();
        ImageIcon img;
        if(value.getUserType() == UserType.ADMIN) {
            img = new ImageIcon("res/admin.png");
        } else if(value.getUserType() == UserType.GROUND) {
            img = new ImageIcon("res/ground.png");
        } else {
            img = new ImageIcon("res/checkin.png");
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
