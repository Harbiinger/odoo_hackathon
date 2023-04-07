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
        String buttonID = button.getId();
        if (buttonID.equals("something")) {
            CSSLine = "some style";
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
        String buttonID = button.getId();
        if (buttonID.equals("something")) {
            CSSLine = "some style";
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