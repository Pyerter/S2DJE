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

public class Img extends Projection {

    private LinkedList<ImageView> imgs = new LinkedList<>();;
    private Group group = new Group();
    // these values are percentages of width and height the anchor is offset from center
    private CVector offset = new CVector();
    private CVector dimensions;
    
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

    public Group getNode() {
	return group;
    }

    public List<ImageView> getImgs() {
	return imgs;
    }

    public boolean addImage(String imgName) {
	imgs.add(new ImageView(App.getImagesPath() + imgName));
	return true;
    }

    public void setX(double x) {
	double diff = x - getX();
	super.setX(x);
	for (ImageView iv: imgs) {
	    iv.setX(iv.getX() + diff);
	}
    }

    public void setY(double y) {
	double diff = y - getY();
	super.setY(y);
	for (ImageView iv: imgs) {
	    iv.setY(iv.getY() + diff);
	}
    }

    public void set(CVector v) {
	setX(v.getX());
	setY(v.getY());
    }

    public void setOffset(CVector offset) {
	this.offset.set(offset);
	resize(null);
    }

    public CVector getOffset() {
	return offset;
    }

    public void rotate(double rot) {
	super.rotate(rot);
	for (ImageView iv: imgs) {
	    iv.setRotate(iv.getRotate() + Math.toDegrees(rot));
	}
    }

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

    public void correctImgs() {
	for (ImageView iv: imgs) {
	    if (!group.getChildren().contains(iv)) {
		group.getChildren().add(iv);
	    }
	}
    }

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
	    outlinePoints.sort((a, b) -> (int)((a.heading() - b.heading()) * 100));
	    LinkedList<CVector> outline = new LinkedList<>();
	    int end = 0;
	    for (int i = 0; i < outlinePoints.size() - 1; i++) {
		end = i + 1;
		if (end == outlinePoints.size()) {
		    end = 0;
		}
		boolean xFirst = true;
		CVector tempFirst = new CVector(outlinePoints.get(i));
		CVector tempSecond = new CVector(outlinePoints.get(i));
		double xLength = tempSecond.getX() - tempFirst.getX();
		double yLength = tempSecond.getY() - tempFirst.getX();
		CVector xTrans = new CVector(xLength, 0.0);
		CVector yTrans = new CVector(0.0, yLength);
		if (Utility.sign(xLength) == Utility.sign(yLength)) {
		    if (!Utility.isAbout(yLength, 0, 0.001)) {
			outline.add(yTrans);
		    }
		    if (!Utility.isAbout(xLength, 0, 0.001)) {
			outline.add(xTrans);
		    }
		} else {
		    if (!Utility.isAbout(xLength, 0, 0.001)) {
			outline.add(xTrans);
		    }		    
		    if (!Utility.isAbout(yLength, 0, 0.001)) {
			outline.add(yTrans);
		    }
		}
		// create two vectors, one creating the difference in x, one in y,
		// and add them to the outline so that the collider is accurate
		// along image edges
	    }
	}
    }

    public CVector[] resetImgPlacement(ImageView iv) {
	iv.setRotate(0.0);
	double xDim = iv.getFitWidth();
	double yDim = iv.getFitHeight();
	if (dimensions != null) {
	    xDim = dimensions.getX() / 2;
	    yDim = dimensions.getY() / 2;
	} else {
	    xDim /= 2;
	    yDim /= 2;
	}
	if (xDim <= 0) {
	    xDim = Double.MIN_NORMAL;
	}
	if (yDim <= 0) {
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

    public int update() {
	return super.update();
    }

    public void endUpdate() {
	correctImgs();
	super.endUpdate();
    }
    
    public String toString() {
	return "Img: Images used[" + imgs.size() + "]";
    }
    
}

