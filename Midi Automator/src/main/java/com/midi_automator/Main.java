package com.midi_automator;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.midi_automator.model.IModel;
import com.midi_automator.model.Model;
import com.midi_automator.presenter.MidiAutomator;
import com.midi_automator.utils.SystemUtils;

public class Main {

	static Logger log = Logger.getLogger(Main.class.getName());

	private static String fileName = null;
	private static String wd = "";
	private static String os = "";
	private static boolean test = false;

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

				if (arg.contains(MidiAutomator.FILE_EXTENSION)) {
					fileName = arg;
					log.info("File in argument: " + fileName);
				}

				if (arg.contains("-test")) {
					log.info("Set \"-test\"");
					test = true;
				}
			}
		}
		config();
	}

	/**
	 * Configures the application dependencies
	 */
	private static void config() {

		ApplicationContext ctx = new ClassPathXmlApplicationContext("Beans.xml");
		Resources resources = (Resources) ctx.getBean("Resources",
				new Object[] { os, wd });
		IModel model = (Model) ctx.getBean("Model");

		if (fileName == null) {
			fileName = model.getPersistenceFileName();
		}
		ctx.getBean("Presenter", new Object[] { model, resources, fileName,
				test });
	}
}
