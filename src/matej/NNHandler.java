package matej;

import java.util.ArrayList;
import org.neuroph.core.NeuralNetwork;
import core.game.StateObservation;
import ontology.Types;
import static ontology.Types.ACTIONS.*;

public class NNHandler {
	public static final String PATH = "nns/";
	public static final String NAME = "MLP.nnet";
	public static final String[] FEATURE_NAMES = {"Use", "UpDown", "LeftRight", 
			"InitHP", "MaxHP", "Speed", "OrientationX", "OrientationY"};

	private StateObservation so;
	private String[] games;

	public NNHandler(StateObservation so, String[] games) {
		this.so = so;
		this.games = games;
	}

	/**
	 * Predicts which game is most likely currently being played.
	 * 
	 * @return Name of predicted game
	 */
	public String getPrediction() {
		double[] features = getFeatures(so);
		double[] predictions = new double[games.length];

		NeuralNetwork nn = NeuralNetwork.createFromFile(PATH + NAME);
		nn.setInput(features);
		nn.calculate();
		predictions = nn.getOutput();

		int maxIndex = 0;
		double max = 0;
		for (int i = 1; i < predictions.length; i++)
			if (predictions[i] > max) {
				maxIndex = i;
				max = predictions[i];
			}
		return games[maxIndex];
	}

	/**
	 * Extracts features from initial state observation.
	 * 
	 * @param stateObs Observation of the initial state
	 * @return A vector of features in the correct order for input to NN
	 */
	public static double[] getFeatures(StateObservation so) {
		double[] features = new double[FEATURE_NAMES.length];
		ArrayList<Types.ACTIONS> actions = so.getAvailableActions();

		// is USE available
		features[0] = actions.contains(ACTION_USE) ? 1.0 : -1.0;
		// are UP/DOWN available
		features[1] = actions.contains(ACTION_UP) ? 1.0 : -1.0;
		// are LEFT/RIGHT available
		features[2] = actions.contains(ACTION_LEFT) ? 1.0 : -1.0;

		// initial and max HP (normalized for NN)
		features[3] = 2 * (double) so.getAvatarHealthPoints() / (double) so.getAvatarLimitHealthPoints() - 1.0;
		features[4] = 2 * (double) so.getAvatarLimitHealthPoints() / 1000.0 - 1.0;
		
		// speed, orientation (already normalized)
		features[5] = 2 * so.getAvatarSpeed() - 1.0;
		features[6] = 2 * so.getAvatarOrientation().x - 1.0;
		features[7] = 2 * so.getAvatarOrientation().y - 1.0;
		
		return features;
	}
}
