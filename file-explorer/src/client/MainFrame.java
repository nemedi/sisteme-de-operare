package client;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

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

		private void setChildren(List<ExpandableTreeNode> children) {
		}

		public void loadChildren() {
			if (children != null) {
				return;
			}
			File[] folders = file == null ? File.listRoots() : file.listFiles(f -> f.isDirectory());
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
	}

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;

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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(1, 0, 0, 0));

		JSplitPane splitPane = new JSplitPane();
		contentPane.add(splitPane);

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
		tree.setRootVisible(false);
		splitPane.setLeftComponent(tree);

		table = new JTable();
		splitPane.setRightComponent(table);

		tree.setModel(model);
		rootNode.loadChildren();
		tree.expandPath(new TreePath(rootNode.getPath()));
	}

}
