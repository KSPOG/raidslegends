package rslbot;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
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
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File(path),
                mapper.getTypeFactory().constructCollectionType(List.class, SellRule.class));
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
