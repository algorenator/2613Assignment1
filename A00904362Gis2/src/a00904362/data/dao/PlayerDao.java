/**
 * Project: A00904362Gis2
 * File: PlayerDao.java
 * Date: Mar 23, 2016
 * Time: 10:22:01 AM
 */

package a00904362.data.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import a00904362.NotFoundException;
import a00904362.data.Player;
import a00904362.data.db.Database;

public class PlayerDao extends Dao {
	private static String table_name;

	public PlayerDao(Database database, String tableName) {
		super(database, tableName);
		table_name = tableName;		//
	}

	public Player createValueObject() {
		return new Player();
	}

	public Player getObject(Connection conn, int id) throws NotFoundException, SQLException {

		Player valueObject = createValueObject();
		valueObject.setId(id);
		load(conn, valueObject);
		return valueObject;
	}

	public void load(Connection conn, Player valueObject) throws NotFoundException, SQLException {

		String sql = "SELECT * FROM " + table_name + "  WHERE (id = ? ) ";
		PreparedStatement stmt = null;

		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, valueObject.getId());

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

	public void fillPlayers(Connection conn, HashMap<Integer, Player> players) throws SQLException {
		for (Entry<Integer, Player> entry : players.entrySet()) {
			create(conn, entry.getValue());
		}
	}

	public synchronized void create(Connection conn, Player valueObject) throws SQLException {

		String sql = "";
		PreparedStatement stmt = null;
		ResultSet result = null;

		try {
			sql = "INSERT INTO " + table_name + " ( id, firstname, lastname, " + "email, dob) VALUES (?, ?, ?, ?, ?) ";
			stmt = conn.prepareStatement(sql);

			stmt.setInt(1, valueObject.getId());
			stmt.setString(2, valueObject.getFirstname());
			stmt.setString(3, valueObject.getLastname());
			stmt.setString(4, valueObject.getEmail());
			stmt.setDate(5, new Date(valueObject.getDob().getTime().getTime()));

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

	public void save(Connection conn, Player valueObject) throws NotFoundException, SQLException {

		String sql = "UPDATE " + table_name + " SET firstname = ?, lastname = ?, email = ?, "
				+ "dob = ? WHERE (id = ? ) ";
		PreparedStatement stmt = null;

		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, valueObject.getFirstname());
			stmt.setString(2, valueObject.getLastname());
			stmt.setString(3, valueObject.getEmail());
			stmt.setDate(4, new Date(valueObject.getDob().getTime().getTime()));

			stmt.setInt(5, valueObject.getId());

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

	public void delete(Connection conn, Player valueObject) throws NotFoundException, SQLException {

		String sql = "DELETE FROM " + table_name + " WHERE (id = ? ) ";
		PreparedStatement stmt = null;

		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, valueObject.getId());

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

	public List searchMatching(Connection conn, Player valueObject) throws SQLException {

		List searchResults;

		boolean first = true;
		StringBuffer sql = new StringBuffer("SELECT * FROM " + table_name + " WHERE 1=1 ");

		if (valueObject.getId() != 0) {
			if (first) {
				first = false;
			}
			sql.append("AND id = ").append(valueObject.getId()).append(" ");
		}

		if (valueObject.getFirstname() != null) {
			if (first) {
				first = false;
			}
			sql.append("AND firstname LIKE '").append(valueObject.getFirstname()).append("%' ");
		}

		if (valueObject.getLastname() != null) {
			if (first) {
				first = false;
			}
			sql.append("AND lastname LIKE '").append(valueObject.getLastname()).append("%' ");
		}

		if (valueObject.getEmail() != null) {
			if (first) {
				first = false;
			}
			sql.append("AND email LIKE '").append(valueObject.getEmail()).append("%' ");
		}

		if (valueObject.getDob() != null) {
			if (first) {
				first = false;
			}
			sql.append("AND dob = '").append(valueObject.getDob()).append("' ");
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

	protected void singleQuery(Connection conn, PreparedStatement stmt, Player valueObject)
			throws NotFoundException, SQLException {

		ResultSet result = null;

		try {
			result = stmt.executeQuery();

			if (result.next()) {

				valueObject.setId(result.getInt("id"));
				valueObject.setFirstname(result.getString("firstname"));
				valueObject.setLastname(result.getString("lastname"));
				valueObject.setEmail(result.getString("email"));
				valueObject.setDob(result.getDate("dob"));

			} else {
				// System.out.println("Player Object Not Found!");
				throw new NotFoundException("Player Object Not Found!");
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
				Player temp = createValueObject();

				temp.setId(result.getInt("id"));
				temp.setFirstname(result.getString("firstname"));
				temp.setLastname(result.getString("lastname"));
				temp.setEmail(result.getString("email"));
				temp.setDob(result.getDate("dob"));

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
				+ " (id int NOT NULL,firstname varchar(255),lastname varchar(255),email varchar(255),dob date,PRIMARY KEY(id))";
		super.create(createStatement);

	}

	public static String getTableName() {
		return table_name;
	}

}