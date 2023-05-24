package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

public class Program {

	public static void main(String[] args) {
		final List<String> tokens = Collections.synchronizedList(new ArrayList<String>());
		final String url = ResourceBundle.getBundle("settings").getString("url");
		Javalin.create(configuration -> 
				configuration.staticFiles.add("web", Location.EXTERNAL)
			)
			.before(context -> {
				var path = context.path();
				if ("/login".equals(path)) {
					String user = context.formParam("user");
					String password = context.formParam("password");
					if (user != null && password != null) {
						try (Connection connection = DriverManager.getConnection(url)) {
							Statement statement = connection.createStatement();
							String query = String.format("SELECT count(*) FROM users where name = '%s' and password = '%s'",
									user, password);
							try (ResultSet resultSet = statement.executeQuery(query)) {
								if (resultSet.next() && resultSet.getInt(1) == 1) {
									String token = UUID.randomUUID().toString();
									tokens.add(token);
									context.cookie("session", token + ":" + user);
									context.redirect("/main.html");
								} else {
									context.redirect("/error.html");
								}
							}
						}
					} else {
						context.redirect("/login.html");
					}
				} else if ("/logout".equals(path)) {
					String session = context.cookie("session");
					if (session != null) {
						context.removeCookie("session");
						String token = session.substring(0, session.indexOf(":"));
						tokens.remove(token);
					}
	            	context.redirect("/login.html");
				} else if (!"/login.html".equals(path)) {
					String session = context.cookie("session");
					if (session != null) {
						String token = session.substring(0, session.indexOf(":"));
						if (!tokens.contains(token)) {
							context.redirect("/login.html");
						}
					} else {
						context.redirect("/login.html");
					}
				}
			})
            .start(8080);
	}

}
