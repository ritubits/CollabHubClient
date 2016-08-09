package collabhubclient;

import java.io.FileNotFoundException;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

public class DirectCollaboratorsViewPart extends ViewPart{

	 private TableViewer viewer;
	 Table table ;
	boolean DEBUG= true;
	public DirectCollaboratorsViewPart()
	{
		super();
	}
	

     ISelectionListener listener = new ISelectionListener() {
    	 
        public void selectionChanged(IWorkbenchPart part, ISelection sel) {
           if (!(sel instanceof IStructuredSelection))
              return;
           IStructuredSelection ss = (IStructuredSelection) sel;
          
           Object o = ss.getFirstElement();
           if (o != null)
           {
           System.out.println("SelectionChanged::"+o.toString());

           if (viewer!=null) viewer.refresh();
    
           }
        }
     };
     
	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub
		
		 int style = SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL;
	      viewer = new TableViewer(parent, style);

	      
	    getSite().getPage().addSelectionListener(listener);
	      
		if (DEBUG) System.out.println("From CreatePartControl:::: DirectCollaborators");
		table = viewer.getTable();//new Table(parent, SWT.SINGLE);
		
		
		
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
		
	//	TableItem row1 = new TableItem(table, SWT.NONE);
	//	TableItem row2 = new TableItem(table, SWT.NONE);

	
		IWorkbench workbench = PlatformUI.getWorkbench();
		//	workbench.
		IWorkbenchPage activePage = workbench.getActiveWorkbenchWindow().getActivePage();
		try {
			getCurrentFileName(activePage);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	//	List<DirectCollaboratorActivityObject> rows = DirectCollaboratorModelProvider.INSTANCE.getInputRows();
		
		DirectCollaboratorModelProvider modelProvider = new DirectCollaboratorModelProvider();
		    viewer.setContentProvider(modelProvider);
		    // get the content for the viewer, setInput will call getElements in the
		    // contentProvider
		    viewer.setInput(modelProvider);
		    // make the selection available to other views
		    getSite().setSelectionProvider(viewer);
		//    setColumnData(table, modelProvider.getElements(inputElement));
		    // set the sorter for the table

		    // define layout for the viewer
		    GridData gridData = new GridData();
		    gridData.verticalAlignment = GridData.FILL;
		    gridData.horizontalSpan = 2;
		    gridData.grabExcessHorizontalSpace = true;
		    gridData.grabExcessVerticalSpace = true;
		    gridData.horizontalAlignment = GridData.FILL;
		    viewer.getControl().setLayoutData(gridData);
		    
       //  viewer.refresh();
	//	row1.setText(new String[] { "Heena", "Method::setData()", "Line No:: 12" });
	//	row2.setText(new String[] { "Sagar", "Method::fillColor()", "Line No:: 62" });
		
	}

	public void setColumnData(Table table, List<DirectCollaboratorActivityObject> rows)
	{
		int count= table.getColumnCount();
		int i=0;
		TableItem row=null;
		for (DirectCollaboratorActivityObject rowObject: rows)
		{
			row = new TableItem(table, SWT.NONE);
			while (i < count){
				row.setText(new String[] { "Heena", "Method::setData()", "Line No:: 12" });
			//	row.setText(rowObject.getActivityArrayObject());		
			i++;
			    }
			}
	}
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		viewer.getControl().setFocus();
	}
	
	public String getCurrentFileName(IWorkbenchPage activePage) throws FileNotFoundException
	{
		String name=null;
		try{
			if (activePage !=null)
			  name = activePage.getActiveEditor().getEditorInput().getName();
			if (DEBUG) System.out.println("CurrentFileName from DirectCollaborators:: "+name);

		} catch(Exception e)
		{
			e.printStackTrace();
		}
		 return name;
		
	}

	public void dispose() {
        getSite().getPage().removeSelectionListener(listener);
     }
}
