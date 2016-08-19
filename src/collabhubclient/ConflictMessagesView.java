package collabhubclient;


import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Color;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.part.ViewPart;
  

public class ConflictMessagesView extends ViewPart{

	boolean DEBUG= true;
	 public static TableViewer viewer;
	public ConflictMessagesView ()
	{
		super();
	}
	
	@Override
	public void createPartControl(Composite parent) {

		 viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
			        | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		 
		 final Table table = viewer.getTable();
		 
	//	Table table = new Table(parent, SWT.SINGLE);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		 viewer.setContentProvider(new ArrayContentProvider());
		 
		 getSite().setSelectionProvider(viewer);
		 
		Display display= table.getDisplay();
		
		 viewer.setInput(ConflictModelProvider.INSTANCE.getConflictMessages());
		 
		display.getActiveShell();
	//	  Color yellow = display.getSystemColor(SWT.COLOR_YELLOW);
	//	    Color blue = display.getSystemColor(SWT.COLOR_CYAN);
		    
		
		TableColumn column21 = new TableColumn(table, SWT.CENTER);
		column21.setWidth(1000);
		column21.setText("Conflict Message");
		column21.setMoveable(false);

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		  viewer.getControl().setFocus();
		  if (viewer!=null) viewer.refresh();
	}
	
	 public TableViewer getViewer() {
		    return viewer;
		  }

	 class MyTableCellRenderer extends DefaultTableCellRenderer//implements TableCellRenderer
	 {
	 public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col)
	 {
	 Component c = super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,col);
	 //setBackground((Color)value);
	 if(isSelected)
	 {
	 c.setBackground(Color.GRAY);
	 }
	 else
	 {
	 c.setBackground(Color.cyan);
	 }
	 return c;
	 }
	 }
}




