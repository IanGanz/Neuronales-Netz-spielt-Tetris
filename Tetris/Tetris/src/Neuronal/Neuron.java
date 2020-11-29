package Neuronal;

public class Neuron {
	private double wert;
	private double old_wert;
	private Activation_function activation_function;
	
	public Neuron(Activation_function activation_function) {
		this.activation_function=activation_function;
	}
	
	public void reset() {
		wert=0;
		old_wert=0;
	}
	public void set(double wert) {
		this.wert=wert;
	}
	public void add(double add) {
		wert+=add;
	}
	public void funktion() {
//		wert=1d/(1+Math.pow(Math.E,-wert));//sigmoid funktion
		old_wert=wert;
		wert = activation_function.activate(wert);
		
	}
	public double get_wert() {
		return(wert);
	}
	public double get_oldwert() {
		return(old_wert);
	}
	
}
