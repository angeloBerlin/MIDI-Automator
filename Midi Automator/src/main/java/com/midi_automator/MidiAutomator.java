package com.midi_automator;

import java.util.Locale;

import javax.swing.SwingUtilities;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.midi_automator.presenter.Presenter;
import com.midi_automator.utils.SystemUtils;

public class MidiAutomator {

	public static String wd = ".";
	public static String os = "";
	public static boolean test = false;
	public static Locale locale = Locale.ENGLISH;

	/**
	 * The main program
	 * 
	 * @param args
	 *            -wd specifies the working directory
	 */
	public static void main(String[] args) {

		Locale.setDefault(locale);

		if (args.length > 0) {

			for (String arg : args) {

				if (arg.contains("-wd=")) {
					wd = SystemUtils.replaceSystemVariables(arg.replace("-wd=",
							""));
				}

				if (arg.contains("-test")) {
					test = true;
				}
			}
		}

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(
						AppConfig.class);
				Presenter presenter = (Presenter) ctx.getBean(Presenter.class);

				presenter.openMainFrame();
			}
		});
	}
}
