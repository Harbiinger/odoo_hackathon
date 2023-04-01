package clia.front.controllers;

import clia.cmd.Analyser;
import clia.cmd.Command;
import clia.cmd.CommandHandler;
import clia.front.actions.Action;
import clia.front.actions.Actions;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;

/**
 * Controller used for the `MainScene` scene defined in the `MainScene.fxml` file.
 */
public class MainSceneController extends Controller {
    int lineCount = 1;
    final int MAX_LINES = 34;
    /**
     * List of all inputs. Coincides with the commands list.
     */
    ArrayList<String> lines = new ArrayList<>();
    /**
     * List of all commands. Coincides with the lines list.
     */
    ArrayList<Command> commands = new ArrayList<>();
    String username = "user";

    @FXML
    TextField inputTextField;
    @FXML
    Label historyLabel;

    @FXML
    void initialize() {
        // TODO : this can be used as "clear" command code to execute in the handler if command == clear
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
                lines.add("[user@edoo ~]$ " + input);
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
                // TODO : make this work with cyril's code
                /*
                for (File file : (ArrayList<File>) resultAction.getArgs()) {
                    for (String line : file.getContent().split("\n")) {
                        pushText(line);
                    }
                    pushText("");
                    pushText("");
                }
                 */
            }
            case DISPLAY_DIRECTORY -> {
                // TODO : get all directories of the cwd and all files from the cwd
                /*
                StringBuilder bobTheBuilder = new StringBuilder();
                for (Directory dir : cwd.getDirectories()) {
                    bobTheBuilder.append(dir).append("\n");
                }
                ArrayList<File> files = cwd.getFiles();
                for (int i = 0; i < files.size(); i++) {
                    bobTheBuilder.append(files.get(i));
                    if (i != files.size() - 1) bobTheBuilder.append("\n");
                }
                pushText(bobTheBuilder.toString());
                 */
            }
            case DISPLAY_MAIL_INFO -> {
                StringBuilder bobTheBuilder = new StringBuilder();
                bobTheBuilder.append("Welcome user. Mailbox content :\n");
                /*
                ArrayList<String> lines = cwd.getContent().split("\n");
                for (String line : lines) {
                    pushText(line);
                }
                 */
            }
            case DISPLAY_MAIL_CONTENT -> {
                /*
                for (File file : resultAction.getArgs()) {
                    pushText("Mail " + file.name + " :");
                    for (String line : file.getContent().split("\n")) {
                        pushText(line);
                    }
                    pushText("");
                }
                 */
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
}
