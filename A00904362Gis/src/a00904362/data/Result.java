/**
 * Project: A00904362Gis
 * File: Result.java
 * Date: Feb 22, 2016
 * Time: 10:04:00 AM
 */

package a00904362.data;

/**
 * @author Alexey Gorbenko, A00904362
 *
 */

public class Result {
	// Win:Loss Game Name Gamertag Platform

	private int persona_id;
	private String game_id;
	private String gameName;
	private String gameTag;
	private String platform;
	private int win = 0;
	private int loss = 0;
	private int player_id;

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
	 * @return the total
	 */
	public int getTotal() {
		return this.win + this.loss;
	}

	/**
	 * @return the scorestr
	 */
	public String getScorestr() {
		return this.win + ":" + this.loss;
	}

	/**
	 * @return the gameName
	 */
	public String getGameName() {
		return gameName;
	}

	/**
	 * @param gameName
	 *            the gameName to set
	 */
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	/**
	 * @return the gameTag
	 */
	public String getGameTag() {
		return gameTag;
	}

	/**
	 * @param gameTag
	 *            the gameTag to set
	 */
	public void setGameTag(String gameTag) {
		this.gameTag = gameTag;
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

	/**
	 * @return the win
	 */
	public int getWin() {
		return win;
	}

	/**
	 * @param win
	 *            the win to set
	 */
	public void setWin(int win) {
		this.win = win;
	}

	/**
	 * @return the loss
	 */
	public int getLoss() {
		return loss;
	}

	/**
	 * @param loss
	 *            the loss to set
	 */
	public void setLoss(int loss) {
		this.loss = loss;
	}

	/**
	 * @param persona_id
	 * @param game_id
	 */
	public Result(int persona_id, String game_id) {
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

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Result [persona_id=" + persona_id + ", game_id=" + game_id + ", gameName=" + gameName + ", gameTag="
				+ gameTag + ", platform=" + platform + ", win=" + win + ", loss=" + loss + ", total=" + getTotal()
				+ ", scorestr=" + getScorestr() + "player_id=" + player_id + "]";
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((game_id == null) ? 0 : game_id.hashCode());
		result = prime * result + persona_id;
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Result other = (Result) obj;
		if (game_id == null) {
			if (other.game_id != null)
				return false;
		} else if (!game_id.equals(other.game_id))
			return false;
		if (persona_id != other.persona_id)
			return false;
		return true;
	}

}
