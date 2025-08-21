package rslbot;

import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

/**
 * Utility for sending click events to on-screen coordinates while restoring the
 * mouse pointer to its original location. This avoids leaving the cursor
 * elsewhere on the user's desktop and keeps all interaction scoped to the
 * Raid: Shadow Legends window.
 */
public final class WindowClicker {
    private WindowClicker() {}

    /**
     * Convenience helper that aims a click at the centre of the screen. The
     * coordinates may require adjustment for a specific window layout.
     */
    public static void clickStartButton() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        click(screen.width / 2, screen.height / 2);
    }

    /**
     * Sends a left mouse click to the given screen coordinates, restoring the
     * cursor to its original position afterwards.
     *
     * @param screenX absolute X coordinate
     * @param screenY absolute Y coordinate
     * @return {@code true} if the click was dispatched successfully
     */
    public static boolean click(int screenX, int screenY) {
        try {
            Robot robot = new Robot();
            Point original = MouseInfo.getPointerInfo().getLocation();
            robot.mouseMove(screenX, screenY);
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseMove(original.x, original.y);
            return true;
        } catch (Exception e) {
            System.out.println("Failed to send click: " + e.getMessage());
            return false;
        }
    }

    /**
     * Captures a screenshot of the current desktop and writes it to the
     * specified file path. The image can then be inspected to determine button
     * locations.
     */
    public static void saveScreenshot(String filePath) {
        BufferedImage img = captureWindowImage();
        if (img == null) {
            return;
        }
        try {
            ImageIO.write(img, "png", new File(filePath));
            System.out.println("Saved screenshot to " + filePath);
        } catch (Exception e) {
            System.out.println("Failed to save screenshot: " + e.getMessage());
        }
    }

    /**
     * Returns a {@link BufferedImage} of the full screen. This method does not
     * attempt to locate the game window; callers may crop the image as needed.
     */
    public static BufferedImage captureWindowImage() {
        try {
            Robot robot = new Robot();
            Rectangle bounds = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            return robot.createScreenCapture(bounds);
        } catch (Exception e) {
            System.out.println("Failed to capture screenshot: " + e.getMessage());
            return null;
        }
    }
}

