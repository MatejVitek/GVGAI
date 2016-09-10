package matej;

import java.util.*;
import matej.rf.DecisionTree;

public class Utils {

	private static final Random rnd = new Random();

	public static double[] concatenate(double[] a, double[] b) {
		double[] result = new double[a.length + b.length];
		System.arraycopy(a, 0, result, 0, a.length);
		System.arraycopy(b, 0, result, a.length, b.length);
		return result;
	}

	public static <T> T[] concatenate(T[] a, T[] b) {
		T[] result = Arrays.copyOf(a, a.length + b.length);
		System.arraycopy(b, 0, result, a.length, b.length);
		return result;
	}

	public static int argmax(double[] arr) {
		int maxIndex = 0;
		double max = Double.NEGATIVE_INFINITY;
		for (int i = 0; i < arr.length; i++)
			if (arr[i] > max) {
				maxIndex = i;
				max = arr[i];
			}
		return maxIndex;
	}

	public static double max(double[] arr) {
		double max = Double.NEGATIVE_INFINITY;
		for (int i = 0; i < arr.length; i++)
			if (arr[i] > max) max = arr[i];
		return max;
	}

	public static boolean allZero(double[] out) {
		for (double d : out)
			if (d != 0.0) return false;
		return true;
	}

	public static <T> List<T> sample(T[] src, int n, boolean replacement) {
		if (replacement) {
			List<T> list = new ArrayList<>(n);
			for (int i = 0; i < n; i++)
				list.add(src[rnd.nextInt(src.length)]);
			return list;
		}
		else {
			List<T> list = new ArrayList<>(Arrays.asList(src));
			Collections.shuffle(list);
			return list.subList(0, n);
		}
	}

	public static <T> List<T> sample(List<T> src, int n, boolean replacement) {
		if (replacement) {
			List<T> list = new ArrayList<>(n);
			for (int i = 0; i < n; i++)
				list.add(src.get(rnd.nextInt(src.size())));
			return list;
		}
		else {
			List<T> list = new ArrayList<>(src);
			Collections.shuffle(list);
			return list.subList(0, n);
		}
	}

	public static String getMajorityClass(List<Instance> instances) {
		HashMap<String, Integer> counter = new HashMap<String, Integer>();
		for (Instance inst : instances) {
			String cls = inst.output;
			if (counter.containsKey(cls)) counter.put(cls, counter.get(cls) + 1);
			else counter.put(cls, 1);
		}

		int max = Integer.MIN_VALUE;
		String cls = null;
		for (String s : counter.keySet()) {
			int current = counter.get(s);
			if (current > max) {
				max = current;
				cls = s;
			}
		}
		return cls;
	}
}
