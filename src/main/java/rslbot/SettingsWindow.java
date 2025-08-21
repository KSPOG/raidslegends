package rslbot;

import javax.swing.*;
import java.awt.*;

/**
 * Simple Swing window that provides a few configuration options
 * similar to those found in the RSLHelper application.
 */
public class SettingsWindow extends JFrame {
    private final JCheckBox autoSell;
    private final JCheckBox enableOcr;
    private final JSpinner runCount;
    private final JTextArea logArea = new JTextArea(8, 40);

    public SettingsWindow() {
        super("RSL Bot Settings");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        LogWindow.install(logArea);

        // Settings controls
        autoSell = new JCheckBox("Auto sell gear");
        enableOcr = new JCheckBox("Enable OCR");
        runCount = new JSpinner(new SpinnerNumberModel(10, 1, 1000, 1));

        JPanel settingsPanel = new JPanel(new GridLayout(0, 1));
        settingsPanel.add(autoSell);
        settingsPanel.add(enableOcr);
        settingsPanel.add(new JLabel("Runs per session:"));
        settingsPanel.add(runCount);

        logArea.setEditable(false);
        JScrollPane logScroll = new JScrollPane(logArea);

        JPanel center = new JPanel(new BorderLayout());
        center.add(settingsPanel, BorderLayout.NORTH);
        center.add(logScroll, BorderLayout.CENTER);

        JButton start = new JButton("Start");
        start.addActionListener(e -> startBot());
        JButton save = new JButton("Save");
        save.addActionListener(e -> saveSettings());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(start);
        buttons.add(save);

        add(center, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null); // center on screen
    }

    private void saveSettings() {
        // In a full implementation, settings would be persisted to disk.
        // For now, just dump to console.
        System.out.println("Settings saved:");
        System.out.println("  Auto sell gear: " + autoSell.isSelected());
        System.out.println("  Enable OCR: " + enableOcr.isSelected());
        System.out.println("  Runs per session: " + runCount.getValue());
    }

    private void startBot() {
        saveSettings();
        // Attempt to click the "Start" button in the Raid: Shadow Legends window.
        // Coordinates are examples and may require adjustment for individual setups.
        WindowClicker.clickStartButton();
        System.out.println("Start button clicked");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SettingsWindow().setVisible(true));
    }
}
