package Neuronal;

public class Data {
	double[] input;
	double[] output;
	public Data(double[] input,double[] output) {
		this.input=input;
		this.output=output;
	}
	
	public Data(String dataline) {
		input = get_doublearray(dataline.substring(dataline.indexOf('[')+1,dataline.indexOf(']')));
		output = get_doublearray(dataline.substring(dataline.lastIndexOf('[')+1, dataline.lastIndexOf(']')));
	}
	public String make_string() {
		String dataline="";
		
		dataline+=get_string(input);
		dataline+=get_string(output);
		
		return dataline;
	}
	
	public double[] get_input() {
		return input;
	}
	public double[] get_output() {
		return output;
	}
	
	public static double[] get_doublearray(String array) {
		String[] daten = array.split(",");
		double[] zahlen = new double[daten.length];
		for(int i=0;i<daten.length;i++)
			zahlen[i] = Double.valueOf(daten[i]);
		return zahlen;		
	}
	
	public static double[] OLDget_doublearray(String array) {
		double[] output;
		{
			int counter=0;
			for (int i = 0; i < array.length(); i++)
			    if (array.charAt(i) == ',')
			    	counter ++;
			if(array.equals(""))
				output = new double[0];
			else
				output = new double[counter+1];
		}
		System.out.println("start");
		int counter =0;
		for(int i=0;i<output.length;i++) {
			int next_komma;
			if(i+1<output.length)
				next_komma = array.indexOf(',');
			else
				next_komma=array.length();
			output[i]=Double.valueOf(array.substring(0,next_komma));
			array=array.substring(next_komma+1,array.length());
			
			counter++;
			if(counter%1000==0)System.out.println("counter: "+counter);
		}
		System.out.println("stop");
		
		return output;
	}
	private static String get_string(double[] array) {
		String output = "[";
		if(array.length>0)output+=array[0];
			for(int i=1;i<array.length;i++)
				output+=","+array[i];
		output+="]";
		return output;
	}
}
