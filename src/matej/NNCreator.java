package matej;

import java.io.*;
import java.util.*;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.*;
import org.neuroph.nnet.*;
import org.neuroph.core.learning.*;
import core.ArcadeMachine;

public class NNCreator {
	public static final String PATH = "datasets/";
	public static final String NAME = "data.csv";
	
	//All public games
	private static final String[] allGames = new String[]{"aliens", "angelsdemons", "assemblyline", "avoidgeorge", "bait", //0-4
	        "blacksmoke", "boloadventures", "bomber", "boulderchase", "boulderdash",      //5-9
	        "brainman", "butterflies", "cakybaky", "camelRace", "catapults",              //10-14
	        "chainreaction", "chase", "chipschallenge", "clusters", "colourescape",       //15-19
	        "chopper", "cookmepasta", "cops", "crossfire", "defem",                       //20-24
	        "defender", "digdug", "dungeon", "eggomania", "enemycitadel",                 //25-29
	        "escape", "factorymanager", "firecaster",  "fireman", "firestorms",           //30-34
	        "freeway", "frogs", "gymkhana", "hungrybirds", "iceandfire",                  //35-39
	        "infection", "intersection", "islands", "jaws", "labyrinth",                  //40-44
	        "labyrinthdual", "lasers", "lasers2", "lemmings", "missilecommand",           //45-49
	        "modality", "overload", "pacman", "painter", "plants",                        //50-54
	        "plaqueattack", "portals", "racebet", "raceBet2", "realportals",              //55-59
	        "realsokoban", "rivers", "roguelike", "run", "seaquest",                      //60-64
	        "sheriff", "shipwreck", "sokoban", "solarfox" ,"superman",                    //65-69
	        "surround", "survivezombies", "tercio", "thecitadel", "thesnowman",           //70-74
	        "waitforbreakfast", "watergame", "waves", "whackamole", "witnessprotection",  //75-79
	        "zelda", "zenpuzzle"};                                                       //80, 81
	private static final String[] games = new String[] {"angelsdemons", "assemblyline", "avoidgeorge",
			"cops", "freeway", "run", "racebet", "thesnowman", "waves", "witnessprotection"};         
	private static final String gamesPath = "examples/gridphysics/";
	private static final String controller = "matej.DataSetAgent";
	private static final int N_SAMPLES = 10;
	
	public static DataSet data;
	public static double[] gameVector;
	
	public static void main(String[] args) throws IOException
    {
	//	createDataSet();
		trainNeuralNetwork();
		testNeuralNetwork();
    }
	
	/**
	 * Runs through all available games and creates a dataset for training.
	 * 
	 * @throws IOException
	 */
	public static void createDataSet() throws IOException {
		data = new DataSet(NNHandler.FEATURE_NAMES.length, games.length);
		data.setColumnNames(Utils.concatenate(NNHandler.FEATURE_NAMES, games));
		
		// run through all levels (0-4) or just a single one for each game?
		boolean levels = false;
		
		for (int k = 0; k < N_SAMPLES; k++) {
			for (int i = 0; i < games.length; i++) {
				String game = gamesPath + games[i] + ".txt";
				gameVector = new double[games.length];
				gameVector[i] = 1;
				
		    	if (levels)
		    		for (int j = 0; j <= 4; j++) {
		    			String level = gamesPath + games[i] + "_lvl" + j + ".txt";
		    			ArcadeMachine.runOneGame(game, level, false, controller, null, new Random().nextInt(), 0);
		    		}
		    	else {
		    		String level = gamesPath + games[i] + "_lvl" + 0 + ".txt";
		    		ArcadeMachine.runOneGame(game, level, false, controller, null, new Random().nextInt(), 0);
				}
			}
		}
		
		gameVector = new double[games.length];
		HashSet<String> checkedGames = new HashSet<String>(Arrays.asList(games));
		
		for (int k = 0; k < N_SAMPLES; k++) {
			int i = new Random().nextInt(allGames.length);
			if (checkedGames.contains(allGames[i])) continue;
			String game = gamesPath + allGames[i] + ".txt";
			
			if (levels)
        		for (int j = 0; j <= 4; j++) {
        			String level = gamesPath + allGames[i] + "_lvl" + j + ".txt";
        			ArcadeMachine.runOneGame(game, level, false, controller, null, new Random().nextInt(), 0);
        		}
        	else {
        		String level = gamesPath + allGames[i] + "_lvl" + 0 + ".txt";
        		ArcadeMachine.runOneGame(game, level, false, controller, null, new Random().nextInt(), 0);
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
    }
	
	public static void trainNeuralNetwork() throws IOException {
		DataSet data = DataSet.load(PATH + NAME);
		
		NeuralNetwork nn = new MultiLayerPerceptron(NNHandler.FEATURE_NAMES.length, 10, games.length);
		IterativeLearning rule = (IterativeLearning) nn.getLearningRule();
		rule.setMaxIterations(10000);
		nn.learn(data);
		
		File f = new File(NNHandler.PATH);
		f.mkdirs();
		nn.save(NNHandler.PATH + NNHandler.NAME);
	}
	
	public static void testNeuralNetwork() throws IOException {
		DataSet data = DataSet.load(PATH + NAME);
		List<DataSetRow> testSet = data.getRows().subList(0, 10);
		NeuralNetwork nn = NeuralNetwork.createFromFile(NNHandler.PATH + NNHandler.NAME);
		for (DataSetRow row : testSet) {
			nn.setInput(row.getInput());
			nn.calculate();
			System.out.print(Utils.argmax(nn.getOutput()) + " " + Utils.argmax(row.getDesiredOutput()));
			System.out.println();
		}
	}
}
