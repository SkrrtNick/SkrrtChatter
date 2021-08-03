package scripts;

import dax.api_lib.WebWalkerServerApi;
import dax.api_lib.models.DaxCredentials;
import dax.api_lib.models.DaxCredentialsProvider;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.Login;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Arguments;
import org.tribot.script.interfaces.Painting;
import org.tribot.script.interfaces.Starting;
import scripts.data.*;

import scripts.skrrt_api.data_tracker.DataTracker;
import scripts.skrrt_api.events.Core;
import scripts.skrrt_api.task.Task;
import scripts.skrrt_api.task.TaskSet;
import scripts.skrrt_api.util.functions.Logging;
import scripts.skrrt_api.util.functions.Sleep;
import scripts.skrrt_api.util.functions.Traversing;
import scripts.skrrt_api.util.numbers.Randomisation;
import scripts.skrrt_api.util.numbers.Reactions;
import scripts.skrrt_api.util.numbers.SeedGenerator;
import scripts.tasks.Chatter;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;


public class SkrrtChatter extends Script implements Starting, PaintInfo, Painting, Arguments {


    @ScriptManifest(name = "SkrrtChatter", authors = {"SkrrtNick"}, category = "Tools")

    private final FluffeesPaint SkrrtPaint = new FluffeesPaint(this, FluffeesPaint.PaintLocations.BOTTOM_LEFT_PLAY_SCREEN, new Color[]{new Color(255, 251, 255)}, "Trebuchet MS", new Color[]{new Color(0, 0, 0, 124)},
            new Color[]{new Color(179, 0, 0)}, 1, false, 5, 3, 0);
    DataTracker tracker = new DataTracker("https://api.skrrtscripts.com", "secret", "chatter");


    @Override
    public void run() {
        Logging.message("SkrrtChatter","Welcome to SkrrtChatter","Interacting with " + Vars.npcName + " via " + Vars.action,"Navigating to: " + "x: " + Vars.x + " y: " + Vars.y + " z: " + Vars.z);
        if(!Vars.dialogue.isEmpty()){
            Logging.message("SkrrtChatter","Dialogue: " + Vars.dialogue.toString(),"Dialogue size: " + (Vars.dialogue.size()));
        }
        Sleep.until(()->!Login.getLoginState().equals(Login.STATE.LOGINSCREEN));
        Randomisation.setMouseSpeed();

        TaskSet tasks = new TaskSet(new Chatter());

        while (Vars.isRunning) {
            Task task = tasks.getValidTask();
            if (task != null) {
                Vars.status = task.toString();
                task.execute();
                tracker.start();
                tracker.trackNumber("runtime",getRunningTime());
                tracker.trackNumber("interactions",1);
            }
            General.sleep(600);
        }

    }

    @Override
    public void onStart() {
        Core.setStatus("Logging in");
        Traversing.setDaxKey(false);

    }

    @Override
    public String[] getPaintInfo() {
        return new String[]{"SkrrtChatter V0.9", "Time ran: " + SkrrtPaint.getRuntimeString(), "Status: " + Core.getStatus()};
    }


    @Override
    public void onPaint(Graphics graphics) {
        SkrrtPaint.paint(graphics);
    }

    @Override
    public void passArguments(HashMap<String, String> hashMap) {
        String scriptSelect = hashMap.get("custom_input");
        String clientStarter = hashMap.get("autostart");
        String input = clientStarter != null ? clientStarter : scriptSelect;
        String[] settings = input.split("(?<!%),");
        if (settings.length > 0) {
            for (String s : settings) {
                if (s.contains("settings:" )&& s.contains(";")) {
                    Vars.npcName = s.split(":")[1] != null ? s.split(":")[1] : null;
                    Vars.action = s.split(":")[2] != null ? s.split(":")[2] : null;
                    Vars.x = s.split(":")[3] != null ? Integer.parseInt(s.split(":")[3]) : null;
                    Vars.y = s.split(":")[4] != null ? Integer.parseInt(s.split(":")[4]) : null;
                    Vars.z = s.split(":")[5] != null ? Integer.parseInt(s.split(":")[5]) : null;
                    Vars.dialogue.addAll(Arrays.asList(s.split(":")[6].replaceAll("%","").split(";")));
                } else if (s.contains("settings:")){
                    Vars.npcName = s.split(":")[1] != null ? s.split(":")[1] : null;
                    Vars.action = s.split(":")[2] != null ? s.split(":")[2] : null;
                    Vars.x = s.split(":")[3] != null ? Integer.parseInt(s.split(":")[3]) : null;
                    Vars.y = s.split(":")[4] != null ? Integer.parseInt(s.split(":")[4]) : null;
                    Vars.z = s.split(":")[5] != null ? Integer.parseInt(s.split(":")[5]) : null;
                }
            } Vars.isRunning = true;
        }
    }
}