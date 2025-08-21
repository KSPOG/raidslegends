package rslbot;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

/**
 * Issues left mouse clicks using {@link Robot}. The cursor position is
 * restored after each click so that the user's mouse is only momentarily
 * affected. This avoids the need for native libraries such as JNA.
 */
public final class WindowClicker {
    private WindowClicker() {}

    /**
     * Performs a left-click at the given absolute screen coordinates. The
     * method preserves the current mouse location and restores it once the
     * click has been issued.
     */
    public static boolean click(int screenX, int screenY) {
        try {
            Robot robot = new Robot();
            Point original = MouseInfo.getPointerInfo().getLocation();
            robot.mouseMove(screenX, screenY);
            robot.mousePress(java.awt.event.InputEvent.BUTTON1_DOWN_MASK);
            Thread.sleep(50);
            robot.mouseRelease(java.awt.event.InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseMove(original.x, original.y);
            return true;
        } catch (Exception e) {
            System.out.println("Click failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Captures a screenshot of the entire desktop and writes it to the given
     * file path. This can assist in locating buttons manually.
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
     * Returns an image of the full screen. Callers may crop to the Raid window
     * if desired.
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
