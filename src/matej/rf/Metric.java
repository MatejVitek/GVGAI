package matej.rf;

import java.util.*;
import java.util.stream.Collectors;
import matej.*;
import tools.Pair;

public abstract class Metric {

	protected List<Feature> features;
	protected List<Instance> instances;

	public Metric(List<Feature> features, List<Instance> instances) {
		this.features = features;
		this.instances = instances;
	}

	public List<Pair<Feature, Split>> rankFeatures() {
		ArrayList<SplitComparable> featureSplits = new ArrayList<>(features.size());

		for (Feature f : features) {
			Split s = split(f);
			double impurity = getImpurity(s.yes) + getImpurity(s.no);
			featureSplits.add(new SplitComparable(f, s, impurity));
		}

		Collections.sort(featureSplits);
		return featureSplits.parallelStream().map(x -> new Pair<>(x.feature, x.split)).collect(Collectors.toList());
	}

	private static class SplitComparable implements Comparable<SplitComparable> {

		public final Feature feature;
		public final Split split;
		public final double impurity;

		public SplitComparable(Feature f, Split s, double imp) {
			feature = f;
			split = s;
			impurity = imp;
		}

		@Override
		public int compareTo(SplitComparable other) {
			return Double.compare(this.impurity, other.impurity);
		}
	}

	public Split split(Feature feature) {
		List<Instance> l1 = new ArrayList<>(), l2 = new ArrayList<>();
		for (Instance inst : instances)
			if (inst.features.get(feature.name).test(feature.value))
				l1.add(inst);
			else l2.add(inst);
		return new Split(l1, l2);
	}

	public abstract double getImpurity(List<Instance> instances);

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (features == null ? 0 : features.hashCode());
		result = prime * result + (instances == null ? 0 : instances.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Metric)) {
			return false;
		}
		Metric other = (Metric) obj;
		if (features == null) {
			if (other.features != null) {
				return false;
			}
		}
		else if (!features.equals(other.features)) {
			return false;
		}
		if (instances == null) {
			if (other.instances != null) {
				return false;
			}
		}
		else if (!instances.equals(other.instances)) {
			return false;
		}
		return true;
	}
}