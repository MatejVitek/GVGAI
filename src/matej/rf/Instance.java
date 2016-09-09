package matej.rf;

import static ontology.Types.ACTIONS.ACTION_LEFT;
import static ontology.Types.ACTIONS.ACTION_UP;
import static ontology.Types.ACTIONS.ACTION_USE;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import core.game.StateObservation;
import matej.nn.NNHandler;
import ontology.Types;

public class Instance {
	private HashMap<String, Object> features = new HashMap<String, Object>(NNHandler.FEATURE_NAMES.length);
	public final String output;
	
	public Instance(StateObservation so) {
		ArrayList<Types.ACTIONS> actions = so.getAvailableActions();

		// is USE available
		features.put("Use", actions.contains(ACTION_USE));
		// are UP/DOWN available
		features.put("UpDown", actions.contains(ACTION_UP));
		// are LEFT/RIGHT available
		features.put("LeftRight", actions.contains(ACTION_LEFT));

		// initial and max HP (normalized for NN)
		features.put("InitHP", (double) so.getAvatarHealthPoints() / (double) so.getAvatarLimitHealthPoints());
		features.put("MaxHP", (double) so.getAvatarLimitHealthPoints() / 1000.0);
		
		// speed, orientation (already normalized)
		features.put("Speed", so.getAvatarSpeed());
		features.put("OrientationX", so.getAvatarOrientation().x);
		features.put("OrientationY", so.getAvatarOrientation().y);
		
		// TODO: Here there should be extra features from Nejc's classifier
		features.put("Puzzle", false);
		features.put("Shooting", false);
		features.put("Destroying", false);
		features.put("Collecting", false);
		features.put("Coloring", false);
		features.put("Positioning", false);
		features.put("Pushing", false);
		features.put("InstantWin", false);
		features.put("Creating", false);
		
		output = null;
	}
	
	public Instance(String[] featureNames, String[] featureList) {
		if (featureNames.length != featureList.length) throw new org.neuroph.core.exceptions.VectorSizeMismatchException("Sizes of feature names and feature list do not match.");
		List<String> doubleNames = Arrays.asList(new String[]{"InitHP", "MaxHP", "Speed", "OrientationX", "OrientationY"});
		for (int i = 0; i < featureNames.length; i++) {
			if (doubleNames.contains(featureNames[i]))
				features.put(featureNames[i], Double.valueOf(featureList[i]));
			else
				features.put(featureNames[i], Double.valueOf(featureList[i]) == 1.0); 
		}
		
		output = featureList[featureList.length - 1];
	}
}
