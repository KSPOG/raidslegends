package rslbot;

import java.util.Collections;
import java.util.List;

/**
 * Simple rule set that determines whether a piece of gear should be sold
 * based on text extracted from the gear popup.
 */
public class GearRules {
    public static class SellRule {
        public String gearType;
        public int minStars;
        public int maxStars;
        public String rarity;
        public String mainStat;
        public List<String> subStats;
    }

    public static List<SellRule> loadRules(String path) throws Exception {
        // JSON parsing removed to keep the project self-contained.
        return Collections.emptyList();
    }

    public static boolean shouldSell(String gearText, List<SellRule> rules) {
        for (SellRule rule : rules) {
            if (gearText.contains(rule.rarity) && gearText.contains(rule.gearType)) {
                return true;
            }
        }
        return false;
    }
}
