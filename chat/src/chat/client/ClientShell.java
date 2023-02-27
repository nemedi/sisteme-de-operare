package chat.client;

import java.io.IOException;
import java.net.UnknownHostException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.ibm.icu.text.MessageFormat;

import chat.common.Settings;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;

public class ClientShell extends Shell {
	
	private Client client;
	private Text txtName;
	private Text txtMessage;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			ClientShell shell = new ClientShell(display);
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
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public ClientShell(Display display) throws UnknownHostException, IOException {
		super(display, SWT.SHELL_TRIM);
		addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {
				try {
					client.close();
				} catch (Exception e1) {
					MessageDialog.openError(ClientShell.this, "Error", e1.getLocalizedMessage());
				}
			}
		});
		setLayout(new GridLayout(3, false));
		
		Label lblName = new Label(this, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText("Name");
		
		txtName = new Text(this, SWT.BORDER);
		txtName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnLogin = new Button(this, SWT.NONE);
		btnLogin.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					if ("Login".equals(btnLogin.getText())) {
						if (txtName.getText().trim().length() > 0) {
							client.login(txtName.getText().trim());
						}
					} else {
						client.logout();
					}
				} catch (IOException e1) {
					MessageDialog.openError(ClientShell.this, "Error", e1.getLocalizedMessage());
				}
			}
		});
		btnLogin.setBounds(0, 0, 96, 27);
		btnLogin.setText("Login");
		
		SashForm sashForm = new SashForm(this, SWT.NONE);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		
		List lstUsers = new List(sashForm, SWT.BORDER);
		lstUsers.setEnabled(false);
		lstUsers.setBounds(0, 0, 3, 66);
		
		List lstMessages = new List(sashForm, SWT.BORDER);
		lstMessages.setEnabled(false);
		sashForm.setWeights(new int[] {1, 3});
		
		Label lblMessage = new Label(this, SWT.NONE);
		lblMessage.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblMessage.setText("Message");
		
		txtMessage = new Text(this, SWT.BORDER);
		txtMessage.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					if (e.keyCode == SWT.CR && txtMessage.getText().trim().length() > 0) {
						client.send(lstUsers.getSelection()[0], txtMessage.getText().trim());
						txtMessage.setText("");
					}
				} catch (IOException e1) {
					MessageDialog.openError(ClientShell.this, "Error", e1.getLocalizedMessage());
				}
			}
		});
		txtMessage.setEnabled(false);
		txtMessage.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		createContents();
		ClientCallback callback = new ClientCallback() {
			
			@Override
			public void onRemoveUser(String name) {
				getDisplay().asyncExec(() -> lstUsers.remove(name));				
			}
			
			@Override
			public void onReceive(String from, String text) {
				getDisplay().asyncExec(() -> lstMessages.add(MessageFormat.format("{0}: {1}", from, text)));
			}
			
			@Override
			public void onExit() {
				getDisplay().asyncExec(() -> {
					txtName.setEnabled(true);
					lstUsers.setEnabled(false);
					lstUsers.removeAll();
					lstMessages.setEnabled(false);
					lstMessages.removeAll();
					txtMessage.setEnabled(false);
					txtName.setFocus();
					btnLogin.setText("Login");
				});
			}
			
			@Override
			public void onDeny() {
				getDisplay().asyncExec(() -> MessageDialog.openWarning(ClientShell.this, "Warning", "User already exists."));
			}
			
			@Override
			public void onAddUser(String name) {
				getDisplay().asyncExec(() -> lstUsers.add(name));
			}
			
			@Override
			public void onAccept(String[] names) {
				getDisplay().asyncExec(() -> {
					txtName.setEnabled(false);
					lstUsers.setEnabled(true);
					lstUsers.setItems(names);
					lstUsers.setSelection(0);
					lstMessages.setEnabled(true);
					txtMessage.setEnabled(true);
					txtMessage.setFocus();
					btnLogin.setText("Logout");
				});
			}
		};
		client = new Client(Settings.HOST, Settings.PORT, callback);
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("Chat");
		setSize(800, 600);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
