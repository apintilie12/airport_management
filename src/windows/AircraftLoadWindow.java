import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static java.lang.Thread.sleep;

public class AircraftLoadWindow {
    private JPanel content;
    private JLabel welcomeLabel;
    private JButton backButton;
    private JLabel imageLabel;
    private JFrame previousFrame;
    private JFrame currentFrame;
    private Aircraft aircraft;
    private volatile boolean paxThreadRunning;
    private Thread paxThread;
    private AircraftState paxState;
    private volatile boolean bagThreadRunning;
    private Thread bagThread;
    private AircraftState bagState;
    private volatile boolean fuelThreadRunning;
    private Thread fuelThread;
    private AircraftState fuelState;

    AircraftLoadWindow(JFrame previousFrame, Aircraft aircraft) {
        paxThreadRunning = false;
        bagThreadRunning = false;
        fuelThreadRunning = false;
        this.previousFrame = previousFrame;
        this.aircraft = aircraft;
        previousFrame.setVisible(false);

        currentFrame = new JFrame("AMS");
        currentFrame.setFocusable(true);
        currentFrame.setContentPane(content);
        currentFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        init();

        currentFrame.setMinimumSize(new Dimension(800, 800));
        currentFrame.pack();
        currentFrame.setLocationRelativeTo(null);

        WindowAdapter adapter = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                back();
            }
        };

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                back();
            }
        });

        currentFrame.addWindowListener(adapter);


        currentFrame.setVisible(true);
    }

    private void back() {
        previousFrame.setVisible(true);
        currentFrame.dispose();
    }

    private void init() {
        welcomeLabel.setText("Selected aircraft: " + aircraft.getAircraftRegistration());

        setupGrid();
    }

    private JPanel getPassengerPanel() {
        GridBagConstraints gc = new GridBagConstraints();
        JPanel paxPan = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        paxPan.setLayout(layout);
        paxPan.setOpaque(false);

        JButton startButton = new JButton("Start Boarding");
        JButton stopButton = new JButton("Stop Boarding");
        JLabel paxLabel = new JLabel("pax LABEL");
        paxState = new AircraftState(0, aircraft.getPaxCapacity(), 1, 1, paxLabel, "PAX");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!paxThreadRunning) {
                    paxThreadRunning = true;
                    paxThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while(paxThreadRunning && paxState.getCurrentState() < paxState.getMaxCapacity()) {
                                paxState.tick();
                                try {
                                    sleep(paxState.getLoadInterval() * 1000L);
                                } catch(Exception exc) {
                                    System.out.println(exc.getMessage());
                                }
                            }
                            paxThreadRunning = false;
                        }
                    });
                    paxThread.start();
                }
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paxThreadRunning = false;
                if(paxThread != null) {
                    try {
                        paxThread.join();
                    } catch(Exception exc) {
                        System.out.println(exc.getMessage());
                    }
                }
            }
        });

        gc.weighty = 0.5;
        gc.gridx = 0;
        gc.gridy = 0;
        paxPan.add(startButton, gc);

        gc.gridy = GridBagConstraints.RELATIVE;
        paxPan.add(stopButton, gc);

        gc.weighty = 1;
        gc.gridx = GridBagConstraints.RELATIVE;
        gc.gridy = GridBagConstraints.REMAINDER;
        gc.gridheight = 2;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.VERTICAL;
        paxPan.add(paxLabel, gc);

        return paxPan;
    }

    private JPanel getBaggagePanel() {
        GridBagConstraints gc = new GridBagConstraints();
        JPanel bagPan = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        bagPan.setLayout(layout);
        bagPan.setOpaque(false);

        JButton startButton = new JButton("Start Bag Loading");
        JButton stopButton = new JButton("Stop Bag Loading");
        JLabel bagLabel = new JLabel("bag LABEL");

        bagState = new AircraftState(0, aircraft.getHoldCapacity(), 25, 1, bagLabel, "kg");

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!bagThreadRunning) {
                    bagThreadRunning = true;
                    bagThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while(bagThreadRunning && bagState.getCurrentState() < bagState.getMaxCapacity()) {
                                bagState.tick();
                                try {
                                    sleep(bagState.getLoadInterval() * 1000L);
                                } catch(Exception exc) {
                                    System.out.println(exc.getMessage());
                                }
                            }
                            bagThreadRunning = false;
                        }
                    });
                    bagThread.start();
                }
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bagThreadRunning = false;
                if(bagThread != null) {
                    try {
                        bagThread.join();
                    } catch(Exception exc) {
                        System.out.println(exc.getMessage());
                    }
                }
            }
        });

        gc.weighty = 0.5;
        gc.gridx = 0;
        gc.gridy = 0;
        bagPan.add(startButton, gc);

        gc.gridy = GridBagConstraints.RELATIVE;
        bagPan.add(stopButton, gc);

        gc.weighty = 1;
        gc.gridx = GridBagConstraints.RELATIVE;
        gc.gridy = GridBagConstraints.REMAINDER;
        gc.gridheight = 2;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.VERTICAL;
        bagPan.add(bagLabel, gc);

        return bagPan;
    }

    private JPanel getFuelPanel() {
        GridBagConstraints gc = new GridBagConstraints();
        JPanel fuelPan = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        fuelPan.setLayout(layout);
        fuelPan.setOpaque(false);

        JButton startButton = new JButton("Start Fuelling");
        JButton stopButton = new JButton("Stop Fuelling");
        JLabel fuelLabel = new JLabel("fuel LABEL");

        fuelState = new AircraftState(0, aircraft.getFuelCapacity(), 50, 1, fuelLabel, "kg");


        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!fuelThreadRunning) {
                    fuelThreadRunning = true;
                    fuelThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while(fuelThreadRunning && fuelState.getCurrentState() < fuelState.getMaxCapacity()) {
                                fuelState.tick();
                                try {
                                    sleep(fuelState.getLoadInterval() * 1000L);
                                } catch(Exception exc) {
                                    System.out.println(exc.getMessage());
                                }
                            }
                            fuelThreadRunning = false;
                        }
                    });
                    fuelThread.start();
                }
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fuelThreadRunning = false;
                if(fuelThread != null) {
                    try {
                        fuelThread.join();
                    } catch(Exception exc) {
                        System.out.println(exc.getMessage());
                    }
                }
            }
        });

        gc.weighty = 0.5;
        gc.gridx = 0;
        gc.gridy = 0;
        fuelPan.add(startButton, gc);

        gc.gridy = GridBagConstraints.RELATIVE;
        fuelPan.add(stopButton, gc);

        gc.weighty = 1;
        gc.gridx = GridBagConstraints.RELATIVE;
        gc.gridy = GridBagConstraints.REMAINDER;
        gc.gridheight = 2;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.VERTICAL;
        fuelPan.add(fuelLabel, gc);

        return fuelPan;
    }

    private void setupGrid() {
        GridBagConstraints gc = new GridBagConstraints();

        GridBagLayout layout1 = new GridBagLayout();
        imageLabel.setLayout(layout1);
        JPanel buttonPanel = new JPanel();
        GridBagLayout layout2 = new GridBagLayout();
        buttonPanel.setLayout(layout2);
        buttonPanel.setOpaque(false);
        gc.fill = GridBagConstraints.BOTH;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.gridx = 3;
        gc.gridy = 3;
        imageLabel.add(buttonPanel, gc);
        imageLabel.setOpaque(true);
        gc.fill = GridBagConstraints.NONE;

        gc.gridx = 0;
        gc.gridy = 0;
        buttonPanel.add(getPassengerPanel(), gc);

        gc.gridx = 2;
        gc.gridy = 0;
        buttonPanel.add(getBaggagePanel(), gc);

        gc.gridx = 0;
        gc.gridy = 2;
        buttonPanel.add(getFuelPanel(), gc);
    }
}
