package Neuronal;

public class Netzmanager {
	private Dateien dateien;
	public Netzmanager(String path) {
		dateien = new Dateien(path);
	}
	public boolean netz_exists(int nummer) {
		return dateien.exists("netz"+nummer);
	}
	public Netz load(int nummer, Activation_function activation_function) {
		return new Netz(dateien.read("netz"+nummer)[0],activation_function);
	}
	public void save(Netz netz, int nummer) {
		dateien.write("netz"+nummer, new String[]{netz.make_string()});
	}
}
