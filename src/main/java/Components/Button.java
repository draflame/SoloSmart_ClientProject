/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Components;

/**
 *
 * @author THANH PHU
 */

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;


public class Button extends JButton {

    public Color getEffectColor() {
        return effectColor;
    }

    public void setEffectColor(Color effectColor) {
        this.effectColor = effectColor;
    }


    private int targetSize;
    private float animatSize;
    private Point pressedPoint;
    private float alpha;
    private Color effectColor = new Color(255, 255, 255);
    private boolean  mousePressed;
    public Button() {
        setContentAreaFilled(false);
        setBorder(new EmptyBorder(5, 0, 5, 0));
        setBackground(Color.WHITE);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        //setBorderPainted(false);
        setFocusPainted(false);
        addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            mousePressed = true;
            repaint();
            
            new Thread(() -> {
                try {
                    Thread.sleep(100); // Đợi 100ms rồi phục hồi
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                mousePressed = false;
                repaint();
            }).start();
        }
    }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)){
                    mousePressed=false;
                }
            }
            
            
        });
    }
    
    @Override
    protected void paintComponent(Graphics grphcs) {
        int width = getWidth();
        int height = getHeight();
        
        int shrinkSize = mousePressed ? 10 : 0; 
        int newWidth = width - shrinkSize;
        int newHeight = height - shrinkSize;
        
        int x = (width - newWidth) / 2;
        int y = (height - newHeight) / 2;
        
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if(mousePressed){
            g2.setColor(getBackground().brighter());
            
        }
        else{
            g2.setColor(getBackground());
        }
        g2.fillRoundRect(x, y, newWidth, newHeight, 5, 5);
        
        g2.dispose();
        grphcs.drawImage(img, 0, 0, null);
        super.paintComponent(grphcs);
    }
}