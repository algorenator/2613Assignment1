/**
 * Project: A00904362Gis
 * File: PersonaReader.java
 * Date: Feb 22, 2016
 * Time: 11:47:47 AM
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
import a00904362.data.Persona;
import a00904362.utils.Validator;

/**
 * @author Alexey Gorbenko, A00904362
 *
 *         test git
 */

public class PersonaReader {

	public static final String FIELD_DELIMITER = "\\|";

	private static final Logger LOG = LogManager.getLogger(PersonaReader.class);

	/**
	 * private constructor to prevent instantiation
	 */
	private PersonaReader() {
		LOG.debug("PersonaReader()");
	}

	public static HashMap<Integer, Persona> read(File file) throws ApplicationException {
		String rec_num;

		LOG.debug(file.getAbsolutePath());
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			throw new ApplicationException(e);
		}
		int i = 1;

		HashMap<Integer, Persona> personas = new HashMap<Integer, Persona>();
		String row = scanner.nextLine();// skip header

		try {
			while (scanner.hasNext()) {
				row = scanner.nextLine();
				// LOG.debug(row);
				String[] elements = row.split(FIELD_DELIMITER);

				rec_num = "Record # " + (i++) + ". ";

				if (elements.length != Persona.class.getDeclaredFields().length) {
					throw new ApplicationException(String.format(rec_num + "Expected %d but got %d: %s",
							Persona.class.getDeclaredFields().length, elements.length, Arrays.toString(elements)));
				}

				Persona persona = new Persona();

				if (!Validator.validate_id(elements[0])) {
					throw new ApplicationException(rec_num + "Id must be integer");
				}

				if (!Validator.validate_id(elements[1])) {
					throw new ApplicationException(rec_num + "Player_Id must be integer");
				}

				persona.setId(Integer.parseInt(elements[0]));
				persona.setPlayer_id(Integer.parseInt(elements[1]));
				persona.setGametag(elements[2]);
				persona.setPlatform(elements[3]);

				personas.put(Integer.parseInt(elements[0]), persona);

				// LOG.debug(persona);
			}
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}

		return personas;
	}

}
