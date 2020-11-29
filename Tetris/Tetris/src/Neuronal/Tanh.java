package Neuronal;

public class Tanh implements Activation_function{

	@Override public double activate(double input) {
		return Math.tanh(input);
	}

	@Override public double derivative(double input) {
		return 2d/(Math.cosh(2*input)+1);
	}

}
