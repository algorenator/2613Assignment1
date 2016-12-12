/**
 * Project: A00904362Gis
 * File: Assignment1.java
 * Date: Feb 2, 2016
 * Time: 4:52:19 PM
 */

package a00904362;

import java.awt.EventQueue;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.JFrame;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import a00904362.data.Game;
import a00904362.data.Persona;
import a00904362.data.Player;
import a00904362.data.Result;
import a00904362.data.Score;
import a00904362.data.dao.GameDao;
import a00904362.data.dao.PersonaDao;
import a00904362.data.dao.PlayerDao;
import a00904362.data.dao.ScoreDao;
import a00904362.data.db.Database;
import a00904362.data.io.GameReader;
import a00904362.data.io.PersonaReader;
import a00904362.data.io.PlayerReader;
import a00904362.data.io.ScoreReader;
import a00904362.ui.MainFrame;
import a00904362.utils.MiscUtil;
import a00904362.utils.ResultsSorter;

/**
 * @author Alexey Gorbenko, A00904362
 *
 */

public class Gis {

	private static final Logger LOG = Logger.getLogger(Gis.class);

	private static String FILE_LOG_SET = "log.properties";
	private static final String DB_PROPERTIES_FILENAME = "db.properties";
	private static final String PLAYERS_DATA_FILENAME = "players.dat";
	private static final String PERSONAS_DATA_FILENAME = "personas.dat";
	private static final String GAMES_DATA_FILENAME = "games.dat";
	private static final String SCORES_DATA_FILENAME = "scores.dat";
	// private static final String REPORT_FILENAME = "leaderboard_report.txt";
	private static final String RESULT_WIN = "WIN";
	private static final String RESULT_LOSS = "LOSE";

	private static final String SORT_BY_TOTAL = "by_total";
	private static final String SORT_BY_GAME = "by_game";
	private static final String SORT_ORDER_DESC = "desc";

	public static HashMap<Integer, Player> players;
	public static HashMap<Integer, Persona> personas;
	public static HashMap<String, Game> games;
	public static List<Score> scores;
	public static List<Result> results;
	public static List<Result> rep_results;
	public static List<String> dist_tags;

	private static Properties _properties;
	public static Database database;
	public static Connection conn;
	public static PlayerDao playerDao;
	public static PersonaDao personaDao;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		PropertyConfigurator.configure(FILE_LOG_SET);
		LOG.info("Starting Assignment 2");

		Instant firstInstant = Instant.now();
		LOG.info(firstInstant);

		try {

			populateData();

			populateResultList();

			showGUI();

		} catch (ApplicationException e) {
			LOG.error(e.getMessage());
			exit(-1);
		} catch (Exception e) {
			LOG.error(MiscUtil.getStackTrace(e));
			exit(-1);
		}

	}

	/**
	 * @param platform
	 * @param sortby
	 * @param order
	 */
	public static void prepareResults(String tag, String sortby, String order) {
		// TODO Auto-generated method stub
		if (tag != null) {
			rep_results = results.stream().filter(p -> p.getGameTag().equalsIgnoreCase(tag))
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
	public static void populateResultList() {

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
		// CollectionUtil.display(results);
	}

	/**
	 * @param file
	 */
	private static File getfile(String nfile) throws ApplicationException {
		File file = new File(nfile);
		if (!file.exists()) {
			throw new ApplicationException("Required " + nfile + " is missing.");
		}
		return file;

	}

	private static void populateData() throws ClassNotFoundException, SQLException, ApplicationException,
			FileNotFoundException, IOException, NotFoundException {
		File file = new File(DB_PROPERTIES_FILENAME);
		if (!file.exists()) {
			throw new ApplicationException("File DB properties " + DB_PROPERTIES_FILENAME + " doesnt exist");
		}

		_properties = new Properties();
		_properties.load(new FileInputStream(file));

		database = new Database(_properties);
		conn = database.getConnection();
		playerDao = new PlayerDao(database, "A00904362_Player");
		personaDao = new PersonaDao(database, "A00904362_Persona");
		GameDao gameDao = new GameDao(database, "A00904362_Game");
		ScoreDao scoreDao = new ScoreDao(database, "A00904362_Score");

		if (!database.tableExists(playerDao.getTableName())) {
			players = PlayerReader.readFromFile(getfile(PLAYERS_DATA_FILENAME));
			playerDao.create();
			playerDao.fillPlayers(conn, players);
		} else {
			List<Player> list = playerDao.loadAll(conn);
			players = new HashMap<Integer, Player>();
			for (Player e : list) {
				LOG.debug(e);
				players.put(e.getId(), e);

			}
		}

		if (!database.tableExists(gameDao.getTableName())) {
			games = GameReader.readFromFile(getfile(GAMES_DATA_FILENAME));
			gameDao.create();
			gameDao.fillGames(conn, games);
		} else {
			List<Game> list = gameDao.loadAll(conn);
			games = new HashMap<String, Game>();
			for (Game e : list) {
				LOG.debug(e);
				games.put(e.getId(), e);
			}
		}

		if (!database.tableExists(scoreDao.getTableName())) {
			scores = ScoreReader.readFromFile(getfile(SCORES_DATA_FILENAME));
			scoreDao.create();
			scoreDao.fillScores(conn, scores);
		} else {
			scores = scoreDao.loadAll(conn);
			LOG.debug(scores.toString());
		}

		if (!database.tableExists(personaDao.getTableName())) {
			personas = PersonaReader.readFromFile(getfile(PERSONAS_DATA_FILENAME));
			personaDao.create();
			personaDao.fillPersonas(conn, personas);
		} else {
			List<Persona> list = personaDao.loadAll(conn);
			personas = new HashMap<Integer, Persona>();
			for (Persona e : list) {
				LOG.debug(e);
				personas.put(e.getId(), e);
			}
		}

		populateDist_tags();

	}

	/**
	 * 
	 */
	public static void populateDist_tags() {
		dist_tags = new ArrayList<String>();
		// dist_tags = personaDao.loadDistinctTags(conn);
		for (Entry<Integer, Persona> entry : Gis.personas.entrySet()) {
			dist_tags.add(entry.getValue().getGametag());
		}
		Collections.sort(dist_tags);

		LOG.debug("gametags- " + dist_tags.toString());

	}

	private static void showGUI() throws ApplicationException {
		EventQueue.invokeLater(new Runnable() {
			public void run() {

				JFrame.setDefaultLookAndFeelDecorated(true);
				MainFrame frame = new MainFrame();
				frame.setVisible(true);
			}
		});

	}

	public static void exit(int code) {

		if (Gis.database != null) {
			Gis.database.shutdown();
		}
		LOG.info("Database closed");
		Instant secondInstant = Instant.now();
		LOG.info(secondInstant);

		LOG.info("GIS2 has finished. Exit code - " + code);
		System.exit(code);

	}
}
