package ontology.sprites.npc;

import java.awt.Dimension;
import core.VGDLSprite;
import core.content.SpriteContent;
import ontology.Types;
import tools.Vector2d;

/**
 * Created with IntelliJ IDEA. User: Diego Date: 21/10/13 Time: 18:13 This is a Java port from Tom Schaul's VGDL - https://github.com/schaul/py-vgdl
 */
public class RandomInertial extends RandomNPC {

	public RandomInertial() {}

	public RandomInertial(Vector2d position, Dimension size, SpriteContent cnt) {
		// Init the sprite
		this.init(position, size);

		// Specific class default parameter values.
		loadDefaults();

		// Parse the arguments.
		this.parseParameters(cnt);
	}

	@Override
	protected void loadDefaults() {
		super.loadDefaults();
		physicstype_id = Types.PHYSICS_CONT;
		is_oriented = true;
	}

	@Override
	public VGDLSprite copy() {
		RandomInertial newSprite = new RandomInertial();
		this.copyTo(newSprite);
		return newSprite;
	}

	@Override
	public void copyTo(VGDLSprite target) {
		RandomInertial targetSprite = (RandomInertial) target;
		super.copyTo(targetSprite);
	}
}
