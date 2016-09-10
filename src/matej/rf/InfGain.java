package matej.rf;

import java.util.List;
import java.util.stream.Collectors;
import matej.*;

public class InfGain extends Metric {

	public InfGain(List<Feature> features, List<Instance> instances) {
		super(features, instances);
	}

	@Override
	public double getImpurity(List<Instance> instances) {
		List<String> classes = instances.parallelStream().map(x -> x.output).distinct().collect(Collectors.toList());
		double infGain = 1;
		for (String cls : classes) {
			long count = instances.parallelStream().filter(x -> x.output.equals(cls)).count();
			double pi = (double) count / classes.size();
			infGain -= xlog2x(pi);
		}
		return infGain;
	}

	private static final double log2 = Math.log(2);

	private static double xlog2x(double x) {
		if (x == 0) return 0;
		return x * Math.log(x) / log2;
	}
}
