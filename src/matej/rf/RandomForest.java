package matej.rf;

import java.io.*;
import java.util.*;
import matej.*;
import tools.ElapsedCpuTimer;

public class RandomForest implements Serializable {

	public static final int DEF_TREES = 100;

	private DecisionTree[] trees;

	public RandomForest() {
		this(DEF_TREES);
	}

	public RandomForest(int nTrees) {
		trees = new DecisionTree[nTrees];
	}

	public void saveToFile(String filePath) {
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath, false));) {
			out.writeObject(this);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static RandomForest loadFromFile(String filePath) {
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath))) {
			return (RandomForest) in.readObject();
		}
		catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public RandomForest train(String filePath, String delimiter) {
		List<Instance> instances = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String[] names = br.readLine().split(delimiter);
			String line;
			while ((line = br.readLine()) != null)
				instances.add(new Instance(names, line.split(delimiter)));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		Instance[] array = new Instance[instances.size()];
		return train(instances.toArray(array));
	}

	public RandomForest train(Instance[] trainingInstances) {
		if (trainingInstances.length == 0) return null;

		Feature[] features = new Feature[trainingInstances[0].features.size()];
		features = trainingInstances[0].features.values().toArray(features);

		for (int i = 0; i < trees.length; i++) {
			int nFeatures;
			if (features.length < 5) nFeatures = features.length;
			else if (features.length < 36) nFeatures = 5;
			else nFeatures = (int) Math.sqrt(features.length);

			List<Feature> featureBag = Utils.sample(features, nFeatures, false);
			List<Instance> instanceBag = Utils.sample(trainingInstances, trainingInstances.length, true);
			trees[i] = new DecisionTree(featureBag);
			trees[i].train(instanceBag);
		}

		return this;
	}

	public Prediction predict(Instance inst) {
		return predict(inst, Long.MAX_VALUE);
	}

	public Prediction predict(Instance inst, long timeInMilliseconds) {
		long time = System.currentTimeMillis();
		HashMap<String, Integer> counter = new HashMap<String, Integer>();
		for (DecisionTree t : trees) {
			if (System.currentTimeMillis() - time > timeInMilliseconds) break;
			String prediction = t.predict(inst);
			if (counter.containsKey(prediction)) counter.put(prediction, counter.get(prediction) + 1);
			else counter.put(prediction, 1);
		}

		int max = Integer.MIN_VALUE;
		String bestPred = null;
		int sum = 0;
		for (String s : counter.keySet()) {
			int current = counter.get(s);
			if (current > max) {
				max = current;
				bestPred = s;
			}
			sum += current;
		}

		return new Prediction(bestPred, (double) max / sum);
	}
}
