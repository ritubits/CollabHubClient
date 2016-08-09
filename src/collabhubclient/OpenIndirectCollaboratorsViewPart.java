package collabhubclient;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.part.ViewPart;

public class OpenIndirectCollaboratorsViewPart extends ViewPart{

	boolean DEBUG= false;
	public OpenIndirectCollaboratorsViewPart()
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
		column1.setWidth(150);
		column1.setText("Name of the Collaborator");
		
		
		TableColumn column2 = new TableColumn(table, SWT.LEFT);
		column2.setWidth(80);
		column2.setText("Artifact Name:1");
		
		TableColumn column3 = new TableColumn(table, SWT.LEFT);
		column3.setWidth(80);
		column3.setText("Artifact Name:2");
		
		TableColumn column4 = new TableColumn(table, SWT.LEFT);
		column4.setWidth(80);
		column4.setText("Artifact Name:3");
		
		TableColumn column5 = new TableColumn(table, SWT.LEFT);
		column5.setWidth(80);
		column5.setText("Artifact Name:4");
			
		TableColumn column6 = new TableColumn(table, SWT.LEFT);
		column6.setWidth(80);
		column6.setText("Artifact Name:5");
		
		TableItem row1 = new TableItem(table, SWT.NONE);
		TableItem row2 = new TableItem(table, SWT.NONE);
		TableItem row3 = new TableItem(table, SWT.NONE);		
		TableItem row4 = new TableItem(table, SWT.NONE);		
		TableItem row5 = new TableItem(table, SWT.NONE);


		row1.setText(new String[] { "Vinita", "triangle.java", "sqaure.java", "rectangle.java", "circle.java" });
		row2.setText(new String[] { "Shikhar","triangle.java","isosceles.java","square.java" });
		row3.setText(new String[] { "Shekhar","square.java"});
		row4.setText(new String[] { "Mihir","triangle.java","equilateral.java","circle.java" });
		

		
		
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}

}
