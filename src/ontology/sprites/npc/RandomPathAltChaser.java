package ontology.sprites.npc;

import java.awt.Dimension;
import core.VGDLSprite;
import core.content.SpriteContent;
import core.game.Game;
import ontology.Types;
import tools.*;

/**
 * Created by Diego on 24/02/14.
 */
public class RandomPathAltChaser extends PathAltChaser {

	public double epsilon;

	public RandomPathAltChaser() {}

	public RandomPathAltChaser(Vector2d position, Dimension size, SpriteContent cnt) {
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
		epsilon = 0.0;
	}

	@Override
	public void postProcess() {
		super.postProcess();
	}

	@Override
	public void update(Game game) {
		double roll = game.getRandomGenerator().nextDouble();
		if (roll < epsilon) {
			// do a sampleRandom move.
			super.updatePassive();
			Direction act = (Direction) Utils.choice(Types.DBASEDIRS, game.getRandomGenerator());
			this.physics.activeMovement(this, act, this.speed);
		}
		else {
			super.update(game);
		}
	}

	@Override
	public VGDLSprite copy() {
		RandomPathAltChaser newSprite = new RandomPathAltChaser();
		this.copyTo(newSprite);
		return newSprite;
	}

	@Override
	public void copyTo(VGDLSprite target) {
		RandomPathAltChaser targetSprite = (RandomPathAltChaser) target;
		targetSprite.epsilon = this.epsilon;
		super.copyTo(targetSprite);
	}

}
