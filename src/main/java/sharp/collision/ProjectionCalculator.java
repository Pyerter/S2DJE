package sharp.collision;

import sharp.utility.Anchor;
import sharp.utility.CVector;
import sharp.unit.LinkedUnit;
import sharp.game.App;

import javafx.scene.Group;

import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

public class ProjectionCalculator extends Projection {

    private Group group;
    private Projection[] fullCollider;
    
    public ProjectionCalculator(Anchor pivot, LinkedUnit<? extends Projection> ... units) {
	setup(pivot);
	group = new Group();
	calculateCollider(units);
    }

    public ProjectionCalculator(List<LinkedUnit<? extends Projection>> units) {
	this(new Anchor(0, 0), units.stream().toArray(LinkedUnit[]::new));
    }

    public void calculateCollider(LinkedUnit<? extends Projection> ... units) {
	group.getChildren().clear();
	fullCollider = getCollider(false, units);
	for (Projection p: fullCollider) {
	    group.getChildren().add(p.getNode());
	}
    }

    public void calculateCollider(List<LinkedUnit<? extends Projection>> units) {
	calculateCollider(units.stream().toArray(LinkedUnit[]::new));
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

    public static Projection[] getCollider(boolean show, LinkedUnit<? extends Projection> ... units) {
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
