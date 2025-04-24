/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Components;

import Dao.IDeThi_DAO;
import Entity.DeThi;
import net.miginfocom.swing.MigLayout;
import service.RmiServiceLocator;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Admin
 */
public class ListDeThi2 extends JPanel{
    private IDeThi_DAO IDeThi_DAO = RmiServiceLocator.getDeThiDao();
    private List<DeThiCard3> dsCard= new ArrayList<>();
    public ListDeThi2(ArrayList<DeThi> dsDeThi) throws RemoteException {
        init(dsDeThi);
        repaint();
        revalidate();
    }
    private DeThi dethi;
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
    public void updateDsDeThi(ArrayList<DeThi> dsDeThi) {
        removeAll();
        init(dsDeThi);
        repaint();
        revalidate();
    }
    
    public ListDeThi2() throws RemoteException {
        setOpaque(false);
        
        setLayout(new BorderLayout());
        JPanel container = new JPanel(new MigLayout("wrap 3, fillx", "[grow][grow][grow]", "[]"));
        container.setOpaque(false);
        
        for (int i= 0; i<10;i++) {
            DeThiCard3 deThiCard = new DeThiCard3();
            container.add(deThiCard, "growx");
            deThiCard.getButton1().addActionListener(x->chonDeThi(new DeThi()));
        }
        JScrollPane scrollPane = new JScrollPane(container);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBar(new ScrollBarCustom());
        scrollPane.setPreferredSize(new Dimension(250, 300));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // Ẩn scrollbar ngang
        scrollPane.getViewport().setOpaque(false); // Nền trong suốt);
        scrollPane.setOpaque(false);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
    }
    public void init(ArrayList<DeThi> dsDeThi) {
        dsCard.clear();
        setOpaque(false);
        
        setLayout(new BorderLayout());
        JPanel container = new JPanel(new MigLayout("wrap 3, fillx", "[grow][grow][grow]", "[]"));
        container.setOpaque(false);
        
        dsDeThi.forEach(item -> {
            int randomIndex= random.nextInt(gradients.length);
            DeThiCard3 deThiCard = new DeThiCard3(item);
            container.add(deThiCard, "growx");
            deThiCard.getButton1().addActionListener(x->chonDeThi(item));
            dsCard.add(deThiCard);
//            deThiCard.getCircleBackgroundPanel1().setColor1(gradients[randomIndex][0]);
        });
        JScrollPane scrollPane = new JScrollPane(container);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBar(new ScrollBarCustom());
        scrollPane.setPreferredSize(new Dimension(250, 300));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // Ẩn scrollbar ngang
        scrollPane.getViewport().setOpaque(false); // Nền trong suốt);
        scrollPane.setOpaque(false);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    public void chonDeThi(DeThi x) {
        this.dethi=x;
        System.out.println(dethi);
    }

    public DeThi getDethi() {
        return dethi;
    }

    public void setDethi(DeThi dethi) {
        this.dethi = dethi;
    }
    
    public void themDeThi(DeThi x){
        
    }

    public List<DeThiCard3> getDsCard() {
        return dsCard;
    }

    public void setDsCard(List<DeThiCard3> dsCard) {
        this.dsCard = dsCard;
    }
    
}
