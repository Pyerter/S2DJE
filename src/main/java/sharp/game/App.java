package sharp.game;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;

/** This class represents the primary app of the game. */
public class App extends Application {

    public static final Double WIDTH = 1280;
    public static final Double HEIGHT = 720;
    public static final Double HALF_WIDTH = 640;
    public static final Double HALF_HEIGHT = 360;
    public static final CVector scalar = new CVector(1.0, 1.0);
    public static final CVector halfScalar = new CVector(0.5, 0.5);
    
    private Stage stage;
    private Scene baseScene;

    /**
     * This sets the scalar vector so that any scaling objects
     * can match with the desired width and height.
     *
     * @param width - the requested new width
     * @param height - the requested new height
     */
    public void setScreen(double width, double height) {
	scalar.setX(width / WIDTH);
	scalar.setY(height / HEIGHT);
	halfScalar.setX(scalar.getX() / 2);
	halfScalar.setY(scalar.getY() / 2);
    }

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
