/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Components;

import Dao.IBaiKiemTra_DAO;
import Dao.IDsCauTraLoi_DAO;
import Dao.IDsLuaChon_DAO;
import Entity.BaiKiemTra;
import Entity.CauHoi;
import Entity.KetQuaKiemTra;
import Entity.LuaChons;
import GUI.Main_GUI;
import net.miginfocom.swing.MigLayout;
import service.RmiServiceLocator;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author THANH PHU
 */
public class ListXemLai extends JPanel {

    private IDsCauTraLoi_DAO dsTL_dao = RmiServiceLocator.getDsCauTraLoiDao();
    private IDsLuaChon_DAO dsLC_dao = RmiServiceLocator.getDsLuaChonDao();
    private IBaiKiemTra_DAO bkt_dao = RmiServiceLocator.getBaiKiemTraDao();

    public ListXemLai() {
        setOpaque(false);
        setLayout(new BorderLayout());
        JPanel container = new JPanel(new MigLayout("wrap 1, fillx", "[grow]", "[]"));
        container.setOpaque(false);

        for (int i = 0; i < 10; i++) {
            CauHoiXemLai card = new CauHoiXemLai();
            container.add(card, "growx, wrap");
        }
        JScrollPane scrollPane = new JScrollPane(container);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBar(new ScrollBarCustom());

        scrollPane.setPreferredSize(new Dimension(470, 460));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // Ẩn scrollbar ngang
        scrollPane.getViewport().setOpaque(false); // Nền trong suốt);
        scrollPane.setOpaque(false);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void updateList(ArrayList<CauHoi> dsCauHoi, KetQuaKiemTra kq, BaiKiemTra bkt) throws RemoteException {
        removeAll();
        setOpaque(false);
        setLayout(new BorderLayout());
        JPanel container = new JPanel(new MigLayout("wrap 1, fillx", "[grow]", "[]"));
        container.setOpaque(false);

        // Tạo map số câu -> đáp án
        List<String> cauTraLoi = dsTL_dao.getDsCauTraLoiCuaSinhVien(Main_GUI.tk.getMaTaiKhoan(),kq.getBaiKiemTra().getMaBaiKiemTra());
        Map<Integer, String> mapCauTraLoi = new HashMap<>();

        for (String ct : cauTraLoi) {
            String[] parts = ct.split("\\.");
            if (parts.length == 2) {
                try {
                    int soCau = Integer.parseInt(parts[0]);
                    mapCauTraLoi.put(soCau, parts[1]);
                } catch (NumberFormatException e) {
                    System.err.println("Lỗi số câu không hợp lệ: " + ct);
                }
            }
        }
        mapCauTraLoi.entrySet().forEach(x->System.out.println(x.getKey()+" "+x.getValue()));
        // Lấy thông tin bài kiểm tra
        bkt = bkt_dao.getBaiKiemTra(bkt.getMaBaiKiemTra());
        
        // Tạo giao diện từng câu hỏi
        for (int i = 0; i < dsCauHoi.size(); i++) {
            ArrayList<LuaChons> dsLC = dsLC_dao.getDSLuaChonTheoMa(dsCauHoi.get(i).getMaCauHoi());
            
            String answer = mapCauTraLoi.getOrDefault(i, ""); // i là số câu (từ 0)
            CauHoiXemLai card = new CauHoiXemLai(dsLC, bkt.isHienThiDapAn(), answer, dsCauHoi.get(i).getLoiGiai());
            card.setCauHoi(dsCauHoi.get(i).getCauHoi(), i);
            card.setEnabled(false);
            container.add(card, "growx, wrap");
        }

        // Scroll
        JScrollPane scrollPane = new JScrollPane(container);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBar(new ScrollBarCustom());
        scrollPane.setPreferredSize(new Dimension(470, 460));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);
        scrollPane.setBorder(null);

        add(scrollPane, BorderLayout.CENTER);
    }

}
