package a00904362.data.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import a00904362.data.db.Database;

public abstract class Dao {
	private static final Logger LOG = LogManager.getLogger(Dao.class);
	protected final Database _database;
	protected final String _tableName;

	protected Dao(Database database, String tableName) {
		LOG.info("Starting " + getClass().toString());
		_database = database;
		_tableName = tableName;
	}

	public abstract void create() throws SQLException;

	protected void create(String createStatement) throws SQLException {
		Statement statement = null;
		try {
			Connection connection = _database.getConnection();
			statement = connection.createStatement();
			statement.executeUpdate(createStatement);
			LOG.info("Table " + _tableName + " created ");

		} finally {
			close(statement);
		}
	}

	protected void add(String updateStatement) throws SQLException {
		Statement statement = null;
		try {
			Connection connection = _database.getConnection();
			statement = connection.createStatement();
			statement.executeUpdate(updateStatement);
		} finally {
			close(statement);
		}
	}

	public void drop() throws SQLException {
		Statement statement = null;
		try {
			Connection connection = _database.getConnection();
			statement = connection.createStatement();
			if (_database.tableExists(_tableName)) {
				statement.executeUpdate("drop table " + _tableName);
			}
		} finally {
			close(statement);
		}
	}

	public int countAll(Connection conn) throws SQLException {

		String sql = "SELECT count(*) FROM " + _tableName;
		PreparedStatement stmt = null;
		ResultSet result = null;
		int allRows = 0;

		try {
			stmt = conn.prepareStatement(sql);
			result = stmt.executeQuery();

			if (result.next())
				allRows = result.getInt(1);
		} finally {
			if (result != null)
				result.close();
			if (stmt != null)
				stmt.close();
		}
		return allRows;
	}

	protected int databaseUpdate(Connection conn, PreparedStatement stmt) throws SQLException {

		int result = stmt.executeUpdate();

		return result;
	}

	protected void close(Statement statement) {
		try {
			if (statement != null) {
				statement.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
