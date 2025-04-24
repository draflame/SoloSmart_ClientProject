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

public class RatioExam extends JRadioButton {
    private String circleText;     // text bên trong hình tròn (ví dụ: "A")
    private String answerText;     // nội dung bên phải hình tròn (ví dụ: "Đáp án 1")
    private String color="#3a8a7d";
    public RatioExam() {
        super();
        this.circleText = "A";
        this.answerText = "Đáp án";
        setOpaque(false);
        setFocusPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(300, 60)); // điều chỉnh kích thước đủ chứa text
        setText(null);
    }
    public RatioExam(String circleText, String answerText) {
        super();
        this.circleText = circleText;
        this.answerText = answerText;
        setOpaque(false);
        setFocusPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(300, 60)); // điều chỉnh kích thước đủ chứa text
        setText(null);
    }
    public void updateRatio(String answerText){
        this.answerText=answerText;
        repaint();
        revalidate();
    }
    public void isFalse(){
        color="#F7374F";
        repaint();
    }
    public void isCorrect(){
        color="#3a8a7d";
        repaint();
        
    }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Font font = new Font("SansSerif", Font.BOLD, 14);
        g2.setFont(font);
        FontMetrics fm = g2.getFontMetrics();

        // Kích thước ô số thứ tự (hình chữ nhật bo góc)
        // Vùng số thứ tự
        int numberWidth = 40;
        int answerY = 10;
        int answerHeight = getHeight() - 20;

        int numberHeight = answerHeight;
        int numberX = 10;
        int numberY = answerY;

        
        
        // Màu nền cho số thứ tự
        Color numberBgColor = isSelected()?Color.decode(color):Color.WHITE; // hồng
        Color numberTextColor = isSelected()?Color.WHITE: Color.BLACK;

        // Vẽ hình chữ nhật bo góc
        g2.setColor(numberBgColor);
        g2.fillRoundRect(numberX, numberY, numberWidth, numberHeight, 10, 10);
        
        // Vẽ viền cho ô số thứ tự
        Color borderColor = isSelected() ? Color.WHITE : Color.BLACK;
        g2.setColor(borderColor);
        g2.drawRoundRect(numberX, numberY, numberWidth, numberHeight, 10, 10);
        
        
        // Vẽ chữ (số thứ tự) trong hình
        int textWidth = fm.stringWidth(circleText);
        int textHeight = fm.getAscent();
        int textX = numberX + (numberWidth - textWidth) / 2;
        int textY = numberY + (numberHeight + textHeight) / 2 - 3;

        g2.setColor(numberTextColor);
        g2.drawString(circleText, textX, textY);

        // Vẽ background answerText (xám, bo góc)
        int answerX = numberX + numberWidth + 10;
        int answerWidth = getWidth() - answerX - 10;
        
        // Viền cho background answerText
        g2.setColor(borderColor); // tái sử dụng borderColor ở trên
        g2.drawRoundRect(answerX, answerY, answerWidth, answerHeight, 10, 10);

        
        g2.setColor(isSelected()?Color.decode(color):Color.WHITE); // xám sáng
        g2.fillRoundRect(answerX, answerY, answerWidth, answerHeight, 10, 10);

        // Vẽ answerText
        g2.setColor(isSelected()?Color.WHITE:Color.BLACK);
        int answerTextX = answerX + 10;
        int answerTextY = (getHeight() + fm.getAscent()) / 2 - 4;
        g2.drawString(answerText, answerTextX, answerTextY);

        g2.dispose();
    }

    public String getCircleText() {
        return this.circleText;
    }
}


