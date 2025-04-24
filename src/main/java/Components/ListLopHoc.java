/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Components;

import Entity.LopHoc;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Admin
 */
public class ListLopHoc extends JPanel{
    
    private final Color[][] gradients = {
        {new Color(255, 92, 92), new Color(255, 154, 158)}, // Đỏ
        {new Color(92, 107, 192), new Color(159, 168, 218)}, // Xanh dương
        {new Color(38, 166, 154), new Color(77, 182, 172)}, // Xanh lá
        {new Color(255, 193, 7), new Color(255, 235, 59)}, // Vàng
        {new Color(156, 39, 176), new Color(224, 64, 251)},  // Tím
        {new Color(255, 94, 98), new Color(255, 154, 158)}, // Đỏ - Hồng nhạt
        {new Color(255, 175, 189), new Color(255, 195, 160)}, // Hồng - Cam
        {new Color(135, 206, 235), new Color(25, 25, 112)}, // Xanh da trời - Xanh đậm
        {new Color(72, 61, 139), new Color(123, 104, 238)}, // Xanh tím - Xanh neon
        {new Color(255, 140, 0), new Color(255, 69, 0)}, // Cam - Đỏ đậm
        {new Color(34, 193, 195), new Color(253, 187, 45)}, // Xanh lục - Vàng
        {new Color(131, 58, 180), new Color(253, 29, 29)}, // Tím - Đỏ
        {new Color(252, 176, 69), new Color(255, 102, 0)}, // Cam nhạt - Cam đậm
        {new Color(173, 83, 137), new Color(255, 117, 117)}, // Tím hồng - Hồng đậm
        {new Color(67, 206, 162), new Color(24, 90, 157)}  // Xanh lục - Xanh biển
    };
    Random random = new Random();
    public ListLopHoc() throws RemoteException {
        setOpaque(false);
        setLayout(new BorderLayout());
        JPanel container = new JPanel(new MigLayout("wrap 3, fillx, filly", "[grow][grow][grow]", "[]"));
        container.setOpaque(false);
        
        for (int i= 0; i<10;i++) {
            LopHocCard lopHocCard = new LopHocCard();
            int randomIndex = random.nextInt(gradients.length); 
            lopHocCard.getCircleBackgroundPanel2().setColor1(gradients[randomIndex][0]);
            lopHocCard.getCircleBackgroundPanel2().setColor2(gradients[randomIndex][1]);
            container.add(lopHocCard, "growx, growy");
        }
        
        JScrollPane scrollPane = new JScrollPane(container);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBar(new ScrollBarCustom());
        scrollPane.setPreferredSize(new Dimension(800, 460));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // Ẩn scrollbar ngang
        scrollPane.getViewport().setOpaque(false); // Nền trong suốt);
        scrollPane.setOpaque(false);
        add(scrollPane, BorderLayout.CENTER);
    }
    public ListLopHoc(ArrayList<LopHoc> lopHocCards){
        init(lopHocCards);
    }
    public void updateList(ArrayList<LopHoc> lopHocCards){
        removeAll();
        init(lopHocCards);
    }
    public void init(ArrayList<LopHoc> lopHocCards){
        
        setOpaque(false);
        setLayout(new BorderLayout());
        JPanel container = new JPanel(new MigLayout("wrap 3, fillx", "[grow][grow][grow]", "[]"));
        container.setOpaque(false);
        
        lopHocCards.forEach(x->{
            LopHocCard lopHocCard = null;
            try {
                lopHocCard = new LopHocCard(x);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            int randomIndex = random.nextInt(gradients.length); 
            lopHocCard.getCircleBackgroundPanel2().setColor1(gradients[randomIndex][0]);
            lopHocCard.getCircleBackgroundPanel2().setColor2(gradients[randomIndex][1]);
            container.add(lopHocCard, "growx");
            
            
        });
        
        JScrollPane scrollPane = new JScrollPane(container);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBar(new ScrollBarCustom());
        scrollPane.setPreferredSize(new Dimension(800, 460));
        scrollPane.setMinimumSize(new Dimension(800, 460));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // Ẩn scrollbar ngang
        scrollPane.getViewport().setOpaque(false); // Nền trong suốt);
        scrollPane.setOpaque(false);
        add(scrollPane, BorderLayout.CENTER);
        repaint();
        revalidate();
    }
    
}
