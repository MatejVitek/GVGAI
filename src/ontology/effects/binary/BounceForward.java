package ontology.effects.binary;

import core.VGDLSprite;
import core.content.InteractionContent;
import core.game.Game;
import ontology.effects.Effect;
import tools.*;

/**
 * Created with IntelliJ IDEA. User: Diego Date: 04/11/13 Time: 15:56 This is a Java port from Tom Schaul's VGDL - https://github.com/schaul/py-vgdl
 */
public class BounceForward extends Effect {

	public BounceForward(InteractionContent cnt) {
		this.parseParameters(cnt);
	}

	@Override
	public void execute(VGDLSprite sprite1, VGDLSprite sprite2, Game game) {
		Vector2d dir = new Vector2d(sprite2.lastDirection());
		dir.normalise();

		if (sprite2.lastDirection().x * sprite2.orientation.x() < 0) dir.x *= -1;

		if (sprite2.lastDirection().y * sprite2.orientation.y() < 0) dir.y *= -1;

		// Rectangle r = new Rectangle(sprite1.rect);
		sprite1.physics.activeMovement(sprite1, new Direction(dir.x, dir.y), sprite2.speed);
		// sprite1.lastrect = r;
		sprite1.orientation = new Direction(dir.x, dir.y);
		game._updateCollisionDict(sprite1);
	}
}
