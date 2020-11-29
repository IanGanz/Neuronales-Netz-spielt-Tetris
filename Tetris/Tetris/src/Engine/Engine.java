package Engine;

import java.awt.Canvas;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

/**
 *
 * @author ian
 */
public abstract class Engine{
    private Display display;
    private double fps;
    private long millisecondsperframe;
    private Eingabe eingabe;
    private Thread thread;
    
    public Engine(float fps,String title, int width, int height){
        display=new Display(title, width, height);
        eingabe=new Eingabe();
        set_fps(fps);
    }
    
    public void set_fps(double fps){
        this.fps=fps;
        millisecondsperframe=(long)(1000/fps);
    }
    public double get_fps() {
    	return fps;
    }
    public Eingabe get_eingabe(){
        return(eingabe);
    }
    public Dimension getsize(){
        return(display.fenster.getsize());
    }
    
    protected abstract void draw(Graphics2D g);
    protected abstract void tick();
    protected abstract void setup();
    
    public void start(){
        if(thread!=null)return;
        thread=new Thread(new Runnable(){
            @Override public void run(){
                setup();
                long timer=0;
                while(true){
                    if(timer>System.currentTimeMillis())
                        try{
                            Thread.sleep(timer-System.currentTimeMillis());
                        }catch(Exception e){
                            System.out.println("Error(Jetzt ist mit dem Thread was richtig schief gelaufen): lKJo3oi8(3");
                        }
                    timer=System.currentTimeMillis()+millisecondsperframe;
                    tick();
                    display.repaint();
                }
            }
        });
        thread.start();
    }
    public void showcursor(boolean showcursor){
        if(showcursor) display.fenster.jframe.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        else display.fenster.jframe.setCursor(java.awt.Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(1,1,BufferedImage.TYPE_4BYTE_ABGR),new java.awt.Point(0,0),"NOCURSOR"));
    }
    
    private class Display{
        private Fenster fenster;
        public Display(String title,int width,int height){
            fenster = new Fenster(title, width, height);
        }
        public void repaint(){
            fenster.repaint();
        }
        private class Fenster{
            JFrame jframe;
            Blatt blatt;
            
            public Fenster(String title, int width, int height){
                jframe=new JFrame();
                jframe.setSize(width,height);
                jframe.setLayout(null);
                jframe.setFocusable(true);
                jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                jframe.setVisible(true);
                jframe.setResizable(true);
                jframe.setTitle(title);
                jframe.setLocationRelativeTo(null);

                blatt=new Blatt();
                blatt.setBounds(0,0,width,height);
                blatt.addKeyListener(new KeyListener() {

                    @Override public void keyPressed(KeyEvent e) {
                        eingabe.Key_pressed(e.getKeyCode());
                    }

                    @Override public void keyReleased(KeyEvent e) {
                        eingabe.Key_released(e.getKeyCode());
                    }

                    @Override public void keyTyped(KeyEvent e) {}
                });
                blatt.addMouseListener(new MouseListener() {
                    @Override public void mousePressed(MouseEvent e) {
                        eingabe.Mouse_pressed();
                    }
                    @Override public void mouseReleased(MouseEvent e) {
                        eingabe.Mouse_released();
                    }
                    @Override public void mouseEntered(MouseEvent e) {}
                    @Override public void mouseExited(MouseEvent e) {}
                    @Override public void mouseClicked(MouseEvent e) {}
                });
                blatt.addMouseMotionListener(new MouseMotionListener() {
                    @Override public void mouseMoved(MouseEvent e) {
                        eingabe.Mouse_newcoords(e.getX(), e.getY());
                    }
                    @Override public void mouseDragged(MouseEvent e) {
                    	eingabe.Mouse_newcoords(e.getX(), e.getY());
                    }
                });
                
                jframe.add(blatt);
                jframe.addComponentListener(new ComponentListener() {
                    @Override public void componentResized(ComponentEvent e) {

                        blatt.setBounds(0,0,jframe.getSize().width,jframe.getSize().height);
                    }
                    @Override public void componentMoved(ComponentEvent e) {}
                    @Override public void componentShown(ComponentEvent e) {}
                    @Override public void componentHidden(ComponentEvent e) {}
                });
                
                jframe.requestFocus();
            }
            public Dimension getsize(){
                return(new Dimension(jframe.getSize().width,jframe.getSize().height-14));
            }
            
            public void repaint(){
                blatt.repaint();
            }

            class Blatt extends Canvas{
				private static final long serialVersionUID = 1L;

				public void repaint(){
                    BufferStrategy bufferstrategy=getBufferStrategy();
                    if(bufferstrategy==null){
                        createBufferStrategy(3);
                        bufferstrategy=getBufferStrategy();
                    }
                    Graphics2D g2d = (Graphics2D) bufferstrategy.getDrawGraphics();
                    draw(g2d);
                    
                    g2d.dispose();
                    bufferstrategy.show();
                }
            }
        }
    }
}
