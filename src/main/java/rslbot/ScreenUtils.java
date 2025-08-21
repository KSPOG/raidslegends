package rslbot;

import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;

import java.awt.*;
import java.awt.image.BufferedImage;

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
            Mat screenMat = bufferedImageToMat(screenshot);
            Mat template = opencv_imgcodecs.imread(templatePath);
            Mat result = new Mat();
            opencv_imgproc.matchTemplate(screenMat, template, result, opencv_imgproc.TM_CCOEFF_NORMED);
            Core.MinMaxLocResult mmr = org.bytedeco.opencv.global.opencv_core.minMaxLoc(result);
            if (mmr.maxVal > 0.8) {
                return new Point((int) mmr.maxLoc.x(), (int) mmr.maxLoc.y());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Mat bufferedImageToMat(BufferedImage bi) {
        int width = bi.getWidth();
        int height = bi.getHeight();
        Mat mat = new Mat(height, width, opencv_core.CV_8UC3);
        // Conversion code omitted for brevity.
        return mat;
    }
}
