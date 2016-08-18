package collabhubclient;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.part.ViewPart;
  

public class SemiDirectCollaboratorsViewPart  extends ViewPart{

	boolean DEBUG= true;
	 public static TableViewer viewer;
	public SemiDirectCollaboratorsViewPart  ()
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
		
		 viewer.setInput(SemiDirectModelProvider.INSTANCE.getCollaborators());
		 
		display.getActiveShell();
    
		
		    TableColumn column1 = new TableColumn(table, SWT.LEFT);
			column1.setWidth(200);
			column1.setText("Name of the Collaborator");
			column1.setMoveable(false);

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

}


