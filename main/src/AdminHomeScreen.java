import javax.swing.*;
import java.awt.*;

public class AdminHomeScreen {

    private JTabbedPane tabbedPane1;
    private JLabel welcomeLabel;
    private JPanel content;
    private JFrame currentFrame;
    private User user;

    public AdminHomeScreen(JFrame previousFrame, User currentUser) {
        this.user = currentUser;
        previousFrame.dispose();

        currentFrame = new JFrame("AMS");
        currentFrame.setFocusable(true);
        currentFrame.setContentPane(content);
        currentFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        currentFrame.setMinimumSize(new Dimension(600, 600));
        currentFrame.pack();
        currentFrame.setLocationRelativeTo(null);

        init();

        currentFrame.setVisible(true);
    }

    private void init() {
        welcomeLabel.setText("Welcome " + user.getUsername() + "!");
    }
}
