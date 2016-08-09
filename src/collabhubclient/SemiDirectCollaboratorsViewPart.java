package collabhubclient;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.part.ViewPart;

public class SemiDirectCollaboratorsViewPart extends ViewPart{

	boolean DEBUG= false;
	public SemiDirectCollaboratorsViewPart()
	{
		super();
	}
	
	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub
		
		Table table = new Table(parent, SWT.SINGLE);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableColumn column1 = new TableColumn(table, SWT.LEFT);
		column1.setWidth(200);
		column1.setText("Name of the Collaborator");
		
		
		TableItem row1 = new TableItem(table, SWT.NONE);
		TableItem row2 = new TableItem(table, SWT.NONE);
		TableItem row3 = new TableItem(table, SWT.NONE);		
		TableItem row4 = new TableItem(table, SWT.NONE);

		row1.setText(new String[] { "Gautam"});
		row2.setText(new String[] { "Mike" });
		row3.setText(new String[] { "Hinam" });
		row4.setText(new String[] { "Jatin"});
		
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}
	
}
