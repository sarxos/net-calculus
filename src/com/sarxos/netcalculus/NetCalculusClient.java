package com.sarxos.netcalculus;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.skin.BusinessBlackSteelSkin;
import org.jvnet.substance.skin.BusinessBlueSteelSkin;
import org.jvnet.substance.skin.CremeSkin;
import org.jvnet.substance.skin.ModerateSkin;
import org.jvnet.substance.skin.OfficeSilver2007Skin;
import org.jvnet.substance.skin.SaharaSkin;
import org.jvnet.substance.skin.SubstanceSkin;
import org.jvnet.substance.theme.SubstanceBarbyPinkTheme;
import org.jvnet.substance.theme.SubstanceEbonyTheme;
import org.jvnet.substance.theme.SubstanceSepiaTheme;
import org.jvnet.substance.theme.SubstanceSunGlareTheme;
import org.jvnet.substance.theme.SubstanceSunsetTheme;
import org.jvnet.substance.theme.SubstanceUltramarineTheme;
import org.jvnet.substance.watermark.SubstanceBinaryWatermark;
import org.jvnet.substance.watermark.SubstanceImageWatermark;
import org.jvnet.substance.watermark.SubstancePlanktonWatermark;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;

import com.sarxos.netcalculus.server.NetCalculusCommands;


/**
 * Klasa klienta obliczajacego. Jeden serwer dodawania/odejmowania i inny 
 * mnozenia/dzielenia.<br>
 */
public class NetCalculusClient extends JFrame implements WindowListener {

	private static final long serialVersionUID = 1L;
	
	protected ClientThread watek_dodawania = null;
	protected ClientThread watek_mnozenia = null;
	
	private String serwer_dodawania = "localhost";
	private String serwer_mnozenia = "localhost";
	private int port_dodawania = 1223;
	private int port_mnozenia = 1224;
	private double liczba_a = 0;
	private double liczba_b = 0;
	private double liczba_wynik = 0;
	
	/* NOTE!
	 * Ramka powitalna jest w statycznym kontekscie zeby mogla byc uruchomiona z main.
	 */
	
	private static JFrame welcom_frame = null; 
	
	private JLabel srv_dodawania_opis = null;
	private JLabel srv_mnozenia_opis = null;
	private JTextField server_dodawania_tf = null;
	private JTextField server_mnozenia_tf = null;
	private JTextField port_dodawania_tf = null;
	private JTextField port_mnozenia_tf = null;
	private JTextField liczba_a_tf = null;
	private JTextField liczba_b_tf = null;
	private JTextField liczba_wynik_tf = null;
	private JButton button_polacz = null;
	private JButton button_plus = null;
	private JButton button_minus = null;
	private JButton button_razy = null;
	private JButton button_dziel = null;
	private JLabel slowo_dzialanie = null;
	private JLabel znak_dzialania = null;
	private JLabel znak_rownasie = null;
	private JLabel port_label_1 = null;
	private JLabel port_label_2 = null;
	
