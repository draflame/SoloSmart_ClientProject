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
import Entity.TaiKhoan;
import jnafilechooser.api.JnaFileChooser;
import service.RmiServiceLocator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 *
 * @author THANH PHU
 */
public class Admin_Account extends JPanel {

    /**
     * Creates new form Admin_Subject
     */
    private IMonHoc_DAO mh_dao= RmiServiceLocator.getMonHocDao();
    private ILopHoc_DAO lh_dao= RmiServiceLocator.getLopHocDao();
    private ITaiKhoan_DAO tk_dao= RmiServiceLocator.getTaiKhoanDao();
    private IKetQuaHocTap_DAO kqht_dao= RmiServiceLocator.getKetQuaHocTapDao();
    private ITaiKhoan_DAO ITaiKhoan_DAO = RmiServiceLocator.getTaiKhoanDao();
    private ImageIcon icon = new ImageIcon(getClass().getResource("/Image/favicon_1.png"));
    private ArrayList<TaiKhoan> listAddStudent= new ArrayList<>();
    private ArrayList<TaiKhoan> listUpdateStudent= new ArrayList<>();
    private ArrayList<KetQuaHocTap> dsKQHT= new ArrayList<>();
    private ArrayList<TaiKhoan> list= new ArrayList<>();
    public Admin_Account() throws RemoteException {
        initComponents();
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        
        TableActionEvent event = new TableActionEvent() {
            @Override
            public void onEdit(int row) throws RemoteException {
               TaiKhoan tk= tk_dao.getTaiKhoan(jTable1.getValueAt(row, 0).toString());
               myTextField1.setText(tk.getHo());
               myTextField13.setText(tk.getTen());
               myTextField19.setText(tk.getTenTaiKhoan());
               myPasswordField2.setText(tk.getMatKhau());
               myTextField15.setText(tk.getEmail());
               myTextField16.setText(tk.getSoDienThoai());
               
               comboBoxSuggestion7.setSelectedItem(tk.getGioiTinh());
               if(tk.getVaiTro().equalsIgnoreCase("SV")){
                   comboBoxSuggestion8.setSelectedIndex(0);
               } else if(tk.getVaiTro().equalsIgnoreCase("GV")){
                   comboBoxSuggestion8.setSelectedIndex(1);
               }else comboBoxSuggestion8.setSelectedIndex(2);
               
               EditDialog.pack();
               EditDialog.setLocationRelativeTo(null);
               EditDialog.setIconImage(icon.getImage());
               EditDialog.setVisible(true);
                
            }

            @Override
            public void onDelete(int row) throws RemoteException {
                int choice= JOptionPane.showConfirmDialog(null, "Bạn có muốn xóa tài khoản này không?", "Xác nhận xóa",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                if(choice==JOptionPane.YES_OPTION){
                    if(tk_dao.deleteTaiKhoan(jTable1.getValueAt(row, 0).toString())){
                        model.removeRow(row);
                    }
                    jTable1.getCellEditor().cancelCellEditing();
                }
                
                
            }

            @Override
            public void onView(int row) throws RemoteException {
                TaiKhoan tk= tk_dao.getTaiKhoan(jTable1.getValueAt(row, 0).toString());
                
                myTextField2.setText(tk.getMaTaiKhoan());
                myTextField3.setText(tk.getHo()+" "+tk.getTen());
                myTextField4.setText(tk.getTenTaiKhoan());
                myPasswordField1.setText(tk.getMatKhau());
                myTextField6.setText(tk.getGioiTinh());
                myTextField7.setText(tk.getVaiTro().equalsIgnoreCase("SV")?"Sinh viên":tk.getVaiTro().equalsIgnoreCase("GV")?"Giảng viên":"Quản trị viên");
                myTextField8.setText(tk.getEmail());
                myTextField10.setText(tk.getSoDienThoai());
                
                ViewDialog.pack();
                ViewDialog.setLocationRelativeTo(null);
                ViewDialog.setIconImage(icon.getImage());
                ViewDialog.setVisible(true);
                
                
                jTable1.getCellEditor().cancelCellEditing();
            }
        };
        jTable1.getColumnModel().getColumn(5).setCellRenderer(new TableActionCellRender());
        jTable1.getColumnModel().getColumn(5).setCellEditor(new TableActionCellEditor(event));
        initTable();
        initPag(1);
        //search
        searchTextField1.addActionListener(x->{
            try {
                filterTaiKhoan();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
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
        list= tk_dao.getDanhSachTaiKhoan();
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
    public void updateTable(ArrayList<TaiKhoan> list){
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);
        list.forEach(x->{
            model.addRow(new Object[]{
                x.getMaTaiKhoan(),
                x.getHo()+" "+x.getTen(),
                x.getGioiTinh(),
                x.getVaiTro().equalsIgnoreCase("SV")?"Sinh viên":x.getVaiTro().equalsIgnoreCase("AD")?"Quản trị viên":"Giảng viên",
                x.getDangOnline()
            });
        });
                
    }
    
    public void filterTaiKhoan() throws RemoteException {
        int slgioiTinh = comboBoxSuggestion1.getSelectedIndex();
            int slvaiTro = comboBoxSuggestion2.getSelectedIndex();
            String key = searchTextField1.getText();
            String vaiTro = "";
            String gioiTinh = "";
            
            if(slgioiTinh==0) {
                gioiTinh = "";
            }else if(slgioiTinh==1){
                gioiTinh = "Nam";
            }else if(slgioiTinh==2) {
                gioiTinh = "Nữ";
            }
            
            if(slvaiTro==0) {
                vaiTro = "";
            }else if(slvaiTro==1) {
                vaiTro = "SV";
            }else if(slvaiTro==2) {
                vaiTro = "GV";
            }else if(slvaiTro == 3){
                vaiTro = "AD";
            }
            
            list= tk_dao.filterTaiKhoan(gioiTinh,vaiTro,key);
            if(list.size()>0){
                initPag(1);
            }
            else{
                JOptionPane.showMessageDialog(null,"khong tim thay");
            }
    }
    
    public boolean validateAccount() throws RemoteException {
        String ho= myTextField5.getText();
        String ten= myTextField14.getText();
        String email = myTextField17.getText().trim();
        String sdt = myTextField18.getText().trim();    
        if (ho.isEmpty() || ten.isEmpty() ) {
            JOptionPane.showMessageDialog(null, "Họ, tên, và tên tài khoản không được để trống.");
            return false;
        }
        if(!(ho.matches("^[\\p{L} ]+$")||ten.matches("^[\\p{L} ]+$"))){
            JOptionPane.showMessageDialog(null, "Họ, tên phải là ký tự chữ, không chứa các ký tự đặc biệt.");
            return false;
        }
        
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            JOptionPane.showMessageDialog(null, "Email không hợp lệ.");
            return false;
        }
        if (!sdt.matches("^0\\d{9}$")) {
            JOptionPane.showMessageDialog(null, "Số điện thoại phải gồm 10 chữ số và bắt đầu bằng số 0.");
            return false;
        }
        if(tk_dao.getTaiKhoanByMailPhone(sdt)!=null){
            JOptionPane.showMessageDialog(null, "Số điện thoại đã tồn tại.");
            return false;
        }
        if(tk_dao.getTaiKhoanByMailPhone(email)!=null){
            JOptionPane.showMessageDialog(null, "Email đã tồn tại.");
            return false;
        }
        return true;
    }
    public boolean validateUpdate(){
         String ho= myTextField1.getText();
        String ten= myTextField13.getText();
        String email = myTextField15.getText().trim();
        String sdt = myTextField16.getText().trim();  
        String pass = new String(myPasswordField2.getPassword());
        
        if (ho.isEmpty() || ten.isEmpty() || email.isEmpty() || sdt.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Không được để trống thông tin!");
            return false;
        }

        // Kiểm tra email hợp lệ
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            JOptionPane.showMessageDialog(null, "Email không hợp lệ!");
            return false;
        }

        // Kiểm tra SĐT chỉ chứa số và có độ dài từ 9 đến 11
        if (!sdt.matches("\\d{9,11}")) {
            JOptionPane.showMessageDialog(null, "Số điện thoại không hợp lệ!");
            return false;
        }

        // Kiểm tra password độ dài tối thiểu
        if (pass.length() < 6) {
            JOptionPane.showMessageDialog(null, "Mật khẩu phải từ 6 ký tự trở lên!");
            return false;
        }
        
        return true;
    }
    public void ClearAddDialog(){
        myTextField1.setText("");
        myTextField13.setText("");
        myTextField15.setText("");
        myTextField16.setText("");
        comboBoxSuggestion7.setSelectedIndex(0);
        comboBoxSuggestion8.setSelectedIndex(0);
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        EditDialog = new JDialog();
        roundedGradientPanel1 = new RoundedGradientPanel();
        jLabel1 = new JLabel();
        myTextField1 = new MyTextField();
        jLabel3 = new JLabel();
        button3 = new Button();
        circleBackgroundPanel2 = new CircleBackgroundPanel();
        jLabel17 = new JLabel();
        myTextField13 = new MyTextField();
        jLabel25 = new JLabel();
        comboBoxSuggestion7 = new ComboBoxSuggestion();
        jLabel27 = new JLabel();
        comboBoxSuggestion8 = new ComboBoxSuggestion();
        jLabel28 = new JLabel();
        myTextField15 = new MyTextField();
        jLabel29 = new JLabel();
        jLabel30 = new JLabel();
        myTextField16 = new MyTextField();
        myTextField19 = new MyTextField();
        jLabel35 = new JLabel();
        jLabel36 = new JLabel();
        myPasswordField2 = new MyPasswordField();
        jLabel22 = new JLabel();
        AddSuccess = new JDialog();
        roundedGradientPanel2 = new RoundedGradientPanel();
        jLabel4 = new JLabel();
        jLabel5 = new JLabel();
        jLabel6 = new JLabel();
        jLabel2 = new JLabel();
        button4 = new Button();
        AddExcel = new JDialog();
        roundedPanel4 = new RoundedPanel();
        jScrollPane3 = new JScrollPane();
        jTable3 = new JTable();
        ViewDialog = new JDialog();
        roundedPanel3 = new RoundedPanel();
        jLabel7 = new JLabel();
        jLabel8 = new JLabel();
        myTextField2 = new MyTextField();
        jLabel18 = new JLabel();
        myTextField3 = new MyTextField();
        jLabel9 = new JLabel();
        myTextField4 = new MyTextField();
        jLabel10 = new JLabel();
        jLabel11 = new JLabel();
        jLabel12 = new JLabel();
        myTextField6 = new MyTextField();
        jLabel13 = new JLabel();
        myTextField7 = new MyTextField();
        jLabel14 = new JLabel();
        myTextField8 = new MyTextField();
        jLabel15 = new JLabel();
        myTextField10 = new MyTextField();
        jLabel19 = new JLabel();
        button5 = new Button();
        myPasswordField1 = new MyPasswordField();
        AddDialog = new JDialog();
        roundedGradientPanel3 = new RoundedGradientPanel();
        jLabel16 = new JLabel();
        myTextField5 = new MyTextField();
        jLabel20 = new JLabel();
        button6 = new Button();
        circleBackgroundPanel3 = new CircleBackgroundPanel();
        jLabel21 = new JLabel();
        myTextField14 = new MyTextField();
        jLabel26 = new JLabel();
        comboBoxSuggestion9 = new ComboBoxSuggestion();
        jLabel31 = new JLabel();
        comboBoxSuggestion10 = new ComboBoxSuggestion();
        jLabel32 = new JLabel();
        myTextField17 = new MyTextField();
        jLabel33 = new JLabel();
        jLabel34 = new JLabel();
        myTextField18 = new MyTextField();
        roundedPanel1 = new RoundedPanel();
        jScrollPane1 = new JScrollPane();
        jTable1 = new JTable();
        circleBackgroundPanel1 = new CircleBackgroundPanel();
        button2 = new Button();
        button1 = new Button();
        searchTextField1 = new SearchTextField();
        button14 = new Button();
        comboBoxSuggestion1 = new ComboBoxSuggestion();
        comboBoxSuggestion2 = new ComboBoxSuggestion();
        jPanel1 = new JPanel();
        pagination1 = new Pagination();

        EditDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        EditDialog.setTitle("Cập nhật tài khoản");
        EditDialog.setResizable(false);

        roundedGradientPanel1.setColor1(new java.awt.Color(255, 255, 255));
        roundedGradientPanel1.setColor2(new java.awt.Color(255, 255, 255));

        jLabel1.setIcon(new ImageIcon(getClass().getResource("/Image/Profiling-bro.png"))); // NOI18N

        myTextField1.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)), BorderFactory.createEmptyBorder(1, 2, 1, 12)));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel3.setText("Họ:");

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

        circleBackgroundPanel2.setColor1(new java.awt.Color(58, 138, 125));

        jLabel17.setFont(new java.awt.Font("Segoe UI", 3, 24)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel17.setText("Cập nhật tài khoản");

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

        myTextField13.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)), BorderFactory.createEmptyBorder(1, 2, 1, 12)));

        jLabel25.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel25.setText("Tên:");

        comboBoxSuggestion7.setModel(new DefaultComboBoxModel(new String[] { "Nam", "Nữ" }));
        comboBoxSuggestion7.setPreferredSize(new java.awt.Dimension(68, 36));

        jLabel27.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel27.setText("Giới tính:");

        comboBoxSuggestion8.setModel(new DefaultComboBoxModel(new String[] { "Sinh Viên", "Giảng Viên", "Quản trị viên" }));
        comboBoxSuggestion8.setPreferredSize(new java.awt.Dimension(109, 36));

        jLabel28.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel28.setText("Vai trò:");

        myTextField15.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)), BorderFactory.createEmptyBorder(1, 2, 1, 12)));

        jLabel29.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel29.setText("Mail:");

        jLabel30.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel30.setText("Số điện thoại:");

        myTextField16.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)), BorderFactory.createEmptyBorder(1, 2, 1, 12)));

        myTextField19.setEditable(false);
        myTextField19.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)), BorderFactory.createEmptyBorder(1, 2, 1, 12)));

        jLabel35.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel35.setText("Tên tài khoản:");

        jLabel36.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel36.setText("Mật khẩu:");

        myPasswordField2.setBorder(BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        myPasswordField2.setText("myPasswordField2");
        myPasswordField2.setPreferredSize(new java.awt.Dimension(99, 36));

        jLabel22.setIcon(new ImageIcon(getClass().getResource("/Image/eye_closed.png"))); // NOI18N
        jLabel22.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel22MouseClicked(evt);
            }
        });

        GroupLayout roundedGradientPanel1Layout = new GroupLayout(roundedGradientPanel1);
        roundedGradientPanel1.setLayout(roundedGradientPanel1Layout);
        roundedGradientPanel1Layout.setHorizontalGroup(
            roundedGradientPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(roundedGradientPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 507, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(roundedGradientPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(circleBackgroundPanel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(button3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(roundedGradientPanel1Layout.createSequentialGroup()
                        .addGroup(roundedGradientPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(comboBoxSuggestion7, GroupLayout.PREFERRED_SIZE, 157, GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel27, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(roundedGradientPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(roundedGradientPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel28, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(comboBoxSuggestion8, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(myTextField19, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(GroupLayout.Alignment.TRAILING, roundedGradientPanel1Layout.createSequentialGroup()
                        .addGroup(roundedGradientPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
                            .addComponent(myTextField1, GroupLayout.PREFERRED_SIZE, 279, GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(roundedGradientPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(myTextField13, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(roundedGradientPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel25, GroupLayout.PREFERRED_SIZE, 151, GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 54, Short.MAX_VALUE))))
                    .addGroup(GroupLayout.Alignment.TRAILING, roundedGradientPanel1Layout.createSequentialGroup()
                        .addGroup(roundedGradientPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel29, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel35, GroupLayout.PREFERRED_SIZE, 151, GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel36, GroupLayout.PREFERRED_SIZE, 151, GroupLayout.PREFERRED_SIZE)
                            .addComponent(myTextField15, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(roundedGradientPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel30, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
                            .addComponent(myTextField16, GroupLayout.PREFERRED_SIZE, 235, GroupLayout.PREFERRED_SIZE)))
                    .addGroup(roundedGradientPanel1Layout.createSequentialGroup()
                        .addComponent(myPasswordField2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel22)))
                .addContainerGap())
        );
        roundedGradientPanel1Layout.setVerticalGroup(
            roundedGradientPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(roundedGradientPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(roundedGradientPanel1Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addGroup(roundedGradientPanel1Layout.createSequentialGroup()
                        .addComponent(circleBackgroundPanel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addGap(7, 7, 7)
                        .addComponent(myTextField1, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE))
                    .addGroup(roundedGradientPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel25)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(myTextField13, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel35)
                .addGap(7, 7, 7)
                .addComponent(myTextField19, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jLabel36)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(roundedGradientPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel22, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
                    .addComponent(myPasswordField2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(roundedGradientPanel1Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addGroup(roundedGradientPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel27)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboBoxSuggestion7, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGroup(roundedGradientPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel28)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboBoxSuggestion8, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(roundedGradientPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(jLabel30))
                .addGap(7, 7, 7)
                .addGroup(roundedGradientPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(myTextField15, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
                    .addComponent(myTextField16, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(button3, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
            .addComponent(jLabel1, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        GroupLayout EditDialogLayout = new GroupLayout(EditDialog.getContentPane());
        EditDialog.getContentPane().setLayout(EditDialogLayout);
        EditDialogLayout.setHorizontalGroup(
            EditDialogLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(EditDialogLayout.createSequentialGroup()
                .addComponent(roundedGradientPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        EditDialogLayout.setVerticalGroup(
            EditDialogLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(roundedGradientPanel1, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        );

        AddSuccess.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        AddSuccess.setIconImage(null);
        AddSuccess.setModal(true);
        AddSuccess.setUndecorated(true);
        AddSuccess.setResizable(false);

        roundedGradientPanel2.setColor1(new java.awt.Color(58, 138, 125));
        roundedGradientPanel2.setColor2(new java.awt.Color(194, 230, 118));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel4.setText("Thêm thành công!");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Tên tài khoản: ");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Mật khẩu:");

        jLabel2.setIcon(new ImageIcon(getClass().getResource("/Image/Mobile login-cuate.png"))); // NOI18N

        button4.setBackground(new java.awt.Color(0, 0, 0));
        button4.setForeground(new java.awt.Color(255, 255, 255));
        button4.setText("Xác nhận");
        button4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        button4.setPreferredSize(new java.awt.Dimension(63, 36));
        button4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button4ActionPerformed(evt);
            }
        });

        GroupLayout roundedGradientPanel2Layout = new GroupLayout(roundedGradientPanel2);
        roundedGradientPanel2.setLayout(roundedGradientPanel2Layout);
        roundedGradientPanel2Layout.setHorizontalGroup(
            roundedGradientPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(roundedGradientPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(roundedGradientPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(roundedGradientPanel2Layout.createSequentialGroup()
                        .addComponent(button4, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(roundedGradientPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(roundedGradientPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel5, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        roundedGradientPanel2Layout.setVerticalGroup(
            roundedGradientPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(roundedGradientPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(roundedGradientPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(roundedGradientPanel2Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel4, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5)
                        .addGap(37, 37, 37)
                        .addComponent(jLabel6))
                    .addComponent(jLabel2))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(button4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10))
        );

        GroupLayout AddSuccessLayout = new GroupLayout(AddSuccess.getContentPane());
        AddSuccess.getContentPane().setLayout(AddSuccessLayout);
        AddSuccessLayout.setHorizontalGroup(
            AddSuccessLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(AddSuccessLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(roundedGradientPanel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        AddSuccessLayout.setVerticalGroup(
            AddSuccessLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(AddSuccessLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(roundedGradientPanel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        AddExcel.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        AddExcel.setTitle("Thêm từ Excel");
        AddExcel.setResizable(false);

        roundedPanel4.setBackground(new java.awt.Color(255, 255, 255));
        roundedPanel4.setPreferredSize(new java.awt.Dimension(827, 460));

        jTable3.setModel(new DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(jTable3);

        GroupLayout roundedPanel4Layout = new GroupLayout(roundedPanel4);
        roundedPanel4.setLayout(roundedPanel4Layout);
        roundedPanel4Layout.setHorizontalGroup(
            roundedPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, roundedPanel4Layout.createSequentialGroup()
                .addGap(0, 229, Short.MAX_VALUE)
                .addComponent(jScrollPane3, GroupLayout.PREFERRED_SIZE, 598, GroupLayout.PREFERRED_SIZE))
        );
        roundedPanel4Layout.setVerticalGroup(
            roundedPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE)
        );

        GroupLayout AddExcelLayout = new GroupLayout(AddExcel.getContentPane());
        AddExcel.getContentPane().setLayout(AddExcelLayout);
        AddExcelLayout.setHorizontalGroup(
            AddExcelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(AddExcelLayout.createSequentialGroup()
                .addComponent(roundedPanel4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        AddExcelLayout.setVerticalGroup(
            AddExcelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(AddExcelLayout.createSequentialGroup()
                .addComponent(roundedPanel4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        ViewDialog.setTitle("Chi tiết tài khoản");
        ViewDialog.setModal(true);
        ViewDialog.setResizable(false);

        roundedPanel3.setBackground(new java.awt.Color(58, 138, 125));
        roundedPanel3.setPreferredSize(new java.awt.Dimension(827, 460));

        jLabel7.setIcon(new ImageIcon(getClass().getResource("/Image/Resume-bro.png"))); // NOI18N

        jLabel8.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Mã tài khoản:");

        myTextField2.setEditable(false);
        myTextField2.setFocusable(false);

        jLabel18.setFont(new java.awt.Font("Segoe UI", 3, 24)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel18.setText("Chi tiết tài khoản");

        myTextField3.setEditable(false);
        myTextField3.setFocusable(false);

        jLabel9.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Họ và tên:");

        myTextField4.setEditable(false);
        myTextField4.setFocusable(false);

        jLabel10.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Tên tài khoản:");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Mật khẩu:");

        jLabel12.setIcon(new ImageIcon(getClass().getResource("/Image/eye_closed.png"))); // NOI18N
        jLabel12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel12MouseClicked(evt);
            }
        });

        myTextField6.setEditable(false);
        myTextField6.setFocusable(false);

        jLabel13.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Giới tính:");

        myTextField7.setEditable(false);
        myTextField7.setFocusable(false);

        jLabel14.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Vai trò:");

        myTextField8.setEditable(false);
        myTextField8.setFocusable(false);

        jLabel15.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Email:");

        myTextField10.setEditable(false);
        myTextField10.setFocusable(false);

        jLabel19.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Số điện thoại:");

        button5.setBackground(new java.awt.Color(0, 0, 0));
        button5.setForeground(new java.awt.Color(255, 255, 255));
        button5.setText("Xác nhận");
        button5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        button5.setPreferredSize(new java.awt.Dimension(42, 36));
        button5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button5ActionPerformed(evt);
            }
        });

        myPasswordField1.setEditable(false);
        myPasswordField1.setFocusable(false);

        GroupLayout roundedPanel3Layout = new GroupLayout(roundedPanel3);
        roundedPanel3.setLayout(roundedPanel3Layout);
        roundedPanel3Layout.setHorizontalGroup(
            roundedPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(roundedPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(roundedPanel3Layout.createSequentialGroup()
                        .addGroup(roundedPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel8, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(myTextField2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(myTextField3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel9, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(myTextField4, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel10, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel11, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(roundedPanel3Layout.createSequentialGroup()
                                .addComponent(myPasswordField1, GroupLayout.PREFERRED_SIZE, 246, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel12))
                            .addGroup(roundedPanel3Layout.createSequentialGroup()
                                .addGroup(roundedPanel3Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                    .addComponent(myTextField6, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 103, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel13, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 103, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(roundedPanel3Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                    .addComponent(myTextField7, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 179, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel14, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 141, GroupLayout.PREFERRED_SIZE)))
                            .addComponent(myTextField8, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel15, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(myTextField10, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel19, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(button5, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 44, Short.MAX_VALUE)))
                .addContainerGap())
        );
        roundedPanel3Layout.setVerticalGroup(
            roundedPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18, GroupLayout.PREFERRED_SIZE, 49, GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22)
                .addComponent(jLabel8)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(myTextField2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(myTextField3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(myTextField4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(roundedPanel3Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel12, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
                    .addComponent(myPasswordField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(roundedPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(roundedPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(myTextField6, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGroup(GroupLayout.Alignment.TRAILING, roundedPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(myTextField7, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                .addGap(7, 7, 7)
                .addComponent(jLabel15)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(myTextField8, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addComponent(jLabel19)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(myTextField10, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(button5, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(68, Short.MAX_VALUE))
            .addComponent(jLabel7, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        GroupLayout ViewDialogLayout = new GroupLayout(ViewDialog.getContentPane());
        ViewDialog.getContentPane().setLayout(ViewDialogLayout);
        ViewDialogLayout.setHorizontalGroup(
            ViewDialogLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(roundedPanel3, GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)
        );
        ViewDialogLayout.setVerticalGroup(
            ViewDialogLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(roundedPanel3, GroupLayout.DEFAULT_SIZE, 646, Short.MAX_VALUE)
        );

        AddDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        AddDialog.setTitle("Thêm lớp học");
        AddDialog.setResizable(false);

        roundedGradientPanel3.setColor1(new java.awt.Color(255, 255, 255));
        roundedGradientPanel3.setColor2(new java.awt.Color(255, 255, 255));

        jLabel16.setIcon(new ImageIcon(getClass().getResource("/Image/Profiling-bro.png"))); // NOI18N

        myTextField5.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)), BorderFactory.createEmptyBorder(1, 2, 1, 12)));

        jLabel20.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel20.setText("Họ:");

        button6.setBackground(new java.awt.Color(58, 138, 125));
        button6.setForeground(new java.awt.Color(255, 255, 255));
        button6.setText("Xác nhận");
        button6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        button6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    button6ActionPerformed(evt);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        circleBackgroundPanel3.setColor1(new java.awt.Color(58, 138, 125));

        jLabel21.setFont(new java.awt.Font("Segoe UI", 3, 24)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel21.setText("Thêm tài khoản");

        GroupLayout circleBackgroundPanel3Layout = new GroupLayout(circleBackgroundPanel3);
        circleBackgroundPanel3.setLayout(circleBackgroundPanel3Layout);
        circleBackgroundPanel3Layout.setHorizontalGroup(
            circleBackgroundPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(circleBackgroundPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel21, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        circleBackgroundPanel3Layout.setVerticalGroup(
            circleBackgroundPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(circleBackgroundPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel21, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(21, 21, 21))
        );

        myTextField14.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)), BorderFactory.createEmptyBorder(1, 2, 1, 12)));

        jLabel26.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel26.setText("Tên:");

        comboBoxSuggestion9.setModel(new DefaultComboBoxModel(new String[] { "Nam", "Nữ" }));
        comboBoxSuggestion9.setPreferredSize(new java.awt.Dimension(68, 36));

        jLabel31.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel31.setText("Giới tính:");

        comboBoxSuggestion10.setModel(new DefaultComboBoxModel(new String[] { "Sinh Viên", "Giảng Viên", "Quản trị viên" }));
        comboBoxSuggestion10.setPreferredSize(new java.awt.Dimension(109, 36));

        jLabel32.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel32.setText("Vai trò:");

        myTextField17.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)), BorderFactory.createEmptyBorder(1, 2, 1, 12)));

        jLabel33.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel33.setText("Mail:");

        jLabel34.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel34.setText("Số điện thoại:");

        myTextField18.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)), BorderFactory.createEmptyBorder(1, 2, 1, 12)));

        GroupLayout roundedGradientPanel3Layout = new GroupLayout(roundedGradientPanel3);
        roundedGradientPanel3.setLayout(roundedGradientPanel3Layout);
        roundedGradientPanel3Layout.setHorizontalGroup(
            roundedGradientPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(roundedGradientPanel3Layout.createSequentialGroup()
                .addComponent(jLabel16, GroupLayout.PREFERRED_SIZE, 507, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(roundedGradientPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(button6, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(circleBackgroundPanel3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(roundedGradientPanel3Layout.createSequentialGroup()
                        .addGroup(roundedGradientPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(comboBoxSuggestion9, GroupLayout.PREFERRED_SIZE, 157, GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel31, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(roundedGradientPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(roundedGradientPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel32, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 19, Short.MAX_VALUE))
                            .addComponent(comboBoxSuggestion10, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(myTextField17, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(myTextField18, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(myTextField5, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(myTextField14, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(roundedGradientPanel3Layout.createSequentialGroup()
                        .addGroup(roundedGradientPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel33, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel34, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel20, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel26, GroupLayout.PREFERRED_SIZE, 151, GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        roundedGradientPanel3Layout.setVerticalGroup(
            roundedGradientPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(roundedGradientPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(roundedGradientPanel3Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addGroup(roundedGradientPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addGap(10, 10, 10))
                    .addGroup(roundedGradientPanel3Layout.createSequentialGroup()
                        .addComponent(circleBackgroundPanel3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel20)
                        .addGap(7, 7, 7)
                        .addComponent(myTextField5, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(jLabel26)
                        .addGap(7, 7, 7)
                        .addComponent(myTextField14, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(roundedGradientPanel3Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                            .addGroup(roundedGradientPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel31)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(comboBoxSuggestion9, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addGroup(roundedGradientPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel32)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(comboBoxSuggestion10, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel33)
                        .addGap(7, 7, 7)
                        .addComponent(myTextField17, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel34)
                        .addGap(7, 7, 7)
                        .addComponent(myTextField18, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(button6, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))))
        );

        GroupLayout AddDialogLayout = new GroupLayout(AddDialog.getContentPane());
        AddDialog.getContentPane().setLayout(AddDialogLayout);
        AddDialogLayout.setHorizontalGroup(
            AddDialogLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(AddDialogLayout.createSequentialGroup()
                .addComponent(roundedGradientPanel3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        AddDialogLayout.setVerticalGroup(
            AddDialogLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(roundedGradientPanel3, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setOpaque(false);

        roundedPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setVerticalScrollBar(new ScrollBarCustom());

        jTable1.setModel(new DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Mã tài khoản", "Họ tên", "Giới tính", "Vai trò", "Trạng thái", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true
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
        button1.setText("Thêm tài khoản");
        button1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        button1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button1ActionPerformed(evt);
            }
        });

        button14.setBorder(null);
        button14.setIcon(new ImageIcon(getClass().getResource("/Image/icons8-excel-30.png"))); // NOI18N
        button14.setText("Thêm từ Excel");
        button14.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        button14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    button14ActionPerformed(evt);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        comboBoxSuggestion1.setModel(new DefaultComboBoxModel(new String[] { "Giới tính", "Nam", "Nữ" }));
        comboBoxSuggestion1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    comboBoxSuggestion1ActionPerformed(evt);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        comboBoxSuggestion2.setModel(new DefaultComboBoxModel(new String[] { "Vai trò", "Sinh viên", "Giảng viên", "Quản trị viên", "" }));
        comboBoxSuggestion2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    comboBoxSuggestion2ActionPerformed(evt);
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
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(comboBoxSuggestion1, GroupLayout.PREFERRED_SIZE, 122, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(comboBoxSuggestion2, GroupLayout.PREFERRED_SIZE, 144, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 82, Short.MAX_VALUE)
                .addComponent(button1, GroupLayout.PREFERRED_SIZE, 137, GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(button14, GroupLayout.PREFERRED_SIZE, 133, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(button2, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );
        circleBackgroundPanel1Layout.setVerticalGroup(
            circleBackgroundPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(circleBackgroundPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(circleBackgroundPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addGroup(circleBackgroundPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(button1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(searchTextField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(comboBoxSuggestion1, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addComponent(comboBoxSuggestion2, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
                    .addComponent(button2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(button14, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(19, 19, 19))
        );

        GroupLayout roundedPanel1Layout = new GroupLayout(roundedPanel1);
        roundedPanel1.setLayout(roundedPanel1Layout);
        roundedPanel1Layout.setHorizontalGroup(
            roundedPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(roundedPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(circleBackgroundPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        roundedPanel1Layout.setVerticalGroup(
            roundedPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, roundedPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(circleBackgroundPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 361, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.add(pagination1);

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(roundedPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(roundedPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void button2ActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {//GEN-FIRST:event_button2ActionPerformed
        list=tk_dao.getDanhSachTaiKhoan();
        initPag(1);
    }//GEN-LAST:event_button2ActionPerformed

    private void button1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button1ActionPerformed
        
        AddDialog.pack();
        AddDialog.setLocationRelativeTo(null);
        AddDialog.setModal(true);
        AddDialog.setIconImage(icon.getImage());
        AddDialog.setVisible(true);
    }//GEN-LAST:event_button1ActionPerformed
        
    private void button3ActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {//GEN-FIRST:event_button3ActionPerformed
//          System.out.println(tk_dao.generatedTenTaiKhoan(comboBoxSuggestion8.getSelectedIndex())+" "+tk_dao.generatePassword(myTextField13.getText(), myTextField1.getText(), comboBoxSuggestion8.getSelectedIndex()));
        if(validateUpdate()){
            TaiKhoan tk= tk_dao.getTaiKhoan(jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString());
            TaiKhoan updateTK= new TaiKhoan(jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString(), myTextField19.getText(),
                    new String(myPasswordField2.getPassword()),
                    myTextField19.getText(), 
                    myTextField13.getText(),
                    comboBoxSuggestion8.getSelectedIndex()==0?"SV":comboBoxSuggestion10.getSelectedIndex()==1?"GV":"AD", 
                    "enable",
                    tk.getDangOnline(),
                    comboBoxSuggestion7.getSelectedItem().toString(), 
                    myTextField15.getText(), myTextField16.getText());
            System.out.println(tk);
            if(tk_dao.updateTaiKhoan(updateTK)){
                EditDialog.dispose();
                initTable();
                jTable1.getCellEditor().cancelCellEditing();
            }else{
                JOptionPane.showMessageDialog(null, "Cập nhật không thành công!");
            }
        }
        
    }//GEN-LAST:event_button3ActionPerformed

    private void button14ActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {//GEN-FIRST:event_button14ActionPerformed
           JnaFileChooser fileChooser = new JnaFileChooser();
            if (fileChooser.showOpenDialog(null)) {
                 File selectedFile = fileChooser.getSelectedFile();
                String filePath = selectedFile.getAbsolutePath();
                tk_dao.importTaiKhoanFromExcel(filePath);
                JOptionPane.showMessageDialog(null, "Thêm danh sách tài khoản thành công","Thông báo",JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_button14ActionPerformed

    private void button4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button4ActionPerformed
        AddSuccess.dispose();
    }//GEN-LAST:event_button4ActionPerformed
    private boolean eyeClick= false;
    private void jLabel12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel12MouseClicked
        if(!eyeClick){
            myPasswordField1.setEchoChar((char)0);
            jLabel12.setIcon(new ImageIcon(getClass().getResource("/Image/eye_open.png")));
            eyeClick=true;
        }else{
            myPasswordField1.setEchoChar('*');
            jLabel12.setIcon(new ImageIcon(getClass().getResource("/Image/eye_closed.png")));
            eyeClick=false;
        }
    }//GEN-LAST:event_jLabel12MouseClicked

    private void button5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button5ActionPerformed
        ViewDialog.dispose();
    }//GEN-LAST:event_button5ActionPerformed

    private void button6ActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {//GEN-FIRST:event_button6ActionPerformed
        if(validateAccount()){
            TaiKhoan tk= new TaiKhoan(tk_dao.generateMa(), tk_dao.generatedTenTaiKhoan(comboBoxSuggestion10.getSelectedIndex()), 
                    tk_dao.generatePassword(myTextField14.getText(), myTextField5.getText(), comboBoxSuggestion10.getSelectedIndex()),
                    myTextField5.getText(), myTextField14.getText(), 
                    comboBoxSuggestion10.getSelectedIndex()==0?"SV":comboBoxSuggestion10.getSelectedIndex()==1?"GV":"AD",
                    "enable", 
                    "offline", comboBoxSuggestion9.getSelectedItem().toString(),
                    myTextField18.getText().trim(), myTextField17.getText().trim());
            if(tk_dao.addTaiKhoan(tk)){
                
                ClearAddDialog();
                AddDialog.dispose();
                jLabel5.setText("Tên tài khoản: "+tk.getTenTaiKhoan());
                jLabel6.setText("Mật khẩu: "+tk.getMatKhau());
                AddSuccess.pack();
                AddSuccess.setLocationRelativeTo(null);
                AddSuccess.setVisible(true);
            }
            else{
                JOptionPane.showMessageDialog(null, "Thêm tài khoản thất bại");
            }
        }else{
            System.out.println("them that bai");
        }
    }//GEN-LAST:event_button6ActionPerformed
    private boolean editEye=false;
    private void jLabel22MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel22MouseClicked
        if(!editEye){
            myPasswordField2.setEchoChar((char)0);
            jLabel22.setIcon(new ImageIcon(getClass().getResource("/Image/eye_open.png")));
            editEye=true;
        }else{
            myPasswordField2.setEchoChar('*');
            jLabel22.setIcon(new ImageIcon(getClass().getResource("/Image/eye_closed.png")));
            editEye=false;
        }
    }//GEN-LAST:event_jLabel22MouseClicked

    private void comboBoxSuggestion1ActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {//GEN-FIRST:event_comboBoxSuggestion1ActionPerformed
        filterTaiKhoan();
    }//GEN-LAST:event_comboBoxSuggestion1ActionPerformed

    private void comboBoxSuggestion2ActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {//GEN-FIRST:event_comboBoxSuggestion2ActionPerformed
        filterTaiKhoan();
    }//GEN-LAST:event_comboBoxSuggestion2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JDialog AddDialog;
    private JDialog AddExcel;
    private JDialog AddSuccess;
    private JDialog EditDialog;
    private JDialog ViewDialog;
    private Button button1;
    private Button button14;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    private Button button6;
    private CircleBackgroundPanel circleBackgroundPanel1;
    private CircleBackgroundPanel circleBackgroundPanel2;
    private CircleBackgroundPanel circleBackgroundPanel3;
    private ComboBoxSuggestion comboBoxSuggestion1;
    private ComboBoxSuggestion comboBoxSuggestion10;
    private ComboBoxSuggestion comboBoxSuggestion2;
    private ComboBoxSuggestion comboBoxSuggestion7;
    private ComboBoxSuggestion comboBoxSuggestion8;
    private ComboBoxSuggestion comboBoxSuggestion9;
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
    private JLabel jLabel25;
    private JLabel jLabel26;
    private JLabel jLabel27;
    private JLabel jLabel28;
    private JLabel jLabel29;
    private JLabel jLabel3;
    private JLabel jLabel30;
    private JLabel jLabel31;
    private JLabel jLabel32;
    private JLabel jLabel33;
    private JLabel jLabel34;
    private JLabel jLabel35;
    private JLabel jLabel36;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JLabel jLabel7;
    private JLabel jLabel8;
    private JLabel jLabel9;
    private JPanel jPanel1;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane3;
    private JTable jTable1;
    private JTable jTable3;
    private MyPasswordField myPasswordField1;
    private MyPasswordField myPasswordField2;
    private MyTextField myTextField1;
    private MyTextField myTextField10;
    private MyTextField myTextField13;
    private MyTextField myTextField14;
    private MyTextField myTextField15;
    private MyTextField myTextField16;
    private MyTextField myTextField17;
    private MyTextField myTextField18;
    private MyTextField myTextField19;
    private MyTextField myTextField2;
    private MyTextField myTextField3;
    private MyTextField myTextField4;
    private MyTextField myTextField5;
    private MyTextField myTextField6;
    private MyTextField myTextField7;
    private MyTextField myTextField8;
    private Pagination pagination1;
    private RoundedGradientPanel roundedGradientPanel1;
    private RoundedGradientPanel roundedGradientPanel2;
    private RoundedGradientPanel roundedGradientPanel3;
    private RoundedPanel roundedPanel1;
    private RoundedPanel roundedPanel3;
    private RoundedPanel roundedPanel4;
    private SearchTextField searchTextField1;
    // End of variables declaration//GEN-END:variables
}
