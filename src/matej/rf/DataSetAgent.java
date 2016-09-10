package matej.rf;

import java.util.*;
import core.game.StateObservation;
import core.player.AbstractPlayer;
import matej.*;
import ontology.Types.ACTIONS;
import tools.ElapsedCpuTimer;

public class DataSetAgent extends AbstractPlayer {

	public static final String[] FEATURES = ClassificationHandler.FEATURE_NAMES;
	private static String game;
	private static Random rnd = new Random();

	public DataSetAgent(StateObservation so, ElapsedCpuTimer elapsedTimer) throws Exception {
		Instance inst = new Instance(so);
		game = RFHandler.currentGame;
		simulateExtraFeatures(inst);
		inst.output = RFHandler.selectedGames.contains(game) ? game : "none";
		RFHandler.writer.write(inst.toCSV(FEATURES, ";"));
		RFHandler.writer.newLine();
	}

	@Override
	public ACTIONS act(StateObservation so, ElapsedCpuTimer elapsedTimer) {
		return so.getAvailableActions().get(0);
	}

	private static void simulateExtraFeatures(Instance inst) {
		String[] features = Arrays.copyOfRange(FEATURES, FEATURES.length - 9, FEATURES.length);
		double[] values = matej.nn.DataSetAgent.nejc.get(game);
		double[] recall = matej.nn.DataSetAgent.recall;
		double[] npv = matej.nn.DataSetAgent.npv;

		for (int i = 0; i < features.length; i++) {
			boolean value;
			if (values[i] == 1) value = !RFHandler.errors || rnd.nextDouble() <= recall[i];
			else value = RFHandler.errors && rnd.nextDouble() > npv[i];
			inst.features.put(features[i], new BoolFeature(features[i], value));
		}
	}
}