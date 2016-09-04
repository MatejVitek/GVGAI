package matej;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import org.neuroph.core.NeuralNetwork;
import core.game.StateObservation;
import ontology.Types;
import static ontology.Types.ACTIONS.*;

public class NNHandler {
	private static final String PATH = "nns\\";

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
		Collections.shuffle(Arrays.asList(games));
	    
		double[] features = getFeatures(so);
		double[] predictions = new double[games.length];

		NeuralNetwork nn = NeuralNetwork.createFromFile(PATH + "MLP.nnet");
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
	private double[] getFeatures(StateObservation so) {
		ArrayList<Double> features = new ArrayList<Double>();
		ArrayList<Types.ACTIONS> actions = so.getAvailableActions();

		// is USE available
		features.add(actions.contains(ACTION_USE) ? 1.0 : 0.0);
		// are UP/DOWN available
		features.add(actions.contains(ACTION_UP) ? 1.0 : 0.0);
		// are LEFT/RIGHT available
		features.add(actions.contains(ACTION_LEFT) ? 1.0 : 0.0);

		// initial and max HP (normalized for NN)
		features.add((double) so.getAvatarHealthPoints() / (double) so.getAvatarLimitHealthPoints());
		features.add((double) so.getAvatarLimitHealthPoints() / 1000.0);
		
		// speed, orientation (already normalized)
		features.add(so.getAvatarSpeed());
		features.add(so.getAvatarOrientation().x);
		features.add(so.getAvatarOrientation().y);

		double[] result = new double[features.size()];
		for (int i = 0; i < result.length; i++)
			result[i] = features.get(i);
		return result;
	}
}
