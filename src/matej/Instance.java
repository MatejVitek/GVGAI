package matej;

import static ontology.Types.ACTIONS.ACTION_LEFT;
import static ontology.Types.ACTIONS.ACTION_UP;
import static ontology.Types.ACTIONS.ACTION_USE;
import java.util.*;
import core.game.StateObservation;
import ontology.Types;

public class Instance {

	public static final String TRUE = "Y", FALSE = "N";

	public HashMap<String, Feature> features;
	public String output;

	public Instance(StateObservation so) {
		ArrayList<Types.ACTIONS> actions = so.getAvailableActions();
		features = new HashMap<String, Feature>(ClassificationHandler.FEATURE_NAMES.length);

		// is USE available
		features.put("Use", new BoolFeature("Use", actions.contains(ACTION_USE)));
		// are UP/DOWN available
		features.put("UpDown", new BoolFeature("UpDown", actions.contains(ACTION_UP)));
		// are LEFT/RIGHT available
		features.put("LeftRight", new BoolFeature("LeftRight", actions.contains(ACTION_LEFT)));

		// initial and max HP (normalized for NN)
		features.put("InitHP", new DoubleFeature("InitHP", (double) so.getAvatarHealthPoints() / (double) so.getAvatarLimitHealthPoints()));
		features.put("MaxHP", new DoubleFeature("MaxHP", (double) so.getAvatarLimitHealthPoints() / 1000.0));

		// speed, orientation (already normalized)
		features.put("Speed", new DoubleFeature("Speed", so.getAvatarSpeed()));
		features.put("OrientationX", new DoubleFeature("OrientationX", so.getAvatarOrientation().x));
		features.put("OrientationY", new DoubleFeature("OrientationY", so.getAvatarOrientation().y));

		// TODO: Here there should be extra features from Nejc's classifier
		List<String> nejcFeatures = Arrays.asList(ClassificationHandler.FEATURE_NAMES);
		for (String s : nejcFeatures.subList(nejcFeatures.size() - 9, nejcFeatures.size()))
			features.put(s, new BoolFeature(s, false));

		output = null;
	}

	public Instance(String[] names, String[] values) {
		if (names.length != values.length) throw new org.neuroph.core.exceptions.VectorSizeMismatchException("Sizes of feature names and feature values do not match.");
		features = new HashMap<String, Feature>(values.length);
		if (values.length <= 1) {
			output = (values.length == 1) ? values[0] : null;
			return;
		}

		for (int i = 0; i < values.length - 1; i++) {
			if (values[i].equals(TRUE) || values[i].equals(FALSE)) features.put(names[i], new BoolFeature(names[i], values[i].equals(TRUE)));
			else features.put(names[i], new DoubleFeature(names[i], Double.valueOf(values[i])));
		}

		output = values[values.length - 1];
	}

	public String toCSV(String[] featureNameOrder, String delimiter) {
		StringBuilder sb = new StringBuilder();
		for (String feature : featureNameOrder) {
			if (features.get(feature) instanceof BoolFeature) sb.append((boolean) features.get(feature).value ? TRUE : FALSE);
			else sb.append((double) features.get(feature).value);
			sb.append(delimiter);
		}
		sb.append(output);
		return sb.toString();
	}
}
