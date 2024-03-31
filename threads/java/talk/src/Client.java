import java.awt.EventQueue;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ResourceBundle;
import java.util.Scanner;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Client extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;

	public Client(Socket socket, BufferedReader reader, PrintWriter writer) {
		final DefaultListModel<String> messages = new DefaultListModel<String>();
		setTitle("Talk");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{424, 0};
		gbl_contentPane.rowHeights = new int[] {0};
		gbl_contentPane.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{1.0, 0.0};
		contentPane.setLayout(gbl_contentPane);
		JList<String> list = new JList<String>(messages);
		GridBagConstraints gbc_list = new GridBagConstraints();
		gbc_list.fill = GridBagConstraints.BOTH;
		gbc_list.insets = new Insets(0, 0, 5, 0);
		gbc_list.gridx = 0;
		gbc_list.gridy = 0;
		contentPane.add(list, gbc_list);
		textField = new JTextField();
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER && !textField.getText().isBlank()) {
					writer.print(textField.getText().trim() + "\r\n");
					writer.flush();
					textField.setText("");
				}
			}
		});
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 0;
		gbc_textField.gridy = 1;
		contentPane.add(textField, gbc_textField);
		textField.setColumns(10);
		new Thread(() -> {
			while (!socket.isClosed()) {
				try {
					reader.lines().forEach(l -> messages.addElement(l));
					Thread.sleep(100);
				} catch (InterruptedException e) {
					break;
				}
			}
		}).start();
	}

	public static void main(String[] args) {
		try {
			ResourceBundle bundle = ResourceBundle.getBundle("settings");
			String host = args.length == 2
					? args[0] : bundle.getString("host");
			int port = Integer.parseInt(args.length == 2
					? args[1] : bundle.getString("port"));
			final Socket socket = new Socket(host, port);
			final BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			final PrintWriter writer = new PrintWriter(socket.getOutputStream());
			System.out.println(String.format("Client is connected to %s on port %d, enter message or 'exit' to close it.", host, port));
			if (GraphicsEnvironment.isHeadless()) {
				new Thread(() -> {
					while (!socket.isClosed()) {
						reader.lines().forEach(l -> System.out.print(l + "\r\n"));
					}
				}).start();
				try (Scanner scanner = new Scanner(System.in)) {
					while (true) {
						String message = scanner.nextLine();
						if ("exit".equals(message)) {
							System.exit(0);
						} else if (!socket.isClosed()) {
							writer.print(message + "\r\n");
							writer.flush();
						}
					}
				}
			} else {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							Client frame = new Client(socket, reader, writer);
							frame.setVisible(true);
						} catch (Exception e) {
						}
					}
				});
			}
		} catch (NumberFormatException | IOException e) {
		}
	}

}
