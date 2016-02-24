/**
 * Project: A00904362Lab3
 * File: Player.java
 * Date: Feb 09, 2016
 * Time: 4:53:36 PM
 */

package a00904362.data;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * @author Alexey Gorbenko, A00904362
 *
 */

public class Player {
	private int id;
	private String firstname;
	private String lastname;
	private String email;
	private GregorianCalendar dob;

	/**
	 * 
	 */
	public Player() {
		super();
	}

	/**
	 * @param id
	 * @param firstname
	 * @param lastname
	 * @param email
	 * @param gamertag
	 * @param dob
	 */
	public Player(int id, String firstname, String lastname, String email, String gamertag, GregorianCalendar dob) {
		super();
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.dob = dob;
	}

	/**
	 * @return the dob
	 */
	public GregorianCalendar getDob() {
		return dob;
	}

	/**
	 * @param date
	 *            the dob to set
	 */
	public void setDob(GregorianCalendar date) {
		this.dob = date;
	}

	public void setDob(LocalDate date) {
		this.dob = GregorianCalendar.from(date.atStartOfDay(ZoneId.systemDefault()));
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	@Deprecated
	public void setId(String id) {
		this.id = Integer.valueOf(id);
	}

	/**
	 * @return the firstname
	 */
	public String getFirstname() {
		return firstname;
	}

	/**
	 * @param firstname
	 *            the firstname to set
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	/**
	 * @return the lastname
	 */
	public String getLastname() {
		return lastname;
	}

	/**
	 * @param lastname
	 *            the lastname to set
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param emal
	 *            the emal to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	public int getAge() {
		Calendar now = new GregorianCalendar();
		return now.get(Calendar.YEAR) - this.getDob().get(Calendar.YEAR);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Player [id=" + id + ", firstname=" + firstname + ", lasttname=" + lastname + ", email=" + email + "]";
	}

}
