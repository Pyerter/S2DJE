package sharp.unit;

import javafx.scene.shape.Polygon;

public class SimpleUnit extends Projection implements Unit {

    public SimpleUnit() {
	super();
    }

    public Projector getProjector() {
	return this;
    }
    
}
