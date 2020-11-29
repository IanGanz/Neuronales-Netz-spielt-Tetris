package Tetris;

public class Tetris {
	private boolean[][] feld;
	private int width,height;
	private boolean[][][][] steinsatz;
	
	private int steinnummer;
	private int steinrotation;
	private int steinposx;
	private int steinposy;
	
	private int score;
	private double[] reward;
	//0: Punkte pro gesetzter Block
	//1: Punkte pro volle Reihe
	//2: eine reward pro tick
	
	
	private boolean lost;
	
	public Tetris(int width, int height, boolean[][][][] steinsatz, double[] reward) {
		this.steinsatz=steinsatz;
		this.width=width;
		this.height=height;
		this.reward=reward;
		feld=new boolean[width][height];
		lost=false;
		
		new_block();
	}
	
	public Tetris(Tetris tetris) {
		width = tetris.width;
		height = tetris.height;
		feld = new boolean[width][height];
		for(int i=0;i<feld.length;i++) {
			for(int j=0;j<feld[i].length;j++) {
				feld[i][j] = tetris.feld[i][j];
			}
		}
		steinsatz = tetris.steinsatz;
		steinnummer = tetris.steinnummer;
		steinrotation = tetris.steinrotation;
		steinposx = tetris.steinposx;
		steinposy = tetris.steinposy;
		score = tetris.score;
		reward = tetris.reward;
		lost = tetris.lost;
	}
	
	public void tick(double[] input) {
		tick(input[0],input[1]);
		if(input[2]>=0.5) {
			int fallen = platz_ganz_runter_fallen()+1;
			for(int i=0;i<fallen;i++) {
				tick(0,0);
			}
		}
	}
	public void tick(double bewegen,double drehen) {
		score+=reward[2];
		
		//drehen (positiv (>0.5) -> Urzeigersin; negativ (<0.5) -> gegenurzeigersin)
		//bewegen (positiv (>0.5)-> eines nach rechts; negativ (<0.5) -> eines nach links)
		if(lost)return;
		
		if(Math.abs(drehen)>0.5) {
			if(drehen>0)drehen=1;
			if(drehen<0)drehen=-1;
			drehen((int)drehen);
		}

		if(Math.abs(bewegen)>0.5) {
			if(bewegen>0)bewegen=1;
			if(bewegen<0)bewegen=-1;
		}
		bewegen((int)bewegen);
		if(test_if_block_collides(steinnummer, steinrotation, steinposx, steinposy+1)) {
			festigen();
			delete_full_lines();
			new_block();
		}else {
			steinposy+=1;
		}
	}
	public void drehen(int drehen) {
		int next_steinrotation=(steinrotation+steinsatz[steinnummer].length+drehen)%steinsatz[steinnummer].length;
		if(!test_if_block_collides(steinnummer, next_steinrotation, steinposx,steinposy)){
			steinrotation=next_steinrotation;
		}
	}
	public void bewegen(int bewegen) {
		int next_steinposx=steinposx+(int)bewegen;
//		if(next_steinposx>=0 & next_steinposx <width) {
			if(!test_if_block_collides(steinnummer, steinrotation, next_steinposx,steinposy)){
				steinposx=next_steinposx;
			}
//		}
	}
	
	public boolean getlost() {
		return(lost);
	}
	public int getscore() {
		return(score);
	}
	public double[] get_spielsituation(){
		double[] output=new double[width*height];
		for(int x=0;x<width;x++)
			for(int y=0;y<height;y++)
				if(feld[x][y])
					output[x*height+y]=1;

		boolean[][]stein=steinsatz[steinnummer][steinrotation];
		for(int x=0;x<stein.length;x++)
			for(int y=0;y<stein[x].length;y++)
				if(stein[x][y]) 
					output[(steinposx+x)*height+(steinposy+y)]=-1;
		return output;
	}
	
	public boolean[][] get_feld(){
		return this.feld;
	}
	
	public void reset() {
		lost=false;
		score=0;

		for(int i=0;i<feld.length;i++) {
			for(int j=0;j<feld[i].length;j++) {
				feld[i][j]=false;
			}
		}
		
		new_block();
	}
	public int get_steinnummer() {
		return steinnummer;
	}
	public int get_steinrotation() {
		return steinrotation;
	}
	public int get_steinposx() {
		return steinposx;
	}
	public int get_steinposy() {
		return steinposy;
	}
	public boolean[][][][] get_steinsatz(){
		return steinsatz;
	}
	
