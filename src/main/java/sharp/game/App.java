package sharp.game;

import sharp.utility.CVector;
import sharp.utility.TimedEventRunner;
import sharp.utility.TimedEvent;
import sharp.unit.*;
import sharp.collision.Collision;

import java.io.*;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

/** This class represents the primary app of the game. */
public class App extends Application {

    public static final Double WIDTH = 1280.0;
    public static final Double HEIGHT = 720.0;
    public static final Double HALF_WIDTH = 640.0;
    public static final Double HALF_HEIGHT = 360.0;
    public static final CVector scalar = new CVector(1.0, 1.0);
    public static final CVector halfScalar = new CVector(0.5, 0.5);

    public static final int DEF_FRAMERATE = 60;
    
    private Stage stage;
    private Scene baseScene;
    private Pane root;

    private TimedEventRunner appUpdater = new TimedEventRunner();

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

    private void createOutput() {
	try {
	    PrintStream o = new PrintStream(new File("javaOutFile.txt"));
	    // System.setOut(o);
	} catch (FileNotFoundException e) {
	    // yes
	}
    }

    private void createDraft() {
	SimplePolyUnit player = new SimplePolyUnit(new CVector(0, 0),
						   new CVector(0, 20),
						   new CVector(20, 20),
						   new CVector(20, 0));
	/*for (int i = 0; i < 12; i++) {
	    poly.getPoints().addAll(20 * Math.cos(2 * Math.PI * (i / 12.0)),
				    20 * Math.sin(2 * Math.PI * (i / 12.0)));
				    }*/
	player.getPolygon().setFill(Color.BLACK);
	System.out.println("Added player poly: " + player.toString());
	root.getChildren().add(player.getNode());

	SimpleImgUnit playerFace = new SimpleImgUnit("sharp title.png",
						     HALF_WIDTH, HEIGHT, WIDTH, HEIGHT);
	root.getChildren().add(playerFace.getNode());
	playerFace.setGrav(false);

	SimplePolyUnit top = new SimplePolyUnit(new CVector(-10, -20),
						new CVector(10, -20),
						new CVector(10, -5),
						new CVector(-10, -5));
	SimplePolyUnit bottom = new SimplePolyUnit(new CVector(-10, 20),
						   new CVector(10, 20),
						   new CVector(10, 5),
						   new CVector(-10, 5));
	ComplexUnit topBottom = new ComplexUnit(new CVector(WIDTH - 40, 40),
						top,
						bottom);
	topBottom.addCollidables(playerFace);
	root.getChildren().add(topBottom.getNode());
	
	TimedEvent playerUpdate = new TimedEvent(e -> {
		// testing movement
		player.getAcceleration().add(new CVector(0.01, 0.0));
		player.setRotAcceleration(0.01);
		player.update();
		// playerFace.setRotAcceleration(0.0001);
		// playerFace.resize(new CVector(playerFace.getIV().getFitWidth() - 5, playerFace.getIV().getFitHeight() - 5));
		playerFace.update();
		topBottom.setRotAcceleration(topBottom.getRotAcceleration() + 0.001);
		topBottom.update();
		Collision.update();
		System.out.println("\n\n - - - - - - Frame " + appUpdater.getCount() + ":\n\n");
	},
	    1);
			   
	appUpdater.addTimedEvent(playerUpdate);
    }

    /**
     * This calls as the app is launched and initializes every
     * neccessary instance variable, starting the game.
     *
     * @param primaryStage - the stage to display this app in
     */
    public void start(Stage primaryStage) {
	this.stage = primaryStage;

	root = new Pane();
	root.setPrefSize(WIDTH, HEIGHT);
	baseScene = new Scene(root);

	Rectangle backer = new Rectangle(0.0, 0.0, WIDTH, HEIGHT);
	backer.setFill(Color.WHITE);
	root.getChildren().add(backer);
	Rectangle yes = new Rectangle(HALF_WIDTH, HALF_HEIGHT, 20, 20);
	yes.setFill(Color.BLACK);
	// root.getChildren().add(yes);

	createOutput();
	createDraft();
	
	stage.setTitle("Sharp");
	stage.setScene(baseScene);
	stage.sizeToScene();
	stage.setResizable(false);
	stage.show();

	appUpdater.startRunning(DEF_FRAMERATE);
    }
    
}
