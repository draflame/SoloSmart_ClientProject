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

public class CustomTextArea extends JTextArea {
    private Color backgroundColor = new Color(0xeeeefc); // Màu nền #eeeefc
    private Color borderColor = Color.RED; // Màu viền nhạt (có thể tùy chỉnh)
    private int borderRadius = 20;
    private String placeholder = "";
    private Color placeholderColor = new Color(150, 150, 150);
    private boolean showPlaceholder = true;

    public CustomTextArea() {
        setOpaque(false); // Để phần nền trong suốt, ta sẽ tự vẽ
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding
        setLineWrap(true);
        setWrapStyleWord(true);

        getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                updatePlaceholderVisibility();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                updatePlaceholderVisibility();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                // Không cần xử lý cho thuộc tính văn bản thuần túy
            }
        });

        updatePlaceholderVisibility();
        
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        repaint();
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        repaint();
    }

    public int getBorderRadius() {
        return borderRadius;
    }

    public void setBorderRadius(int borderRadius) {
        this.borderRadius = borderRadius;
        repaint();
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        updatePlaceholderVisibility();
        repaint();
    }

    public Color getPlaceholderColor() {
        return placeholderColor;
    }

    public void setPlaceholderColor(Color placeholderColor) {
        this.placeholderColor = placeholderColor;
        repaint();
    }

    private void updatePlaceholderVisibility() {
        showPlaceholder = getText().isEmpty() && !placeholder.isEmpty();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        // Khử răng cưa
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Nền màu trắng, bo góc 20px
        Shape rounded = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20);
        g2.setColor(Color.WHITE); 
        g2.fill(rounded);

        // Vẽ viền (tùy chọn)
        g2.setColor(Color.LIGHT_GRAY);
        g2.draw(rounded);

        g2.dispose();
        super.paintComponent(g);
    }

}
