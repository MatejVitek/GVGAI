package matej;

public class Prediction {
	public final String game;
	public final double confidence;
	
	public Prediction(String predictedGame, double confidence) {
		this.game = predictedGame;
		this.confidence = confidence;
	}
}
