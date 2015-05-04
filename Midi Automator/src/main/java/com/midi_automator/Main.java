package com.midi_automator;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.midi_automator.model.IModel;
import com.midi_automator.model.Model;
import com.midi_automator.presenter.MidiAutomator;
import com.midi_automator.utils.SystemUtils;

public class Main {

	private static String fileName = null;
	private static String wd = "";
	private static String os = "";
	private static boolean debug = false;
	private static boolean test = false;

	/**
	 * The main program
	 * 
	 * @param args
	 *            -debug puts debug information to the console, -dev puts the
	 *            application to development mode, -wd specifies the working
	 *            directory, -os specifies the current operating system
	 *            ["MacOS"|"Win"], no prefix specifies the .mido file to load
	 */
	public static void main(String[] args) {

		if (args.length > 0) {

			for (String arg : args) {

				if (arg.contains("-wd=")) {
					wd = SystemUtils.replaceSystemVariables(arg.replace("-wd=",
							""));
				}

				if (arg.contains("-os=")) {
					os = arg.replace("-os=", "");
				}

				if (arg.contains(MidiAutomator.FILE_EXTENSION)) {
					fileName = arg;
				}

				if (arg.contains("-debug")) {
					debug = true;
				}

				if (arg.contains("-test")) {
					test = true;
					debug = true;
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
				debug, test });
	}
}
