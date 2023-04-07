package clia.front.controllers;

import clia.App;
import clia.back.fs.File;
import clia.back.fs.Folder;
import clia.back.fs.Initialiser;
import clia.back.fs.Users;
import clia.cmd.Analyser;
import clia.cmd.Command;
import clia.cmd.CommandHandler;
import clia.cmd.LinesBuffer;
import clia.front.actions.Action;
import clia.front.animation.FadeInTransition;
import clia.front.animation.FadeOutTransition;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Controller used for the `MainScene` scene defined in the `MainScene.fxml` file.
 */
public class MainSceneController extends Controller {
    final int MAX_LINES = 18;
    int arrowIndex = 0;
    LinesBuffer<String> linesBuffer = new LinesBuffer<>(MAX_LINES);
    /**
     * List of all commands. Coincides with the lines list.
     */
    ArrayList<Command> commands = new ArrayList<>();
    Users user = Users.Employee;
    Initialiser initialiser = new Initialiser(Paths.get(System.getProperty("user.dir")) + "/build/resources/main/files/filesystem.json");
    Folder cwd;
    boolean waitingForPassword = false;

    @FXML
    TextField inputTextField;
    @FXML
    Label historyLabel, prefixLabel;
    @FXML
    AnchorPane rootPane;

    @FXML
    void initialize() throws IOException, ParseException {
        cwd = initialiser.Init();
        user = Users.Employee;
        prefixLabel.setText("[" + Users.getUsername(user) + "@edoo ~]$");
        pushText("Welcome to TTWhy !");
        LocalDateTime dateTime = LocalDateTime.now().minusMinutes(20);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss yyyy");
        pushText("Last login: " + dateTime.format(formatter) + " from " + InetAddress.getLocalHost().getHostAddress());
        pushText("You have two unread emails. Open mailbox via the 'mail' command.");
        pushText(" ");
    }

