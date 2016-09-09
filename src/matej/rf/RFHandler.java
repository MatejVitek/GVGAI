package matej.rf;

import java.io.*;
import java.util.*;
import core.ArcadeMachine;
import core.game.StateObservation;
import matej.*;

public class RFHandler extends ClassificationHandler {
	
	public static final String RF_PATH = "rfs/basic.rf";
	public static final String DS_PATH = "datasets/rf/extra_with_errors.csv";
	public static final String TEST_DS_PATH = "datasets/rf/extra_with_errors_test.csv";
	
	public RFHandler(StateObservation so, String[] games) {
		super(so, games);
	}
	
	@Override
	public Prediction getPrediction() {
		Instance instance = new Instance(so);
		RandomForest rf = RandomForest.loadFromFile(RF_PATH);
		return rf.predict(instance);
	}
	
	
	
	/*
	 * Functions for handling the data set and training/testing the RF classifier.
	 */
	
	public static void main(String[] args) {
		createDataSet();
		//trainRandomForest();
		//testRandomForest();
	}
	
	//All public games
	public static final String[] allGames = {"aliens", "boulderdash",
			"butterflies", "chase", "frogs", "missilecommand", "portals", "sokoban",
			"survivezombies", "zelda", "camelRace", "digdug", "firestorms", "infection",
			"firecaster", "overload", "pacman", "seaquest", "whackamole", "eggomania", "bait",
			"boloadventures", "brainman", "chipschallenge", "modality", "painter", "realportals",
			"realsokoban", "thecitadel", "zenpuzzle", "roguelike", "surround", "catapults", "plants",
			"plaqueattack", "jaws", "labyrinth", "boulderchase", "escape", "lemmings", "solarfox",
			"defender", "enemycitadel", "crossfire", "lasers", "sheriff", "chopper", "superman",
			"waitforbreakfast", "cakybaky", "lasers2", "hungrybirds", "cookmepasta", "factorymanager",
			"raceBet2", "intersection", "blacksmoke", "iceandfire", "gymkhana", "tercio"};                                                       //80, 81
	public static final HashSet<String> selectedGames = new HashSet<String>(Arrays.asList(Agent.games));
	private static final String gamesPath = "examples/gridphysics/";
	private static final String controller = "matej.rf.DataSetAgent";
	private static final int N_SAMPLES = 10;
	private static final Random rnd = new Random();
	
	// things for DataSetAgent
	public static BufferedWriter writer;
	public static String currentGame;
	public static boolean errors;
	
	/**
	 * Runs through all available games and creates a dataset for training.
	 */
	public static void createDataSet() {
		// should an instance be created for each level (0-4) or just a single one for each game?
		boolean levels = false;
		// balance the dataset?
		boolean balanced = false;
		// simulate errors for extra attributes?
		errors = true;
		
		StringBuilder sb = new StringBuilder();
		for (String s : ClassificationHandler.FEATURE_NAMES)
			sb.append(s + ";");
		sb.append("Game");
		String headers = sb.toString();
		
		try (BufferedWriter tmp = new BufferedWriter(new FileWriter(DS_PATH, false))) {
			writer = tmp;
			writer.write(headers);
			writer.newLine();
			
			int bound = (errors || balanced) ? N_SAMPLES : 1;
			for (int k = 0; k < bound; k++) {
				for (int i = 0; i < Agent.games.length; i++) {
					String game = gamesPath + Agent.games[i] + ".txt";
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
			
			bound = balanced ? N_SAMPLES : allGames.length;
			for (int k = 0; k < bound; k++) {
				int i = balanced ? rnd.nextInt(allGames.length) : k;
				if (selectedGames.contains(allGames[i])) {
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
		}
		catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		// create separate test set
		if (errors) {
			try (BufferedWriter tmp = new BufferedWriter(new FileWriter(TEST_DS_PATH, false))) {
				writer = tmp;
				writer.write(headers);
				writer.newLine();
				
				for (int k = 0; k < N_SAMPLES; k++) {
					for (int i = 0; i < Agent.games.length; i++) {
						String game = gamesPath + Agent.games[i] + ".txt";
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
	
				for (int i = 0; i < allGames.length; i++) {
					if (selectedGames.contains(allGames[i]))
						continue;
					String game = gamesPath + allGames[i] + ".txt";
					currentGame = allGames[i];
					
					String level = gamesPath + allGames[i] + "_lvl" + rnd.nextInt(5) + ".txt";
		        	ArcadeMachine.runOneGame(game, level, false, controller, null, rnd.nextInt(), 0);
		    	}
			}
			catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
    }
	
	public static void trainRandomForest() {
		RandomForest rf = new RandomForest();
		rf.train(DS_PATH, ";");
		rf.saveToFile(RF_PATH);
	}
	
	public static void testRandomForest() {
		RandomForest rf = RandomForest.loadFromFile(RF_PATH);
		try (BufferedReader br = new BufferedReader(new FileReader(TEST_DS_PATH))) {
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
}
