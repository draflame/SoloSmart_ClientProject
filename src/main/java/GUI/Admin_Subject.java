/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package GUI;

import Components.*;
import Dao.IMonHoc_DAO;
import Entity.MonHoc;
import service.RmiServiceLocator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 *
 * @author THANH PHU
 */
public class Admin_Subject extends JPanel {

    /**
     * Creates new form Admin_Subject
     */
    private IMonHoc_DAO mh_dao= RmiServiceLocator.getMonHocDao();
    private ArrayList<MonHoc> list= new ArrayList<>();
    public Admin_Subject() throws RemoteException {
        initComponents();
        TableActionEvent event = new TableActionEvent() {
            @Override
            public void onEdit(int row) {
                String maMonHoc = jTable1.getValueAt(row, 0).toString();
                MonHoc monHoc = list.stream().filter(mh -> mh.getMaMonHoc().equals(maMonHoc)).findFirst().orElse(null);
                myTextField6.setText(monHoc.getTenMonHoc());
                myTextField7.setText(monHoc.getTrangThai());
                EditDialog.pack();
                EditDialog.setLocationRelativeTo(null);
                EditDialog.setVisible(true);
            }

            @Override
            public void onDelete(int row) throws RemoteException {
                int[] selectedRows = jTable1.getSelectedRows();

                if (selectedRows.length == 0) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn ít nhất một dòng để xóa!");
                } else {
                int confirm = JOptionPane.showConfirmDialog(
                null,
                "Bạn có chắc chắn muốn xóa " + selectedRows.length + " môn học đã chọn?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                for (int r : selectedRows) {
                    String maMonHoc = jTable1.getValueAt(r, 0).toString(); // giả sử cột 0 là mã môn học
                    mh_dao.deleteMonHoc(maMonHoc);
                    jTable1.getCellEditor().cancelCellEditing();
                }
                initTable();
            }
        }

            }

