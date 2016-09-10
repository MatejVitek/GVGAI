package controllers.multiPlayer.sampleMCTS;

import java.util.*;
import core.game.*;
import core.player.AbstractMultiPlayer;
import ontology.Types;
import tools.ElapsedCpuTimer;

/**
 * Created with IntelliJ IDEA. User: ssamot Date: 14/11/13 Time: 21:45 This is a Java port from Tom Schaul's VGDL - https://github.com/schaul/py-vgdl
 */
public class Agent extends AbstractMultiPlayer {

	public static int[] NUM_ACTIONS;
	public static int ROLLOUT_DEPTH = 10;
	public static double K = Math.sqrt(2);
	public static Types.ACTIONS[][] actions;

	public static int id, oppID, no_players;

	/**
	 * Random generator for the agent.
	 */
	private SingleMCTSPlayer mctsPlayer;

	/**
	 * Public constructor with state observation and time due.
	 *
	 * @param so state observation of the current game.
	 * @param elapsedTimer Timer for the controller creation.
	 * @param playerID ID if this agent
	 */
	public Agent(StateObservationMulti so, ElapsedCpuTimer elapsedTimer, int playerID) {
		// get game information

		no_players = so.getNoPlayers();
		id = playerID;
		oppID = (id + 1) % so.getNoPlayers();

		// Get the actions for all players in a static array.

		NUM_ACTIONS = new int[no_players];
		actions = new Types.ACTIONS[no_players][];
		for (int i = 0; i < no_players; i++) {

			ArrayList<Types.ACTIONS> act = so.getAvailableActions(i);

			actions[i] = new Types.ACTIONS[act.size()];
			for (int j = 0; j < act.size(); ++j) {
				actions[i][j] = act.get(j);
			}
			NUM_ACTIONS[i] = actions[i].length;
		}

		// Create the player.
		mctsPlayer = new SingleMCTSPlayer(new Random());
	}

	/**
	 * Picks an action. This function is called every game step to request an action from the player.
	 *
	 * @param stateObs Observation of the current state.
	 * @param elapsedTimer Timer when the action returned is due.
	 * @return An action for the current state
	 */
	@Override
	public Types.ACTIONS act(StateObservationMulti stateObs, ElapsedCpuTimer elapsedTimer) {

		ArrayList<Observation> obs[] = stateObs.getFromAvatarSpritesPositions();
		ArrayList<Observation> grid[][] = stateObs.getObservationGrid();

		// Set the state observation object as the new root of the tree.
		mctsPlayer.init(stateObs);

		// Determine the action using MCTS...
		int action = mctsPlayer.run(elapsedTimer);

		// ... and return it.
		return actions[id][action];
	}

	/**
	 * Function called when the game is over. This method must finish before CompetitionParameters.TEAR_DOWN_TIME, or the agent will be DISQUALIFIED
	 *
	 * @param stateObservation the game state at the end of the game
	 * @param elapsedCpuTimer timer when this method is meant to finish.
	 */
	@Override
	public void result(StateObservation stateObservation, ElapsedCpuTimer elapsedCpuTimer) {
		// System.out.println("MCTS avg iters: " + SingleMCTSPlayer.iters / SingleMCTSPlayer.num);
		// Include your code here to know how it all ended.
		// System.out.println("Game over? " + stateObservation.isGameOver());
	}

}