	private boolean test_if_block_collides(int steinnummer,int steinrotation, int steinposx,int steinposy) {
		boolean[][]stein=steinsatz[steinnummer][steinrotation];
		
		
		//is in boundary
		for(int x=0;x<stein.length;x++)
			for(int y=0;y<stein[x].length;y++)
				if(stein[x][y]) {
					if(x+steinposx<0 | x+steinposx>=width) return true;
					if(y+steinposy<0 | y+steinposy>=height) return true;
				}
		
		
		//collides with feld
		for(int x=0;x<stein.length;x++)
			for(int y=0;y<stein[x].length;y++)
				if(stein[x][y])
					if(feld[x+steinposx][y+steinposy])
						return true;

		return false;
	}
	private void festigen() {
		boolean[][]stein=steinsatz[steinnummer][steinrotation];
		for(int x=0;x<stein.length;x++) {
			for(int y=0;y<stein[x].length;y++) {
				if(stein[x][y]){
					feld[x+steinposx]
							[y+steinposy]=true;
					score+=reward[0];
				}
			}
		}
	}
	private void delete_full_lines() {
		int linescanning=height-1;
		int linepasting=height-1;
		while(linescanning>=0) {
			if(line_full(linescanning)) {
				linescanning--;
			}else {
				for(int x=0;x<width;x++) {
					feld[x][linepasting]=feld[x][linescanning];
				}
				linescanning--;
				linepasting--;
			}
		}
		//der oberste teil noch löschen
		while(linepasting>=0) {
			score+=reward[1];
			for(int x=0;x<width;x++) {
				feld[x][linepasting]=false;
			}
			linepasting--;
		}
	}
	private boolean line_full(int line) {
		for(int x=0;x<width;x++) {
			if(!feld[x][line])return(false);
		}
		return(true);
	}
	private void new_block(){
		steinnummer=(int)(Math.random()*steinsatz.length);
		steinrotation=(int)(Math.random()*steinsatz[steinnummer].length);
		steinposy=0;
		steinposx=(width-steinsatz[steinnummer][steinrotation].length)/2;
		if(test_if_block_collides(steinnummer, steinrotation, steinposx, steinposy)) {
			lost=true;
		}
	}
	
	public int platz_ganz_runter_fallen() {
		int i=1;
		while(!test_if_block_collides(steinnummer, steinrotation, steinposx, steinposy+i)) {
			i++;
		}
		return(i-1);
			
	}
	
	public void print() {
		System.out.println("score: "+score);
		
		boolean[][]stein=steinsatz[steinnummer][steinrotation];
		
		for(int x=0;x<width+2;x++) {
			System.out.print("_");
		}
		System.out.println();
		
		for(int y=0;y<height;y++) {
			System.out.print("|");
			for(int x=0;x<width;x++) {
				boolean herunterfallend=false;
				for(int i=0;i<stein.length;i++) {
					for(int j=0;j<stein[i].length;j++) {
						if(stein[i][j]) {
							if(i+steinposx==x & j+steinposy==y) {
								System.out.print("o");
								herunterfallend=true;
							}
						}
					}
				}
				if(!herunterfallend) {
					if(feld[x][y])
						System.out.print("x");
					else
						System.out.print(" ");
				}
			}
			System.out.println("|");
		}
		for(int x=0;x<width+2;x++) {
			System.out.print("_");
		}
		System.out.println();
	}
	
	public static boolean[][][][] normale_steine(){
		return(new boolean[][][][] {
			//quadrat
			new boolean[][][] {
				new boolean[][] {
					{true,true},
					{true,true},
				},
			},
			//Stange
			/*
			new boolean[][][] {
				new boolean[][] {
					{false,false,false,false},
					{false,false,false,false},
					{true,true,true,true},
//					{false,false,false,false},
				},
				new boolean[][] {
					{false,false,true,false},
					{false,false,true,false},
					{false,false,true,false},
					{false,false,true,false},
				}
			},
			//L
			new boolean[][][] {
				new boolean[][] {
					{false,true,false},
					{false,true,false},
					{false,true,true},
				},
				new boolean[][] {
					{false,false,true},
					{true,true,true},
					{false,false,false},
				},
				new boolean[][] {
					{true,true,false},
					{false,true,false},
					{false,true,false},
				},
				new boolean[][] {
					{false,false,false},
					{true,true,true},
					{true,false,false},
				},
			},
			//L gespiegelt
			new boolean[][][] {
				new boolean[][] {
					{false,true,false},
					{false,true,false},
					{true,true,false},
				},
				new boolean[][] {
					{true,false,false},
					{true,true,true},
					{false,false,false},
				},
				new boolean[][] {
					{false,true,true},
					{false,true,false},
					{false,true,false},
				},
				new boolean[][] {
					{false,false,false},
					{true,true,true},
					{false,false,true},
				},
			},
			//Treppe nach links
			new boolean[][][] {
				new boolean[][] {
					{true,true,false},
					{false,true,true},
					{false,false,false},
				},
				new boolean[][] {
					{false,true,false},
					{true,true,false},
					{true,false,false},
				},
			},
			//Treppe nach rechts
			new boolean[][][] {
				new boolean[][] {
					{false,true,true},
					{true,true,false},
					{false,false,false},
				},
				new boolean[][] {
					{false,true,false},
					{false,true,true},
					{false,false,true},
				},
			},
			*/
			//T
			new boolean[][][] {
				new boolean[][] {
					{false,false,false},
					{true,true,true},
					{false,true,false},
				},
				new boolean[][] {
					{false,true,false},
					{true,true,false},
					{false,true,false},
				},
				new boolean[][] {
					{false,true,false},
					{true,true,true},
					{false,false,false},
				},
				new boolean[][] {
					{false,true,false},
					{false,true,true},
					{false,true,false},
				},
			},
			
		});
	}
	public static double[] normeler_reward(){
		return(new double[]{1,10,1});
	}
}
