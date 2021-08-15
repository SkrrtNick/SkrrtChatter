package scripts.api.util.numbers;

public class Reactions {
    public static ReactionGenerator rg = new ReactionGenerator();
    public static long p, n, a;
    public static int d;
    public static double playerSeed;


    public static long getPredictable() {
        generatePlayerSeed();
        double predictable = playerSeed * rg.nextReactionTime(30, 10, 0.02, 0.01, 30, 500);
        if (rg != null) {
            p = (long) predictable;
        }
        return p;
    }

    public static long getNormal() {
        generatePlayerSeed();
        double normal = playerSeed * rg.nextReactionTime(200, 50, 0.007, 0.2, 100, 2000);
        if (rg != null) {
            n = (long) normal;
        }
        return n;
    }
    public static long getSemiAFK() {
        generatePlayerSeed();
        double normal = playerSeed * rg.nextReactionTime(500, 200, 5, 0.1, 2000, 10000);
        if (rg != null) {
            n = (long) normal;
        }
        return n;
    }

    public static long getAgilitySleep() {
        generatePlayerSeed();
        double normal = playerSeed * rg.nextReactionTime(200, 50, 0.007, 0.2, 750, 3000);
        if (rg != null) {
            n = (long) normal;
        }
        return n;
    }

    public static long getAFK() {
        generatePlayerSeed();
        double afk = playerSeed * rg.nextReactionTime(5000, 2000, 5, 0.1, 200, 100000);
        if (rg != null) {
            a = (long) afk;
        }
        return a;
    }

    public static int getDecision(int options){
        generatePlayerSeed();
        double decision = rg.nextReactionTime(playerSeed * 30,10,0.02,0.01,30,100 * options);
    if (rg != null){
        d = (int) decision;
    } return d;
    }

    public static void generatePlayerSeed(){
        if(playerSeed == 0){
            SeedGenerator seed = new SeedGenerator();
            playerSeed = seed.generateRandom();
        }
    }

//    PREDICTABLE(30,10,0.02,0.01,30,500),
//    NORMAL(200,50,0.007,0.2,100,2000),
//    AFK(5000,2000,5,0.1,200,100000);


}

