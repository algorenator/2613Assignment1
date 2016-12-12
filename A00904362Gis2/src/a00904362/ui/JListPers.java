/**
 * Project: A00904362Gis2
 * File: JListExample.java
 * Date: Mar 24, 2016
 * Time: 4:01:41 PM
 */

package a00904362.ui;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;

import a00904362.ApplicationException;
import a00904362.Gis;
import a00904362.NotFoundException;
import a00904362.data.Persona;
import a00904362.data.Player;

/**
 * @author Alexey Gorbenko, A00904362
 *
 */

public class JListPers extends JFrame {
	private JList<String> vwList;

	public JListPers(List<String> list) {

		String[] labels = list.toArray(new String[list.size()]);

		JFrame frame = new JFrame("Selecting Gametags");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JList jlist = new JList(labels);
		JScrollPane scrollPane1 = new JScrollPane(jlist);
		frame.add(scrollPane1, BorderLayout.CENTER);

		MouseListener mouseListener = new MouseAdapter() {
			public void mouseClicked(MouseEvent mouseEvent) {
				JList theList = (JList) mouseEvent.getSource();
				if (mouseEvent.getClickCount() == 2) {
					int index = theList.locationToIndex(mouseEvent.getPoint());
					if (index >= 0) {
						Object o = theList.getModel().getElementAt(index);
						System.out.println("Double-clicked on: " + o.toString());

						Persona pers = new Persona();
						pers.setGametag(o.toString());
						ArrayList<Persona> pers_res = new ArrayList<Persona>();
						try {
							pers_res = Gis.personaDao.searchMatching(Gis.conn, pers);

							pers = pers_res.get(0);
							System.out.println(pers.toString());

							Player player = new Player();
							player = Gis.playerDao.getObject(Gis.conn, pers.getPlayer_id());

							PersonaDialog dialog = new PersonaDialog(player, pers);
							dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
							dialog.setVisible(true);

						} catch (SQLException e) {
							new ApplicationException(e);
						} catch (NotFoundException e) {
							new ApplicationException(e);
						}

					}
				}
			}
		};
		jlist.addMouseListener(mouseListener);

		frame.setSize(350, 200);
		frame.setBounds(120, 120, 300, 300);
		frame.setVisible(true);
	}

}