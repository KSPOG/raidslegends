package rslbot;

import java.awt.Robot;
import java.awt.event.InputEvent;

/**
 * Utility to send mouse clicks to the Raid: Shadow Legends window.
 * <p>
 * This implementation simply moves the mouse and performs a click at the
 * provided screen coordinates. In a full solution the coordinates would be
 * calculated dynamically based on image recognition or window position.
 * </p>
 */
public final class WindowClicker {
    private WindowClicker() {}

    /**
     * Clicks the game's "Start" button using absolute screen coordinates.
     * The coordinates may need adjustment depending on the user's
     * screen resolution and window location.
     */
    public static void clickStartButton() {
        click(960, 540); // example centre coordinates
    }

    /**
     * Moves the mouse to {@code (x, y)} and performs a left click.
     */
    public static void click(int x, int y) {
        try {
            Robot robot = new Robot();
            robot.mouseMove(x, y);
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        } catch (Exception e) {
            // Log stack trace for troubleshooting
            e.printStackTrace();
        }
    }
}
