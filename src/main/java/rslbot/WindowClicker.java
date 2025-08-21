package rslbot;

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
 * Utility to send mouse clicks directly to the Raid: Shadow Legends window
 * without taking control of the user's mouse cursor.
 */
public final class WindowClicker {
    private WindowClicker() {}

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
        }
    }
}
