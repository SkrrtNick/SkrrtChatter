package scripts.api.util.numbers;

import org.tribot.api2007.Player;

public class SeedGenerator {
    private double playerSeed;
    public double generateRandom() {
        String username = Player.getRSPlayer() != null ? Player.getRSPlayer().getName() : "noname";;

        long seed = (long) username.chars().mapToDouble(c -> Math.pow(c, 2.53)).sum();

        return playerSeed = Double.parseDouble(String.format("1.%1d", seed));
    }

    public double getPlayerSeed() {
        return playerSeed;
    }
}
