package collabhubclient;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.widgets.Listener;

public class DirectCollaboratorsViewPart extends ViewPart {

	boolean DEBUG = true;
	public static TableViewer viewer;

	public DirectCollaboratorsViewPart() {
		super();
	}

	@Override
	public void createPartControl(Composite parent) {

		viewer = new TableViewer(parent, SWT.SINGLE | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);

		final Table table = viewer.getTable();

		// Table table = new Table(parent, SWT.SINGLE);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		viewer.setContentProvider(new ArrayContentProvider());

		getSite().setSelectionProvider(viewer);

		Display display = table.getDisplay();

		viewer.setInput(DirectCollaboratorModelProvider.INSTANCE
				.getCollaborators());

		display.getActiveShell();

		createColumns(parent, viewer);

		table.addListener(SWT.DefaultSelection, new Listener() {
			public void handleEvent(Event e) {
				String string = "";
				TableItem[] selection = table.getSelection();
				for (int i = 0; i < selection.length; i++)
					string += selection[i] + " ";
				System.out.println("DefaultSelection={" + string + "}");
			}
		});
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		viewer.getControl().setFocus();
		if (viewer != null)
			viewer.refresh();
	}

	public TableViewer getViewer() {
		return viewer;
	}

	private void createColumns(final Composite parent, final TableViewer viewer) {
		String[] titles = { "Name of the Collaborator", "Current AST Element",
				"Current Line No." };
		int[] bounds = { 200, 200, 100 };

		// first column is for the Name of the Collaborator
		TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0], 0);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				String s = (String) element;
				System.out.println("STRING::: " + s);
				int index = s.indexOf(",");
				System.out.println("STRING INDEX::: " + index);
				if (index != -1)
					s = s.substring(0, index);
				return s;
			}
		});

		// second column is for the Current AST Element
		col = createTableViewerColumn(titles[1], bounds[1], 1);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				String s = (String) element;
				if (s.contains("null")) {
					return "---";
					// return "Null";
				}
				if (s.contains("Null")) {
					return "---";
					// return "Null";
				}
				if (s.contains("No Collaborators")) {
					return "No Collaborators";
				}

				{
					System.out
							.println("In AST Element::::::::::::::::::::::::::"
									+ s);
					String type = null;
					int index = s.indexOf(",");
					if (index != -1)
						s = s.substring(index + 1, s.length());

					// index= s.indexOf(",");
					// if (index != -1) s= s.substring(index+1, s.length());

					int index2 = s.indexOf(",");
					if ((index2 != -1))
						s = s.substring(0, index2);

					type = s.substring(0, 1);
					s = s.substring(1, s.length());

					if (type.contains("M"))
						s = "Method::" + s;

					if (type.contains("F"))
						s = "Field::" + s;

					if (type.contains("I"))
						s = "ImportDec::" + s;

					if (type.contains("P"))
						s = "PackageDec::" + s;

					if (type.contains("L"))
						s = "LocalVar::" + s;
					return s;

				}

			}
		});

		// Current Line No.
		col = createTableViewerColumn(titles[2], bounds[2], 2);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				String s = (String) element;
				int index = s.indexOf(",");
				if (index != -1)
					s = s.substring(index + 1, s.length());
				index = s.indexOf(",");
				if (index != -1)
					s = s.substring(index + 1, s.length());
				index = s.indexOf(",");
				if (index != -1)
					s = s.substring(0, index);
				return ("Line No:: " + s);
			}
		});
		if (viewer != null)
			getViewer().refresh();
	}

	private TableViewerColumn createTableViewerColumn(String title, int bound,
			final int colNumber) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(viewer,
				SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(true);
		return viewerColumn;
	}
}
