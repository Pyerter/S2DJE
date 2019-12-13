package sharp.game;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    public static final Double WIDTH = 1280;
    public static final Double HEIGHT = 720;
    public static final Double HALF_WIDTH = 640;
    public static final Double HALF_HEIGHT = 360;
    public static final CVector scalar = new CVector(1.0, 1.0);
    public static final CVector halfScalar = new CVector(0.5, 0.5);
    private Stage stage;

    public void setScreen(double width, double height) {
	scalar.setX(width / WIDTH);
	scalar.setY(height / HEIGHT);
	halfScalar.setX(scalar.getX() / 2);
	halfScalar.setY(scalar.getY() / 2);
    }
    
    public void start(Stage stage) {
	this.stage = stage;

	
	// do other cool things
    }

}
