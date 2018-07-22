package com.example.api.sequence;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class JdbcSequenceDelegate extends SequenceDelegate {

	@Override
	protected void doInitialize(SequenceAdapter adapter) throws IOException {
		// スレッドに唯一のJdbcSequenceAdapterを取得する
		JdbcSequenceAdapter jdbcAdapter = adapter.getInstance(JdbcSequenceAdapter.class,
				() -> new JdbcSequenceAdapter());

		try {
			Connection connection = jdbcAdapter.getConnection(getName(), adapter);
			doInitialize(connection);
		} catch (SQLException e) {
			throw new IOException(e);
		}
	}

	protected abstract void doInitialize(Connection connection) throws SQLException;
}
