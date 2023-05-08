package notes;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class NoteFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	public static final String NEW_NOTE = "<New note>";
    
	@SuppressWarnings("unchecked")
	public NoteFrame(NoteContract proxy) {
        setTitle("Notes");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 700, 400);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5,5,5,5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);
        JSplitPane splitPane = new JSplitPane();
        splitPane.setResizeWeight(0.3);
        contentPane.add(splitPane, BorderLayout.CENTER);
        final DefaultListModel<NoteModel> notes = new DefaultListModel<NoteModel>();
        JList<NoteModel> list = new JList<>(notes);
        splitPane.setLeftComponent(list);
        JPanel panel = new JPanel();
        splitPane.setRightComponent(panel);
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWeights = new double[]{1, 0};
        gridBagLayout.columnWidths = new int[]{0, 0};
        gridBagLayout.rowHeights = new int[]{0, 0, 0};
        gridBagLayout.rowWeights = new double[]{1, 0, 0};
        panel.setLayout(gridBagLayout);
        final NotePanel notePanel = new NotePanel();
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(0, 0, 5, 0);
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 0 ;
        gridBagConstraints.gridy =0;
        panel.add(notePanel, gridBagConstraints);
        notePanel.setVisible(false);
        JPanel buttonsPanel  = new JPanel();
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 0 ;
        gridBagConstraints.gridy = 1;
        panel.add(buttonsPanel, gridBagConstraints);
        JButton createButton = new JButton("Create");
        createButton.addActionListener(event -> {
            NoteModel note = notePanel.getNote();
            if (!note.getTitle().isEmpty() && !note.getContent().isEmpty()) {
                try {
                    note = proxy.createNote(note.getTitle(), note.getContent());
                    notes.addElement(note);
                    notePanel.clear();
                    list.setSelectedIndex(notes.size()-1);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(NoteFrame.this, e.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        buttonsPanel.add(createButton);
        createButton.setVisible(false);
        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(event -> {
            NoteModel note = notePanel.getNote();
            if (!note.getTitle().isEmpty() && !note.getContent().isEmpty()) {
                try {
                    if(proxy.updateNote(note)){
                        notes.set(list.getSelectedIndex(), note);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(NoteFrame.this, e.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        buttonsPanel.add(updateButton);
        updateButton.setVisible(false);
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(event -> {
            try {
                if (proxy.deleteNote(notePanel.getNote().getId())) {
                    int index = list.getSelectedIndex();
                    list.setSelectedIndex(0);
                    notes.remove(index);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(NoteFrame.this, e.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        buttonsPanel.add(deleteButton);
        deleteButton.setVisible(false);
        JButton printButton = new JButton("Print");
        printButton.addActionListener(event -> {
            try {
                NotePrinter.print(notes, NoteFrame.this);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(NoteFrame.this, e.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        buttonsPanel.add(printButton);
        printButton.setVisible(false);
        notes.addElement(new NoteModel().title(NEW_NOTE));
        list.addListSelectionListener(event -> {
        	notePanel.setVisible(true);
            NoteModel note = ((JList<NoteModel>) event.getSource()).getSelectedValue();
            notePanel.setNote(note);
            createButton.setVisible(NEW_NOTE.equals(note.getTitle()));
            updateButton.setVisible(!NEW_NOTE.equals(note.getTitle()));
            deleteButton.setVisible(!NEW_NOTE.equals(note.getTitle()));
            printButton.setVisible(notes.getSize() > 1);
        });
        try {
            proxy.readNotes().forEach(note -> notes.addElement(note));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(NoteFrame.this, e.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((int) ((dimension.getWidth() - getWidth()) / 2),
        		(int) ((dimension.getHeight() - getHeight()) / 2));
    }
	
	public static void centerFrameInScreen(JFrame frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation((int) ((dimension.getWidth() - frame.getWidth()) / 2),
        		(int) ((dimension.getHeight() - frame.getHeight()) / 2));
	}
}
