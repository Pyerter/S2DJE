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
		System.out.println("Adding " + c + " to fineUpdaters");
		fineUpdaters.add(c);
	    }
	}
    }

    public static boolean willFineUpdate(Collidable c) {
	return fineUpdaters.contains(c);
    }

    public static void sortCollidables(List<Collidable> collidables) {
	if (collidables.size() <= 1) {
	    return;
	}
	System.out.println("Sorting fineUpdaters");
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
	    if (c.getTransforms().size() > maxTransforms) {
		maxTransforms = c.getTransforms().size();
	    }
	}
	System.out.println("Will sort through at least " + maxTransforms + " transforms");
	for (int i = 0; i < maxTransforms; i++) {
	    for (Collidable c: fineUpdaters) {
		System.out.println("Checking fine update for " + c);
		if (c.getTransforms().size() > i) {
		    System.out.println("Finely updating: " + c);
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
	System.out.println("Checking collision between " + c1 + " and " + c2);
	CVector collisionPoint = Collision.collides(c1.getCollider(), c2.getCollider());
	if (collisionPoint != null) {
	    System.out.println("The two Collidables have collided!");
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
	System.out.println("Checking collision (with projections) between " + p1 + " and " + p2);
	CVector diff = CVector.subtract(p1.getPivot(), p2.getPivot());
	if (p1.getCollisionRadius() + p2.getCollisionRadius() < diff.getMag()) {
	    System.out.println("The distances were too far. They don't collide.");
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

	end = 0;
	for (int start = 0; start < p1.getOutline().size(); start++) {
	    end = start + 1;
	    if (end == p1.getOutline().size()) {
		end = 0;
	    }

            CVector collidePoint = collides(p2, p1.getOutline().get(start));
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

	if (doesCollide) {
	    return new CVector(v);
	}
        return null;
    }
    
}
