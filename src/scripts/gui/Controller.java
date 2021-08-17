package scripts.gui;

import com.allatori.annotations.DoNotRename;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import org.tribot.api.General;
import org.tribot.util.Util;
import scripts.api.Core;
import scripts.api.util.functions.Logging;
import scripts.data.Profile;
import scripts.tasks.ChatterTask;
import scripts.utilities.FileUtilities;

import java.io.File;
import java.io.IOException;

import static scripts.data.Vars.runningPrep;
import static scripts.data.Vars.runtimeSettings;

@DoNotRename
public class Controller extends AbstractGUIController {
    private String profile = "";
    @DoNotRename
    @FXML
    private Pane ownerWindow;

    @DoNotRename
    @FXML
    private TextField zCoordinate;

    @DoNotRename
    @FXML
    private TextField yCoordinate;

    @DoNotRename
    @FXML
    private TextField xCoordinate;

    @DoNotRename
    @FXML
    private TextField npcName;

    @DoNotRename
    @FXML
    private MenuItem save;

    @DoNotRename
    @FXML
    private MenuItem saveAs;

    @DoNotRename
    @FXML
    private MenuItem load;

    @DoNotRename
    @FXML
    private MenuItem exit;
    @DoNotRename
    @FXML
    private Button btnAddTask;
    @DoNotRename
    @FXML
    private TextField dialogue;
    @DoNotRename
    @FXML
    private TextField action;

    @DoNotRename
    @FXML
    private ListView<ChatterTask> taskList;

    @DoNotRename

    @FXML
    private CheckMenuItem darkMode;


    @DoNotRename
    @FXML
    private Button startBtn;

    @DoNotRename
    @FXML
    void loadClicked(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Profile");
        fileChooser.setInitialDirectory(new File(Util.getWorkingDirectory() + Core.getProfileDirectory()));
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json"));
        File file = fileChooser.showOpenDialog(ownerWindow.getScene().getWindow());
        if (file != null) {
            String fileName = file.getName();
            save.setDisable(false);
            profile = fileName;
            try {
                Profile settings = FileUtilities.gson.fromJson(new String(FileUtilities.loadFile(new File(Util.getWorkingDirectory().getAbsolutePath() + Core.getProfileDirectory() + "/" + fileName))), Profile.class);
                load(settings);
            } catch (IOException e) {
                General.println(e.getMessage());
            }
        } else {
            General.println(file);
        }
    }

    @DoNotRename
    @FXML
    void addTaskPressed(ActionEvent event) {
        ObservableList<ChatterTask> temp = taskList.getItems();
        temp.add(new ChatterTask(Integer.parseInt(xCoordinate.getText()),Integer.parseInt(yCoordinate.getText()),Integer.parseInt(zCoordinate.getText()),npcName.getText(),action.getText()));
        taskList.setItems(temp);
    }

    @DoNotRename
    @FXML
    public void saveAsClicked(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        Profile settings = new Profile();
        save(settings);
        fileChooser.setTitle("Save Profile");
        fileChooser.setInitialDirectory(new File(Util.getWorkingDirectory() + Core.getProfileDirectory()));
        File file = fileChooser.showSaveDialog(ownerWindow.getScene().getWindow());
        if (file != null) {
            String fileName = file.getName();
            save.setDisable(false);
            if (!fileName.endsWith(".json")) {
                fileName += ".json";
            }
            profile = fileName;
            FileUtilities.createFile(FileUtilities.gson.toJson(settings), Core.getProfileDirectory() + "/" + fileName);
        }
    }


    @DoNotRename
    @FXML
    public void checkDarkMode(ActionEvent event) {
        if (darkMode.isSelected()) {
            getGUI().getScene().getStylesheets().add("https://raw.githubusercontent.com/SkrrtNick/SkrrtPicker/master/src/scripts/gui/dark-mode.css");
        } else {
            getGUI().getScene().getStylesheets().remove("https://raw.githubusercontent.com/SkrrtNick/SkrrtPicker/master/src/scripts/gui/dark-mode.css");
        }
    }

    @DoNotRename
    @FXML
    void exitClicked(ActionEvent event) {
        this.getGUI().close();
        runningPrep = false;
    }

    @DoNotRename
    @FXML
    public void saveClicked(ActionEvent event) {
        Profile settings = new Profile();
        save(settings);
        FileUtilities.createFile(FileUtilities.gson.toJson(settings), Core.getProfileDirectory() + "/" + profile);
    }

    @DoNotRename
    @FXML
    public void startScriptPressed() {
        Profile settings = new Profile();
        save(settings);
        FileUtilities.createFile(FileUtilities.gson.toJson(settings), Core.getProfileDirectory() + "/" + "last.json");
        runningPrep = true;
        runtimeSettings = settings;
        this.getGUI().close();
    }

    @Override
    public void initialize() {
        try {
            Profile settings = FileUtilities.gson.fromJson(new String(FileUtilities.loadFile(new File(Util.getWorkingDirectory().getAbsolutePath() + Core.getProfileDirectory() + "/last.json"))), Profile.class);
            load(settings);
        } catch (IOException e) {
            Logging.debug("Wasn't able to load last.json");
        }
    }

    public void load(Profile settings) {
        taskList.setItems(settings.getChatterTasks());
        darkMode.setSelected(settings.isDarkModeEnabled());
        Platform.runLater(() -> {
            if (darkMode.isSelected()) {
                getGUI().getScene().getStylesheets().add("https://raw.githubusercontent.com/SkrrtNick/SkrrtPicker/master/src/scripts/gui/dark-mode.css");
            } else {
                getGUI().getScene().getStylesheets().remove("https://raw.githubusercontent.com/SkrrtNick/SkrrtPicker/master/src/scripts/gui/dark-mode.css");
            }
        });
    }

    public void save(Profile settings) {
        settings.setChatterTasks(taskList.getItems());
        settings.setDarkModeEnabled(darkMode.isSelected());
    }



}
