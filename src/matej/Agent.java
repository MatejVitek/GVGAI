package matej;

import core.game.StateObservation;
import core.player.AbstractPlayer;
import matej.nn.NNHandler;
import matej.rf.RFHandler;
import ontology.Types;
import tools.ElapsedCpuTimer;

public class Agent extends AbstractPlayer {

	public static final String[] games = {"camelRace", "digdug", "firestorms", "infection", "firecaster", "overload", "pacman", "seaquest", "whackamole", "eggomania"};

	public static int NUM_ACTIONS;
	public static int ROLLOUT_DEPTH = 10;
	public static double K = Math.sqrt(2);
	public static Types.ACTIONS[] actions;
	private Policy policy;

	/**
	 * Public constructor with state observation and time due.
	 * 
	 * @param so
	 *            state observation of the current game.
	 * @param elapsedTimer
	 *            Timer for the controller creation.
	 */
	public Agent(StateObservation so, ElapsedCpuTimer elapsedTimer) {
		ClassificationHandler handler = new RFHandler(so, games);
		String game = handler.getPrediction(elapsedTimer.remainingTimeMillis() - 5).game;

		// in actual implementation all policies should be learned offline and saved via Policy.saveToFile
		// the following constructor call should then be replaced with policy.loadFromFile
		// learning the policy from scratch during the actual agent run may take too long and timeout the agent
		policy = new Policy(game);
	}

	/**
	 * Picks an action. This function is called every game step to request an action from the player.
	 * 
	 * @param stateObs
	 *            Observation of the current state.
	 * @param elapsedTimer
	 *            Timer when the action returned is due.
	 * @return An action for the current state
	 */
	@Override
	public Types.ACTIONS act(StateObservation so, ElapsedCpuTimer elapsedTimer) {
		return policy.getAction(so);
	}

	// TODO: implement policy learning and utilizing
	private static class Policy {

		private String game;

		public Policy(String game) {
			this.game = game;
			learn();
		}

		public void learn() {}

		public Types.ACTIONS getAction(StateObservation so) {
			return so.getAvailableActions().get(0);
		}

		public void saveToFile(String filePath) {}

		public static Policy loadFromFile(String filePath) {
			return null;
		}
	}
}
