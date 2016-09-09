package matej.nn;

import java.util.ArrayList;
import org.neuroph.core.NeuralNetwork;
import core.game.StateObservation;
import matej.ClassificationHandler;
import matej.Prediction;
import ontology.Types;

import static ontology.Types.ACTIONS.*;

public class NNHandler extends ClassificationHandler {

	public static final String PATH = "nns/";
	public static final String NAME = "MLP.nnet";
	
	public NNHandler(StateObservation so, String[] games) {
		super(so, games);
	}

	/**
	 * Predicts which game is most likely currently being played.
	 * 
	 * @return Name of predicted game
	 */
	public Prediction getPrediction() {
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
		return new Prediction(games[maxIndex], max);
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
		features[0] = actions.contains(ACTION_USE) ? 1.0 : 0.0;
		// are UP/DOWN available
		features[1] = actions.contains(ACTION_UP) ? 1.0 : 0.0;
		// are LEFT/RIGHT available
		features[2] = actions.contains(ACTION_LEFT) ? 1.0 : 0.0;

		// initial and max HP (normalized for NN)
		features[3] = (double) so.getAvatarHealthPoints() / (double) so.getAvatarLimitHealthPoints();
		features[4] = (double) so.getAvatarLimitHealthPoints() / 1000.0;
		
		// speed, orientation (already normalized)
		features[5] = so.getAvatarSpeed();
		features[6] = so.getAvatarOrientation().x;
		features[7] = so.getAvatarOrientation().y;
		
		// TODO: Here there should be extra features from Nejc's classifier
		for (int i = 8; i < FEATURE_NAMES.length; i++)
			features[i] = 0.0;
		
		return features;
	}
}
