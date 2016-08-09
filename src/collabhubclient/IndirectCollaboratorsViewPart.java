package collabhubclient;


import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.part.ViewPart;

public class IndirectCollaboratorsViewPart extends ViewPart{

	boolean DEBUG= false;
	public IndirectCollaboratorsViewPart()
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
		column2.setText("Artifact Name");
		
		TableColumn column4 = new TableColumn(table, SWT.LEFT);
		column4.setWidth(200);
		column4.setText("Current AST Element");
		
		TableColumn column5 = new TableColumn(table, SWT.LEFT);	
		column5.setWidth(100);
		column5.setText("Current Line No.");
		
		TableItem row1 = new TableItem(table, SWT.NONE);
		TableItem row2 = new TableItem(table, SWT.NONE);
		TableItem row3 = new TableItem(table, SWT.NONE);		
		TableItem row4 = new TableItem(table, SWT.NONE);		
		TableItem row5 = new TableItem(table, SWT.NONE);


		row1.setText(new String[] { "Vinita", "rectangle.java","Method::setData()", "Line No:: 22" });
		row2.setText(new String[] { "Sagar","triangle.java", "Method::fillColor()", "Line No:: 92" });
		row3.setText(new String[] { "Shekhar","rectangle.java", "Method::getColor()", "Line No:: 23" });
		row4.setText(new String[] { "Mihir","square.java", "Method::drawShape()", "Line No:: 11" });
		

		
		
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}

}
