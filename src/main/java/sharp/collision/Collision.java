package sharp.collision;

import sharp.unit.Projector;
import sharp.utility.CVector;

import javafx.scene.shape.Polygon;
import javafx.scene.shape.Line;
import javafx.geometry.Point2D;

/** This class has methods to determine collision between different shapes. */
public class Collision {

    public static boolean collides(Collidable c1, Collidable c2) {
	return Collision.collides(c1.getCollider(), c2.getCollider());
    }

    public static boolean collides(Projector p1, Projector p2) {
	CVector diff = CVector.subtract(p1.getPosition(), p2.getPosition());
	if (p1.getCollisionRadius() + p2.getCollisionRadius() > diff.getMag()) {
	    return false;
	}
	return Collision.collides(p1, p2);
    }


    /**
     * This method takes two polygons and splits up their parts
     * and checks with other methods to see if the two collide.
     *
     * @param p1 - the first polygon to check with
     * @param p2 - the second polygon to check with
     * @return true if the two overlap at all
     */
    public static boolean collides(Polygon p1, Polygon p2) {
	int end = 0;
	for (int start = 0; start < p1.getPoints().size(); start += 2) {
	    end = start + 2;
	    if (end == p1.getPoints().size()) {
		end = 0;
	    }

	    Line l = new Line(p1.getPoints().get(start),
                              p1.getPoints().get(start + 1),
                              p1.getPoints().get(end),
                              p1.getPoints().get(end + 1));

            boolean doesCollide = collides(p2, l);
            if (doesCollide) {
                return true;
            }

            doesCollide = collides(p2, new Point2D(p1.getPoints().get(start), p1.getPoints().get(start + 1)));
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
    public static boolean collides(Polygon p, Line l1) {
        int end = 0;
        for (int start = 0; start < p.getPoints().size(); start += 2) {
            end = start + 2;
            if (end == p.getPoints().size()) {
                end = 0;
            }

            Line l2 = new Line(p.getPoints().get(start),
                               p.getPoints().get(start + 1),
                               p.getPoints().get(end),
                               p.getPoints().get(end + 1));

            boolean doesCollide = collides(l1, l2);
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
    public static boolean collides(Line l1, Line l2) {
        // This equation is found to be similar as the divider, so it's only calculated once
        double divider = ((l2.getEndY() - l2.getStartY()) * (l1.getEndX() - l1.getStartX()) -
                         (l2.getEndX() - l2.getStartX()) * (l1.getEndY() - l1.getStartY()));

        double uA = ((l2.getEndX() - l2.getStartX()) * (l1.getStartY() - l2.getStartY()) -
                    (l2.getEndY() - l2.getStartY()) * (l1.getStartX() - l2.getStartX())) /
            divider;

        double uB = ((l1.getEndX() - l1.getStartX()) * (l1.getStartY() - l2.getStartY()) -
                    (l1.getEndY() - l1.getStartY()) * (l1.getStartX() - l2.getStartX())) /
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
     * @param point - the point to check inside the polygon
     * @return true if the point is inside the polygon
     */
    public static boolean collides(Polygon p, Point2D point) {
        boolean doesCollide = false;

        int end = 0;
        for (int start = 0; start < p.getPoints().size(); start += 2) {
            end = start + 2;
            if (end == p.getPoints().size()) {
                end = 0;
            }

            Line l = new Line(p.getPoints().get(start),
                              p.getPoints().get(start + 1),
                              p.getPoints().get(end),
                              p.getPoints().get(end + 1));

            if (((l.getStartY() > point.getY()) != (l.getEndY() > point.getY())) &&
                (point.getX() < (l.getEndX() - l.getStartX()) *
                 (point.getY() - l.getStartY()) /
                 (l.getEndY() - l.getStartY()) +
                  l.getStartX())) {
                doesCollide = !doesCollide;
            }
        }

        return doesCollide;
    }
    
}
