/**
 * Project: A00904362Gis2
 * File: GameDao.java
 * Date: Mar 22, 2016
 * Time: 4:24:29 PM
 */

package a00904362.data.dao;

/**
 * @author Alexey Gorbenko, A00904362
 *
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import a00904362.NotFoundException;
import a00904362.data.Game;
import a00904362.data.db.Database;

public class GameDao extends Dao {
	private static String table_name;

	/**
	 * @param database
	 * @param tableName
	 */
	public GameDao(Database database, String tableName) {
		super(database, tableName);
		table_name = tableName;		//
	}

	public Game createValueObject() {
		return new Game();
	}

	public Game getObject(Connection conn, String id) throws NotFoundException, SQLException {

		Game valueObject = createValueObject();
		valueObject.setId(id);
		load(conn, valueObject);
		return valueObject;
	}

	public void load(Connection conn, Game valueObject) throws NotFoundException, SQLException {

		if (valueObject.getId() == null) {
			// System.out.println("Can not select without Primary-Key!");
			throw new NotFoundException("Can not select without Primary-Key!");
		}

		String sql = "SELECT * FROM " + table_name + " WHERE (id = ? ) ";
		PreparedStatement stmt = null;

		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, valueObject.getId());

			singleQuery(conn, stmt, valueObject);

		} finally {
			if (stmt != null)
				stmt.close();
		}
	}

	public List loadAll(Connection conn) throws SQLException {

		String sql = "SELECT * FROM " + table_name + " ORDER BY id ASC ";
		List searchResults = listQuery(conn, conn.prepareStatement(sql));

		return searchResults;
	}

	public void fillGames(Connection conn, HashMap<String, Game> games) throws SQLException {
		for (Entry<String, Game> entry : games.entrySet()) {
			create(conn, entry.getValue());
		}

		// games.forEach((k,v)->create(conn, v));
	}

	public synchronized void create(Connection conn, Game valueObject) throws SQLException {

		String sql = "";
		PreparedStatement stmt = null;
		ResultSet result = null;

		try {
			sql = "INSERT INTO " + table_name + " ( id, name, producer) VALUES (?, ?, ?) ";
			stmt = conn.prepareStatement(sql);

			stmt.setString(1, valueObject.getId());
			stmt.setString(2, valueObject.getName());
			stmt.setString(3, valueObject.getProducer());

			int rowcount = databaseUpdate(conn, stmt);
			if (rowcount != 1) {
				// System.out.println("PrimaryKey Error when updating DB!");
				throw new SQLException("PrimaryKey Error when updating DB!");
			}

		} finally {
			if (stmt != null)
				stmt.close();
		}

	}

	public void save(Connection conn, Game valueObject) throws NotFoundException, SQLException {

		String sql = "UPDATE " + table_name + " SET name = ?, producer = ? WHERE (id = ? ) ";
		PreparedStatement stmt = null;

		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, valueObject.getName());
			stmt.setString(2, valueObject.getProducer());

			stmt.setString(3, valueObject.getId());

			int rowcount = databaseUpdate(conn, stmt);
			if (rowcount == 0) {
				// System.out.println("Object could not be saved! (PrimaryKey not found)");
				throw new NotFoundException("Object could not be saved! (PrimaryKey not found)");
			}
			if (rowcount > 1) {
				// System.out.println("PrimaryKey Error when updating DB! (Many objects were affected!)");
				throw new SQLException("PrimaryKey Error when updating DB! (Many objects were affected!)");
			}
		} finally {
			if (stmt != null)
				stmt.close();
		}
	}

	public void delete(Connection conn, Game valueObject) throws NotFoundException, SQLException {

		if (valueObject.getId() == null) {
			// System.out.println("Can not delete without Primary-Key!");
			throw new NotFoundException("Can not delete without Primary-Key!");
		}

		String sql = "DELETE FROM " + table_name + " WHERE (id = ? ) ";
		PreparedStatement stmt = null;

		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, valueObject.getId());

			int rowcount = databaseUpdate(conn, stmt);
			if (rowcount == 0) {
				// System.out.println("Object could not be deleted (PrimaryKey not found)");
				throw new NotFoundException("Object could not be deleted! (PrimaryKey not found)");
			}
			if (rowcount > 1) {
				// System.out.println("PrimaryKey Error when updating DB! (Many objects were deleted!)");
				throw new SQLException("PrimaryKey Error when updating DB! (Many objects were deleted!)");
			}
		} finally {
			if (stmt != null)
				stmt.close();
		}
	}

	public List searchMatching(Connection conn, Game valueObject) throws SQLException {

		List searchResults;

		boolean first = true;
		StringBuffer sql = new StringBuffer("SELECT * FROM " + table_name + " WHERE 1=1 ");

		if (valueObject.getId() != null) {
			if (first) {
				first = false;
			}
			sql.append("AND id LIKE '").append(valueObject.getId()).append("%' ");
		}

		if (valueObject.getName() != null) {
			if (first) {
				first = false;
			}
			sql.append("AND name LIKE '").append(valueObject.getName()).append("%' ");
		}

		if (valueObject.getProducer() != null) {
			if (first) {
				first = false;
			}
			sql.append("AND producer LIKE '").append(valueObject.getProducer()).append("%' ");
		}

		sql.append("ORDER BY id ASC ");

		// Prevent accidential full table results.
		// Use loadAll if all rows must be returned.
		if (first)
			searchResults = new ArrayList();
		else
			searchResults = listQuery(conn, conn.prepareStatement(sql.toString()));

		return searchResults;
	}

	protected void singleQuery(Connection conn, PreparedStatement stmt, Game valueObject)
			throws NotFoundException, SQLException {

		ResultSet result = null;

		try {
			result = stmt.executeQuery();

			if (result.next()) {

				valueObject.setId(result.getString("id"));
				valueObject.setName(result.getString("name"));
				valueObject.setProducer(result.getString("producer"));

			} else {
				// System.out.println("Game Object Not Found!");
				throw new NotFoundException("Game Object Not Found!");
			}
		} finally {
			if (result != null)
				result.close();
			if (stmt != null)
				stmt.close();
		}
	}

	protected List listQuery(Connection conn, PreparedStatement stmt) throws SQLException {

		ArrayList searchResults = new ArrayList();
		ResultSet result = null;

		try {
			result = stmt.executeQuery();

			while (result.next()) {
				Game temp = createValueObject();

				temp.setId(result.getString("id"));
				temp.setName(result.getString("name"));
				temp.setProducer(result.getString("producer"));

				searchResults.add(temp);
			}

		} finally {
			if (result != null)
				result.close();
			if (stmt != null)
				stmt.close();
		}

		return (List) searchResults;
	}

	public void create() throws SQLException {

		String createStatement = "CREATE TABLE  " + table_name
				+ " (id varchar(20) NOT NULL,name varchar(255),producer varchar(255),PRIMARY KEY (ID))";
		super.create(createStatement);

	}

	public static String getTableName() {
		return table_name;
	}
}
