/**
 * Project: A00904362Gis
 * File: PlayerReader.java
 * Date: Feb 22, 2016
 * Time: 10:16:15 AM
 */

package a00904362.data.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import a00904362.ApplicationException;
import a00904362.data.Player;
import a00904362.utils.Validator;

/**
 * @author Alexey Gorbenko, A00904362
 *
 */

public class PlayerReader {

	public static final String FIELD_DELIMITER = "\\|";

	private static final Logger LOG = LogManager.getLogger(PlayerReader.class);

	/**
	 * private constructor to prevent instantiation
	 */
	private PlayerReader() {
		LOG.debug("PlayerReader()");
	}

	public static HashMap<Integer, Player> readFromFile(File file) throws ApplicationException {
		String rec_num;
		LocalDate date;

		LOG.debug(file.getAbsolutePath());
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			throw new ApplicationException(e);
		}
		int i = 1;

		HashMap<Integer, Player> players = new HashMap<Integer, Player>();
		String row = scanner.nextLine();// skip header

		try {
			while (scanner.hasNext()) {
				row = scanner.nextLine();
				// LOG.debug(row);
				String[] elements = row.split(FIELD_DELIMITER);

				rec_num = "Record # " + (i++) + ". ";

				if (elements.length != Player.class.getDeclaredFields().length) {
					throw new ApplicationException(String.format(rec_num + "Expected %d but got %d: %s",
							Player.class.getDeclaredFields().length, elements.length, Arrays.toString(elements)));
				}

				Player player = new Player();

				if (!Validator.validate_email(elements[3])) {
					throw new ApplicationException(rec_num + "'" + elements[3] + "' is an invalid email address");
				}

				if (!Validator.validate_id(elements[0])) {
					throw new ApplicationException(rec_num + "Id in the list of players must be integer");
				}

				try {
					date = LocalDate.parse(elements[4], DateTimeFormatter.ofPattern("yyyyMMdd"));
				} catch (DateTimeParseException e) {
					throw new ApplicationException(rec_num + elements[4] + " - wrong date format. must be yyyyMMdd");
				}

				player.setId(Integer.parseInt(elements[0]));
				player.setLastname(elements[1]);
				player.setFirstname(elements[2]);
				String email = elements[3];
				player.setEmail(email);
				player.setDob(date);

				players.put(Integer.parseInt(elements[0]), player);

				// LOG.debug(player);
			}
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}

		return players;
	}

}