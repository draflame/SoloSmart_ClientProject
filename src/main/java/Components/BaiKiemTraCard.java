/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Components;

import Components.chart.ModelChart;
import Dao.*;
import Entity.*;
import GUI.GV_ClassRoom_Detail;
import GUI.Main_GUI;
import GUI.SV_KiemTra;
import service.RmiServiceLocator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author THANH PHU
 */
public class BaiKiemTraCard extends JPanel {
    // private DateChooser datach= new DateChooser();

    /**
     * Creates new form BaiKiemTraCard
     */
    private CustomDateTimeChooser datech = new CustomDateTimeChooser();
    private IDeThi_DAO dt_dao = RmiServiceLocator.getDeThiDao();
    private IBaiKiemTra_DAO bkt_dao = RmiServiceLocator.getBaiKiemTraDao();
    private IKetQuaHocTap_DAO kqht_dao = RmiServiceLocator.getKetQuaHocTapDao();
    private IMonHoc_DAO mh_dao = RmiServiceLocator.getMonHocDao();
    private IKetQuaKiemTra_DAO kqkt_dao = RmiServiceLocator.getKetQuaKiemTraDao();
    private BaiKiemTra bkt;
    private DateTimeFormatter df = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
    private DecimalFormat de = new DecimalFormat("#.##");

    public BaiKiemTraCard() throws RemoteException {
        initComponents();
    }

