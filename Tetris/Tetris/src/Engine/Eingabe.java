package Engine;

import java.awt.event.KeyEvent;

/**
 *
 * @author ian
 */
public class Eingabe {
    
    //Tastatur
    private boolean[] Key_tasten=new boolean[1024];
    public void Key_pressed(int keycode){
        Key_tasten[keycode]=true;
    }
    public void Key_released(int keycode){
        Key_tasten[keycode]=false;
    }
    public boolean Key_is_pressed(int keycode){
        return(Key_tasten[keycode]);
    }
    public boolean Key_is_pressed(char Buchstabe){
        return(Key_is_pressed(KeyEvent.getExtendedKeyCodeForChar(Buchstabe)));
    }
    
    //Maus
    public boolean Mouse_pressed;
    public void Mouse_pressed(){
        Mouse_pressed=true;
    }
    public void Mouse_released(){
        Mouse_pressed=false;
    }
    public int Mouse_x=0;
    public int Mouse_y=0;
    public void Mouse_newcoords(int Mouse_x,int Mouse_y){
        this.Mouse_x=Mouse_x;
        this.Mouse_y=Mouse_y;
    }
}