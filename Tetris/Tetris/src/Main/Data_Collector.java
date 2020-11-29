package Main;

import java.awt.Color;
import java.awt.Graphics2D;

import Engine.Engine;
import Neuronal.Data;
import Neuronal.Datamanager;
import Tetris.Tetris;

public class Data_Collector extends Engine{
	private Tetris tetris;
//	private Netz netz;
	
	private int beginning_ticks_zwischen_bewegung=100;
	private int ticks_zwischen_bewegung=beginning_ticks_zwischen_bewegung;
	private int ticks_zwischen_bewegung_counter;
	private Datamanager datamanager;
	private boolean key_space_pressed;
	private double[] next_input = new double[3];
	private double[] latest_spielsituation;
	
	boolean key_w_pressed;
	boolean key_a_pressed;
	boolean key_s_pressed;
	boolean key_d_pressed;
	boolean key_e_pressed;
	
	public Data_Collector(Datamanager datamanager) {
		super(60, "Tetris", 500, 700);
		this.datamanager = datamanager;
		tetris = new Tetris(10, 14, Tetris.normale_steine(), Tetris.normeler_reward());
		latest_spielsituation = tetris.get_spielsituation();
	}
	
	@Override protected void draw(Graphics2D g) {
		//reset background
		g.setColor(Color.white);
		g.fillRect(0, 0, getsize().width, getsize().height);
		//draw feste Steine
		g.setColor(Color.red);
		for(int i=0;i<tetris.get_feld().length;i++)
			for(int j=0;j<tetris.get_feld()[i].length;j++)
				if(tetris.get_feld()[i][j])
					g.fillRect((int)(i*getsize().width/tetris.get_feld().length),(int) (j*getsize().height/tetris.get_feld()[i].length),(int)getsize().width/tetris.get_feld().length, (int)getsize().height/tetris.get_feld()[i].length);
		//draw fallende Steine
		g.setColor(Color.green);
		for(int i=0;i<tetris.get_steinsatz()[tetris.get_steinnummer()][tetris.get_steinrotation()].length;i++)
			for(int j=0;j<tetris.get_steinsatz()[tetris.get_steinnummer()][tetris.get_steinrotation()][i].length;j++)
				if(tetris.get_steinsatz()[tetris.get_steinnummer()][tetris.get_steinrotation()][i][j])
					g.fillRect((int)((tetris.get_steinposx()+i)*getsize().width/tetris.get_feld().length), (int)((tetris.get_steinposy()+j)*getsize().height/tetris.get_feld()[i].length), (int)getsize().width/tetris.get_feld().length, (int)getsize().height/tetris.get_feld()[i].length);
	}

	@Override protected void tick() {
		if(next_input[0]==0) {
			if(get_eingabe().Key_is_pressed('a')) {tetris.bewegen(-1);if(!key_a_pressed){next_input[0]--;key_a_pressed=true;}}else key_a_pressed=false;     
			if(get_eingabe().Key_is_pressed('d')) {tetris.bewegen(1);if(!key_d_pressed){next_input[0]++;key_d_pressed=true;}}else key_d_pressed=false;
		}
		if(next_input[1]==0) {
			if(get_eingabe().Key_is_pressed('w')) {tetris.drehen(-1);if(!key_w_pressed){next_input[1]--;key_w_pressed=true;}}else key_w_pressed=false;
			if(get_eingabe().Key_is_pressed('s')) {tetris.drehen(1);if(!key_s_pressed){next_input[1]++;key_s_pressed=true;}}else key_s_pressed=false;
		}
		if(get_eingabe().Key_is_pressed('e')) {if(!key_e_pressed){ticks_zwischen_bewegung_counter = ticks_zwischen_bewegung;key_e_pressed=true;}}else key_e_pressed=false;
		
		if(get_eingabe().Key_is_pressed(' ')) {//put block down
			if(!key_space_pressed){
				key_space_pressed=true;
				int turns=tetris.platz_ganz_runter_fallen();
//				if(next_input[0]!=0 | next_input[1]!=0) {
//					tetris.tick(0,0);
//					datamanager.add(new Data(latest_spielsituation, next_input));
//					latest_spielsituation = tetris.get_spielsituation();
//					next_input[0]=0;
//					next_input[1]=0;
//					next_input[2]=0;
//				}
				next_input[2]=1;
				datamanager.add(new Data(latest_spielsituation,next_input));
				next_input[0]=0;
				next_input[1]=0;
				next_input[2]=0;
				
				for(int i=0;i<turns+1;i++) {
					tetris.tick(0,0);
				}
				latest_spielsituation = tetris.get_spielsituation();
				ticks_zwischen_bewegung_counter=(int)(-0.25*ticks_zwischen_bewegung);
			}
		}else key_space_pressed=false;
		
		ticks_zwischen_bewegung_counter++;
		
		
		if(ticks_zwischen_bewegung<ticks_zwischen_bewegung_counter) {
			ticks_zwischen_bewegung_counter=0;			
			
			//savedata
			datamanager.add(new Data(latest_spielsituation, next_input));
			tetris.tick(0,0);
			latest_spielsituation = tetris.get_spielsituation();
			
			//reset input
			next_input[0]=0;
			next_input[1]=0;
			next_input[2]=0;
		}
		
		if(tetris.getlost()) {
			ticks_zwischen_bewegung=beginning_ticks_zwischen_bewegung;
			tetris.reset();
			ticks_zwischen_bewegung_counter=0;
		}
	}

	@Override protected void setup() {}

}
