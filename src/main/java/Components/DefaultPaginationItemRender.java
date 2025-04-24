package Components;

import javax.swing.*;
import java.awt.*;

public class DefaultPaginationItemRender implements PaginationItemRender {

    @Override
    public JButton createPaginationItem(Object value, boolean isPrevious, boolean isNext, boolean enable) {
        JButton cmd = createButton(value, isPrevious, isNext, enable);
        if (isPrevious) {
            Object icon = createPreviousIcon();
            if (icon != null) {
                if (icon instanceof Icon) {
                    cmd.setIcon((Icon) icon);
                } else {
                    cmd.setText(icon.toString());
                }
            }
            cmd.setForeground(Color.decode("#3a8a7d"));
        } else if (isNext) {
            Object icon = createNextIcon();
            if (icon != null) {
                if (icon instanceof Icon) {
                    cmd.setIcon((Icon) icon);
                } else {
                    cmd.setText(icon.toString());
                }
            }
             cmd.setForeground(Color.decode("#3a8a7d"));
        } else {
            cmd.setText(value.toString());
            
                cmd.setForeground(Color.decode("#3a8a7d"));
            
            
        }
        if (!enable) {
            cmd.setFocusable(false);
        }
        return cmd;
    }

    @Override
    public JButton createButton(Object value, boolean isPrevious, boolean isNext, boolean enable) {
        return new JButton();
    }

    @Override
    public Object createPreviousIcon() {
        return "Previous";
    }

    @Override
    public Object createNextIcon() {
        return "Next";
    }
}
