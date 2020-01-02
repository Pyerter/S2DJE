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
    private String imgName;
    
    public SimpleImgUnit(String  image, double x, double y, double width, double height) {
	super();
	this.imgName = image;
	img = new Img(image, new Anchor(x, y), new CVector(width, height));
    }

    public SimpleImgUnit(String image, double x, double y, CVector offset) {
	super();
	this.imgName = image;
	img = new Img(image, new Anchor(x, y), offset.getX(), offset.getY());
    }

    public SimpleImgUnit(String image, SimpleImgUnit baseRef) {
	super();
	this.imgName = image;
	img = new Img(image, new Anchor(baseRef.getProjection().getPivot()), offset.getX(), offset.getY());
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

    public void resize(CVector dimensions) {
	img.resize(dimensions);
    }

    public void setX(double x) {
	img.setX(x);
    }

    public void setY(double y) {
	img.setY(y);
    }

    public double getX() {
	return img.getX();
    }

    public double getY() {
	return img.getY();
    }

    public void rotate(double rot) {
	img.rotate(rot);
    }

    public void rotateAround(CVector pivot, double rot) {
	img.rotateAround(pivot, rot);
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
	super.update();
    }

    public void endUpdate() {
	super.endUpdate();
    }

    public String toString() {
	return "Img-Unit: Image(" + imgName + "), Priority(" + getPriority() + ")";
    }

}
