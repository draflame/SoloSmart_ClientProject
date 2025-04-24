/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Components;

import java.rmi.RemoteException;

/**
 *
 * @author Trong Nghia
 */
public interface TableActionEvent {

    public void onEdit(int row) throws RemoteException;

    public void onDelete(int row) throws RemoteException;

    public void onView(int row) throws RemoteException;
}
