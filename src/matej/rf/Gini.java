package matej.rf;

import java.util.*;
import java.util.stream.Collectors;
import matej.*;

public class Gini extends Metric {

	public Gini(List<Feature> features, List<Instance> instances) {
		super(features, instances);
	}

	@Override
	public double getImpurity(List<Instance> instances) {
		List<String> classes = instances.parallelStream().map(x -> x.output).distinct().collect(Collectors.toList());
		double gini = 1;
		for (String cls : classes) {
			long count = instances.parallelStream().filter(x -> x.output.equals(cls)).count();
			double pi = (double) count / classes.size();
			gini -= pi * pi;
		}
		return gini;
	}
}
