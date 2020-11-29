package Neuronal;

public class Datamanager {
	private static String infofile="info";
	private static String data_file_name="Data";
	private static String next_dateinummer_in_infofile="Next Dateinummer:";
	
	private Dateien dateien;
	private int data_in_file=100;
	private int next_dateinummer;
	private int batchsize = 1;
	
	public Datamanager(String path) {
		dateien=new Dateien(path);
		setup();
	}
	public void setup() {
		if(info_file_exists())
			scan_infofile();
		else
			create_new_infofile();
	}
	
	public void add(Data adddata) {
		if(!dateien.exists(data_file_name + next_dateinummer))
			dateien.createfile(data_file_name + next_dateinummer);
		String[] data = dateien.read(data_file_name + next_dateinummer);
		{
			String[] save_read_data = data;
			data = new String[data.length+1];
			for(int i=0;i<save_read_data.length;i++)
				data[i] = save_read_data[i];
			data[data.length-1] = adddata.make_string();
		}
		dateien.write(data_file_name + next_dateinummer, data);
		if(data.length > data_in_file) {
			next_dateinummer++;
			update_info_file();
		}
	}
	public Data[] get_data(int filenummer) {
		String[] data_in_string = dateien.read(data_file_name+filenummer);
		//TODO verschnellern
		Data[] data = new Data[data_in_string.length];
		for(int i=0;i<data_in_string.length;i++) {
			data[i] = new Data(data_in_string[i]); 
		}
		return data;
	}
	
	public int get_dateienlength() {
		return next_dateinummer+1;
	}
	
	public void set_batchsize(int batchsize) {
		this.batchsize=batchsize;
	}
	public int get_batchsize() {
		return batchsize;
	}
	
	//infofile
	public boolean info_file_exists() {
		return dateien.exists(infofile);
	}
	public void scan_infofile() {
		String[] infos=dateien.read(infofile);
		next_dateinummer=Integer.valueOf(infos[0].substring(infos[0].indexOf(next_dateinummer_in_infofile)+next_dateinummer_in_infofile.length(), infos[0].length()));
	}
	public void create_new_infofile() {
		dateien.createfile(infofile);
		update_info_file();
	}
	public void update_info_file() {
		dateien.write(infofile, new String[] {next_dateinummer_in_infofile+next_dateinummer});
	}
}
