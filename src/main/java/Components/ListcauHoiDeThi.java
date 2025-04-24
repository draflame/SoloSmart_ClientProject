/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Components;

import Dao.IDeThi_DAO;
import Entity.CauHoi;
import Entity.LuaChons;
import net.miginfocom.swing.MigLayout;
import service.RmiServiceLocator;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author THANH PHU
 */
public class ListcauHoiDeThi extends JPanel{
    private List<CauHoiDeThi> dsCauHoiDeThis = new ArrayList<>();
    private List<CauHoi> dsCauHoi = new ArrayList<>();
    private IDeThi_DAO IDeThi_DAO = RmiServiceLocator.getDeThiDao();
    private String maDeThi;

    public String getMaDeThi() {
        return maDeThi;
    }
    
    public List<CauHoiDeThi> getDsCauHoiDeThis() {
        return this.dsCauHoiDeThis;
    }
    
    public List<CauHoi> getDsCauHoi() {
        return dsCauHoi;
    }

    public ListcauHoiDeThi(String maDeThi) throws RemoteException {
        this.maDeThi = maDeThi;
        setOpaque(false);
        setLayout(new BorderLayout());
        JPanel container = new JPanel(new MigLayout("wrap 1, fillx", "[grow]", "[]"));
        container.setOpaque(false);
        for(int i=0;i<10;i++){
            CauHoiDeThi cauHoi= new CauHoiDeThi();
            cauHoi.setCauHoi("Câu "+(i+1));
            dsCauHoiDeThis.add(cauHoi);
            container.add(cauHoi,"growx, wrap");
        }
        JScrollPane scrollPane = new JScrollPane(container);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBar(new ScrollBarCustom());

        scrollPane.setPreferredSize(new Dimension(800, 460));
        scrollPane.setMinimumSize(new Dimension(800, 460));
        scrollPane.setMaximumSize(new Dimension(1200,600));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // Ẩn scrollbar ngang
        scrollPane.getViewport().setOpaque(false); // Nền trong suốt);
        scrollPane.setOpaque(false);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    public void updateListcauHoiDeThi(String maDeThi,List<CauHoi> dsCauHoi,List<LuaChons> dsLuaChon) throws RemoteException {
        dsCauHoiDeThis.clear();
        this.maDeThi = maDeThi;
        setOpaque(false);
        setLayout(new BorderLayout());
        JPanel container = new JPanel(new MigLayout("wrap 1, fillx", "[grow]", "[]"));
        container.setOpaque(false);
        int indexLuaChon = 0; // để lấy từng nhóm 4 phần tử

        for (var cauHoi : dsCauHoi) {
            // Lấy 4 phần tử từ dsLuaChon, nếu còn đủ
            int endIndex = Math.min(indexLuaChon + 4, dsLuaChon.size());
            List<LuaChons> luaChonSubList = dsLuaChon.subList(indexLuaChon, endIndex);

            // Tạo câu hỏi từ nhóm lựa chọn
            CauHoiDeThi cauHoiDeThi = new CauHoiDeThi(luaChonSubList);
            cauHoiDeThi.setCauHoi("Câu " + (indexLuaChon / 4 + 1));
            cauHoiDeThi.setNoiDungCauHoi(cauHoi.getCauHoi());
            cauHoiDeThi.setTfLoiGiai(cauHoi.getLoiGiai());
            cauHoiDeThi.setMaCauHoi(cauHoi.getMaCauHoi());

            dsCauHoiDeThis.add(cauHoiDeThi);
            container.add(cauHoiDeThi, "growx, wrap");
            indexLuaChon += 4;
        }

        JScrollPane scrollPane = new JScrollPane(container);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBar(new ScrollBarCustom());

        scrollPane.setPreferredSize(new Dimension(800, 460));
        scrollPane.setMinimumSize(new Dimension(800, 460));
        scrollPane.setMaximumSize(new Dimension(1200,600));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // Ẩn scrollbar ngang
        scrollPane.getViewport().setOpaque(false); // Nền trong suốt);
        scrollPane.setOpaque(false);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    public ListcauHoiDeThi() throws RemoteException {
        setOpaque(false);
        setLayout(new BorderLayout());
        JPanel container = new JPanel(new MigLayout("wrap 1, fillx", "[grow]", "[]"));
        container.setOpaque(false);
        for(int i=0;i<10;i++){
            CauHoiDeThi cauHoi= new CauHoiDeThi();
            cauHoi.setCauHoi("Câu "+(i+1));
            dsCauHoiDeThis.add(cauHoi);
            container.add(cauHoi,"growx, wrap");
        }
        JScrollPane scrollPane = new JScrollPane(container);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBar(new ScrollBarCustom());
        
        scrollPane.setPreferredSize(new Dimension(800, 460));
        scrollPane.setMinimumSize(new Dimension(800, 460));
        scrollPane.setMaximumSize(new Dimension(1200,600));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // Ẩn scrollbar ngang
        scrollPane.getViewport().setOpaque(false); // Nền trong suốt);
        scrollPane.setOpaque(false);
        add(scrollPane, BorderLayout.CENTER);
    }
    public ListcauHoiDeThi(int soLuong) throws RemoteException {
        init(soLuong);
    }
    public void updateCauHoi(int soluong) throws RemoteException {
        removeAll();
        init(soluong);
    }
    public void init(int soLuong) throws RemoteException {

        dsCauHoiDeThis.clear();
        setOpaque(false);
        setLayout(new BorderLayout());
        JPanel container = new JPanel(new MigLayout("wrap 1, fillx", "[grow]", "[]"));
        container.setOpaque(false);
        for(int i=0;i<soLuong;i++){
            CauHoiDeThi cauHoi= new CauHoiDeThi(maDeThi);
            cauHoi.setCauHoi("Câu "+(i+1));
            dsCauHoiDeThis.add(cauHoi);
            container.add(cauHoi,"growx, wrap");
        }
        JScrollPane scrollPane = new JScrollPane(container);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBar(new ScrollBarCustom());
        
        scrollPane.setPreferredSize(new Dimension(800, 460));
        scrollPane.setMinimumSize(new Dimension(800, 460));
        scrollPane.setMaximumSize(new Dimension(1200,600));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // Ẩn scrollbar ngang
        scrollPane.getViewport().setOpaque(false); // Nền trong suốt);
        scrollPane.setOpaque(false);
        add(scrollPane, BorderLayout.CENTER);
        
        repaint();
        revalidate();
    }
    
    public boolean isDone() {
        int i=1;
        for (CauHoiDeThi x : dsCauHoiDeThis) {
            if (!x.isDone()){
                JOptionPane.showMessageDialog(null, "Thông tin câu hỏi "+(i++)+ "chưa hoàn chỉnh. Xin hãy kiểm tra lại.");
                return false;
            }
        }
        return true;
    }
}

