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
import javafx.scene.input.KeyCode;

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
    private static String configs = "saves";
    private static String currentConfigs = "";
    private static String defaultConfigs = "default_save";

    public static final String OUTPUT_INDICATOR = "output: false";
    private static boolean printOutput = true;
    private static final String OUTPUT_PREFIX = "Sharp$ ";
    
    private Stage stage;
    private Scene baseScene;
    private Pane root;

    private TimedEventRunner appUpdater = new TimedEventRunner();

    // private PlayerControl player1;

    public static String getFileSeperator() {
	return fileSeperator;
    }

    public static String getImagesPath() {
	return imageResources;
    }

    public static String getAudioPath() {
	return audioResources;
    }

    public static String getConfigsPath() {
	return currentConfigs;
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

    public void test1() {
	Unit titleUnit = new Unit(new Img(new Anchor(HALF_WIDTH, HALF_HEIGHT), new CVector(0, 0), "sharp title.gif"));
	root.getChildren().add(titleUnit.getNode());
	TimedEvent updater = new TimedEvent(e -> {
		titleUnit.update();
		Collision.update();
	},
	    1);
	appUpdater.addTimedEvent(updater);
    }

    public void createTests() {
	
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
	configs += fileSeperator;
	currentConfigs = configs + defaultConfigs + fileSeperator;

	root = new Pane();
	root.setPrefSize(WIDTH, HEIGHT);
	baseScene = new Scene(root);

	root.setOnKeyPressed(e -> {
		App.print("Input");
		if (e.getCode().equals(KeyCode.SPACE)) {
		    togglePause(true);
		} else if (e.getCode().equals(KeyCode.ENTER) && appUpdater.getPaused()) {
		    TimedEvent playToggler = new TimedEvent(event -> togglePause(true), 1);
		    playToggler.setMeeseeks(true);
		    appUpdater.addTimedEvent(playToggler);
		    togglePause(false);
		} else {
		    System.out.println("Sending input to player");
		    player1.receiveInput(e);
		}
	    });
	root.setOnKeyReleased(e -> {
		App.print("Input");
		if (e.getCode().equals(KeyCode.SPACE)) {
		    togglePause(false);
		} else {
		    player1.receiveInput(e);
		}
	    });

	Rectangle backer = new Rectangle(0.0, 0.0, WIDTH, HEIGHT);
	backer.setFill(Color.WHITE);
	root.getChildren().add(backer);
	Rectangle yes = new Rectangle(HALF_WIDTH, HALF_HEIGHT, 20, 20);
	yes.setFill(Color.BLACK);
	// root.getChildren().add(yes);

	createOutput();
	createTests();
	appUpdater.setCheckCollision(true);
	
	stage.setTitle("Sharp");
	stage.setScene(baseScene);
	stage.sizeToScene();
	stage.setResizable(false);
	stage.show();

	appUpdater.startRunning(DEF_FRAMERATE);

	root.requestFocus();
    }

    public static boolean print(String output) {
	if (printOutput) {
	    System.out.println(OUTPUT_PREFIX + output);
	    return true;
	}
	return false;
    }

    public void togglePause(boolean pressed) {
	appUpdater.setPaused(pressed);
    }
    
}
