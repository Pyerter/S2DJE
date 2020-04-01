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
import javafx.scene.image.Image;

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
    public static final String OUTPUT_FALSE_INDICATOR = "output: true";
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

    public static void setImagesPath(String s) {
	imageResources = "file:" + s;
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
	Unit<Img> imgUnit = new Unit<>(new Img(new Anchor(50, 0), null, 
					       new CVector(0, 0), "sharp title.png"));
	imgUnit.getProjection().addImage(new Image(getImagesPath() + "player character" + fileSeperator + "front face" + fileSeperator + "base_0.png", 50, 50, false, true));
	// imgUnit.addTransform(new Transform(0, HALF_HEIGHT / 2));
	imgUnit.getPivot().setGrav(true);
	root.getChildren().add(imgUnit.getNode());
	Unit<Poly> polyUnit = new Unit<>(new Poly(new Anchor(0, HALF_HEIGHT - 50),
						  new CVector(-10, HALF_HEIGHT - 60),
						  new CVector(10, HALF_HEIGHT - 60),
						  new CVector(10, HALF_HEIGHT - 40),
						  new CVector(-10, HALF_HEIGHT - 40)));
	polyUnit.getPivot().setGrav(true);
	root.getChildren().add(polyUnit.getNode());
	Unit<Poly> groundUnit = new Unit<>(new Poly(new Anchor(HALF_WIDTH, HEIGHT),
						    new CVector(0, HALF_HEIGHT),
						    new CVector(WIDTH, HALF_HEIGHT),
						    new CVector(WIDTH, HEIGHT + HALF_HEIGHT),
						    new CVector(0, HEIGHT + HALF_HEIGHT)));
	TimedEvent updater = new TimedEvent(e -> {
		imgUnit.update();
		polyUnit.update();
		groundUnit.update();
	},
	    1);
	imgUnit.getCollidables().add(groundUnit);
	polyUnit.getCollidables().add(groundUnit);
	root.getChildren().add(groundUnit.getNode());
	appUpdater.addTimedEvent(updater);
    }

    public void test2() {
	// this creates the center polygon of the linked unit
	Poly pc1 = new Poly(new Anchor(HALF_WIDTH, (HALF_HEIGHT / 2)),
			    new CVector(HALF_WIDTH - 20, (HALF_HEIGHT / 2) - 20),
			    new CVector(HALF_WIDTH + 20, (HALF_HEIGHT / 2) - 20),
			    new CVector(HALF_WIDTH + 20, (HALF_HEIGHT / 2) + 20),
			    new CVector(HALF_WIDTH - 20, (HALF_HEIGHT / 2) + 20));
	LinkedUnit<Poly> polyCenter1 = new LinkedUnit<>(pc1);
	//enable gravity
	polyCenter1.getPivot().setGrav(true);
	polyCenter1.setKinematics(true);

	// this creates the left protrusion
	Poly pl1 = new Poly(new Anchor(HALF_WIDTH - 20, (HALF_HEIGHT / 2)),
			   new CVector(HALF_WIDTH - 20, (HALF_HEIGHT / 2) - 10),
			   new CVector(HALF_WIDTH - 20, (HALF_HEIGHT / 2) + 10),
			   new CVector(HALF_WIDTH - 45, (HALF_HEIGHT / 2) + 10),
			   new CVector(HALF_WIDTH - 45, (HALF_HEIGHT / 2) - 10));
	LinkedUnit<Poly> polyLeft1 = new LinkedUnit<>(pl1);
	polyCenter1.addSubUnit(polyLeft1);

	// this creates the right protrusion
	Poly pr1 = new Poly(new Anchor(0, 0),
			    new CVector(0, -10),
			    new CVector(0, 10),
			    new CVector(25, 10),
			    new CVector(25, -10));
	LinkedUnit<Poly> polyRight1 = new LinkedUnit<>(pr1);
	polyCenter1.addSubUnit(polyRight1, new CVector(20, 0));

	// this creates the top protrusion
	Poly pt1 = new Poly(new Anchor(0, 0),
			    new CVector(10, 0),
			    new CVector(-10, 0),
			    new CVector(-10, -25),
			    new CVector(10, -25));
	LinkedUnit<Poly> polyTop1 = new LinkedUnit<>(pt1);
	polyCenter1.addSubUnit(polyTop1, new CVector(0, -20));

	// this creates the bottom protrusion
	Poly pb1 = new Poly(new Anchor(0, 0),
			    new CVector(10, 0),
			    new CVector(-10, 0),
			    new CVector(-10, 25),
			    new CVector(10, 25));
	LinkedUnit<Poly> polyBottom1 = new LinkedUnit<>(pb1);
	polyCenter1.addSubUnit(polyBottom1, new CVector(0, 20));

	// this creates the ground
	Unit<Poly> ground = new Unit<>(new Poly(new Anchor(HALF_WIDTH, (HEIGHT * 7 / 8)),
						new CVector(0, HEIGHT * 3 / 4),
						new CVector(WIDTH, HEIGHT * 3 / 4),
						new CVector(WIDTH, HEIGHT),
						new CVector(0, HEIGHT)));
	Unit<Poly> rightWall = new Unit<>(new Poly(new Anchor(WIDTH, HALF_HEIGHT),
						   new CVector(WIDTH - 10, 0),
						   new CVector(WIDTH + 10, 0),
						   new CVector(WIDTH + 10, HEIGHT),
						   new CVector(WIDTH - 10, HEIGHT)));
	Unit<Poly> leftWall = new Unit<>(new Poly(new Anchor(0, HALF_HEIGHT),
						   new CVector(-10, 0),
						   new CVector(10, 0),
						   new CVector(10, HEIGHT),
						   new CVector(-10, HEIGHT)));

	// add the ground to the collision list for the unit
	polyCenter1.getCollidables().add(ground);
	polyCenter1.getCollidables().add(rightWall);
	polyCenter1.getCollidables().add(leftWall);

	// add the linked unit and ground unit to the pane for display
	root.getChildren().add(polyCenter1.getNode());
	root.getChildren().add(ground.getNode());
	root.getChildren().add(rightWall.getNode());
	root.getChildren().add(leftWall.getNode());

	Force spinForce = k -> k.getRotAcceleration().setValue(0.01 + k.getRotAcceleration().getValue());

	// create an updater timed event (every 60 fps is default)
	TimedEvent updater = new TimedEvent(e -> {
		polyCenter1.getPivot().queueForce(spinForce);
		polyCenter1.update();
		ground.update();
		rightWall.update();
		leftWall.update();
	    },
	    1);

	// add it to the app updater
	appUpdater.addTimedEvent(updater);
	
    }

    public void createTests() {
	test2();
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

	if (getParameters().getRaw().contains(OUTPUT_INDICATOR)
	    && !getParameters().getRaw().contains(OUTPUT_FALSE_INDICATOR)) {
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
		    // player1.receiveInput(e);
		}
	    });
	root.setOnKeyReleased(e -> {
		App.print("Input");
		if (e.getCode().equals(KeyCode.SPACE)) {
		    togglePause(false);
		} else {
		    // player1.receiveInput(e);
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
	    output = output.replace("\n", "\n" + OUTPUT_PREFIX);
	    /*for (int i = 0; i < output.length(); i++) {
		if (output.substring(i, i+1).equals("\n")) {
		    output = output.substring(0, i + 1) + OUTPUT_PREFIX + output.substring(i + 1);
		}
		}*/
	    System.out.println(OUTPUT_PREFIX + output);
	    return true;
	}
	return false;
    }

    public void togglePause(boolean pressed) {
	appUpdater.setPaused(pressed);
    }
    
}
