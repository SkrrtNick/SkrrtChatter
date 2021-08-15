package scripts.api.util.functions;

import org.tribot.api.General;

import java.awt.*;

public class Logging {

    public static void  debug(String log){
        General.println("[Debug] " + log,Color.BLACK,new Color(255, 255, 204));
    }

    public static void debug(String name, int val){
        General.println("[Debug] " + name + " value is " + val,Color.BLACK,new Color(255, 255, 204));
    }

    public static void debug(String name, boolean bool){
        General.println("[Debug] " + name + " value is " + bool,Color.BLACK,new Color(255, 255, 204));
    }

    public static void message(String header, String message){
        General.println("["+header+"] " + message,Color.BLACK,new Color(0, 204, 102));
    }
    public static void message(String header, String ... message){
        for(String m :message){
            General.println("["+header+"] " + m,Color.BLACK,new Color(0, 204, 102));        }
    }
    public static void message(String message){
        General.println("[Skrrt] " + message,Color.BLACK,new Color(0, 204, 102));
    }

}