    @FXML
    void handleInputTextFieldOnKeyReleased(KeyEvent keyEvent) throws IOException, ParseException {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            keyEvent.consume();

            String input = inputTextField.getText();

            if (input.equals("")) linesBuffer.add(" ");
            if (input.equals("")) commands.add(null);
            else {
                linesBuffer.add("[" + Users.getUsername(user) + "@edoo ~]$ " + input);
                // Send the input to the analyser and retrieve the resulting command
                Command command = Analyser.analyse(input, user);
                // Send the command to the handler and retrieve the potential actions that must be taken in the GUI
                Action resultAction = CommandHandler.handle(command);
                // Send the resulting action to the local handler
                handleResultAction(resultAction, input.split(" ")[0]);
            }

            inputTextField.setText("");
            historyLabel.setText(linesBuffer.formatBufferContent());
            arrowIndex = 1;
        } else if (keyEvent.getCode() == KeyCode.UP) {
            /*
            try {
                String previousText = linesBuffer.get(linesBuffer.size() - arrowIndex * 2).substring(15);
                if (!previousText.equals("y !") && !previousText.equals("read emails. Open mailbox via the 'mail' command.")) {
                    inputTextField.setText(previousText);
                    arrowIndex++;
                }
            } catch (IndexOutOfBoundsException ignored) {
            }
             */
        } else if (keyEvent.getCode() == KeyCode.DOWN) {
            /*
            try {
                if (arrowIndex >= 2) {
                    arrowIndex--;
                    String previousText = linesBuffer.get(linesBuffer.size() - arrowIndex * 2).substring(15);
                    if (!previousText.equals("y !") && !previousText.equals("read emails. Open mailbox via the 'mail' command.")) {
                        inputTextField.setText(previousText);
                    }
                }
            } catch (IndexOutOfBoundsException ignored) {
            }
             */
        }
    }

    private void handleResultAction(Action resultAction, String initialInputCommand) throws IOException, ParseException {
        ResultActionHandler handler = new ResultActionHandler();

        switch (resultAction.getAction()) {
            case CLEAR -> handler.handleCLEAR();
            case ERROR_NOT_ENOUGH_ARGS -> handler.handleERROR_NOT_ENOUGH_ARGS(initialInputCommand);
            case ERROR_COMMAND_NOT_FOUND -> handler.handlerERROR_COMMAND_NOT_FOUND(initialInputCommand);
            case DISPLAY_CONTENT_OF_FILE -> handler.handleDISPLAY_CONTENT_OF_FILE(resultAction.getArgs(), resultAction.isAsRoot());
            case DISPLAY_DIRECTORY -> handler.handleDISPLAY_DIRECTORY(cwd.getContent(resultAction.isAsRoot() ? Users.Manager : user));
            case DISPLAY_MAIL_INFO -> handler.handleDISPLAY_MAIL_INFO(resultAction.isAsRoot());
            case DISPLAY_MAIL_CONTENT -> handler.handleDISPLAY_MAIL_CONTENT(resultAction.getArgs(), resultAction.isAsRoot());
            case DISPLAY_CURRENT_USER -> handler.handleDISPLAY_CURRENT_USER();
            case CHANGE_DIRECTORY -> handler.handleCHANGE_DIRECTORY(resultAction.getArgs().get(0));
            case CHANGE_USER -> handler.handleCHANGE_USER(resultAction.getArgs().get(0));
            case ERROR_TOO_MANY_ARGS -> handler.handleERROR_TOO_MANY_ARGS(initialInputCommand);
            case INVALID_USERNAME -> handler.handleINVALID_USERNAME(resultAction);
            case INVALID_MAIL_ID -> handler.handleINVALID_MAIL_ID(resultAction.getArgs());
            case INVALID_FLAG -> handler.handleINVALID_FLAG(resultAction.getArgs().get(0), initialInputCommand);
            case LIST_CRONS -> handler.handleLIST_CRONS();
            case REBOOT -> handler.handleREBOOT();
            case INSUFFICIENT_PERMISSION -> handler.handleINSUFFICIENT_PERMISSION();
            case SHUTDOWN -> handler.handleSHUTDOWN();
            case ERROR_NOT_ENOUGH_ARGS_KILL -> handler.handleERROR_NOT_ENOUGH_ARGS_KILL(initialInputCommand);
            case KILL -> handler.handleKILL(resultAction.getArgs().get(0));
            case GETPID -> handler.handleGETPID(resultAction.getArgs().get(0));
        }
    }

    /**
     * Executes the given command. For example : pushCommand("ls") types in 'ls' and sends the command.
     *
     * @param command The command to execute
     */
    private void pushCommand(String command) throws IOException, ParseException {
        inputTextField.setText(command);
        handleInputTextFieldOnKeyReleased(new KeyEvent(null, null, null, "\n", "\n", KeyCode.ENTER, false, false, false, false));
    }

    /**
     * Adds the given text to the visualised history for display. Serves a purpose for response to user commands.
     * For example : the user inputs 'azerty' and the result is 'command not found'.
     *
     * @param text The text to display
     */
    private void pushText(String text) {
        linesBuffer.add(text);
        historyLabel.setText(linesBuffer.formatBufferContent());
    }

    public void video() {
        URL url = App.class.getResource("/video/odoo.mp4");
        Media media = new Media(url.toString());

        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);

        Pane pane = new Pane();
        pane.setOpacity(0);
        pane.setPrefSize(1280, 720);
        pane.setStyle("-fx-background-color: black;");

        System.out.println(pane);

        StackPane mainPane = new StackPane();
        mainPane.getChildren().add(mediaView);
        mainPane.getChildren().add(pane);

        mediaPlayer.play();

        Scene scene = new Scene(mainPane, 1280, 720);

        App.setScene(scene);
    }



    private class ResultActionHandler {
        private void handleKILL(String arg) {
            // TODO : check if arg is a number
            int pid;
            try {
                pid = Integer.parseInt(arg);
            } catch (NumberFormatException e) {
                pushText("The argument must be a PID.");
                return;
            }
            switch (pid) {
                case 2177 -> video();
                case 2178 -> pushText("Can't kill process mngr. Important tasks are running.");
                case 2179 -> pushText("Can't kill process theo. Important tasks are running.");
                default -> pushText("Error : process " + arg + " not found");
            }
        }

        private void handleGETPID(String arg) {
            switch (arg) {
                case "user" -> pushText("PID of user : 2177");
                case "mngr" -> pushText("PID of mngr : 2178");
                case "theo" -> pushText("PID of theo : 2179");
                default -> pushText("Error : process " + arg + " not found");
            }
        }

        private void handleCLEAR() {
            for (int i = 0; i < MAX_LINES; i++) {
                pushText("");
            }
        }

        private void handleERROR_NOT_ENOUGH_ARGS(String initialInputCommand) {
            pushText("Error : not enough arguments for command '" + initialInputCommand + "'");
        }

        private void handlerERROR_COMMAND_NOT_FOUND(String initialInputCommand) {
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

        private void handleDISPLAY_DIRECTORY(String content) {
            for (String line : content.split("\n")) {
                pushText(line);
            }
        }

        private void handleCHANGE_DIRECTORY(String dir) {
            if (dir.equals("..")) {
                Folder tmp = cwd.getParent();
                if (tmp != null) {
                    cwd = tmp;
                    pushText("Switched to " + cwd.getName() + " directory");
                }
            } else {
                Folder tmp = cwd.getFolder(dir);
                if (tmp != null) {
                    cwd = tmp;
                    pushText("Switched to " + cwd.getName() + " directory");
                } else pushText("Error : folder '" + dir + "' not found");
            }
        }

        private void handleDISPLAY_MAIL_INFO(boolean runAsRoot) {
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

        private void handleDISPLAY_MAIL_CONTENT(ArrayList<String> mailNumbers, boolean runAsRoot) {
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

        private void handleDISPLAY_CONTENT_OF_FILE(ArrayList<String> filenames, boolean runAsRoot) {
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
                    handleINSUFFICIENT_PERMISSION();
                } else {
                    handleDISPLAY_DIRECTORY(file.getContent(user));
                }
            }
        }

        private void handleDISPLAY_CURRENT_USER() {
            pushText(Users.getUsername(user));
        }

        private void handleCHANGE_USER(String username) {
            if (username.equals("theo")) {
                pushText("Password:");
                waitingForPassword = true;
            } else {
                user = Users.getUser(username);
                pushText("Current user changed to '" + Users.getUsername(user) + "'");
                prefixLabel.setText("[" + Users.getUsername(user) + "@edoo ~]$");
            }
        }

        private void handleERROR_TOO_MANY_ARGS(String initialInputCommand) {
            pushText("Error : too many arguments for command '" + initialInputCommand + "'");
        }

        private void handleINVALID_USERNAME(Action resultAction) {
            pushText("Error : invalid username '" + resultAction.getArgs().get(0) + "'");
        }

        private void handleINVALID_MAIL_ID(ArrayList<String> IDs) {
            StringBuilder mails = new StringBuilder();
            for (String s : IDs) mails.append(s);
            pushText("Error : invalid mail ID in '" + mails + "'");
        }

        private void handleINVALID_FLAG(String arg, String initialInputCommand) {
            pushText("Error : invalid flag '" + arg + "' for command '" + initialInputCommand + "'");
        }

        private void handleLIST_CRONS() {
            pushText("# Reboot slaves' brains daily");
            pushText("* * /1 * * rebot");
            pushText("");
            pushText("Error line 2 : 'rebot' command not found.");
            pushText("");
            pushText("READ-ONLY filesystem in DEBUG mode.");
            pushText("In DEBUG mode, you can sudo without password.");
        }

        private void handleREBOOT() {
            pushText("Reboot is already scheduled by the system and should not be triggered manually.");
        }

        private void handleINSUFFICIENT_PERMISSION() {
            pushText("Insufficient permission");
        }

        private void handleSHUTDOWN() throws IOException, ParseException {
            FadeTransition ft = FadeOutTransition.playFromStartOn(rootPane, new Duration(1500));
            ft.setOnFinished(event -> {
                try {
                    handleCLEAR();
                    initialize();
                } catch (IOException | ParseException e) {
                    throw new RuntimeException(e);
                }
                FadeInTransition.playFromStartOn(rootPane, new Duration(1500));
            });
            ft.playFromStart();

        }

        private void handleERROR_NOT_ENOUGH_ARGS_KILL(String initialInputCommand) {
            handleERROR_NOT_ENOUGH_ARGS(initialInputCommand);
            pushText("Usage : kill [pid]");
        }
    }
}
