/*
*@ (#) CustomTableModel.java		1.0		Oct 23, 2024
*Copyright (c) 2024 IUH. All rights reserved.
*/
package Components;

import javax.swing.table.DefaultTableModel;

/*
* @description:
* @author: Nguyen Trong Nghia
* @date: 	Oct 23, 2024
* @version:		1.0
*/
public class CustomTableModel extends DefaultTableModel {
	public CustomTableModel(Object[][] data, Object[] columnNames) {
        super(data, columnNames);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        // Không cho phép chỉnh sửa ở dòng 0, 1, 2 và cột 0, 1, 2
        return row >= 3 && column >= 3; // Cho phép chỉnh sửa từ dòng 3 trở đi và cột 3 trở đi
    }
}
