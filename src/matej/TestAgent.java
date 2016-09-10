package matej;

import core.game.StateObservation;
import core.player.AbstractPlayer;
import ontology.Types.ACTIONS;
import tools.ElapsedCpuTimer;

public class TestAgent extends AbstractPlayer {

	public TestAgent(StateObservation so, ElapsedCpuTimer elapsedTimer) throws Exception {

	}

	@Override
	public ACTIONS act(StateObservation so, ElapsedCpuTimer elapsedTimer) {
		return so.getAvailableActions().get(0);
	}
}