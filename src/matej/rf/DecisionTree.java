package matej.rf;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import matej.*;
import tools.Pair;

public class DecisionTree implements Serializable {

	private transient List<Feature> features;
	private Node root;

	public DecisionTree() {
		this(null);
	}

	public DecisionTree(List<Feature> features) {
		this.features = features;
		root = null;
	}

	public DecisionTree train(List<Instance> trainingInstances) {
		if (trainingInstances.size() == 0) return null;
		if (features == null) features = new ArrayList<>(trainingInstances.get(0).features.values());

		root = buildNode(trainingInstances);
		return this;
	}

	private Node buildNode(List<Instance> instances) {
		if (pure(instances)) return new Leaf(instances.get(0).output);

		else {
			// add separate features for each possible point of splitting in continuous variables
			List<Feature> extendedFeatures = new LinkedList<>();
			for (Feature feature : features)
				if (feature instanceof BoolFeature) extendedFeatures.add(feature);
				else extendedFeatures.addAll(instances.parallelStream().map(x -> x.features.get(feature.name)).distinct().collect(Collectors.toList()));

			// rank features in order of best split to worst
			Metric metric = new Gini(extendedFeatures, instances);
			List<Pair<Feature, Split>> features = metric.rankFeatures();

			// while there are still features to try
			for (Pair<Feature, Split> f : features)
				// if neither child is empty, create an internal node
				if (!f.second.yes.isEmpty() && !f.second.no.isEmpty()) {
					Node yesChild = buildNode(f.second.yes);
					Node noChild = buildNode(f.second.no);
					return new InternalNode(f.first, yesChild, noChild);
				}

			// no feature achieved a successful split - we have an impure leaf
			return new Leaf(Utils.getMajorityClass(instances));
		}
	}

	private static boolean pure(List<Instance> instances) {
		if (instances.size() == 0) return true;
		String output = instances.get(0).output;
		return instances.parallelStream().allMatch(x -> x.equals(output));
	}

	public String predict(Instance inst) {
		return root.traverse(inst);
	}

	/*
	 * Tree node internal classes
	 */

	private static abstract class Node implements Serializable {

		public abstract String traverse(Instance inst);
	}

	private static class Leaf extends Node {

		private String output;

		public Leaf(String out) {
			output = out;
		}

		@Override
		public String traverse(Instance inst) {
			return output;
		}
	}

	private static class InternalNode extends Node {

		protected Feature feature;
		protected Node yes, no;

		public InternalNode(Feature f, Node yesChild, Node noChild) {
			feature = f;
			yes = yesChild;
			no = noChild;
		}

		@Override
		public String traverse(Instance inst) {
			Node child = (inst.features.get(feature.name).test(feature.value)) ? yes : no;
			return child.traverse(inst);
		}
	}
}