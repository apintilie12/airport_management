import javax.swing.*;

public class AircraftState {
    private int currentState;
    private int maxCapacity;
    private int loadQuantity;
    private int loadInterval;
    private JLabel infoLabel;
    private String suffix;

    AircraftState(int currentState, int maxCapacity, int loadQuantity, int loadInterval, JLabel infoLabel, String suffix) {
        this.currentState = currentState;
        this.maxCapacity = maxCapacity;
        this.loadQuantity = loadQuantity;
        this.loadInterval = loadInterval;
        this.infoLabel = infoLabel;
        this.suffix = suffix;

        infoLabel.setText(String.format("%d/%d %s", currentState, maxCapacity, suffix));
    }

    public synchronized void tick() {
        if(currentState + loadQuantity <= maxCapacity) {
            currentState += loadQuantity;
        } else {
            currentState = maxCapacity;
        }
        infoLabel.setText(String.format("%d/%d %s", currentState, maxCapacity, suffix));
    }

    public int getCurrentState() {
        return currentState;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public int getLoadInterval() {
        return loadInterval;
    }
}
