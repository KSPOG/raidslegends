package rslbot;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * Sends click events to the Raid: Shadow Legends window and captures its
 * contents using the native RaidClient.dll.
 */
public final class WindowClicker {
    private WindowClicker() {
    }

    /**
     * Convenience helper that aims a click at the centre of the primary screen.
     */
    public static void clickStartButton() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        click(screen.width / 2, screen.height / 2);
    }

    /**
     * Dispatches a click to the game window.
     *
     * @return {@code true} if the click was sent successfully
     */
    public static boolean click(int screenX, int screenY) {
        return RaidClient.INSTANCE.clickRaid(screenX, screenY);
    }

    /**
     * Captures the Raid window to an image file for inspection.
     */
    public static void saveScreenshot(String filePath) {
        BufferedImage img = captureWindowImage();
        if (img == null) {
            return;
        }
        try {
            ImageIO.write(img, "bmp", new File(filePath));
            System.out.println("Saved screenshot to " + filePath);
        } catch (Exception e) {
            System.out.println("Failed to save screenshot: " + e.getMessage());
        }
    }

    /**
     * Returns a {@link BufferedImage} containing the contents of the Raid
     * window, or {@code null} if the capture fails.
     */
    public static BufferedImage captureWindowImage() {
        try {
            File tmp = File.createTempFile("raid", ".bmp");
            tmp.deleteOnExit();
            if (!RaidClient.INSTANCE.captureRaid(tmp.getAbsolutePath())) {
                return null;
            }
            BufferedImage img = ImageIO.read(tmp);
            // Remove the temporary file; captureRaid creates a BMP
            tmp.delete();
            return img;
        } catch (Exception e) {
            System.out.println("Failed to capture screenshot: " + e.getMessage());
            return null;
        }
    }
}
