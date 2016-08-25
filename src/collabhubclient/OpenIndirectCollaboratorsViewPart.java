package collabhubclient;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.part.ViewPart;
  

public class OpenIndirectCollaboratorsViewPart  extends ViewPart{

	boolean DEBUG= true;
	 public static TableViewer viewer;
	public OpenIndirectCollaboratorsViewPart  ()
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
		
		 viewer.setInput(OpenIndirectCollaboratorModelProvider.INSTANCE.getCollaborators());
		 
		display.getActiveShell();
    
		createColumns(parent,viewer);
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
	 
	 
	 private void createColumns(final Composite parent, final TableViewer viewer) {
		    String[] titles = { "Name of the Collaborator", "Artifact Name::1", "Artifact Name::2", "Artifact Name::3", "Artifact Name::4" };
		    int[] bounds = { 200, 150, 150, 150, 150 };

		    // first column is for the Name of the Collaborator
		    TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0], 0);
		    col.setLabelProvider(new ColumnLabelProvider() {
		      @Override
		      public String getText(Object element) {
		        String s = (String) element;
		        System.out.println("STRING::: "+s);
		        int index= s.indexOf(",");
		        System.out.println("STRING INDEX::: "+index);
		        if (index != -1) s= s.substring(0, index);
		        return s;
		      }
		    });

		    // artifact 1
		    col = createTableViewerColumn(titles[1], bounds[1], 1);
		    col.setLabelProvider(new ColumnLabelProvider() {
		      @Override
		      public String getText(Object element) {
		    	  String s = (String) element;
			        int index= s.indexOf(",");
			        if (index != -1) s= s.substring(index+1, s.length());
			        index= s.indexOf(",");
			        if (index != -1) s= s.substring(0, index);
			        return s;
		      }
		    });

		    // artifact 2.
		    col = createTableViewerColumn(titles[2], bounds[2], 2);
		    col.setLabelProvider(new ColumnLabelProvider() {
		      @Override
		      public String getText(Object element) {
		    	  String s = (String) element;
			        int index= s.indexOf(",");
			        if (index != -1) s= s.substring(index+1, s.length());
			        index= s.indexOf(",");
			        if (index != -1) s= s.substring(index+1, s.length());
			        index= s.indexOf(",");
			        if (index != -1) s= s.substring(0, index);
			        return (s);
		      }
		    });
		    
		    // artifact 3
		    col = createTableViewerColumn(titles[3], bounds[3], 3);
		    col.setLabelProvider(new ColumnLabelProvider() {
		      @Override
		      public String getText(Object element) {
		    	  String s = (String) element;
			        int index= s.indexOf(",");
			        if (index != -1) s= s.substring(index+1, s.length());
			        index= s.indexOf(",");
			        if (index != -1) s= s.substring(index+1, s.length());
			        index= s.indexOf(",");
			        if (index != -1) s= s.substring(index+1, s.length());
			        index= s.indexOf(",");
			        if (index != -1) s= s.substring(0, index);
			        return (s);
		      }
		    });
		    
		    // artifact 3
		    col = createTableViewerColumn(titles[4], bounds[4], 4);
		    col.setLabelProvider(new ColumnLabelProvider() {
		      @Override
		      public String getText(Object element) {
		    	  String s = (String) element;
			        int index= s.indexOf(",");
			        if (index != -1) s= s.substring(index+1, s.length());
			        index= s.indexOf(",");
			        if (index != -1) s= s.substring(index+1, s.length());
			        index= s.indexOf(",");
			        if (index != -1) s= s.substring(index+1, s.length());
			        index= s.indexOf(",");
			        if (index != -1) s= s.substring(index+1, s.length());
			        index= s.indexOf(",");
			        if (index != -1) s= s.substring(0, index);
			        return (s);
		      }
		    });


		  }

	  private TableViewerColumn createTableViewerColumn(String title, int bound, final int colNumber) {
		    final TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
		    final TableColumn column = viewerColumn.getColumn();
		    column.setText(title);
		    column.setWidth(bound);
		    column.setResizable(true);
		    column.setMoveable(true);
		    return viewerColumn;
		  }
}


