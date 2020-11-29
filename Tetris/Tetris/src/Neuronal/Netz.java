package Neuronal;


public class Netz {
//	private int[] neuronenkonfi;
	private Neuron[][] neuronen;
	private Layerverbindung[] layerverbindungen;
	private Activation_function activation_function;
	
//	public static double multiplier = 100000;
	
	public Netz(Neuron[][] neuronen,Layerverbindung[] layerverbindungen, Activation_function activation_function) {
		this.neuronen = neuronen;
		this.layerverbindungen = layerverbindungen;
		this.activation_function = activation_function;
	}
	
	public Netz(int[] neuronenkonfi, Activation_function activation_function) {
		this.activation_function = activation_function;
		neuronen_konfigurieren(neuronenkonfi);
		layerverbindungen_konfigurieren();
	}
	
	public Netz(String dataline, Activation_function activation_function){
		this.activation_function = activation_function;
		
		dataline = dataline.substring(dataline.indexOf('[')+1, dataline.indexOf(']'));
		
		//anzahl Neuronenschichten
		int anzahl_neuronenschichten = Integer.parseInt(dataline.substring(0,dataline.indexOf(',')));
		neuronen = new Neuron[anzahl_neuronenschichten][]; 
		dataline = dataline.substring(dataline.indexOf(',')+1,dataline.length());
		
		//Neuronenschichten
		for(int i=0;i<neuronen.length;i++) {
			neuronen[i] = new Neuron[Integer.parseInt(dataline.substring(0,dataline.indexOf(',')))];
			for(int j=0;j<neuronen[i].length;j++) {
				neuronen[i][j] = new Neuron(activation_function);
			}
			dataline = dataline.substring(dataline.indexOf(',')+1,dataline.length());
		}
		layerverbindungen_konfigurieren();

		//Leitungen
		double[] alleleitungen = Data.get_doublearray(dataline);
		int startpointer=0;
		for(int i=0;i<layerverbindungen.length;i++) {
			double[] leitungen = new double[layerverbindungen[i].get_leitungen_length()];
			for(int j=0;j<leitungen.length;j++) {
				leitungen[j]=alleleitungen[startpointer+j];
			}
			startpointer+=leitungen.length;
			layerverbindungen[i].set_leitungen(leitungen);
		}
		
	}
	public String make_string() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		
		//anzahl Neuronenschichten
		sb.append(neuronen.length);
		
		//Neuronenschichten
		for(int i=0;i<neuronen.length;i++) {
			sb.append(","+neuronen[i].length);
		}
		
		for(int i=0;i<layerverbindungen.length;i++) {
			double[] verbindungen = layerverbindungen[i].get_leitungen();
			for(int j=0;j<verbindungen.length;j++) {
				sb.append(","+verbindungen[j]);
			}
		}
		
		sb.append("]");
		
