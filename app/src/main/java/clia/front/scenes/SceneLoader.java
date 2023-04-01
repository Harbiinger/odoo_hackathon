package clia.front.scenes;

import clia.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.net.URL;

/**
 * Handles the loading of FXML files to create the Scenes.
 */
public class SceneLoader {

    /**
     * Takes the given scene name to find the corresponding FXML file.
     *
     * @param sceneName the name of the scene to load (that is, the name of the FXML file,
     *                  .fxml extension is not mandatory in the string, but it is in the name of the file
     * @return the Scene that was created from the FXML file
     */
    public static Scene load(String sceneName) {
        if (!sceneName.endsWith(".fxml")) sceneName += ".fxml";
        URL url = App.class.getResource("/scenes/" + sceneName);
        FXMLLoader FXMLLoader = new FXMLLoader(url);
        try {
            Parent sceneParent = FXMLLoader.load();
            return new Scene(sceneParent, 1280, 720);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(url + " could not be found");
        }
        return null;
    }
}