	public NetCalculusClient() {

		setTitle("NetCalculus v1.0.1");
		setIconImage(new ImageIcon(getResourceURL("calculus/resources/ikonka.png")).getImage());
		setLayout(new AbsoluteLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(325, 220));
		setResizable(false);
		
		NetCalculusToolkit.setCenteredWindow(this);

		srv_dodawania_opis = new JLabel("Serwer [+ -]:", JLabel.RIGHT);
		srv_mnozenia_opis = new JLabel("Serwer [* /]:", JLabel.RIGHT);
		
		server_dodawania_tf = createTextField_ServerDodajacy();
		server_mnozenia_tf = createTextField_SerwerMnozacy();
		port_dodawania_tf = createTextField_PortDodawania();
		port_mnozenia_tf = createTextField_PortMnozenia();

		slowo_dzialanie = new JLabel("Dzia³anie:", JLabel.RIGHT);
		znak_dzialania = new JLabel("+", JLabel.CENTER);
		znak_rownasie = new JLabel("=", JLabel.RIGHT);

		port_label_1 = new JLabel("port:", JLabel.RIGHT);
		port_label_2 = new JLabel("port:", JLabel.RIGHT);
		
		liczba_a_tf = createTextField_LiczbaA();
		liczba_b_tf = createTextField_LiczbaB();
		liczba_wynik_tf = createTextField_LiczbaWynik();

		button_polacz = createButton_Polaczenie();
		
		button_plus = createButton_DzialaniePlus();
		button_minus = createButton_DzialanieMinus();
		button_razy = createButton_DzialanieMnozenie();
		button_dziel = createButton_DzialanieDzielenie();
		
		/* NOTE!
		 * Uk³adamy wszytkie komponenty tak zeby pasowaly. Konstruktory constraintsów 
		 * po³ozenia komponentu maja postac AbsoluteConstraints(x, y, width, height)
		 */

		add(new JSeparator(), new AbsoluteConstraints(0, 5, 330, 1));
		add(srv_dodawania_opis, new AbsoluteConstraints(5, 10, 65, 20));
		add(srv_mnozenia_opis, new AbsoluteConstraints(5, 35, 65, 20));
		add(server_dodawania_tf, new AbsoluteConstraints(75, 10, 160, 20));
		add(server_mnozenia_tf, new AbsoluteConstraints(75, 35, 160, 20));
		add(port_label_1, new AbsoluteConstraints(205, 10, 60, 20));
		add(port_label_2, new AbsoluteConstraints(205, 35, 60, 20));
		add(port_dodawania_tf, new AbsoluteConstraints(270, 10, 40, 20));
		add(port_mnozenia_tf, new AbsoluteConstraints(270, 35, 40, 20));
		add(new JSeparator(), new AbsoluteConstraints(0, 60, 330, 1));
		add(slowo_dzialanie, new AbsoluteConstraints(5, 65, 65, 20));
		add(liczba_a_tf, new AbsoluteConstraints(75, 65, 110, 20));
		add(znak_dzialania, new AbsoluteConstraints(185, 66, 17, 20));
		add(liczba_b_tf, new AbsoluteConstraints(200, 66, 110, 20));
		
		add(znak_rownasie, new AbsoluteConstraints(5, 90, 63, 20));
		add(liczba_wynik_tf, new AbsoluteConstraints(75, 90, 235, 20));

		add(new JSeparator(), new AbsoluteConstraints(0, 114, 330, 1));
		
		add(button_polacz, new AbsoluteConstraints(25, 127, 25, 25));
		
		add(button_plus, new AbsoluteConstraints(75, 120, 55, 40));
		add(button_minus, new AbsoluteConstraints(135, 120, 55, 40));
		add(button_razy, new AbsoluteConstraints(195, 120, 55, 40));
		add(button_dziel, new AbsoluteConstraints(255, 120, 55, 40));
		
		add(new JSeparator(), new AbsoluteConstraints(0, 165, 330, 1));
		
		final JFrame f = this;
		JMenu program = new JMenu("Program");
		program.add(
				new AbstractAction("O programie") {
					public void actionPerformed(ActionEvent e) {
						JOptionPane.showMessageDialog(
								f, // parent frame
								"Net Calculus v1.0.1 - program do obliczeñ równoleg³ych " +
								"na dwóch serwerach.", // msg
								"Informacja o programie", // title
								JOptionPane.INFORMATION_MESSAGE // message type
						);
					}
				}
		);
		
		JMenuBar menu = new JMenuBar();
		menu.add(program);
		
		setJMenuBar(menu);
		
		pack();
		setVisible(true);
	}

