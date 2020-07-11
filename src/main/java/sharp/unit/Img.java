package sharp.unit;

import sharp.game.App;
import sharp.utility.Transform;
import sharp.utility.Translatable;
import sharp.utility.CVector;
import sharp.utility.Anchor;
import sharp.utility.Utility;
import sharp.collision.Collidable;
import sharp.collision.Projection;
import sharp.collision.Collision;

import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.Group;

/**
 * This class represents a {@code Projection} that uses one or more images to display the projection.
 */
public class Img extends Projection {

    private LinkedList<ImageView> imgs = new LinkedList<>();;
    private Group group = new Group();
    // these values are percentages of width and height the anchor is offset from center
    private CVector offset = new CVector();
    private CVector dimensions;

    /**
     * A public constructor that takes in a pivot point, the image dimensions (width, height), the image offset to center,
     * and a varargs list of image names (without the app's image path).
     *
     * @param pivot - the {@code Anchor} that acts as the pivot of the {@code Img}.
     * @param dimensions - the {@code CVector} that represents  the rectangular dimensions of the image. If this is null,
     * it will not be considered in resizing the images.
     * @param offset - the {@code CVector} that represents the offset of the image's center. If this is null,
     * the offset will be considered (0, 0).
     * @param imgNames - the {@code String[]} array that holds all images that will be displayed on this {@code Projection}.
     */
    public Img(Anchor pivot, CVector dimensions, CVector offset, String ... imgNames) {
	setup(pivot);
	if (offset != null) {
	    this.offset.set(offset);
	}
	if (dimensions != null) {
	    this.dimensions = new CVector(dimensions);
	}
	for (String s: imgNames) {
	    ImageView temp = new ImageView(App.getImagesPath() + s);
	    imgs.add(temp);
	    if (dimensions != null) {
		temp.setFitWidth(dimensions.getX());
		temp.setFitHeight(dimensions.getY());
	    }   
	}
	if (dimensions == null) {
	    resize(null);
	} else {
	    resize(dimensions);
	}
    }

    /**
     * This method returns the group holding the display.
     *
     * @return the {@code Group} that holds the display
     */
    public Group getNode() {
	return group;
    }

    /**
     * This method returns the list of displayed {@code ImageView}s.
     *
     * @return the list if {@code ImageView}s to display
     */
    public List<ImageView> getImgs() {
	return imgs;
    }

    /**
     * This method adds an {@code Image} to this {@code Projection}'s list to display.
     *
     * @param imgName - the name of the image (relative to the app's image path) to add to the list.
     * @return true if the image was successfully added to the list
     */
    public boolean addImage(String imgName) {
	imgs.add(new ImageView(App.getImagesPath() + imgName));
	resize(dimensions);
	return true;
    }

    /**
     * This method adds an {@code Image} to this {@code Projection}'s list to display.
     *
     * @param img - the {@code Image} to add to the list.
     * @return true if the image was successfully added to the list
     */
    public boolean addImage(Image img) {
	imgs.add(new ImageView(img));
	resize(dimensions);
	return true;
    }

    /**
     * This method sets the x coordinate of this {@code Projection}.
     *
     * @param x - the new x coordinate
     */
    public void setX(double x) {
	double diff = x - getX();
	super.setX(x);
	for (ImageView iv: imgs) {
	    iv.setX(iv.getX() + diff);
	}
    }

    /**
     * This method sets the y coordinate of this {@code Projection}.
     *
     * @param y - the new y coordinate
     */
    public void setY(double y) {
	double diff = y - getY();
	super.setY(y);
	for (ImageView iv: imgs) {
	    iv.setY(iv.getY() + diff);
	}
    }

    /**
     * This method sets the x and y coordinates of this {@code Projection}.
     *
     * @param v - the new vector representing the coordinates
     */
    public void set(CVector v) {
	setX(v.getX());
	setY(v.getY());
    }

    /**
     * This method sets the x and y offsets of this {@code Projection}.
     *
     * @param v - the new vector representing the offset
     */
    public void setOffset(CVector offset) {
	this.offset.set(offset);
	resize(null);
    }

    /**
     * Get the offset.
     *
     * @return the offset
     */
    public CVector getOffset() {
	return offset;
    }

    /**
     * This method rotates the {@code Projection} and all {@code Image}s in the projection
     * around the pivot.
     *
     * @param rot - the value, in radians, to rotate around the pivot.
     */
    public void rotate(double rot) {
	super.rotate(rot);
	for (ImageView iv: imgs) {
	    iv.setRotate(iv.getRotate() + Math.toDegrees(rot));
	}
    }

    /**
     * This method rotates the {@code Projection} and all {@code Image}s in the projection
     * around the given pivot.
     *
     * @param pivot - the {@code CVector} to act as the pivot point for this rotation.
     * @param rot - the value, in radians, to rotate around the pivot.
     */
    public void rotateAround(CVector pivot, double rot) {
	CVector temp = CVector.subtract(getPivot(), pivot);
	temp.rotate(rot);
	temp.add(pivot);
	CVector diff = CVector.subtract(temp, getPivot());
	double rotation = Math.toDegrees(rot);
	for (ImageView iv: imgs) {
	    iv.setX(iv.getX() + diff.getX());
	    iv.setY(iv.getY() + diff.getY());
	    iv.setRotate(iv.getRotate() + Math.toDegrees(rotation));
	}
	super.rotateAround(pivot, rot);
    }

