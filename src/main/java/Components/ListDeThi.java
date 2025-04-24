/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Components;

import Dao.IDeThi_DAO;
import Entity.DeThi;
import GUI.Main_GUI;
import net.miginfocom.swing.MigLayout;
import service.RmiServiceLocator;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Admin
 */
public class ListDeThi extends JPanel{
    private IDeThi_DAO IDeThi_DAO = RmiServiceLocator.getDeThiDao();
    public ListDeThi(ArrayList<DeThi> dsDeThi) throws RemoteException {
        init(dsDeThi);
        repaint();
        revalidate();
    }
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
    
    public ListDeThi() throws RemoteException {
        setOpaque(false);
        
        setLayout(new BorderLayout());
        JPanel container = new JPanel(new MigLayout("wrap 3, fillx", "[grow][grow][grow]", "[]"));
        container.setOpaque(false);
        
        for (int i= 0; i<10;i++) {
            DeThiCard deThiCard = new DeThiCard();
            container.add(deThiCard, "growx");
        }
        JScrollPane scrollPane = new JScrollPane(container);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBar(new ScrollBarCustom());
        scrollPane.setPreferredSize(new Dimension(450, 500));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // Ẩn scrollbar ngang
        scrollPane.getViewport().setOpaque(false); // Nền trong suốt);
        scrollPane.setOpaque(false);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
    }
    public void init(ArrayList<DeThi> dsDeThi) {
        setOpaque(false);
        
        setLayout(new BorderLayout());
        JPanel container = new JPanel(new MigLayout("wrap 3, fillx", "[grow][grow][grow]", "[]"));
        container.setOpaque(false);
        
        dsDeThi.forEach(item -> {
            int randomIndex= random.nextInt(gradients.length);
            DeThiCard deThiCard = new DeThiCard(item);
            container.add(deThiCard, "growx");
            deThiCard.getButton2().addActionListener(x-> {
                try {
                    deleteDeThi(item);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            });
            deThiCard.getButton1().addActionListener(x->themDeThi(item));
            deThiCard.getCircleBackgroundPanel1().setColor1(gradients[randomIndex][0]);
        });
        JScrollPane scrollPane = new JScrollPane(container);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBar(new ScrollBarCustom());
        scrollPane.setPreferredSize(new Dimension(450, 500));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // Ẩn scrollbar ngang
        scrollPane.getViewport().setOpaque(false); // Nền trong suốt);
        scrollPane.setOpaque(false);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    public void deleteDeThi(DeThi x) throws RemoteException {
        int options = JOptionPane.showConfirmDialog(null, "Xác nhận xóa","Xóa đề thi",JOptionPane.YES_NO_OPTION);
        if(options == JOptionPane.YES_OPTION) {
            IDeThi_DAO.deleteDeThi(x.getMaDeThi());
            updateDsDeThi(IDeThi_DAO.getDanhSachDeThiCuaGiaoVien(Main_GUI.tk.getMaTaiKhoan()));
        }
    }
    public void themDeThi(DeThi x){
        
    }
    
}
