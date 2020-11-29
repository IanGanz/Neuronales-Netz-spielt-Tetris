package Neuronal;

public class LeakyReLu implements Activation_function{

	@Override public double activate(double input) {
		if(input>0)return input;
		return input*-0.1;
	}

	@Override public double derivative(double input) {
		if(input>0)return 1;
		return -0.1;
	}
	
}
