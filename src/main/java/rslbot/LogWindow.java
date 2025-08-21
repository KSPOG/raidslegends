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
        System.setOut(ps);
        System.setErr(ps);
    }

    private static class TextAreaOutputStream extends OutputStream {
        private final JTextArea area;
        private final StringBuilder buffer = new StringBuilder();

        TextAreaOutputStream(JTextArea area) {
            this.area = area;
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
                buffer.setLength(0);
            } else {
                buffer.append((char) b);
            }
        }
    }
}
