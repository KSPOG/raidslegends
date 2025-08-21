package rslbot;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ChampionUpgradeTest {

    @Test
    void factorialGrowth() {
        assertEquals(1, ChampionUpgrade.championsNeeded(1));
        assertEquals(2, ChampionUpgrade.championsNeeded(2));
        assertEquals(6, ChampionUpgrade.championsNeeded(3));
        assertEquals(24, ChampionUpgrade.championsNeeded(4));
    }

    @Test
    void invalidRankThrows() {
        assertThrows(IllegalArgumentException.class, () -> ChampionUpgrade.championsNeeded(0));
    }
}