            @Override
            public void onView(int row) {
                String maMonHoc = jTable1.getValueAt(row, 0).toString();
                MonHoc monHoc = list.stream().filter(mh -> mh.getMaMonHoc().equals(maMonHoc)).findFirst().orElse(null);
                myTextField2.setText(monHoc.getMaMonHoc());
                myTextField3.setText(monHoc.getTenMonHoc());
                myTextField4.setText(monHoc.getTrangThai());
                ViewDialog.pack();
                ViewDialog.setLocationRelativeTo(null);
                ViewDialog.setVisible(true);
            }
        };
        jTable1.getColumnModel().getColumn(2).setCellRenderer(new TableActionCellRender());
        jTable1.getColumnModel().getColumn(2).setCellEditor(new TableActionCellEditor(event));
        
        
        searchTextField1.addActionListener(x->{

            try {
                list= mh_dao.getDanhSachMonHocTheoTen(searchTextField1.getText());
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            if(list.size()>0){
                initPage(1);
            }
            else{
                JOptionPane.showMessageDialog(null,"khong tim thay");
            }
            
        });
        initTable();
        pagination1.setPaginationItemRender(new PaginationItemRenderStyle1());
        initPage(1);
        pagination1.addEventPagination(new EventPagination(){
            @Override
            public void pageChanged(int page) {
                initPage(page);
            }
            
        });
    }
    public void initPage(int page){
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
    public void initTable() throws RemoteException {
        list=mh_dao.getDanhSachMonHoc();
        initPage(1);
                
    }
    public void updateTable(ArrayList<MonHoc> list){
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);
        list.forEach(x->{
            model.addRow(new Object[]{
                x.getMaMonHoc(),
                x.getTenMonHoc()
            });
        });
                
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
        jPanel1 = new JPanel();
        jLabel2 = new JLabel();
        jPanel2 = new JPanel();
        jLabel1 = new JLabel();
        myTextField1 = new MyTextField();
        jPanel3 = new JPanel();
        button3 = new Button();
        ViewDialog = new JDialog();
        jPanel4 = new JPanel();
        jLabel3 = new JLabel();
        jPanel5 = new JPanel();
        jLabel4 = new JLabel();
        myTextField2 = new MyTextField();
        jLabel5 = new JLabel();
        myTextField3 = new MyTextField();
        jLabel6 = new JLabel();
        myTextField4 = new MyTextField();
        EditDialog = new JDialog();
        jPanel6 = new JPanel();
        jLabel7 = new JLabel();
        jPanel7 = new JPanel();
        jLabel9 = new JLabel();
        myTextField6 = new MyTextField();
        jLabel10 = new JLabel();
        myTextField7 = new MyTextField();
        jPanel11 = new JPanel();
        button5 = new Button();
        roundedPanel1 = new RoundedPanel();
        jScrollPane1 = new JScrollPane();
        jTable1 = new JTable();
        circleBackgroundPanel1 = new CircleBackgroundPanel();
        button2 = new Button();
        button1 = new Button();
        searchTextField1 = new SearchTextField();
        jPanel8 = new JPanel();
        pagination1 = new Pagination();

        AddDialog.setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(58, 122, 141));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));

        jLabel2.setBackground(new java.awt.Color(58, 122, 141));
        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel2.setText("Thêm Môn Học");

        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel2, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
                .addGap(0, 6, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Tên Môn Học");
        jLabel1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jPanel2.add(jLabel1);

        myTextField1.setBackground(new java.awt.Color(204, 204, 204));
        myTextField1.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        myTextField1.setPreferredSize(new java.awt.Dimension(200, 36));
        myTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                myTextField1ActionPerformed(evt);
            }
        });
        jPanel2.add(myTextField1);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        button3.setBackground(new java.awt.Color(58, 122, 141));
        button3.setForeground(new java.awt.Color(255, 255, 255));
        button3.setText("Xác nhận");
        button3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        button3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    button3ActionPerformed(evt);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        GroupLayout jPanel3Layout = new GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(button3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(10, Short.MAX_VALUE)
                .addComponent(button3, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        GroupLayout AddDialogLayout = new GroupLayout(AddDialog.getContentPane());
        AddDialog.getContentPane().setLayout(AddDialogLayout);
        AddDialogLayout.setHorizontalGroup(
            AddDialogLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(AddDialogLayout.createSequentialGroup()
                .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jPanel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        AddDialogLayout.setVerticalGroup(
            AddDialogLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(AddDialogLayout.createSequentialGroup()
                .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        );

        jPanel4.setBackground(new java.awt.Color(58, 122, 141));
        jPanel4.setForeground(new java.awt.Color(255, 255, 255));

        jLabel3.setBackground(new java.awt.Color(58, 122, 141));
        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel3.setText("Thông tin chi tiết");

        GroupLayout jPanel4Layout = new GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel3, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
                .addGap(0, 6, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Mã môn học");
        jLabel4.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jLabel4.setPreferredSize(new java.awt.Dimension(110, 40));
        jPanel5.add(jLabel4);

        myTextField2.setEditable(false);
        myTextField2.setBackground(new java.awt.Color(204, 204, 204));
        myTextField2.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        myTextField2.setPreferredSize(new java.awt.Dimension(200, 36));
        myTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                myTextField2ActionPerformed(evt);
            }
        });
        jPanel5.add(myTextField2);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("Tên môn học");
        jLabel5.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jLabel5.setMaximumSize(new java.awt.Dimension(110, 40));
        jLabel5.setPreferredSize(new java.awt.Dimension(110, 40));
        jPanel5.add(jLabel5);

        myTextField3.setEditable(false);
        myTextField3.setBackground(new java.awt.Color(204, 204, 204));
        myTextField3.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        myTextField3.setPreferredSize(new java.awt.Dimension(200, 36));
        myTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                myTextField3ActionPerformed(evt);
            }
        });
        jPanel5.add(myTextField3);

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setText("Trạng thái");
        jLabel6.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jLabel6.setPreferredSize(new java.awt.Dimension(110, 40));
        jPanel5.add(jLabel6);

        myTextField4.setEditable(false);
        myTextField4.setBackground(new java.awt.Color(204, 204, 204));
        myTextField4.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        myTextField4.setAutoscrolls(false);
        myTextField4.setPreferredSize(new java.awt.Dimension(200, 36));
        myTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                myTextField4ActionPerformed(evt);
            }
        });
        jPanel5.add(myTextField4);

        GroupLayout ViewDialogLayout = new GroupLayout(ViewDialog.getContentPane());
        ViewDialog.getContentPane().setLayout(ViewDialogLayout);
        ViewDialogLayout.setHorizontalGroup(
            ViewDialogLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(ViewDialogLayout.createSequentialGroup()
                .addComponent(jPanel4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jPanel5, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        ViewDialogLayout.setVerticalGroup(
            ViewDialogLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(ViewDialogLayout.createSequentialGroup()
                .addComponent(jPanel4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, GroupLayout.PREFERRED_SIZE, 154, GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        EditDialog.setBackground(new java.awt.Color(255, 255, 255));

        jPanel6.setBackground(new java.awt.Color(58, 122, 141));
        jPanel6.setForeground(new java.awt.Color(255, 255, 255));

        jLabel7.setBackground(new java.awt.Color(58, 122, 141));
        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel7.setText("Thêm Môn Học");

        GroupLayout jPanel6Layout = new GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7, GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel7, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
                .addGap(0, 6, Short.MAX_VALUE))
        );

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setText("Tên Môn Học");
        jLabel9.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jPanel7.add(jLabel9);

        myTextField6.setBackground(new java.awt.Color(204, 204, 204));
        myTextField6.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        myTextField6.setPreferredSize(new java.awt.Dimension(200, 36));
        myTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                myTextField6ActionPerformed(evt);
            }
        });
        jPanel7.add(myTextField6);

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setText("Trạng Thái");
        jLabel10.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jLabel10.setPreferredSize(new java.awt.Dimension(109, 40));
        jPanel7.add(jLabel10);

        myTextField7.setBackground(new java.awt.Color(204, 204, 204));
        myTextField7.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        myTextField7.setPreferredSize(new java.awt.Dimension(200, 36));
        myTextField7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                myTextField7ActionPerformed(evt);
            }
        });
        jPanel7.add(myTextField7);

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));

        button5.setBackground(new java.awt.Color(58, 122, 141));
        button5.setForeground(new java.awt.Color(255, 255, 255));
        button5.setText("Xác nhận");
        button5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        button5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button5ActionPerformed(evt);
            }
        });

        GroupLayout jPanel11Layout = new GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(button5, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap(10, Short.MAX_VALUE)
                .addComponent(button5, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        GroupLayout EditDialogLayout = new GroupLayout(EditDialog.getContentPane());
        EditDialog.getContentPane().setLayout(EditDialogLayout);
        EditDialogLayout.setHorizontalGroup(
            EditDialogLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(EditDialogLayout.createSequentialGroup()
                .addComponent(jPanel6, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jPanel7, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(jPanel11, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        EditDialogLayout.setVerticalGroup(
            EditDialogLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(EditDialogLayout.createSequentialGroup()
                .addComponent(jPanel6, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, GroupLayout.PREFERRED_SIZE, 97, GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel11, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        );

        setOpaque(false);

        roundedPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setVerticalScrollBar(new ScrollBarCustom());

        jTable1.setModel(new DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Mã môn học", "Tên môn học", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true
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

        button2.setBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
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
        button1.setText("Thêm môn học");
        button1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        button1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button1ActionPerformed(evt);
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
                .addGap(22, 22, 22)
                .addGroup(circleBackgroundPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addGroup(circleBackgroundPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(button1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(searchTextField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addComponent(button2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.add(pagination1);

        GroupLayout roundedPanel1Layout = new GroupLayout(roundedPanel1);
        roundedPanel1.setLayout(roundedPanel1Layout);
        roundedPanel1Layout.setHorizontalGroup(
            roundedPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(roundedPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(circleBackgroundPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel8, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        roundedPanel1Layout.setVerticalGroup(
            roundedPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, roundedPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(circleBackgroundPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE)
                .addGap(5, 5, 5)
                .addComponent(jPanel8, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        );

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(roundedPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(roundedPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void button2ActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {//GEN-FIRST:event_button2ActionPerformed
        ArrayList<MonHoc> list= mh_dao.getDanhSachMonHoc();
        updateTable(list);
    }//GEN-LAST:event_button2ActionPerformed

    private void myTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_myTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_myTextField1ActionPerformed

    private void button3ActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {//GEN-FIRST:event_button3ActionPerformed
        String name = myTextField1.getText();
        if(name.trim().equals("")) {
            // Hiển thị thông báo khi tên môn học trống
            JOptionPane.showMessageDialog(
            null, 
            "Tên môn học không được để trống!", 
            "Lỗi", 
            JOptionPane.ERROR_MESSAGE
        );
    }
        else {
    // Thêm môn học mới
        mh_dao.addMonHoc(new MonHoc(mh_dao.generateMa(), name, "enable"));
    
        // Hiển thị thông báo thành công
        JOptionPane.showMessageDialog(
            null, 
            "Thêm môn học thành công!", 
            "Thành công", 
            JOptionPane.INFORMATION_MESSAGE
        );
    
        // Đóng dialog và làm mới bảng
        AddDialog.dispose();
        initTable();
        }
    }//GEN-LAST:event_button3ActionPerformed

    private void button1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button1ActionPerformed

        AddDialog.setLocationRelativeTo(null);
        AddDialog.setVisible(true);
        AddDialog.pack();
    }//GEN-LAST:event_button1ActionPerformed

    private void myTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_myTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_myTextField2ActionPerformed

    private void myTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_myTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_myTextField3ActionPerformed

    private void myTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_myTextField4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_myTextField4ActionPerformed

    private void myTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_myTextField6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_myTextField6ActionPerformed

    private void button5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button5ActionPerformed
        // Lấy thông tin từ các trường nhập liệu
        String tenMonHoc = myTextField6.getText().trim();
        String trangThai = myTextField7.getText().trim();

        // Kiểm tra dữ liệu hợp lệ
        if(tenMonHoc.isEmpty()) {
            JOptionPane.showMessageDialog(
                    null,
                    "Tên môn học không được để trống!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if(!trangThai.equals("enable") && !trangThai.equals("disable")) {
            JOptionPane.showMessageDialog(
                    null,
                    "Trạng thái phải là 'enable' hoặc 'disable'!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        try {
            // Lấy mã môn học từ dòng đang được chọn trong bảng
            int selectedRow = jTable1.getSelectedRow();
            if(selectedRow == -1) {
                JOptionPane.showMessageDialog(
                        null,
                        "Vui lòng chọn môn học cần sửa!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            String maMonHoc = jTable1.getValueAt(selectedRow, 0).toString();

            // Tạo đối tượng môn học mới với thông tin đã chỉnh sửa
            MonHoc monHoc = new MonHoc(maMonHoc, tenMonHoc, trangThai);

            // Cập nhật vào CSDL
            mh_dao.updateMonHoc(monHoc);

            // Hiển thị thông báo thành công
            JOptionPane.showMessageDialog(
                    null,
                    "Cập nhật môn học thành công!",
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE
            );

            // Đóng dialog và làm mới bảng
            EditDialog.dispose();
            initTable();

        } catch(Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    null,
                    "Có lỗi xảy ra khi cập nhật môn học!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }//GEN-LAST:event_button5ActionPerformed

    private void myTextField7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_myTextField7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_myTextField7ActionPerformed

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JDialog AddDialog;
    private JDialog EditDialog;
    private JDialog ViewDialog;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button5;
    private CircleBackgroundPanel circleBackgroundPanel1;
    private JLabel jLabel1;
    private JLabel jLabel10;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JLabel jLabel7;
    private JLabel jLabel9;
    private JPanel jPanel1;
    private JPanel jPanel11;
    private JPanel jPanel2;
    private JPanel jPanel3;
    private JPanel jPanel4;
    private JPanel jPanel5;
    private JPanel jPanel6;
    private JPanel jPanel7;
    private JPanel jPanel8;
    private JScrollPane jScrollPane1;
    private JTable jTable1;
    private MyTextField myTextField1;
    private MyTextField myTextField2;
    private MyTextField myTextField3;
    private MyTextField myTextField4;
    private MyTextField myTextField6;
    private MyTextField myTextField7;
    private Pagination pagination1;
    private RoundedPanel roundedPanel1;
    private SearchTextField searchTextField1;
    // End of variables declaration//GEN-END:variables
}
