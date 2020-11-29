package Neuronal;

public class Sigmoid implements Activation_function{

	@Override public double activate(double input) {
		return 1d/(1+Math.pow(Math.E, -input));
	}

	@Override public double derivative(double input) {
		return activate(input)*(1-activate(input));
	}

}
