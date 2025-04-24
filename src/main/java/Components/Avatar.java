package Components;

import GUI.GV_Classroom;
import GUI.GV_Exams;
import GUI.Main_GUI;
import GUI.SV_Classroom;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;

public class Avatar extends JPanel {

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

    private Color color1;
    private Color color2;

    public Avatar() {
        initComponents();
        setOpaque(false);
        color1 = Color.BLACK;
        color2 = Color.WHITE;
    }

    public void setData(Model_Card data) {
        lbIcon.setIcon(data.getIcon());
        lbTitle.setText(data.getTitle());
        lbValues.setText(data.getValues());

    }
    public void setAvt(Boolean avt) {
        System.out.println(avt);
        if(avt){
            jLabel1.setIcon(new ImageIcon(getClass().getResource("/Image/avt-male.png")));
        }
        else{
            jLabel1.setIcon(new ImageIcon(getClass().getResource("/Image/avt-female.png")));
        }
    }
    public static void updateTitle(String newTitle){
        title.setText(newTitle);
    }
    public static void isBack(){
        title.setIcon(new ImageIcon( Avatar.class.getResource("/Image/icons8-back-16.png")));
        title.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    public static void Backed(){
        title.setIcon(null);
        title.setCursor(Cursor.getDefaultCursor());
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbIcon = new JLabel();
        lbTitle = new JLabel();
        lbValues = new JLabel();
        jLabel1 = new JLabel();
        title = new JLabel();

        lbTitle.setFont(new Font("sansserif", 1, 14)); // NOI18N
        lbTitle.setForeground(new Color(255, 255, 255));
        lbTitle.setHorizontalAlignment(SwingConstants.RIGHT);
        lbTitle.setText("Title");

        lbValues.setFont(new Font("sansserif", 1, 18)); // NOI18N
        lbValues.setForeground(new Color(255, 255, 255));
        lbValues.setHorizontalAlignment(SwingConstants.RIGHT);
        lbValues.setText("Values");

        jLabel1.setIcon(new ImageIcon(getClass().getResource("/Image/avt-male.png"))); // NOI18N

        title.setFont(new Font("Segoe UI", 1, 18)); // NOI18N
        title.setForeground(new Color(255, 255, 255));
        title.setHorizontalAlignment(SwingConstants.LEFT);
        title.setText("Class");
        title.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                try {
                    titleMouseClicked(evt);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(title, GroupLayout.PREFERRED_SIZE, 159, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbIcon)
                .addGap(541, 541, 541)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(lbTitle, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(lbValues, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbTitle)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lbValues)
                                .addContainerGap())
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbIcon)
                                .addGap(0, 0, Short.MAX_VALUE))))))
            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(title)
                .addGap(22, 22, 22))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void titleMouseClicked(java.awt.event.MouseEvent evt) throws RemoteException {//GEN-FIRST:event_titleMouseClicked
        String tab= title.getText();
        String role= Main_GUI.tk.getVaiTro();
        Main_GUI.main_panel.removeAll();
        System.out.println(role);
        switch (tab) {
            case "Exam":
                if(role.equalsIgnoreCase("GV")){
                    Main_GUI.main_panel.add(new GV_Exams());
                }
                
                break;
            case "Class":
                if(role.equalsIgnoreCase("GV")){
                    Main_GUI.main_panel.add(new GV_Classroom());
                }
                else if(role.equalsIgnoreCase("SV")){
                    Main_GUI.main_panel.add(new SV_Classroom());
                }
                break;
            default:
                throw new AssertionError();
        }
        title.setIcon(null);
        title.setCursor(Cursor.getDefaultCursor());
        Main_GUI.main_panel.repaint();
        Main_GUI.main_panel.revalidate();
    }//GEN-LAST:event_titleMouseClicked

    @Override
    protected void paintComponent(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint g = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
        g2.setPaint(g);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
        g2.setColor(new Color(255, 255, 255, 50));
        g2.fillOval(getWidth() - (getHeight() / 2), 10, getHeight(), getHeight());
        g2.fillOval(getWidth() - (getHeight() / 2) - 20, getHeight() / 2 + 20, getHeight(), getHeight());
        super.paintComponent(grphcs);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JLabel jLabel1;
    private JLabel lbIcon;
    private JLabel lbTitle;
    private JLabel lbValues;
    private static JLabel title;
    // End of variables declaration//GEN-END:variables
}
