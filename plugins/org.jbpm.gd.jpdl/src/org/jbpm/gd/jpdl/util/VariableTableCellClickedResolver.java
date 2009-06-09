package org.jbpm.gd.jpdl.util;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class VariableTableCellClickedResolver implements MouseListener {
	
	private Table table;
	private VariableTableItemWrapper selectedWrapper;
	
	public VariableTableCellClickedResolver(Table table){
		this.table = table;
	}

	public void mouseDoubleClick(MouseEvent e) {
	}

	public void mouseDown(MouseEvent e) {
		TableItem item = getSelectedTableItem();
		if (item == null) return;
		if (selectedWrapper != null) {
			selectedWrapper.cancelEditing();
			selectedWrapper = null;
		}
		int column = getSelectedColumn(e.x, e.y);
		if (column == -1) return;
		if (column == 1 || column == 2) {
			selectedWrapper = (VariableTableItemWrapper)item.getData();
			selectedWrapper.editCell(column);
		}
	}

	public void mouseUp(MouseEvent e) {
	}
	
	private TableItem getSelectedTableItem() {
		TableItem[] selection = table.getSelection();
		if (selection.length > 0) {
			return selection[0];
		} else {
			return null;
		}
	}
	
	private int getSelectedColumn(int x, int y) {
        int columnToEdit = -1;
        int columns = table.getColumnCount();
        TableItem tableItem = getSelectedTableItem();
        if (tableItem == null) return -1;
        for (int i = 0; i < columns; i++) {
            Rectangle bounds = getBounds(tableItem, i);
            if (bounds.contains(x, y)) {
                columnToEdit = i;
                break;
            }
        }	
        return columnToEdit;
	}
	
	private Rectangle getBounds(TableItem item, int columnIndex) {
		return item.getBounds(columnIndex);
	}
	
}
