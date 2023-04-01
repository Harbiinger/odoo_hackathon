package clia.front.controllers;

import clia.back.fs.File;
import clia.back.fs.Folder;
import clia.back.fs.Initialiser;
import clia.back.fs.Users;
import clia.cmd.Analyser;
import clia.cmd.Command;
import clia.cmd.CommandHandler;
import clia.front.actions.Action;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Line;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Paths;
import java.util.ArrayList;

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

    @FXML
    TextField inputTextField;
    @FXML
    Label historyLabel, prefixLabel;

    @FXML
    void initialize() throws IOException, ParseException {
        cwd = initialiser.Init();
        // TODO : this can be used as "clear" command code to execute in the handler if command == clear
        pushText("Welcome to TTWhy !");
        // TODO : make date = now - 20 min
        // TODO : time loop duration is a variable
        pushText("Last login: Fri Mar 31 15:17:09 2023 from 163.221.18.5");
        pushText("You have two unread emails. Open mailbox via the 'mail' command.");
        pushText(" ");
    }

    @FXML
    void handleInputTextFieldOnKeyReleased(KeyEvent keyEvent) throws IOException, ParseException {
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
                Command command = Analyser.analyse(input);
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
            case CLEAR -> {
                for (int i = 0; i < MAX_LINES; i++) {
                    pushText("");
                }
            }
            case ERROR_NOT_ENOUGH_ARGS -> pushText("Error : not enough arguments for command '" + initialInputCommand + "'");
            case ERROR_COMMAND_NOT_FOUND -> pushText("Error : command '" + initialInputCommand + "' not found");
            case DISPLAY_CONTENT_OF_FILE -> {
                ArrayList<File> files = new ArrayList<>();
                for (String filename : resultAction.getArgs()) {
                    File fileFound = cwd.getFile(filename);
                    if (fileFound == null) {
                        pushText("File '" + filename + "' not found");
                        return;
                    }
                    files.add(fileFound);
                }
                for (File file : files) {
                    String[] lines = file.getContent(user).split("\n");
                    pushText(file.getName() + " : ");
                    if (lines.length == 1 && lines[0].equals("")) {
                        pushText("Insufficient permission");
                    } else {
                        for (String line : file.getContent(user).split("\n")) {
                            pushText(line);
                        }
                    }
                }
            }
            case DISPLAY_DIRECTORY -> {
                // TODO : get all directories of the cwd and all files from the cwd
                for (String line : cwd.getContent(user).split("\n")) {
                    pushText(line);
                }
            }
            case DISPLAY_MAIL_INFO -> {
                pushText("Welcome user. Mailbox content :");
                cwd = cwd.getFolder("mail");
                for (String line : cwd.getContent(user).split("\n")) {
                    pushText(line.substring(0, line.length() - 4));
                }
                pushText("Read mail via 'mail <number>', i.e. : 'mail 1' to read mail number 1.");
                cwd = cwd.getParent();
            }
            case DISPLAY_MAIL_CONTENT -> {
                cwd = cwd.getFolder("mail");
                for (String mailNumber : resultAction.getArgs()) {
                    pushText("Mail " + mailNumber + " :");
                    for (String line : cwd.getFile("mail" + mailNumber + ".txt").getContent(user).split("\n")) {
                        pushText(line);
                    }
                }
                cwd = cwd.getParent();
            }
            case DISPLAY_CURRENT_USER -> pushText(Users.getUsername(user));
            case CHANGE_USER -> {
                user = Users.getUser(resultAction.getArgs().get(0));
                pushText("Current user changed to '" + Users.getUsername(user) + "'");
                prefixLabel.setText("[" + Users.getUsername(user) + "@edoo ~]$");
            }
            case ERROR_TOO_MANY_ARGS -> pushText("Error : too many arguments for command '" + initialInputCommand + "'");
            case INVALID_USERNAME -> pushText("Error : invalid username '" + resultAction.getArgs().get(0) + "'");
            case INVALID_MAIL_ID -> {
                StringBuilder mails = new StringBuilder();
                for (String s : resultAction.getArgs()) mails.append(" ").append(s);
                pushText("Error : invalid email ID in'" + mails + "'");
            }
            case INVALID_FLAG -> pushText("Error : invalid flag '" + resultAction.getArgs().get(0) + "' for command '" + initialInputCommand + "'");
            case LIST_CRONS -> {
                pushText("# Reboot slaves' brains daily");
                pushText("* * /1 * * rebot");
                pushText("");
                pushText("Error line 2 : 'rebot' command not found.");
                pushText("");
                pushText("READ-ONLY filesystem in DEBUG mode.");
                pushText("In DEBUG mode, you can sudo without password.");
            }
        }
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
    private void pushCommand(String command) throws IOException, ParseException {
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
}
