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
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundedGradientPanel extends JPanel {
    private Color color1;
    private Color color2;
    private int cornerRadius; // Độ bo góc

    public Color getColor1() {
        return color1;
    }

    public void setColor1(Color color1) {
        this.color1 = color1;
    }

    public Color getColor2() {
        return color2;
    }

    public void setColor2(Color color2) {
        this.color2 = color2;
    }
    
    public RoundedGradientPanel(){
        this.cornerRadius=10;
        this.color1=Color.decode("#15B392");
        this.color2=Color.decode("#D2FF72");
    }
    public RoundedGradientPanel(Color color1, Color color2, int cornerRadius) {
        this.color1 = color1;
        this.color2 = color2;
        this.cornerRadius = cornerRadius;
        setOpaque(false); // Để hỗ trợ vẽ bo tròn
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        
        int width = getWidth();
        int height = getHeight();

        // Bật khử răng cưa
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Tạo gradient
        GradientPaint gradient = new GradientPaint(0, 0, color1, width, height, color2);
        g2d.setPaint(gradient);

        // Vẽ hình chữ nhật bo góc
        Shape roundedRect = new RoundRectangle2D.Float(0, 0, width, height, cornerRadius, cornerRadius);
        g2d.fill(roundedRect);

        g2d.dispose(); // Giải phóng tài nguyên đồ họa
    }

   
}
