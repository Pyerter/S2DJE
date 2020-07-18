package sharp.unit;

import sharp.game.App;
import sharp.utility.Transform;
import sharp.utility.Translatable;
import sharp.utility.CVector;
import sharp.utility.Anchor;
import sharp.collision.Collidable;
import sharp.collision.Projection;
import sharp.collision.Collision;

import java.util.LinkedList;
import java.util.ArrayList;

import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeLineCap;

/**
 * This class represents a projection that is created by using a series of point vectors
 * used in pair with the javafx {@code javafx.scene.shape.Polygon} class to display this projection.
 */
public class Poly extends Projection {

    private Polygon p;

    /**
     * Creates a {@code Poly} given an anchor and a series of points to create the outline.
     *
     * @param pivot - this point is used as a pivot point when rotating the poly
     * @param points - a varargs list of points used to create the polygon
     */
    public Poly(Anchor pivot, CVector ... points) {
	setup(pivot, points);
	p = new Polygon();
    }

    /**
     * Sets true or false if the polygon will be rounded at the vertices.
     */
    public void round(boolean rnd) {
	if (rnd) {
	    p.setStrokeLineCap(StrokeLineCap.ROUND);
	} else {
	    p.setStrokeLineCap(StrokeLineCap.BUTT);
	}
    }

    /**
     * Runs the update method to apply any actions necessary.
     *
     * @return 0 if successful, a negative number otherwise
     */
    public int update() {
	return super.update();
    }

    /**
     * Run any end update methods to correct anything that moved.
     * This {@code Poly} specific update method updates all points on the
     * Polygon object to update the display to match the vertices.
     */
    public void endUpdate() {
	adjustPoly(true);
	super.endUpdate();
    }

    /**
     * Retrieve the {@code Polygon} object to use when displaying the object.
     *
     * @return the Polygon object
     */
    public Polygon getNode() {
	return p;
    }

    /**
     * Corrects the points on the Polygon object to match the size of the outline list in this object.
     */
    public void correctPoly() {
	while (p.getPoints().size() != getOutline().size() * 2) {
	    if (p.getPoints().size() < getOutline().size() * 2) {
		p.getPoints().add(0.0);
	    } else if (p.getPoints().size() > getOutline().size() * 2) {
		p.getPoints().remove(p.getPoints().size() - 1);
	    }
	}
    }

    /**
     * Adjusts the points on the Polygon object to match the coordinate vectors in this object's list.
     */
    public void adjustPoly() {
	for (int i = 0; i < p.getPoints().size(); i += 2) {
	    CVector point = getOutline().get(i / 2);
	    p.getPoints().set(i, point.getX());
	    p.getPoints().set(i + 1, point.getY());
	}
    }

    /**
     * Adjusts the polygon with the option to also correct the amount of coordinate vectors used in case
     * the outline list was changed.
     *
     * @param checkSize - true if the polygon's number of points should be changed
     */
    public void adjustPoly(boolean checkSize) {
	if (checkSize) {
	    correctPoly();
	}
	adjustPoly();
    }

    /**
     * Returns a string to represent this object in the following format: "Poly: Vertices(# of vertices)"
     *
     * @return the string representation
     */
    public String toString() {
	return "Poly: Vertices(" + getOutline().size() + ")";
    }
    
}
