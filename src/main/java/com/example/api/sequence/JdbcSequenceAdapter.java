package com.example.api.sequence;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

// スレッドごとに1インスタンス作られる想定、つまりマルチスレッドで同時に呼ばれることは無い
public class JdbcSequenceAdapter implements Closeable {

	private final Map<String, Connection> connectionlMap = new HashMap<>();

	public Connection getConnection(String name, SequenceAdapter adapter) throws SQLException {
		String url = adapter.getConfig(name, "url");
		String user = adapter.getConfig(name, "user");
		String key = url + " " + user;
		Connection connection = connectionlMap.get(key);
		if (connection == null) {
			String password = adapter.getConfig(name, "password");
			connection = DriverManager.getConnection(url, user, password);
			if (connection == null) {
				throw new AssertionError(key);
			}
			connectionlMap.put(key, connection);
		}
		return connection;
	}

	@Override
	public void close() throws IOException {
		IOException exception = null;

		for (Connection connection : connectionlMap.values()) {
			try {
				connection.close();
			} catch (SQLException e) {
				if (exception == null) {
					exception = new IOException(e);
				} else {
					exception.addSuppressed(e);
				}
			}
		}
		connectionlMap.clear();

		if (exception != null) {
			throw exception;
		}
	}
}
