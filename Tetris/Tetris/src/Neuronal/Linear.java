package Neuronal;

public class Linear implements Activation_function{
	private static double a=1; 
	@Override public double activate(double input) {
		return input*a;
	}

	@Override public double derivative(double input) {
		return a;
	}
}
