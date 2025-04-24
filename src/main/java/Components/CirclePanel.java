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

public class CirclePanel extends JPanel {
    private int percentage = 100; // Hiển thị cung 19%
     private Color progressColor = Color.decode("#ffffff"); 
    public CirclePanel() {
        this.setPreferredSize(new Dimension(100, 100)); // kích thước panel
        this.setBackground(Color.WHITE); // nền trắng
    }
    public void setBackgroundCircleColor(String color) {
        this.progressColor= Color.decode("#"+color);
        repaint(); // Vẽ lại khi thay đổi màu
    }
    public void setPercentage(int num){
        this.percentage=num;
        repaint();
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Kích hoạt anti-aliasing để vẽ mịn hơn
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int size = Math.min(getWidth(), getHeight()) - 10; // Kích thước hình tròn
        int x = (getWidth() - size) / 2;
        int y = (getHeight() - size) / 2;

        // Vẽ vòng tròn ngoài màu trắng (đường viền nền)
        g2.setStroke(new BasicStroke(8)); // độ dày
        g2.setColor(Color.decode("#ffffff")); // màu xám nhạt gần trắng
        g2.drawOval(x, y, size, size);

        // Vẽ cung màu xanh dương
        g2.setColor(progressColor); // màu xanh kiểu Material Design
        int angle = (int) (360 * (percentage / 100.0));
        g2.drawArc(x, y, size, size, 90, -angle);
    }
}

