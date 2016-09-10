package matej.nn;

import java.util.*;
import java.io.*;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.*;
import org.neuroph.core.learning.IterativeLearning;
import org.neuroph.nnet.MultiLayerPerceptron;
import core.ArcadeMachine;
import matej.*;

public class NNCreator {

	public static final String PATH = "datasets/nn/";
	public static final String NAME = "extra_with_errors.csv";

	// All public games
	public static final String[] allGames = {"aliens", "boulderdash", "butterflies", "chase", "frogs", "missilecommand", "portals", "sokoban", "survivezombies", "zelda", "camelRace", "digdug",
			"firestorms", "infection", "firecaster", "overload", "pacman", "seaquest", "whackamole", "eggomania", "bait", "boloadventures", "brainman", "chipschallenge", "modality", "painter",
			"realportals", "realsokoban", "thecitadel", "zenpuzzle", "roguelike", "surround", "catapults", "plants", "plaqueattack", "jaws", "labyrinth", "boulderchase", "escape", "lemmings",
			"solarfox", "defender", "enemycitadel", "crossfire", "lasers", "sheriff", "chopper", "superman", "waitforbreakfast", "cakybaky", "lasers2", "hungrybirds", "cookmepasta", "factorymanager",
			"raceBet2", "intersection", "blacksmoke", "iceandfire", "gymkhana", "tercio"}; // 80, 81
	private static final String gamesPath = "examples/gridphysics/";
	private static final String controller = "matej.nn.DataSetAgent";
	private static final int N_SAMPLES = 10;
	private static final Random rnd = new Random();
	private static final double THRESHOLD = 0.1;

	// things for DataSetAgent
	public static DataSet data;
	public static String currentGame;
	public static double[] outputVector;
	public static boolean errors;

	public static void main(String[] args) throws IOException {
		// createDataSet();
		// trainNeuralNetwork();
		testNeuralNetwork();
	}

