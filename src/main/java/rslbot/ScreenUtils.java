package rslbot;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Helper methods for locating images on the screen and clicking within the
 * Raid: Shadow Legends window without hijacking the user's mouse.
 */
public class ScreenUtils {
    /**
     * Finds the given template and sends a click to its top-left coordinate.
     */
    public boolean findAndClick(String templatePath) {
        Point p = find(templatePath);
        if (p != null) {
            WindowClicker.click(p.x, p.y);
            return true;
        }
        return false;
    }

    /**
     * Returns the on-screen coordinates of the template if it is found with a
     * reasonable match score, otherwise {@code null}.
     */
    public Point find(String templatePath) {
        try {
            BufferedImage screenshot = new Robot().createScreenCapture(
                    new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));

            File file = new File(templatePath);
            if (!file.exists()) {
                System.out.println("Template not found: " + templatePath);
                return null;
            }

            BufferedImage template = ImageIO.read(file);

            for (int y = 0; y <= screenshot.getHeight() - template.getHeight(); y++) {
                for (int x = 0; x <= screenshot.getWidth() - template.getWidth(); x++) {
                    if (matches(screenshot, template, x, y)) {
                        return new Point(x, y);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean matches(BufferedImage screen, BufferedImage template, int startX, int startY) {
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
