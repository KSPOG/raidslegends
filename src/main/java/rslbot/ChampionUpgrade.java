package rslbot;

/**
 * Utility for computing how many 1★ champions are required to create
 * a champion of a given rank. In the game, promoting a champion to the
 * next rank requires feeding it a number of fodder champions equal to
 * its current rank, and each fodder champion must itself be ranked up
 * in the same way. This results in a factorial growth in the amount of
 * fodder needed.
 */
public class ChampionUpgrade {

    /**
     * Returns the total number of 1★ champions required to build a single
     * champion of the supplied rank.
     *
     * @param rank target rank (must be positive)
     * @return number of base 1★ champions needed
     */
    public static int championsNeeded(int rank) {
        if (rank < 1) {
            throw new IllegalArgumentException("rank must be positive");
        }
        int total = 1;
        for (int i = 2; i <= rank; i++) {
            total *= i; // factorial growth
        }
        return total;
    }

    public static void main(String[] args) {
        System.out.println("To upgrade 3★ → " + championsNeeded(3) + " fodders");
        System.out.println("To upgrade 4★ → " + championsNeeded(4) + " fodders");
    }
}
