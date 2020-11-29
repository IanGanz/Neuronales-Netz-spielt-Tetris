package Neuronal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Dateien {
	private String path;
	
	
	public Dateien(String path) {
		this.path=path;
	}
	public void write(String name, String[] inhalt) {
		try {
			FileWriter fw=new FileWriter(new File(path+File.separator+name));
			if(inhalt.length>0)
				fw.write(inhalt[0]);
			for(int i=1;i<inhalt.length;i++)
				fw.write("\n"+inhalt[i]);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public String[] read(String name){
		File file=new File(path+File.separator+name);
		Scanner sc;
		try {
			sc = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return(null);
		}
		ArrayList<String> output=new ArrayList<String>();
		while(sc.hasNextLine()) {
			output.add(sc.nextLine());
		}
		sc.close();
		return output.toArray(new String[output.size()]);
	}
	public boolean exists(String name) {
		File f=new File(path+File.separator+name);
		return f.exists();
	}
	public void createfile(String name){
		File file=new File(path+File.separator+name);
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void createdirectory(String name) {
		File file=new File(path+File.separator+name);
		file.mkdir();
	}
	
	public void delete(String path) {
		File file=new File(this.path+File.separator+path);
		file.delete();
	}
	
	public String get_path() {
		return path;
	}
}
