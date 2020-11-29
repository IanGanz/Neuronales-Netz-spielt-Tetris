package Tetris;

import java.util.ArrayList;
import java.util.Scanner;

import Neuronal.Data;

public class Get_training_data {
	public static Tetris t=new Tetris(10,14, Tetris.normale_steine(), Tetris.normeler_reward());
	private static char key=0;
	public static ArrayList<Data> daten=new ArrayList<Data>();
	
	
	public static void generate_data(int amout) {
		t.reset();
		Scanner sc=new Scanner(System.in);
		sc.nextLine();
		System.out.println("Ready");
		for(int i=0;i<amout;i++) {
			t.print();
			String input=sc.nextLine();
			if(input.length()==0)
				key=' ';
			else
				key=input.toCharArray()[0];
			
			int bewegen=0;
			int drehen=0;
			if(key=='w') {
				drehen=1;
			}
			if(key=='a') {
				bewegen=-1;
			}
			if(key=='s') {
				drehen=-1;
			}
			if(key=='d') {
				bewegen=1;
			}
			daten.add(new Data(t.get_spielsituation(), new double[] {bewegen,drehen}));
			t.tick(bewegen,drehen);
			key=0;
		}
		sc.close();
	}
	
}
