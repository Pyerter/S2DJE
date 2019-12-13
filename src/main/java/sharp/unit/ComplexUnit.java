package sharp.unit;

import sharp.game;
import sharp.utility;
import javafx.group;


public class ComplexUnit  extends Group implements Unit {

    private Projector projection;
    private ArrayList<Polygon> polies;
    private ArrayList<ImageView> images;
    private CVector velocity;
    private CVector acceleration;
    private CVector rotVelocity;
    private CVector rotAcceleration;
    
    public ComplexUnit(boolean hasPolies, boolean hasImages) {
	if (hasPolies) {
	    polies = new ArrayList<>();
	}
	if (hasImages) {
	    images = new ArrayList<>();
	}
    }

    

}
