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

public class Img extends Projection {

    private LinkedList<ImageView> imgs;
    private Group group;
    // these values are percentages of width and height the anchor is offset from center
    private CVector offset = new CVector();
    private double xOffset = 0.0;
    private double yOffset = 0.0;
    
    public Img(Anchor pivot, CVector dimensions, CVector offset, String ... imgNames) {
	setup(pivot);
	if (offset != null) {
	    this.offset.set(offset);
	}
	for (String s: imgNames) {
	    imgs.add(new ImageView(App.getImagesPath() + s));
	    if (dimensions != null) {
		imgs.setFitWidth(dimensions.getX());
		imgs.setFitHeight(dimensions.getY());
	    }
	    
	}
	if (dimensions == null) {
	    resize(new CVector(iv.getImage().getWidth(), iv.getImage().getHeight()));
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
	iv.setX(iv.getX() + diff);
    }

    public void setY(double y) {
	double diff = y - getY();
	super.setY(y);
	iv.setY(iv.getY() + diff);
    }

    public void set(CVector v) {
	setX(v.getX());
	setY(v.getY());
    }

    public void setOffset(CVector offset) {
	this.offset.set(offset);
	resize(new CVector(iv.getImage().getWidth(), iv.getImage().getHeight()));
    }

    public CVector getOffset() {
	return offset;
    }

    public void rotate(double rot) {
	super.rotate(rot);
	iv.setRotate(iv.getRotate() + Math.toDegrees(rot));
    }

    public void rotateAround(CVector pivot, double rot) {
	CVector temp = CVector.subtract(getPivot(), pivot);
	temp.rotate(rot);
	temp.add(pivot);
	CVector diff = CVector.subtract(temp, getPivot());
	iv.setX(iv.getX() + diff.getX());
	iv.setY(iv.getY() + diff.getY());
	iv.setRotate(iv.getRotate() + Math.toDegrees(rot));
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
	    outlinePoints.sort((a, b) -> a.heading() - b.heading());
	    LinkedList<CVector> outline = new LinkedList<>();
	    int end = 0;
	    for (int i = 0; i < tempOutline.size() - 1; i++) {
		end = i + 1;
		if (end == tempOutline.size()) {
		    end = 0;
		}
		boolean xFirst = true;
		CVector tempFirst = new CVector(tempOutline.get(i));
		CVector tempSecond = new CVector(tempOutline.get(i));
		double xLength = tempSecond.getX() - tempFirst.getX();
		double yLength = tempSecound.getY() - tempFirst.getX();
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
	    iv.setFitWidth(dimensions.getX());
	    iv.setFitHeight(dimensions.getY());
	    resetImgPlacement(iv);
	}
    }

    public int update() {
	return super.update();
    }

    public void endUpdate() {
	correctImgs();
	super.endUpdate();
    }

    public ImageView getIV() {
	return iv;
    }

    public String toString() {
	return "Img: ImageUsed(" + imgName + "), Priority(" + getPriority() + ")";
    }
    
}

