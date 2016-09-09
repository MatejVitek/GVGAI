package matej.rf;

import java.io.*;
import java.util.ArrayList;

public class RandomForest implements Serializable {
	
	public RandomForest() {
		// TODO
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
		// TODO
	}
	
	public String predict(Instance inst) {
		// TODO
		return null;
	}
}
