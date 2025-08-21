package rslbot;

import javax.swing.*;
import java.awt.*;

/**
 * Simple Swing window that provides a few configuration options similar to
 * those found in the RSLHelper application. A progress bar shows run progress
 * and the Start button toggles to Stop while the bot is active.



 * Simple Swing window that provides a few configuration options
 * similar to those found in the RSLHelper application.

 */
public class SettingsWindow extends JFrame {
    private final JCheckBox autoSell;
    private final JCheckBox enableOcr;
    private final JSpinner runCount;
    private final JTextArea logArea = new JTextArea(8, 40);
    private final JProgressBar progress = new JProgressBar();
    private final JButton start = new JButton("Start");
    private final BotCore bot = new BotCore();
    private Thread botThread;

    public SettingsWindow() {
        super("RSL Bot Settings");
        TemplateImages.ensureDefaults();





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

        progress.setStringPainted(true);

        JButton screenshot = new JButton("Screenshot");
        screenshot.addActionListener(e -> WindowClicker.saveScreenshot("raid-window.png"));

        start.addActionListener(e -> toggleBot());



        JButton screenshot = new JButton("Screenshot");
        screenshot.addActionListener(e -> WindowClicker.saveScreenshot("raid-window.png"));

        JButton start = new JButton("Start");
        start.addActionListener(e -> startBot());


        JButton start = new JButton("Start");
        start.addActionListener(e -> startBot());

        JButton save = new JButton("Save");
        save.addActionListener(e -> saveSettings());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(screenshot);
        buttons.add(start);
        buttons.add(save);

        JPanel south = new JPanel(new BorderLayout());
        south.add(progress, BorderLayout.NORTH);
        south.add(buttons, BorderLayout.SOUTH);

        add(center, BorderLayout.CENTER);
        add(south, BorderLayout.SOUTH);



        buttons.add(screenshot);

        buttons.add(start);
        buttons.add(save);

        add(center, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);



        LogWindow.install(logArea);

        JPanel center = new JPanel(new GridLayout(0, 1));
        center.add(autoSell);
        center.add(enableOcr);
        center.add(new JLabel("Runs per session:"));
        center.add(runCount);

        JButton save = new JButton("Save");
        save.addActionListener(e -> saveSettings());

        add(center, BorderLayout.CENTER);
        add(save, BorderLayout.SOUTH);

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

    private void toggleBot() {
        if (botThread != null && botThread.isAlive()) {
            bot.stop();
            start.setEnabled(false);
            return;
        }

        saveSettings();
        int runs = (Integer) runCount.getValue();
        progress.setMaximum(runs);
        progress.setValue(0);
        start.setText("Stop");

        botThread = new Thread(() -> {
            try {
                bot.startBot(runs, (cur, total) ->
                        SwingUtilities.invokeLater(() -> progress.setValue(cur)));
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                SwingUtilities.invokeLater(() -> {
                    start.setText("Start");
                    start.setEnabled(true);
                });
            }
        }, "RSL-Bot");
        botThread.start();


    private void startBot() {
        saveSettings();
        // Attempt to click the "Start" button in the Raid: Shadow Legends window.
        // Coordinates are examples and may require adjustment for individual setups.
        WindowClicker.clickStartButton();
        System.out.println("Start button clicked");



    private void startBot() {
        saveSettings();
        // Placeholder for actual bot start logic.
        System.out.println("Bot started");

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SettingsWindow().setVisible(true));






    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LogWindow.install();
            new SettingsWindow().setVisible(true);
            System.out.println("Settings window displayed");
        });

        SwingUtilities.invokeLater(() -> new SettingsWindow().setVisible(true));

    }
}
