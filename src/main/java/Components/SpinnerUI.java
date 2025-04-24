package Components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicSpinnerUI;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SpinnerUI extends BasicSpinnerUI {

    @Override
    public void installUI(JComponent jc) {
        super.installUI(jc);
        spinner.setEditor(new Editor(spinner));
    }

    @Override
    protected Component createNextButton() {
        ArrowButton cmd = new ArrowButton(true);
        installNextButtonListeners(cmd);
        return cmd;
    }

    @Override
    protected Component createPreviousButton() {
        ArrowButton cmd = new ArrowButton(false);
        installPreviousButtonListeners(cmd);
        return cmd;
    }

    public class Editor extends JTextField implements ChangeListener {

        private final JSpinner spinner;
        private String labelText = "";

        public Editor(JSpinner spinner) {
            this.spinner = spinner;
            setText(spinner.getValue().toString());
            setHorizontalAlignment(JTextField.CENTER);
            setBorder(null);
            spinner.addChangeListener(this);

            addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    try {
                        int value = Integer.parseInt(getText());
                        spinner.setValue(value);
                    } catch (NumberFormatException ex) {
                        // optional: báo lỗi hoặc ignore
                    }
                }
            });
        }

        public void setLabelText(String text) {
            this.labelText = text;
        }

        public String getLabelText() {
            return labelText;
        }

        @Override
        public void stateChanged(ChangeEvent ce) {
            setText(spinner.getValue().toString());
        }
    }

    private class ArrowButton extends JButton {

        private final boolean next;

        public ArrowButton(boolean next) {
            this.next = next;
            setContentAreaFilled(false);
            setFocusable(false);
            setBorder(new EmptyBorder(5, 7, 5, 7));
            setBackground(new Color(231, 231, 231));
            setForeground(new Color(150, 150, 150));
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent me) {
                    setSelected(true);
                }

                @Override
                public void mouseReleased(MouseEvent me) {
                    setSelected(false);
                }
            });
        }

        @Override
        public void paint(Graphics grphcs) {
            super.paint(grphcs);
            Graphics2D g2 = (Graphics2D) grphcs;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 1, getWidth(), getHeight() - 2, 5, 5);
            if (next) {
                int width = getWidth();
                int height = getHeight();
                int size = 10;
                int x = (width - size) / 2;
                int y = (height - size) / 2;
                int px[] = {x + size / 2, x + size, x};
                int py[] = {y, y + size, y + size};
                if (isSelected()) {
                    g2.setColor(new Color(181, 181, 181));
                } else {
                    g2.setColor(getForeground());
                }
                g2.fillPolygon(px, py, px.length);
            } else {
                int width = getWidth();
                int height = getHeight();
                int size = 10;
                int x = (width - size) / 2;
                int y = (height - size) / 2;
                int px[] = {x, x + size, x + size / 2};
                int py[] = {y, y, y + size};
                if (isSelected()) {
                    g2.setColor(new Color(181, 181, 181));
                } else {
                    g2.setColor(getForeground());
                }
                g2.fillPolygon(px, py, px.length);
            }
            g2.dispose();
        }
    }
}
