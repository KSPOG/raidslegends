package rslbot;

import javax.swing.*;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Utility that redirects System.out/err to a provided JTextArea.
 * The textarea itself is expected to be displayed by the caller.
 */
public final class LogWindow {

    private LogWindow() {
        // utility
    }

    /**
     * Redirects standard output streams to the given text area.
     */
    public static void install(JTextArea area) {
        PrintStream ps = new PrintStream(new TextAreaOutputStream(area), true);


/* * Simple window that displays log messages redirected from System.out/err.
 */
public class LogWindow extends JFrame {
    private static LogWindow instance;
    private final JTextArea area = new JTextArea();

    private LogWindow() {
        super("RSL Bot Log");
        setSize(500, 300);
        setLocationRelativeTo(null);
        area.setEditable(false);
        add(new JScrollPane(area));
    }

    public static synchronized LogWindow getInstance() {
        if (instance == null) {
            instance = new LogWindow();
        }
        return instance;
    }

    private void appendLine(String text) {
        SwingUtilities.invokeLater(() -> {
            area.append(text + System.lineSeparator());
            area.setCaretPosition(area.getDocument().getLength());
        });
    }

    /**
     * Displays the log window and redirects standard output streams to it.
     */
    public static void install() {
        LogWindow log = getInstance();
        log.setVisible(true);
        PrintStream ps = new PrintStream(new TextAreaOutputStream(log), true);


        System.setOut(ps);
        System.setErr(ps);
    }

    private static class TextAreaOutputStream extends OutputStream {

        private final JTextArea area;
        private final StringBuilder buffer = new StringBuilder();

        TextAreaOutputStream(JTextArea area) {
            this.area = area;


        private final LogWindow window;
        private final StringBuilder buffer = new StringBuilder();

        TextAreaOutputStream(LogWindow window) {
            this.window = window;

        }

        @Override
        public void write(int b) {
            if (b == '\r') {
                return;
            }
            if (b == '\n') {
                String line = buffer.toString();
                SwingUtilities.invokeLater(() -> {
                    area.append(line + System.lineSeparator());
                    area.setCaretPosition(area.getDocument().getLength());
                });


                window.appendLine(buffer.toString());

                buffer.setLength(0);
            } else {
                buffer.append((char) b);
            }
        }
    }
}
