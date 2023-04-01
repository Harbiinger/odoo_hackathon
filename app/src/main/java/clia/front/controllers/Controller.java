package clia.front.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

/**
 * This mother Controller class gives access to a few useful methods to all children classes.
 */
public abstract class Controller {
    /**
     * Returns a new style line with the darker background color to use when the mouse enters a button.
     *
     * @param button The entered button
     * @return The CSS line
     */
    public static String formatNewCSSLineMouseEntered(Button button) {
        String CSSLine = "";
        String buttonText = button.getText();
        if (buttonText.equals("Load") || buttonText.equals("Linux")) {
            // Big buttons
            CSSLine = "-fx-background-color: rgb(218, 163, 171); -fx-border-color: rgb(10, 10, 20); -fx-border-width: 2; -fx-border-insets: -1; -fx-border-radius: 20; -fx-background-radius: 20;";
        }
        return CSSLine;
    }

    /**
     * Returns a new style line with the original background color to use when the mouse exits a button.
     *
     * @param button The exited button
     * @return The CSS line
     */
    public static String formatNewCSSLineMouseExited(Button button) {
        String CSSLine = "";
        String buttonText = button.getText();
        if (buttonText.equals("Load") || buttonText.equals("Linux")) {
            // Big buttons
            CSSLine = "-fx-background-color: rgba(218, 163, 171, 0.7); -fx-border-color: rgb(10, 10, 20); -fx-border-width: 2; -fx-border-insets: -1; -fx-border-radius: 20; -fx-background-radius: 20;";
        }
        return CSSLine;
    }

    @FXML
    void handleButtonMouseEntered(MouseEvent event) {
        Button buttonSource = (Button) event.getSource();
        buttonSource.setStyle(formatNewCSSLineMouseEntered(buttonSource));
    }

    @FXML
    void handleButtonMouseExited(MouseEvent event) {
        Button buttonSource = (Button) event.getSource();
        buttonSource.setStyle(formatNewCSSLineMouseExited(buttonSource));
    }
}