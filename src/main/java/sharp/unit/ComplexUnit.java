package sharp.unit;

import sharp.game.App;
import sharp.utility.CVector;
import sharp.collision.Collidable;
import sharp.collision.Projection;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Group;
import javafx.scene.shape.Polygon;
import javafx.scene.image.ImageView;

public class ComplexUnit extends Group implements Unit, Collidable {

    private Projection projection = new Projection();
    private ArrayList<Polygon> polies;
    private ArrayList<ImageView> images;
    private CVector velocity = new CVector();
    private CVector acceleration = new CVector();
    private Double rotVelocity = 0.0;
    private Double rotAcceleration = 0.0;
    private boolean grav = true;
    
    public ComplexUnit(boolean hasPolies, boolean hasImages) {
	super();
	if (hasPolies) {
	    polies = new ArrayList<>();
	}
	if (hasImages) {
	    images = new ArrayList<>();
	}
    }

    public ComplexUnit(boolean hasPolies, boolean hasImages, CVector position) {
	this(hasPolies, hasImages);
	projection.getPivot().setX(position.getX());
	projection.getPivot().setY(position.getY());
    }

    public ComplexUnit(List<Polygon> polies, List<ImageView> images) {
	super();
	if (polies != null && polies.size() > 0) {
	    this.polies = new ArrayList<>();
	    for (Polygon p: polies) {
		this.polies.add(p);
	    }
	}
	if (images != null && images.size() > 0) {
	    this.images = new ArrayList<>();
	    for (ImageView iv: images) {
		this.images.add(iv);
	    }
	}
    }

    public ComplexUnit(List<Polygon> polies, List<ImageView> images, CVector position) {
	this(polies, images);
	projection.getPivot().setX(position.getX());
	projection.getPivot().setY(position.getY());
    }

    public void setIncludeProjection(boolean include) {
	if (include) {
	    if (!this.getChildren().contains(projection)) {
		this.getChildren().add(projection);
	    }
	} else {
	    if (this.getChildren().contains(projection)) {
		this.getChildren().add(projection);
	    }
	}
    }

    public void setGrav(boolean grav) {
	this.grav = grav;
    }

    public void setRotAcceleration(double rotAcceleration) {
	this.rotAcceleration = rotAcceleration;
    }

    public void setRotVelocity(double rotVelocity) {
	this.rotVelocity = rotVelocity;
    }

    public Double getRotAcceleration() {
	return rotAcceleration;
    }

    public Double getRotVelocity() {
	return rotVelocity;
    }

    public CVector getVelocity() {
	return velocity;
    }

    public CVector getAcceleration() {
	return acceleration;
    }

    public Projection getCollider() {
	return projection;
    }

    public Projector getProjection() {
	return projection;
    }

    public boolean addImage(ImageView iv) {
	if (images != null && !images.contains(iv)) {
	    iv.setRotate(iv.getRotate() + projection.getRotation());
	    iv.setX(iv.getX() + projection.getPosition().getX());
	    iv.setY(iv.getY() + projection.getPosition().getY());
	    images.add(iv);
	    this.getChildren().add(iv);
	    return true;
	}
	return false;
    }

    public boolean addPoly(Polygon p) {
	if (polies != null && p != null && !polies.contains(p)) {
	    for (int i = 0; i < p.getPoints().size(); i += 2) {
		CVector point = new CVector(p.getPoints().get(i), p.getPoints().get(i + 1));
		point.rotate(projection.getRotation());
		point.add(projection.getPosition());
		p.getPoints().set(i, point.getX());
		p.getPoints().set(i + 1, point.getY());
	    }
	    polies.add(p);
	    this.getChildren().add(p);
	    return true;
	}
	return false;
    }

    public void checkChildren() {
	if (polies != null) {
	    for (Polygon p: polies) {
		if (!this.getChildren().contains(p)) {
		    this.getChildren().add(p);
		}
	    }
	}
	if (images != null) {
	    for (ImageView iv: images) {
		if (!this.getChildren().contains(iv)) {
		    this.getChildren().add(iv);
		}
	    }
	}
    }

    public void update() {
	checkChildren();
	rotVelocity += rotAcceleration;
	rotAcceleration = 0.0;
	if (grav) {
	    Unit.GRAVITY.apply(acceleration);
	}
	velocity.add(acceleration);
	acceleration.mult(0.0);
	projection.update(velocity, rotVelocity, polies, images);
    }

}
