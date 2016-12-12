/**
 * Project: A00904362Gis
 * File: OptionalReport.java
 * Date: Feb 23, 2016
 * Time: 6:02:25 PM
 */

package a00904362.data.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import a00904362.data.Player;
import a00904362.data.PlayerResult;
import a00904362.data.Result;

/**
 * @author Alexey Gorbenko, A00904362
 *
 */

public class PlayersResultsReport {

	private static final Logger LOG = LogManager.getLogger(PlayersResultsReport.class);
	private static List<PlayerResult> playersresults;

	public static void showPlayersReport(List<Result> results, HashMap<Integer, Player> players) {
		LOG.debug("PlayersResultsReport()");
		playersresults = new ArrayList<>();
		populatePlayersResultList(results, players);
		printReport();
	};

	private static void populatePlayersResultList(List<Result> results, HashMap<Integer, Player> players) {

		// Map<Integer, Player> sorted_players = new TreeMap<Integer, Player>(players);

		for (Map.Entry<Integer, Player> player : players.entrySet()) {
			playersresults.add(new PlayerResult(player.getValue().getId(),
					player.getValue().getFirstname() + " " + player.getValue().getLastname(),
					player.getValue().getEmail(), player.getValue().getAge(), 0, 0));
		}

		// populate results
		for (PlayerResult player : playersresults) {
			for (Result result : results) {
				if (player.getPlayer_id() == result.getPlayer_id()) {
					player.setTotal(player.getTotal() + result.getTotal());
					player.setWon(player.getWon() + result.getWin());
				}
			}
		}
		// CollectionUtil.display(playersresults);
		LOG.debug("Size: " + playersresults.size());
	}

	/**
	 * 
	 */
	private static void printReport() {

		String separator = "------------------------------------------------------------------------------------------------------";
		System.out.format("%-10s%-20s%-30s%-10s%-20s%-20s%n", "Player ID", "Full name", "Email", "Age",
				"Total games played", "Total Wins");
		System.out.println(separator);
		for (PlayerResult player : playersresults) {
			System.out.format("%-10d%-20s%-30s%-10d%-20d%-20d%n", player.getPlayer_id(), player.getFull_name(),
					player.getEmail(), player.getAge(), player.getTotal(), player.getWon());
		}
		System.out.println(separator);
	}
}