    public BaiKiemTraCard(BaiKiemTra bkt) throws RemoteException {
        this.bkt = bkt;
        initComponents();
        DeThi dt = dt_dao.getDeThi(bkt.getDeThi().getMaDeThi());
        jLabel2.setText(dt.getTenDeThi());
        jLabel3.setText("Thời gian bắt đầu: " + df.format(bkt.getThoiGianBatDau()));
        jLabel4.setText("Thời gian kết thúc: " + df.format(bkt.getThoiGianKetThuc()));
        jLabel5.setText("Thời gian làm bài: " + bkt.getThoiGianLamBai() + " phút");
        jLabel6.setText("Số lần làm bài: " + bkt.getSoLanLamBai());
        if (Main_GUI.tk.getVaiTro().equalsIgnoreCase("GV")) {
            button2.setText("Xem");
            button2.addActionListener(x -> {
                try {
                    buttonGVXem();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            });
            button3.setText("Xóa");
            button3.addActionListener(x -> {
                try {
                    buttonGVXoa();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            });
        } else {
            button2.addActionListener(x -> {
                try {
                    buttonSVThamGia();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            });
            if(kqkt_dao.getDanhSachKetQuaKiemTra(Main_GUI.tk.getMaTaiKhoan(), bkt.getMaBaiKiemTra()).size()<=0
                    ||bkt.getThoiGianBatDau().isAfter(LocalDateTime.now())){
                button3.setVisible(false);
            }
            button3.addActionListener(x->
                    {
                try {
                    buttonSVXemLai();
                } catch (RemoteException ex) {
                    Logger.getLogger(BaiKiemTraCard.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            );
            
        }

    }

    private Map<TaiKhoan, Float> dsSVLamBaiKiemTra;

    public void buttonSVThamGia() throws RemoteException {
        LocalDateTime now = LocalDateTime.now();
        ArrayList<KetQuaKiemTra> dsKQKT = kqkt_dao.getDanhSachKetQuaKiemTra(Main_GUI.tk.getMaTaiKhoan(),
                bkt.getMaBaiKiemTra());
        if (bkt.getThoiGianBatDau().isAfter(now)) {
            JOptionPane.showMessageDialog(null, "Chưa tới thời gian làm bài kiểm tra");
        } else if (bkt.getThoiGianKetThuc().isBefore(now)) {
            JOptionPane.showMessageDialog(null, "Đã hết thời gian làm bài kiểm tra");
        } else if (bkt.getSoLanLamBai() < dsKQKT.size()) {
            JOptionPane.showMessageDialog(null, "Đã hết lượt làm bài kiểm tra");
        } else {
            String matKhau = bkt.getMatKhauBaiKiemTra();
            System.out.println("mat khâu " + matKhau);
            if (JOptionPane.showConfirmDialog(null,
                    "Khi bắt đầu bài kiểm tra, bạn sẽ không thể thoát ra giữa chừng. Nếu bạn thoát, bài làm sẽ tự động được nộp và kết quả sẽ được lưu lại.\nXác nhận kiểm tra?",
                    "Xác nhận kiểm tra", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                if (matKhau == null || matKhau.trim().equals("")) {
                    initBaiKiemTra();
                } else {
                    String nhapMatKhau = JOptionPane.showInputDialog(null,
                            "Vui lòng nhập mật khẩu để bắt đầu bài kiểm tra");
                    if (nhapMatKhau == null) {
                        return;
                    }
                    if (!nhapMatKhau.equalsIgnoreCase(matKhau)) {
                        JOptionPane.showMessageDialog(null, "Mật khẩu không đúng, vui lòng thử lại");
                        return;
                    }
                    initBaiKiemTra();
                }
            }
        }
    }
    public void buttonSVXemLai() throws RemoteException{
        listKetQuaKiemTra1.updateList(kqkt_dao.getDanhSachKetQuaKiemTra(Main_GUI.tk.getMaTaiKhoan(), bkt.getMaBaiKiemTra()), bkt);
        
        DialogXemLai.pack();
        DialogXemLai.setLocationRelativeTo(null);
        DialogXemLai.setVisible(true);
    }
    public void initBaiKiemTra() throws RemoteException {
        SV_KiemTra kiemTraGUI = new SV_KiemTra(bkt);
        System.out.println(bkt);
        kiemTraGUI.setExtendedState(JFrame.MAXIMIZED_BOTH);
        kiemTraGUI.setVisible(true);
    }

    public void buttonGVXem() throws RemoteException {
        dsSVLamBaiKiemTra = bkt_dao.getDsTaiKhoanThamGiaKiemTraVaDiemSo(bkt.getMaBaiKiemTra());
        int soLuongSV = dsSVLamBaiKiemTra.size();
        int tongSV = kqht_dao.getDanhSachKetQuaHocTap(bkt.getLopHoc().getMaLop()).size();
        initData();
        float trungBinhDiem = (float) dsSVLamBaiKiemTra.values()
                .stream()
                .mapToDouble(Float::doubleValue)
                .average()
                .orElse(0.0);
        thongKeCard1.updateCard("E74888", "885FBF", "F66060", soLuongSV + "", "icons8-user-groups-50",
                "Số sinh viên làm bài kiểm tra", (int) (((float) soLuongSV / tongSV) * 100));
        thongKeCard2.updateCard("38419D", "52D3D8", "38419D", de.format(trungBinhDiem) + "", "icons8-test-50",
                "Điểm trung bình", (int) (((float) trungBinhDiem / 10) * 100));
        loadTable(1);
        pagination1.setPaginationItemRender(new PaginationItemRenderStyle1());
        pagination1.addEventPagination(new EventPagination() {
            @Override
            public void pageChanged(int page) {
                loadTable(page);
            }

        });
        XemBKT.pack();
        XemBKT.setLocationRelativeTo(null);
        XemBKT.setVisible(true);
    }

    public void buttonGVXoa() throws RemoteException {
        if (JOptionPane.showConfirmDialog(null, "Bạn có muốn xóa bài kiểm tra này không?", "Xóa bài kiểm tra",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            if (bkt_dao.deleteBaiKiemTra(bkt.getMaBaiKiemTra())) {
                GV_ClassRoom_Detail.loadBKT();
            } else {
                JOptionPane.showMessageDialog(null, "Xóa thất bại");
            }
        }
    }

    public void initData() throws RemoteException {
        jCheckBoxCustom1.setSelected(bkt.isChoPhepXemDiem());
        jCheckBoxCustom2.setSelected(bkt.isChoPhepXemLai());
        jCheckBoxCustom3.setSelected(bkt.isHienThiDapAn());
        deThiCard21.update(dt_dao.getDeThi(bkt.getDeThi().getMaDeThi()));
        customDateChooser1.setDateTime(bkt.getThoiGianBatDau());
        customDateChooser2.setDateTime(bkt.getThoiGianKetThuc());
        jSlider1.setValue(bkt.getThoiGianLamBai());
        jLabel11.setText(bkt.getThoiGianLamBai() + " phút");
        jSpinner1.setValue(bkt.getSoLanLamBai());
        comboBoxSuggestion1.setSelectedItem(bkt.getHeSo() + "");
        myPasswordField1.setText(bkt.getMatKhauBaiKiemTra());
        eye = false;
    }

    public void loadTable(int page) {
        int limit = 10;
        int totalPage = (int) Math.ceil((double) dsSVLamBaiKiemTra.size() / limit);
        pagination1.setPagegination(page, totalPage);
        int currentPage = page; // Bạn có thể thay đổi thành biến động nếu muốn điều khiển trang
        int skip = (currentPage - 1) * limit;
        System.out.println(dsSVLamBaiKiemTra.size());
        Map<TaiKhoan, Float> listToShow = dsSVLamBaiKiemTra.entrySet().stream()
                .skip(skip)
                .limit(limit)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        System.out.println(listToShow.size());
        model.setRowCount(0);
        listToShow.entrySet().forEach(x -> {
            TaiKhoan sv = x.getKey();
            Float diem = x.getValue();
            model.addRow(new Object[] {
                    sv.getHo() + " " + sv.getTen(), diem
            });
        });
    }

    public void initDSDeThi() throws RemoteException {
        MonHoc mon = mh_dao.getMonHoc(GV_ClassRoom_Detail.lopHoc.getMonHoc().getMaMonHoc());
        ArrayList<DeThi> dsDeThi = dt_dao.getDanhSachDeThiTheoMonCuaGV(Main_GUI.tk.getMaTaiKhoan(), mon.getTenMonHoc());
        listDeThi21.updateDsDeThi(dsDeThi);
        listDeThi21.getDsCard().forEach(x -> {
            x.getButton1().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    deThiCard21.update(x.getDethi());
                    jDialog1.dispose();
                }
            });
        });
    }

    public static Button getButton4() {
        return button4;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() throws RemoteException {

        customDateChooser1 = new CustomDateTimeChooser();
        XemBKT = new JDialog();
        roundedPanel4 = new RoundedPanel();
        circleBackgroundPanel1 = new CircleBackgroundPanel();
        thongKeCard1 = new ThongKeCard();
        thongKeCard2 = new ThongKeCard();
        roundedPanel5 = new RoundedPanel();
        jCheckBoxCustom1 = new JCheckBoxCustom();
        jCheckBoxCustom2 = new JCheckBoxCustom();
        jCheckBoxCustom3 = new JCheckBoxCustom();
        jLabel7 = new JLabel();
        myTextField1 = new MyTextField();
        jLabel8 = new JLabel();
        myTextField2 = new MyTextField();
        jLabel9 = new JLabel();
        button1 = new Button();
        button4 = new Button();
        button5 = new Button();
        deThiCard21 = new DeThiCard2();
        jSpinner1 = new JSpinner();
        jSlider1 = new JSlider();
        jLabel10 = new JLabel();
        jLabel11 = new JLabel();
        button6 = new Button();
        jLabel12 = new JLabel();
        myPasswordField1 = new MyPasswordField();
        button7 = new Button();
        comboBoxSuggestion1 = new ComboBoxSuggestion();
        jLabel13 = new JLabel();
        jScrollPane1 = new JScrollPane();
        jTable1 = new JTable();
        jPanel1 = new JPanel();
        pagination1 = new Pagination();
        customDateChooser2 = new CustomDateTimeChooser();
        DialogThongKe = new JDialog();
        chart1 = new Components.chart.Chart(quantity);
        jDialog1 = new JDialog();
        listDeThi21 = new ListDeThi2();
        DialogXemLai = new JDialog();
        jPanel2 = new JPanel();
        listKetQuaKiemTra1 = new ListKetQuaKiemTra();
        roundedPanel1 = new RoundedPanel();
        roundedPanel2 = new RoundedPanel();
        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        jLabel3 = new JLabel();
        jLabel4 = new JLabel();
        jLabel5 = new JLabel();
        jLabel6 = new JLabel();
        roundedPanel3 = new RoundedPanel();
        button2 = new Button();
        button3 = new Button();

        customDateChooser1.setTextReference(myTextField1);

        XemBKT.setModal(true);
        XemBKT.setResizable(false);

        roundedPanel4.setBackground(new Color(255, 255, 255));

        circleBackgroundPanel1.setColor1(new Color(58, 138, 125));

        GroupLayout circleBackgroundPanel1Layout = new GroupLayout(circleBackgroundPanel1);
        circleBackgroundPanel1.setLayout(circleBackgroundPanel1Layout);
        circleBackgroundPanel1Layout.setHorizontalGroup(
                circleBackgroundPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(circleBackgroundPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(thongKeCard1, GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(thongKeCard2, GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(14, 14, 14)));
        circleBackgroundPanel1Layout.setVerticalGroup(
                circleBackgroundPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING,
                                circleBackgroundPanel1Layout.createSequentialGroup()
                                        .addContainerGap(10, Short.MAX_VALUE)
                                        .addGroup(circleBackgroundPanel1Layout
                                                .createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                .addComponent(thongKeCard2, GroupLayout.PREFERRED_SIZE,
                                                        GroupLayout.DEFAULT_SIZE,
                                                        GroupLayout.PREFERRED_SIZE)
                                                .addComponent(thongKeCard1, GroupLayout.PREFERRED_SIZE,
                                                        GroupLayout.DEFAULT_SIZE,
                                                        GroupLayout.PREFERRED_SIZE))
                                        .addContainerGap()));

        roundedPanel5.setBackground(new Color(247, 247, 247));

        jCheckBoxCustom1.setText("Cho phép xem điểm");

        jCheckBoxCustom2.setText("Cho phép xem lại");

        jCheckBoxCustom3.setText("Hiển thị đáp án");

        jLabel7.setText("Số lần làm lại:");

        myTextField1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                myTextField1ActionPerformed(evt);
            }
        });

        jLabel8.setText("Ngày bắt đầu:");

        myTextField2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                myTextField2ActionPerformed(evt);
            }
        });

        jLabel9.setText("Ngày kết thúc:");

        button1.setIcon(new ImageIcon(getClass().getResource("/Image/icons8-reload-30.png"))); // NOI18N
        button1.setText("Đặt lại");
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try {
                    button1ActionPerformed(evt);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        button4.setBackground(new Color(0, 0, 0));
        button4.setForeground(new Color(255, 255, 255));
        button4.setText("Cập nhật");
        button4.setFont(new Font("Segoe UI", 1, 14)); // NOI18N
        button4.setPreferredSize(new Dimension(47, 40));
        button4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try {
                    button4ActionPerformed(evt);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        button5.setText("Chọn đề thi");
        button5.setPreferredSize(new Dimension(61, 36));
        button5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try {
                    button5ActionPerformed(evt);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        deThiCard21.setOpaque(false);

        jSpinner1.setModel(new SpinnerNumberModel(1, 1, null, 1));

        jSlider1.setMajorTickSpacing(15);
        jSlider1.setMaximum(120);
        jSlider1.setMinimum(5);
        jSlider1.setPaintLabels(true);
        jSlider1.setPaintTicks(true);
        jSlider1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider1StateChanged(evt);
            }
        });

        jLabel10.setText("Thời gian làm bài:");

        jLabel11.setText("jLabel11");

        button6.setIcon(new ImageIcon(getClass().getResource("/Image/icons8-statistics-30.png"))); // NOI18N
        button6.setText("Thống kê");
        button6.setPreferredSize(new Dimension(42, 40));
        button6.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try {
                    button6ActionPerformed(evt);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        jLabel12.setText("Mật khẩu:");

        myPasswordField1.setPreferredSize(new Dimension(64, 40));
        myPasswordField1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                myPasswordField1ActionPerformed(evt);
            }
        });

