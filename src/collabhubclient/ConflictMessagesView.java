package collabhubclient;


import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import java.awt.Color;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.part.ViewPart;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
  

public class ConflictMessagesView extends ViewPart{

	
	private final Image RED_IMAGE = getImage("redSmall.jpg");
	private final Image ORANGE_IMAGE = getImage("orangeSmall.jpg");
	
	boolean DEBUG= true;
	 public static TableViewer viewer;
	 Display display= null;
	public ConflictMessagesView ()
	{
		super();
	}
	
	private static Image getImage(String file) {

	    // assume that the current class is called View.java
	  Bundle bundle = FrameworkUtil.getBundle(ConflictMessagesView.class);
	  Path imgPath= new Path("images/" + file);
	  URL url = FileLocator.find(bundle,imgPath, null);
	  System.out.println("Path for Image::::::::::::::::::::::::::"+imgPath);
	  System.out.println("url for Image::::::::::::::::::::::::::"+url);
	 
	  ImageDescriptor image = ImageDescriptor.createFromURL(url);
	  return image.createImage();

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
		 
		display= table.getDisplay();
		
		 viewer.setInput(ConflictModelProvider.INSTANCE.getConflictMessages());
		 
		display.getActiveShell();
		createColumns(parent,viewer);

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		  viewer.getControl().setFocus();
		// viewer.getControl().setBackground(display.getSystemColor(SWT.COLOR_YELLOW));
	
		  if (viewer!=null) viewer.refresh();
	}
	
	 public TableViewer getViewer() {
		    return viewer;
		  }
	 	 
	 private void createColumns(final Composite parent, final TableViewer viewer) {
		    String[] titles = { "Severity", "Type of  Collaborator","Conflict Message"};
		    int[] bounds = { 100, 150, 800 };

		    // first column is for the Name of the Collaborator
		    TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0], 0);
		    col.setLabelProvider(new ColumnLabelProvider() {
		      @Override
		      public String getText(Object element) {
		        return null;
		      }
		      
		      public Image getImage(Object element) {
			        String s = (String) element;
			       
			        int index1= s.indexOf("#");
			        int index2= s.indexOf("|");
			        System.out.println("STRING INDEX::: "+index1);
			        if (index1 != -1 && index2 != -1) s= s.substring(index1, index2);
			        System.out.println("Returining Image::: "+s);
		    	  if (s.contains("EDC")) {
		    	    return RED_IMAGE;
		    	  } 
		    	  return ORANGE_IMAGE;
		    	  }
		    	
		    });

		    col = createTableViewerColumn(titles[1], bounds[1], 1);
		    col.setLabelProvider(new ColumnLabelProvider() {
		      @Override
		      public String getText(Object element) {
		        String s = (String) element;
		        System.out.println("STRING::: "+s);
		        int index1= s.indexOf("#");
		        int index2= s.indexOf("|");
		        System.out.println("STRING INDEX::: "+index1);
		        if (index1 != -1 && index2 != -1) s= s.substring(index1, index2);
		        return s;
		      }
		    });
		    // second column
		    col = createTableViewerColumn(titles[2], bounds[2], 2);
		    col.setLabelProvider(new ColumnLabelProvider() {
		      @Override
		      public String getText(Object element) {
		    	  String s = (String) element;
			        int index= s.indexOf("|");
			        if (index != -1) s= s.substring(index+1, s.length());			        
			        return s;
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




