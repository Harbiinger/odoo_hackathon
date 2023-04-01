package clia.front.controllers;

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
    ArrayList<String> lines = new ArrayList<>();

    @FXML
    TextField inputTextField;
    @FXML
    Label historyLabel;

    @FXML
    void initialize() {
        // TODO : this can be used as "clear" command code to execute in the handler if command == clear
        command("Welcome to TTWhy !");
        // TODO : make date = now - 20 min
        // TODO : time loop duration is a variable
        command("Last login: Fri Mar 31 15:17:09 2023 from 163.221.18.5");
        command("You have two unread emails. Open mailbox via the 'mail' command.");
        command("");
    }

    @FXML
    void handleInputTextFieldOnKeyReleased(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            keyEvent.consume();
            if (lineCount == MAX_LINES) {
                lines.remove(0);
            } else lineCount++;
            if (inputTextField.getText().equals("")) lines.add(" ");
            else lines.add(inputTextField.getText());
            inputTextField.setText("");
            historyLabel.setText(formatLines());
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

    private void command(String command) {
        inputTextField.setText(command);
        handleInputTextFieldOnKeyReleased(new KeyEvent(null, null, null, "\n", "\n", KeyCode.ENTER, false, false, false, false));
    }
}
