package com.example.api.sequence;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OracleSequenceDelegate extends JdbcSequenceDelegate {

	private PreparedStatement statement;

	@Override
	protected void doInitialize(Connection connection) throws SQLException {
		String sql = createSql();
		this.statement = connection.prepareStatement(sql);
	}

	protected String createSql() {
		String seqName = getConfig("sequenceName");
		return String.format("select %s.nextval from DUAL", seqName);
	}

	@Override
	public long nextLong() throws IOException {
		try (ResultSet rs = executeQuery()) {
			if (rs.next()) {
				return rs.getLong(1);
			}
			throw new IOException("Illegal ResultSet");
		} catch (SQLException e) {
			throw new IOException(e);
		}
	}

	@Override
	public BigDecimal nextDecimal() throws IOException {
		try (ResultSet rs = executeQuery()) {
			if (rs.next()) {
				return rs.getBigDecimal(1);
			}
			throw new IOException("Illegal ResultSet");
		} catch (SQLException e) {
			throw new IOException(e);
		}
	}

	private ResultSet executeQuery() throws SQLException {
		if (statement == null) {
			throw new IllegalStateException("statement already closed");
		}
		return statement.executeQuery();
	}

	@Override
	public void close() throws IOException {
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				throw new IOException(e);
			} finally {
				this.statement = null;
			}
		}
	}
}
