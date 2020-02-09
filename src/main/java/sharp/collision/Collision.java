package sharp.collision;

import sharp.game.App;
import sharp.utility.CVector;
import sharp.utility.CVectorPair;
import sharp.utility.Transform;
import sharp.utility.Utility;
import sharp.utility.WrappedValue;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/** This class has methods to determine collision between different shapes. */
public class Collision {

    private static int priority = 0;
    private static ArrayList<Collidable> registeredCollidables = new ArrayList<>();
    private static LinkedList<Collidable> continuousUpdaters = new LinkedList<>();

    /**
     * Set the priority of a collidable by placing it at the end of the list.
     * 
     * @param c - the collidable to set priority of
     */
    public static void setPriority(Collidable c) {
	c.setPriority(registeredCollidables.size());
	registeredCollidables.add(c);
    }
    
    /**
     * Set the priority of a collidable by placing it at the beginning of the list
     * and moving everything else down one.
     *
     * @param c - the collidable to set at the start of the list
     */
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

    /**
     * Set the priority of a collidable to the bottom of the list and
     * move the priority of all other units up one.
     *
     * @param c - the collidable to set to the end of the list
     */
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

    /**
     * Add any collidables to the list of collidables which will be added to the
     * continuous update queue to finely transform each one.
     *
     * @param collidables - any collidables to queue
     */
    public static void queueContinuousCollidables(Collidable ... collidables) {
	for (Collidable c: collidables) {
	    if (!registeredCollidables.contains(c)) {
		Collision.setPriority(c);
	    }
	    if (!continuousUpdaters.contains(c)) {
		App.print("Adding " + c + " to fineUpdaters");
		continuousUpdaters.add(c);
	    }
	}
    }

    /**
     * Check if a collidable has already been added to the update queue
     *
     * @param c - the colidable to check with
     * @return true if this collidable is in the list
     */
    public static boolean willContinuousUpdate(Collidable c) {
	return continuousUpdaters.contains(c);
    }

    /**
     * Sort the collidables in a list by comparing by priority.
     * 
     * @param collidables - the list to sort
     */
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

    /**
     * Update any collidables add to continuous updaters by updating them
     * in order of how they were queued. Transforms are applied one at a time
     * for each of the units.
     */
    public static void update() {
	sortCollidables(continuousUpdaters);
	int maxTransforms = 0;
	for (Collidable c: continuousUpdaters) {
	    if (c.getTransforms().size() > maxTransforms) {
		maxTransforms = c.getTransforms().size();
	    }
	}
	App.print("Will sort through at least " + maxTransforms + " transforms");
	for (int i = 0; i < maxTransforms; i++) {
	    App.print("- - - Running continuous update, i=" + i);
	    for (Collidable c: continuousUpdaters) {
		App.print("Requesting continuous udpate " + i + " from " + c.toString());
		c.continuousUpdate(i);
	    }
	}
	for (Collidable c: registeredCollidables) {
	    c.endUpdate();
	}
	continuousUpdaters.clear();
    }

    /**
     * Check collision between two collidables.
     *
     * @param c1 - the first collidable
     * @param c2 - the second collidable
     * @return true if the vector collision point is null
     */
    public static boolean collides(Collidable c1, Collidable c2) {
	return collides(c1, c2, false) != null;
    }

    /**
     * Check collision between two collidables through detecting a point of collision.
     * @param c1 - the first collidable
     * @param c2 - the second collidable
     * @param yes - a throw away parameter that overloads the method to have one collides()
     * that returns the vector point and one that returns a boolean
     * @return return the vector point that any collision was found
     */
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

    /**
     * Check collision by going through two arrays of vectors.
     * 
     * @param p1 - the first array of projections
     * @param p2 - the second array of projections
     * @return any vector points found where any projections collide
     */
    public static CVector collides(Projection[] p1, Projection[] p2) {
	for (Projection p: p1) {
	    CVector collidePoint = collides(p, p2);
	    if (collidePoint != null) {
		return collidePoint;
	    }
	}
	return null;
    }

    /**
     * Check collision between an array of projections and one projection.
     *
     * @param p1 - the array of projections
     * @param p2 - the single projection
     * @return any vector point found between the projections
     */
    public static CVector collides(Projection[] p1, Projection p2) {
	return collides(p2, p1);
    }

    /**
     * Check collision between an array of projections and one projection.
     *
     * @param p1 - the single projection
     * @param p2 - the array of projections
     * @return any vector point found between the projections
     */
    public static CVector collides(Projection p1, Projection[] p2) {
	for (Projection p: p2) {
	    CVector collidePoint = CollisionCalculator.collides(p, p1);
	    if (collidePoint != null) {
		return collidePoint;
	    }
	}
	return null;
    }

    /**
     * Retrieve the closest point on the first collidable from the second collidable.
     * 
     * @param c1 - the collidable to check for a close point from on its outline
     * @param c2 - the collidable to check distances from
     * @return any vector point found between the projections
     */
    public static CVector getClosestPoint(Collidable c1, Collidable c2) {
	return getClosestPoint(c1, null, c2);
    }

    /**
     * Retrieve the closest point from the outlines of the first collidable to
     * any point on the other.
     * 
     * @param c1 - the collidable to check for a close point from on its outline
     * @param pivot - the pivot point to break ties based on nearness to distance (near is better)
     * @param c2 - the collidable to check distances from
     * @return any vector point found between the projections
     */
    public static CVector getClosestPoint(Collidable c1, CVector pivot, Collidable c2) {
	CVector closest = null;
	WrappedValue<Double> closestDist = new WrappedValue<>(new Double(0.0));
	for (Projection p: c2.getCollider()) {
	    closest = getClosestPoint(c1, pivot, p, closest, closestDist);
	}
	return closest;
    }

    /**
     * Retrieve the closest point from the outlines of the first collidable to
     * any point on the other.
     * 
     * @param c1 - the collidable to check for a close point from on its outline
     * @param pivot - the pivot point to break ties based on nearness to distance (near is better)
     * @param c2 - the collidable to check distances from
     * @param closest - the previously found closest point
     * @param closestDist - the wrapped value for the previously found distance for reference of closest point
     * @return any vector point found between the projections
     */
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
     * @param c1 - the collidable to check for a close point from on its outline
     * @param pivot - the pivot point to break ties based on nearness to distance (near is better)
     * @param c2 - the collidable to check distances from
     * @param closest - the previously found closest point
     * @param closestDist - the wrapped value for the previously found distance for reference of closest point
     * @return the closest point on p1 from p2
     */
    public static CVector getClosestPoint(Projection p1, CVector pivot, Projection p2, CVector closest, WrappedValue<Double> closestDist) {
	for (CVector v: p1.getOutline()) {
	    WrappedValue<Double> newDist = new WrappedValue<Double>(CollisionCalculator.getDistanceFromPoint(v, p2));
	    if (closest == null) {
		closestDist.setValue(newDist.getValue());
		closest = v;
	    } else if (Utility.isAbout(newDist.getValue(), closestDist.getValue(), 0.0001)) {
		double oldDistFromPivot = CVector.subtract(closest, pivot).getMag();
		double newDistFromPivot = CVector.subtract(v, pivot).getMag();
		if (newDistFromPivot < oldDistFromPivot) {
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
     * @param c - the collidable to check the distance from
     * @return the distance
     */
    public static Double getDistanceFromPoint(CVector v, Collidable c) {
	return CollisionCalculator.getDistanceFromPoint(v, c);
    }
    
}
