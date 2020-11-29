package Main;

import Neuronal.*;
import Tetris.*;

public class Main {	
	
	public static void main(String[] arcs) {
		//Datamanager dm = new Datamanager("/Users/ian/Desktop/Tetris/traingdata");
		//new Data_Collector(dm).start();System.exit(1);
		
		//Xor_test();System.exit(1);
		
		int[] netzkombi = new int[]{};
		
		int counter = 0;
		netzkombi = new int[]{};
		Activation_function activationfunction= new Tanh();
		
		
		netzkombi = new int[]{140,100,100,30,20,10,3};
		
		while(counter < 10000) {
			System.out.println("Starting Section "+counter);
			Tester.testen(netzkombi, 0.00001, new Dateien("/Users/ian/Desktop/Tetris"), activationfunction);
			System.out.println("Finished Section "+counter);
			counter++;
		}
		
		Netzmanager nm = new Netzmanager("/Users/ian/Desktop/Tetris/[140,100,100,30,20,10,3]1.0E-5");
		Netz n;
		n = nm.load(0,activationfunction);

		Tetris tetris = new Tetris(10,14,Tetris.normale_steine(),Tetris.normeler_reward());
		tetris.print();
		while(!tetris.getlost()) {
			double[] output = n.berechnen(tetris.get_spielsituation());
			tetris.tick(output);
			System.out.println(output[0]+" / "+output[1]+ "/" +output[2]);
			tetris.print();
		}
		

	}
		
		
		
		
		
	
	
	

	public static void print(double[] x) {
		System.out.print("[");
		if(x.length>0)
			System.out.print(x[0]);
		for(int i=1;i<x.length;i++) {
			System.out.print(","+x[i]);
		}
		System.out.println("]");
	}
	
	public static void Xor_test() {
		int[] Netzkonfiguration = new int[]{2,3,1};
		Netz n = new Netz(Netzkonfiguration, new Sigmoid());
		n.randomise();
		for(int i=0;i<1000000;i++) {
			n.backpropagateandadd(new double[] {0,0} , new double[] {0},1);
			n.backpropagateandadd(new double[] {1,0} , new double[] {1},1);
			n.backpropagateandadd(new double[] {0,1} , new double[] {1},1);
			n.backpropagateandadd(new double[] {1,1} , new double[] {0},1);
		}
		print(n.berechnen(new double[] {0,0}));
		print(n.berechnen(new double[] {1,0}));
		print(n.berechnen(new double[] {0,1}));
		print(n.berechnen(new double[] {1,1})); 
	}
}
