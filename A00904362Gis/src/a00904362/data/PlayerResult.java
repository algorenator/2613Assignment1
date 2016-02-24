/**
 * Project: A00904362Gis
 * File: PlayerResult.java
 * Date: Feb 23, 2016
 * Time: 6:29:25 PM
 */

package a00904362.data;

/**
 * @author Alexey Gorbenko, A00904362
 *
 */

public class PlayerResult {
	private int player_id;
	private String full_name;
	private String email;
	private int age;
	private int total;
	private int won;

	/**
	 * @return the player_id
	 */
	public int getPlayer_id() {
		return player_id;
	}

	/**
	 * @param player_id
	 * @param full_name
	 * @param email
	 * @param age
	 * @param total
	 * @param won
	 */
	public PlayerResult(int player_id, String full_name, String email, int age, int total, int won) {
		super();
		this.player_id = player_id;
		this.full_name = full_name;
		this.email = email;
		this.age = age;
		this.total = total;
		this.won = won;
	}

	/**
	 * @return the age
	 */
	public int getAge() {
		return age;
	}

	/**
	 * @param age
	 *            the age to set
	 */
	public void setAge(int age) {
		this.age = age;
	}

	/**
	 * @param player_id
	 *            the player_id to set
	 */
	public void setPlayer_id(int player_id) {
		this.player_id = player_id;
	}

	/**
	 * @return the full_name
	 */
	public String getFull_name() {
		return full_name;
	}

	/**
	 * @param full_name
	 *            the full_name to set
	 */
	public void setFull_name(String full_name) {
		this.full_name = full_name;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the total
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * @param total
	 *            the total to set
	 */
	public void setTotal(int total) {
		this.total = total;
	}

	/**
	 * @return the won
	 */
	public int getWon() {
		return won;
	}

	/**
	 * @param won
	 *            the won to set
	 */
	public void setWon(int won) {
		this.won = won;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PlayerResult [player_id=" + player_id + ", full_name=" + full_name + ", email=" + email + ", age=" + age
				+ ", total=" + total + ", won=" + won + "]";
	}

}