		return sb.toString();
	}
	
	private void neuronen_konfigurieren(int[] neuronenkonfi) {
		neuronen=new Neuron[neuronenkonfi.length][];
		for(int i=0;i<neuronen.length;i++) {
			neuronen[i]=new Neuron[neuronenkonfi[i]];
			for(int j=0;j<neuronen[i].length;j++) {
				neuronen[i][j]=new Neuron(activation_function);
			}
		}
	}
	private void layerverbindungen_konfigurieren() {
		layerverbindungen=new Layerverbindung[neuronen.length-1];
		for(int i=0;i<layerverbindungen.length;i++) {
			layerverbindungen[i]=new Layerverbindung(neuronen[i], neuronen[i+1]);
		}
	}
	
	
	
	
	public double[] berechnen(double[] input) {
		for(int i=0;i<input.length;i++) {
			neuronen[0][i].set(input[i]);
//			System.out.println(input[i]);
		}
		for(int i=0;i<layerverbindungen.length;i++) {
			layerverbindungen[i].berechnen();
		}
		
		Neuron[] lastneuronlayer=neuronen[neuronen.length-1];
		double[] output = new double[lastneuronlayer.length];
		for(int i=0;i<output.length;i++){
			output[i]=lastneuronlayer[i].get_wert();
		}
		return(output);
	}

	public void backpropagateandadd(double[] input, double[] solloutput, double lernrate) {
		double delta[][]=backpropagate(input, solloutput);
		for(int i_neuronenschicht=0;i_neuronenschicht<neuronen.length-1;i_neuronenschicht++)
			for(int i_from=0; i_from < neuronen[i_neuronenschicht].length;i_from ++)
				for(int i_to=0; i_to < neuronen[i_neuronenschicht+1].length ; i_to++) {
					layerverbindungen[i_neuronenschicht].add_to_leitungswert(i_from, i_to, lernrate * neuronen[i_neuronenschicht][i_from].get_wert() * delta[i_neuronenschicht+1][i_to]);
				}
	}
	
	public double[][] backpropagate(double[] input, double[] solloutput) {
		berechnen(input);
		
		//init delta from Neurons
		double[][] delta = new double[neuronen.length][];
		for(int i=0;i<delta.length;i++) {
			delta[i] = new double[neuronen[i].length];
		}
		
		//calculate delta from output Neurons
		for(int i=0;i<neuronen[neuronen.length-1].length;i++) {
			double outputwert=neuronen[neuronen.length-1][i].get_wert();
			delta[delta.length-1][i] = activation_function.derivative(neuronen[neuronen.length-1][i].get_oldwert()) * (solloutput[i] - outputwert);
//			System.out.println(activation_function.derivative(neuronen[neuronen.length-1][i].get_oldwert())+"/"+neuronen[neuronen.length-1][i].get_oldwert());
		}
		
		//calculate delta from hidden and input Neurons
		for(int i_neuronenschicht=neuronen.length-2 ; i_neuronenschicht>=0 ; i_neuronenschicht--)
			for(int i_neuron=0; i_neuron< neuronen[i_neuronenschicht].length; i_neuron++) {
				delta[i_neuronenschicht][i_neuron] = 0;
				

				for(int i = 0 ; i<neuronen[i_neuronenschicht+1].length ; i++) {//summenzeichen
					delta[i_neuronenschicht][i_neuron]+=delta[i_neuronenschicht+1][i]*layerverbindungen[i_neuronenschicht].get_leitungswert(i_neuron, i);
					//System.out.println(delta[i_neuronenschicht+1][i]*layerverbindungen[i_neuronenschicht].get_leitungswert(i_neuron, i));
				}
				
				delta[i_neuronenschicht][i_neuron] *= activation_function.derivative(neuronen[i_neuronenschicht][i_neuron].get_oldwert());
			}
		
		return delta;
		
	}
	public void backpropagate(Datamanager datamanager,double lernrate) {
		if(datamanager.get_dateienlength()==0) {System.out.println("No Data found: Skipped training");return;}
		int filenummer = 0;
		int filepos = 0;
		Data[] data = datamanager.get_data(0);
		
		//creating delta
		double[][] delta = new double[neuronen.length][];
		for(int i=0;i<delta.length;i++)
			delta[i] = new double[neuronen[i].length];
		
		
		int counter=0;
		while(true) {
			if(counter>=datamanager.get_batchsize()) {
				addup(delta,lernrate);
				counter =0;
			}
			if(data.length == filepos) {//if a new file has to be loaded
				filepos=0;
				filenummer++;
				if(filenummer >= datamanager.get_dateienlength()) {
					break;
				}
				data = datamanager.get_data(filenummer);
			}

			double[][] del= backpropagate(data[filepos].input, data[filepos].output);
			for(int i=0;i<del.length;i++) {
				for(int j=0;j<del[i].length;j++) {
					delta[i][j]+=del[i][j];
				}
			}
			filepos++;
			counter++;
		}
		addup(delta, lernrate);
//		for(int i_neuronenschicht=0;i_neuronenschicht<neuronen.length-1;i_neuronenschicht++)
//			for(int i_from=0; i_from < neuronen[i_neuronenschicht].length;i_from ++) {
//				for(int i_to=0; i_to < neuronen[i_neuronenschicht+1].length ; i_to++) {
//					layerverbindungen[i_neuronenschicht].add_to_leitungswert(i_from, i_to, lernrate * neuronen[i_neuronenschicht][i_from].get_wert() * delta[i_neuronenschicht+1][i_to]);
//				}
//			}
		//System.out.println();
		//this.print_neurons();
	}
	private void addup(double[][] delta, double lernrate) {
		for(int i_neuronenschicht=0;i_neuronenschicht<neuronen.length-1;i_neuronenschicht++)
			for(int i_from=0; i_from < neuronen[i_neuronenschicht].length;i_from ++) {
				for(int i_to=0; i_to < neuronen[i_neuronenschicht+1].length ; i_to++) {
					layerverbindungen[i_neuronenschicht].add_to_leitungswert(i_from, i_to, lernrate * neuronen[i_neuronenschicht][i_from].get_wert() * delta[i_neuronenschicht+1][i_to]);
				}
			}
		for(int i=0;i<delta.length;i++)
			for(int j=0;j<delta[i].length;j++)
				delta[i][j]=0;
	}
	
	public double test(Datamanager datamanager) {
		if(datamanager.get_dateienlength()==0) {System.out.println("No Data found: Skipped testing");return 0;}
		int filenummer = 0;
		int filepos = 0;
		Data[] data = datamanager.get_data(0);
		
		
		
//		double counter=0;
		double fehler=0;
//		System.out.println("anfang");
		while(true) {
			if(data.length ==  filepos) {//if a new file has to be loaded
				filepos=0;
				filenummer++;
				if(filenummer >= datamanager.get_dateienlength()) {
					filenummer=0;
					break;
				}
				data = datamanager.get_data(filenummer);
			}
			double[] output = berechnen(data[filepos].input);
//			if(Math.abs(output[0])>=1)counter += Math.abs(output[0]);//Math.abs(output[1])
//			System.out.println(output[0]+","+output[1]);
			//fehler += Math.abs(output[0] - data[filepos].output[0])+ Math.abs(output[1] - data[filepos].output[1]);
			for(int i=0;i<output.length;i++) {
				fehler += Math.abs(output[i] - data[filepos].output[i]);
//				System.out.println(i+": "+output[i]+","+data[filepos].output[i]);
			}
//			System.out.println(Math.abs(output[0] - data[filepos].output[0]) + Math.abs(output[1] - data[filepos].output[1]));
			filepos++;
		}
//		System.out.println(counter);
		return fehler;
	}
	
	public void variate() {
		for(int i=0;i<layerverbindungen.length;i++) {
			layerverbindungen[i].variate(1);
		}
	}
	public void randomise() {
		for(int i=0;i<layerverbindungen.length;i++) {
			layerverbindungen[i].randomise(1);
		}
	}
	public void override_with(Netz n) {
		for(int i=0;i<neuronen.length;i++) 
			for(int j=0;j<neuronen[i].length;j++)
				neuronen[i][j]=n.neuronen[i][j];
		for(int i=0;i<layerverbindungen.length;i++) {
			layerverbindungen[i].override_with(n.layerverbindungen[i]);
		}
	}
	
	public Neuron[][] get_neuronen(){
		return neuronen;
	}
	public Layerverbindung[] get_layerverbindungen() {
		return layerverbindungen;
	}
	public Activation_function get_activation_function() {
		return activation_function;
	}

	
	public void print_neurons() {
		System.out.println("Neuronenprint start");
		for(int i=0;i<neuronen.length;i++) {
			System.out.println("Schicht ["+i+"]");
			for(int j=0;j<neuronen[i].length;j++) {
				System.out.print("["+j+"] " + neuronen[i][j].get_oldwert() +"--sig-->"+ neuronen[i][j].get_wert() );
			}
			System.out.println();
		}
		System.out.println();
	}
	
}
