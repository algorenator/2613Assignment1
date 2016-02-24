/**
 * Project: A00904362Lab3
 * File: Valdator.java
 * Date: Feb 09, 2016
 * Time: 4:10:46 PM
 */

package a00904362.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Alexey Gorbenko, A00904362
 *
 */

public class Validator {
	static final String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
	static final String INT_REGEX = "^\\d+$";

	public static boolean validate_email(String emailStr) {
		if (emailStr.matches(EMAIL_REGEX)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean validate_id(String id) {
		if (id.matches(INT_REGEX)) {
			return true;
		} else {
			return false;
		}
	}

	@Deprecated
	public static boolean validate_dob(String dob, SimpleDateFormat formatter) {
		try {
			@SuppressWarnings("unused")
			Date date = formatter.parse(dob);
			return true;
		} catch (ParseException e) {
			return false;
		}

	}

	public static boolean validate_args(String[] args) {
		if (args.length == 0) {
			return false;
		} else {
			return true;
		}

	}

}
