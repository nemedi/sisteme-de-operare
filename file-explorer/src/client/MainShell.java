package client;

import java.io.File;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Tree;

public class MainShell extends Shell {
	private Table table;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			MainShell shell = new MainShell(display);
			shell.open();
			shell.layout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the shell.
	 * @param display
	 */
	public MainShell(Display display) {
		super(display, SWT.SHELL_TRIM);
		setLayout(new GridLayout(1, false));
		
		SashForm sashForm = new SashForm(this, SWT.NONE);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		sashForm.setLocation(0, 0);
		
		TreeViewer treeViewer = new TreeViewer(sashForm, SWT.BORDER);
		treeViewer.setContentProvider(new ITreeContentProvider() {
			
			
			@Override
			public boolean hasChildren(Object item) {
				File[] files = ((File) item)
						.listFiles(f -> f.isDirectory());
				return files != null && files.length > 0;
			}
			
			@Override
			public Object getParent(Object item) {
				return ((File) item).getParentFile();
			}
			
			@Override
			public Object[] getElements(Object input) {
				return ((File[]) input);
			}
			
			@Override
			public Object[] getChildren(Object item) {
				File[] folders = ((File) item)
						.listFiles(f -> f.isDirectory());
				return folders;
			}
			
		});
		treeViewer.setLabelProvider(new LabelProvider() {
			
			@Override
			public String getText(Object item) {
				File file = (File) item;
				if (!file.getName().isEmpty()) {
					return file.getName();
				} else {	
					return file.getPath();
				}
			}
			
		});
		treeViewer.setInput(File.listRoots());
		Tree tree = treeViewer.getTree();
		
		TableViewer tableViewer = new TableViewer(sashForm, SWT.BORDER | SWT.FULL_SELECTION);
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableViewerColumn nameTableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		nameTableViewerColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((File) element).getName();
			}
		});
		TableColumn tblclmnName = nameTableViewerColumn.getColumn();
		tblclmnName.setWidth(275);
		tblclmnName.setText("Name");
		
		TableViewerColumn sizeTableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		sizeTableViewerColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return String.valueOf(Math.round(((File) element).length() / 1024));
			}
		});
		TableColumn tblclmnSize = sizeTableViewerColumn.getColumn();
		tblclmnSize.setAlignment(SWT.RIGHT);
		tblclmnSize.setWidth(100);
		tblclmnSize.setText("Size");
		sashForm.setWeights(new int[] {1, 3});
		
		tree.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				tableViewer.setInput(((File) e.item.getData()).listFiles(f -> f.isFile()));
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		createContents();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("File Explorer");
		setSize(656, 300);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
