package scripts.data;

import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.Setter;
import scripts.tasks.ChatterTask;

import java.util.LinkedList;

public class Profile {
    @Getter
    @Setter
    boolean
            darkModeEnabled;
    @Getter
    @Setter
    ObservableList<ChatterTask> chatterTasks;
    @Getter
    @Setter
    String npcName;
}
