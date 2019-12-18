package sharp.collision;

import sharp.utility.CVector;
import sharp.utility.CVectorPair;

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
		fineUpdaters.add(c);
	    }
	}
    }

    public static boolean willFineUpdate(Collidable c) {
	return fineUpdaters.contains(c);
    }

    public static void sortCollidables(List<Collidable> collidables) {
	// sort the collidables by priority values
    }

    public static void update() {
	// do some fancy crap to finely update all the fineUpdaters
    }
    
    public static boolean collides(Collidable c1, Collidable c2) {
	return Collision.collides(c1.getCollider(), c2.getCollider());
    }

    /*public static boolean collides(Projection p1, Projection p2) {
	CVector diff = CVector.subtract(p1.getPivot(), p2.getPivot());
	if (p1.getCollisionRadius() + p2.getCollisionRadius() > diff.getMag()) {
	    return false;
	}
	return Collision.collides(p1, p2);
	}*/


    /**
     * This method takes two polygons and splits up their parts
     * and checks with other methods to see if the two collide.
     *
     * @param p1 - the first polygon to check with
     * @param p2 - the second polygon to check with
     * @return true if the two overlap at all
     */
    public static boolean collides(Projection p1, Projection p2) {
	CVector diff = CVector.subtract(p1.getPivot(), p2.getPivot());
	if (p1.getCollisionRadius() + p2.getCollisionRadius() > diff.getMag()) {
	    return false;
	}

	int end = 0;
	for (int start = 0; start < p1.getOutline().size(); start++) {
	    end = start + 1;
	    if (end == p1.getOutline().size()) {
		end = 0;
	    }

	    CVectorPair vp = new CVectorPair(p1.getOutline().get(start), p1.getOutline().get(end));

            boolean doesCollide = collides(p2, vp);
            if (doesCollide) {
                return true;
            }

            doesCollide = collides(p2, p1.getOutline().get(start));
            if (doesCollide) {
                return true;
            }
        }
	
        return false;
    }

    /**
     * This method takes a polygon and a line and checks if any of the polygon's
     * lines collide with the specified line.
     *
     * @param p - the polygon to check with
     * @param l1 - the line to check with
     * @return true if any parts collided
     */
    public static boolean collides(Projection p, CVectorPair vp2) {
        int end = 0;
        for (int start = 0; start < p.getOutline().size(); start++) {
            end = start + 1;
            if (end == p.getOutline().size()) {
                end = 0;
            }

	    CVectorPair vp1 = new CVectorPair(p.getOutline().get(start), p.getOutline().get(end));

            boolean doesCollide = collides(vp1, vp2);
            if (doesCollide) {
                return true;
            }
        }

        return false;
    }

    /**
     * This method takes two lines and uses a special formula to check
     * if the two lines overlap at all.
     *
     * @param l1 - the first line to check with
     * @param l2 - the second line to check with
     * @return true if the lines overlap
     */
    public static boolean collides(CVectorPair p1, CVectorPair p2) {
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
            return true;
        }
        return false;
    }

    /**
     * This method takes a point and a polygon and checks if the point
     * is inside of the polygon at all.
     *
     * @param p - the polygon to check inside of
     * @param v - the point to check inside the polygon
     * @return true if the point is inside the polygon
     */
    public static boolean collides(Projection p, CVector v) {
        boolean doesCollide = false;

        int end = 0;
        for (int start = 0; start < p.getOutline().size(); start++) {
            end = start + 1;
            if (end == p.getOutline().size()) {
                end = 0;
            }

	    CVectorPair vp = new CVectorPair(p.getOutline().get(start), p.getOutline().get(end));

            if (((vp.getStartY() > v.getY()) != (vp.getEndY() > v.getY())) &&
                (v.getX() < (vp.getEndX() - vp.getStartX()) *
                 (v.getY() - vp.getStartY()) /
                 (vp.getEndY() - vp.getStartY()) +
                  vp.getStartX())) {
                doesCollide = !doesCollide;
            }
        }

        return doesCollide;
    }
    
}
