package matej;

import java.util.*;

public class Utils {
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
}