        button7.setIcon(new ImageIcon(getClass().getResource("/Image/eye_closed.png"))); // NOI18N
        button7.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                button7ActionPerformed(evt);
            }
        });

        comboBoxSuggestion1.setModel(new DefaultComboBoxModel(new String[] { "0.2", "0.3", "0.5" }));

        jLabel13.setText("Hệ số:");

        GroupLayout roundedPanel5Layout = new GroupLayout(roundedPanel5);
        roundedPanel5.setLayout(roundedPanel5Layout);
        roundedPanel5Layout.setHorizontalGroup(
                roundedPanel5Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(roundedPanel5Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(roundedPanel5Layout
                                        .createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jCheckBoxCustom3, GroupLayout.PREFERRED_SIZE, 141,
                                                GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jCheckBoxCustom2, GroupLayout.PREFERRED_SIZE, 141,
                                                GroupLayout.PREFERRED_SIZE)
                                        .addGroup(roundedPanel5Layout.createSequentialGroup()
                                                .addComponent(jLabel7)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jSpinner1, GroupLayout.PREFERRED_SIZE,
                                                        GroupLayout.DEFAULT_SIZE,
                                                        GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED,
                                                        GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jLabel13, GroupLayout.PREFERRED_SIZE, 43,
                                                        GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(comboBoxSuggestion1,
                                                        GroupLayout.PREFERRED_SIZE,
                                                        GroupLayout.DEFAULT_SIZE,
                                                        GroupLayout.PREFERRED_SIZE)
                                                .addGap(6, 6, 6))
                                        .addGroup(roundedPanel5Layout.createSequentialGroup()
                                                .addComponent(jLabel12)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(myPasswordField1, GroupLayout.PREFERRED_SIZE,
                                                        195, GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 0, 0)
                                                .addComponent(button7, GroupLayout.PREFERRED_SIZE,
                                                        GroupLayout.DEFAULT_SIZE,
                                                        GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jCheckBoxCustom1, GroupLayout.PREFERRED_SIZE, 141,
                                                GroupLayout.PREFERRED_SIZE))
                                .addGap(6, 6, Short.MAX_VALUE)
                                .addGroup(roundedPanel5Layout
                                        .createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addGroup(roundedPanel5Layout.createSequentialGroup()
                                                .addComponent(jLabel10)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel11, GroupLayout.PREFERRED_SIZE, 200,
                                                        GroupLayout.PREFERRED_SIZE))
                                        .addGroup(roundedPanel5Layout.createSequentialGroup()
                                                .addComponent(jLabel8, GroupLayout.PREFERRED_SIZE, 89,
                                                        GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(myTextField1, GroupLayout.PREFERRED_SIZE, 198,
                                                        GroupLayout.PREFERRED_SIZE))
                                        .addGroup(roundedPanel5Layout.createSequentialGroup()
                                                .addComponent(jLabel9, GroupLayout.PREFERRED_SIZE, 95,
                                                        GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(myTextField2, GroupLayout.PREFERRED_SIZE, 198,
                                                        GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jSlider1, GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(deThiCard21, GroupLayout.PREFERRED_SIZE,
                                        GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(roundedPanel5Layout
                                        .createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(button4, GroupLayout.PREFERRED_SIZE, 107,
                                                GroupLayout.PREFERRED_SIZE)
                                        .addGroup(roundedPanel5Layout
                                                .createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                .addComponent(button5, GroupLayout.DEFAULT_SIZE, 107,
                                                        Short.MAX_VALUE)
                                                .addComponent(button6, GroupLayout.DEFAULT_SIZE,
                                                        GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addComponent(button1, GroupLayout.PREFERRED_SIZE, 107,
                                                GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(18, Short.MAX_VALUE)));
        roundedPanel5Layout.setVerticalGroup(
                roundedPanel5Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(roundedPanel5Layout.createSequentialGroup()
                                .addGroup(roundedPanel5Layout
                                        .createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(roundedPanel5Layout.createSequentialGroup()
                                                .addGap(3, 3, 3)
                                                .addGroup(roundedPanel5Layout
                                                        .createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                        .addGroup(roundedPanel5Layout.createSequentialGroup()
                                                                .addGap(0, 0, Short.MAX_VALUE)
                                                                .addComponent(button1,
                                                                        GroupLayout.PREFERRED_SIZE,
                                                                        GroupLayout.DEFAULT_SIZE,
                                                                        GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                        LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(button4,
                                                                        GroupLayout.PREFERRED_SIZE,
                                                                        GroupLayout.DEFAULT_SIZE,
                                                                        GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(roundedPanel5Layout.createSequentialGroup()
                                                                .addGroup(roundedPanel5Layout.createParallelGroup(
                                                                        GroupLayout.Alignment.LEADING)
                                                                        .addGroup(roundedPanel5Layout
                                                                                .createSequentialGroup()
                                                                                .addComponent(myTextField1,
                                                                                        GroupLayout.PREFERRED_SIZE,
                                                                                        GroupLayout.DEFAULT_SIZE,
                                                                                        GroupLayout.PREFERRED_SIZE)
                                                                                .addPreferredGap(
                                                                                        LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                .addComponent(myTextField2,
                                                                                        GroupLayout.PREFERRED_SIZE,
                                                                                        GroupLayout.DEFAULT_SIZE,
                                                                                        GroupLayout.PREFERRED_SIZE))
                                                                        .addGroup(roundedPanel5Layout
                                                                                .createSequentialGroup()
                                                                                .addGroup(roundedPanel5Layout
                                                                                        .createParallelGroup(
                                                                                                GroupLayout.Alignment.BASELINE)
                                                                                        .addComponent(jCheckBoxCustom1,
                                                                                                GroupLayout.PREFERRED_SIZE,
                                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                                GroupLayout.PREFERRED_SIZE)
                                                                                        .addComponent(jLabel8))
                                                                                .addGroup(roundedPanel5Layout
                                                                                        .createParallelGroup(
                                                                                                GroupLayout.Alignment.LEADING)
                                                                                        .addGroup(roundedPanel5Layout
                                                                                                .createSequentialGroup()
                                                                                                .addGap(28, 28, 28)
                                                                                                .addComponent(jLabel9))
                                                                                        .addGroup(roundedPanel5Layout
                                                                                                .createSequentialGroup()
                                                                                                .addGap(22, 22, 22)
                                                                                                .addComponent(
                                                                                                        jCheckBoxCustom2,
                                                                                                        GroupLayout.PREFERRED_SIZE,
                                                                                                        GroupLayout.DEFAULT_SIZE,
                                                                                                        GroupLayout.PREFERRED_SIZE)
                                                                                                .addGap(15, 15, 15)
                                                                                                .addComponent(
                                                                                                        jCheckBoxCustom3,
                                                                                                        GroupLayout.PREFERRED_SIZE,
                                                                                                        GroupLayout.DEFAULT_SIZE,
                                                                                                        GroupLayout.PREFERRED_SIZE)
                                                                                                .addGap(20, 20, 20)
                                                                                                .addGroup(
                                                                                                        roundedPanel5Layout
                                                                                                                .createParallelGroup(
                                                                                                                        GroupLayout.Alignment.LEADING)
                                                                                                                .addComponent(
                                                                                                                        jLabel13)
                                                                                                                .addComponent(
                                                                                                                        comboBoxSuggestion1,
                                                                                                                        GroupLayout.PREFERRED_SIZE,
                                                                                                                        GroupLayout.DEFAULT_SIZE,
                                                                                                                        GroupLayout.PREFERRED_SIZE)
                                                                                                                .addGroup(
                                                                                                                        roundedPanel5Layout
                                                                                                                                .createParallelGroup(
                                                                                                                                        GroupLayout.Alignment.BASELINE)
                                                                                                                                .addComponent(
                                                                                                                                        jLabel7,
                                                                                                                                        GroupLayout.PREFERRED_SIZE,
                                                                                                                                        21,
                                                                                                                                        GroupLayout.PREFERRED_SIZE)
                                                                                                                                .addComponent(
                                                                                                                                        jSpinner1,
                                                                                                                                        GroupLayout.PREFERRED_SIZE,
                                                                                                                                        GroupLayout.DEFAULT_SIZE,
                                                                                                                                        GroupLayout.PREFERRED_SIZE)
                                                                                                                                .addComponent(
                                                                                                                                        jLabel10)
                                                                                                                                .addComponent(
                                                                                                                                        jLabel11)))))))
                                                                .addPreferredGap(
                                                                        LayoutStyle.ComponentPlacement.UNRELATED,
                                                                        GroupLayout.DEFAULT_SIZE,
                                                                        Short.MAX_VALUE)
                                                                .addGroup(roundedPanel5Layout.createParallelGroup(
                                                                        GroupLayout.Alignment.LEADING)
                                                                        .addComponent(jLabel12)
                                                                        .addComponent(button7,
                                                                                GroupLayout.PREFERRED_SIZE,
                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(myPasswordField1,
                                                                                GroupLayout.PREFERRED_SIZE,
                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(
                                                                        LayoutStyle.ComponentPlacement.RELATED,
                                                                        3, Short.MAX_VALUE))))
                                        .addGroup(roundedPanel5Layout.createSequentialGroup()
                                                .addGap(9, 9, 9)
                                                .addGroup(roundedPanel5Layout
                                                        .createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                        .addComponent(jSlider1, GroupLayout.PREFERRED_SIZE,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(roundedPanel5Layout
                                                                .createParallelGroup(
                                                                        GroupLayout.Alignment.LEADING)
                                                                .addComponent(deThiCard21,
                                                                        GroupLayout.PREFERRED_SIZE, 176,
                                                                        GroupLayout.PREFERRED_SIZE)
                                                                .addGroup(roundedPanel5Layout.createSequentialGroup()
                                                                        .addComponent(button5,
                                                                                GroupLayout.PREFERRED_SIZE,
                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                GroupLayout.PREFERRED_SIZE)
                                                                        .addPreferredGap(
                                                                                LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addComponent(button6,
                                                                                GroupLayout.PREFERRED_SIZE,
                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                GroupLayout.PREFERRED_SIZE))))
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addContainerGap()));

        jTable1.setModel(new DefaultTableModel(
                new Object[][] {
                        { null, null },
                        { null, null },
                        { null, null },
                        { null, null }
                },
                new String[] {
                        "Họ tên", "Điểm"
                }) {
            boolean[] canEdit = new boolean[] {
                    false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jPanel1.setBackground(new Color(255, 255, 255));
        jPanel1.add(pagination1);

        GroupLayout roundedPanel4Layout = new GroupLayout(roundedPanel4);
        roundedPanel4.setLayout(roundedPanel4Layout);
        roundedPanel4Layout.setHorizontalGroup(
                roundedPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(circleBackgroundPanel1, GroupLayout.DEFAULT_SIZE,
                                GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(roundedPanel5, GroupLayout.DEFAULT_SIZE,
                                GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(roundedPanel4Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(roundedPanel4Layout
                                        .createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane1)
                                        .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap()));
        roundedPanel4Layout.setVerticalGroup(
                roundedPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(roundedPanel4Layout.createSequentialGroup()
                                .addComponent(circleBackgroundPanel1, GroupLayout.PREFERRED_SIZE,
                                        GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(roundedPanel5, GroupLayout.PREFERRED_SIZE,
                                        GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE,
                                        GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)));

        GroupLayout XemBKTLayout = new GroupLayout(XemBKT.getContentPane());
        XemBKT.getContentPane().setLayout(XemBKTLayout);
        XemBKTLayout.setHorizontalGroup(
                XemBKTLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(roundedPanel4, GroupLayout.DEFAULT_SIZE,
                                GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        XemBKTLayout.setVerticalGroup(
                XemBKTLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(XemBKTLayout.createSequentialGroup()
                                .addComponent(roundedPanel4, GroupLayout.PREFERRED_SIZE,
                                        GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)));

        customDateChooser2.setTextReference(myTextField2);

        DialogThongKe.setTitle("Biểu đồ phân phối điểm");
        DialogThongKe.setIconImage(new ImageIcon(getClass().getResource("/Image/favicon_1.png")).getImage());
        DialogThongKe.setResizable(false);

        chart1.setToolTipText("");

        GroupLayout DialogThongKeLayout = new GroupLayout(DialogThongKe.getContentPane());
        DialogThongKe.getContentPane().setLayout(DialogThongKeLayout);
        DialogThongKeLayout.setHorizontalGroup(
                DialogThongKeLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(DialogThongKeLayout.createSequentialGroup()
                                .addComponent(chart1, GroupLayout.PREFERRED_SIZE,
                                        GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)));
        DialogThongKeLayout.setVerticalGroup(
                DialogThongKeLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(DialogThongKeLayout.createSequentialGroup()
                                .addComponent(chart1, GroupLayout.PREFERRED_SIZE,
                                        GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)));

        GroupLayout jDialog1Layout = new GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
                jDialog1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jDialog1Layout.createSequentialGroup()
                                .addComponent(listDeThi21, GroupLayout.PREFERRED_SIZE, 898,
                                        GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)));
        jDialog1Layout.setVerticalGroup(
                jDialog1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(listDeThi21, GroupLayout.DEFAULT_SIZE, 512, Short.MAX_VALUE));

        DialogXemLai.setModal(true);
        DialogXemLai.setResizable(false);

        jPanel2.setBackground(new Color(255, 255, 255));

        GroupLayout jPanel2Layout = new GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(listKetQuaKiemTra1, GroupLayout.PREFERRED_SIZE,
                                        GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)));
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGap(0, 0, 0)
                                .addComponent(listKetQuaKiemTra1, GroupLayout.PREFERRED_SIZE, 479,
                                        GroupLayout.PREFERRED_SIZE)
                                .addGap(1, 1, 1)));

        GroupLayout DialogXemLaiLayout = new GroupLayout(DialogXemLai.getContentPane());
        DialogXemLai.getContentPane().setLayout(DialogXemLaiLayout);
        DialogXemLaiLayout.setHorizontalGroup(
                DialogXemLaiLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel2, GroupLayout.DEFAULT_SIZE,
                                GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        DialogXemLaiLayout.setVerticalGroup(
                DialogXemLaiLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(DialogXemLaiLayout.createSequentialGroup()
                                .addComponent(jPanel2, GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(0, 0, 0)));

        roundedPanel1.setBackground(new Color(255, 255, 255));

        roundedPanel2.setBackground(new Color(58, 138, 125));
        roundedPanel2.setPreferredSize(new Dimension(72, 72));

        jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel1.setIcon(new ImageIcon(getClass().getResource("/Image/icons8-exam-30-reverse.png"))); // NOI18N

        GroupLayout roundedPanel2Layout = new GroupLayout(roundedPanel2);
        roundedPanel2.setLayout(roundedPanel2Layout);
        roundedPanel2Layout.setHorizontalGroup(
                roundedPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(roundedPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1, GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                                .addContainerGap()));
        roundedPanel2Layout.setVerticalGroup(
                roundedPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(roundedPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1, GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                                .addContainerGap()));

        jLabel2.setFont(new Font("Segoe UI", 3, 14)); // NOI18N
        jLabel2.setText("jLabel2");

        jLabel3.setIcon(new ImageIcon(getClass().getResource("/Image/icons8-calendar-18.png"))); // NOI18N
        jLabel3.setText("jLabel3");

        jLabel4.setIcon(new ImageIcon(getClass().getResource("/Image/icons8-calendar-18.png"))); // NOI18N
        jLabel4.setText("jLabel4");

        jLabel5.setIcon(new ImageIcon(getClass().getResource("/Image/icons8-time-18 (1).png"))); // NOI18N
        jLabel5.setText("jLabel5");

        jLabel6.setIcon(new ImageIcon(getClass().getResource("/Image/icons8-how-many-quest-18.png"))); // NOI18N
        jLabel6.setText("jLabel6");

        roundedPanel3.setPreferredSize(new Dimension(72, 72));

        button2.setBackground(new Color(0, 0, 0));
        button2.setForeground(new Color(255, 255, 255));
        button2.setText("Tham gia");
        button2.setFont(new Font("Segoe UI", 1, 14)); // NOI18N
        button2.setPreferredSize(new Dimension(80, 26));
        roundedPanel3.add(button2);

        button3.setText("Xem lại");
        button3.setPreferredSize(new Dimension(80, 26));
        roundedPanel3.add(button3);

        GroupLayout roundedPanel1Layout = new GroupLayout(roundedPanel1);
        roundedPanel1.setLayout(roundedPanel1Layout);
        roundedPanel1Layout.setHorizontalGroup(
                roundedPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(roundedPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(roundedPanel2, GroupLayout.PREFERRED_SIZE,
                                        GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(roundedPanel1Layout
                                        .createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(roundedPanel1Layout.createSequentialGroup()
                                                .addGroup(roundedPanel1Layout
                                                        .createParallelGroup(GroupLayout.Alignment.LEADING,
                                                                false)
                                                        .addComponent(jLabel3, GroupLayout.DEFAULT_SIZE,
                                                                GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jLabel4, GroupLayout.DEFAULT_SIZE,
                                                                139, Short.MAX_VALUE))
                                                .addGap(33, 33, 33)
                                                .addGroup(roundedPanel1Layout
                                                        .createParallelGroup(GroupLayout.Alignment.LEADING,
                                                                false)
                                                        .addComponent(jLabel5, GroupLayout.DEFAULT_SIZE,
                                                                170, Short.MAX_VALUE)
                                                        .addComponent(jLabel6, GroupLayout.DEFAULT_SIZE,
                                                                GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                        .addComponent(jLabel2, GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addComponent(roundedPanel3, GroupLayout.PREFERRED_SIZE, 94,
                                        GroupLayout.PREFERRED_SIZE)
                                .addContainerGap()));
        roundedPanel1Layout.setVerticalGroup(
                roundedPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(roundedPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(roundedPanel1Layout
                                        .createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(roundedPanel3, GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE)
                                        .addGroup(roundedPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel2)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(roundedPanel1Layout
                                                        .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel3)
                                                        .addComponent(jLabel5))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(roundedPanel1Layout
                                                        .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel4)
                                                        .addComponent(jLabel6)))
                                        .addComponent(roundedPanel2, GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE))
                                .addContainerGap()));

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(roundedPanel1, GroupLayout.DEFAULT_SIZE,
                                GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(roundedPanel1, GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(0, 0, 0)));
    }// </editor-fold>//GEN-END:initComponents

    private void button4ActionPerformed(ActionEvent evt) throws RemoteException {// GEN-FIRST:event_button4ActionPerformed
        if(deThiCard21.getDethi().getMaDeThi()!=dt_dao.getDeThi(bkt.getDeThi().getMaDeThi()).getMaDeThi()){
             if(customDateChooser1.getSelectedDateTime().isAfter(customDateChooser2.getSelectedDateTime())) {
            JOptionPane.showMessageDialog(null, "Thời gian bắt đầu phải sau thời gian kết thúc!");
        }else{
            BaiKiemTra bktUpdate = new BaiKiemTra(bkt.getMaBaiKiemTra(),
                    customDateChooser1.getSelectedDateTime(), customDateChooser2.getSelectedDateTime(),
                    jSlider1.getValue(), new String(myPasswordField1.getPassword()).trim(),
                    jCheckBoxCustom1.isSelected(), jCheckBoxCustom2.isSelected(), jCheckBoxCustom3.isSelected(),
                    (Integer) jSpinner1.getValue(),
                    10, Float.parseFloat(comboBoxSuggestion1.getSelectedItem().toString()),
                    "enable", deThiCard21.getDethi(), GV_ClassRoom_Detail.lopHoc);
            if (bkt_dao.updateBaiKiemTra(bktUpdate)) {
                JOptionPane.showMessageDialog(null, "Cập nhật bài kiểm tra thành công");
                GV_ClassRoom_Detail.loadBKT();
            } else {
                JOptionPane.showMessageDialog(null, "Cập nhật bài kiểm tra thất bại");
            }
        }
            
        }else{
            if (bkt.getThoiGianBatDau().isBefore(LocalDateTime.now())) {
                JOptionPane.showMessageDialog(null, "Thời gian bắt đầu dã qua, không thể cập nhât!");

            } else if (customDateChooser1.getSelectedDateTime().isAfter(customDateChooser2.getSelectedDateTime())) {
                JOptionPane.showMessageDialog(null, "Thời gian bắt đầu phải sau thời gian kết thúc!");
            } else {
                BaiKiemTra bktUpdate = new BaiKiemTra(bkt.getMaBaiKiemTra(),
                        customDateChooser1.getSelectedDateTime(), customDateChooser2.getSelectedDateTime(),
                        jSlider1.getValue(), new String(myPasswordField1.getPassword()).trim(),
                        jCheckBoxCustom1.isSelected(), jCheckBoxCustom2.isSelected(), jCheckBoxCustom3.isSelected(),
                        (Integer) jSpinner1.getValue(),
                        10, Float.parseFloat(comboBoxSuggestion1.getSelectedItem().toString()),
                        "enable", deThiCard21.getDethi(), GV_ClassRoom_Detail.lopHoc);
                if (bkt_dao.updateBaiKiemTra(bktUpdate)) {
                    JOptionPane.showMessageDialog(null, "Cập nhật bài kiểm tra thành công");
                    GV_ClassRoom_Detail.loadBKT();
                } else {
                    JOptionPane.showMessageDialog(null, "Cập nhật bài kiểm tra thất bại");
                }
            }
        }
        
    }// GEN-LAST:event_button4ActionPerformed

    private boolean eye = false;

    private void button7ActionPerformed(ActionEvent evt) {// GEN-FIRST:event_button7ActionPerformed
        if (!eye) {
            myPasswordField1.setEchoChar((char) 0);
            button7.setIcon(new ImageIcon(BaiKiemTraCard.class.getResource("/Image/eye_open.png")));
            eye = true;

        } else {
            myPasswordField1.setEchoChar('*');
            button7.setIcon(new ImageIcon(BaiKiemTraCard.class.getResource("/Image/eye_closed.png")));
            eye = false;
        }
    }// GEN-LAST:event_button7ActionPerformed

    private void myPasswordField1ActionPerformed(ActionEvent evt) {// GEN-FIRST:event_myPasswordField1ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_myPasswordField1ActionPerformed

    private void myTextField1ActionPerformed(ActionEvent evt) {// GEN-FIRST:event_myTextField1ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_myTextField1ActionPerformed

    private void myTextField2ActionPerformed(ActionEvent evt) {// GEN-FIRST:event_myTextField2ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_myTextField2ActionPerformed

    private void jSlider1StateChanged(javax.swing.event.ChangeEvent evt) {// GEN-FIRST:event_jSlider1StateChanged
        jLabel11.setText(jSlider1.getValue() + " phút");
    }// GEN-LAST:event_jSlider1StateChanged

    private void button1ActionPerformed(ActionEvent evt) throws RemoteException {// GEN-FIRST:event_button1ActionPerformed
        initData();
    }// GEN-LAST:event_button1ActionPerformed

    private int quantity;

    private void button6ActionPerformed(ActionEvent evt) throws RemoteException {// GEN-FIRST:event_button6ActionPerformed
        String maBaiKiemTra = bkt.getMaBaiKiemTra();
        Map<TaiKhoan, Float> map = bkt_dao.getDsTaiKhoanThamGiaKiemTraVaDiemSo(maBaiKiemTra);

        int[] thongKeDiem = new int[10];
        quantity = 0;
        for (Float diem : map.values()) {
            if (diem != null) {
                int diemLamTron = (int) Math.ceil(diem);
                if (diemLamTron >= 1 && diemLamTron <= 10) {
                    thongKeDiem[diemLamTron - 1]++;
                    quantity++;
                }
            }
        }

        chart1.clearData();
        chart1.addLegend("Số điểm sinh viên", Color.decode("#3a8a7d"));
        for (int i = 0; i < thongKeDiem.length; i++) {
            String nhan = String.valueOf(i + 1);
            chart1.addData(new ModelChart(nhan, new double[] { thongKeDiem[i] }));
        }

        DialogThongKe.pack();
        DialogThongKe.setModal(true);
        DialogThongKe.setLocationRelativeTo(null);
        DialogThongKe.setVisible(true);
    }// GEN-LAST:event_button6ActionPerformed

    private void button5ActionPerformed(ActionEvent evt) throws RemoteException {// GEN-FIRST:event_button5ActionPerformed
        initDSDeThi();
        jDialog1.pack();
        jDialog1.setModal(true);
        jDialog1.setLocationRelativeTo(null);
        jDialog1.setVisible(true);
    }// GEN-LAST:event_button5ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JDialog DialogThongKe;
    private JDialog DialogXemLai;
    private JDialog XemBKT;
    private Button button1;
    private Button button2;
    private Button button3;
    public static Button button4;
    private Button button5;
    private Button button6;
    private Button button7;
    private Components.chart.Chart chart1;
    private CircleBackgroundPanel circleBackgroundPanel1;
    private ComboBoxSuggestion comboBoxSuggestion1;
    private CustomDateTimeChooser customDateChooser1;
    private CustomDateTimeChooser customDateChooser2;
    private DeThiCard2 deThiCard21;
    private JCheckBoxCustom jCheckBoxCustom1;
    private JCheckBoxCustom jCheckBoxCustom2;
    private JCheckBoxCustom jCheckBoxCustom3;
    private JDialog jDialog1;
    private JLabel jLabel1;
    private JLabel jLabel10;
    private JLabel jLabel11;
    private JLabel jLabel12;
    private JLabel jLabel13;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JLabel jLabel7;
    private JLabel jLabel8;
    private JLabel jLabel9;
    private JPanel jPanel1;
    private JPanel jPanel2;
    private JScrollPane jScrollPane1;
    private JSlider jSlider1;
    private JSpinner jSpinner1;
    private JTable jTable1;
    private ListDeThi2 listDeThi21;
    private ListKetQuaKiemTra listKetQuaKiemTra1;
    private MyPasswordField myPasswordField1;
    private MyTextField myTextField1;
    private MyTextField myTextField2;
    private Pagination pagination1;
    private RoundedPanel roundedPanel1;
    private RoundedPanel roundedPanel2;
    private RoundedPanel roundedPanel3;
    private RoundedPanel roundedPanel4;
    private RoundedPanel roundedPanel5;
    private ThongKeCard thongKeCard1;
    private ThongKeCard thongKeCard2;
    // End of variables declaration//GEN-END:variables
}
