package rslbot;

/**
 * Entry point for launching the bot without the settings UI.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("=== RSL Bot starting ===");
        TemplateImages.ensureDefaults();
        BotCore bot = new BotCore();
        bot.startBot(50, null); // run 50 battles by default
    }
}
