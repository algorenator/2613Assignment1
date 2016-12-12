/**
 * Project: A00904362Gis
 * File: Score.java
 * Date: Feb 22, 2016
 * Time: 10:03:39 AM
 */

package a00904362.data;

/**
 * @author Alexey Gorbenko, A00904362
 *
 */

public class Score {

	private int persona_id;
	private String game_id;
	private String result;

	/**
	 * 
	 */
	public Score() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Score(int persona_id, String game_id) {
		super();
		this.persona_id = persona_id;
		this.game_id = game_id;
	}

	/**
	 * @return the persona_id
	 */
	public int getPersona_id() {
		return persona_id;
	}

	/**
	 * @param persona_id
	 *            the persona_id to set
	 */
	public void setPersona_id(int persona_id) {
		this.persona_id = persona_id;
	}

	/**
	 * @return the game_id
	 */
	public String getGame_id() {
		return game_id;
	}

	/**
	 * @param game_id
	 *            the game_id to set
	 */
	public void setGame_id(String game_id) {
		this.game_id = game_id;
	}

	/**
	 * @return the result
	 */
	public String getResult() {
		return result;
	}

	/**
	 * @param result
	 *            the result to set
	 */
	public void setResult(String result) {
		this.result = result;
	}

	/*
	 * @Override
	 * public int compareTo(Score other) {
	 * if (persona_id - other.persona_id != 0)
	 * return -1;
	 * if (game_id.equals(other.game_id))
	 * return 0;
	 * return -1;
	 * }
	 */
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Score [persona_id=" + persona_id + ", game_id=" + game_id + ", result=" + result + "]";
	}
}
