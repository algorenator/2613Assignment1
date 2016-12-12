/**
 * Project: examples8
 * File: Database.java
 * Date: 2012-10-28
 * Time: 12:26:04 PM
 */

package a00904362.data.db;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @author scirka
 * 
 */
public class Database {

	private static final String DB_DRIVER_KEY = "db.driver";
	private static final String DB_URL_KEY = "db.url";
	private static final String DB_USER_KEY = "db.user";
	private static final String DB_PASSWORD_KEY = "db.password";

	private static Logger LOG = Logger.getLogger(Database.class.getName());

	private static Connection connection;
	private final Properties _properties;

	public Database(Properties properties) throws FileNotFoundException, IOException {
		LOG.debug("Loading database properties from db.properties");
		_properties = properties;
	}

	public Connection getConnection() throws SQLException {
		if (connection != null) {
			return connection;
		}

		try {
			connect();
		} catch (ClassNotFoundException e) {
			throw new SQLException(e);
		}

		return connection;
	}

	private void connect() throws ClassNotFoundException, SQLException {
		Class.forName(_properties.getProperty(DB_DRIVER_KEY));
		LOG.debug("Driver loaded ");
		connection = DriverManager.getConnection(_properties.getProperty(DB_URL_KEY),
				_properties.getProperty(DB_USER_KEY), _properties.getProperty(DB_PASSWORD_KEY));
		LOG.debug("Database connected " + connection.getMetaData().getDatabaseProductName());
	}

	public void shutdown() {
		if (connection != null) {
			try {
				connection.close();
				connection = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean tableExists(String tableName) throws SQLException {
		DatabaseMetaData databaseMetaData = connection.getMetaData();
		ResultSet resultSet = null;
		String rsTableName = null;

		try {
			resultSet = databaseMetaData.getTables(connection.getCatalog(), "%", "%", null);
			while (resultSet.next()) {
				rsTableName = resultSet.getString("TABLE_NAME");
				if (rsTableName.equalsIgnoreCase(tableName)) {
					return true;
				}
			}
		} finally {
			resultSet.close();
		}

		return false;
	}

}
