package ibotsmp3player;

import java.awt.event.ActionEvent;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import javax.swing.Timer;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class IBotsMp3Player extends Thread implements java.awt.event.ActionListener {

    Timer timer = new Timer(1,this);
    
    FileInputStream music;
    BufferedInputStream audio;
    public Player player;
    public long pauseLoc;
    public static long totalLength;
    public String fileLoc;

    public static void main(String[] args) {
        UI ui = new UI();
        ui.setVisible(true);

    }

    public void Stop() //Function to stop the music 
    {
        if (player != null) {
            player.close();
            UI.stpBtnClickFlag = false;
        }
        timer.stop();
    }

    public void pauseMusic() //Function to Pause the music
    {
        if (player != null) {
            try {
                pauseLoc = music.available();
                //Thread.wait();
            } catch (IOException ex) {
            }
            player.close();

        }
    }

    public void resumeMusic() //Function to resume the music 
    {
        try {
            music = new FileInputStream(fileLoc);
            audio = new BufferedInputStream(music);
            player = new Player(audio);
            music.skip(totalLength - pauseLoc);

        } 
        catch (Exception ex) {
        }
        new Thread() {
            public void run() {
                try {
                    player.play();
                    if (player.isComplete() && UI.count == 1) //For repeating a music
                    {
                        playMusic(fileLoc);
                    }
                } catch (JavaLayerException ex) {
                }
                catch(NullPointerException ex){
                    
                }
            }
        }.start();
    }

    public void playMusic(String path) //This function plays the music either from start or from previous time
    {
        try {
            music = new FileInputStream(path);
            audio = new BufferedInputStream(music);
            player = new Player(audio);
            totalLength = music.available();
            fileLoc = path + "";

        } 
        catch (Exception ex) {
        }
        timer.start();

        new Thread() {
            public void run() {
                try {
                    player.play();
                    if (player.isComplete() && UI.count == 1) //For repeating a music
                    {
                        playMusic(fileLoc);
                    }
                } catch (JavaLayerException ex) {
                }
                catch(NullPointerException ex){
                    
                }
            }
        }.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try{
                if (UI.MX.player.isComplete()==true && UI.count==0){
                    UI.songplay = false;
                    UI.playpauseBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Play.png")));
                    UI.display.setText("");
                    UI.songTitle.setText("");
                    UI.songavailable = false;
                    Menu.singleSong = false;
                    UI.stpBtnClickFlag = false;
                }
            }catch(NullPointerException ex){
            
            }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
