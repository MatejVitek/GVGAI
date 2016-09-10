package matej;

import core.game.StateObservation;

public abstract class ClassificationHandler {

	public static final String[] FEATURE_NAMES = {"Use", "UpDown", "LeftRight", "InitHP", "MaxHP", "Speed", "OrientationX", "OrientationY", "Puzzle", "Shooting", "Destroying", "Collecting",
			"Coloring", "Positioning", "Pushing", "InstantWin", "Creating"};

	protected StateObservation so;
	protected String[] games;

	public ClassificationHandler(StateObservation so, String[] games) {
		this.so = so;
		this.games = games;
	}

	public abstract Prediction getPrediction(long timeInMilliseconds);
}
