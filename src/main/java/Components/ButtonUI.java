package Components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class ButtonUI extends BasicButtonUI {

    private boolean hover;
    private JButton button;

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
        button = (JButton) c;
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hover = true;
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hover = false;
            }
        });
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(false);
        button.setForeground(Color.WHITE);
        button.setBackground(Color.decode("#3a8a7d"));
        button.setBorder(new EmptyBorder(5, 10, 5, 10));
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        if (button.isSelected() || hover) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (hover) {
                g2.setColor(Color.decode("#008894"));
            } else {
                g2.setColor(c.getBackground());
            }
            int width = c.getWidth();
            int height = c.getHeight();
            if (button.isSelected()) {
                Shape shape = new RoundRectangle2D.Double(0, 0, width, height, 5, 5);
                g2.fill(shape);
            }
            g2.dispose();
        }
        super.paint(g, c);
    }
}
