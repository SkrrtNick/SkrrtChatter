package scripts.api;

import lombok.Getter;
import lombok.Setter;

public class Core {
    @Getter @Setter
    private static String status;
    @Getter @Setter
    private static String log;
    @Getter @Setter
    private static double playerSeed = 0;
    @Getter @Setter
    private static boolean isRunning;
    @Getter @Setter
    private static String profileDirectory;
    @Getter @Setter
    private static long startTime;

}
