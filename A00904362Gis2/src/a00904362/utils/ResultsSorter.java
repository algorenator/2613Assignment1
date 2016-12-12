/**
 * Project: A00904362Gis
 * File: ResultsSorter.java
 * Date: Feb 22, 2016
 * Time: 11:06:43 PM
 */

package a00904362.utils;

import java.util.Comparator;

import a00904362.data.Result;

/**
 * @author Alexey Gorbenko, A00904362
 *
 */

public class ResultsSorter {

	public static class CompareByGame implements Comparator<Result> {
		public int compare(Result res1, Result res2) {
			return res1.getGameName().compareTo(res2.getGameName());
		}
	}

	public static class CompareByGameDesc implements Comparator<Result> {
		public int compare(Result res1, Result res2) {
			return res2.getGameName().compareTo(res1.getGameName());
		}
	}

	public static class CompareByTotal implements Comparator<Result> {
		public int compare(Result res1, Result res2) {
			return res1.getTotal() - res2.getTotal();
		}
	}

	public static class CompareByTotaDesc implements Comparator<Result> {
		public int compare(Result res1, Result res2) {
			return res2.getTotal() - res1.getTotal();
		}
	}
}
