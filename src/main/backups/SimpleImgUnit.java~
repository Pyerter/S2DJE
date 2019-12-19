package sharp.unit;

import sharp.utility.Anchor;
import sharp.utility.CVector;
import sharp.utility.Transform;
import sharp.collision.*;

import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

import javafx.scene.Node;
import javafx.scene.image.ImageView;

public class SimpleImgUnit extends SimpleUnit {

    private Img img;
    
    public SimpleImgUnit(String  image, double x, double y, double width, double height) {
	super();
	img = new Img(image, x, y, width, height);
    }

    public Projection getProjection() {
	return img.getProjection();
    }
    
    public Projection[] getCollider() {
	return img.getCollider();
    }

    public Node getNode() {
	return img.getIV();
    }

    public ImageView getIV() {
	return img.getIV();
    }

    public void setX(double x) {
	img.getProjection().setX(x);
    }

    public void setY(double y) {
	img.getProjection().setY(y);
    }

    public double getX() {
	return img.getProjection().getX();
    }

    public double getY() {
	return img.getProjection().getY();
    }

    public void rotate(double rot) {
	img.getProjection().rotate(rot);
    }

    public void rotateAround(CVector pivot, double rot) {
	img.getProjection().rotateAround(pivot, rot);
    }

    public boolean canLocallyRotate() {
	return img.canLocallyRotate();
    }

    public List<Transform> getTransforms() {
	return img.getTransforms();
    }

    public void addTransform(Transform t) {
	img.addTransform(t);
    }

    public void applyTransform(Transform t) {
	img.applyTransform(t);
    }

    public void revertTransform(Transform t) {
	img.revertTransform(t);
    }

    public boolean getHasTransformed() {
	return img.getHasTransformed();
    }

    public void setHasTransformed(boolean hasTransformed) {
	img.setHasTransformed(hasTransformed);
    }

    public ArrayList<Collidable> getCollidables() {
	return img.getCollidables();
    }

    public void update() {
	System.out.println("\n\n\nUpdating image:");
	img.update();
	super.update();
    }

}
