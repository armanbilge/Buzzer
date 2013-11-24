/**
 * Buzzer.java
 *
 * Buzzer
 */
package org.lexingtonma.lhs.nhb;

import java.awt.Color;
import java.awt.DefaultKeyboardFocusManager;
import java.awt.Font;
import java.awt.KeyEventDispatcher;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineEvent.Type;
import javax.sound.sampled.LineListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


/**
 * @author Arman D. Bilge
 *
 */
public class Buzzer extends JFrame implements KeyListener {
    
    private static final long serialVersionUID = 7492374642744742658L;
    private static final String BUZZ_A = "BuzzA.wav";
    private static final String BUZZ_B = "BuzzB.wav";
    private boolean listening = true;
    private final JTextField display = new JTextField(3);
    private final JButton reset = new JButton("Reset");
    
    {
        
        DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager()
            .addKeyEventDispatcher(new KeyEventDispatcher() {
                public boolean dispatchKeyEvent(KeyEvent e) {
                    keyTyped(e);
                    return false;
                }
            });
        setTitle("Buzzer");
        final JPanel panel = new JPanel();
        setSize(256, 162);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        display.setFont(new Font("Helvetica", Font.BOLD, 64));
        display.setForeground(Color.WHITE);
        display.setVisible(true);
        display.setEditable(false);
        display.setHorizontalAlignment(JTextField.CENTER);
        panel.add(display);
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                display.setText("");
                display.setBackground(Color.WHITE);
                listening = true;
                reset.setEnabled(false);
            }
        });
        reset.setEnabled(false);
        panel.add(reset);
        add(panel);
    }
    
    public static final void main(String args[]) {
        Buzzer b = new Buzzer();
        b.setVisible(true);
    }

    public void keyPressed(KeyEvent e) {
        // Do nothing        
    }

    public void keyReleased(KeyEvent e) {
        // Do nothing        
    }

    public void keyTyped(KeyEvent e) {
        final char c = e.getKeyChar();
        if (listening && Character.isLetterOrDigit(c)) {
            listening = false;
            if (Character.isDigit(c)) {
                display.setBackground(Color.RED);
                play(getClass().getResource(BUZZ_A));
            } else {
                display.setBackground(Color.BLUE);
                play(getClass().getResource(BUZZ_B));
            }
            display.setText("" + c);
            reset.setEnabled(true);
        }
        
    }
    
    public void play(URL url) {
        try{
          final Clip clip = AudioSystem.getClip();
          clip.addLineListener(new LineListener() {
              public void update(LineEvent e) {
                  LineEvent.Type type = e.getType();
                  if(type == Type.STOP) clip.close();
              }
          });
          clip.open(AudioSystem.getAudioInputStream(url));
          clip.start();
        } catch(Exception ex){
            JOptionPane.showMessageDialog(this, ex.getLocalizedMessage(), "FATAL ERROR", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
    
}
