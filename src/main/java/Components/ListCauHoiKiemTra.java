/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Components;

import Dao.IDsLuaChon_DAO;
import Entity.CauHoi;
import net.miginfocom.swing.MigLayout;
import service.RmiServiceLocator;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 *
 * @author THANH PHU
 */
public class ListCauHoiKiemTra extends JPanel{
    private ArrayList<CauHoiKiemTra> dsCauHoi=new ArrayList<>();
    private IDsLuaChon_DAO lc_dao= RmiServiceLocator.getDsLuaChonDao();
    public ListCauHoiKiemTra() throws RemoteException {
        setOpaque(false);
        setLayout(new BorderLayout());
        JPanel container = new JPanel(new MigLayout("wrap 1, fillx", "[grow]", "[]"));
        container.setOpaque(false);
        for(int i=0;i<5;i++){
            CauHoiKiemTra cauHoi= new CauHoiKiemTra();
            dsCauHoi.add(cauHoi);
            container.add(cauHoi,"growx, wrap");
        }
        JScrollPane scrollPane = new JScrollPane(container);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBar(new ScrollBarCustom());
        scrollPane.setMinimumSize(new Dimension(800, 460));
        scrollPane.setPreferredSize(new Dimension(800, 460));
//        scrollPane.setMaximumSize(new Dimension(1200,600));
         scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // Ẩn scrollbar ngang
        scrollPane.getViewport().setOpaque(false); // Nền trong suốt);
        scrollPane.setOpaque(false);
        add(scrollPane, BorderLayout.CENTER);
    }
    public ListCauHoiKiemTra(ArrayList<CauHoi> ds) throws RemoteException {
        init(ds);
    }
    public void updateList(ArrayList<CauHoi> ds) throws RemoteException {
        removeAll();
        init(ds);
    }
    public void init(ArrayList<CauHoi> ds) throws RemoteException {
        dsCauHoi.clear();
        setOpaque(false);
        setLayout(new BorderLayout());
        JPanel container = new JPanel(new MigLayout("wrap 1, fillx", "[grow]", "[]"));
        container.setOpaque(false);
        for(int i=0;i<ds.size();i++){
            
            CauHoiKiemTra cauHoi= new CauHoiKiemTra(i+1,ds.get(i));
            dsCauHoi.add(cauHoi);
            container.add(cauHoi,"growx, wrap");
        }
        JScrollPane scrollPane = new JScrollPane(container);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBar(new ScrollBarCustom());
        scrollPane.setMinimumSize(new Dimension(800, 460));
        scrollPane.setPreferredSize(new Dimension(800, 460));
//        scrollPane.setMaximumSize(new Dimension(1200,600));
         scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // Ẩn scrollbar ngang
        scrollPane.getViewport().setOpaque(false); // Nền trong suốt);
        scrollPane.setOpaque(false);
        add(scrollPane, BorderLayout.CENTER);
    }
    public boolean isAnswered(int i) {
        if(i==1){
            int index = 1;
        for (CauHoiKiemTra x : dsCauHoi) {
            ButtonGroup buttonGroup = x.getButtonGroup1(); // Giả sử getButtonGroup1() trả về ButtonGroup của câu hỏi

            // Kiểm tra xem có nút nào được chọn trong ButtonGroup hay không
            boolean isAnyButtonSelected = false;
            Enumeration<AbstractButton> buttons = buttonGroup.getElements();
            while (buttons.hasMoreElements()) {
                if (buttons.nextElement().isSelected()) {
                    isAnyButtonSelected = true;
                    break; // Tìm thấy một nút được chọn, không cần kiểm tra thêm cho câu hỏi này
                }
            }

            // Nếu không có nút nào được chọn, hiển thị thông báo và trả về false
            if (!isAnyButtonSelected) {
                JOptionPane.showMessageDialog(null, "Câu " + index + " chưa chọn đáp án!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            index++; // Tăng index cho câu hỏi tiếp theo
        }
        }
        
        return true; // Tất cả các câu hỏi đã được trả lời
    }
    
    public List<String> getDsLuaChonCuaSinhVien() {
        List<String> dsLuaChon = new ArrayList<>();
        int i = 1;
         for (CauHoiKiemTra x : dsCauHoi) {
            ButtonGroup buttonGroup = x.getButtonGroup1();
            Enumeration<AbstractButton> buttons = buttonGroup.getElements();
                while (buttons.hasMoreElements()) {
                AbstractButton button = buttons.nextElement();
                if (button.isSelected()) {
                    if (button instanceof RatioExam) {
                        RatioExam ratio = (RatioExam) button;
                        dsLuaChon.add(i+"."+ratio.getCircleText());
                    }
                }
            }
                i++;
         }  
        return dsLuaChon;
    }
}
