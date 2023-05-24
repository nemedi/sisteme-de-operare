package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.staticfiles.Location;

public class Program {

	private static final String LOGIN_PAGE = "/login.html";
	private static final String ERROR_PAGE = "/error.html";
	private static final String MAIN_PAGE = "/main.html";
	private static final List<String> SKIP_CHECK = Arrays.asList(LOGIN_PAGE, ERROR_PAGE);
	private static final String LOGIN = "/login";
	private static final String LOGOUT = "/logout";
	private static final String SESSION = "session";
	private static final String USER = "user";
	private static final String PASSWORD = "password";
	private static final int PORT;
	private static final String URL;
	private static final boolean SAFE;
	private static final List<String> TOKENS;
	
	static {
		ResourceBundle bundle = ResourceBundle.getBundle("settings");
		PORT = Integer.parseInt(bundle.getString("port"));
		URL = bundle.getString("url");
		SAFE = Boolean.parseBoolean(bundle.getString("safe"));
		TOKENS = Collections.synchronizedList(new ArrayList<String>());
	}
	
	public static void main(String[] args) {
		Javalin.create(configuration -> 
				configuration.staticFiles.add("web", Location.EXTERNAL)
			)
			.before(context -> {
				var path = context.path();
				if (LOGIN.equals(path)) {
					handleLogin(context);
				} else if (LOGOUT.equals(path)) {
					handleLogout(context);
				} else if (!SKIP_CHECK.contains(path)) {
					handleCheck(context);
				}
			})
	        .start(PORT);
	}

	private static void handleLogin(Context context) throws SQLException {
		String user = context.formParam(USER);
		String password = context.formParam(PASSWORD);
		if (user != null && password != null) {
			try (Connection connection = DriverManager.getConnection(URL)) {
				try (ResultSet resultSet = SAFE
						? getResultSetSafe(connection, user, password)
						: getResultSetUnsafe(connection, user, password)) {
					if (resultSet.next() && resultSet.getInt(1) == 1) {
						String token = UUID.randomUUID().toString();
						TOKENS.add(token);
						context.cookie(SESSION, token + ":" + user);
						context.redirect(MAIN_PAGE);
					} else {
						context.redirect(ERROR_PAGE);
					}
				}
			}
		} else {
			context.redirect(LOGIN_PAGE);
		}
	}

	private static void handleLogout(Context context) {
		String session = context.cookie(SESSION);
		if (session != null) {
			context.removeCookie(SESSION);
			String token = session.substring(0, session.indexOf(":"));
			TOKENS.remove(token);
		}
		context.redirect(LOGIN_PAGE);
	}

	private static void handleCheck(Context context) {
		String session = context.cookie(SESSION);
		if (session != null) {
			String token = session.substring(0, session.indexOf(":"));
			if (!TOKENS.contains(token)) {
				context.redirect(LOGIN_PAGE);
			}
		} else {
			context.redirect(LOGIN_PAGE);
		}
	}

	private static ResultSet getResultSetUnsafe(Connection connection,
			String user, String password) throws SQLException {
		Statement statement = connection.createStatement();
		String query = String.format(
				"SELECT count(*) FROM users where name = '%s' and password = '%s'",
				user, password);
		return statement.executeQuery(query);
	}
	
	private static ResultSet getResultSetSafe(Connection connection,
			String user, String password) throws SQLException {
		String query = "SELECT count(*) FROM users where name = ? and password = ?";
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setString(1, user);
		statement.setString(2, password);
		return statement.executeQuery(query);
	}

}
