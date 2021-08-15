package scripts.api.util.numbers;

import org.tribot.api.General;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.Game;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSPlayer;
import scripts.api.util.functions.Logging;

import java.util.Random;

public class Randomisation {

    public static boolean setMouseSpeed(){
        while(Game.getGameState() != 30){
            General.sleep(500);
        }
        RSPlayer player = Player.getRSPlayer();
        String name = player == null ?  "noname" : player.getName();
        if(name.equals("noname")){
            Logging.debug("Player is null, are we logged in?");
            return false;
        }
        Random random = new Random(name.hashCode());
        int mouseSpeedMin = 100;
        int mouseSpeedMax = 300;
        int mouseSpeed = random.nextInt(mouseSpeedMax - mouseSpeedMin) + mouseSpeedMin;
        Mouse.setSpeed(mouseSpeed);
        Logging.debug("Mouse Speed is " + Mouse.getSpeed());
        return true;
    }


}