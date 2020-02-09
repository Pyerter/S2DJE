package sharp.collision;

import sharp.utility.Anchor;
import sharp.utility.CVector;
import sharp.unit.LinkedUnit;
import sharp.game.App;

import javafx.scene.Group;
import javafx.scene.Node;

import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.function.IntFunction;

/** 
 * This class extends Projection and is used to create a {@code Projection[]}
 * containing all projections extending from a single {@code LinkedUnit<? extends Projection>}.
 */
public class ProjectionCalculator extends Projection {

    private Group group;
    private Projection[] fullCollider;

    /**
     * Contructor used with a center value.
     * @param pivot - the center point of the projection passed as {@code CVector}
     */
    public ProjectionCalculator(CVector pivot) {
	setup(new Anchor(pivot));
	group = new Group();
    }
    
    /**
     * Contructor used with a center value and an array param of units to account for.
     * @param pivot - the center point of the projection passed as {@code CVector}
     * @param units - a varargs parameter of {@code LinkedUnit<? extends Projection>}
     */
    public ProjectionCalculator(CVector pivot, LinkedUnit<? extends Projection>[] units) {
	setup(new Anchor(pivot));
	group = new Group();
	calculateCollider(units);
    }

    /**
     * Constructor used with a list param of units to account for and placed as
     * starting position (0, 0).
     * @param units - a varargs parameter of {@code LinkedUnit<? extends Projection>}
     */
    public ProjectionCalculator(List<LinkedUnit<? extends Projection>> units) {
	setup(new Anchor(0, 0));
	group = new Group();
	calculateCollider(units);
    }

    /**
     * This method is used to create the array of projections and store each
     * projection's {@code Node} into the {@code Group} this projection uses
     * to display any {@code Node}s.
     * @param units - an array parameter of {@code LinkedUnit<? extends Projection>}
     * to add to this projection
     */
    public void calculateCollider(LinkedUnit<? extends Projection>[] units) {
	group.getChildren().clear();
	fullCollider = getCollider(false, units);
	for (Projection p: fullCollider) {
	    group.getChildren().add(p.getNode());
	}
    }

    /**
     * This method is the same as {@code calculateCollider(LinkedUnit<? extends Projection> ... units)} but takes in a list and converts it to an array.
     * @param a {@code List<LinkedUnit<? extends Projection>>} to add to this projection
     */
    public void calculateCollider(List<LinkedUnit<? extends Projection>> units) {
	group.getChildren().clear();
	fullCollider = getCollider(false, units);
	for (Projection p: fullCollider) {
	    group.getChildren().add(p.getNode());
	}
    }

    /**
     * Getter. Usually NOT used.
     * @return likely a {@code null} value.
     */
    public List<CVector> getOutline() {
	App.print("Requesting outline of ProjectionCalculator, returning empty outline.");
	return super.getOutline();
    }

    /**
     * Getter.
     * Get the {@code Projection[]} containing each projection this object as calculated.
     * @return the array of projections
     */
    public Projection[] getCollider() {
	return fullCollider;
    }

    /**
     * Getter.
     * Returns the group this object uses to store all calculated projections' nodes.
     * @reutrn a {@code Group} node of projections' {@code Node}s
     */
    public Group getNode() {
	return group;
    }

    /**
     * This method will take in a varargs array of units to add into a projection
     * array for this object. If the units are labeled as showing or show is set
     * to true, then they will be added to the list.
     * @param show - if the unit is force added to the list
     * @param untis - the array of units
     * @return the {@code Projection[]} containing all added units
     */
    public static Projection[] getCollider(boolean show, LinkedUnit<? extends Projection>[] units) {
	LinkedList<LinkedUnit<? extends Projection>> listUnits = new LinkedList<>();
	for (LinkedUnit<? extends Projection> u: units) {
	    if (show || u.getShow()) {
		listUnits.add(u);
	    }
	    for (LinkedUnit<? extends Projection> su: u.getAllSubUnits()) {
		if (show || su.getShow()) {
		    listUnits.add(su);
		}
	    }
	}
	return listUnits.stream()
	    .map(e -> e.getProjection())
	    .toArray(Projection[]::new);
    }
    
    /**
     * This method will take in a varargs array of units to add into a projection
     * array for this object. If the units are labeled as showing or show is set
     * to true, then they will be added to the list.
     * @param show - if the unit is force added to the list
     * @param untis - the array of units
     * @return the {@code Projection[]} containing all added units
     */
    public static Projection[] getCollider(boolean show, List<LinkedUnit<? extends Projection>> units) {
	LinkedList<LinkedUnit<? extends Projection>> listUnits = new LinkedList<>();
	for (LinkedUnit<? extends Projection> u: units) {
	    if (show || u.getShow()) {
		listUnits.add(u);
	    }
	    for (LinkedUnit<? extends Projection> su: u.getAllSubUnits()) {
		if (show || su.getShow()) {
		    listUnits.add(su);
		}
	    }
	}
	return listUnits.stream()
	    .map(e -> e.getProjection())
	    .toArray(Projection[]::new);
    }
    
}
