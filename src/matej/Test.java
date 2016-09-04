package matej;

import java.util.Random;
import core.ArcadeMachine;

public class Test {
	public static void main(String[] args)
    {
        //All public games
        String[] allGames = new String[]{"aliens", "angelsdemons", "assemblyline", "avoidgeorge", "bait", //0-4
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
                "zelda", "zenpuzzle" };                                                       //80, 81
        String[] games = new String[] {"angelsdemons", "assemblyline", "avoidgeorge",
    			"cops", "freeway", "run", "thesnowman", "waves", "witnessprotection"};         
        String gamesPath = "examples/gridphysics/";
        
        games = allGames;
        boolean levels = true;
        
        String controller = "matej.TestAgent";
        for (int i = 0; i < games.length; i++) {
        	String game = gamesPath + games[i] + ".txt";
        	if (levels)
        		for (int j = 0; j <= 4; j++) {
        			System.out.println(games[i] + " level " + j + ":");
        			String level = gamesPath + games[i] + "_lvl" + j + ".txt";
        			ArcadeMachine.runOneGame(game, level, false, controller, null, new Random().nextInt(), 0);
        			System.out.println();
        		}
        	else {
        		System.out.println(games[i] + ":");
        		String level = gamesPath + games[i] + "_lvl" + 0 + ".txt";
        		ArcadeMachine.runOneGame(game, level, false, controller, null, new Random().nextInt(), 0);
    			System.out.println();
        	}
        }
        // 3. This replays a game from an action file previously recorded
        //String readActionsFile = recordActionsFile;
        //ArcadeMachine.replayGame(game, level1, visuals, readActionsFile);

        // 4. This plays a single game, in N levels, M times :
//        String level2 = gamesPath + games[gameIdx] + "_lvl" + 1 +".txt";
//        int M = 10;
//        for(int i=0; i<games.length; i++){
//        	game = gamesPath + games[i] + ".txt";
//        	level1 = gamesPath + games[i] + "_lvl" + levelIdx +".txt";
//        	ArcadeMachine.runGames(game, new String[]{level1}, M, sampleMCTSController, null);
//        }
        
        //5. This starts a game, in a generated level created by a specific level generator

        //if(ArcadeMachine.generateOneLevel(game, randomLevelGenerator, recordLevelFile)){
        //	ArcadeMachine.playOneGeneratedLevel(game, recordActionsFile, recordLevelFile, seed);
        //}
        
        //6. This plays N games, in the first L levels, M times each. Actions to file optional (set saveActions to true).
//        int N = 82, L = 5, M = 1;
//        boolean saveActions = false;
//        String[] levels = new String[L];
//        String[] actionFiles = new String[L*M];
//        for(int i = 0; i < N; ++i)
//        {
//            int actionIdx = 0;
//            game = gamesPath + games[i] + ".txt";
//            for(int j = 0; j < L; ++j){
//                levels[j] = gamesPath + games[i] + "_lvl" + j +".txt";
//                if(saveActions) for(int k = 0; k < M; ++k)
//                    actionFiles[actionIdx++] = "actions_game_" + i + "_level_" + j + "_" + k + ".txt";
//            }
//            ArcadeMachine.runGames(game, levels, M, sampleMCTSController, saveActions? actionFiles:null);
//        }
    }
}
