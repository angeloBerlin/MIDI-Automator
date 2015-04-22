package com.midi_automator;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

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

		String fileName = null;
		String wd = "";
		String os = "";
		boolean debug = false;
		boolean test = false;

		if (args.length > 0) {

			for (String arg : args) {

				if (arg.contains("-wd=")) {
					wd = replaceSystemVariables(arg.replace("-wd=", ""));
				}

				if (arg.contains("-os=")) {
					os = arg.replace("-os=", "");
				}

				if (arg.contains(IApplication.FILE_EXTENSION)) {
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

		new MidiAutomator(wd, os, fileName, debug, test);
	}

	/**
	 * Replaces all system variables in a String with their corresponding value.
	 * 
	 * @param str
	 *            The string with possible system variables
	 * @return the string with substituted variables
	 */
	public static String replaceSystemVariables(String str) {

		String pattern = "%[A-Za-z0-9_]+%";
		Pattern expr = Pattern.compile(pattern);
		Matcher matcher = expr.matcher(str);
		Map<String, String> env = System.getenv();

		while (matcher.find()) {

			String sysVar = matcher.group(0);
			String sysVarValue = env.get(sysVar.replace("%", ""));

			if (sysVarValue != null) {
				str = str.replace(sysVar, sysVarValue);
			}
		}

		return str;
	}
}
