package rslbot;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Utility for generating placeholder template images so the project can run
 * without requiring manual screenshot assets. The generated images contain the
 * file name rendered as text, making it easy to identify which placeholder is
 * being used.
 */
public final class TemplateImages {
    private static final String[] DEFAULTS = {
            "images/fight_button.png",
            "images/victory.png",
            "images/sell_button.png",
            "images/keep_button.png",
            "images/replay_button.png"
    };

    private TemplateImages() {
    }

    /**
     * Ensures that all default template images exist. Placeholders are created
     * when files are missing.
     */
    public static void ensureDefaults() {
        for (String path : DEFAULTS) {
            ensure(path);
        }
    }

    /**
     * Ensures that a template image exists at the given path. If the file is
     * missing, a small placeholder PNG is generated and written to disk.
     *
     * @param path relative file path to check
     */
    public static void ensure(String path) {
        File file = new File(path);
        if (file.exists()) {
            return;
        }
        File parent = file.getParentFile();
        if (parent != null) {
            parent.mkdirs();
        }

        int width = 180;
        int height = 60;
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, width - 1, height - 1);
        String text = file.getName().replace(".png", "");
        g.drawString(text, 10, height / 2);
        g.dispose();
        try {
            ImageIO.write(img, "png", file);
            System.out.println("Generated placeholder template: " + path);
        } catch (IOException e) {
            System.out.println("Failed to generate template " + path + ": " + e.getMessage());
        }
    }
}

