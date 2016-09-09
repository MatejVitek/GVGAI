package matej.rf;

import java.io.*;
import java.util.*;
import matej.*;

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
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath, false));){
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

	public void train(String filePath, String delimiter) {
		ArrayList<Instance> instances = new ArrayList<Instance>();
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
		train(instances.toArray(array));
	}
	
	public void train(Instance[] trainingInstances) {
		if (trainingInstances.length == 0) return;
		
		String[] features = new String[trainingInstances[0].features.size()];
		features = trainingInstances[0].features.keySet().toArray(features);
			
		for (int i = 0; i < trees.length; i++) {
			int nFeatures;
			if (features.length < 5) nFeatures = features.length;
			else if (features.length < 36) nFeatures = 5;
			else nFeatures = (int) Math.sqrt(features.length);
			
			String[] featureBag = new String[nFeatures];
			Instance[] instanceBag = new Instance[trainingInstances.length];
			featureBag = Utils.sample(features, featureBag, false);
			instanceBag = Utils.sample(trainingInstances, instanceBag, true);
			trees[i] = new DecisionTree(featureBag);
			trees[i].train(instanceBag);
		}
	}
	
	public Prediction predict(Instance inst) {
		HashMap<String, Integer> counter = new HashMap<String, Integer>();
		for (DecisionTree t : trees) {
			String prediction = t.predict(inst);
			if (counter.containsKey(prediction))
				counter.put(prediction, counter.get(prediction) + 1);
			else
				counter.put(prediction, 1);
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
