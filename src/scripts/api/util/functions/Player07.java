package scripts.api.util.functions;

import org.tribot.api.interfaces.Positionable;
import org.tribot.api2007.Equipment;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSTile;

public class Player07 extends Player {


    public static int distanceTo(Positionable positionable) {
        return Player.getPosition().distanceTo(positionable);
    }

    public static int getPlane() {
        return Player.getPosition().getPlane();
    }

    public static RSTile getPosition() {
        return Player.getRSPlayer().getPosition();
    }

    public static int getX() {
        return Player.getPosition().getX();
    }

    public static int getY() {
        return Player.getPosition().getY();
    }

    public static boolean isAnimating() {
        return Player07.getAnimation() != -1;
    }

    public static boolean isNaked() {
        return Equipment.getItems().length == 0;
    }
}

