package rslbot;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * Helper methods for locating images on the screen and interacting with the
 * Raid client through the native library.
 */
public class ScreenUtils {
    /**
     * Finds the given template and clicks the centre of the matched region.
     */
    public static boolean findAndClick(String templatePath) {
        Rectangle r = find(templatePath);
        if (r != null) {
            int centreX = r.x + r.width / 2;
            int centreY = r.y + r.height / 2;
            return WindowClicker.click(centreX, centreY);
        }
        return false;
    }

    /**
     * Returns the on-screen coordinates of the template if it is found, otherwise
     * {@code null}.
     */
    public static Rectangle find(String templatePath) {
        try {
            BufferedImage screenshot = WindowClicker.captureWindowImage();
            if (screenshot == null) {
                return null;
            }
            BufferedImage template = ImageIO.read(new File(templatePath));
            for (int y = 0; y <= screenshot.getHeight() - template.getHeight(); y++) {
                for (int x = 0; x <= screenshot.getWidth() - template.getWidth(); x++) {
                    if (matches(screenshot, template, x, y)) {
                        return new Rectangle(x, y, template.getWidth(), template.getHeight());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static boolean matches(BufferedImage screen, BufferedImage template, int startX, int startY) {
        for (int y = 0; y < template.getHeight(); y++) {
            for (int x = 0; x < template.getWidth(); x++) {
                if (screen.getRGB(startX + x, startY + y) != template.getRGB(x, y)) {
                    return false;
                }
            }
        }
        return true;
    }
}
