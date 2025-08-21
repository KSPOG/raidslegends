package rslbot;

import javax.swing.*;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Simple window that displays log messages redirected from System.out/err.
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
                window.appendLine(buffer.toString());
                buffer.setLength(0);
            } else {
                buffer.append((char) b);
            }
        }
    }
}
