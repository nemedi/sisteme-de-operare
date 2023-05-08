package notes;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.RepaintManager;
import javax.swing.UIManager;

public class NotePrinter implements Printable, ActionListener {

	private JFrame frame;
    private JComponent component;

    public int print(Graphics graphics, PageFormat pageFormat, int page) throws PrinterException {
        if (page > 0) {
            return NO_SUCH_PAGE;
        }
        ((Graphics2D) graphics).setClip(0, 0, (int)pageFormat.getImageableWidth(), (int)pageFormat.getImageableHeight());
        ((Graphics2D) graphics).translate((int)pageFormat.getImageableX(), (int)pageFormat.getImageableY());
        RepaintManager repaintManager = RepaintManager.currentManager(component);
        boolean doubleBuffer = repaintManager.isDoubleBufferingEnabled();
        repaintManager.setDoubleBufferingEnabled(false);
        component.setSize((int)pageFormat.getImageableWidth(), (int)pageFormat.getImageableHeight());
        component.printAll(graphics);
        repaintManager.setDoubleBufferingEnabled(doubleBuffer);
        return PAGE_EXISTS;
    }

    public void actionPerformed(ActionEvent e) {
         PrinterJob job = PrinterJob.getPrinterJob();
         job.setPrintable(this);
         if (job.printDialog()) {
             try {
                  job.print();
                  frame.dispose();
             } catch (PrinterException ex) {
             }
         }
    }

    public NotePrinter(JFrame frame, JComponent component) {
    	this.frame = frame;
    	this.component = component;
    }

    public static void print(DefaultListModel<NoteModel> notes, JFrame parent) {
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        JFrame frame = new JFrame("Print Notes");
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWeights = new double[]{1, 0};
        gridBagLayout.columnWidths = new int[]{0, 0};
        gridBagLayout.rowHeights = new int[]{0, 0, 0};
        gridBagLayout.rowWeights = new double[]{1, 0, 0};
        frame.setLayout(gridBagLayout);
        String html = "<html>" +
                "<body>" +
                "<table border=\"0\">" +
                "<tr>" +
                "<th align=\"left\"><b>Title</b></th>" +
                "<th align=\"left\"><b>Content</b></th>" +
                "</tr>";
        for (int i = 1; i < notes.getSize(); i++) {
        	NoteModel note = notes.get(i);
        	html += String.format("<tr>"
        			+ "<td align=\"left\" valign=\"top\" nowrap>%s</td>"
        			+ "<td align=\"left\" valign=\"top\">%s</td>"
        			+ "</tr>",
        			note.getTitle(),
        			note.getContent());
        }
        html += "</table>"
                + "</body>"
                + "</html>";
        JLabel label = new JLabel(html);
        label.setHorizontalAlignment(JLabel.LEFT);
        label.setHorizontalTextPosition(JLabel.LEFT);
        label.setVerticalAlignment(JLabel.TOP);
        label.setVerticalTextPosition(JLabel.TOP);
        JScrollPane pane = new JScrollPane(label);
        pane.setPreferredSize(parent.getSize());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        frame.add(pane, gridBagConstraints);
        JButton button = new JButton("Print");
        button.addActionListener(new NotePrinter(frame, label));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        frame.add(button, gridBagConstraints);
        frame.pack();
        frame.setVisible(true);
        frame.setLocation(parent.getLocation());
    }
}