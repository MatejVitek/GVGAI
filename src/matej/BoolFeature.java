package matej;

public class BoolFeature extends Feature<Boolean> {

	public BoolFeature(String name, boolean value) {
		super(name, value);
	}

	@Override
	public boolean test(Boolean value) {
		return this.value;
	}
}