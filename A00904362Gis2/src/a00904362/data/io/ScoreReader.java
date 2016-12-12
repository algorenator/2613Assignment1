/**
 * Project: A00904362Gis
 * File: ScoreReader.java
 * Date: Feb 22, 2016
 * Time: 1:13:08 PM
 */

package a00904362.data.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import a00904362.ApplicationException;
import a00904362.data.Score;
import a00904362.utils.Validator;

/**
 * @author Alexey Gorbenko, A00904362
 *
 */

public class ScoreReader {

	public static final String FIELD_DELIMITER = "\\|";

	private static final Logger LOG = LogManager.getLogger(Score.class);

	/**
	 * private constructor to prevent instantiation
	 */
	private ScoreReader() {
		LOG.debug("Score()");
	}

	public static ArrayList<Score> readFromFile(File file) throws ApplicationException {
		String rec_num;

		LOG.debug(file.getAbsolutePath());
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			throw new ApplicationException(e);
		}
		int i = 1;

		ArrayList<Score> scores = new ArrayList<Score>();
		String row = scanner.nextLine();// skip header

		try {
			while (scanner.hasNext()) {
				row = scanner.nextLine();
				// LOG.debug(row);
				String[] elements = row.split(FIELD_DELIMITER);

				rec_num = "Record # " + (i++) + ". ";

				if (elements.length != Score.class.getDeclaredFields().length) {
					throw new ApplicationException(String.format(rec_num + "Expected %d but got %d: %s",
							Score.class.getDeclaredFields().length, elements.length, Arrays.toString(elements)));
				}

				if (!Validator.validate_id(elements[0])) {
					throw new ApplicationException(rec_num + "Persona_Id in the list of scores must be integer");
				}

				Score score = new Score();
				score.setPersona_id(Integer.parseInt(elements[0]));
				score.setGame_id(elements[1]);
				score.setResult(elements[2]);

				scores.add(score);

				// LOG.debug(score);
			}
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}

		return scores;
	}

}
