package rslbot.ocr;

import net.sourceforge.tess4j.Tesseract;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Thin wrapper around Tesseract OCR to read gear popup text.
 */
public class OcrReader {
    private final Tesseract tesseract;

    public OcrReader() {
        tesseract = new Tesseract();
        tesseract.setDatapath("tessdata");
        tesseract.setLanguage("eng");
    }

    public String readGearPopup() {
        try {
            Rectangle popup = new Rectangle(500, 300, 600, 400); // example coords
            BufferedImage img = new Robot().createScreenCapture(popup);
            return tesseract.doOCR(img);
        } catch (Exception e) {
            return null;
        }
    }
}
