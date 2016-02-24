/**
 * Project: A00904362Gis
 * File: Assignment1.java
 * Date: Feb 2, 2016
 * Time: 4:52:19 PM
 */

package a00904362;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import a00904362.data.Game;
import a00904362.data.Persona;
import a00904362.data.Player;
import a00904362.data.Result;
import a00904362.data.Score;
import a00904362.data.io.GameReader;
import a00904362.data.io.PersonaReader;
import a00904362.data.io.PlayerReader;
import a00904362.data.io.PlayersResultsReport;
import a00904362.data.io.ResultsReport;
import a00904362.data.io.ScoreReader;
import a00904362.utils.CollectionUtil;
import a00904362.utils.MiscUtil;
import a00904362.utils.ResultsSorter;

/**
 * @author Alexey Gorbenko, A00904362
 *
 */

public class Assignment1 {

	private static int exit_code = 0;

	private static final Logger LOG = LogManager.getLogger(Assignment1.class);

	private static String FILE_LOG_SET = "log.properties";
	private static final String PLAYERS_DATA_FILENAME = "players.dat";
	private static final String PERSONAS_DATA_FILENAME = "personas.dat";
	private static final String GAMES_DATA_FILENAME = "games.dat";
	private static final String SCORES_DATA_FILENAME = "scores.dat";
	private static final String REPORT_FILENAME = "leaderboard_report.txt";
	private static final String RESULT_WIN = "WIN";
	private static final String RESULT_LOSS = "LOSE";

	private static final String SORT_BY_TOTAL = "by_total";
	private static final String SORT_BY_GAME = "by_game";
	private static final String SORT_ORDER_DESC = "desc";
	private static final String SHOW_TOTAL = "total";
	private static final String FILTER_BY = "platform";

	private static HashMap<Integer, Player> players;
	private static HashMap<Integer, Persona> personas;
	private static HashMap<String, Game> games;
	private static List<Score> scores;
	private static List<Result> results;
	private static List<Result> rep_results;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		PropertyConfigurator.configure(FILE_LOG_SET);
		LOG.info("Starting Assignment 1");

		Instant firstInstant = Instant.now();
		LOG.info(firstInstant);

		try {
			players = PlayerReader.read(getfile(PLAYERS_DATA_FILENAME));
			personas = PersonaReader.read(getfile(PERSONAS_DATA_FILENAME));
			games = GameReader.read(getfile(GAMES_DATA_FILENAME));
			scores = ScoreReader.read(getfile(SCORES_DATA_FILENAME));

			populateResultList();

			if (args[0].equals("players")) {
				/// optional playersresults report
				PlayersResultsReport.showPlayersReport(results, players);
			} else {
				prepareResults(getArgValue(args, "filter"), getArgValue(args, "sort_by"), getArgValue(args, "desc"));

				displayResults(rep_results, getArgValue(args, "total"));
			}
		} catch (ApplicationException e) {
			LOG.error(e.getMessage());
			exit_code = -1;
		} catch (Exception e) {
			LOG.error(MiscUtil.getStackTrace(e));
			exit_code = -1;
		}

		Instant secondInstant = Instant.now();
		LOG.info(secondInstant);
		LOG.info(Duration.between(firstInstant, secondInstant).toMillis() + " ms");
		LOG.info("End Assignment 1. exit_code " + exit_code);

		System.exit(exit_code);
	}

	/**
	 * @param platform
	 * @param sortby
	 * @param order
	 */
	private static void prepareResults(String platform, String sortby, String order) {
		// TODO Auto-generated method stub
		if (platform != null) {
			rep_results = results.stream().filter(p -> p.getPlatform().equalsIgnoreCase(platform))
					.collect(Collectors.toList());
		} else {
			rep_results = new ArrayList<Result>(results);
		}

		if (sortby != null) {
			if (sortby.equalsIgnoreCase(SORT_BY_GAME)) {
				if (order != null && order.equalsIgnoreCase(SORT_ORDER_DESC)) {
					Collections.sort(rep_results, new ResultsSorter.CompareByGameDesc());
				} else {
					Collections.sort(rep_results, new ResultsSorter.CompareByGame());
				}
			}
			if (sortby.equalsIgnoreCase(SORT_BY_TOTAL)) {
				if (order != null && order.equalsIgnoreCase(SORT_ORDER_DESC)) {
					Collections.sort(rep_results, new ResultsSorter.CompareByTotaDesc());
				} else {
					Collections.sort(rep_results, new ResultsSorter.CompareByTotal());
				}
			}
		}
	}

	/**
	 * 
	 */
	private static void populateResultList() {

		List<Result> result_tmp = new ArrayList<>(); // prepare distinct result list
		for (Score p : scores) {
			result_tmp.add(new Result(p.getPersona_id(), p.getGame_id()));
		}
		Set<Result> hashsetList = new HashSet<Result>(result_tmp);
		results = new ArrayList<>(hashsetList);

		// populate results
		for (Result res : results) {
			for (Score score : scores) {
				if (res.getPersona_id() == score.getPersona_id() && res.getGame_id().equals(score.getGame_id())) {
					if (score.getResult().equals(RESULT_WIN)) {
						res.setWin(res.getWin() + 1);
					}
					if (score.getResult().equals(RESULT_LOSS)) {
						res.setLoss(res.getLoss() + 1);
					}
				}
			}
			res.setGameName(games.get(res.getGame_id()).getName());
			res.setGameTag(personas.get(res.getPersona_id()).getGametag());
			res.setPlatform(personas.get(res.getPersona_id()).getPlatform());
			res.setPlayer_id(personas.get(res.getPersona_id()).getPlayer_id());

			LOG.debug(res);
		}
		LOG.debug("Initial list. Size: " + results.size());
		CollectionUtil.display(results);
	}

	/**
	 * @param file
	 */
	private static File getfile(String nfile) throws ApplicationException {
		File file = new File(nfile);
		if (!file.exists()) {
			exit_code = -1;
			throw new ApplicationException("Required " + nfile + " is missing.");
		}
		return file;

	}

	private static void displayResults(List<Result> results, String total) throws ApplicationException {
		File inventory = new File(REPORT_FILENAME);
		PrintStream out = null;
		LOG.debug("Ready to print. Size: " + results.size());
		try {
			out = new PrintStream(new FileOutputStream(inventory));
			ResultsReport.write(results, out, total);
		} catch (FileNotFoundException e) {
			exit_code = -1;
			throw new ApplicationException(e.getMessage());

		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	private static String getArgValue(String[] args, String param) {

		String ret = null;

		switch (param) {
		case "sort_by":
			for (String input : args) {
				if (input.contains(SORT_BY_TOTAL)) {
					ret = SORT_BY_TOTAL;
				}
				if (input.contains(SORT_BY_GAME)) {
					ret = SORT_BY_GAME;
				}
			}
			break;
		case "total":
			for (String input : args) {
				if (input.contains(SHOW_TOTAL)) {
					ret = SHOW_TOTAL;
				}
			}
			break;
		case "desc":
			for (String input : args) {
				if (input.contains(SORT_ORDER_DESC)) {
					ret = SORT_ORDER_DESC;
				}
			}
			break;
		case "filter":
			for (String input : args) {
				if (input.contains(FILTER_BY + "=") && input.length() == FILTER_BY.length() + 3) {
					ret = input.substring(input.indexOf("=") + 1, input.indexOf("=") + 3);
				}
			}
			break;

		}

		LOG.debug("arg - " + ret);

		return ret;
	}

}
