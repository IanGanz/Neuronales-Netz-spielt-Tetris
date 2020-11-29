package Neuronal;

public class ReLu implements Activation_function{
	public static double steigung_unter_null=0.1;
	@Override public double activate(double input) {
		if(input>0)return input;
		else return steigung_unter_null*input;
	}

	@Override public double derivative(double input) {
		if(input>0)return 1;
		else return steigung_unter_null;
	}

}
