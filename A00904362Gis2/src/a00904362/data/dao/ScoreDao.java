/**
 * Project: A00904362Gis2
 * File: ScoreDao.java
 * Date: Mar 22, 2016
 * Time: 4:47:06 PM
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
import java.util.List;

import a00904362.NotFoundException;
import a00904362.data.Score;
import a00904362.data.db.Database;

public class ScoreDao extends Dao {
	private static String table_name;

	public ScoreDao(Database database, String tableName) {
		super(database, tableName);
		table_name = tableName;		//
	}

	public Score createValueObject() {
		return new Score();
	}

	public Score getObject(Connection conn, int persona_id, String game_id) throws NotFoundException, SQLException {

		Score valueObject = createValueObject();
		valueObject.setPersona_id(persona_id);
		valueObject.setGame_id(game_id);
		load(conn, valueObject);
		return valueObject;
	}

	public void load(Connection conn, Score valueObject) throws NotFoundException, SQLException {

		if (valueObject.getGame_id() == null) {
			// System.out.println("Can not select without Primary-Key!");
			throw new NotFoundException("Can not select without Primary-Key!");
		}

		String sql = "SELECT * FROM " + table_name + " WHERE (persona_id = ? AND game_id = ? ) ";
		PreparedStatement stmt = null;

		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, valueObject.getPersona_id());
			stmt.setString(2, valueObject.getGame_id());

			singleQuery(conn, stmt, valueObject);

		} finally {
			if (stmt != null)
				stmt.close();
		}
	}

	public List loadAll(Connection conn) throws SQLException {

		String sql = "SELECT * FROM " + table_name + " ORDER BY persona_id ASC ";
		List searchResults = listQuery(conn, conn.prepareStatement(sql));

		return searchResults;
	}

	public void fillScores(Connection conn, List<Score> scores) throws SQLException {
		for (Score s : scores) {
			create(conn, s);
		}
	}

	public synchronized void create(Connection conn, Score valueObject) throws SQLException {

		String sql = "";
		PreparedStatement stmt = null;
		ResultSet result = null;

		try {
			sql = "INSERT INTO " + table_name + " ( persona_id, game_id, result) VALUES (?, ?, ?) ";
			stmt = conn.prepareStatement(sql);

			stmt.setInt(1, valueObject.getPersona_id());
			stmt.setString(2, valueObject.getGame_id());
			stmt.setString(3, valueObject.getResult());

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

	public void save(Connection conn, Score valueObject) throws NotFoundException, SQLException {

		String sql = "UPDATE " + table_name + " SET result = ? WHERE (persona_id = ? AND game_id = ? ) ";
		PreparedStatement stmt = null;

		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, valueObject.getResult());

			stmt.setInt(2, valueObject.getPersona_id());
			stmt.setString(3, valueObject.getGame_id());

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

	public void delete(Connection conn, Score valueObject) throws NotFoundException, SQLException {

		if (valueObject.getGame_id() == null) {
			// System.out.println("Can not delete without Primary-Key!");
			throw new NotFoundException("Can not delete without Primary-Key!");
		}

		String sql = "DELETE FROM " + table_name + " WHERE (persona_id = ? AND game_id = ? ) ";
		PreparedStatement stmt = null;

		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, valueObject.getPersona_id());
			stmt.setString(2, valueObject.getGame_id());

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

	public List searchMatching(Connection conn, Score valueObject) throws SQLException {

		List searchResults;

		boolean first = true;
		StringBuffer sql = new StringBuffer("SELECT * FROM " + table_name + " WHERE 1=1 ");

		if (valueObject.getPersona_id() != 0) {
			if (first) {
				first = false;
			}
			sql.append("AND persona_id = ").append(valueObject.getPersona_id()).append(" ");
		}

		if (valueObject.getGame_id() != null) {
			if (first) {
				first = false;
			}
			sql.append("AND game_id LIKE '").append(valueObject.getGame_id()).append("%' ");
		}

		if (valueObject.getResult() != null) {
			if (first) {
				first = false;
			}
			sql.append("AND result LIKE '").append(valueObject.getResult()).append("%' ");
		}

		sql.append("ORDER BY persona_id ASC ");

		// Prevent accidential full table results.
		// Use loadAll if all rows must be returned.
		if (first)
			searchResults = new ArrayList();
		else
			searchResults = listQuery(conn, conn.prepareStatement(sql.toString()));

		return searchResults;
	}

	protected void singleQuery(Connection conn, PreparedStatement stmt, Score valueObject)
			throws NotFoundException, SQLException {

		ResultSet result = null;

		try {
			result = stmt.executeQuery();

			if (result.next()) {

				valueObject.setPersona_id(result.getInt("persona_id"));
				valueObject.setGame_id(result.getString("game_id"));
				valueObject.setResult(result.getString("result"));

			} else {
				// System.out.println("Score Object Not Found!");
				throw new NotFoundException("Score Object Not Found!");
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
				Score temp = createValueObject();

				temp.setPersona_id(result.getInt("persona_id"));
				temp.setGame_id(result.getString("game_id"));
				temp.setResult(result.getString("result"));

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

		String createStatement = "CREATE TABLE " + table_name + " (" + "persona_id int NOT NULL,"
				+ "game_id varchar(20) NOT NULL, result varchar(20))";
		super.create(createStatement);

	}

	public static String getTableName() {
		return table_name;
	}

}
