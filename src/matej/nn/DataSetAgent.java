package matej.nn;

import java.util.HashMap;
import org.neuroph.core.data.DataSetRow;
import core.game.StateObservation;
import core.player.AbstractPlayer;
import ontology.Types.ACTIONS;
import tools.ElapsedCpuTimer;

public class DataSetAgent extends AbstractPlayer {
	public DataSetAgent(StateObservation so, ElapsedCpuTimer elapsedTimer) throws Exception {
		double[] features = NNHandler.getFeatures(so);
		double[] extras = nejc.get(NNCreator.currentGame);
		if (NNCreator.simulateErrors) simulateErrors(extras);
		System.arraycopy(extras, 0, features, features.length - extras.length, extras.length);
		DataSetRow row = new DataSetRow(features, NNCreator.outputVector);
		NNCreator.data.addRow(row);
	}
	
	@Override
	public ACTIONS act(StateObservation so, ElapsedCpuTimer elapsedTimer) {
		return so.getAvailableActions().get(0);
	}
	
	private static void simulateErrors(double[] features) {
		for (int i = 0; i < features.length; i++)
			if (features[i] == 1 && Math.random() < 1 - recall[i])
				features[i] = 0;
			else if (features[i] == 0 && Math.random() < 1 - npv[i])
				features[i] = 1;
	}
	
	public static final HashMap<String, double[]> nejc;
	public static final double[] recall, npv;
	static {
		nejc = new HashMap<String, double[]>();
		nejc.put("aliens", new double[]{0,1,1,0,0,0,0,0,0});
		nejc.put("boulderdash", new double[]{0,0,1,1,1,0,0,0,0});
		nejc.put("butterflies", new double[]{0,0,0,1,0,0,0,0,0});
		nejc.put("chase", new double[]{0,0,1,0,0,1,0,0,0});
		nejc.put("frogs", new double[]{0,0,0,0,0,0,0,0,0});
		nejc.put("missilecommand", new double[]{0,1,1,0,0,0,0,0,0});
		nejc.put("portals", new double[]{0,0,0,0,0,1,0,0,0});
		nejc.put("sokoban", new double[]{1,0,0,0,0,0,1,0,0});
		nejc.put("survivezombies", new double[]{0,0,0,1,0,0,0,0,0});
		nejc.put("zelda", new double[]{0,0,1,1,0,0,0,0,0});
		nejc.put("camelRace", new double[]{0,0,0,0,0,0,0,0,0});
		nejc.put("digdug", new double[]{0,1,1,1,0,0,0,0,0});
		nejc.put("firestorms", new double[]{0,0,0,1,0,0,0,0,0});
		nejc.put("infection", new double[]{0,0,1,1,0,1,0,0,0});
		nejc.put("firecaster", new double[]{1,0,1,1,0,0,0,0,0});
		nejc.put("overload", new double[]{0,0,1,1,0,0,0,0,0});
		nejc.put("pacman", new double[]{0,0,0,1,0,1,1,0,0});
		nejc.put("seaquest", new double[]{0,1,0,1,0,0,0,0,0});
		nejc.put("whackamole", new double[]{0,0,0,1,0,0,0,0,0});
		nejc.put("eggomania", new double[]{0,1,0,1,0,0,0,0,0});
		nejc.put("bait", new double[]{1,0,0,1,0,0,1,0,0});
		nejc.put("boloadventures", new double[]{1,0,0,0,0,0,1,0,0});
		nejc.put("brainman", new double[]{1,0,0,1,0,0,1,0,0});
		nejc.put("chipschallenge", new double[]{1,0,0,1,0,0,0,0,0});
		nejc.put("modality", new double[]{1,0,0,0,0,1,1,0,0});
		nejc.put("painter", new double[]{1,0,0,0,1,0,0,0,0});
		nejc.put("realportals", new double[]{1,1,0,0,0,1,1,0,0});
		nejc.put("realsokoban", new double[]{1,0,0,0,0,0,1,0,0});
		nejc.put("thecitadel", new double[]{1,0,0,0,0,0,1,0,0});
		nejc.put("zenpuzzle", new double[]{1,0,0,0,1,0,0,0,0});
		nejc.put("roguelike", new double[]{0,0,1,1,0,1,0,0,0});
		nejc.put("surround", new double[]{0,0,0,0,1,0,0,1,0});
		nejc.put("catapults", new double[]{1,0,0,1,0,0,0,0,0});
		nejc.put("plants", new double[]{0,0,0,0,0,0,0,0,1});
		nejc.put("plaqueattack", new double[]{0,1,0,0,0,1,0,0,0});
		nejc.put("jaws", new double[]{0,1,0,1,0,0,0,0,0});
		nejc.put("labyrinth", new double[]{1,0,0,0,0,0,0,0,0});
		nejc.put("boulderchase", new double[]{0,0,1,1,0,0,0,0,0});
		nejc.put("escape", new double[]{1,0,0,0,0,0,1,0,0});
		nejc.put("lemmings", new double[]{0,0,1,0,0,0,0,0,0});
		nejc.put("solarfox", new double[]{0,0,0,1,0,0,0,0,0});
		nejc.put("defender", new double[]{0,1,0,1,0,0,0,0,0});
		nejc.put("enemycitadel", new double[]{0,0,0,0,0,0,1,0,0});
		nejc.put("crossfire", new double[]{0,0,0,0,0,0,0,0,0});
		nejc.put("lasers", new double[]{1,1,0,0,0,0,1,0,0});
		nejc.put("sheriff", new double[]{0,1,0,0,0,0,0,0,0});
		nejc.put("chopper", new double[]{0,1,0,1,0,0,0,0,0});
		nejc.put("superman", new double[]{0,0,0,1,0,0,0,0,0});
		nejc.put("waitforbreakfast", new double[]{0,0,0,0,0,1,0,0,0});
		nejc.put("cakybaky", new double[]{0,0,0,1,0,0,0,0,0});
		nejc.put("lasers2", new double[]{1,1,0,0,0,0,1,0,0});
		nejc.put("hungrybirds", new double[]{1,0,0,1,0,0,0,0,0});
		nejc.put("cookmepasta", new double[]{1,0,0,0,0,0,1,0,0});
		nejc.put("factorymanager", new double[]{1,1,0,0,0,1,1,0,0});
		nejc.put("raceBet2", new double[]{0,0,0,0,0,1,0,0,0});
		nejc.put("intersection", new double[]{0,0,0,1,0,0,0,0,0});
		nejc.put("blacksmoke", new double[]{0,0,1,1,0,0,0,0,0});
		nejc.put("iceandfire", new double[]{1,0,0,1,0,0,0,0,0});
		nejc.put("gymkhana", new double[]{0,0,0,1,0,0,0,0,0});
		nejc.put("tercio", new double[]{1,0,0,0,1,0,1,0,0});
		
		recall = new double[]{0.95, 0.29, 0.88, 0.67, 0.64, 0.93, 0.40, 1.0, 1.0};
		double[] ca = {0.85, 0.83, 0.88, 0.84, 1.00, 0.95, 0.92, 1.0, 1.0};
		npv = new double[recall.length];
		for (int i = 0; i < recall.length; i++) {
			double p1 = 0;
			for (double[] features : nejc.values())
				p1 += features[i];
			p1 /= nejc.size();
			npv[i] = (ca[i] - recall[i] * p1) / (1 - p1);
		}
	}
}