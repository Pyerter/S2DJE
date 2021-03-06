package sharp.game;

import javafx.application.Application;

/** This class launches the main javafx application. */
public class Driver {

    /**
     * Main method of the driver. Takes in args (really don't matter).
     * Run the main game javafx app.
     *
     * @param args - any arguments for the app
     */
    public static void main(String[] args) {
	try {
	    Application.launch(App.class, args);
	} catch (UnsupportedOperationException e) {
	    System.out.println("Uh oh!: " + e.getMessage());
	}
    }

}
