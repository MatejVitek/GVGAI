package matej;

import java.io.Serializable;

public abstract class Feature<T extends Serializable> implements Serializable {

	public final String name;
	public final T value;

	public Feature(String name, T value) {
		this.name = name;
		this.value = value;
	}

	public abstract boolean test(T value);
}
