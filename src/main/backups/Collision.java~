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
    private static LinkedList<Collidable> fineUpdaters = new LinkedList<>();

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
    
    public static void addFineColliders(Collidable ... collidables) {
	for (Collidable c: collidables) {
	    if (!registeredCollidables.contains(c)) {
		Collision.setPriority(c);
	    }
	    if (!fineUpdaters.contains(c)) {
		App.print("Adding " + c + " to fineUpdaters");
		fineUpdaters.add(c);
	    }
	}
    }

    public static boolean willFineUpdate(Collidable c) {
	App.print("Checking for collidable: " + c + ". Collidables contains: ");
	for (Collidable coll: fineUpdaters) {
	    App.print(coll.toString());
	}
	return fineUpdaters.contains(c);
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
	sortCollidables(fineUpdaters);
	int maxTransforms = 0;
	for (Collidable c: fineUpdaters) {
	    App.print("Fine updater included: " + c);
	    String allTs = "";
	    for (Transform t: c.getTransforms()) {
		allTs += t + "      ";
	    }
	    App.print("Transforms under this unit: " + allTs);
	    if (c.getTransforms().size() > maxTransforms) {
		maxTransforms = c.getTransforms().size();
	    }
	}
	App.print("Will sort through at least " + maxTransforms + " transforms");
	for (int i = 0; i < maxTransforms; i++) {
	    for (Collidable c: fineUpdaters) {
		App.print("Checking fine update for " + c);
		if (c.getTransforms().size() > i) {
		    App.print("Finely updating: " + c);
		    c.applyFineTransform(c.getTransforms().get(i));
		}
	    }
	}
	for (Collidable c: fineUpdaters) {
	    c.endUpdate();
	}
	fineUpdaters.clear();
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

    /*public static boolean collides(Projection p1, Projection p2) {
	CVector diff = CVector.subtract(p1.getPivot(), p2.getPivot());
	if (p1.getCollisionRadius() + p2.getCollisionRadius() > diff.getMag()) {
	    return false;
	}
	return Collision.collides(p1, p2);
	}*/


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
	    CVector collidePoint = collides(p, p1);
	    if (collidePoint != null) {
		return collidePoint;
	    }
	}
	return null;
    }
    
    /**
     * This method takes two polygons and splits up their parts
     * and checks with other methods to see if the two collide.
     *
     * @param p1 - the first polygon to check with
     * @param p2 - the second polygon to check with
     * @return true if the two overlap at all
     */
    public static CVector collides(Projection p1, Projection p2) {
	App.print("Checking collision (with projections) between " + p1 + " and " + p2);
	CVector diff = CVector.subtract(p1.getPivot(), p2.getPivot());
	if (p1.getCollisionRadius() + p2.getCollisionRadius() < diff.getMag()) {
	    App.print("The distances were too far. They don't collide.");
	    return null;
	}

	int end = 0;
	for (int start = 0; start < p1.getOutline().size(); start++) {
	    end = start + 1;
	    if (end == p1.getOutline().size()) {
		end = 0;
	    }

	    CVectorPair vp = new CVectorPair(p1.getOutline().get(start), p1.getOutline().get(end));

            CVector collidePoint = collides(p2, vp);
            if (collidePoint != null) {
                return collidePoint;
            }
        }

	for (int start = 0; start < p1.getOutline().size(); start++) {
	    CVector collidePoint = collides(p2, p1.getOutline().get(start));
            if (collidePoint != null) {
                return collidePoint;
            }
        }

	for (int start = 0; start < p2.getOutline().size(); start++) {
	    CVector collidePoint = collides(p1, p2.getOutline().get(start));
            if (collidePoint != null) {
                return collidePoint;
            }
        }
	
        return null;
    }

    /**
     * This method takes a polygon and a line and checks if any of the polygon's
     * lines collide with the specified line.
     *
     * @param p - the polygon to check with
     * @param l1 - the line to check with
     * @return true if any parts collided
     */
    public static CVector collides(Projection p, CVectorPair vp2) {
        int end = 0;
        for (int start = 0; start < p.getOutline().size(); start++) {
            end = start + 1;
            if (end == p.getOutline().size()) {
                end = 0;
            }

	    CVectorPair vp1 = new CVectorPair(p.getOutline().get(start), p.getOutline().get(end));

            CVector collidePoint = collides(vp1, vp2);
            if (collidePoint != null) {
                return collidePoint;
            }
        }

        return null;
    }

    /**
     * This method takes two lines and uses a special formula to check
     * if the two lines overlap at all.
     *
     * @param l1 - the first line to check with
     * @param l2 - the second line to check with
     * @return true if the lines overlap
     */
    public static CVector collides(CVectorPair p1, CVectorPair p2) {
        // This equation is found to be similar as the divider, so it's only calculated once
        double divider = ((p2.getEndY() - p2.getStartY()) * (p1.getEndX() - p1.getStartX()) -
                         (p2.getEndX() - p2.getStartX()) * (p1.getEndY() - p1.getStartY()));

        double uA = ((p2.getEndX() - p2.getStartX()) * (p1.getStartY() - p2.getStartY()) -
                    (p2.getEndY() - p2.getStartY()) * (p1.getStartX() - p2.getStartX())) /
            divider;

        double uB = ((p1.getEndX() - p1.getStartX()) * (p1.getStartY() - p2.getStartY()) -
                    (p1.getEndY() - p1.getStartY()) * (p1.getStartX() - p2.getStartX())) /
            divider;


        if (uA >= 0 && uA <= 1 && uB >= 0 && uB <= 1) {
	    double intersectionX = p1.getStartX() + (uA * (p1.getEndX() - p1.getStartX()));
	    double intersectionY = p1.getStartY() + (uA * (p1.getEndY() - p1.getStartY()));
	    return new CVector(intersectionX, intersectionY);
        }
        return null;
    }

    /**
     * This method takes a point and a polygon and checks if the point
     * is inside of the polygon at all.
     *
     * @param p - the polygon to check inside of
     * @param v - the point to check inside the polygon
     * @return true if the point is inside the polygon
     */
    public static CVector collides(Projection p, CVector v) {
        boolean doesCollide = false;

	// App.print("Checking poly/point collisions with " + p + " and " + v);

        int end = 0;
        for (int start = 0; start < p.getOutline().size(); start++) {
            end = start + 1;
            if (end == p.getOutline().size()) {
                end = 0;
            }

	    CVectorPair vp = new CVectorPair(p.getOutline().get(start), p.getOutline().get(end));
	    
            if (((vp.getStartY() >= v.getY()) != (vp.getEndY() > v.getY())) &&
                (v.getX() < (vp.getEndX() - vp.getStartX()) *
                 (v.getY() - vp.getStartY()) /
                 (vp.getEndY() - vp.getStartY()) +
                  vp.getStartX())) {
                doesCollide = !doesCollide;
            }

	    // below is the same thing un-simplified

	    /* if (((vp.getStartY() >= v.getY() && vp.getEndY() < v.getY()) || (vp.getStartY() < v.getY() && vp.getEndY() >= v.getY())) &&
		(v.getX() < (vp.getEndX() - vp.getStartX()) * (v.getY() - vp.getStartY()) / (vp.getEndY() - vp.getStartY()) + vp.getStartX())) {
		App.print("Flipping does collide");
		doesCollide = !doesCollide;
		}*/
        }

	if (doesCollide) {
	    return new CVector(v);
	}
        return null;
    }

    public static CVector getClosestPoint(HingedUnit c1, boolean clockwise, Collidable c2) {
	CVector closest = null;
	WrappedValue<Double> closestDist = new WrappedValue<>(null);
	List<Projection> unitProjections = new LinkedList<>();
	for (Projection p: c1.getCollider()) {
	    unitProjections.add(p);
	}
	CVector pivotRef = c1.getRootUnit().getProjection().getPivot();
	for (HingedUnit u: c1.getAllChildHinges()) {
	    for (Projection p: u.getCollider()) {
		unitProjections.add(p);
	    }
	}
	for (Projection p2: c2.getCollider()) {
	    for (Projection p1: unitProjections) {
		for (CVector v: p1.getOutline()) {
		    if (closest == null) {
			closest = v;
			closestDist.setValue(getDistanceFromPoint(v, p2, new WrappedValue<Double>(null)).getValue());
			continue;
		    }
		    WrappedValue<Double> newDist = getDistanceFromPoint(v, p2, new WrappedValue<Double>(null));
		    if (Utility.isAbout(newDist.getValue(), closestDist.getValue(), 0.1)) {
			CVector temp = CVector.subtract(pivotRef, closest);
			CVector temp2 = CVector.subtract(pivotRef, v);
			double tempMag = temp.getMag();
			double tempMag2 = temp2.getMag();
			if (Utility.isAbout(tempMag, tempMag2, 0.01)) {
			    double angleDiff = temp2.heading() - temp.heading();
			    if ((clockwise && angleDiff > 0) || (!clockwise && angleDiff < 0)) {
				closest = v;
				closestDist.setValue(newDist.getValue());
			    }
			} else if (tempMag2 > tempMag) {
			    closest = v;
			    closestDist.setValue(newDist.getValue());
			}
		    } else if (newDist.getValue() < closestDist.getValue()) {
			closest = v;
			closestDist.setValue(newDist.getValue());
		    }
		}
	    }
	}
	return closest;
    }
    
    public static CVector getClosestPoint(Collidable c1, CVector pivotRef, Collidable c2) {
	CVector closest = null;
	WrappedValue<Double> closestDist = new WrappedValue<>(null);
	for (Projection p2: c2.getCollider()) {
	    for (Projection p1: c1.getCollider()) {
		for (CVector v: p1.getOutline()) {
		    if (closest == null) {
			closest = v;
			closestDist.setValue(getDistanceFromPoint(v, p2, new WrappedValue<Double>(null)).getValue());
			continue;
		    }
		    WrappedValue<Double> newDist = getDistanceFromPoint(v, p2, new WrappedValue<Double>(null));
		    if (Utility.isAbout(newDist.getValue(), closestDist.getValue(), 0.1)) {
			CVector temp = CVector.subtract(pivotRef, closest);
			CVector temp2 = CVector.subtract(pivotRef, v);
			if (temp2.getMag() < temp.getMag()) {
			    closest = v;
			    closestDist.setValue(newDist.getValue());
			}
		    } else if (newDist.getValue() > closestDist.getValue()) {
			closest = v;
			closestDist.setValue(newDist.getValue());
		    }
		}
	    }
	}
	return closest;
    }

    public static CVector getClosestPoint(Collidable c1, Collidable c2) {
	CVector closest = null;
	WrappedValue<Double> closestDist = new WrappedValue<>(new Double(0.0));
	for (Projection p: c2.getCollider()) {
	    closest = getClosestPoint(c1, p, closest, closestDist);
	}
	// System.out.println("Closest point: " + closest + " is " + closestDist.getValue() + " away");
	return closest;
    }

    public static CVector getClosestPoint(Collidable c1, Projection p1, CVector closest, WrappedValue<Double> closestDist) {
	for (Projection p: c1.getCollider()) {
	    closest = getClosestPoint(p, p1, closest, closestDist);
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
    public static CVector getClosestPoint(Projection p1, Projection p2, CVector closest, WrappedValue<Double> closestDist) {
	int end = 0;
	for (int i = 0; i < p2.getOutline().size(); i++) {
	    end = i + 1;
	    if (end == p2.getOutline().size()) {
		end = 0;
	    }
	    CVectorPair diff = new CVectorPair(p2.getOutline().get(i), p2.getOutline().get(end));
	    double lengthSq = Math.pow(diff.getLength(), 2);
	    
	    for (CVector cv: p1.getOutline()) {
		CVector v = CVector.subtract(cv, p1.getPivot());
		double check = -1;
		double dotProduct = CVector.dot(v, CVector.subtract(diff.getV2(), diff.getV1()));
		if (lengthSq != 0) {
		    check = dotProduct / lengthSq;
		}

		CVector dist = new CVector();
		if (check < 0) {
		    dist.setX(diff.getStartX());
		    dist.setY(diff.getStartY());
		} else if (check > 1) {
		    dist.setX(diff.getEndX());
		    dist.setX(diff.getEndY());
		} else {
		    dist.setX(diff.getStartX() + (check * (diff.getEndX() - diff.getStartX())));
		    dist.setY(diff.getStartY() + (check * (diff.getEndY() - diff.getStartY())));
		}

		dist.setX(v.getX() - dist.getX());
		dist.setY(v.getY() - dist.getY());

		double distance = dist.getMag();
		if (closest == null) {
		    closest = cv;
		    closestDist.setValue(distance);
		} else if (distance < closestDist.getValue()) {
		    closest = cv;
		    closestDist.setValue(distance);
		} else if (Utility.isAbout(distance, closestDist.getValue(), 0.00001)) {
		    double dist1 = CVector.subtract(cv, p1.getPivot()).getMag();
		    double dist2 = CVector.subtract(closest, p1.getPivot()).getMag();
		    if (dist1 < dist2) {
			closest = cv;
			closestDist.setValue(distance);
		    }
		}
	    }
	}

	if (closest != null) {
	    closest = new CVector(closest);
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
	WrappedValue<Double> closestDistance = new WrappedValue<>(null);
	for (Projection p: c.getCollider()) {
	    closestDistance = getDistanceFromPoint(v, p, closestDistance);
	}
	return closestDistance.getValue();
    }

    public static WrappedValue<Double> getDistanceFromPoint(CVector v, Projection p, WrappedValue<Double> closestDist) {
	int end = 0;
	for (int i = 0; i < p.getOutline().size(); i++) {
	    end = i + 1;
	    if (end == p.getOutline().size()) {
		end = 0;
	    }
	    CVectorPair diff = new CVectorPair(p.getOutline().get(i), p.getOutline().get(end));
	    double lengthSq = Math.pow(diff.getLength(), 2);
	    
	    
	    double check = -1;
	    double dotProduct = CVector.dot(v, CVector.subtract(diff.getV2(), diff.getV1()));
	    if (lengthSq != 0) {
		check = dotProduct / lengthSq;
	    }

	    CVector dist = new CVector();
	    if (check < 0) {
		dist.setX(diff.getStartX());
		dist.setY(diff.getStartY());
	    } else if (check > 1) {
		dist.setX(diff.getEndX());
		dist.setX(diff.getEndY());
	    } else {
		dist.setX(diff.getStartX() + (check * (diff.getEndX() - diff.getStartX())));
		dist.setY(diff.getStartY() + (check * (diff.getEndY() - diff.getStartY())));
	    }

	    dist.setX(v.getX() - dist.getX());
	    dist.setY(v.getY() - dist.getY());

	    double distance = dist.getMag();
	    if (closestDist.getValue() == null) {
		closestDist.setValue(distance);
	    } else if (distance < closestDist.getValue()) {
		closestDist.setValue(distance);
	    }
	}

	return closestDist;
    }
    
}
