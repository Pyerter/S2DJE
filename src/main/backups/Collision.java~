package sharp.collision;

import sharp.game.App;
import sharp.utility.CVector;
import sharp.utility.CVectorPair;
import sharp.utility.Transform;
import sharp.utility.Utility;
import sharp.utility.WrappedValue;
import sharp.unit.HingedUnit;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/** This class has methods to determine collision between different shapes. */
public class Collision {

    private static int priority = 0;
    private static ArrayList<Collidable> registeredCollidables = new ArrayList<>();
    private static LinkedList<Collidable> continuousUpdaters = new LinkedList<>();

    public static void setPriority(Collidable c) {
	c.setPriority(registeredCollidables.size());
	registeredCollidables.add(c);
    }

    public static void setTopPriority(Collidable c) {
	c.setPriority(0);
	for (Collidable coll: registeredCollidables) {
	    if (coll != c) {
		coll.setPriority(coll.getPriority() + 1);
	    }
	}
	if (!registeredCollidables.contains(c)) {
	    registeredCollidables.add(c);
	}
    }

    public static void setLowPriority(Collidable c) {
	if (!registeredCollidables.contains(c)) {
	    setPriority(c);
	    return;
	}
	for (Collidable coll: registeredCollidables) {
	    if (coll.getPriority() > c.getPriority()) {
		coll.setPriority(coll.getPriority() - 1);
	    }
	}
	c.setPriority(registeredCollidables.size());
	
    }
    
    public static void addContinuousCollidables(Collidable ... collidables) {
	for (Collidable c: collidables) {
	    if (!registeredCollidables.contains(c)) {
		Collision.setPriority(c);
	    }
	    if (!fineUpdaters.contains(c)) {
		App.print("Adding " + c + " to fineUpdaters");
		continuousUpdaters.add(c);
	    }
	}
    }

    public static boolean willContinuousUpdate(Collidable c) {
	/*App.print("Checking for collidable: " + c + ". Collidables contains: ");
	for (Collidable coll: continuousUpdaters) {
	    App.print(coll.toString());
	    }*/
	return continuousUpdaters.contains(c);
    }

    public static void sortCollidables(List<Collidable> collidables) {
	if (collidables.size() <= 1) {
	    return;
	}
	App.print("Sorting fineUpdaters");
	for (int i = 0; i < collidables.size() - 1; i++) {
	    int min = i;
	    for (int j = i + 1; j < collidables.size(); j++) {
		if (collidables.get(j).getPriority() < collidables.get(min).getPriority()) {
		    min = j;
		}
	    }
	    if (min == i) {
		continue;
	    }
	    Collidable temp = collidables.get(i);
	    collidables.set(i, collidables.get(min));
	    collidables.set(min, temp);
	}
    }

    public static void update() {
	// do some fancy crap to finely update all the fineUpdaters
	sortCollidables(continuousUpdaters);
	int maxTransforms = 0;
	for (Collidable c: continuousUpdaters) {
	    /*App.print("Fine updater included: " + c);
	    String allTs = "";
	    for (Transform t: c.getTransforms()) {
		allTs += t + "      ";
	    }
	    App.print("Transforms under this unit: " + allTs);*/
	    if (c.getTransforms().size() > maxTransforms) {
		maxTransforms = c.getTransforms().size();
	    }
	}
	App.print("Will sort through at least " + maxTransforms + " transforms");
	for (int i = 0; i < maxTransforms; i++) {
	    App.print("Running continuous update, i=" + i);
	    for (Collidable c: continuousUpdaters) {
		App.print("Requesting continuous udpate " + i + " from " + c.toString());
		c.continuousUpdate(i);
		/*if (c.getTransforms().size() > i) {
		    App.print("Finely updating: " + c);
		    c.applyFineTransform(c.getTransforms().get(i));
		    }*/
	    }
	}
	for (Collidable c: registeredCollidables) {
	    c.endUpdate();
	}
	continuousUpdaters.clear();
    }

    public static boolean collides(Collidable c1, Collidable c2) {
	return collides(c1, c2, false) != null;
    }
    
    public static CVector collides(Collidable c1, Collidable c2, boolean yes) {
	App.print("\nChecking collision between " + c1 + " and " + c2);
	CVector collisionPoint = Collision.collides(c1.getCollider(), c2.getCollider());
	if (collisionPoint != null) {
	    App.print("The two Collidables have collided!\n");
	} else {
	    App.print("No collision detected.\n");
	}
	return collisionPoint;
    }

    public static CVector collides(Projection[] p1, Projection[] p2) {
	for (Projection p: p1) {
	    CVector collidePoint = collides(p, p2);
	    if (collidePoint != null) {
		return collidePoint;
	    }
	}
	return null;
    }

    public static CVector collides(Projection[] p1, Projection p2) {
	return collides(p2, p1);
    }

    public static CVector collides(Projection p1, Projection[] p2) {
	for (Projection p: p2) {
	    CVector collidePoint = CollisionCalculator.collides(p, p1);
	    if (collidePoint != null) {
		return collidePoint;
	    }
	}
	return null;
    }
    
    public static CVector getClosestPoint(Collidable c1, Collidable c2) {
	return getClosestPoint(c1, null, c2);
    }

    public static CVector getClosestPoint(Collidable c1, CVector pivot, Collidable c2) {
	CVector closest = null;
	WrappedValue<Double> closestDist = new WrappedValue<>(new Double(0.0));
	for (Projection p: c2.getCollider()) {
	    closest = getClosestPoint(c1, pivot, p, closest, closestDist);
	}
	return closest;
    }

    public static CVector getClosestPoint(Collidable c1, CVector pivot, Projection p1, CVector closest, WrappedValue<Double> closestDist) {
	for (Projection p: c1.getCollider()) {
	    closest = getClosestPoint(p, pivot, p1, closest, closestDist);
	}
	return closest;
    }
    
    /**
     * This method returns the closest point in the outline of p1 from the
     * outline of p2.
     *
     * @param p1 - the first projection
     * @param p2 - the second projection
     * @return the closest point on p1 from p2
     */
    public static CVector getClosestPoint(Projection p1, CVector pivot, Projection p2, CVector closest, WrappedValue<Double> closestDist) {
	for (CVector v: p1.getOutline()) {
	    WrappedValue<Double> newDist = CollisionCalculator.getDistanceFromPoint(v, p2);
	    if (closest == null) {
		closestDist.setValue(newDist.getValue());
		closest = v;
	    } else if (Utility.isAbout(newDist.getValue(), closestDist.getValue(), 0.0001)) {
		double oldDistFromPivot = CVector.subtract(closest, pivot).getMag();
		double newDistFromPivot = CVector.subtract(v, pivot).getMag();
		if (newDistFromPivot > oldDistFromPivot) {
		    closestDist.setValue(newDist.getValue());
		    closest = v;
		    continue;
		}
	    }
	    if (newDist.getValue() < closestDist.getValue()) {
		closestDist.setValue(newDist.getValue());
		closest = v;
	    }
	}
	return closest;
    }

    /**
     * This method is used to find the minimum distance from a point to a collidable.
     *
     * @param v - if this point comes from a unit/collidable, pass it in as CVector.subtract(v, getPivot())
     * @param c -
     * @return the distance
     */
    public static Double getDistanceFromPoint(CVector v, Collidable c) {
	return CollisionCalculator.getDistanceFromPoint(v, c);
    }
    
}
