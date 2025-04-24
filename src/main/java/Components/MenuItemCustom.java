package Components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MenuItemCustom extends JRadioButton {
    private static final Color SELECTED_COLOR = Color.decode("#2a3144"); // Màu khi chọn
    private static final Color DEFAULT_COLOR = new Color(255, 255, 255); // Màu mặc định
    private ImageIcon iconImage;
    private ImageIcon iconImageReverse;
    private String itemText="default";

    public String getItemText() {
        return itemText;
    }

    public void setItemText(String itemText) {
        this.itemText = itemText;
    }
    public MenuItemCustom(){
        super("");
        setPreferredSize(new Dimension(50, 50));
        setMinimumSize(new Dimension(50, 50));
    }
    public MenuItemCustom(String text, String icon) {
        super(); // Không đặt text ở đây
        setToolTipText(text); // Tooltip khi di chuột vào
        setItemText(text);
        setIcon(new ImageIcon(getClass().getResource("/Image/" + icon + ".png"))); // Đặt icon
        iconImage = new ImageIcon(getClass().getResource("/Image/" + icon + ".png"));
        iconImageReverse=new ImageIcon(getClass().getResource("/Image/" + icon + "-reverse.png"));
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setOpaque(false);
        setBackground(DEFAULT_COLOR);
        setPreferredSize(new Dimension(50, 50));
        setMinimumSize(new Dimension(50, 50));// Kích thước cố định cho hình tròn

        
        // Sự kiện khi chọn
        addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                setBackground(SELECTED_COLOR);
                 setIcon(new ImageIcon(getClass().getResource("/Image/" + icon + "-reverse.png")));
            } else {
                setBackground(DEFAULT_COLOR);
                 setIcon(new ImageIcon(getClass().getResource("/Image/" + icon + ".png")));
            }
            repaint();
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setToolTipText(text); // Cập nhật tooltip khi rê chuột vào
            }
        });
    }
@Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Vẽ hình tròn làm nền
        g2.setColor(isSelected() ? SELECTED_COLOR : DEFAULT_COLOR);
        g2.fillOval(0, 0, getWidth(), getHeight());
        
        // Vẽ icon ở trung tâm
        if (iconImage != null) {
            int iconWidth = iconImage.getIconWidth();
            int iconHeight = iconImage.getIconHeight();
            int x = (getWidth() - iconWidth) / 2;
            int y = (getHeight() - iconHeight) / 2;
            if(isSelected())
                g2.drawImage(iconImageReverse.getImage(), x, y, this);
            else
            g2.drawImage(iconImage.getImage(), x, y, this);
            
        }

        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        // Không vẽ border
    }
}
