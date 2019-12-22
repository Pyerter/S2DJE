package sharp.game;

import sharp.utility.CVector;
import sharp.utility.Anchor;
import sharp.utility.TimedEventRunner;
import sharp.utility.TimedEvent;
import sharp.utility.Sound;
import sharp.utility.Transform;
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

    public static final int DEF_FRAMERATE = 30;

    public static final String MAC_INDICATOR = "os: mac";
    private static String fileSeperator = "\\";
    private static String audioResources = "resources";
    private static String imageResources = "file:resources";

    public static final String OUTPUT_INDICATOR = "output: false";
    private static boolean printOutput = true;
    
    private Stage stage;
    private Scene baseScene;
    private Pane root;

    private TimedEventRunner appUpdater = new TimedEventRunner();

    public static String getFileSeperator() {
	return fileSeperator;
    }

    public static String getImagesPath() {
	return imageResources;
    }

    public static String getAudioPath() {
	return audioResources;
    }

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
	},
	    1);

	TimedEvent makeNoise = new TimedEvent(e -> {
		switch ((int)(Math.random() * 4)) {
		default:
		case 0:
		    Sound.play("Laser Gun 2 Short.wav");
		    break;
		case 1:
		    Sound.play("Laser Gun 2 Long.wav");
		    break;
		case 2:
		    Sound.play("Laser Gun 1 Short.wav");
		    break;
		case 3:
		    Sound.play("Laser Gun 1 Long.wav");
		    break;
		}
	},
	    60);
			   
	appUpdater.addTimedEvent(playerUpdate);
	appUpdater.addTimedEvent(makeNoise);

	SimplePolyUnit base = new SimplePolyUnit(new Anchor(0, 0),
						 new CVector(-10, -10),
						 new CVector(10, -10),
						 new CVector(10, 10),
						 new CVector(0, 10),
						 new CVector(-10, 10));
	base.getPolygon().setFill(Color.BLACK);
	HingedUnit parent = new HingedUnit(base);
	SimplePolyUnit extension = new SimplePolyUnit(new Anchor(0, -10),
						      new CVector(5, 10),
						      new CVector(-5, 10),
						      new CVector(-5, -10),
						      new CVector(5, -10));
	extension.getPolygon().setFill(Color.BLACK);
	HingedUnit extensionHinge = new HingedUnit(extension);
	parent.addHingedUnit(extensionHinge, 3);
	parent.setGrav(true);
	parent.applyTransform(new Transform(HALF_WIDTH, 0.0));
	root.getChildren().add(parent.getNode());

	parent.addCollidables(playerFace);

	TimedEvent hingeUpdater = new TimedEvent(e -> {
		extensionHinge.setRotAcceleration(extensionHinge.getRotAcceleration() + 0.01);
		parent.getAcceleration().add(new CVector(0.01, 0));
		parent.update();
	},
	    1);

	appUpdater.addTimedEvent(hingeUpdater);
	
    }

    public void createHingedTest() {
	
    }

    /**
     * This calls as the app is launched and initializes every
     * neccessary instance variable, starting the game.
     *
     * @param primaryStage - the stage to display this app in
     */
    public void start(Stage primaryStage) {
	this.stage = primaryStage;

	if (getParameters().getRaw().contains(MAC_INDICATOR)) {
	    fileSeperator = "/";
	}

	if (getParameters().getRaw().contains(OUTPUT_INDICATOR)) {
	    printOutput = false;
	}

	audioResources += fileSeperator + "audio" + fileSeperator;
	imageResources += fileSeperator + "images" + fileSeperator;

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
	createHingedTest();
	appUpdater.setCheckCollision(true);
	
	stage.setTitle("Sharp");
	stage.setScene(baseScene);
	stage.sizeToScene();
	stage.setResizable(false);
	stage.show();

	appUpdater.startRunning(DEF_FRAMERATE);
    }

    public boolean print(String output) {
	if (printOutput) {
	    System.out.println("Sharp$(Game Output:) " + output);
	    return true;
	}
	return false;
    }
    
}
