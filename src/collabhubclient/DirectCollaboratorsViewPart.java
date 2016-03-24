package collabhubclient;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.part.ViewPart;

public class DirectCollaboratorsViewPart extends ViewPart{

	public DirectCollaboratorsViewPart()
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
		
		TableColumn column2 = new TableColumn(table, SWT.LEFT);
		column2.setWidth(200);
		column2.setText("Current AST Element");
		
		TableColumn column3 = new TableColumn(table, SWT.LEFT);	
		column3.setWidth(100);
		column3.setText("Current Line No.");
		
		TableItem row1 = new TableItem(table, SWT.NONE);
		TableItem row2 = new TableItem(table, SWT.NONE);


		row1.setText(new String[] { "Heena", "Method::setData()", "Line No:: 12" });
		row2.setText(new String[] { "Sagar", "Method::fillColor()", "Line No:: 62" });
		
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}

}
