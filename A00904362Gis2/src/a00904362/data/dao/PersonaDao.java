/**
 * Project: A00904362Gis2
 * File: PersonaDao.java
 * Date: Mar 22, 2016
 * Time: 4:39:28 PM
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
import a00904362.data.Persona;
import a00904362.data.db.Database;

public class PersonaDao extends Dao {
	private static String table_name;

	public PersonaDao(Database database, String tableName) {
		super(database, tableName);
		table_name = tableName;		//
	}

	public Persona createValueObject() {
		return new Persona();
	}

	public Persona getObject(Connection conn, int id) throws NotFoundException, SQLException {

		Persona valueObject = createValueObject();
		valueObject.setId(id);
		load(conn, valueObject);
		return valueObject;
	}

	public void load(Connection conn, Persona valueObject) throws NotFoundException, SQLException {

		String sql = "SELECT * FROM " + table_name + " WHERE (id = ? ) ";
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

	public List<String> loadDistinctTags(Connection conn) throws SQLException {

		String sql = "select  distinct gametag  from  " + table_name + " order by gametag ";

		ArrayList searchResults = new ArrayList();
		ResultSet result = null;

		try {
			result = conn.prepareStatement(sql).executeQuery();

			while (result.next()) {
				searchResults.add(result.getString(1));
			}

		} finally {
			if (result != null)
				result.close();
			if (conn.prepareStatement(sql) != null)
				conn.prepareStatement(sql).close();
		}

		return (List) searchResults;
	}

	public List loadAll(Connection conn) throws SQLException {

		String sql = "SELECT * FROM " + table_name + " ORDER BY id ASC ";
		List searchResults = listQuery(conn, conn.prepareStatement(sql));

		return searchResults;
	}

	public void fillPersonas(Connection conn, HashMap<Integer, Persona> personas) throws SQLException {
		for (Entry<Integer, Persona> entry : personas.entrySet()) {
			create(conn, entry.getValue());
		}
	}

	public synchronized void create(Connection conn, Persona valueObject) throws SQLException {

		String sql = "";
		PreparedStatement stmt = null;
		ResultSet result = null;

		try {
			sql = "INSERT INTO " + table_name + " ( id, player_id, gametag, " + "platform) VALUES (?, ?, ?, ?) ";
			stmt = conn.prepareStatement(sql);

			stmt.setInt(1, valueObject.getId());
			stmt.setInt(2, valueObject.getPlayer_id());
			stmt.setString(3, valueObject.getGametag());
			stmt.setString(4, valueObject.getPlatform());

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

	public void save(Connection conn, Persona valueObject) throws NotFoundException, SQLException {

		String sql = "UPDATE " + table_name + " SET player_id = ?, gametag = ?, platform = ? WHERE (id = ? ) ";
		PreparedStatement stmt = null;

		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, valueObject.getPlayer_id());
			stmt.setString(2, valueObject.getGametag());
			stmt.setString(3, valueObject.getPlatform());

			stmt.setInt(4, valueObject.getId());

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

	public void delete(Connection conn, Persona valueObject) throws NotFoundException, SQLException {

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

	public ArrayList<Persona> searchMatching(Connection conn, Persona valueObject) throws SQLException {

		List searchResults;

		boolean first = true;
		StringBuffer sql = new StringBuffer("SELECT * FROM " + table_name + " WHERE 1=1 ");

		if (valueObject.getId() != 0) {
			if (first) {
				first = false;
			}
			sql.append("AND id = ").append(valueObject.getId()).append(" ");
		}

		if (valueObject.getPlayer_id() != 0) {
			if (first) {
				first = false;
			}
			sql.append("AND player_id = ").append(valueObject.getPlayer_id()).append(" ");
		}

		if (valueObject.getGametag() != null) {
			if (first) {
				first = false;
			}
			sql.append("AND gametag LIKE '").append(valueObject.getGametag()).append("%' ");
		}

		if (valueObject.getPlatform() != null) {
			if (first) {
				first = false;
			}
			sql.append("AND platform LIKE '").append(valueObject.getPlatform()).append("%' ");
		}

		sql.append("ORDER BY id ASC ");

		// Prevent accidential full table results.
		// Use loadAll if all rows must be returned.
		if (first)
			searchResults = new ArrayList();
		else
			searchResults = listQuery(conn, conn.prepareStatement(sql.toString()));

		return (ArrayList<Persona>) searchResults;
	}

	protected void singleQuery(Connection conn, PreparedStatement stmt, Persona valueObject)
			throws NotFoundException, SQLException {

		ResultSet result = null;

		try {
			result = stmt.executeQuery();

			if (result.next()) {

				valueObject.setId(result.getInt("id"));
				valueObject.setPlayer_id(result.getInt("player_id"));
				valueObject.setGametag(result.getString("gametag"));
				valueObject.setPlatform(result.getString("platform"));

			} else {
				// System.out.println("Persona Object Not Found!");
				throw new NotFoundException("Persona Object Not Found!");
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
				Persona temp = createValueObject();

				temp.setId(result.getInt("id"));
				temp.setPlayer_id(result.getInt("player_id"));
				temp.setGametag(result.getString("gametag"));
				temp.setPlatform(result.getString("platform"));

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

		String createStatement = "CREATE TABLE " + table_name + " (" + "id bigint NOT NULL," + "player_id int,"
				+ "gametag varchar(255)," + "platform char(2)," + "PRIMARY KEY(id))";
		super.create(createStatement);

	}

	public static String getTableName() {
		return table_name;
	}

}