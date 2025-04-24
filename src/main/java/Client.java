import Dao.ITaiKhoan_DAO;
import service.RmiServiceLocator;

import java.rmi.RemoteException;

public class Client {
    public static void main(String[] args) throws RemoteException {
        ITaiKhoan_DAO taiKhoanDao = RmiServiceLocator.getTaiKhoanDao();

        if (taiKhoanDao != null) {
            System.out.println(taiKhoanDao.getDanhSachTaiKhoan());
        } else {
            System.out.println("Không thể kết nối đến DAO từ xa.");
        }
    }
}
