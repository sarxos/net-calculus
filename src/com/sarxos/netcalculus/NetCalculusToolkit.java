package com.sarxos.netcalculus;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;

/**
 * Narzedzia dodatkowe dla aplikacji.<br>
 */
public class NetCalculusToolkit {

	/**
	 * Wysrodkowywuje window na ekranie.<br>
	 * @param w
	 */
	public static void setCenteredWindow(Window w) {
		Toolkit t = Toolkit.getDefaultToolkit();
		Dimension size = t.getScreenSize();
		w.setLocation(
				new Point(
						size.width/2 - w.getPreferredSize().width/2,
						size.height/2 - w.getPreferredSize().height/2
				)
		);
	}
	
}
