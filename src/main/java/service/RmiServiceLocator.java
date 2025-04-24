package service;

import Dao.*;

import javax.naming.Context;
import javax.naming.InitialContext;

public class RmiServiceLocator {

    private static String hostname = "LAPTOP-9P64CM8L";
    private static final String RMI_URL = "rmi://" + hostname + ":4951/";
    private static Context context;

    static {
        try {
            context = new InitialContext();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ITaiKhoan_DAO getTaiKhoanDao() {
        try {
            return (ITaiKhoan_DAO) context.lookup(RMI_URL + "TaiKhoanDao");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static IBaiKiemTra_DAO getBaiKiemTraDao() {
        try {
            return (IBaiKiemTra_DAO) context.lookup(RMI_URL + "BaiKiemTraDao");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ICauHoi_DAO getCauHoiDao() {
        try {
            return (ICauHoi_DAO) context.lookup(RMI_URL + "CauHoiDao");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static IDeThi_DAO getDeThiDao() {
        try {
            return (IDeThi_DAO) context.lookup(RMI_URL + "DeThiDao");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static IDsCauTraLoi_DAO getDsCauTraLoiDao() {
        try {
            return (IDsCauTraLoi_DAO) context.lookup(RMI_URL + "DsCauTraLoiDao");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static IDsLuaChon_DAO getDsLuaChonDao() {
        try {
            return (IDsLuaChon_DAO) context.lookup(RMI_URL + "DsLuaChonDao");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static IKetQuaHocTap_DAO getKetQuaHocTapDao() {
        try {
            return (IKetQuaHocTap_DAO) context.lookup(RMI_URL + "KetQuaHocTapDao");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static IKetQuaKiemTra_DAO getKetQuaKiemTraDao() {
        try {
            return (IKetQuaKiemTra_DAO) context.lookup(RMI_URL + "KetQuaKiemTraDao");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ILopHoc_DAO getLopHocDao() {
        try {
            return (ILopHoc_DAO) context.lookup(RMI_URL + "LopHocDao");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static IMonHoc_DAO getMonHocDao() {
        try {
            return (IMonHoc_DAO) context.lookup(RMI_URL + "MonHocDao");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static INganHangDeThi_DAO getNganHangDeThiDao() {
        try {
            return (INganHangDeThi_DAO) context.lookup(RMI_URL + "NganHangDeThiDao");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
