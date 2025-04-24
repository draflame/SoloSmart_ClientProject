package Components;

import javax.swing.*;
import java.awt.*;

public class RoundedRectPanel extends JPanel {
    private String text;
    private Color backgroundColor = Color.WHITE;
    private Color textColor = Color.BLACK;
    private Font font = new Font("SansSerif", Font.BOLD, 16);
    private int arcWidth = 15;
    private int arcHeight = 15;
    private int padding = 2; // khoảng cách để tránh bị cắt viền
    public RoundedRectPanel() {
        this.text = "A.";
        setOpaque(false);
        setPreferredSize(new Dimension(42, 42));
    }
    public RoundedRectPanel(String text) {
        this.text = text;
        setOpaque(false);
        setPreferredSize(new Dimension(42, 42));
    }

    public void setBackgroundColor(Color color) {
        this.backgroundColor = color;
        repaint();
    }

    public void setTextColor(Color color) {
        this.textColor = color;
        repaint();
    }

    public void setFont(Font font) {
        this.font = font;
        repaint();
    }

    public void setText(String text) {
        this.text = text;
        repaint();
    }

    public void setArc(int arcWidth, int arcHeight) {
        this.arcWidth = arcWidth;
        this.arcHeight = arcHeight;
        repaint();
    }

    @Override
    public Insets getInsets() {
        return new Insets(padding, padding, padding, padding);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth() - padding * 2;
        int height = getHeight() - padding * 2;

        // Nền
        g2.setColor(backgroundColor);
        g2.fillRoundRect(padding, padding, width, height, arcWidth, arcHeight);

        // Viền
        g2.setColor(Color.BLACK);
        g2.drawRoundRect(padding, padding, width, height, arcWidth, arcHeight);

        // Chữ
        g2.setFont(font);
        g2.setColor(textColor);
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getAscent();
        int x = (getWidth() - textWidth) / 2;
        int y = (getHeight() + textHeight) / 2 - 3;

        g2.drawString(text, x, y);
        g2.dispose();
    }
}
