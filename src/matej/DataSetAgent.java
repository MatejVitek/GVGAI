package matej;

import org.neuroph.core.data.DataSetRow;
import core.game.StateObservation;
import core.player.AbstractPlayer;
import ontology.Types.ACTIONS;
import tools.ElapsedCpuTimer;

public class DataSetAgent extends AbstractPlayer {
	public DataSetAgent(StateObservation so, ElapsedCpuTimer elapsedTimer) throws Exception {
		double[] features = NNHandler.getFeatures(so);
		DataSetRow row = new DataSetRow(features, NNCreator.gameVector);
		NNCreator.data.addRow(row);
	}
	
	@Override
	public ACTIONS act(StateObservation so, ElapsedCpuTimer elapsedTimer) {
		return so.getAvailableActions().get(0);
	}
}