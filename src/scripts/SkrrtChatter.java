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
import org.tribot.script.interfaces.Ending;
import org.tribot.script.interfaces.Painting;
import org.tribot.script.interfaces.Starting;
import org.tribot.util.Util;
import scripts.data.*;

import scripts.api.data_tracker.DataTracker;
import scripts.api.Core;
import scripts.api.task.Task;
import scripts.api.task.TaskSet;
import scripts.api.util.functions.Logging;
import scripts.api.util.functions.Sleep;
import scripts.api.util.functions.Traversing;
import scripts.api.util.numbers.Randomisation;
import scripts.api.util.numbers.Reactions;
import scripts.api.util.numbers.SeedGenerator;
import scripts.gui.GUI;
import scripts.tasks.Chatter;
import scripts.utilities.FileUtilities;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;

import static scripts.data.Vars.*;


public class SkrrtChatter extends Script implements Starting, PaintInfo, Painting, Arguments, Ending {


    @ScriptManifest(name = "SkrrtChatter", authors = {"SkrrtNick"}, category = "Tools")
    private boolean launchGUI = true;
    private URL fxml;
    private GUI gui;
    private final FluffeesPaint SkrrtPaint = new FluffeesPaint(this, FluffeesPaint.PaintLocations.BOTTOM_LEFT_PLAY_SCREEN, new Color[]{new Color(255, 251, 255)}, "Trebuchet MS", new Color[]{new Color(0, 0, 0, 124)},
            new Color[]{new Color(179, 0, 0)}, 1, false, 5, 3, 0);
    DataTracker tracker = new DataTracker("https://api.skrrtscripts.com", "secret", "chatter");


    @Override
    public void run() {

        if (launchGUI) {
            try {
                fxml = new URL("https://raw.githubusercontent.com/SkrrtNick/SkrrtChatter/master/src/scripts/gui/gui.fxml");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            gui = new GUI(fxml);
            gui.show();
            while (gui.isOpen()) {
                sleep(500);
            }
        }

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
                Core.setStatus(task.toString());
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
        return new String[]{"SkrrtChatter V1.0", "Time ran: " + SkrrtPaint.getRuntimeString(), "Status: " + Core.getStatus()};
    }


    @Override
    public void onPaint(Graphics graphics) {
        SkrrtPaint.paint(graphics);
    }

    @Override
    public void passArguments(HashMap<String, String> hashMap) {
        Core.setProfileDirectory("/Skrrt/Chatter/Profiles");
        FileUtilities.createProfileDirectory(Core.getProfileDirectory());
        String scriptSelect = hashMap.get("custom_input");
        String clientStarter = hashMap.get("autostart");
        String input = clientStarter != null ? clientStarter : scriptSelect;
        String[] settings = input.split(",");
        if (settings.length > 0) {
            for (String s : settings) {
                if (s.contains("settings:")) {
                    profileName = s.split(":")[1] != null ? s.split(":")[1] : null;
                }
            }
        }
        if (profileName != null) {
            if (!profileName.endsWith(".json")) {
                profileName += ".json";
            }
            try {
                runtimeSettings = FileUtilities.gson.fromJson(new String(FileUtilities.loadFile(new File(Util.getWorkingDirectory().getAbsolutePath() + Core.getProfileDirectory() + "/" + profileName))), Profile.class);
                launchGUI = false;
                runningPrep = true;
            } catch (IOException e) {
                Logging.debug(e.getMessage());
                Logging.debug("Unable to locate profile");
            }
        }
    }

    @Override
    public void onEnd() {
        tracker.stop();
    }
}