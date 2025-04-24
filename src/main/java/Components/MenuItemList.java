package Components;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MenuItemList extends JPanel {

    private ButtonGroup group = new ButtonGroup();  // Đảm bảo chỉ chọn 1 item
    private JPanel itemPanel = new JPanel();  // Panel chứa các MenuItem

    public MenuItemList() {

        List<MenuItemCustom> menuItems = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            MenuItemCustom item = new MenuItemCustom("Home", "icons8-home-30");
            menuItems.add(item);
        }
        init(menuItems);
    }

    public MenuItemList(List<MenuItemCustom> menuItems) {
        init(menuItems);

    }

    public void updateMenu(List<MenuItemCustom> menuItems) {

        init(menuItems);
    }

    public void init(List<MenuItemCustom> menuItems) {
        itemPanel.removeAll();

        group.clearSelection();
        setLayout(new BorderLayout());
        setBackground(Color.white);
        // Sử dụng GridBagLayout thay vì BoxLayout để căn giữa & giữ kích thước
        itemPanel.setLayout(new GridBagLayout());
        itemPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE; // Tự động tăng chỉ số hàng
        gbc.weightx = 1; // Kéo dãn theo chiều ngang
        gbc.fill = GridBagConstraints.HORIZONTAL; // Đảm bảo rộng bằng nhau
        gbc.insets = new Insets(30, 5, 10, 5); // Khoảng cách giữa các item

        for (MenuItemCustom item : menuItems) {
            group.add(item);
            item.setPreferredSize(new Dimension(50, 50)); // Đảm bảo kích thước cố định

            itemPanel.add(item, gbc);
        }
        menuItems.get(0).setSelected(true);

        add(itemPanel, BorderLayout.NORTH);
    }

    public void addMenuItems(List<MenuItemCustom> menuItems) {
        itemPanel.removeAll(); // Xóa cũ trước khi thêm mới
        for (MenuItemCustom item : menuItems) {
            group.add(item);  // Thêm vào nhóm để đảm bảo chỉ có 1 item được chọn
            itemPanel.add(item);
            itemPanel.add(Box.createVerticalStrut(25)); // Thêm khoảng cách giữa các item

        }
        revalidate();
        repaint();
    }

    public String getItemClicked() {
        for (Component comp : getComponents()) {
            if (comp instanceof MenuItemCustom) {
                MenuItemCustom item = (MenuItemCustom) comp;
                if (item.isSelected()) {
                    return item.getItemText();
                }
            }
        }
        return null;
    }
}
