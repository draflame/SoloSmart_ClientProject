/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Components;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author THANH PHU
 */
public class RoundedPanel extends JPanel{
    private static  int radius=10;
    public RoundedPanel(){
        setOpaque(false);
    }
    public RoundedPanel(int radius){
        setOpaque(false);
        this.radius=radius;
        revalidate();
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new Color(0, 0, 0, 50)); // Màu shadow (màu đen mờ)
        g2.fillRoundRect(5, 5, getWidth() - 10, getHeight() - 10, radius, radius); // Vẽ shadow phía dưới và bên phải

        
        // Vẽ nền bo tròn
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        g2.dispose();
    }
}
