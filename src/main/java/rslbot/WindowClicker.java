package rslbot;



import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;

import java.awt.event.InputEvent;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.LPARAM;
import com.sun.jna.platform.win32.WinDef.RECT;
import com.sun.jna.platform.win32.WinDef.WPARAM;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

/**
 * Issues left mouse clicks using {@link Robot}. The cursor position is
 * restored after each click so that the user's mouse is only momentarily
 * affected. This avoids the need for native libraries such as JNA.
 * Utility for sending click events to on-screen coordinates while restoring the
 * mouse pointer to its original location. This avoids leaving the cursor
 * elsewhere on the user's desktop and keeps all interaction scoped to the
 * Raid: Shadow Legends window.
 * Utility to send mouse clicks directly to the Raid: Shadow Legends window
 * without taking control of the user's mouse cursor.

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
     * Performs a left-click at the given absolute screen coordinates. The
     * method preserves the current mouse location and restores it once the
     * click has been issued.

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

    interface User32Ex extends User32 {
        User32Ex INSTANCE = Native.load("user32", User32Ex.class);
        HWND FindWindowA(String lpClassName, String lpWindowName);
        boolean PostMessageA(HWND hWnd, int msg, WPARAM wParam, LPARAM lParam);
        boolean GetWindowRect(HWND hWnd, RECT rect);
    }

    /**
     * Clicks the game's "Start" button using absolute screen coordinates.
     * The coordinates may need adjustment depending on the user's
     * screen resolution and window location.
     */
    public static void clickStartButton() {
        click(960, 540); // example centre coordinates
    }

    /**
     * Sends a left-click message to the Raid window at the given screen
     * coordinates, ignoring the request if the point lies outside the
     * window bounds.
     */
    public static boolean click(int screenX, int screenY) {
        HWND hwnd = User32Ex.INSTANCE.FindWindowA(null, "Raid: Shadow Legends");
        if (hwnd == null) {
            System.out.println("Game window not found!");
            return false;
        }

        RECT rect = new RECT();
        if (!User32Ex.INSTANCE.GetWindowRect(hwnd, rect)) {
            System.out.println("Unable to obtain window bounds!");
            return false;
        }

        int relX = screenX - rect.left;
        int relY = screenY - rect.top;
        int width = rect.right - rect.left;
        int height = rect.bottom - rect.top;
        if (relX < 0 || relY < 0 || relX >= width || relY >= height) {
            System.out.println("Coordinates outside game window");
            return false;
        }

        int lParam = (relY << 16) | (relX & 0xFFFF);
        // WM_LBUTTONDOWN
        User32Ex.INSTANCE.PostMessageA(hwnd, 0x0201, new WPARAM(1), new LPARAM(lParam));
        // WM_LBUTTONUP
        User32Ex.INSTANCE.PostMessageA(hwnd, 0x0202, new WPARAM(0), new LPARAM(lParam));
        return true;
    }

    /**
     * Captures a screenshot of the Raid: Shadow Legends window and writes it
     * to the supplied file path. The screenshot can be used to inspect button
     * locations without guessing coordinates.
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


     * Returns a {@link BufferedImage} containing the contents of the game
     * window, or {@code null} if the window cannot be located.
     */
    public static BufferedImage captureWindowImage() {
        HWND hwnd = User32Ex.INSTANCE.FindWindowA(null, "Raid: Shadow Legends");
        if (hwnd == null) {
            System.out.println("Game window not found!");
            return null;
        }

        RECT rect = new RECT();
        if (!User32Ex.INSTANCE.GetWindowRect(hwnd, rect)) {
            System.out.println("Unable to obtain window bounds!");
            return null;
        }

        int width = rect.right - rect.left;
        int height = rect.bottom - rect.top;
        try {
            Robot robot = new Robot();
            return robot.createScreenCapture(new Rectangle(rect.left, rect.top, width, height));
        } catch (Exception e) {
            System.out.println("Failed to capture screenshot: " + e.getMessage());
            return null;

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
