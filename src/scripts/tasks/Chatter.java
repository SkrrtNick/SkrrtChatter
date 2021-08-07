package scripts.tasks;

import dax.walker.utils.path.DaxPathFinder;
import dax.walker_engine.interaction_handling.NPCInteraction;
import org.tribot.api.General;
import org.tribot.api2007.Game;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Login;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSTile;
import scripts.data.Vars;
import scripts.skrrt_api.events.Core;
import scripts.skrrt_api.task.Priority;
import scripts.skrrt_api.task.Task;
import scripts.skrrt_api.util.functions.*;
import scripts.skrrt_api.util.numbers.Reactions;

public class Chatter implements Task {

    @Override
    public String toString() {
        return "Using " + Vars.action + " on " + Vars.npcName;
    }

    @Override
    public Priority priority() {
        return Priority.HIGH;
    }

    @Override
    public boolean validate() {
        return Login.getLoginState().equals(Login.STATE.INGAME);
    }

    @Override
    public void execute() {
        General.sleep(Reactions.getNormal());
        RSNPC npc = Interaction.getNPC(Vars.npcName);
        RSTile customLocation = new RSTile(Vars.x, Vars.y, Vars.z);
        if (Game.getGameState() == 30 && (npc == null || !npc.isClickable())) {
            if (Traversing.walkTo(customLocation)) {
                Core.setStatus("Traversing to " + Vars.npcName);
            }
            if (Player07.distanceTo(customLocation) < 5 && !Interaction.npcExists(Vars.npcName)) {
                Logging.debug("Unable to find NPC, stopping script");
                Vars.isRunning = false;
            }
        }
        if(npc!=null){
            if (DaxPathFinder.canReach(npc.getPosition())) {
                if (Interaction.clickNPC(Vars.npcName, Vars.action)) {
                    Vars.hasClicked = true;
                    if(!Vars.dialogue.isEmpty()){
                        Sleep.until(NPCInteraction::isConversationWindowUp,Reactions.getNormal()*Player07.distanceTo(npc.getPosition()));
                    } else {
                        General.sleep(Reactions.getNormal());
                    }
                }
            } else {
                Traversing.walkTo(npc.getPosition());
            }
            if (!Vars.dialogue.isEmpty() && Player07.distanceTo(customLocation) < 5) {
                if (!NPCInteraction.isConversationWindowUp()) {
                    if (Interaction.clickNPC(Vars.npcName, Vars.action)) {
                        Vars.hasClicked = true;
                        Sleep.until(NPCInteraction::isConversationWindowUp, 2000);
                    }
                }
                Core.setStatus("Handling dialogue");
                if (NPCInteraction.isConversationWindowUp()) {
                    NPCInteraction.handleConversation(Vars.dialogue.toArray(new String[Vars.dialogue.size()]));
                    Sleep.until(() -> !NPCInteraction.isConversationWindowUp());
                }
                NPCInteraction.handleConversation();
                Interaction.handleContinue();
                Sleep.until(() -> !NPCInteraction.isConversationWindowUp());
                Interfaces.closeAll();
            }
            if (Vars.hasClicked && !NPCInteraction.isConversationWindowUp()) {
                Logging.message("[SkrrtChatter]", " Completed the task, thanks for using Skrrt Chatter");
                Vars.isRunning = false;
            }

        }

    }

}
