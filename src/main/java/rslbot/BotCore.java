package rslbot;

import rslbot.ocr.OcrReader;
import java.util.List;

/**
 * Core automation routine that searches for battle buttons, waits for
 * victory and applies auto-sell rules to obtained gear.
 */
public class BotCore {
    private final ScreenUtils screen = new ScreenUtils();
    private final OcrReader ocr = new OcrReader();
    private volatile boolean running;

    public interface ProgressListener {
        void onProgress(int currentRun, int totalRuns);
    }

    /**
     * Starts the bot loop for the given number of runs. Progress is reported
     * via the supplied listener on the calling thread.
     */
    public void startBot(int runs, ProgressListener progress) throws Exception {
        running = true;
        List<GearRules.SellRule> rules = GearRules.loadRules(
                "src/main/resources/rules/Early Mid Game Basic Sell rules (1).hsf");

        for (int i = 0; i < runs && running; i++) {
            System.out.println("\u25B6 Starting run " + (i + 1));

            // If a previous run left the Victory screen open, immediately
            // trigger a replay instead of searching for the Fight button.
            if (screen.find("images/victory.png") != null) {
                screen.findAndClick("images/replay_button.png");
                Thread.sleep(3000);
            } else if (!screen.findAndClick("images/fight_button.png")) {


            // Locate and click the Fight button inside the game client.
            if (!screen.findAndClick("images/fight_button.png")) {

                System.out.println("Fight button not found!");
                Thread.sleep(3000);
                continue;
            }

            // Wait for the victory screen to appear.
            while (running && screen.find("images/victory.png") == null) {
                Thread.sleep(5000);
            }
            if (!running) {
                break;
            }

            // OCR the gear popup and decide whether to sell or keep.
            String gearText = ocr.readGearPopup();
            if (gearText != null) {
                boolean sell = GearRules.shouldSell(gearText, rules);
                if (sell) {
                    screen.findAndClick("images/sell_button.png");
                    System.out.println("\uD83D\uDDD1 Sold gear: " + gearText);
                } else {
                    screen.findAndClick("images/keep_button.png");
                    System.out.println("\uD83D\uDC8E Kept gear: " + gearText);
                }
            }

            // Start another run.
            screen.findAndClick("images/replay_button.png");
            Thread.sleep(3000);

            if (progress != null) {
                progress.onProgress(i + 1, runs);
            }
        }
        running = false;
    }

    /**
     * Requests the running loop to stop after the current iteration.
     */
    public void stop() {
        running = false;
    }
}
