package clia.front.controllers;

import clia.App;
import clia.back.fs.File;
import clia.back.fs.Folder;
import clia.back.fs.Initialiser;
import clia.back.fs.Users;
import clia.cmd.Analyser;
import clia.cmd.Command;
import clia.cmd.CommandHandler;
import clia.front.actions.Action;
import clia.front.navigation.Flow;
import clia.front.scenes.SceneLoader;
import clia.front.scenes.Scenes;
import com.sun.tools.javac.Main;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;

/**
 * Controller used for the `MainScene` scene defined in the `MainScene.fxml` file.
 */
public class MainSceneController extends Controller {
    int lineCount = 1;
    final int MAX_LINES = 19;
    /**
     * List of all inputs. Coincides with the commands list.
     */
    ArrayList<String> lines = new ArrayList<>();
    /**
     * List of all commands. Coincides with the lines list.
     */
    ArrayList<Command> commands = new ArrayList<>();
    Users user = Users.Employee;
    Initialiser initialiser = new Initialiser(Paths.get(System.getProperty("user.dir")) + "/build/resources/main/gameData/data.json");
    Folder cwd;
    boolean waitingForPassword = false;

    @FXML
    TextField inputTextField;
    @FXML
    Label historyLabel, prefixLabel;

    @FXML
    void initialize() throws IOException, ParseException {
        cwd = initialiser.Init();
        pushText("Welcome to TTWhy !");
        // TODO : make date = now - 20 min
        // TODO : time loop duration is a variable
        pushText("Last login: Fri Mar 31 15:17:09 2023 from 163.221.18.5");
        pushText("You have two unread emails. Open mailbox via the 'mail' command.");
        pushText(" ");
    }