	/**
	 * Uruchomienie nawi¹zania po³¹czenia z serwerem. Jesli w tej metodzie zostanie
	 * wychwycony Exception to znaczy ze na danym serwerze na damym porcie nie znajduje
	 * sie serwer liczacy dla naszej aplikacji.<br>
	 */
	protected void initCommunication() {
		if(watek_dodawania == null && watek_mnozenia == null) {
			try {
				watek_dodawania = new ClientThread(serwer_dodawania, port_dodawania);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(
						null, 
						"Serwer " + serwer_dodawania + ":" + port_dodawania + 
						" nie odpowiada. Polaczenie odrzucone...",
						"Wykryto b³¹d",
						JOptionPane.ERROR_MESSAGE
				);
				watek_dodawania = null;
				return;
			}
			try {
				watek_mnozenia = new ClientThread(serwer_mnozenia, port_mnozenia);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(
						null, 
						"Serwer " + serwer_mnozenia + ":" + port_mnozenia + 
						" nie odpowiada. Polaczenie odrzucone...",
						"Wykryto b³¹d",
						JOptionPane.ERROR_MESSAGE
				);
				watek_mnozenia = null;
				return;
			}
			button_polacz.setIcon(new ImageIcon(getResourceURL("calculus/resources/connected.png")));
		}
	}
	
