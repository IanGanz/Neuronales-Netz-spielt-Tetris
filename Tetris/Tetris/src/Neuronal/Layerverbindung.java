package Neuronal;

public class Layerverbindung {
	private Neuron[] layer_a;
	private Neuron[] layer_b;
	private double[] leitungen;
	
	public Layerverbindung(Neuron[] layer_a, Neuron[] layer_b) {
		this.layer_a=layer_a;
		this.layer_b=layer_b;
		leitungen=new double[layer_a.length*layer_b.length];
	}
	public Layerverbindung(Neuron[] layer_a, Neuron[] layer_b, double[] leitungen) {
		this.layer_a=layer_a;
		this.layer_b=layer_b;
		this.leitungen=leitungen;
	}
	
	public double[] get_leitungen() {
		return leitungen;
	}
	public int get_leitungen_length() {
		return leitungen.length;
	}
	public void set_leitungen(double[] leitungen) {
		this.leitungen = leitungen;
	}
	public void berechnen() {
		for(int i=0;i<layer_b.length;i++) {
			layer_b[i].reset();
		}
		
		for(int a=0;a<layer_a.length;a++) {
			for(int b=0;b<layer_b.length;b++) {
				layer_b[b].add(layer_a[a].get_wert()*get_leitungswert(a,b));
			}
		}

		for(int i=0;i<layer_b.length;i++) {
			layer_b[i].funktion();
		}

	}
	public double get_leitungswert(int von, int nach) {
		return leitungen[nach*layer_a.length+von];
	}
	public void set_leitungswert(int von, int nach, double wert){
		leitungen[nach*layer_a.length+von]=wert;
	}
	public void add_to_leitungswert(int von, int nach, double wert) {
		leitungen[nach*layer_a.length+von]+=wert;
	}

	
	public void variate(double staerke) {
		for(int i=0;i<leitungen.length;i++) {
			leitungen[i]+=(Math.random()*2-1)*staerke;
		}
	}
	public void randomise(double staerke) {
		for(int i=0;i<leitungen.length;i++) {
			leitungen[i]=(Math.random()*2-1)*staerke;
		}
	}
	public void override_with(Layerverbindung l) {
		for(int i=0;i<leitungen.length;i++) {
			leitungen[i]=l.leitungen[i];
		}
	}
}