    @FXML
    void handleInputTextFieldOnKeyReleased(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            keyEvent.consume();
            if (lineCount == MAX_LINES) {
                lines.remove(0);
            } else lineCount++;

            String input = inputTextField.getText();

            if (input.equals("")) lines.add(" ");
            else {
                lines.add("[" + Users.getUsername(user) + "@edoo ~]$ " + input);
                // Send the input to the analyser and retrieve the resulting command
                Command command = Analyser.analyse(input, user);
                // Send the command to the handler and retrieve the potential actions that must be taken in the GUI
                Action resultAction = CommandHandler.handle(command);
                // Send the resulting action to the local handler
                handleResultAction(resultAction, input.split(" ")[0]);
            }

            inputTextField.setText("");
            historyLabel.setText(formatLines());
        }
    }

    private void handleResultAction(Action resultAction, String initialInputCommand) {
        switch (resultAction.getAction()) {
            case CLEAR -> clear();
            case ERROR_NOT_ENOUGH_ARGS -> notEnoughArgs(initialInputCommand);
            case ERROR_COMMAND_NOT_FOUND -> commandNotFound(initialInputCommand);
            case DISPLAY_CONTENT_OF_FILE -> cat(resultAction.getArgs(), resultAction.isAsRoot());
            case DISPLAY_DIRECTORY -> ls(cwd.getContent(resultAction.isAsRoot() ? Users.Manager : user));
            case DISPLAY_MAIL_INFO -> displayMailInfo(resultAction.isAsRoot());
            case DISPLAY_MAIL_CONTENT -> displayMailContent(resultAction.getArgs(), resultAction.isAsRoot());
            case DISPLAY_CURRENT_USER -> whoami();
            case CHANGE_DIRECTORY -> cd(resultAction.getArgs().get(0));
            // TODO : password
            case CHANGE_USER -> su(resultAction.getArgs().get(0));
            case ERROR_TOO_MANY_ARGS -> tooManyArguments(initialInputCommand);
            case INVALID_USERNAME -> invalidUsername(resultAction);
            case INVALID_MAIL_ID -> invalidMailID(resultAction.getArgs());
            case INVALID_FLAG -> invalidFlag(resultAction.getArgs().get(0), initialInputCommand);
            case LIST_CRONS -> listCrons();
            case REBOOT -> reboot();
            case INSUFFICIENT_PERMISSION -> insufficientPermission();
            case SHUTDOWN -> shutdown();
            case ERROR_NOT_ENOUGH_ARGS_KILL -> notEnoughArgsKill(initialInputCommand);
            case KILL -> kill(resultAction.getArgs().get(0));
            case GETPID -> getpid(resultAction.getArgs().get(0));
        }
    }

    private void kill(String arg) {
        // TODO : check if arg is a number
        int pid;
        try {
            pid = Integer.parseInt(arg);
        } catch (NumberFormatException e) {
            pushText("The argument must be a PID.");
            return;
        }
        switch (pid) {
            case 2177 -> {
                video();
            }
            case 2178 -> pushText("Can't kill process mngr. Important tasks are running.");
            case 2179 -> pushText("Can't kill process theo. Important tasks are running.");
            default -> pushText("Error : process " + arg + " not found");
        }
    }

    private void getpid(String arg) {
        switch (arg) {
            case "user" -> pushText("PID of user : 2177");
            case "mngr" -> pushText("PID of mngr : 2178");
            case "theo" -> pushText("PID of theo : 2179");
            default -> pushText("Error : process " + arg + " not found");
        }
    }

    private void clear() {
        for (int i = 0; i < MAX_LINES; i++) {
            pushText("");
        }
    }

    private void notEnoughArgs(String initialInputCommand) {
        pushText("Error : not enough arguments for command '" + initialInputCommand + "'");
    }

    private void commandNotFound(String initialInputCommand) {
        if (waitingForPassword) {
            if (initialInputCommand.equals("4-8-15-16-23-42")) {
                user = Users.CEO;
                pushText("Current user changed to '" + Users.getUsername(user) + "'");
                prefixLabel.setText("[" + Users.getUsername(user) + "@edoo ~]$");
            } else {
                pushText("Incorrect password.");
                waitingForPassword = false;
            }
        } else {
            pushText("Error : command '" + initialInputCommand + "' not found");
        }
    }

    private void ls(String content) {
        for (String line : content.split("\n")) {
            pushText(line);
        }
    }

    private void cd(String dir) {
        if (dir.equals("..")) {
            Folder tmp = cwd.getParent();
            if (tmp != null) {
                cwd = tmp;
                pushText("Switch to "+ cwd.getName() + " directory");
            }
        } else {
            Folder tmp = cwd.getFolder(dir);
            if (tmp != null) {
                cwd = tmp;
                pushText("Switch to "+ cwd.getName() + " directory");
            }
            else pushText("Error : folder '" + dir + "' not found");
        }
    }

    private void displayMailInfo(boolean runAsRoot) {
        pushText("Welcome " + Users.getUsername(user) + ". Mailbox content :");
        if (runAsRoot || user == Users.Manager) {
            Folder tmp = cwd.getFolder("StaffOnly");
            if (tmp != null) cwd = tmp;
        }
        cwd = cwd.getFolder("mail");
        System.out.println(cwd.getContent(runAsRoot ? Users.Manager : user));
        for (String line : cwd.getContent(runAsRoot ? Users.Manager : user).split("\n")) {
            pushText(line.substring(0, line.length() - 4));
        }
        pushText("Read mail via 'mail <number>', i.e. : 'mail 1' to read mail1.");
        cwd = cwd.getParent();
        if (runAsRoot) cwd = cwd.getParent();
    }

    private void displayMailContent(ArrayList<String> mailNumbers, boolean runAsRoot) {
        // TODO : show mail from home dir (careful if in documents)
        if (runAsRoot || user == Users.Manager) {
            Folder tmp = cwd.getFolder("StaffOnly");
            if (tmp != null) cwd = tmp;
        }
        cwd = cwd.getFolder("mail");
        for (String mailNumber : mailNumbers) {
            pushText("Mail " + mailNumber + " :");
            for (String line : cwd.getFile("mail" + mailNumber + ".txt").getContent(runAsRoot ? Users.Manager : user).split("\n")) {
                pushText(line);
            }
        }
        cwd = cwd.getParent();
        if (runAsRoot) cwd = cwd.getParent();
    }

    private void cat(ArrayList<String> filenames, boolean runAsRoot) {
        ArrayList<File> files = new ArrayList<>();
        for (String filename : filenames) {
            File fileFound = cwd.getFile(filename);
            if (fileFound == null) {
                pushText("File '" + filename + "' not found");
                return;
            }
            files.add(fileFound);
        }
        for (File file : files) {
            String[] lines = file.getContent(runAsRoot ? Users.Manager : user).split("\n");
            pushText(file.getName() + " : ");
            if (lines.length == 1 && lines[0].equals("")) {
                insufficientPermission();
            } else {
                ls(file.getContent(user));
            }
        }
    }

    private void whoami() {
        pushText(Users.getUsername(user));
    }

    private void su(String username) {
        if (username.equals("theo")) {
            pushText("Password:");
            waitingForPassword = true;
        } else {
            user = Users.getUser(username);
            pushText("Current user changed to '" + Users.getUsername(user) + "'");
            prefixLabel.setText("[" + Users.getUsername(user) + "@edoo ~]$");
        }
    }

    private void tooManyArguments(String initialInputCommand) {
        pushText("Error : too many arguments for command '" + initialInputCommand + "'");
    }

    private void invalidUsername(Action resultAction) {
        pushText("Error : invalid username '" + resultAction.getArgs().get(0) + "'");
    }

    private void invalidMailID(ArrayList<String> IDs) {
        StringBuilder mails = new StringBuilder();
        for (String s : IDs) mails.append(s);
        pushText("Error : invalid mail ID in '" + mails + "'");
    }

    private void invalidFlag(String arg, String initialInputCommand) {
        pushText("Error : invalid flag '" + arg + "' for command '" + initialInputCommand + "'");
    }

    private void listCrons() {
        pushText("# Reboot slaves' brains daily");
        pushText("* * /1 * * rebot");
        pushText("");
        pushText("Error line 2 : 'rebot' command not found.");
        pushText("");
        pushText("READ-ONLY filesystem in DEBUG mode.");
        pushText("In DEBUG mode, you can sudo without password.");
    }

    private void reboot() {
        pushText("Reboot is already scheduled by the system and should not be triggered manually.");
    }

    private void insufficientPermission() {
        pushText("Insufficient permission");
    }

    private void shutdown() {
        pushText("Shutting down...");
    }

    private void notEnoughArgsKill(String initialInputCommand) {
        notEnoughArgs(initialInputCommand);
        pushText("Usage : kill [pid]");
    }

    private String formatLines() {
        StringBuilder bobTheBuilder = new StringBuilder();
        for (int i = 0; i < lines.size(); i++) {
            bobTheBuilder.append(lines.get(i));
            if (i != lines.size() - 1) bobTheBuilder.append("\n");
        }
        return bobTheBuilder.toString();
    }

    /**
     * Executes the given command. For example : pushCommand("ls") types in 'ls' and sends the command.
     * @param command The command to execute
     */
    private void pushCommand(String command) {
        inputTextField.setText(command);
        handleInputTextFieldOnKeyReleased(new KeyEvent(null, null, null, "\n", "\n", KeyCode.ENTER, false, false, false, false));
    }

    /**
     * Adds the given text to the visualised history for display. Serves a purpose for response to user commands.
     * For example : the user inputs 'azerty' and the result is 'command not found'.
     * @param text The text to display
     */
    private void pushText(String text) {
        if (lineCount == MAX_LINES) {
            lines.remove(0);
        } else lineCount++;
        lines.add(text);
        historyLabel.setText(formatLines());
    }

    public void video() {
        URL url = App.class.getResource("/video/jellyfish jam.mp4" );
        Media media = new Media(url.toString());

        MediaPlayer mediaPlayer = new MediaPlayer(media);
        System.out.println("Media loaded");
        MediaView mediaView = new MediaView(mediaPlayer);
        System.out.println("MediaView created");

        StackPane mainPane = new StackPane();
        mainPane.getChildren().add(mediaView);
        System.out.println("StackPane created");

        mediaPlayer.play();
        System.out.println("Media playing");

        Scene scene = new Scene(mainPane, 1280, 720);

        App.setScene(scene);
    }
}
