package collabhubclient;

import javax.swing.table.DefaultTableCellRenderer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.part.ViewPart;
  

public class ConflictMessagesView extends ViewPart{

	public ConflictMessagesView ()
	{
		super();
	}
	
	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub
		
/*		Display display = new Display();
		Color red = display.getSystemColor(SWT.COLOR_RED);
	    Color blue = display.getSystemColor(SWT.COLOR_BLUE);
	    Color white = display.getSystemColor(SWT.COLOR_WHITE);
	    Color gray = display.getSystemColor(SWT.COLOR_GRAY);    */
	    
		Table table = new Table(parent, SWT.SINGLE);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableColumn column1 = new TableColumn(table, SWT.LEFT);
		column1.setWidth(10);
		column1.setText("");
		column1.setMoveable(false);
		column1.setResizable(false);
		
		TableColumn column2 = new TableColumn(table, SWT.LEFT);
		column2.setWidth(10);
		column2.setText("");
		column2.setMoveable(false);
		column2.setResizable(false);
		
		TableColumn column3 = new TableColumn(table, SWT.LEFT);
		column3.setWidth(10);
		column3.setText("");
		column3.setMoveable(false);
		column3.setResizable(false);
		
		TableColumn column4 = new TableColumn(table, SWT.LEFT);
		column4.setWidth(10);
		column4.setText("");
		column4.setMoveable(false);
		column4.setResizable(false);
		
		TableColumn column5 = new TableColumn(table, SWT.LEFT);
		column5.setWidth(10);
		column5.setText("");
		column5.setMoveable(false);
		column5.setResizable(false);
		
		TableColumn column6 = new TableColumn(table, SWT.LEFT);
		column6.setWidth(10);
		column6.setText("");
		column6.setMoveable(false);
		column6.setResizable(false);
		
		TableColumn column7 = new TableColumn(table, SWT.LEFT);
		column7.setWidth(10);
		column7.setText("");
		column7.setMoveable(false);
		column7.setResizable(false);
		
		TableColumn column8 = new TableColumn(table, SWT.LEFT);
		column8.setWidth(10);
		column8.setText("");
		column8.setMoveable(false);
		column8.setResizable(false);
		
		TableColumn column9 = new TableColumn(table, SWT.LEFT);
		column9.setWidth(10);
		column9.setText("");
		column9.setMoveable(false);
		column9.setResizable(false);
		
		TableColumn column10 = new TableColumn(table, SWT.LEFT);
		column10.setWidth(10);
		column10.setText("");
		column10.setMoveable(false);
		column10.setResizable(false);
		
		TableColumn column11 = new TableColumn(table, SWT.LEFT);
		column11.setWidth(10);
		column11.setText("");
		column11.setMoveable(false);
		column11.setResizable(false);
		
		TableColumn column12 = new TableColumn(table, SWT.LEFT);
		column12.setWidth(10);
		column12.setText("");
		column12.setMoveable(false);
		column12.setResizable(false);
		
		TableColumn column13 = new TableColumn(table, SWT.LEFT);
		column13.setWidth(10);
		column13.setText("");
		column13.setMoveable(false);
		column13.setResizable(false);
		
		TableColumn column14 = new TableColumn(table, SWT.LEFT);
		column14.setWidth(10);
		column14.setText("");
		column14.setMoveable(false);
		column14.setResizable(false);
	      
		
		TableColumn column15 = new TableColumn(table, SWT.LEFT);
		column15.setWidth(10);
		column15.setText("");
		column15.setMoveable(false);
		column15.setResizable(false);
		
		TableColumn column16 = new TableColumn(table, SWT.LEFT);
		column16.setWidth(10);
		column16.setText("");
		column16.setMoveable(false);
		column16.setResizable(false);
		
		TableColumn column17 = new TableColumn(table, SWT.LEFT);
		column17.setWidth(10);
		column17.setText("");
		column17.setMoveable(false);
		column17.setResizable(false);
		
		TableColumn column18 = new TableColumn(table, SWT.LEFT);
		column18.setWidth(10);
		column18.setText("");
		column18.setMoveable(false);
		column18.setResizable(false);
		
		TableColumn column19 = new TableColumn(table, SWT.LEFT);
		column19.setWidth(10);
		column19.setText("");
		column19.setMoveable(false);
		column19.setResizable(false);
		
		TableColumn column20 = new TableColumn(table, SWT.LEFT);
		column20.setWidth(10);
		column20.setText("");
		column20.setMoveable(false);
		column20.setResizable(false);
		
		TableColumn column21 = new TableColumn(table, SWT.CENTER);
		column21.setWidth(700);
		column21.setText("Conflict Message");
		column21.setMoveable(false);

		
		TableItem row1 = new TableItem(table, SWT.NONE);
		TableItem row2 = new TableItem(table, SWT.NONE);


	//	row1.setText(new String[] { "Heena", "Method::setData()", "Line No:: 12" });
	//	row2.setText(new String[] { "Sagar", "Method::fillColor()", "Line No:: 62" });
		
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}
}
	
/*	class MyTableCellRenderer extends DefaultTableCellRenderer {

	    @Override
	    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
	    	DefaultTableModel  model = (DefaultTableModel ) table.getModel();
	        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	        c.setBackground(Color.RED);
	        return c;
	    }
	}*/
