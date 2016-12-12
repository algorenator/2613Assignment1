/**
 * Project: A00904362Gis
 * File: GameReader.java
 * Date: Feb 22, 2016
 * Time: 12:51:20 PM
 */

package a00904362.data.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import a00904362.ApplicationException;
import a00904362.data.Game;

/**
 * @author Alexey Gorbenko, A00904362
 *
 */

public class GameReader {

	public static final String FIELD_DELIMITER = "\\|";

	private static final Logger LOG = LogManager.getLogger(GameReader.class);

	/**
	 * private constructor to prevent instantiation
	 */
	private GameReader() {
		LOG.debug("GameReader()");
	}

	public static HashMap<String, Game> readFromFile(File file) throws ApplicationException {
		String rec_num;

		LOG.debug(file.getAbsolutePath());
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			throw new ApplicationException(e);
		}
		int i = 1;

		HashMap<String, Game> games = new HashMap<String, Game>();
		String row = scanner.nextLine();// skip header

		try {
			while (scanner.hasNext()) {
				row = scanner.nextLine();
				// LOG.debug(row);
				String[] elements = row.split(FIELD_DELIMITER);

				rec_num = "Record # " + (i++) + ". ";

				if (elements.length != Game.class.getDeclaredFields().length) {
					throw new ApplicationException(String.format(rec_num + "Expected %d but got %d: %s",
							Game.class.getDeclaredFields().length, elements.length, Arrays.toString(elements)));
				}

				Game game = new Game();
				game.setId(elements[0]);
				game.setName(elements[1]);
				game.setProducer(elements[2]);

				games.put(elements[0], game);

				// LOG.debug(game);
			}
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}

		return games;
	}

}
