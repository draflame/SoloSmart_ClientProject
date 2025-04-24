package Components;

import Components.chart.ModelChart;
import DB.CreateDB;
import Dao.IBaiKiemTra_DAO;
import Entity.TaiKhoan;
import jakarta.persistence.EntityManager;
import service.RmiServiceLocator;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.Map;

/**
 *
 * @author Admin
 */
public class DialogThongKeBaiKiemTra extends JDialog {
    private static EntityManager em;
    private IBaiKiemTra_DAO baiKiemTra_DAO = RmiServiceLocator.getBaiKiemTraDao();
    private int quantity;

    public DialogThongKeBaiKiemTra() throws RemoteException {
        em = CreateDB.createDB();
        String maBaiKiemTra = "BKT21042025233306599";

        initComponents();
        getContentPane().setBackground(new Color(250, 250, 250));

        chart.addLegend("Số lượng sinh viên", new Color(100, 149, 237));

        // Lấy dữ liệu điểm từ DAO
        Map<TaiKhoan, Float> map = baiKiemTra_DAO.getDsTaiKhoanThamGiaKiemTraVaDiemSo(maBaiKiemTra);

        // Tạo mảng thống kê tần suất điểm từ 1 đến 10
        int[] thongKeDiem = new int[10];
        quantity = 0;

        System.out.println(map.values());

        for (Float diem : map.values()) {
            if (diem != null) {
                int diemLamTron = (int) Math.ceil(diem);
                if (diemLamTron >= 1 && diemLamTron <= 10) {
                    thongKeDiem[diemLamTron-1]++;
                    quantity++;
                }
            }
        }

        for(int diem : thongKeDiem) {
            System.out.println(diem);
        }

        for (int i = 0; i < thongKeDiem.length; i++) {
            String nhan = String.valueOf(i + 1);
            chart.addData(new ModelChart(nhan, new double[]{thongKeDiem[i]}));
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        chart = new Components.chart.Chart(quantity);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Biểu đồ tần suất điểm sinh viên");

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(50, 50, 50)
                                .addComponent(chart, GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)
                                .addGap(50, 50, 50))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(50, 50, 50)
                                .addComponent(chart, GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
                                .addGap(50, 50, 50))
        );

        pack();
        setLocationRelativeTo(null);
    }

    public static void main(String args[]) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException ex) {
        }

        EventQueue.invokeLater(() -> {
            try {
                new DialogThongKeBaiKiemTra().setVisible(true);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private Components.chart.Chart chart;
}
