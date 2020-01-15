package sharp.collision;

import sharp.utility.Anchor;

import javafx.scene.Group;

import java.util.List;
import java.util.ArrayList;

public class ProjectionCalculator extends Projection {

    private Group group;
    private Projection[] fullCollider;
n    
    public ProjectionCalculator(Anchor pivot, LinkedUnit ... units) {
	setup(pivot);
	group = new Group();
	calculateCollider(units);
    }

    public ProjectionCalculator(List<LinkedUnit> units) {
	this(new Anchor(0, 0), units.stream().toArray(LinkedUnit[]::new));
    }

    public void calculateCollider(LinkedUnit ... units) {
	group.getChildren().clear();
	fullCollider = getCollider(false, units);
	for (Projection p: fullCollider) {
	    group.getChildren().add(p.getNode());
	}
    }

    public void calculateCollider(List<LinkedUnit> units) {
	calculateCollider(units.stream().toArray(LinkedUnit[]::units));
    }

    public List<CVector> getOutline() {
	App.print("Requesting outline of ProjectionCalculator, returning empty outline.");
	return super.getOutline();
    }

    public Projection[] getCollider() {
	return fullCollider;
    }

    public Group getNode() {
	return group;
    }

    public static Projection[] getCollider(boolean show, LinkedUnit ... units) {
	LinkedList<LinkedUnit> listUnits = new LinkedList<>();
	for (LinkedUnit u: units) {
	    if (show || u.getShow()) {
		listUnits.add(u);
	    }
	    for (LinkedUnit su: u.getAllSubUnits()) {
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