    /**
     * This method makes sure that all {@code ImageView}s in the local list are
     * children in the {@code Group}.
     */
    public void correctImgs() {
	for (ImageView iv: imgs) {
	    if (!group.getChildren().contains(iv)) {
		group.getChildren().add(iv);
	    }
	}
    }

    /**
     * This method will readjust all {@code ImageView} locations to confirm that they
     * are correct, relative to the pivot and offset.
     */
    public void correctImagePlacements() {
	ArrayList<CVector> outlinePoints = new ArrayList<>();
	for (ImageView iv: imgs) {
	    CVector[] tempOutline = resetImgPlacement(iv);
	    if (tempOutline != null) {
		for (CVector v: tempOutline) {
		    outlinePoints.add(v);
		}
	    }
	}
	if (outlinePoints.size() > 0) {
	    outlinePoints.sort((a, b) -> (int)((b.heading() - a.heading()) * 1000));
	    ArrayList<CVector> outline = new ArrayList<>(outlinePoints.size());
	    System.out.println();
	    for (CVector c: outlinePoints) {
		System.out.println("Point for outline: " + c);
	    }
	    int end = 0;
	    for (int i = 0; i < outlinePoints.size(); i++) {
		boolean xFirst = true;
		end = i + 1;
		if (end == outlinePoints.size()) {
		    end = 0;
		}
		CVector tempFirst = new CVector(outlinePoints.get(i));
		CVector tempSecond = new CVector(outlinePoints.get(end));
		double xLength = tempSecond.getX() - tempFirst.getX();
		double yLength = tempSecond.getY() - tempFirst.getY();
		CVector xTrans = new CVector(xLength + tempFirst.getX(), tempFirst.getY());
		CVector yTrans = new CVector(tempFirst.getX(), yLength + tempFirst.getY());
		if (Utility.sign(xLength) != Utility.sign(yLength)) {
		    if (!Utility.isAbout(yLength, 0, 1)) {
			outline.add(yTrans);
			System.out.println("Added coord " + yTrans);
		    }
		    if (!Utility.isAbout(xLength, 0, 1)) {
			outline.add(CVector.add(xTrans, new CVector(0, yLength)));
			System.out.println("Added coord " + xTrans);
		    }
		} else {
		    if (!Utility.isAbout(xLength, 0, 1)) {
			outline.add(xTrans);
			System.out.println("Added coord " + xTrans);
		    }		    
		    if (!Utility.isAbout(yLength, 0, 1)) {
			outline.add(CVector.add(yTrans, new CVector(xLength, 0)));
			System.out.println("Added coord " + yTrans);
		    }
		}
		System.out.println("Difference vector #" + i + ": " + CVector.subtract(tempFirst, tempSecond));
		// create two vectors, one creating the difference in x, one in y,
		// and add them to the outline so that the collider is accurate
		// along image edges
	    }
	    getOutline().clear();
	    setOutline(outline);
	}
    }

    /**
     * This method corrects the rotation and location of all {@code ImageView}s.
     */
    public CVector[] resetImgPlacement(ImageView iv) {
	iv.setRotate(0.0);
	double xDim = iv.getImage().getWidth();
	double yDim = iv.getImage().getHeight();
	if (dimensions != null) {
	    xDim = dimensions.getX() / 2;
	    yDim = dimensions.getY() / 2;
	} else {
	    xDim /= 2;
	    yDim /= 2;
	}
	if (xDim <= 0) {
	    App.print("Set x dimension basically 0 from " + xDim);
	    xDim = Double.MIN_NORMAL;
	}
	if (yDim <= 0) {
	    App.print("Set y dimension basically 0 from " + yDim);
	    yDim = Double.MIN_NORMAL;
	}
	double xOff = xDim * offset.getX();
	double yOff = yDim * offset.getY();
	iv.setX(getPivot().getX() + xOff - xDim);
	iv.setY(getPivot().getY() + yOff - yDim);
	if (dimensions == null) {
	    CVector[] newOutline = {
		new CVector(getPivot().getX() + xOff - xDim,
			    getPivot().getY() + yOff - yDim),
		new CVector(getPivot().getX() + xOff + xDim,
			    getPivot().getY() + yOff - yDim),
		new CVector(getPivot().getX() + xOff + xDim,
			    getPivot().getY() + yOff + yDim),
		new CVector(getPivot().getX() + xOff - xDim,
			    getPivot().getY() + yOff + yDim)};
	    
	    
	    return newOutline;
	}
	return null;
    }

    /**
     * This method resizes all {@code ImageView}s to fit a given dimension.
     *
     * @param dimensions - the {@code CVector} representing the width and height.
     */
    public void resize(CVector dimensions) {
	for (ImageView iv: imgs) {
	    if (dimensions != null) {
		iv.setFitWidth(dimensions.getX());
		iv.setFitHeight(dimensions.getY());
	    }
	    resetImgPlacement(iv);
	}
	correctImagePlacements();
    }

    /**
     * Updates the {@code Projection}.
     *
     * @return 0 if no errors
     */
    public int update() {
	return super.update();
    }

    /**
     * Runs the end of update actions.
     */
    public void endUpdate() {
	correctImgs();
	super.endUpdate();
    }

    /**
     * Returns "Img: Images used[number of images]".
     */
    public String toString() {
	return "Img: Images used[" + imgs.size() + "]";
    }
    
}

