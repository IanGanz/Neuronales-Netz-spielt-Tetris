package Main;

import java.io.File;

import Neuronal.Activation_function;
import Neuronal.Datamanager;
import Neuronal.Dateien;
import Neuronal.Netz;
import Neuronal.Netzmanager;

public class Tester {
	public static void testen(int[] netzkonfi, double lernrate, Dateien dateien,Activation_function aktivierungsfunktion) {
		Datamanager datamanager = new Datamanager(dateien.get_path()+File.separator+"traingdata");
		String name = "["+netzkonfi[0];
		for(int i=1;i<netzkonfi.length;i++)
			name +=","+netzkonfi[i];
		name += "]"+lernrate;
		if(!dateien.exists(name)){
			dateien.createdirectory(name);
		}
		dateien = new Dateien(dateien.get_path()+File.separator+name);
		
		Netzmanager netzmanager = new Netzmanager(dateien.get_path());
		Netz netz;
		if(netzmanager.netz_exists(0)) {
			netz = netzmanager.load(0, aktivierungsfunktion);
			
		}else {
			netz = new Netz(netzkonfi, aktivierungsfunktion);
			netz.randomise();
		}
		
		int versuche=100;
		double[] ergebnisse = new double[versuche]; 
		for(int i=0;i<versuche;i++) {
			netz.backpropagate(datamanager, lernrate);
			ergebnisse[i] = netz.test(datamanager);
		}
		//save infos
		netzmanager.save(netz, 0);
		
		StringBuilder sb = new StringBuilder();
		if(dateien.exists("data")) {
			String[] old_data = dateien.read("data");
			for(int i=0;i<old_data.length;i++)
				sb.append(old_data[i]+"\n");
		}
		for(int i=0;i<ergebnisse.length;i++)
			sb.append(ergebnisse[i]+"\n");
//		dateien.createfile("data");
		dateien.write("data", new String[] {sb.toString()});
		
	}
}
