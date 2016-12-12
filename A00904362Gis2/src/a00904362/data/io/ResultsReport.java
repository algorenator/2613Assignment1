/**
 * Project: A00123456Lab4
 * File: PlayerReport.java
 */

package a00904362.data.io;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import a00904362.Gis;
import a00904362.data.Game;
import a00904362.data.Player;
import a00904362.data.Result;
import a00904362.data.Score;
import a00904362.ui.MainFrame;

/**
 * Prints a formated Players report.
 * 
 * @author Fred Fish, A00123456
 *
 */
public class ResultsReport {

	public static final String SEPERATOR = "-----------------------------------------------------------------------------";
	public static final String HEADER_FORMAT = " %-12s %-22s %-22s %-12s%n";
	public static final String PERSONA_FORMAT = " %-12s %-22s %-22s %-12s%n";
	private static final Logger LOG = LogManager.getLogger(ResultsReport.class);

	/**
	 * private constructor to prevent instantiation
	 */
	private ResultsReport() {
		LOG.debug("ResultsReport()");
	}

	public static List<Result> getTotal(List<Result> results) {
		Set<Result> set_tmp = new HashSet<Result>();
		for (Result p : results) {
			set_tmp.add(new Result(0, p.getGame_id()));
		}

		for (Result game : set_tmp) {
			for (Result score : results) {
				if (game.getGame_id().equals(score.getGame_id())) {
					game.setWin(game.getWin() + score.getWin());
					game.setLoss(game.getLoss() + score.getLoss());
					game.setGameName(score.getGameName());
				}
			}
		}
		// CollectionUtil.display(set_tmp);

		List<Result> list = new ArrayList<Result>(set_tmp);
		return list;

	}

	public static String getResultsStr(String ord) {

		LOG.info("wil be filtered by " + MainFrame.filter_tag);

		Gis.prepareResults(MainFrame.filter_tag, ord, (MainFrame.chckbxmntmDescending.isSelected()) ? "desc" : "asc");

		String out = String.format(HEADER_FORMAT, "Win:Loss", "Game Name ", "Gamertag", "Platform") + SEPERATOR + "\n";
		// out = "";
		for (Result res : Gis.rep_results) {
			out = out + out.format(PERSONA_FORMAT, res.getScorestr(), res.getGameName(), res.getGameTag(),
					res.getPlatform());
		}
		return out;
	}

	public static String getScoresStr() {
		String out = String.format("%-20s %-20s %-10s %n%n", "GameName", "GamerTag", "Result");
		for (Score sc : Gis.scores) {
			out = out + String.format("%-20s %-20s %-10s %n", Gis.games.get(sc.getGame_id()).getName(),
					Gis.personas.get(sc.getPersona_id()).getGametag(), sc.getResult());
		}
		return out;
	}

	public static String getPlayersStr() {
		String out = String.format(" %-5s %-20s %-20s %-30s %-10s %n%n", "ID", "FirstName", "LastName", "Email", "Age");

		for (Entry<Integer, Player> entry : Gis.players.entrySet()) {
			out = out + String.format(" %-5d %-20s %-20s %-30s %-10d %n", entry.getValue().getId(),
					entry.getValue().getFirstname(), entry.getValue().getLastname(), entry.getValue().getEmail(),
					entry.getValue().getAge());

			// prep_list.add(String.format(" %-5d %-$30s %-$30s %-$30s %-$10d %n", entry.getValue().getId(),
			// entry.getValue().getFirstname(), entry.getValue().getLastname(),
			// entry.getValue().getEmail(), entry.getValue().getAge()));

		}
		return out;
	}

	public static String getGamesStr() {
		String out = String.format(" %-20s %-20s %-20s %n%n", "ID", "Name", "Producer");

		for (Entry<String, Game> entry : Gis.games.entrySet()) {
			out = out + String.format(" %-20s %-20s %-20s %n", entry.getValue().getId(), entry.getValue().getName(),
					entry.getValue().getProducer());
		}
		return out;
	}

	public static String getTotalStr() {
		String out = "";
		for (Result res : getTotal(Gis.results)) {
			out = out + out.format("%-25s%-5s%n", res.getGameName(), res.getTotal());
		}

		return out;

	}

}
