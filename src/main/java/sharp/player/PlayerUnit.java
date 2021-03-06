/*package sharp.player;
 
import sharp.game.App;
import sharp.unit.SimpleImgUnit;
import sharp.unit.SimplePolyUnit;
import sharp.unit.ComplexUnit;
import sharp.unit.HingedUnit;
import sharp.utility.CVector;
import sharp.utility.Anchor;
import sharp.utility.Transform;
import sharp.utility.Oscillation;
import sharp.configurations.*;

import java.util.List;
import java.util.LinkedList;

import javafx.scene.paint.Color;

public class PlayerUnit extends ComplexUnit {

    private static final String PLAYER_CONFIGS = "player.txt";
    /*    private static final SimplePolyUnit PLAYER_BASE_UNIT = new SimplePolyUnit(new Anchor(0, 0),
									new CVector(-15, -40),
									new CVector(0, -40),
									new CVector(15, -40),
									new CVector(15, -35),
									new CVector(15, 40),
									new CVector(7, 40),
									new CVector(-7, 40),
									new CVector(-15, 40),
									new CVector(-15, -35));
    private ConfigSet configs;
    private HingedUnit torso;
    private HingedUnit face;
    private HingedUnit rightThigh;
    private HingedUnit rightShin;
    private HingedUnit leftThigh;
    private HingedUnit leftShin;
    private LinkedList<LinkedList<SimpleImgUnit>> faceViews = new LinkedList<LinkedList<SimpleImgUnit>>();

    private Oscillation headbob = new Oscillation(0, 0.1, 0, 1, true);

    private Oscillation walking = new Oscillation(0, Math.PI / 36, -Math.PI / 2,  Math.PI / 2, false);
    
    public PlayerUnit(CVector position) {
	super(new CVector(0, 0), new CVector(1, 0), new CVector(0, 1), new CVector(-1, 0), new CVector(0, -1));
	configs = ConfigReader.getConfigs(App.getConfigsPath() + PLAYER_CONFIGS);
	loadPresets();
	applyTransform(new Transform(position.getX(), position.getY()));
	headbob.setAccuracy(0.1);
	setHasTransformed(false);
	// Transform t = new Transform(torso.getRootUnit().getProjection().getPivot(), Math.PI);
	// applyTransform(t);
	System.out.println(torso.getGroup().getChildren().size());
	setGrav(true);
    }

    public void loadPresets() {
	torso = new HingedUnit(new SimplePolyUnit(new Anchor(0, 0),
						  new CVector(-15, -40),
						  new CVector(0, -40),
						  new CVector(15, -40),
						  new CVector(15, -35),
						  new CVector(15, 40),
						  new CVector(7, 40),
						  new CVector(-7, 40),
						  new CVector(-15, 40),
						  new CVector(-15, -35)));
	
	
	face = new HingedUnit(SimplePolyUnit.square(0.0));
	
	CVector imgOffset = new CVector(-0.5, -0.5);
	String playerFrontFacePath = "player character" + App.getFileSeperator() + "front face" + App.getFileSeperator();
	String playerRightFacePath = "player character" + App.getFileSeperator() + "right face" + App.getFileSeperator();
	String playerLeftFacePath = "player character" + App.getFileSeperator() + "left face" + App.getFileSeperator();

	String[] facePieceStrings = {"base", "left", "right", "brow", "hair"};
	LinkedList<SimpleImgUnit> faceRightView = new LinkedList<>();
	LinkedList<SimpleImgUnit> faceProfileView = new LinkedList<>();
	LinkedList<SimpleImgUnit> faceLeftView = new LinkedList<>();
	for (String s: facePieceStrings) {
	    if (configs.contains(s)) {
		String conf = configs.getConfig(s).getValue();
		faceProfileView.add(new SimpleImgUnit(playerFrontFacePath + conf, 0, 0, imgOffset));
		faceRightView.add(new SimpleImgUnit(playerRightFacePath + conf, 0, 0, imgOffset));
		faceLeftView.add(new SimpleImgUnit(playerLeftFacePath + conf, 0, 0, imgOffset));
	    } else if (s.equals("base")) {
		faceProfileView.add(new SimpleImgUnit(playerFrontFacePath + "base_0.png", 0, 0, imgOffset));
		faceRightView.add(new SimpleImgUnit(playerRightFacePath + "base_0.png", 0, 0, imgOffset));
		faceLeftView.add(new SimpleImgUnit(playerLeftFacePath + "base_0.png", 0, 0, imgOffset));
		App.print("Creating base config img because base config not found");
	    }
	}

	faceViews.add(faceRightView);
	faceViews.add(faceProfileView);
	faceViews.add(faceLeftView);
	
	for (LinkedList<SimpleImgUnit> l: faceViews) {
	    for (SimpleImgUnit u: l) {
		face.addChildUnit(u);
	    }
	}

	face.setRigid(true);
	torso.addHingedUnit(face, 1);

	SimplePolyUnit leg1 = new SimplePolyUnit(new Anchor(0, 0),
						 new CVector(-10, 0),
						 new CVector(0, 0),
						 new CVector(10, 0),
						 new CVector(10, 40),
						 new CVector(0, 40),
						 new CVector(-10, 40));
	SimplePolyUnit leg2 = new SimplePolyUnit(new Anchor(0, 0),
						 new CVector(-10, 0),
						 new CVector(0, 0),
						 new CVector(10, 0),
						 new CVector(10, 40),
						 new CVector(0, 40),
						 new CVector(-10, 40));
	SimplePolyUnit leg3 = new SimplePolyUnit(new Anchor(0, 0),
						 new CVector(-10, 0),
						 new CVector(0, 0),
						 new CVector(10, 0),
						 new CVector(10, 40),
						 new CVector(0, 40),
						 new CVector(-10, 40));
	SimplePolyUnit leg4 = new SimplePolyUnit(new Anchor(0, 0),
						 new CVector(-10, 0),
						 new CVector(0, 0),
						 new CVector(10, 0),
						 new CVector(10, 40),
						 new CVector(0, 40),
						 new CVector(-10, 40));
	rightThigh = new HingedUnit(leg1);
	leftThigh = new HingedUnit(leg2);
	rightShin = new HingedUnit(leg3);
	leftShin = new HingedUnit(leg4);
	rightThigh.setRigid(true);
	leftThigh.setRigid(true);
	rightShin.setRigid(true);
	leftShin.setRigid(true);
	

	torso.addHingedUnit(rightThigh, 5);
	torso.addHingedUnit(leftThigh, 6);
	rightThigh.addHingedUnit(rightShin, 4);
	leftThigh.addHingedUnit(leftShin, 4);
	
	this.addChildUnit(torso);
	setViews("profile");
    }

    public void setViews(String facing) {
	for (List<SimpleImgUnit> units: faceViews) {
	    for (SimpleImgUnit u: units) {
		u.setShow(false);
	    }
	}
	int index = 0;
	switch (facing) {
	case "right":
	    index = 0;
	    break;
	default:
	case "profile":
	    index = 1;
	    break;
	case "left":
	    index = 2;
	    break;
	}
	for (SimpleImgUnit u: faceViews.get(index)) {
	    u.setShow(true);
	}
    }

    public void update() {
	double bob = headbob.getOscillated();
	face.addTransform(new Transform(0.0, bob));
	super.update();
	System.out.println(torso.getCollidables().size());
    }

    public void walk(int direction) {
	rightThigh.rotate(direction * walking.getOscillated());
	leftThigh.rotate(direction * -walking.getOscillated());
    }

    public void stand() {
	rightThigh.resetRotation();
	leftThigh.resetRotation();
	walking.reset();
    }

}
*/
