/**
 * Project: A00904362Lab9
 * File: PersonaDialog.java
 * Date: Mar 14, 2016
 * Time: 5:49:51 PM
 */

package a00904362.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import a00904362.ApplicationException;
import a00904362.Gis;
import a00904362.NotFoundException;
import a00904362.data.Persona;
import a00904362.data.Player;
import a00904362.utils.MiscUtil;
import a00904362.utils.Validator;
import net.miginfocom.swing.MigLayout;

/**
 * @author Alexey Gorbenko, A00904362
 *
 */

public class PersonaDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtId;
	private JTextField txtFirstname;
	private JTextField txtLastname;
	private JTextField txtEmail;
	private JTextField txtGamertag;
	private JTextField txtDob;
	private JTextField txtTag;

	/**
	 * Create the dialog.
	 * 
	 * @param pers
	 */
	public PersonaDialog(Player player, Persona pers) {
		setBounds(100 + MiscUtil.randomInteger(40, 60), 100 + MiscUtil.randomInteger(40, 60), 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[][][grow]", "[][][][][][]"));
		{
			JLabel lblId_1 = new JLabel("ID");
			contentPanel.add(lblId_1, "cell 1 0,alignx trailing");
		}
		{
			txtId = new JTextField();
			txtId.setEnabled(false);
			txtId.setText(Integer.toString(player.getId()));
			contentPanel.add(txtId, "cell 2 0,growx");
			txtId.setColumns(10);
		}
		{
			JLabel lblFirstName = new JLabel("First Name");
			contentPanel.add(lblFirstName, "cell 1 1,alignx trailing");
		}
		{
			txtFirstname = new JTextField();
			txtFirstname.setText(player.getFirstname());
			contentPanel.add(txtFirstname, "cell 2 1,growx");
			txtFirstname.setColumns(10);
		}
		{
			JLabel lblLastName = new JLabel("Last Name");
			contentPanel.add(lblLastName, "cell 1 2,alignx trailing,aligny center");
		}
		{
			txtLastname = new JTextField();
			txtLastname.setText(player.getLastname());
			contentPanel.add(txtLastname, "cell 2 2,growx");
			txtLastname.setColumns(10);
		}
		{
			JLabel lblEmail = new JLabel("Email");
			contentPanel.add(lblEmail, "cell 1 3,alignx trailing");
		}
		{
			txtEmail = new JTextField();
			txtEmail.setText(player.getEmail());
			contentPanel.add(txtEmail, "cell 2 3,growx");
			txtEmail.setColumns(10);
		}

		{
			JLabel lblBirthday = new JLabel("Birthday");
			contentPanel.add(lblBirthday, "cell 1 4,alignx trailing");
		}
		txtDob = new JTextField();
		txtDob.setEnabled(false);
		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd yyyy");
		txtDob.setText(sdf.format(player.getDob().getTime()));
		contentPanel.add(txtDob, "cell 2 4,growx");
		txtDob.setColumns(10);
		{
			JLabel lblGamertag = new JLabel("GamerTag");
			contentPanel.add(lblGamertag, "cell 1 5,alignx trailing");
		}
		{
			txtTag = new JTextField();
			txtTag.setText(pers.getGametag());
			contentPanel.add(txtTag, "cell 2 5,growx");
			txtTag.setColumns(10);
		}

		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						if (Validator.validate_email(txtEmail.getText())) {
							// update entity Lists
							Gis.players.get(player.getId()).setFirstname(txtFirstname.getText());
							Gis.players.get(player.getId()).setLastname(txtLastname.getText());
							Gis.players.get(player.getId()).setEmail(txtEmail.getText());

							Gis.personas.get(pers.getId()).setGametag(txtTag.getText());

							try {
								// update DB
								Gis.playerDao.save(Gis.conn, Gis.players.get(player.getId()));
								Gis.personaDao.save(Gis.conn, Gis.personas.get(pers.getId()));
							} catch (NotFoundException e1) {
								new ApplicationException(e1);
							} catch (SQLException e1) {
								new ApplicationException(e1);
							}

							// refresh prepared lists
							Gis.populateResultList();
							Gis.populateDist_tags();

							dispose();
						} else {
							JOptionPane.showMessageDialog(null, "Wrong email address", "Error",
									JOptionPane.ERROR_MESSAGE);

						}

					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}

	}

}
