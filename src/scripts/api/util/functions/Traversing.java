package scripts.api.util.functions;


import dax.api_lib.DaxWalker;
import dax.api_lib.WebWalkerServerApi;
import dax.api_lib.models.DaxCredentials;
import dax.api_lib.models.DaxCredentialsProvider;
import dax.shared.helpers.BankHelper;
import dax.walker_engine.WalkingCondition;

import org.tribot.api.General;
import org.tribot.api.interfaces.Positionable;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;
import org.tribot.api2007.util.DPathNavigator;
import scripts.api.util.antiban.Antiban;


public class Traversing {

    private static int attempt = 0;

    public static boolean walkTo(Positionable positionable) {
        if (Player07.distanceTo(positionable) < 8) {
            return true;
        }
        Antiban.activateRun();
        int x = General.random(0, 3);
        int y = General.random(0, 3);
        RSTile newPos = positionable.getPosition().translate(x, y);
        if (!Player07.getPosition().equals(newPos)) {
            if (DaxWalker.walkTo(newPos)) {
                Logging.message("Walker", "Navigating to " + newPos);
                Sleep.until(()->Player07.getPosition().equals(newPos));
                attempt = 0;
            } else {
                attempt++;
                Logging.debug("Walker failed to produce a path, attempt: " + attempt);
                General.sleep(60);
                if (attempt >= 3) {
                    Logging.debug("Walker failed to produce a path 3 times, using DPathNavigator");
                    DPathNavigator path = new DPathNavigator();
                    path.traverse(positionable);
                }

            }
            Sleep.until(()->Player07.getPosition().equals(newPos));
        } else {
            return true;
        }
        return Player07.getPosition().equals(newPos);
    }


    public static void setDaxKey(boolean publicKey) {
        WebWalkerServerApi.getInstance().setDaxCredentialsProvider(new DaxCredentialsProvider() {
            @Override
            public DaxCredentials getDaxCredentials() {
                if (publicKey) {
                    return new DaxCredentials("sub_DPjXXzL5DeSiPf", "PUBLIC-KEY");
                } else {
                    return new DaxCredentials("REDACTED", "REDACTED");
                }
            }
        });
    }


}
