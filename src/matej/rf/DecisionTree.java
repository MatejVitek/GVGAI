package matej.rf;

public class DecisionTree {
	private String[] features;
	private Node root;
	
	public DecisionTree() {
		this(null);
	}
	
	public DecisionTree(String[] featureNames) {
		features = featureNames;
		root = null;
	}

	public void train(Instance[] trainingInstances) {
		if (trainingInstances.length == 0) return;
		if (features == null) {
			features = new String[trainingInstances[0].features.size()];
			features = trainingInstances[0].features.keySet().toArray(features);
		}
		
		
	}

	public String predict(Instance inst) {
		return root.traverse(inst);
	}
	
	
	
	/*
	 * Tree node internal classes
	 */
	
	private static abstract class Node {
		public abstract String traverse(Instance inst);
	}

	private static class Leaf extends Node {
		private String output;
		
		@Override
		public String traverse(Instance inst) {
			return output;
		}
	}
	
	private static abstract class InternalNode extends Node {
		protected String feature;
		protected Node yes, no;
		
		public InternalNode(String featureName, Node yesChild, Node noChild) {
			feature = featureName;
			yes = yesChild;
			no = noChild;
		}

		@Override
		public String traverse(Instance inst) {
			Node child = (boolean) (inst.features.get(feature).value) ? yes : no;
			return child.traverse(inst);
		}
	}
	
	private static class DoubleNode extends InternalNode {
		private double value;
		
		public DoubleNode(String featureName, double val, Node yesChild, Node noChild) {
			super(featureName, yesChild, noChild);
			value = val;
		}

		@Override
		public String traverse(Instance inst) {
			Node child = (double) (inst.features.get(feature).value) < value ? yes : no;
			return child.traverse(inst);
		}
	}
}