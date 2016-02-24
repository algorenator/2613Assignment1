/**
 * Project: A00904362Gis
 * File: MiscUtil.java
 * Date: Feb 23, 2016
 * Time: 5:24:18 PM
 */

package a00904362.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author Alexey Gorbenko, A00904362
 *
 */

public class MiscUtil {

	public static String getStackTrace(final Throwable throwable) {
		final StringWriter sw = new StringWriter();
		final PrintWriter pw = new PrintWriter(sw, true);
		throwable.printStackTrace(pw);
		return sw.getBuffer().toString();
	}
}
