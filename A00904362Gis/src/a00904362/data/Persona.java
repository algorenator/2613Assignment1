/**
 * Project: A00904362Gis
 * File: Persona.java
 * Date: Feb 22, 2016
 * Time: 10:01:10 AM
 */

package a00904362.data;

/**
 * @author Alexey Gorbenko, A00904362
 *
 */

public class Persona {
	private int id;
	private int player_id;
	private String gametag;
	private String platform;

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the player_id
	 */
	public int getPlayer_id() {
		return player_id;
	}

	/**
	 * @param player_id
	 *            the player_id to set
	 */
	public void setPlayer_id(int player_id) {
		this.player_id = player_id;
	}

	/**
	 * @return the gametag
	 */
	public String getGametag() {
		return gametag;
	}

	/**
	 * @param gametag
	 *            the gametag to set
	 */
	public void setGametag(String gametag) {
		this.gametag = gametag;
	}

	/**
	 * @return the platform
	 */
	public String getPlatform() {
		return platform;
	}

	/**
	 * @param platform
	 *            the platform to set
	 */
	public void setPlatform(String platform) {
		this.platform = platform;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Persona [id=" + id + ", player_id=" + player_id + ", gametag=" + gametag + ", platform=" + platform
				+ "]";
	}

}
