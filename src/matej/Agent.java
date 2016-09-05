package matej;

import java.util.*;
import core.game.Observation;
import core.game.StateObservation;
import core.player.AbstractPlayer;
import ontology.Types;
import tools.ElapsedCpuTimer;

public class Agent extends AbstractPlayer {

    public static int NUM_ACTIONS;
    public static int ROLLOUT_DEPTH = 10;
    public static double K = Math.sqrt(2);
    public static Types.ACTIONS[] actions;
    
    private static String[] games = {"angelsdemons", "assemblyline", "avoidgeorge",
		"cops", "freeway", "racebet", "run", "thesnowman", "waves", "witnessprotection"};         
    private String game;
    
	/**
	 * Public constructor with state observation and time due.
	 * @param so state observation of the current game.
	 * @param elapsedTimer Timer for the controller creation.
	 */
	public Agent(StateObservation so, ElapsedCpuTimer elapsedTimer) 
	{
		NNHandler nn = new NNHandler(so, games);
	    game = nn.getPrediction();
	}
	
	
	/**
	 * Picks an action. This function is called every game step to request an
	 * action from the player.
	 * @param stateObs Observation of the current state.
	 * @param elapsedTimer Timer when the action returned is due.
	 * @return An action for the current state
	 */
	@Override
	public Types.ACTIONS act(StateObservation so, ElapsedCpuTimer elapsedTimer) {
	
	    ArrayList<Observation> obs[] = so.getFromAvatarSpritesPositions();
	    ArrayList<Observation> grid[][] = so.getObservationGrid();
	
	    return actions[0];
	}
	
	/**
	 * Function called when the game is over. This method must finish before CompetitionParameters.TEAR_DOWN_TIME,
	 *  or the agent will be DISQUALIFIED
	 * @param stateObservation the game state at the end of the game
	 * @param elapsedCpuTimer timer when this method is meant to finish.
	 */
	public void result(StateObservation stateObservation, ElapsedCpuTimer elapsedCpuTimer)
	{
		// System.out.println("MCTS avg iters: " + SingleMCTSPlayer.iters / SingleMCTSPlayer.num);
		// Include your code here to know how it all ended.
		// System.out.println("Game over? " + stateObservation.isGameOver());
    }
}
