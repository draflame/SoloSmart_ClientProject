/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package GUI;

import Components.*;
import Dao.IKetQuaHocTap_DAO;
import Dao.ILopHoc_DAO;
import Dao.IMonHoc_DAO;
import Dao.ITaiKhoan_DAO;
import Entity.KetQuaHocTap;
import Entity.LopHoc;
import Entity.MonHoc;
import Entity.TaiKhoan;
import jnafilechooser.api.JnaFileChooser;
import service.RmiServiceLocator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 *
 * @author THANH PHU
 */
public class Admin_Classrom extends JPanel {

    /**
     * Creates new form Admin_Subject
     */
    private IMonHoc_DAO mh_dao= RmiServiceLocator.getMonHocDao();
    private ILopHoc_DAO lh_dao= RmiServiceLocator.getLopHocDao();
    private ITaiKhoan_DAO tk_dao= RmiServiceLocator.getTaiKhoanDao();
    private IKetQuaHocTap_DAO kqht_dao= RmiServiceLocator.getKetQuaHocTapDao();
    private ImageIcon icon = new ImageIcon(getClass().getResource("/Image/favicon_1.png"));
    private ArrayList<TaiKhoan> listAddStudent= new ArrayList<>();
    private ArrayList<TaiKhoan> listUpdateStudent= new ArrayList<>();
    private ArrayList<KetQuaHocTap> dsKQHT= new ArrayList<>();
    private ArrayList<LopHoc> list= new ArrayList<>();
    public Admin_Classrom() throws RemoteException {
        initComponents();
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        
        TableActionEvent event = new TableActionEvent() {
            @Override
            public void onEdit(int row) throws RemoteException {
                LopHoc lophoc= lh_dao.getLopHoc(jTable1.getValueAt(row, 0).toString());
                
                initEdit(lophoc);
                    
                
                
            }

            @Override
            public void onDelete(int row) throws RemoteException {
                int choice= JOptionPane.showConfirmDialog(null, "Bạn có muốn xóa lớp học này không?", "Xác nhận xóa",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                if(choice==JOptionPane.YES_OPTION){
                    if(lh_dao.deleteLopHoc(jTable1.getValueAt(row, 0).toString())){
                        model.removeRow(row);
                        
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "Xóa không thành công");
                    }
                     jTable1.getCellEditor().cancelCellEditing();
                }
                
                
            }

            @Override
            public void onView(int row) throws RemoteException {
                LopHoc lophoc= lh_dao.getLopHoc(jTable1.getValueAt(row, 0).toString());
                if(lophoc!=null){
                    myTextField2.setText(lophoc.getMaLop());
                    myTextField3.setText(lophoc.getTenLop());
                    myTextField4.setText(lophoc.getNamHoc());
                    myTextField5.setText(lophoc.getSiSo()+"");
                    TaiKhoan tk= tk_dao.getTaiKhoan(lophoc.getGiaoVien().getMaTaiKhoan());
                    myTextField6.setText(tk.getHo()+" "+tk.getTen());
                    MonHoc mh= mh_dao.getMonHoc(lophoc.getMonHoc().getMaMonHoc());
                    myTextField7.setText(mh.getTenMonHoc());
                    ViewDialog.setIconImage(icon.getImage());
                    ViewDialog.pack();
                    ViewDialog.setLocationRelativeTo(null);
                    ViewDialog.setVisible(true);
                }
                                            // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }
        };
        jTable1.getColumnModel().getColumn(4).setCellRenderer(new TableActionCellRender());
        jTable1.getColumnModel().getColumn(4).setCellEditor(new TableActionCellEditor(event));
        initTable();
        
        searchTextField1.addActionListener(x->{
            try {
                list= lh_dao.getDanhSachLopHocByKey(searchTextField1.getText());
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            System.out.println(list.size());
            if(list.size()>0){
                initPag(1);
            }
            else{
                JOptionPane.showMessageDialog(null,"khong tim thay");
            }
            
        });
        pagination1.setPaginationItemRender(new PaginationItemRenderStyle1());
        pagination1.addEventPagination(new EventPagination(){
            @Override
            public void pageChanged(int page) {
                initPag(page);
            }
            
        });
    }
    public void initTable() throws RemoteException {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);
        list= lh_dao.getDanhSachLopHoc();
        initPag(1);
                
    }
    public void initPag(int page){
        int limit= 15;
        int totalPage=(int) Math.ceil((double)list.size()/limit);
        pagination1.setPagegination(page, totalPage);
        int currentPage = page; // Bạn có thể thay đổi thành biến động nếu muốn điều khiển trang
        int skip = (currentPage - 1) * limit;
        
        updateTable(list.stream()
                .skip(skip)
                .limit(limit)
                .collect(Collectors.toCollection(ArrayList::new)));
    }
    public void updateTable(ArrayList<LopHoc> list){
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);
        list.forEach(x->{
            model.addRow(new Object[]{
                x.getMaLop(),
                x.getTenLop(),
                x.getNamHoc(),
                x.getSiSo()
            });
        });
                
    }
    public void updateTableAddStudent(ArrayList<TaiKhoan> list){
        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
        model.setRowCount(0);
        list.forEach(x->{
            model.addRow(new Object[]{
                x.getMaTaiKhoan(),
                x.getHo()+" "+x.getTen(),
                x.getGioiTinh()
            });
        });
    }
    public void updateSisoThemLopHoc(){
        myTextField8.setText(listAddStudent.size()+"");
    }
    public void initEdit(LopHoc lophoc) throws RemoteException {
        if(lophoc!=null){
                    myTextField10.setText(lophoc.getTenLop());
                    ArrayList<TaiKhoan> tkGV=tk_dao.getDanhSachTaiKhoanGV();
                    tkGV.forEach(x->{
                        comboBoxSuggestion6.addItem(x.getHo()+" "+x.getTen());
                    });
                    TaiKhoan gv= tk_dao.getTaiKhoan(lophoc.getGiaoVien().getMaTaiKhoan());
                    comboBoxSuggestion6.setSelectedItem(gv.getHo()+" "+gv.getTen());
                    ArrayList<MonHoc> listMH= mh_dao.getDanhSachMonHoc();
                    listMH.forEach(x->{
                        comboBoxSuggestion4.addItem(x.getTenMonHoc());
                    });
                    MonHoc mh= mh_dao.getMonHoc(lophoc.getMonHoc().getMaMonHoc());
                    comboBoxSuggestion4.setSelectedItem(mh.getTenMonHoc());
                    
                    for(int i=0;i<10;i++){
                        LocalDateTime now= LocalDateTime.now().plusYears(i);
                        LocalDateTime nextYear= now.plusYears(1);
                        String date=now.getYear()+"-"+nextYear.getYear();
                        comboBoxSuggestion5.addItem(date);

                    }
                    comboBoxSuggestion5.setSelectedItem(lophoc.getNamHoc());
                    
                    ArrayList<KetQuaHocTap> kqht= kqht_dao.getDanhSachKetQuaHocTap(lophoc.getMaLop());
                    dsKQHT= new ArrayList<>(kqht);
                    myTextField11.setText(kqht.size()+"");
                    EditDialog.pack();
                    EditDialog.setIconImage(icon.getImage());
                    EditDialog.setLocationRelativeTo(null);
                    EditDialog.setModal(true);
                     EditDialog.setVisible(true);
                    
        }            
    }
    public boolean isExitTable(JTable table, String ma){
        for( int i=0;i<table.getRowCount();i++){
            if(table.getValueAt(i, 0).toString().equalsIgnoreCase(ma)){
                return true;
            }
        }
        return false;
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        AddDialog = new JDialog();
        roundedGradientPanel1 = new RoundedGradientPanel();
        jLabel1 = new JLabel();
        myTextField1 = new MyTextField();
        jLabel3 = new JLabel();
        jLabel5 = new JLabel();
        comboBoxSuggestion1 = new ComboBoxSuggestion();
        button3 = new Button();
        comboBoxSuggestion2 = new ComboBoxSuggestion();
        jLabel13 = new JLabel();
        jLabel14 = new JLabel();
        myTextField8 = new MyTextField();
        button5 = new Button();
        comboBoxSuggestion3 = new ComboBoxSuggestion();
        jLabel15 = new JLabel();
        circleBackgroundPanel2 = new CircleBackgroundPanel();
        jLabel17 = new JLabel();
        ViewDialog = new JDialog();
        roundedGradientPanel2 = new RoundedGradientPanel();
        jLabel4 = new JLabel();
        jLabel6 = new JLabel();
        myTextField2 = new MyTextField();
        jLabel7 = new JLabel();
        button4 = new Button();
        myTextField3 = new MyTextField();
        jLabel8 = new JLabel();
        myTextField4 = new MyTextField();
        jLabel9 = new JLabel();
        myTextField5 = new MyTextField();
        jLabel10 = new JLabel();
        myTextField6 = new MyTextField();
        jLabel11 = new JLabel();
        myTextField7 = new MyTextField();
        jLabel12 = new JLabel();
        AddStudents = new JDialog();
        roundedPanel2 = new RoundedPanel();
        jScrollPane2 = new JScrollPane();
        jTable2 = new JTable();
        button6 = new Button();
        button7 = new Button();
        myTextField9 = new MyTextField();
        jLabel16 = new JLabel();
        button8 = new Button();
        EditDialog = new JDialog();
        roundedGradientPanel3 = new RoundedGradientPanel();
        jLabel2 = new JLabel();
        myTextField10 = new MyTextField();
        jLabel18 = new JLabel();
        jLabel19 = new JLabel();
        comboBoxSuggestion4 = new ComboBoxSuggestion();
        button9 = new Button();
        comboBoxSuggestion5 = new ComboBoxSuggestion();
        jLabel20 = new JLabel();
        jLabel21 = new JLabel();
        myTextField11 = new MyTextField();
        button10 = new Button();
        comboBoxSuggestion6 = new ComboBoxSuggestion();
        jLabel22 = new JLabel();
        circleBackgroundPanel3 = new CircleBackgroundPanel();
        jLabel23 = new JLabel();
        UpdateStudent = new JDialog();
        roundedPanel3 = new RoundedPanel();
        jScrollPane3 = new JScrollPane();
        jTable3 = new JTable();
        button11 = new Button();
        button12 = new Button();
        myTextField12 = new MyTextField();
        jLabel24 = new JLabel();
        button13 = new Button();
        roundedPanel1 = new RoundedPanel();
        jScrollPane1 = new JScrollPane();
        jTable1 = new JTable();
        circleBackgroundPanel1 = new CircleBackgroundPanel();
        button2 = new Button();
        button1 = new Button();
        searchTextField1 = new SearchTextField();
        jPanel1 = new JPanel();
        pagination1 = new Pagination();

        AddDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        AddDialog.setTitle("Thêm lớp học");
        AddDialog.setResizable(false);

        roundedGradientPanel1.setColor1(new java.awt.Color(255, 255, 255));
        roundedGradientPanel1.setColor2(new java.awt.Color(255, 255, 255));

        jLabel1.setIcon(new ImageIcon(getClass().getResource("/Image/Classroom-bro (1).png"))); // NOI18N

        myTextField1.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)), BorderFactory.createEmptyBorder(1, 2, 1, 12)));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel3.setText("Tên lớp học:");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel5.setText("Tên môn học:");

        comboBoxSuggestion1.setPreferredSize(new java.awt.Dimension(59, 36));

        button3.setBackground(new java.awt.Color(58, 138, 125));
        button3.setForeground(new java.awt.Color(255, 255, 255));
        button3.setText("Xác nhận");
        button3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        button3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    button3ActionPerformed(evt);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        comboBoxSuggestion2.setPreferredSize(new java.awt.Dimension(59, 36));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel13.setText("Năm học:");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel14.setText("Sỉ số:");

        myTextField8.setEditable(false);
        myTextField8.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)), BorderFactory.createEmptyBorder(1, 2, 1, 12)));
        myTextField8.setText("0");

        button5.setBackground(new java.awt.Color(58, 138, 125));
        button5.setIcon(new ImageIcon(getClass().getResource("/Image/icons8-add-male-user-group-30.png"))); // NOI18N
        button5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button5ActionPerformed(evt);
            }
        });

        comboBoxSuggestion3.setPreferredSize(new java.awt.Dimension(59, 36));

        jLabel15.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel15.setText("Giảng viên:");

        circleBackgroundPanel2.setColor1(new java.awt.Color(58, 138, 125));

        jLabel17.setFont(new java.awt.Font("Segoe UI", 3, 24)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel17.setText("Thêm lớp học");

        GroupLayout circleBackgroundPanel2Layout = new GroupLayout(circleBackgroundPanel2);
        circleBackgroundPanel2.setLayout(circleBackgroundPanel2Layout);
        circleBackgroundPanel2Layout.setHorizontalGroup(
            circleBackgroundPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(circleBackgroundPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        circleBackgroundPanel2Layout.setVerticalGroup(
            circleBackgroundPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(circleBackgroundPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(21, 21, 21))
        );

        GroupLayout roundedGradientPanel1Layout = new GroupLayout(roundedGradientPanel1);
        roundedGradientPanel1.setLayout(roundedGradientPanel1Layout);
        roundedGradientPanel1Layout.setHorizontalGroup(
            roundedGradientPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(roundedGradientPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 507, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(roundedGradientPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(myTextField1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(comboBoxSuggestion1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(button3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(roundedGradientPanel1Layout.createSequentialGroup()
                        .addGroup(roundedGradientPanel1Layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                            .addComponent(comboBoxSuggestion2, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                            .addComponent(jLabel5, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                            .addComponent(jLabel13, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(roundedGradientPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
                            .addComponent(myTextField8, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(button5, GroupLayout.PREFERRED_SIZE, 68, GroupLayout.PREFERRED_SIZE))
                    .addComponent(comboBoxSuggestion3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(roundedGradientPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel15, GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                        .addGap(219, 219, 219))
                    .addComponent(circleBackgroundPanel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        roundedGradientPanel1Layout.setVerticalGroup(
            roundedGradientPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(roundedGradientPanel1Layout.createSequentialGroup()
                .addGroup(roundedGradientPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addGroup(roundedGradientPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(circleBackgroundPanel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addGap(7, 7, 7)
                        .addComponent(myTextField1, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(roundedGradientPanel1Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                            .addGroup(roundedGradientPanel1Layout.createSequentialGroup()
                                .addComponent(comboBoxSuggestion1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel13)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(roundedGradientPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                    .addComponent(button5, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(comboBoxSuggestion2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                            .addGroup(roundedGradientPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel14)
                                .addGroup(roundedGradientPanel1Layout.createSequentialGroup()
                                    .addGap(22, 22, 22)
                                    .addComponent(myTextField8, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE))))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel15)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboBoxSuggestion3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(button3, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1))
                .addGap(10, 10, 10))
        );

        GroupLayout AddDialogLayout = new GroupLayout(AddDialog.getContentPane());
        AddDialog.getContentPane().setLayout(AddDialogLayout);
        AddDialogLayout.setHorizontalGroup(
            AddDialogLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(AddDialogLayout.createSequentialGroup()
                .addComponent(roundedGradientPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        AddDialogLayout.setVerticalGroup(
            AddDialogLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(roundedGradientPanel1, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        ViewDialog.setTitle("Thông tin lớp học");
        ViewDialog.setResizable(false);

        roundedGradientPanel2.setColor1(new java.awt.Color(61, 141, 122));
        roundedGradientPanel2.setColor2(new java.awt.Color(116, 195, 174));

        jLabel4.setIcon(new ImageIcon(getClass().getResource("/Image/Classroom-bro (1).png"))); // NOI18N

        jLabel6.setFont(new java.awt.Font("Segoe UI", 3, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel6.setText("Thông tin lớp học");

        myTextField2.setEditable(false);
        myTextField2.setFocusable(false);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Mã lớp học:");

        button4.setBackground(new java.awt.Color(58, 138, 125));
        button4.setForeground(new java.awt.Color(255, 255, 255));
        button4.setText("Xác nhận");
        button4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        button4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button4ActionPerformed(evt);
            }
        });

        myTextField3.setEditable(false);
        myTextField3.setFocusable(false);

        jLabel8.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Tên lớp học:");

        myTextField4.setEditable(false);
        myTextField4.setFocusable(false);

        jLabel9.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Năm học:");

        myTextField5.setEditable(false);
        myTextField5.setFocusable(false);

        jLabel10.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Sỉ số:");

        myTextField6.setEditable(false);
        myTextField6.setFocusable(false);

        jLabel11.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Giảng viên:");

        myTextField7.setEditable(false);
        myTextField7.setFocusable(false);

        jLabel12.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Môn học:");

        GroupLayout roundedGradientPanel2Layout = new GroupLayout(roundedGradientPanel2);
        roundedGradientPanel2.setLayout(roundedGradientPanel2Layout);
        roundedGradientPanel2Layout.setHorizontalGroup(
            roundedGradientPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(roundedGradientPanel2Layout.createSequentialGroup()
                .addComponent(jLabel4, GroupLayout.PREFERRED_SIZE, 507, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(roundedGradientPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, GroupLayout.DEFAULT_SIZE, 339, Short.MAX_VALUE)
                    .addComponent(myTextField2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(button4, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(myTextField3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(roundedGradientPanel2Layout.createSequentialGroup()
                        .addGroup(roundedGradientPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
                            .addComponent(myTextField4, GroupLayout.PREFERRED_SIZE, 148, GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 43, Short.MAX_VALUE)
                        .addGroup(roundedGradientPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
                            .addComponent(myTextField5, GroupLayout.PREFERRED_SIZE, 148, GroupLayout.PREFERRED_SIZE)))
                    .addComponent(myTextField6, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(roundedGradientPanel2Layout.createSequentialGroup()
                        .addGroup(roundedGradientPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(myTextField7, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        roundedGradientPanel2Layout.setVerticalGroup(
            roundedGradientPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(roundedGradientPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(roundedGradientPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(roundedGradientPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel4))
                    .addGroup(roundedGradientPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel6, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel7)
                        .addGap(7, 7, 7)
                        .addComponent(myTextField2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel8)
                        .addGap(7, 7, 7)
                        .addComponent(myTextField3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(roundedGradientPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10))
                        .addGap(7, 7, 7)
                        .addGroup(roundedGradientPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(myTextField4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(myTextField5, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel11)
                        .addGap(7, 7, 7)
                        .addComponent(myTextField6, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel12)
                        .addGap(7, 7, 7)
                        .addComponent(myTextField7, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                        .addComponent(button4, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))))
        );

        GroupLayout ViewDialogLayout = new GroupLayout(ViewDialog.getContentPane());
        ViewDialog.getContentPane().setLayout(ViewDialogLayout);
        ViewDialogLayout.setHorizontalGroup(
            ViewDialogLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(ViewDialogLayout.createSequentialGroup()
                .addComponent(roundedGradientPanel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        ViewDialogLayout.setVerticalGroup(
            ViewDialogLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(roundedGradientPanel2, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        AddStudents.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        AddStudents.setResizable(false);

        roundedPanel2.setBackground(new java.awt.Color(58, 138, 125));

        jScrollPane2.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane2.setOpaque(false);
        jScrollPane2.setVerticalScrollBar(new ScrollBarCustom());

        jTable2.setModel(new DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã tài khoản", "Họ tên", "Giới tính", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.setRowHeight(40);
        jTable2.setSelectionBackground(new java.awt.Color(255, 255, 255));
        jTable2.getTableHeader().setResizingAllowed(false);
        jTable2.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(jTable2);

        button6.setBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        button6.setForeground(new java.awt.Color(51, 51, 51));
        button6.setIcon(new ImageIcon(getClass().getResource("/Image/icons8-excel-30.png"))); // NOI18N
        button6.setText("Thêm từ Excel");
        button6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        button6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    button6ActionPerformed(evt);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        button7.setBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        button7.setForeground(new java.awt.Color(51, 51, 51));
        button7.setIcon(new ImageIcon(getClass().getResource("/Image/icons8-add-30 (1).png"))); // NOI18N
        button7.setText("Thêm sinh viên");
        button7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        button7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    button7ActionPerformed(evt);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Mã sinh viên:");

        button8.setBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        button8.setText("Xác nhận");
        button8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        button8.setPreferredSize(new java.awt.Dimension(42, 36));
        button8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    button8ActionPerformed(evt);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        GroupLayout roundedPanel2Layout = new GroupLayout(roundedPanel2);
        roundedPanel2.setLayout(roundedPanel2Layout);
        roundedPanel2Layout.setHorizontalGroup(
            roundedPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(roundedPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(myTextField9, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(button7, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(button6, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(roundedPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel16, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 100, Short.MAX_VALUE))
                    .addComponent(button8, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, GroupLayout.PREFERRED_SIZE, 559, GroupLayout.PREFERRED_SIZE))
        );
        roundedPanel2Layout.setVerticalGroup(
            roundedPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 491, Short.MAX_VALUE)
            .addGroup(roundedPanel2Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel16)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(myTextField9, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(button7, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(button6, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(button8, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );

        GroupLayout AddStudentsLayout = new GroupLayout(AddStudents.getContentPane());
        AddStudents.getContentPane().setLayout(AddStudentsLayout);
        AddStudentsLayout.setHorizontalGroup(
            AddStudentsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(roundedPanel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        );
        AddStudentsLayout.setVerticalGroup(
            AddStudentsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(roundedPanel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        EditDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        EditDialog.setTitle("Cập nhật lớp học");
        EditDialog.setResizable(false);

        roundedGradientPanel3.setColor1(new java.awt.Color(255, 255, 255));
        roundedGradientPanel3.setColor2(new java.awt.Color(255, 255, 255));

        jLabel2.setIcon(new ImageIcon(getClass().getResource("/Image/Classroom-bro (1).png"))); // NOI18N

        myTextField10.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)), BorderFactory.createEmptyBorder(1, 2, 1, 12)));

        jLabel18.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel18.setText("Tên lớp học:");

        jLabel19.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel19.setText("Tên môn học:");

        comboBoxSuggestion4.setPreferredSize(new java.awt.Dimension(59, 36));

        button9.setBackground(new java.awt.Color(58, 138, 125));
        button9.setForeground(new java.awt.Color(255, 255, 255));
        button9.setText("Xác nhận");
        button9.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        button9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    button9ActionPerformed(evt);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        comboBoxSuggestion5.setPreferredSize(new java.awt.Dimension(59, 36));

        jLabel20.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel20.setText("Năm học:");

        jLabel21.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel21.setText("Sỉ số:");

        myTextField11.setEditable(false);
        myTextField11.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)), BorderFactory.createEmptyBorder(1, 2, 1, 12)));
        myTextField11.setText("0");

        button10.setBackground(new java.awt.Color(58, 138, 125));
        button10.setIcon(new ImageIcon(getClass().getResource("/Image/icons8-add-male-user-group-30.png"))); // NOI18N
        button10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button10ActionPerformed(evt);
            }
        });

        comboBoxSuggestion6.setPreferredSize(new java.awt.Dimension(59, 36));

        jLabel22.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel22.setText("Giảng viên:");

        circleBackgroundPanel3.setColor1(new java.awt.Color(58, 138, 125));

        jLabel23.setFont(new java.awt.Font("Segoe UI", 3, 24)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel23.setText("Cập nhật lớp học");

        GroupLayout circleBackgroundPanel3Layout = new GroupLayout(circleBackgroundPanel3);
        circleBackgroundPanel3.setLayout(circleBackgroundPanel3Layout);
        circleBackgroundPanel3Layout.setHorizontalGroup(
            circleBackgroundPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(circleBackgroundPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel23, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        circleBackgroundPanel3Layout.setVerticalGroup(
            circleBackgroundPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(circleBackgroundPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel23, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(21, 21, 21))
        );

        GroupLayout roundedGradientPanel3Layout = new GroupLayout(roundedGradientPanel3);
        roundedGradientPanel3.setLayout(roundedGradientPanel3Layout);
        roundedGradientPanel3Layout.setHorizontalGroup(
            roundedGradientPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(roundedGradientPanel3Layout.createSequentialGroup()
                .addComponent(jLabel2, GroupLayout.PREFERRED_SIZE, 507, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(roundedGradientPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(myTextField10, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(comboBoxSuggestion4, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(button9, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(roundedGradientPanel3Layout.createSequentialGroup()
                        .addGroup(roundedGradientPanel3Layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                            .addComponent(comboBoxSuggestion5, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel18, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                            .addComponent(jLabel19, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                            .addComponent(jLabel20, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(roundedGradientPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel21, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
                            .addComponent(myTextField11, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(button10, GroupLayout.PREFERRED_SIZE, 68, GroupLayout.PREFERRED_SIZE))
                    .addComponent(comboBoxSuggestion6, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(roundedGradientPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel22, GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                        .addGap(219, 219, 219))
                    .addComponent(circleBackgroundPanel3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        roundedGradientPanel3Layout.setVerticalGroup(
            roundedGradientPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(roundedGradientPanel3Layout.createSequentialGroup()
                .addGroup(roundedGradientPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addGroup(roundedGradientPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(circleBackgroundPanel3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel18)
                        .addGap(7, 7, 7)
                        .addComponent(myTextField10, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel19)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(roundedGradientPanel3Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                            .addGroup(roundedGradientPanel3Layout.createSequentialGroup()
                                .addComponent(comboBoxSuggestion4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel20)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(roundedGradientPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                    .addComponent(button10, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(comboBoxSuggestion5, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                            .addGroup(roundedGradientPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel21)
                                .addGroup(roundedGradientPanel3Layout.createSequentialGroup()
                                    .addGap(22, 22, 22)
                                    .addComponent(myTextField11, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE))))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel22)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboBoxSuggestion6, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(button9, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel2))
                .addGap(10, 10, 10))
        );

        GroupLayout EditDialogLayout = new GroupLayout(EditDialog.getContentPane());
        EditDialog.getContentPane().setLayout(EditDialogLayout);
        EditDialogLayout.setHorizontalGroup(
            EditDialogLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(EditDialogLayout.createSequentialGroup()
                .addComponent(roundedGradientPanel3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        EditDialogLayout.setVerticalGroup(
            EditDialogLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(roundedGradientPanel3, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        UpdateStudent.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        UpdateStudent.setResizable(false);

        roundedPanel3.setBackground(new java.awt.Color(58, 138, 125));

        jScrollPane3.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane3.setOpaque(false);
        jScrollPane3.setVerticalScrollBar(new ScrollBarCustom());

        jTable3.setModel(new DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã tài khoản", "Họ tên", "Giới tính", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable3.setRowHeight(40);
        jTable3.setSelectionBackground(new java.awt.Color(255, 255, 255));
        jTable3.getTableHeader().setResizingAllowed(false);
        jTable3.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(jTable3);

        button11.setBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        button11.setForeground(new java.awt.Color(51, 51, 51));
        button11.setIcon(new ImageIcon(getClass().getResource("/Image/icons8-excel-30.png"))); // NOI18N
        button11.setText("Thêm từ Excel");
        button11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        button11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    button11ActionPerformed(evt);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        button12.setBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        button12.setForeground(new java.awt.Color(51, 51, 51));
        button12.setIcon(new ImageIcon(getClass().getResource("/Image/icons8-add-30 (1).png"))); // NOI18N
        button12.setText("Thêm sinh viên");
        button12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        button12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    button12ActionPerformed(evt);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        myTextField12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                myTextField12ActionPerformed(evt);
            }
        });

        jLabel24.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setText("Mã sinh viên:");

        button13.setBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        button13.setText("Xác nhận");
        button13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        button13.setPreferredSize(new java.awt.Dimension(42, 36));
        button13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button13ActionPerformed(evt);
            }
        });

        GroupLayout roundedPanel3Layout = new GroupLayout(roundedPanel3);
        roundedPanel3.setLayout(roundedPanel3Layout);
        roundedPanel3Layout.setHorizontalGroup(
            roundedPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(roundedPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(myTextField12, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(button12, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(button11, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(roundedPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel24, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 100, Short.MAX_VALUE))
                    .addComponent(button13, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, GroupLayout.PREFERRED_SIZE, 559, GroupLayout.PREFERRED_SIZE))
        );
        roundedPanel3Layout.setVerticalGroup(
            roundedPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 491, Short.MAX_VALUE)
            .addGroup(roundedPanel3Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel24)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(myTextField12, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(button12, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(button11, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(button13, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );

        GroupLayout UpdateStudentLayout = new GroupLayout(UpdateStudent.getContentPane());
        UpdateStudent.getContentPane().setLayout(UpdateStudentLayout);
        UpdateStudentLayout.setHorizontalGroup(
            UpdateStudentLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(roundedPanel3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        );
        UpdateStudentLayout.setVerticalGroup(
            UpdateStudentLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(roundedPanel3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setOpaque(false);

        roundedPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setVerticalScrollBar(new ScrollBarCustom());

        jTable1.setModel(new DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Mã lớp học", "Tên lớp học", "Năm học", "Sỉ số", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setGridColor(new java.awt.Color(61, 141, 122));
        jTable1.setRowHeight(40);
        jTable1.getTableHeader().setResizingAllowed(false);
        jTable1.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(jTable1);

        circleBackgroundPanel1.setColor1(new java.awt.Color(61, 141, 122));

        button2.setBorder(null);
        button2.setIcon(new ImageIcon(getClass().getResource("/Image/icons8-reload-30.png"))); // NOI18N
        button2.setText("Làm mới");
        button2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        button2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    button2ActionPerformed(evt);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        button1.setBackground(new java.awt.Color(0, 0, 0));
        button1.setForeground(new java.awt.Color(255, 255, 255));
        button1.setIcon(new ImageIcon(getClass().getResource("/Image/icons8-add-30.png"))); // NOI18N
        button1.setText("Thêm lớp học");
        button1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        button1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    button1ActionPerformed(evt);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        GroupLayout circleBackgroundPanel1Layout = new GroupLayout(circleBackgroundPanel1);
        circleBackgroundPanel1.setLayout(circleBackgroundPanel1Layout);
        circleBackgroundPanel1Layout.setHorizontalGroup(
            circleBackgroundPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, circleBackgroundPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(searchTextField1, GroupLayout.PREFERRED_SIZE, 432, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 278, Short.MAX_VALUE)
                .addComponent(button1, GroupLayout.PREFERRED_SIZE, 137, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(button2, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );
        circleBackgroundPanel1Layout.setVerticalGroup(
            circleBackgroundPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(circleBackgroundPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(circleBackgroundPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addGroup(circleBackgroundPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(button1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(searchTextField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addComponent(button2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.add(pagination1);

        GroupLayout roundedPanel1Layout = new GroupLayout(roundedPanel1);
        roundedPanel1.setLayout(roundedPanel1Layout);
        roundedPanel1Layout.setHorizontalGroup(
            roundedPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(roundedPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(circleBackgroundPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        roundedPanel1Layout.setVerticalGroup(
            roundedPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, roundedPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(circleBackgroundPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8))
        );

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(roundedPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(roundedPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void button2ActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {//GEN-FIRST:event_button2ActionPerformed
         list= lh_dao.getDanhSachLopHoc();
        initPag(1);
    }//GEN-LAST:event_button2ActionPerformed

    private void button1ActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {//GEN-FIRST:event_button1ActionPerformed
        ArrayList<MonHoc> listMH= mh_dao.getDanhSachMonHoc();
        listMH.forEach(x->{
            comboBoxSuggestion1.addItem(x.getTenMonHoc());
        });
        for(int i=0;i<10;i++){
            LocalDateTime now= LocalDateTime.now().plusYears(i);
            LocalDateTime nextYear= now.plusYears(1);
            String date=now.getYear()+"-"+nextYear.getYear();
            comboBoxSuggestion2.addItem(date);
            
        }
        ArrayList<TaiKhoan> tkGV=tk_dao.getDanhSachTaiKhoanGV();
        tkGV.forEach(x->{
            comboBoxSuggestion3.addItem(x.getHo()+" "+x.getTen());
        });
//        AddDialog.setAlwaysOnTop(true);
        AddDialog.pack();
        AddDialog.setIconImage(icon.getImage());
        AddDialog.setLocationRelativeTo(null);
        AddDialog.setModal(true);
//        SwingUtilities.invokeLater(()->AddDialog.setAlwaysOnTop(true));
         AddDialog.setVisible(true);
        
    }//GEN-LAST:event_button1ActionPerformed

    private void button4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button4ActionPerformed
        ViewDialog.setVisible(false);
    }//GEN-LAST:event_button4ActionPerformed

    private void button5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button5ActionPerformed
        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
        
        model.setRowCount(0);
        myTextField9.requestFocus();
        TableActionEvent event2= new TableActionEvent() {
            @Override
            public void onEdit(int row) {
             
            }

            @Override
            public void onDelete(int row) {
                model.removeRow(row);
                jTable2.getCellEditor().cancelCellEditing();
            }

            @Override
            public void onView(int row) {
            }
        };
        
        
        
        
        AddStudents.setIconImage(icon.getImage());
        AddStudents.pack();
        AddStudents.setLocationRelativeTo(null);
        
        TableActionCellRender render=new TableActionCellRender("delete");
        jTable2.getColumnModel().getColumn(3).setCellRenderer(render);
        
        jTable2.getColumnModel().getColumn(3).setCellEditor(new TableActionCellEditor(event2, "delete"));
        updateTableAddStudent(listAddStudent);
        updateSisoThemLopHoc();
//        SwingUtilities.invokeLater(() -> AddStudents.setAlwaysOnTop(true));
        AddStudents.setModal(true);
        AddStudents.setVisible(true);
    }//GEN-LAST:event_button5ActionPerformed
    
    private void button6ActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {//GEN-FIRST:event_button6ActionPerformed
       JnaFileChooser fileChooser = new JnaFileChooser();
         // Đảm bảo hiện trên cùng nếu cần

        if (fileChooser.showOpenDialog(null)) {
        File selectedFile = fileChooser.getSelectedFile();
        String filePath = selectedFile.getAbsolutePath();

        ArrayList<TaiKhoan> listTemp = tk_dao.getDanhSachTaiKhoanFromExcel(filePath);
        if (!listTemp.isEmpty()) {
            DefaultTableModel model = (DefaultTableModel) jTable2.getModel();

            for (TaiKhoan tk : listTemp) {
                if(!isExitTable(jTable2, tk.getMaTaiKhoan())){
                    model.addRow(new Object[]{
                    tk.getMaTaiKhoan(),
                    tk.getHo() + " " + tk.getTen(),
                    tk.getGioiTinh()
                });
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Không có dữ liệu tài khoản trong file.");
        }
    }
    }//GEN-LAST:event_button6ActionPerformed

    private void button7ActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {//GEN-FIRST:event_button7ActionPerformed
        String value= myTextField9.getText();
        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
        if(value.trim().isEmpty()||!value.trim().equalsIgnoreCase("")){
            TaiKhoan sv= tk_dao.getTaiKhoanByName(value);
            if(sv!=null&&!isExitTable(jTable2, sv.getMaTaiKhoan())){
                model.addRow(new Object[]{
                    sv.getMaTaiKhoan(),
                    sv.getHo()+" "+sv.getTen(),
                    sv.getGioiTinh()
                });
            }
            else{
                JOptionPane.showMessageDialog(null, "Không tìm thấy sinh viên");
            }
        }
    }//GEN-LAST:event_button7ActionPerformed

    private void button8ActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {//GEN-FIRST:event_button8ActionPerformed
        listAddStudent=new ArrayList<>();
        for(int i=0;i<jTable2.getRowCount();i++){
           listAddStudent.add(tk_dao.getTaiKhoan(jTable2.getValueAt(i, 0).toString()));
       }
       updateSisoThemLopHoc();
       AddStudents.dispose();
    }//GEN-LAST:event_button8ActionPerformed
    public void clearAddDialog(){
            myTextField1.setText("");
            comboBoxSuggestion2.setSelectedIndex(0);
            myTextField8.setText("0");
            comboBoxSuggestion1.setSelectedIndex(0);
            comboBoxSuggestion3.setSelectedIndex(0);
    }
    public boolean validateAdd(){
        if(myTextField1.getText().trim().equalsIgnoreCase("")){
            JOptionPane.showMessageDialog(null, "Tên lớp không được rỗng");
            return false;
        }
            
        return true;
    }
    private void button3ActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {//GEN-FIRST:event_button3ActionPerformed
        String ma= lh_dao.generateMa();
        String tenLopHoc= myTextField1.getText();
        String namHoc= comboBoxSuggestion2.getSelectedItem().toString();
        int siSo= Integer.parseInt(myTextField8.getText().toString());
        ArrayList<MonHoc> listMH= mh_dao.getDanhSachMonHoc();
        MonHoc monHoc= listMH.get(comboBoxSuggestion1.getSelectedIndex());
        ArrayList<TaiKhoan> tkGV=tk_dao.getDanhSachTaiKhoanGV();
        TaiKhoan gv= tkGV.get(comboBoxSuggestion3.getSelectedIndex());
        
        LopHoc newLop= new LopHoc(ma, tenLopHoc, siSo, namHoc, "enable", monHoc, gv);
        if(validateAdd()){
            
            if(lh_dao.addLopHoc(newLop)){
                listAddStudent.forEach(x->{
                    KetQuaHocTap kq= new KetQuaHocTap(newLop, x);
                    try {
                        if(!kqht_dao.themKetQuaHocTap(kq)) System.out.println("Them khong thanh cong");
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                });
                listAddStudent.clear();
                initTable();
                AddDialog.dispose();
                clearAddDialog();
                jTable1.getCellEditor().cancelCellEditing();
            }else{
                JOptionPane.showMessageDialog(null, "Thêm lớp học thất bại");
            }
        }
         
    }//GEN-LAST:event_button3ActionPerformed
//    public boolean KQHTContain(){
//        
//    }
    private void button9ActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {//GEN-FIRST:event_button9ActionPerformed
        ArrayList<MonHoc> listMH= mh_dao.getDanhSachMonHoc();
        ArrayList<TaiKhoan> tkGV=tk_dao.getDanhSachTaiKhoanGV();
        LopHoc lophoc= new LopHoc(jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString(), 
                myTextField10.getText(), Integer.parseInt(myTextField11.getText()), comboBoxSuggestion5.getSelectedItem().toString(), "enable", 
                listMH.get(comboBoxSuggestion4.getSelectedIndex()), 
                tkGV.get(comboBoxSuggestion6.getSelectedIndex()));
        if(lh_dao.updateLopHoc(lophoc)){
            ArrayList<KetQuaHocTap> dsKetQuaBefore= kqht_dao.getDanhSachKetQuaHocTap(jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString());
            //kiem tra xem ket qua hoc tap co trong db chua, neu chua se them vao
            dsKQHT.forEach(x->{
                System.out.println("Sau update");
                try {
                    System.out.println(kqht_dao.getKetQuaHocTap(x.getTaiKhoan().getMaTaiKhoan(), x.getLopHoc().getMaLop()));
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                try {
                    if(kqht_dao.getKetQuaHocTap(x.getTaiKhoan().getMaTaiKhoan(), x.getLopHoc().getMaLop())==null){
                        kqht_dao.themKetQuaHocTap(x);
                    }
                    else{
                    System.out.println("khong duoc");
                }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            });
            
            //kiem tra xem ket qua trong ds ban dau co trong ds sau khi update khong, neu khong thi xoa
            dsKetQuaBefore.forEach(x->{
                if(!dsKQHT.contains(x)){
                    try {
                        kqht_dao.xoaKetQuaHocTap(x);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }

                }
            });
        }
        else{
            System.out.println("khong duoc");
        }
        initTable();
        EditDialog.dispose();
        jTable1.getCellEditor().cancelCellEditing();
    }//GEN-LAST:event_button9ActionPerformed

    private void button10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button10ActionPerformed
        DefaultTableModel model= (DefaultTableModel) jTable3.getModel();
        model.setRowCount(0);
        
        dsKQHT.forEach(x->{
            TaiKhoan sv= null;
            try {
                sv = tk_dao.getTaiKhoan(x.getTaiKhoan().getMaTaiKhoan());
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            model.addRow(new Object[]{
                sv.getMaTaiKhoan(),
                sv.getHo()+" "+sv.getTen(),
                sv.getGioiTinh()
            });
        });
        
        TableActionEvent event3= new TableActionEvent() {
            @Override
            public void onEdit(int row) {
                
            }

            @Override
            public void onDelete(int row) {
                
                model.removeRow(row);
                jTable3.getCellEditor().cancelCellEditing();
            }

            @Override
            public void onView(int row) {
                
            }
        };
        
        jTable3.getColumnModel().getColumn(3).setCellRenderer(new TableActionCellRender("delete"));
        jTable3.getColumnModel().getColumn(3).setCellEditor(new TableActionCellEditor(event3, "delete"));
        
        
        UpdateStudent.pack();
        UpdateStudent.setLocationRelativeTo(null);
        UpdateStudent.setIconImage(icon.getImage());
        UpdateStudent.setModal(true);
        UpdateStudent.setVisible(true);
    }//GEN-LAST:event_button10ActionPerformed

    private void button11ActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {//GEN-FIRST:event_button11ActionPerformed
       JnaFileChooser fileChooser = new JnaFileChooser();
         // Đảm bảo hiện trên cùng nếu cần

        if (fileChooser.showOpenDialog(null)) {
        File selectedFile = fileChooser.getSelectedFile();
        String filePath = selectedFile.getAbsolutePath();

        ArrayList<TaiKhoan> listTemp = tk_dao.getDanhSachTaiKhoanFromExcel(filePath);
            if (!listTemp.isEmpty()) {
                DefaultTableModel model = (DefaultTableModel) jTable3.getModel();

                for (TaiKhoan tk : listTemp) {
                    if(!isExitTable(jTable3, tk.getMaTaiKhoan())){
                        model.addRow(new Object[]{
                        tk.getMaTaiKhoan(),
                        tk.getHo() + " " + tk.getTen(),
                        tk.getGioiTinh()
                    });
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Không có dữ liệu tài khoản trong file.");
            }
        } 
    }//GEN-LAST:event_button11ActionPerformed

    private void button12ActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {//GEN-FIRST:event_button12ActionPerformed
        String ma= myTextField12.getText().trim();
        DefaultTableModel model= (DefaultTableModel) jTable3.getModel();
        TaiKhoan sv= tk_dao.getTaiKhoanByName(ma);
        if(sv!=null&&!isExitTable(jTable3, ma)){
            model.addRow(new Object[]{
                sv.getMaTaiKhoan(),
                sv.getHo()+" "+sv.getTen(),
                sv.getGioiTinh()
            });
        }
    }//GEN-LAST:event_button12ActionPerformed

    private void button13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button13ActionPerformed
            myTextField11.setText(jTable3.getRowCount()+"");
            myTextField12.setText("");
            
            dsKQHT=new ArrayList<>();
            for(int i=0;i<jTable3.getRowCount();i++){
               dsKQHT.add(new KetQuaHocTap(new LopHoc(jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString())
                       , new TaiKhoan(jTable3.getValueAt(i, 0).toString())));
            }
            
            UpdateStudent.dispose();
    }//GEN-LAST:event_button13ActionPerformed

    private void myTextField12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_myTextField12ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_myTextField12ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JDialog AddDialog;
    private JDialog AddStudents;
    private JDialog EditDialog;
    private JDialog UpdateStudent;
    private JDialog ViewDialog;
    private Button button1;
    private Button button10;
    private Button button11;
    private Button button12;
    private Button button13;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    private Button button6;
    private Button button7;
    private Button button8;
    private Button button9;
    private CircleBackgroundPanel circleBackgroundPanel1;
    private CircleBackgroundPanel circleBackgroundPanel2;
    private CircleBackgroundPanel circleBackgroundPanel3;
    private ComboBoxSuggestion comboBoxSuggestion1;
    private ComboBoxSuggestion comboBoxSuggestion2;
    private ComboBoxSuggestion comboBoxSuggestion3;
    private ComboBoxSuggestion comboBoxSuggestion4;
    private ComboBoxSuggestion comboBoxSuggestion5;
    private ComboBoxSuggestion comboBoxSuggestion6;
    private JLabel jLabel1;
    private JLabel jLabel10;
    private JLabel jLabel11;
    private JLabel jLabel12;
    private JLabel jLabel13;
    private JLabel jLabel14;
    private JLabel jLabel15;
    private JLabel jLabel16;
    private JLabel jLabel17;
    private JLabel jLabel18;
    private JLabel jLabel19;
    private JLabel jLabel2;
    private JLabel jLabel20;
    private JLabel jLabel21;
    private JLabel jLabel22;
    private JLabel jLabel23;
    private JLabel jLabel24;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JLabel jLabel7;
    private JLabel jLabel8;
    private JLabel jLabel9;
    private JPanel jPanel1;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;
    private JScrollPane jScrollPane3;
    private JTable jTable1;
    private JTable jTable2;
    private JTable jTable3;
    private MyTextField myTextField1;
    private MyTextField myTextField10;
    private MyTextField myTextField11;
    private MyTextField myTextField12;
    private MyTextField myTextField2;
    private MyTextField myTextField3;
    private MyTextField myTextField4;
    private MyTextField myTextField5;
    private MyTextField myTextField6;
    private MyTextField myTextField7;
    private MyTextField myTextField8;
    private MyTextField myTextField9;
    private Pagination pagination1;
    private RoundedGradientPanel roundedGradientPanel1;
    private RoundedGradientPanel roundedGradientPanel2;
    private RoundedGradientPanel roundedGradientPanel3;
    private RoundedPanel roundedPanel1;
    private RoundedPanel roundedPanel2;
    private RoundedPanel roundedPanel3;
    private SearchTextField searchTextField1;
    // End of variables declaration//GEN-END:variables
}
