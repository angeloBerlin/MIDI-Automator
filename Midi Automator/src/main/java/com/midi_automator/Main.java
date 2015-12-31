package com.midi_automator;

import java.util.Locale;

import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.midi_automator.presenter.MidiAutomator;
import com.midi_automator.utils.SystemUtils;

public class Main {

	static Logger log = Logger.getLogger(Main.class.getName());

	public static String wd = "";
	public static String os = "";
	public static boolean test = false;
	public static Locale locale = Locale.ENGLISH;

	/**
	 * The main program
	 * 
	 * @param args
	 *            -wd specifies the working directory, -os specifies the current
	 *            operating system ["MacOS"|"Win"], no prefix specifies the
	 *            .mido file to load
	 */
	public static void main(String[] args) {

		if (args.length > 0) {

			for (String arg : args) {

				if (arg.contains("-wd=")) {
					wd = SystemUtils.replaceSystemVariables(arg.replace("-wd=",
							""));
					log.info("Working Driectory (-wd) set to: " + wd);
				}

				if (arg.contains("-os=")) {
					os = arg.replace("-os=", "");
					log.info("Operating System (-os) set to: " + os);
				}

				if (arg.contains("-test")) {
					log.info("Set \"-test\"");
					test = true;
				}
			}
		}

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(
						AppConfig.class);
				MidiAutomator presenter = (MidiAutomator) ctx
						.getBean(MidiAutomator.class);

				presenter.openMainFrame();
			}
		});
	}
}
