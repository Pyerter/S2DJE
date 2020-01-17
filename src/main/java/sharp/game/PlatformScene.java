package sharp.game;

import sharp.unit.Unit;
import sharp.unit.Terrain;
import sharp.utility.Updatable;

import java.util.ArrayList;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public class PlatformScene extends Scene implements Updatable {

    private ArrayList<Terrain> terrain = new ArrayList<>();
    private ArrayList<Updatable> updatables = new ArrayList<>();
    private Pane root;
    
    public PlatformScene(Pane root) {
	super(root);

	this.root = root;
    }

    public int update() {
	for (Updatable u: updatables) {
	    u.update();
	}
	return 0;
    }

}
