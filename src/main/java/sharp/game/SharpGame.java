package sharp.game;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;

/** This class represents the primary app of the game. */
public class SharpGame extends Application {

    private Stage stage;
    private Scene baseScene;
    
    /**
     * This calls as the app is launched and initializes every
     * neccessary instance variable, starting the game.
     *
     * @param primaryStage - the stage to display this app in
     */
    public void start(Stage primaryStage) {
	this.stage = primaryStage;
	
	baseScene = new Scene(new VBox());
	
	stage.setTitle("Sharp");
	stage.setScene(baseScene);
	stage.sizeToScene();
	stage.setResizable(false);
	stage.show();
    }

}
