package matej;

public class DoubleFeature extends Feature<Double> {

	public DoubleFeature(String name, double value) {
		super(name, value);
	}

	@Override
	public boolean test(Double value) {
		return this.value < value;
	}
}
