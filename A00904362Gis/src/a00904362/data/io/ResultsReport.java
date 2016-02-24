/**
 * Project: A00123456Lab4
 * File: PlayerReport.java
 */

package a00904362.data.io;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import a00904362.data.Result;

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

	/**
	 * Print the report.
	 * 
	 * @param players
	 */

	public static void write(List<Result> results, PrintStream out, String total) {
		LOG.debug("ResultsReport.write(..)");
		out.println("Games Report");
		out.println(SEPERATOR);
		out.format(HEADER_FORMAT, "Win:Loss", "Game Name ", "Gamertag", "Platform");
		out.println(SEPERATOR);
		for (Result res : results) {
			out.format(PERSONA_FORMAT, res.getScorestr(), res.getGameName(), res.getGameTag(), res.getPlatform());
		}
		out.println(SEPERATOR);

		if (total != null) {
			// print total
			for (Result res : getTotal(results)) {
				out.format("%-25s%-5s%n", res.getGameName(), res.getTotal());
			}
		}
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

}
