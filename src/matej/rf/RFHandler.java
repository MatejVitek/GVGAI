package matej.rf;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import core.game.StateObservation;
import matej.ClassificationHandler;

public class RFHandler extends ClassificationHandler {
	
	public static final String PATH = "rfs/";
	public static final String NAME = "basic.rf";
	public static final String DSPATH = "datasets/";
	public static final String DSNAME = "rf_extra_with_erorrs.csv";
	
	public RFHandler(StateObservation so, String[] games) {
		super(so, games);
	}
	
	public static void main(String[] args) {
		trainRandomForest();
		testRandomForest();
	}
	
	public static void trainRandomForest() {
		RandomForest rf = new RandomForest();
		rf.train(DSPATH + DSNAME, ";");
		rf.saveToFile(PATH + NAME);
	}
	
	public static void testRandomForest() {
		RandomForest rf = RandomForest.loadFromFile(PATH + NAME);
		try (BufferedReader br = new BufferedReader(new FileReader(DSPATH + "test_" + DSNAME))) {
			String[] names = br.readLine().split(";");
			String line;

			int correct = 0, total = 0;
			while ((line = br.readLine()) != null) {
				Instance inst = new Instance(names, line.split(";"));
				if (rf.predict(inst).equals(inst.output)) correct++;
				total++;
			}
			System.out.println((double) correct / total);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String getPrediction() {
		Instance instance = new Instance(so);
		RandomForest rf = RandomForest.loadFromFile(PATH + NAME);
		return rf.predict(instance);
	}
}