	protected void changeSkin(SubstanceSkin skin) {
		try {
			SubstanceLookAndFeel.setSkin(skin);
			UIManager.setLookAndFeel(new SubstanceLookAndFeel());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Zwraca URL do zasobu plikowego.<br>
	 * @param res - relatywna sciezka do zasobu
	 * @return URL
	 */
	protected URL getResourceURL(String res) {
		ClassLoader loader = getClass().getClassLoader();
		URL url = loader.getResource(res);
		return url;
	}
	
	/**
	 * Zapisuje liczby do zmiennych.<br>
	 */
	protected void updateNumbers() {
		double num = repairNumber(liczba_a_tf);
		if(num != Double.POSITIVE_INFINITY) {
			liczba_a = num;
		}
		num = repairNumber(liczba_b_tf);
		if(num != Double.POSITIVE_INFINITY) {
			liczba_b = num;
		}
	}
	
	/**
	 * Wyzwala okreslona komende Calculusa dla zadanego watku konteksu.<br>
	 * @param thread - watek kontekstu (dodawania/odejmowania lub mnozenia/dzielenia)
	 * @param command - jeden z obiektow enumeracji NetCalculusCommans
	 */
	public void gainCommand(ClientThread thread, NetCalculusCommands command) {
		updateNumbers();
		if(thread == null) {
			JOptionPane.showMessageDialog(
					null, 
					"Nie polaczono z serwerem...",
					"Brak po³¹czenia",
					JOptionPane.INFORMATION_MESSAGE
			);
			return;
		}
		thread.send(
				liczba_a, liczba_b,
				command.getCode()
		);
		double wynik = thread.receive();
		liczba_wynik_tf.setText(new Double(wynik).toString());
	}
	
	/**
	 * Uruchamianie.<br>
	 * @param args
	 */
	public static void main(String[] args) {

		welcom_frame = new JFrame();
		welcom_frame.setUndecorated(true);
		welcom_frame.setPreferredSize(new Dimension(350, 30));
		
		JLabel welcome_label = new JLabel("Proszê czekaæ... Trwa uruchamianie aplikacji.", JLabel.CENTER);
		welcome_label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		welcome_label.setFont(new Font("Verdana", Font.PLAIN, 12));

		NetCalculusToolkit.setCenteredWindow(welcom_frame);
		
		welcom_frame.add(welcome_label);
		welcom_frame.pack();
		welcom_frame.setVisible(true);
		
		try {
			
			/* NOTE!
			 * Linijka poni¿ej odpowiada za dekorowanie ramki JFrame w ktorej jest
			 * wyswietlana aplikacja. Czyli caly LAF (Look And Feel) windowsa zostaje 
			 * usuniety a na jego miejsce jako dekorator wchodzi Substance z ustawianymi 
			 * przez nas skinami. 
			 */
			
			JFrame.setDefaultLookAndFeelDecorated(true);
			JDialog.setDefaultLookAndFeelDecorated(true);
			
			/* NOTE!
			 * UIManager zajmuje sie trawersowaniem drzewa komponentow i zmiana LAF kazdego
			 * z komponentow ktory znajdzie po drodze (i do ktorego oczywiscie posiada 
			 * odpowiedni LAF).
			 */
			
			UIManager.setLookAndFeel(new SubstanceLookAndFeel());
			SubstanceLookAndFeel.setSkin(new BusinessBlackSteelSkin());
			
			/* NOTE!
			 * Powtorne przypisanie LAF powoduje ukazanie zmian w themesach, skinach i 
			 * watermarkach. 
			 */
			
			UIManager.setLookAndFeel(new SubstanceLookAndFeel());
			
			
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		EventQueue.invokeLater(
				new Runnable() {
					public void run() {
						new NetCalculusClient();
						welcom_frame.dispose();
					}
				}
		);
	}

	/* (non-Javadoc)
	 * Zamykanie okna aplikacji.<br>
	 * @see java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
	 */
	public void windowClosing(WindowEvent e) {
		watek_dodawania.stopThread();
		watek_mnozenia.stopThread();
	}

	/* NOTE!
	 * Dalej juz tylko metody obs³uguj¹ce i tworz¹ce GUI. Czyli nic ciekawego... aczkolwiek
	 * trzeba to bylo napisac.
	 */
	
	private JButton createButton_Polaczenie() {
		if(button_polacz == null) {
			button_polacz = new JButton();
			button_polacz.setFocusable(false);
			button_polacz.setAction(
					new AbstractAction("", new ImageIcon(getResourceURL("calculus/resources/unconnected.png"))) {
						private static final long serialVersionUID = 1L;
						public void actionPerformed(ActionEvent e) {
							port_dodawania = repairPortNumber(port_dodawania_tf);
							port_mnozenia =	repairPortNumber(port_mnozenia_tf);
							initCommunication();
						}
					}	
			);
		}
		return button_polacz;
	}
	
	private JButton createButton_DzialanieDzielenie() {
		if(button_dziel == null) {
			button_dziel = new JButton();
			button_dziel.setFocusable(false);
			button_dziel.setAction(
					new AbstractAction("", new ImageIcon(getResourceURL("calculus/resources/dziel.png"))) {
						private static final long serialVersionUID = 1L;
						public void actionPerformed(ActionEvent e) {
							znak_dzialania.setText("/");
							gainCommand(watek_mnozenia, NetCalculusCommands.DZIELENIE);
						}
					}
			);
		}
		return button_dziel;
	}

	private JButton createButton_DzialanieMnozenie() {
		if(button_razy == null) {
			button_razy = new JButton();
			button_razy.setFocusable(false);
			button_razy.setAction(
					new AbstractAction("", new ImageIcon(getResourceURL("calculus/resources/razy.png"))) {
						private static final long serialVersionUID = 1L;
						public void actionPerformed(ActionEvent e) {
							znak_dzialania.setText("*");
							gainCommand(watek_mnozenia, NetCalculusCommands.MNOZENIE);
						}
					}
			);
		}
		return button_razy;	
	}

	private JButton createButton_DzialanieMinus() {
		if(button_minus == null) {
			button_minus = new JButton();
			button_minus.setFocusable(false);
			button_minus.setAction(
					new AbstractAction("", new ImageIcon(getResourceURL("calculus/resources/minus.png"))) {
						private static final long serialVersionUID = 1L;
						public void actionPerformed(ActionEvent e) {
							znak_dzialania.setText("-");
							gainCommand(watek_dodawania, NetCalculusCommands.MINUS);
						}
					}
			);
		} 
		return button_minus;
	}

	private JButton createButton_DzialaniePlus() {
		if(button_plus == null) {
			button_plus = new JButton();
			button_plus.setFocusable(false);
			button_plus.setAction(
					new AbstractAction("", new ImageIcon(getResourceURL("calculus/resources/plus.png"))) {
						private static final long serialVersionUID = 1L;
						public void actionPerformed(ActionEvent e) {
							znak_dzialania.setText("+");
							gainCommand(watek_dodawania, NetCalculusCommands.PLUS);
						}
					}
			);
		}
		return button_plus;
	}
	
	private JTextField createTextField_LiczbaWynik() {
		if(liczba_wynik_tf == null) {
			liczba_wynik_tf = new JTextField();
			liczba_wynik_tf.setText(new Double(liczba_wynik).toString());
			liczba_wynik_tf.setEditable(false);
		}
		return liczba_wynik_tf;
	}
	
	private JTextField createTextField_LiczbaB() {
		if(liczba_b_tf == null) {
			liczba_b_tf = new JTextField();
			liczba_b_tf.setText(new Double(liczba_b).toString());
			liczba_b_tf.addKeyListener(
					new KeyListener() {
						public void keyTyped(KeyEvent e) {}
						public void keyReleased(KeyEvent e) {}
						public void keyPressed(KeyEvent e) {
							int c = e.getKeyCode(); 
							if(c == KeyEvent.VK_ENTER) {
								JTextField jtf = (JTextField) e.getSource();
								double num = repairNumber(jtf);
								if(num != Double.POSITIVE_INFINITY) {
									liczba_b = num;
								}
							}
						}
					}
			);
			liczba_b_tf.addFocusListener(
					new FocusListener() {
						public void focusGained(FocusEvent e) {}
						public void focusLost(FocusEvent e) {
							JTextField jtf = (JTextField) e.getSource();
							double num = repairNumber(jtf);
							if(num != Double.POSITIVE_INFINITY) {
								liczba_b = num;
							}
						}
					}
			);
		}
		return liczba_b_tf;
	}

	private JTextField createTextField_LiczbaA() {
		if(liczba_a_tf == null) {
			liczba_a_tf = new JTextField();
			liczba_a_tf.setText(new Double(liczba_a).toString());
			liczba_a_tf.addKeyListener(
					new KeyListener() {
						public void keyTyped(KeyEvent e) {}
						public void keyReleased(KeyEvent e) {}
						public void keyPressed(KeyEvent e) {
							int c = e.getKeyCode(); 
							if(c == KeyEvent.VK_ENTER) {
								JTextField jtf = (JTextField) e.getSource();
								double num = repairNumber(jtf);
								if(num != Double.POSITIVE_INFINITY) {
									liczba_a = num;
								}
							}
						}
					}
			);
			liczba_a_tf.addFocusListener(
					new FocusListener() {
						public void focusGained(FocusEvent e) {}
						public void focusLost(FocusEvent e) {
							JTextField jtf = (JTextField) e.getSource();
							double num = repairNumber(jtf);
							if(num != Double.POSITIVE_INFINITY) {
								liczba_a = num;
							}
						}
					}
			);
		}
		return liczba_a_tf;
	}

	private JTextField createTextField_PortMnozenia() {
		if(port_mnozenia_tf == null) {
			port_mnozenia_tf = new JTextField();
			port_mnozenia_tf.setText(new Integer(port_mnozenia).toString());
			port_mnozenia_tf.addKeyListener(
					new KeyListener() {
						public void keyTyped(KeyEvent e) {}
						public void keyReleased(KeyEvent e) {}
						public void keyPressed(KeyEvent e) {
							int c = e.getKeyCode(); 
							if(c == KeyEvent.VK_ENTER) {
								JTextField jtf = (JTextField) e.getSource();
								int num = repairPortNumber(jtf);
								if(num > -1) {
									port_mnozenia = num;
								}
							}
						}
					}
			);
			port_mnozenia_tf.addFocusListener(
					new FocusListener() {
						public void focusGained(FocusEvent e) {}
						public void focusLost(FocusEvent e) {
							JTextField jtf = (JTextField) e.getSource();
							int num = repairPortNumber(jtf);
							if(num > -1) {
								port_mnozenia = num;
							}
						}
					}
			);
		}
		return port_mnozenia_tf;
	}

	private JTextField createTextField_PortDodawania() {
		if(port_dodawania_tf == null) {
			port_dodawania_tf = new JTextField();
			port_dodawania_tf.setText(new Integer(port_dodawania).toString());
			port_dodawania_tf.addKeyListener(
					new KeyListener() {
						public void keyTyped(KeyEvent e) {}
						public void keyReleased(KeyEvent e) {}
						public void keyPressed(KeyEvent e) {
							int c = e.getKeyCode(); 
							if(c == KeyEvent.VK_ENTER) {
								JTextField jtf = (JTextField) e.getSource();
								int num = repairPortNumber(jtf);
								if(num > -1) {
									port_dodawania = num;
								}
							}
						}
					}
			);
			port_dodawania_tf.addFocusListener(
					new FocusListener() {
						public void focusGained(FocusEvent e) {}
						public void focusLost(FocusEvent e) {
							JTextField jtf = (JTextField) e.getSource();
							int num = repairPortNumber(jtf);
							if(num > -1) {
							}
								port_dodawania = num;
						}
					}
			);
		}
		return port_dodawania_tf;
	}

	private JTextField createTextField_SerwerMnozacy() {
		if(server_mnozenia_tf == null) { 
			server_mnozenia_tf = new JTextField();
			server_mnozenia_tf.setText(serwer_mnozenia);
			server_mnozenia_tf.addKeyListener(
					new KeyListener() {
						public void keyTyped(KeyEvent e) {}
						public void keyReleased(KeyEvent e) {}
						public void keyPressed(KeyEvent e) {
							int c = e.getKeyCode(); 
							if(c == KeyEvent.VK_ENTER) {
								JTextField jtf = (JTextField) e.getSource();
								repairServerAddr(jtf);
								serwer_mnozenia = jtf.getText();
							}
						}
					}
			);
			server_mnozenia_tf.addFocusListener(
					new FocusListener() {
						public void focusGained(FocusEvent e) {}
						public void focusLost(FocusEvent e) {
							JTextField jtf = (JTextField) e.getSource();
							repairServerAddr(jtf);
							serwer_mnozenia = jtf.getText();
						}
					}
			);
		}
		return server_mnozenia_tf;
	}

	private JTextField createTextField_ServerDodajacy() {
		if(server_dodawania_tf == null) {
			server_dodawania_tf = new JTextField();
			server_dodawania_tf.setText(serwer_dodawania);
			server_dodawania_tf.addKeyListener(
					new KeyListener() {
						public void keyTyped(KeyEvent e) {}
						public void keyReleased(KeyEvent e) {}
						public void keyPressed(KeyEvent e) {
							int c = e.getKeyCode(); 
							if(c == KeyEvent.VK_ENTER) {
								JTextField jtf = (JTextField) e.getSource();
								repairServerAddr(jtf);
								serwer_dodawania = jtf.getText();
							}
						}
					}
			);
			server_dodawania_tf.addFocusListener(
					new FocusListener() {
						public void focusGained(FocusEvent e) {}
						public void focusLost(FocusEvent e) {
							JTextField jtf = (JTextField) e.getSource();
							repairServerAddr(jtf);
							serwer_dodawania = jtf.getText();
						}
					}
			);
		}
		return server_dodawania_tf;
	}

	protected void repairServerAddr(JTextField jtf) {
		String adres = jtf.getText();
		if(adres.length() == 0) {
			jtf.setText("localhost");
		}
	}
	
	protected int repairPortNumber(JTextField jtf) {
		int port = -1;
		try {
			port = Integer.parseInt(jtf.getText());
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(
					null, 
					"Z³y format zapisu portu serwera.\n\nPoprawny format to " +
					"tylko liczby sk³adaj¹ce siê na numer z zakresu 0 .. 65535.\n\n" +
					"Proszê wprowadziæ poprawny numer, a nastêpnie wcisn¹æ Enter.",
					"Wykryto b³¹d",
					JOptionPane.ERROR_MESSAGE
			);
			//jtf.requestFocus();
		}
		return port;
	}

	protected double repairNumber(JTextField jtf) {
		double num = Double.POSITIVE_INFINITY;
		try {
			num = Double.parseDouble(jtf.getText());
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(
					null, 
					"Z³y format zapisu liczby.\n\nPoprawny format to " +
					"cyfry rozdzielone kropk¹, lub mantysa razy E+/-wykladnik.",
					"Wykryto b³¹d",
					JOptionPane.ERROR_MESSAGE
			);
		}
		return num;
	}
	
	public void windowOpened(WindowEvent e) {}
	public void windowClosed(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}
}
