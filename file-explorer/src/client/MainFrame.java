package client;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.ListSelectionModel;
import javax.swing.JScrollPane;

public class MainFrame extends JFrame {

	private class ExpandableTreeNode extends DefaultMutableTreeNode {

		private static final long serialVersionUID = 1L;
		private File file;
		private List<ExpandableTreeNode> children;

		public ExpandableTreeNode(File file) {
			this.file = file;
			String name = file != null ? file.getName() : "Root";
			if (file != null && name.isBlank()) {
				name = file.getPath();
			}
			setUserObject(name);
		}

		public void loadChildren() {
			if (children != null) {
				return;
			}
			File[] folders = file == null
					? File.listRoots()
					: file.listFiles(f -> f.isDirectory());
			if (folders == null) {
				folders = new File[] {};
			}
			children = Arrays.stream(folders)
					.map(folder -> new ExpandableTreeNode(folder))
					.collect(Collectors.toList());
			removeAllChildren();
			setAllowsChildren(children.size() > 0);
			for (MutableTreeNode node : children) {
				add(node);
			}
		}

		public List<ExpandableTreeNode> getChildren() {
			return children;
		}
		
		public File[] getFiles() {
			File[] files = file.listFiles(f -> f.isFile());
			return files != null ? files : new File[] {};
		}
	}

	private static final long serialVersionUID = 1L;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setTitle("File Explorer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(panel);
		panel.setLayout(new GridLayout(1, 0, 0, 0));

		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.25);
		panel.add(splitPane);

		JTree tree = new JTree();
		tree.setShowsRootHandles(true);
		ExpandableTreeNode rootNode = new ExpandableTreeNode(null);
		DefaultTreeModel model = new DefaultTreeModel(rootNode);
		tree.addTreeWillExpandListener(new TreeWillExpandListener() {
			@Override
			public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
				TreePath path = event.getPath();
				if (path.getLastPathComponent() instanceof ExpandableTreeNode) {
					
					ExpandableTreeNode node = (ExpandableTreeNode) path.getLastPathComponent();
					node.getChildren().forEach(child -> child.loadChildren());
				}
			}

			@Override
			public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
			}
		});
		
		JScrollPane leftScrollPane = new JScrollPane();
		leftScrollPane.setViewportView(tree);
		splitPane.setLeftComponent(leftScrollPane);

		final JTable table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		loadTable(table, new Object[0][0]);
		JScrollPane rightScrollPane = new JScrollPane();
		rightScrollPane.setViewportView(table);
		splitPane.setRightComponent(rightScrollPane);
		

		tree.addTreeSelectionListener(new TreeSelectionListener() {
			
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				table.setModel(new DefaultTableModel(new Object[0][0], new String[] {"Name", "Size"}));
				if (tree.getLastSelectedPathComponent() instanceof ExpandableTreeNode) {
					File[] files = ((ExpandableTreeNode) tree.getLastSelectedPathComponent()).getFiles();
					Object[][] content = new Object[files.length][];
					for (int i = 0; i < files.length; i++) {
						content[i] = new Object[] {files[i].getName(), files[i].length()};
					}
					loadTable(table, content);
				}
			}
		});
		tree.setRootVisible(false);
		tree.setModel(model);
		
		
		rootNode.loadChildren();
		tree.expandPath(new TreePath(rootNode.getPath()));
	}
	
	private void loadTable(JTable table, Object[][] content) {
		table.setModel(new DefaultTableModel(content, new String[] {"Name", "Size"}));
		DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
		leftRenderer.setHorizontalAlignment(JLabel.LEFT);
		table.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);
		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
		table.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
	}

}
