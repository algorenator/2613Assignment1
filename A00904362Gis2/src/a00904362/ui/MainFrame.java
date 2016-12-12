/**
 * Project: A00904362Lab9
 * File: MainFrame.java
 * Date: Mar 14, 2016
 * Time: 5:21:43 PM
 */

package a00904362.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import a00904362.Gis;
import a00904362.data.io.ResultsReport;
import a00904362.utils.MiscUtil;

/**
 * @author Alexey Gorbenko, A00904362
 *
 */

@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	private static final Logger LOG = LogManager.getLogger(MainFrame.class);
	public static JCheckBoxMenuItem chckbxmntmDescending;
	public static String filter_tag;

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public MainFrame() {

		LOG.debug(LOG.getName());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setTitle("GIS 2");
		buildMenu();

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent evt) {
				Gis.exit(0);
			}
		});
	}

	/**
	 * 
	 */

	private void buildMenu() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		mnFile.setMnemonic('F');
		menuBar.add(mnFile);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Gis.exit(0);
			}
		});
		mntmExit.setMnemonic('x');
		mnFile.add(mntmExit);

		JMenu mnLists = new JMenu("Lists");
		mnLists.setMnemonic('L');
		menuBar.add(mnLists);

		JMenuItem mntmPlayers = new JMenuItem("Players");
		mntmPlayers.setMnemonic('y');
		mntmPlayers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// List<String> prep_list = new ArrayList<String>();

				JFrame frame = new JFrame("Players");
				frame.getContentPane().setLayout(new FlowLayout());
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

				JTextArea textAreal = new JTextArea(ResultsReport.getPlayersStr(), Gis.players.size() + 2, 5);
				textAreal.setPreferredSize(new Dimension(600, 200));
				frame.setLocation(50, 200);

				textAreal.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

				textAreal.setLineWrap(false);
				textAreal.setEditable(false);

				frame.getContentPane().add(textAreal);

				frame.pack();
				frame.setVisible(true);

				// new JListExample(prep_list);
			}
		});
		mnLists.add(mntmPlayers);

		JMenuItem mntmPersonas = new JMenuItem("Personas");
		mntmPersonas.setMnemonic('e');
		mntmPersonas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				new JListPers(Gis.dist_tags);

			}
		});
		mnLists.add(mntmPersonas);

		JMenuItem mntmScores = new JMenuItem("Scores");
		mntmScores.setMnemonic('s');
		mntmScores.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				JFrame frame = new JFrame("Scores");
				frame.getContentPane().setLayout(new FlowLayout());
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

				JTextArea textAreal = new JTextArea(ResultsReport.getScoresStr(), Gis.scores.size() + 2, 3);
				textAreal.setPreferredSize(new Dimension(400, 200));
				frame.setLocation(50, 200);
				textAreal.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

				textAreal.setLineWrap(false);
				textAreal.setEditable(false);

				frame.getContentPane().add(textAreal);

				frame.pack();
				frame.setVisible(true);

				// JOptionPane.showMessageDialog(MainFrame.this, ResultsReport.getScoresStr(), "Scores",
				// JOptionPane.INFORMATION_MESSAGE);

			}
		});
		mnLists.add(mntmScores);

		JMenuItem mntmGames = new JMenuItem("Games");
		mntmGames.setMnemonic('g');
		mntmGames.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFrame frame = new JFrame("Games");
				frame.getContentPane().setLayout(new FlowLayout());
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

				JTextArea textAreal = new JTextArea(ResultsReport.getGamesStr(), Gis.games.size() + 2, 3);
				textAreal.setPreferredSize(new Dimension(500, 200));
				frame.setLocation(100, 200);

				textAreal.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

				textAreal.setLineWrap(false);
				textAreal.setEditable(false);

				frame.getContentPane().add(textAreal);

				frame.pack();
				frame.setVisible(true);
			}
		});
		mnLists.add(mntmGames);

		JMenu mnReports = new JMenu("Reports");
		mnReports.setMnemonic('R');
		menuBar.add(mnReports);

		JMenuItem mntmTotal = new JMenuItem("Total");
		mntmTotal.setMnemonic('T');
		mntmTotal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(MainFrame.this, ResultsReport.getTotalStr(), "Totals",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});
		mnReports.add(mntmTotal);

		chckbxmntmDescending = new JCheckBoxMenuItem("Descending");
		chckbxmntmDescending.setMnemonic('D');
		mnReports.add(chckbxmntmDescending);

		JMenuItem mntmByGame = new JMenuItem("By Game");
		mntmByGame.setMnemonic('G');
		mntmByGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame frame = new JFrame("Results by game");
				frame.getContentPane().setLayout(new FlowLayout());
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

				JTextArea textAreal = new JTextArea(ResultsReport.getResultsStr("by_game"), Gis.results.size() + 2, 4);
				textAreal.setPreferredSize(new Dimension(500, 200));
				frame.setLocation(100, 200);

				textAreal.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

				textAreal.setLineWrap(false);
				textAreal.setEditable(false);

				frame.getContentPane().add(textAreal);

				frame.pack();
				frame.setVisible(true);
			}
		});
		mnReports.add(mntmByGame);

		JMenuItem mntmByCount = new JMenuItem("By Count");
		mntmByCount.setMnemonic('C');
		mntmByCount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame frame = new JFrame("Results by total");
				frame.getContentPane().setLayout(new FlowLayout());
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

				JTextArea textAreal = new JTextArea(ResultsReport.getResultsStr("by_total"), Gis.results.size() + 2, 4);
				textAreal.setPreferredSize(new Dimension(500, 200));
				frame.setLocation(100, 200);

				textAreal.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

				textAreal.setLineWrap(false);
				textAreal.setEditable(false);

				frame.getContentPane().add(textAreal);

				frame.pack();
				frame.setVisible(true);
			}
		});
		mnReports.add(mntmByCount);

		JMenuItem mntmGamertag = new JMenuItem("Gamertag[  ]");
		mntmGamertag.setMnemonic('a');
		mntmGamertag.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// filter_tag = (String) JOptionPane.showInputDialog(null, "Please choose a filter", "Gamertag",
				// JOptionPane.QUESTION_MESSAGE, null, new Object[] { null, "AN", "IO", "PC", "PS", "XB" }, "");
				// mntmGamertag.setText("Gamertag[" + MiscUtil.nvl(filter_tag, " ") + "]");

				List<String> dist_tags_tmp = new<String> ArrayList();
				dist_tags_tmp = Gis.dist_tags;
				dist_tags_tmp.add(0, null);

				filter_tag = (String) JOptionPane.showInputDialog(null, "Please choose a filter", "Gamertag",
						JOptionPane.QUESTION_MESSAGE, null, dist_tags_tmp.toArray(new String[dist_tags_tmp.size()]),
						"");
				mntmGamertag.setText("Gamertag[" + MiscUtil.nvl(filter_tag, "  ") + "]");

			}
		});
		mnReports.add(mntmGamertag);

		JMenu mnHelp = new JMenu("Help");
		mnHelp.setMnemonic('H');
		menuBar.add(mnHelp);

		JMenuItem mntmAbout = new JMenuItem("About");
		mntmAbout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));

		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(MainFrame.this, "GIS 2\nBy Alexey Gorbenko A00904362", "About GIS 2",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});
		mnHelp.add(mntmAbout);

	}

}
