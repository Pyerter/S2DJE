package sharp.collision;

import sharp.utility.Anchor;

import javafx.scene.Group;

import java.util.List;
import java.util.ArrayList;

public class ProjectionCalculator extends Projection {

    private Group group;
    private Projection[] fullCollider;
    
    public ProjectionCalculator(Anchor pivot, LinkedUnit ... units) {
	setup(pivot);
	int size = 0;
	group = new Group();
	for (LinkedUnit u: units) {
	    size += u.getAllSubUnits().size() + 1;
	}
	fullCollider = new Projection[size];
	for (Projection p: fullCollider) {
	    group.getChildren().add(p.getNode());
	}
    }

    public ProjectionCalculator(List<LinkedUnit> units) {
	this(new Anchor(0, 0), units.stream().toArray(LinkedUnit[]::new));
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

    public static Projection[] getCollider(LinkedUnit u) {
	Projection[] collider;
	int size = 1 + u.getAllSubUnits().size();
	fullCollider = new Projection[size];
    }
    
}