	/**
	 * Runs through all available games and creates a dataset for training.
	 *
	 * @throws IOException
	 */
	public static void createDataSet() throws IOException {
		// should an instance be created for each level (0-4) or just a single one for each game?
		boolean levels = false;
		// balance the dataset?
		boolean balanced = false;
		// simulate errors for extra attributes?
		errors = true;

		data = new DataSet(ClassificationHandler.FEATURE_NAMES.length, Agent.games.length);
		data.setColumnNames(Utils.concatenate(ClassificationHandler.FEATURE_NAMES, Agent.games));

		int bound = errors || balanced ? N_SAMPLES : 1;
		for (int k = 0; k < bound; k++) {
			for (int i = 0; i < Agent.games.length; i++) {
				String game = gamesPath + Agent.games[i] + ".txt";
				outputVector = new double[Agent.games.length];
				outputVector[i] = 1;
				currentGame = Agent.games[i];

				if (levels)
					for (int j = 0; j <= 4; j++) {
						String level = gamesPath + Agent.games[i] + "_lvl" + j + ".txt";
						ArcadeMachine.runOneGame(game, level, false, controller, null, rnd.nextInt(), 0);
					}
				else {
					String level = gamesPath + Agent.games[i] + "_lvl" + rnd.nextInt(5) + ".txt";
					ArcadeMachine.runOneGame(game, level, false, controller, null, rnd.nextInt(), 0);
				}
			}
		}

		outputVector = new double[Agent.games.length];
		HashSet<String> checkedGames = new HashSet<String>(Arrays.asList(Agent.games));

		bound = balanced ? N_SAMPLES : allGames.length;
		for (int k = 0; k < bound; k++) {
			int i = balanced ? rnd.nextInt(allGames.length) : k;
			if (checkedGames.contains(allGames[i])) {
				if (balanced) k--;
				continue;
			}
			String game = gamesPath + allGames[i] + ".txt";
			currentGame = allGames[i];

			if (levels || errors)
				for (int j = 0; j <= 4; j++) {
					String level = gamesPath + allGames[i] + "_lvl" + j + ".txt";
					ArcadeMachine.runOneGame(game, level, false, controller, null, rnd.nextInt(), 0);
				}
			else {
				String level = gamesPath + allGames[i] + "_lvl" + rnd.nextInt(5) + ".txt";
				ArcadeMachine.runOneGame(game, level, false, controller, null, rnd.nextInt(), 0);
			}
		}

		// save dataset to a file
		File f = new File(PATH);
		f.mkdirs();
		data.save(PATH + NAME);

		// and create a human-readable file
		f = new File(PATH + "readable_" + NAME);
		f.createNewFile();
		BufferedWriter br = new BufferedWriter(new FileWriter(f, false));
		br.write(data.toCSV());
		br.flush();
		br.close();

		// create separate test set
		if (errors) {
			data = new DataSet(ClassificationHandler.FEATURE_NAMES.length, Agent.games.length);
			data.setColumnNames(Utils.concatenate(ClassificationHandler.FEATURE_NAMES, Agent.games));

			for (int k = 0; k < N_SAMPLES; k++) {
				for (int i = 0; i < Agent.games.length; i++) {
					String game = gamesPath + Agent.games[i] + ".txt";
					outputVector = new double[Agent.games.length];
					outputVector[i] = 1;
					currentGame = Agent.games[i];

					if (levels)
						for (int j = 0; j <= 4; j++) {
							String level = gamesPath + Agent.games[i] + "_lvl" + j + ".txt";
							ArcadeMachine.runOneGame(game, level, false, controller, null, rnd.nextInt(), 0);
						}
					else {
						String level = gamesPath + Agent.games[i] + "_lvl" + rnd.nextInt(5) + ".txt";
						ArcadeMachine.runOneGame(game, level, false, controller, null, rnd.nextInt(), 0);
					}
				}
			}

			outputVector = new double[Agent.games.length];

			for (String gameName : allGames) {
				if (checkedGames.contains(gameName)) continue;
				String game = gamesPath + gameName + ".txt";
				currentGame = gameName;

				String level = gamesPath + gameName + "_lvl" + rnd.nextInt(5) + ".txt";
				ArcadeMachine.runOneGame(game, level, false, controller, null, rnd.nextInt(), 0);
			}

			// save dataset to a file
			data.save(PATH + "test_" + NAME);

			// and create a human-readable file
			f = new File(PATH + "readable_test_" + NAME);
			f.createNewFile();
			br = new BufferedWriter(new FileWriter(f, false));
			br.write(data.toCSV());
			br.flush();
			br.close();
		}
	}

	public static void trainNeuralNetwork() throws IOException {
		DataSet data = DataSet.load(PATH + NAME);

		NeuralNetwork nn = new MultiLayerPerceptron(ClassificationHandler.FEATURE_NAMES.length, 10, Agent.games.length);
		IterativeLearning rule = (IterativeLearning) nn.getLearningRule();
		rule.setMaxIterations(10000);
		nn.learn(data);

		File f = new File(NNHandler.PATH);
		f.mkdirs();
		nn.save(NNHandler.PATH + NNHandler.NAME);
	}

	public static void testNeuralNetwork() throws IOException {
		DataSet data;
		try {
			data = DataSet.load(PATH + "test_" + NAME);
		}
		catch (org.neuroph.core.exceptions.NeurophException e) {
			data = DataSet.load(PATH + NAME);
		}
		NeuralNetwork nn = NeuralNetwork.createFromFile(NNHandler.PATH + NNHandler.NAME);
		int correct = 0;
		for (DataSetRow row : data.getRows()) {
			nn.setInput(row.getInput());
			nn.calculate();
			double[] out = nn.getOutput();
			if (Utils.max(out) < THRESHOLD && Utils.allZero(row.getDesiredOutput()) || Utils.max(out) >= THRESHOLD && Utils.argmax(out) == Utils.argmax(row.getDesiredOutput())) correct++;
			System.out.print(Utils.max(nn.getOutput()) + " " + Utils.argmax(nn.getOutput()) + " " + Utils.argmax(row.getDesiredOutput()));
			System.out.println();
		}
		System.out.println("CA: " + 100.0 * correct / data.size());
	}
}
