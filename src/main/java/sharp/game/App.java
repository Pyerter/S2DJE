package sharp.game;

import sharp.utility.CVector;
import sharp.unit.ComplexUnit;

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
    
    private Stage stage;
    private Scene baseScene;
    private Pane root;

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

    private void createDraft() {
	ComplexUnit player = new ComplexUnit(true, false);
	Polygon poly = new Polygon();
	for (int i = 0; i < 12; i++) {
	    poly.getPoints().addAll(20 * Math.cos(2 * Math.PI * (i / 12.0)),
				    20 * Math.sin(2 * Math.PI * (i / 12.0)));
	}
	poly.setFill(Color.BLACK);
	System.out.println("Added player poly: " + player.addPoly(poly));
	root.getChildren().add(player);
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

	createDraft();
	
	stage.setTitle("Sharp");
	stage.setScene(baseScene);
	stage.sizeToScene();
	stage.setResizable(false);
	stage.show();
    }
    
}
