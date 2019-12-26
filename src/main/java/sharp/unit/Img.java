package sharp.unit;

import sharp.game.App;
import sharp.utility.Transform;
import sharp.utility.Translatable;
import sharp.utility.CVector;
import sharp.utility.Anchor;
import sharp.collision.Collidable;
import sharp.collision.Projection;
import sharp.collision.Collision;

import java.util.LinkedList;
import java.util.ArrayList;

import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

public class Img extends Projection implements Collidable {

    private ImageView iv;
    private LinkedList<Transform> transforms = new LinkedList<>();
    private boolean hasTransformed;
    private ArrayList<Collidable> collidables = new ArrayList<>();
    private int priority;
    private Projection[] projections;
    private CVector previousPosition = new CVector();
    private String imgName;
    // these values are percentages of width and height the anchor is offset from center
    private double xOffset = 0.0;
    private double yOffset = 0.0;
    
    public Img(String image, Anchor pivot, CVector dimensions) {
	super(pivot);
	this.imgName = image;
	iv = new ImageView(App.getImagesPath() + image);
	resize(dimensions);
	Collision.setPriority(this);
	projections = new Projection[]{this};
	previousPosition.set(pivot);
    }

    public Img(String image, Anchor pivot, double xOffset, double yOffset) {
	super(pivot);
	this.imgName = image;
	this.xOffset = xOffset;
	this.yOffset = yOffset;
	iv = new ImageView(App.getImagesPath() + image);
	resize(new CVector(iv.getImage().getWidth(), iv.getImage().getHeight()));
	Collision.setPriority(this);
	projections = new Projection[]{this};
	previousPosition.set(pivot);
    }

    public void setPreviousPosition(CVector previousPosition) {
	this.previousPosition.set(previousPosition);
    }

    public CVector getPreviousPosition() {
	return previousPosition;
    }

    public void setX(double x) {
	double diff = x - getX();
	super.setX(x);
	iv.setX(iv.getX() + diff);
    }

    public void setY(double y) {
	double diff = y - getY();
	super.setY(y);
	iv.setY(iv.getY() + diff);
    }

    public void rotate(double rot) {
	super.rotate(rot);
	iv.setRotate(iv.getRotate() + Math.toDegrees(rot));
    }

    public void rotateAround(CVector pivot, double rot) {
	CVector temp = CVector.subtract(getPivot(), pivot);
	super.rotateAround(pivot, rot);
	temp.rotate(rot);
	iv.setX(iv.getX() + temp.getX());
	iv.setY(iv.getY() + temp.getY());
	iv.setRotate(iv.getRotate() + Math.toDegrees(rot));
    }

    public void resize(CVector dimensions) {
	double xDim = (dimensions.getX() / 2);
	double yDim = (dimensions.getY() / 2);
	if (dimensions.getX() <= 0) {
	    dimensions.setX(Double.MIN_NORMAL);
	    xDim = Double.MIN_NORMAL;	    
	}

	if (dimensions.getY() <= 0) {
	    yDim = Double.MIN_NORMAL;
	    dimensions.setY(Double.MIN_NORMAL);
	}
	
	double xOff = xDim * xOffset;
	double yOff = yDim * yOffset;
	CVector[] newOutline = {
	    new CVector(getPivot().getX() + xOff - xDim,
			getPivot().getY() + yOff - yDim),
	    new CVector(getPivot().getX() + xOff + xDim,
			getPivot().getY() + yOff - yDim),
	    new CVector(getPivot().getX() + xOff + xDim,
			getPivot().getY() + yOff + yDim),
	    new CVector(getPivot().getX() + xOff - xDim,
			getPivot().getY() + yOff + yDim)};
	setOutline(newOutline);
	iv.setRotate(0.0);
	iv.setFitWidth(dimensions.getX());
	iv.setFitHeight(dimensions.getY());
	iv.setX(newOutline[0].getX());
	iv.setY(newOutline[0].getY());
    }
    
    public ArrayList<Collidable> getCollidables() {
	return collidables;
    }

    public Projection[] getCollider() {
	return projections;
    }

    public Projection getProjection() {
	return this;
    }

    public void addCollidables(Collidable ... c) {
	if (collidables == null) {
	    collidables = new ArrayList<>();
	}
	for (Collidable coll: c) {
	    if (!collidables.contains(coll)) {
		collidables.add(coll);
	    }
	}
    }

    public void setPriority(int priority) {
	this.priority = priority;
    }

    public int getPriority() {
	return priority;
    }

    public void update() {
	if (collidables == null || collidables.size() == 0) {
	    super.update();
	}
	setPreviousPosition(getPivot());
	boolean doneMoving = !fineUpdate(discreteUpdate());
	if (doneMoving) {
	    endUpdate();
	}
    }

    public ImageView getIV() {
	return iv;
    }

    public String toString() {
	return "Img: ImageUsed(" + imgName + "), Priority(" + getPriority() + ")";
    }
    
}
